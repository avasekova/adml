package params;

import java.util.Locale;

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
        return "Basic statistics (" + varName + "): Mean: " + String.format(Locale.UK, "%.2f", mean) + "; Std. deviation: "
                + String.format(Locale.UK, "%.2f", stdDev) + "; Median: " + String.format(Locale.UK, "%.2f", median);
    }
}
