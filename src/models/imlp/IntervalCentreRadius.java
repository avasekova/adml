package models.imlp;

import models.imlp.Interval;

public class IntervalCentreRadius extends Interval {
    
    private double centre;
    private double radius;

    @Override
    public double getCentre() {
        return centre;
    }

    public void setCentre(double centre) {
        this.centre = centre;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public double getUpperBound() {
        return centre + radius;
    }

    @Override
    public double getLowerBound() {
        return centre - radius;
    }
    
    
}
