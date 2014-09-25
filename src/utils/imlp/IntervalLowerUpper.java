package utils.imlp;

public class IntervalLowerUpper extends Interval {
    
    private final double lowerBound;
    private final double upperBound;
    
    public IntervalLowerUpper(double lowerBound, double upperBound) {
        //make sure that lowerBound is smaller than upperBound
        if (lowerBound <= upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        } else { //switch
            this.lowerBound = upperBound;
            this.upperBound = lowerBound;
        }
    }

    @Override
    public double getLowerBound() {
        return lowerBound;
    }
    
    @Override
    public double getUpperBound() {
        return upperBound;
    }

    @Override
    public double getCentre() {
        return (upperBound + lowerBound)/2;
    }

    @Override
    public double getRadius() {
        return (upperBound - lowerBound)/2;
    }

}