package core.convoulter;

public class Filters {
    public final Filter Id = new Filter(new int[][]{
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}}, 3, 3, 1.0, 0.0);
    public final Filter BlurFilter = new Filter(new int[][]{
            {0, 0, 1, 0, 0},
            {0, 1, 1, 1, 0},
            {1, 1, 1, 1, 1},
            {0, 1, 1, 1, 0},
            {0, 0, 1, 0, 0}}, 5, 5, 1.0 / 13.0, 0.1);

    public final Filter GBlurFilter = new Filter(new int[][]{
            {1, 4, 6, 4, 1},
            {4, 16, 24, 16, 4},
            {6, 24, 36, 24, 6},
            {4, 16, 24, 16, 4},
            {1, 4, 6, 4, 1}}, 5, 5, 1.0 / 256.0, 0.0);

    public final Filter MBlurFilter = new Filter(new int[][]{
            {1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1}
    }, 9, 9, 1.0 / 9.0, 0.0);

    public final Filter EdgesFilter = new Filter(new int[][]{
            {0, 0, -1, 0, 0},
            {0, 0, -1, 0, 0},
            {0, 0, 2, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
    }, 5, 5, 1.0, 0.0);

    public final Filter SharpenFilter = new Filter(new int[][]{
            {-1, -1, -1},
            {-1, 9, -1},
            {-1, -1, -1}
    }, 3, 3, 1.0, 0.0);

    public final Filter EmbossFilter = new Filter(new int[][]{
            {-1, -1, -1, -1, 0},
            {-1, -1, -1, 0, 1},
            {-1, -1, 0, 1, 1},
            {-1, 0, 1, 1, 1},
            {0, 1, 1, 1, 1}
    }, 5, 5, 1.0, 128.0);
}
