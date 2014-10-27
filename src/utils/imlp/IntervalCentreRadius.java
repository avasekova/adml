package utils.imlp;

public class IntervalCentreRadius extends Interval {

    private final double centre;
    private final double radius;
    
    public IntervalCentreRadius(double centre, double radius) {
        this.centre = centre;
        this.radius = radius;
    }

    @Override
    public double getCentre() {
        return centre;
    }

    @Override
    public double getRadius() {
        return radius;
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

