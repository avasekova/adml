package utils;

import java.io.Serializable;

public interface Improvable extends Serializable {
    
    boolean isBetterThanBest(double currentValue, double bestValue);
    
}
