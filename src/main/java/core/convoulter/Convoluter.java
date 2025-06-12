package core.convoulter;

import core.console.FilterType;
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

    public void convolution(FilterType Filter) {
        switch (Filter) {
            case BLUR -> {
                MakeConvolution(Filters.BlurFilter);
            }
            case GBLUR -> {
                MakeConvolution(Filters.GBlurFilter);
            }
            case MBLUR -> {
                MakeConvolution(Filters.MBlurFilter);
            }
            case FEDGES -> {
                MakeConvolution(Filters.EdgesFilter);
            }
            case ID -> {
                MakeConvolution(Filters.Id);
            }
        }
    }

    private void MakeConvolution(Filter Filter) {
        int filterHeight = Filter.getHeight();
        int filterWidth = Filter.getWidth();
        int[][] filter = Filter.getFilter();
        double bias = Filter.getBias();
        double factor = Filter.getFactor();
        int w = Image.arrayWidth();
        int h = Image.arrayHeight();
        Mat result = Image.clone();
        for (int x = 0; x < w; x++) {

            System.out.println(x);
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
