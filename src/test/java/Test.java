
import core.console.FilterType;
import core.console.ParallelType;
import core.convoulter.Convoluter;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.testng.*;
import org.bytedeco.opencv.opencv_core.Mat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static core.console.FilterType.*;
import static core.console.ParallelType.*;

public class Test {
    private final ParallelType[] PTypes = {null, ROWS, COLS, PIXEL, FRGM};
    private final FilterType[] FTypes = {BLUR, GBLUR, MBLUR, FEDGES, SHARP, EMBOSS, ID, SL, SR};

    @org.testng.annotations.Test
    public void testSimpleGradientId() throws Exception {
        Convoluter Conv = new Convoluter();
        Conv.setImage(opencv_imgcodecs.imread("src/test/resources/test_gradient.bmp", 0));
        Conv.convolution(FilterType.ID, null, 1);
        Mat actual = opencv_imgcodecs.imread("src/test/resources/test_gradient.bmp", 0);
        Assert.assertEquals(Conv.getImage().arrayHeight(), actual.arrayHeight());
        Assert.assertEquals(Conv.getImage().arrayWidth(), actual.arrayWidth());
        for (int x = 0; x < actual.arrayHeight(); x++) {
            for (int y = 0; y < actual.arrayWidth(); y++) {
                Assert.assertEquals(Conv.getImage().ptr(x, y).get(), actual.ptr(x, y).get());
            }
        }
    }

    @org.testng.annotations.Test
    public void testSimpleGradientChanges() throws Exception {
        Convoluter Conv = new Convoluter();
        Conv.setImage(opencv_imgcodecs.imread("src/test/resources/test_gradient.bmp", 0));
        Conv.convolution(BLUR, null, 1);
        Mat actual = opencv_imgcodecs.imread("src/test/resources/test_gradient.bmp", 0);
        Assert.assertEquals(Conv.getImage().arrayHeight(), actual.arrayHeight());
        Assert.assertEquals(Conv.getImage().arrayWidth(), actual.arrayWidth());
        boolean dif = false;
        for (int x = 0; x < actual.arrayHeight(); x++) {
            for (int y = 0; y < actual.arrayWidth(); y++) {
                if ((Conv.getImage().ptr(x, y).get() != actual.ptr(x, y).get())) {
                    dif = true;
                }
                ;
            }
        }
        Assert.assertTrue(dif);
    }

    @org.testng.annotations.Test
    public void testSimplePutinChanges() throws Exception {
        Convoluter Conv = new Convoluter();
        Conv.setImage(opencv_imgcodecs.imread("src/test/resources/putin_sharped.bmp", 0));
        Conv.convolution(BLUR, null, 1);
        Mat actual = opencv_imgcodecs.imread("src/test/resources/putin_sharped.bmp", 0);
        Assert.assertEquals(Conv.getImage().arrayHeight(), actual.arrayHeight());
        Assert.assertEquals(Conv.getImage().arrayWidth(), actual.arrayWidth());
        boolean dif = false;
        for (int x = 0; x < actual.arrayHeight(); x++) {
            for (int y = 0; y < actual.arrayWidth(); y++) {
                if ((Conv.getImage().ptr(x, y).get() != actual.ptr(x, y).get())) {
                    dif = true;
                }
                ;
            }
        }
        Assert.assertTrue(dif);
    }

    @org.testng.annotations.Test
    public void testSimplePutinSharpedId() throws Exception {
        Convoluter Conv = new Convoluter();
        Conv.setImage(opencv_imgcodecs.imread("src/test/resources/putin_sharped.bmp", 0));
        Conv.convolution(FilterType.ID, null, 1);
        Mat actual = opencv_imgcodecs.imread("src/test/resources/putin_sharped.bmp", 0);
        Assert.assertEquals(Conv.getImage().arrayHeight(), actual.arrayHeight());
        Assert.assertEquals(Conv.getImage().arrayWidth(), actual.arrayWidth());
        for (int x = 0; x < actual.arrayHeight(); x++) {
            for (int y = 0; y < actual.arrayWidth(); y++) {
                Assert.assertEquals(Conv.getImage().ptr(x, y).get(), actual.ptr(x, y).get());
            }
        }
    }

    @org.testng.annotations.Test
    public void testPutinShiftId() throws Exception {
        Convoluter Conv = new Convoluter();
        Conv.setImage(opencv_imgcodecs.imread("src/test/resources/putin_sharped.bmp", 0));
        Conv.convolution(FilterType.SL, null, 1);
        Conv.convolution(FilterType.SR, null, 1);
        Mat actual = opencv_imgcodecs.imread("src/test/resources/putin_sharped.bmp", 0);
        Assert.assertEquals(Conv.getImage().arrayHeight(), actual.arrayHeight());
        Assert.assertEquals(Conv.getImage().arrayWidth(), actual.arrayWidth());
        for (int x = 0; x < actual.arrayHeight(); x++) {
            for (int y = 0; y < actual.arrayWidth(); y++) {
                Assert.assertEquals(Conv.getImage().ptr(x, y).get(), actual.ptr(x, y).get());
            }
        }
    }

