package core.convoulter;

import core.console.FilterType;
import core.console.ParallelType;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Math.*;

public class Convoluter {
    private Mat Image;
    private final int effectiveThreads = 4; //amount of effective threads on your machine/threads you want to run stream with
    private final BlockingQueue<StreamRecord> streamingQueue = new LinkedBlockingQueue<>(); //queue of loaded and ready-to-go images
    private final AtomicInteger currentlyRunning = new AtomicInteger(0); //amount of currently running tasks
    private final AtomicInteger currentlyProcessed = new AtomicInteger(0); //amount of completely processing tasks

    private final ReentrantLock setLock = new ReentrantLock(); //Set of running tasks lock
    Set<String> runningTasks = HashSet.newHashSet(effectiveThreads);
    // Set of currently processing images. We don't start new processing of the same image
    // if it's processing right now

    Filters Filters = new Filters();

    public void setImage(Mat Image) {
        this.Image = Image;
    }

    public Mat getImage() {
        return this.Image;
    }

    public void init() {
        asyncStreamConvolution();
    }

    private Filter decideFilter(FilterType filterType) {
        return switch (filterType) {
            case BLUR -> Filters.BlurFilter;
            case GBLUR -> Filters.GBlurFilter;
            case MBLUR -> Filters.MBlurFilter;
            case FEDGES -> Filters.EdgesFilter;
            case EMBOSS -> Filters.EmbossFilter;
            case SHARP -> Filters.SharpenFilter;
            case ID -> Filters.Id;
            case SL -> Filters.ShiftLeft;
            case SR -> Filters.ShiftRight;
            case null -> null;
        };
    }

    public void convolution(FilterType filterType, ParallelType parallelType, int threadsCount) {
        Filter Filter = decideFilter(filterType);
        if (Filter == null) return;
        int filterHeight = Filter.getHeight();
        int filterWidth = Filter.getWidth();
        int[][] filter = Filter.getFilter();
        double bias = Filter.getBias();
        double factor = Filter.getFactor();
        int w = Image.arrayWidth();
        int h = Image.arrayHeight();
        switch (parallelType) {
            case null:
                MakeSimpleConvolution(filterHeight, filterWidth, filter, bias, factor, w, h);
                break;
            case FRGM:
                MakeFragmentConvolution(filterHeight, filterWidth, filter, bias, factor, w, h, threadsCount);
                break;
            case ROWS:
                MakeRowsConvolution(filterHeight, filterWidth, filter, bias, factor, w, h, threadsCount);
                break;
            case COLS:
                MakeColsConvolution(filterHeight, filterWidth, filter, bias, factor, w, h, threadsCount);
                break;
            case PIXEL:
                MakePixelConvolution(filterHeight, filterWidth, filter, bias, factor, w, h, threadsCount);
                break;
        }
    }

    private void convoult(Mat original, BytePointer result, int y, int x, int w, int h, double factor, double bias, int filterHeight, int filterWidth, int[][] filter) {
        double color = 0.0;
        for (int filterY = 0; filterY < filterHeight; filterY++)
            for (int filterX = 0; filterX < filterWidth; filterX++) {
                int imageX = (x - filterWidth / 2 + filterX + w) % w;
                int imageY = (y - filterHeight / 2 + filterY + h) % h;
                int t = (original.ptr(imageY, imageX).get());

                if (t < 0) {
                    t = t + 256;
                }
                color += (t) * filter[filterX][filterY];
            }

        int res_byte = (int) max(min((factor * color + bias), 255), 0);
        if (res_byte > 127) {
            res_byte = res_byte - 256;
        }
        ;
        result.put((byte) (res_byte));
    }

    public void stream(String[] paths, FilterType filterType) {
        long timeOut = 1000; //1s constant
        for (String path : paths) {
            Mat readedImage = opencv_imgcodecs.imread(path, 0);
            if (!readedImage.empty()) {
                try {
                    if (currentlyRunning.get() < effectiveThreads) {
                        streamingQueue.put(new StreamRecord(readedImage, path, decideFilter(filterType)));
                    } else {
                        int awaitK = toIntExact((int) sqrt(currentlyRunning.get()));
                        Thread.sleep(timeOut * awaitK); //waiting before new reading and passing value to convoulter if execution queue filled enough
                        streamingQueue.put(new StreamRecord(readedImage, path, decideFilter(filterType)));
                    }
                } catch (InterruptedException ex) {
                    System.out.println("Thread interrupted while reading value");
                }
            } else {
                System.out.println("Could not receive transmission from file " + path);
            }
        }
    }

    private void asyncStreamConvolution() {
        while (true) {
            try {
                StreamRecord imageMeta;
                if (currentlyProcessed.get() == 0) { //no tasks processed
                    setLock.lock();
                    {
                        imageMeta = streamingQueue.take();
                        if (runningTasks.contains(imageMeta.getPath())) {
                            streamingQueue.add(imageMeta); //push image back to queue, still processing
                            setLock.unlock();
                            continue;
                        } else {
                            runningTasks.add(imageMeta.getPath()); //ready-to-go
                        }
                    }
                    setLock.unlock();
                } else {
                    continue;
                }
                Mat image = imageMeta.getImage();
                String path = imageMeta.getPath();
                Filter filter = imageMeta.getFilter();
                CompletableFuture.runAsync(() ->
                        MakeStreamedConvolution(path, image, filter.getHeight(), filter.getWidth(), filter.getFilter(), filter.getBias(), filter.getFactor(), image.arrayWidth(), image.arrayHeight(), currentlyRunning.get())); //process image
            } catch (InterruptedException ex) {
                System.out.println("Thread interrupted while waiting value");
            }
        }
    }

