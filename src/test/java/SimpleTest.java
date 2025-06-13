
import core.console.FilterType;
import core.convoulter.Convoluter;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.testng.*;
import org.testng.annotations.Test;
import org.bytedeco.opencv.opencv_core.Mat;

public class SimpleTest {
    @Test
    public void testSimpleGradientId() {
        Convoluter Conv = new Convoluter();
        Conv.setImage(opencv_imgcodecs.imread("src/test/resources/test_gradient.bmp", 0));
        Conv.convolution(FilterType.ID);
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
    public void testSimplePutinSharpedId() {
        Convoluter Conv = new Convoluter();
        Conv.setImage(opencv_imgcodecs.imread("src/test/resources/test_gradient.bmp", 0));
        Conv.convolution(FilterType.ID);
        Mat actual = opencv_imgcodecs.imread("src/test/resources/test_gradient.bmp", 0);
        Assert.assertEquals(Conv.getImage().arrayHeight(), actual.arrayHeight());
        Assert.assertEquals(Conv.getImage().arrayWidth(), actual.arrayWidth());
        for (int x = 0; x < actual.arrayHeight(); x++) {
            for (int y = 0; y < actual.arrayWidth(); y++) {
                Assert.assertEquals(Conv.getImage().ptr(x, y).get(), actual.ptr(x, y).get());
            }
        }
    }
}