    @org.testng.annotations.Test
    public void testPutinShiftPixelId() throws Exception {
        Convoluter Conv = new Convoluter();
        Conv.setImage(opencv_imgcodecs.imread("src/test/resources/putin_sharped.bmp", 0));
        Conv.convolution(FilterType.SL, PIXEL, 16);
        Conv.convolution(FilterType.SR, PIXEL, 16);
        Mat actual = opencv_imgcodecs.imread("src/test/resources/putin_sharped.bmp", 0);
        Assert.assertEquals(Conv.getImage().arrayHeight(), actual.arrayHeight());
        Assert.assertEquals(Conv.getImage().arrayWidth(), actual.arrayWidth());
        for (int x = 0; x < actual.arrayHeight(); x++) {
            for (int y = 0; y < actual.arrayWidth(); y++) {
                Assert.assertEquals(Conv.getImage().ptr(x, y).get(), actual.ptr(x, y).get());
            }
        }
    }


    public void PutinParallelSeqEq(ParallelType Ptype, FilterType Ftype) {
        Convoluter Conv = new Convoluter();
        Conv.setImage(opencv_imgcodecs.imread("src/test/resources/putin_sharped.bmp", 0));
        Conv.convolution(Ftype, Ptype, 1);
        Mat actual = Conv.getImage();
        Conv.setImage(opencv_imgcodecs.imread("src/test/resources/putin_sharped.bmp", 0));
        Conv.convolution(Ftype, Ptype, 16);
        Assert.assertEquals(Conv.getImage().arrayHeight(), actual.arrayHeight());
        Assert.assertEquals(Conv.getImage().arrayWidth(), actual.arrayWidth());
        for (int x = 0; x < actual.arrayHeight(); x++) {
            for (int y = 0; y < actual.arrayWidth(); y++) {
                Assert.assertEquals(Conv.getImage().ptr(x, y).get(), actual.ptr(x, y).get());
            }
        }
    }

    // ecbe48ffe3376f9d8547861aa42f2ae6205f3f0f 742efa509021fb387ba748eea96f3da46da50eea
    public void PutinStreamSeqEq(String[] paths, FilterType Ftype) throws Exception {
        ArrayList<Mat> images_origin = new ArrayList<>();
        Convoluter Conv = new Convoluter();
        CompletableFuture<Void> initFuture = CompletableFuture.runAsync(Conv::init);
        CompletableFuture<Void> originFuture = CompletableFuture.runAsync(() -> {
            for (String path : paths) {
                images_origin.add(opencv_imgcodecs.imread(path, 0));
            }
        });
        originFuture.get();
        Conv.setImage(opencv_imgcodecs.imread(paths[0], 0));
        CompletableFuture<Void> actualFuture = CompletableFuture.runAsync(() -> {
            Conv.convolution(Ftype, null, 16); //convolve with original tools
        });
        Mat actual = Conv.getImage();
        actualFuture.get();
        CompletableFuture<Void> streamFuture = CompletableFuture.runAsync(() -> {
            Conv.stream(paths, Ftype); //convolve with stream 1-8 images
        });
        streamFuture.get();
        ArrayList<Mat> images_processed = new ArrayList<>();
        for (String path : paths) {
            images_processed.add(opencv_imgcodecs.imread(path, 0));
        }
        Thread.sleep(2000); //await 'till convolution will process, some kind of java kickstand here, we cannot close an inner future
        CompletableFuture<Void> returnFuture = CompletableFuture.runAsync(() -> {
            for (int i = 0; i < images_origin.size(); i++) {
                opencv_imgcodecs.imwrite(paths[i], images_origin.get(i)); //return original images
            }
        });
        returnFuture.get();
        for (Mat expected : images_processed) {
            Assert.assertEquals(expected.arrayHeight(), actual.arrayHeight());
            Assert.assertEquals(expected.arrayWidth(), actual.arrayWidth());
            for (int x = 0; x < actual.arrayHeight(); x++) {
                for (int y = 0; y < actual.arrayWidth(); y++) {
                    Assert.assertEquals(expected.ptr(x, y).get(), actual.ptr(x, y).get());
                }
            }
        }
        initFuture.cancel(true);
    }

    @org.testng.annotations.Test
    public void testPutinParallelSeqEq() {
        for (ParallelType pType : PTypes)
            for (FilterType fType : FTypes)
                PutinParallelSeqEq(pType, fType);
    }

    @org.testng.annotations.Test
    public void testPutinStreamSeqEq() {
        try {
            PutinStreamSeqEq(new String[]{
                    "src/test/resources/putin_stream_check_1.bmp",
                    "src/test/resources/putin_stream_check_2.bmp",
                    "src/test/resources/putin_stream_check_3.bmp",
                    "src/test/resources/putin_stream_check_4.bmp",
                    "src/test/resources/putin_stream_check_5.bmp",
                    "src/test/resources/putin_stream_check_6.bmp",
                    "src/test/resources/putin_stream_check_7.bmp",
                    "src/test/resources/putin_stream_check_8.bmp"
            }, BLUR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @org.testng.annotations.Test
    public void testPutinStreamSeqId() {
        try {
            PutinStreamSeqEq(new String[]{
                    "src/test/resources/putin_stream_check_1.bmp",
                    "src/test/resources/putin_stream_check_2.bmp",
                    "src/test/resources/putin_stream_check_3.bmp",
                    "src/test/resources/putin_stream_check_4.bmp",
                    "src/test/resources/putin_stream_check_5.bmp",
                    "src/test/resources/putin_stream_check_6.bmp",
                    "src/test/resources/putin_stream_check_7.bmp",
                    "src/test/resources/putin_stream_check_8.bmp"
            }, ID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}