package models.imlp;

public class WeightedEuclideanDistance implements Distance {
    
    private final double beta;
    
    public WeightedEuclideanDistance(double beta) {
        this.beta = beta;
    }

    @Override
    public double getDistance(Interval a, Interval b) {
        return (beta *(Math.pow(a.getCentre() - b.getCentre(), 2))) +
            ((1-beta)*(Math.pow(a.getRadius() - b.getRadius(), 2)));
    }
    
}
