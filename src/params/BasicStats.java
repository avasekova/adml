package params;

import java.util.Locale;
import utils.Utils;

public class BasicStats {
    
    private final String varName;
    private double mean;
    private double stdDev;
    private double median;

    public BasicStats(String varName) {
        this.varName = varName;
    }
    
    public String getVarName() {
        return varName;
    }
            
    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getStdDev() {
        return stdDev;
    }

    public void setStdDev(double stdDev) {
        this.stdDev = stdDev;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }
    
    @Override
    public String toString() {
        return "Basic statistics (" + varName + "):\n   Mean: " + Utils.valToDecPoints(mean)
                + "\n   Std. deviation: " + Utils.valToDecPoints(stdDev) 
                + "\n   Median: " + Utils.valToDecPoints(median);
    }
}
