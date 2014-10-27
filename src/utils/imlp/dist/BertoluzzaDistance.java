package utils.imlp.dist;

import utils.imlp.Interval;

public class BertoluzzaDistance extends WeightedEuclideanDistance {
    
    public BertoluzzaDistance(double beta) {
        super(beta);
    }
    
    @Override
    public double getDistance(Interval forecast, Interval real) {
        return Math.sqrt(super.getDistance(forecast, real));
    }
    
    @Override
    public String toString() {
        return "Bertoluzza (" + getBeta() + ")";
    }
}