    private void MakeSimpleConvolution(int filterHeight, int filterWidth, int[][] filter, double bias, double factor, int w, int h) {
        Mat result = Image.clone();

        // src/main/resources/test.bmp
        // /convolution blur 16 2

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                convoult(Image, result.ptr(y, x), y, x, w, h, factor, bias, filterHeight, filterWidth, filter);
            }
        }
        setImage(result);
    }


    private void MakePixelConvolution(int filterHeight, int filterWidth, int[][] filter, double bias, double factor, int w, int h, int ThreadsCount) {
        ExecutorService threadPool = Executors.newFixedThreadPool(ThreadsCount);
        Mat result = Image.clone();

        // /load src/main/resources/test.bmp
        // /convolution blur 2 16

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int finalY = y;
                int finalX = x;
                threadPool.execute(() -> convoult(Image, result.ptr(finalY, finalX), finalY, finalX, w, h, factor, bias, filterHeight, filterWidth, filter));
            }
        }
        threadPool.close();
        setImage(result);
    }

    private void MakeRowsConvolution(int filterHeight, int filterWidth, int[][] filter, double bias, double factor, int w, int h, int ThreadsCount) {
        ExecutorService threadPool = Executors.newFixedThreadPool(ThreadsCount);
        Mat result = Image.clone();

        // /load src/main/resources/test.bmp
        // /convolution blur 3 16

        for (int y = 0; y < h; y++) {
            int finalY = y;
            threadPool.execute(() -> {
                for (int x = 0; x < w; x++) {
                    convoult(Image, result.ptr(finalY, x), finalY, x, w, h, factor, bias, filterHeight, filterWidth, filter);
                }
            });
        }
        threadPool.close();
        setImage(result);
    }

    private void MakeColsConvolution(int filterHeight, int filterWidth, int[][] filter, double bias, double factor, int w, int h, int ThreadsCount) {
        ExecutorService threadPool = Executors.newFixedThreadPool(ThreadsCount);
        Mat result = Image.clone();

        // /load src/main/resources/test_1.bmp
        // /convolution blur 4 16

        for (int x = 0; x < w; x++) {
            int finalX = x;
            threadPool.execute(() -> {
                for (int y = 0; y < h; y++) {
                    convoult(Image, result.ptr(y, finalX), y, finalX, w, h, factor, bias, filterHeight, filterWidth, filter);
                }
            });
        }
        threadPool.close();
        setImage(result);
    }

    private void MakeFragmentConvolution(int filterHeight, int filterWidth, int[][] filter, double bias, double factor, int w, int h, int ThreadsCount) {
        ExecutorService threadPool = Executors.newFixedThreadPool(ThreadsCount);
        Mat result = Image.clone();

        // /load src/main/resources/test_1.bmp
        // /convolution blur 5 16

        for (int x = 0; x * ThreadsCount < w; x++) {
            for (int y = 0; y * ThreadsCount < h; y++) {
                int finalX = x;
                int finalY = y;
                threadPool.execute(() -> {
                    for (int y_t = finalY * ThreadsCount; y_t < min(h, (finalY + 1) * ThreadsCount); y_t++) {
                        for (int x_t = finalX * ThreadsCount; x_t < min(w, (finalX + 1) * ThreadsCount); x_t++) {
                            convoult(Image, result.ptr(y_t, x_t), y_t, x_t, w, h, factor, bias, filterHeight, filterWidth, filter);
                        }
                    }
                });
            }
        }
        threadPool.close();
        setImage(result);
    }

    private void MakeStreamedConvolution(String path, Mat image, int filterHeight, int filterWidth, int[][] filter, double bias, double factor, int w, int h, int balancingParameter) {
        int threadsCount = (int) Math.min(Math.max(1, Math.pow(2, (sqrt(effectiveThreads) - balancingParameter))), effectiveThreads); //balancing factor for amount of threads given on single image processing
        ExecutorService threadPool = Executors.newFixedThreadPool(threadsCount);
        Mat result = image.clone();
        currentlyRunning.addAndGet(1);

        // /load src/main/resources/test_1.bmp
        // /stream blur src/main/resources/test_1.bmp src/main/resources/test_2.bmp src/main/resources/test_3.bmp src/main/resources/test_4.bmp src/main/resources/test_5.bmp src/main/resources/test_6.bmp src/main/resources/test_7.bmp src/main/resources/test_8.bmp src/main/resources/test_9.bmp src/main/resources/test_10.bmp
        // /stream blur src/main/resources/test_11.bmp src/main/resources/test_11.bmp src/main/resources/test_11.bmp src/main/resources/test_11.bmp src/main/resources/test_11.bmp src/main/resources/test_11.bmp src/main/resources/test_11.bmp src/main/resources/test_11.bmp src/main/resources/test_11.bmp src/main/resources/test_11.bmp

        for (int y = 0; y < h; y++) {
            int finalY = y;
            threadPool.execute(() -> {
                for (int x = 0; x < w; x++) {
                    convoult(image, result.ptr(finalY, x), finalY, x, w, h, factor, bias, filterHeight, filterWidth, filter);
                }
            });
        }
        threadPool.close();

        //image saving, has highest priority and has no balancing factor
        if (result != null) {
            if (!opencv_imgcodecs.imwrite(path, result)) {
                System.out.println("Failed to save streamed\n" + path + " file");
            } else {
                System.out.print("\nStreamed in the background file " + path + " successfully processed and saved\n> ");
            }
        }
        currentlyProcessed.addAndGet(1);
        setLock.lock();
        {
            runningTasks.remove(path);
        }
        setLock.unlock();
        currentlyProcessed.addAndGet(-1);
        currentlyRunning.addAndGet(-1);
    }
}
