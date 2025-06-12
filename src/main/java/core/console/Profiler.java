package core.console;

import org.bytedeco.opencv.opencv_core.Mat;

public class Profiler {
    private final ProfilerType Type;
    private final Mat Image;
    private final String Path;
    private final FilterType Filter;

    public Profiler(ProfilerType Type, String Path) {
        this.Type = Type;
        this.Image = null;
        this.Path = Path;
        this.Filter = null;
    }

    public Profiler(ProfilerType Type, FilterType Filter) {
        this.Type = Type;
        this.Image = null;
        this.Path = null;
        this.Filter = Filter;
    }

    public Profiler(ProfilerType Type, Mat Image) {
        this.Type = Type;
        this.Image = Image;
        this.Path = null;
        this.Filter = null;
    }

    public Profiler(ProfilerType Type) {
        this.Type = Type;
        this.Image = null;
        this.Path = null;
        this.Filter = null;
    }

    public Profiler() {
        this.Type = null;
        this.Image = null;
        this.Path = null;
        this.Filter = null;
    }

    public Mat getImage() {
        return Image;
    }

    public ProfilerType getType() {
        return Type;
    }

    public String getPath() {
        return Path;
    }

    public FilterType getFilter() {
        return Filter;
    }
}
