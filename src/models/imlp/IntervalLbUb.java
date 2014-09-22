package models.imlp;

import models.imlp.Interval;

public class IntervalLbUb extends Interval {
    
    //making sure that lowerBound is always smaller than upperBound
    private double lowerBound;
    private double upperBound;

    @Override
    public double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(double lowerBound) {
        if (lowerBound <= this.upperBound) {
            this.lowerBound = lowerBound;
        } else { //switch
            this.lowerBound = this.upperBound;
            this.upperBound = lowerBound;
        }
    }

    @Override
    public double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(double upperBound) {
        if (upperBound >= this.lowerBound) {
            this.upperBound = upperBound;
        } else { //switch
            this.upperBound = this.lowerBound;
            this.lowerBound = upperBound;
        }
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
