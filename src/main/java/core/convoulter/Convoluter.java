package core.convoulter;

import core.console.FilterType;
import core.console.ParallelType;
import org.bytedeco.opencv.opencv_core.Mat;

import static java.lang.Math.*;

public class Convoluter {
    private Mat Image;
    Filters Filters = new Filters();

    public void setImage(Mat Image) {
        this.Image = Image;
    }

    public Mat getImage() {
        return this.Image;
    }

    public void convolution(FilterType FilterType, ParallelType parallelType, int threadsCount) {
        Filter Filter;
        switch (FilterType) {
            case BLUR -> {
                Filter = Filters.BlurFilter;
            }
            case GBLUR -> {
                Filter = Filters.GBlurFilter;
            }
            case MBLUR -> {
                Filter = Filters.MBlurFilter;
            }
            case FEDGES -> {
                Filter = Filters.EdgesFilter;
            }
            case EMBOSS -> {
                Filter = Filters.EmbossFilter;
            }
            case SHARP -> {
                Filter = Filters.SharpenFilter;
            }
            case ID -> {
                Filter = Filters.Id;
            }
            case null -> {
                return;
            }
        }
        switch (parallelType) {
            case null:
                MakeSimpleConvolution(Filter);
            case COLS:
                MakeSimpleConvolution(Filter);
            case ROWS:
                MakeRowsConvolution(Filter, threadsCount);
            default:
                MakeSimpleConvolution(Filter);
        }
    }

    private void MakeSimpleConvolution(Filter Filter) {
        int filterHeight = Filter.getHeight();
        int filterWidth = Filter.getWidth();
        int[][] filter = Filter.getFilter();
        double bias = Filter.getBias();
        double factor = Filter.getFactor();
        int w = Image.arrayWidth();
        int h = Image.arrayHeight();
        Mat result = Image.clone();
        //src/main/resources/test.bmp
        // /convolution blur parallel 16 2
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                double color = 0.0;
                for (int filterY = 0; filterY < filterHeight; filterY++)
                    for (int filterX = 0; filterX < filterWidth; filterX++) {
                        int imageX = (x - filterWidth / 2 + filterX + w) % w;
                        int imageY = (y - filterHeight / 2 + filterY + h) % h;
                        int t = (Image.ptr(imageY, imageX).get());

                        if (t < 0) {
                            t = t + 256;
                        }
                        color += (t) * filter[filterX][filterY];
                    }

                int res_byte = (int) max(min((factor * color + bias), 255), 0);
                if (res_byte > 127) {
                    res_byte = res_byte - 256;
                }
                result.ptr(y, x).put((byte) (res_byte));
            }
        }
        Image = result;
    }

    private void MakeRowsConvolution(Filter Filter, int ThreadsCount) {
        int filterHeight = Filter.getHeight();
        int filterWidth = Filter.getWidth();
        int[][] filter = Filter.getFilter();
        double bias = Filter.getBias();
        double factor = Filter.getFactor();
        int w = Image.arrayWidth();
        int h = Image.arrayHeight();
        Mat result = Image.clone();
        //src/main/resources/test.bmp
        // /convolution blur parallel 16 2
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                double color = 0.0;
                for (int filterY = 0; filterY < filterHeight; filterY++)
                    for (int filterX = 0; filterX < filterWidth; filterX++) {
                        int imageX = (x - filterWidth / 2 + filterX + w) % w;
                        int imageY = (y - filterHeight / 2 + filterY + h) % h;
                        int t = (Image.ptr(imageY, imageX).get());

                        if (t < 0) {
                            t = t + 256;
                        }
                        color += (t) * filter[filterX][filterY];
                    }

                int res_byte = (int) max(min((factor * color + bias), 255), 0);
                if (res_byte > 127) {
                    res_byte = res_byte - 256;
                }
                result.ptr(y, x).put((byte) (res_byte));
            }
        }
        Image = result;
    }
}
