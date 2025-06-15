package core.console;

import org.bytedeco.opencv.opencv_core.Mat;

public class Profiler {
    private final ProfilerType Type;
    private final Mat Image;
    private final String[] Paths;
    private final FilterType Filter;
    private final int ThreadsCount;
    private final ParallelType ParallelType;

    public Profiler(ProfilerType Type, String[] Paths) {
        this.Type = Type;
        this.Image = null;
        this.Paths = Paths;
        this.Filter = null;
        this.ParallelType = null;
        this.ThreadsCount = 1;
    }

    public Profiler(ProfilerType Type, FilterType Filter, ParallelType PType, int ThreadsCount) {
        this.Type = Type;
        this.Image = null;
        this.Paths = null;
        this.Filter = Filter;
        this.ParallelType = PType;
        this.ThreadsCount = ThreadsCount;
    }

    public Profiler(ProfilerType Type, Mat Image) {
        this.Type = Type;
        this.Image = Image;
        this.Paths = null;
        this.Filter = null;
        this.ParallelType = null;
        this.ThreadsCount = 1;
    }

    public Profiler(ProfilerType Type) {
        this.Type = Type;
        this.Image = null;
        this.Paths = null;
        this.Filter = null;
        this.ParallelType = null;
        this.ThreadsCount = 1;
    }

    public Mat getImage() {
        return Image;
    }

    public ProfilerType getType() {
        return Type;
    }

    public String[] getPaths() {
        return Paths;
    }

    public ParallelType getParallelType() {
        return ParallelType;
    }

    public int getThreadsCount() {
        return ThreadsCount;
    }

    public FilterType getFilter() {
        return Filter;
    }
}
