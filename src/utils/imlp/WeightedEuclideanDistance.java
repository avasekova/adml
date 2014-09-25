package utils.imlp;

public class WeightedEuclideanDistance implements Distance {
    
    private final double beta;

    public WeightedEuclideanDistance(double beta) {
        this.beta = beta;
    }

   @Override
    public double getDistance(Interval forecast, Interval real) {
        return (beta *(Math.pow(forecast.getCentre() - real.getCentre(), 2))) +
            ((1-beta)*(Math.pow(forecast.getRadius() - real.getRadius(), 2)));
    }

}
