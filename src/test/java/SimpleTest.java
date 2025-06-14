
import core.console.FilterType;
import core.console.ParallelType;
import core.convoulter.Convoluter;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.testng.*;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.bytedeco.opencv.opencv_core.Mat;

import java.util.Arrays;

import static core.console.FilterType.*;
import static core.console.ParallelType.*;

public class SimpleTest {
    private final ParallelType[] PTypes = {null, ROWS, COLS, PIXEL};
    private final FilterType[] FTypes = {BLUR, GBLUR, MBLUR, FEDGES, SHARP, EMBOSS, ID, SL, SR};

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

    @Test
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

    @Test
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

    @Test
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


    public void PutinParallelSeqEq(ParallelType Ptype, FilterType Ftype) throws Exception {
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

    @Test
    public void testPutinParallelSeqEq() throws Exception {
        for (ParallelType pType : PTypes)
            for (FilterType fType : FTypes)
                PutinParallelSeqEq(pType, fType);
    }
}