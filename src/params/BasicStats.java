package params;

public class BasicStats {
    
    private double mean;
    private double stdDev;
    private double median;

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
        return "Basic statistics: Mean: " + String.format("%.2f", mean) + "; Std. deviation: " + String.format("%.2f", stdDev)
                + "; Median: " + String.format("%.2f", median);
    }
}
