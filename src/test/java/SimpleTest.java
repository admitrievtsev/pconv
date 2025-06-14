
import core.console.FilterType;
import core.convoulter.Convoluter;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.testng.*;
import org.testng.annotations.Test;
import org.bytedeco.opencv.opencv_core.Mat;

import static core.console.ParallelType.*;

public class SimpleTest {
    @Test
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

    @Test
    public void testSimplePutinSharpedId() throws Exception {
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

    @Test
    public void testPutinShiftId() throws Exception {
        Convoluter Conv = new Convoluter();
        Conv.setImage(opencv_imgcodecs.imread("src/test/resources/test_gradient.bmp", 0));
        Conv.convolution(FilterType.SL, null, 1);
        Conv.convolution(FilterType.SR, null, 1);
        Mat actual = opencv_imgcodecs.imread("src/test/resources/test_gradient.bmp", 0);
        Assert.assertEquals(Conv.getImage().arrayHeight(), actual.arrayHeight());
        Assert.assertEquals(Conv.getImage().arrayWidth(), actual.arrayWidth());
        for (int x = 0; x < actual.arrayHeight(); x++) {
            for (int y = 0; y < actual.arrayWidth(); y++) {
                Assert.assertEquals(Conv.getImage().ptr(x, y).get(), actual.ptr(x, y).get());
            }
        }
    }

    @Test
    public void testPutinShiftPixelId() throws Exception {
        Convoluter Conv = new Convoluter();
        Conv.setImage(opencv_imgcodecs.imread("src/test/resources/test_gradient.bmp", 0));
        Conv.convolution(FilterType.SL, PIXEL, 16);
        Conv.convolution(FilterType.SR, PIXEL, 16);
        Mat actual = opencv_imgcodecs.imread("src/test/resources/test_gradient.bmp", 0);
        Assert.assertEquals(Conv.getImage().arrayHeight(), actual.arrayHeight());
        Assert.assertEquals(Conv.getImage().arrayWidth(), actual.arrayWidth());
        for (int x = 0; x < actual.arrayHeight(); x++) {
            for (int y = 0; y < actual.arrayWidth(); y++) {
                Assert.assertEquals(Conv.getImage().ptr(x, y).get(), actual.ptr(x, y).get());
            }
        }
    }

    @Test
    public void testPutinParallelSeqPixelEq() throws Exception {
        Convoluter Conv = new Convoluter();
        Conv.setImage(opencv_imgcodecs.imread("src/test/resources/test_gradient.bmp", 0));
        Conv.convolution(FilterType.BLUR, null, 1);
        Mat actual = Conv.getImage();
        Conv.setImage(opencv_imgcodecs.imread("src/test/resources/test_gradient.bmp", 0));
        Conv.convolution(FilterType.BLUR, PIXEL, 16);
        Assert.assertEquals(Conv.getImage().arrayHeight(), actual.arrayHeight());
        Assert.assertEquals(Conv.getImage().arrayWidth(), actual.arrayWidth());
        for (int x = 0; x < actual.arrayHeight(); x++) {
            for (int y = 0; y < actual.arrayWidth(); y++) {
                Assert.assertEquals(Conv.getImage().ptr(x, y).get(), actual.ptr(x, y).get());
            }
        }
    }
}