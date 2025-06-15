package core.convoulter;

import org.bytedeco.opencv.opencv_core.Mat;

public class StreamRecord {
    private final Mat image;
    private final String path;
    private final Filter filter;

    public StreamRecord(Mat image, String path, Filter filter) {
        this.image = image;
        this.path = path;
        this.filter = filter;
    }

    public Mat getImage() {
        return this.image;
    }

    public String getPath() {
        return this.path;
    }

    public Filter getFilter() {
        return this.filter;
    }
}
