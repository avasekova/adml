package utils;

import java.io.Serializable;

public class MinMaxTuple implements Serializable {

    private static final long serialVersionUID = 0L;
    
    private final double min;
    private final double max;
    
    public MinMaxTuple(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "(min=" + min + ", max=" + max + ')';
    }
}
