package core.convoulter;

import java.lang.reflect.Array;

public class Filter {
    private final int Height;
    private final int Width;
    private final int[][] Filter;
    private final double Factor;
    private final double Bias;

    public int[][] getFilter() {
        return Filter;
    }

    public int getHeight() {
        return Height;
    }

    public int getWidth() {
        return Width;
    }

    public double getFactor() {
        return Factor;
    }

    public double getBias() {
        return Bias;
    }

    public Filter(int[][] Filter, int Height, int Width, double Factor, double Bias) {
        this.Filter = Filter;
        this.Height = Height;
        this.Width = Width;
        this.Factor = Factor;
        this.Bias = Bias;
    }
}
