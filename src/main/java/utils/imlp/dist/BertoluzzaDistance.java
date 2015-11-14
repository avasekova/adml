package utils.imlp.dist;

import utils.imlp.Interval;

public class BertoluzzaDistance extends WeightedEuclideanDistance {
    
    public BertoluzzaDistance() {
        super(1/3);
    }
    
    @Override
    public double getDistance(Interval forecast, Interval real) {
        return Math.sqrt(super.getDistance(forecast, real));
    }
    
    @Override
    public String toString() {
        return "Bertoluzza distance\n" +
               "beta = 1/3";
    }
}
