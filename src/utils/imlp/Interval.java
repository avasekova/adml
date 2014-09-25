package utils.imlp;

public abstract class Interval {
    
    public abstract double getUpperBound();
    public abstract double getLowerBound();
    public abstract double getCentre();
    public abstract double getRadius();
    
    @Override
    public String toString() {
        return "<" + getCentre() + ", " + getRadius() + ">";
    }
    
}
