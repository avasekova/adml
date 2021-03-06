package utils.imlp.dist;

import utils.imlp.Interval;

public class HausdorffDistance implements Distance {
    private static final long serialVersionUID = 1L;
    
    @Override
    public double getDistance(Interval forecast, Interval real) {
        return (Math.abs(forecast.getCentre() - real.getCentre()) + Math.abs(forecast.getRadius() - real.getRadius()));
    }
    
    @Override
    public String toString() {
        return "Hausdorff distance";
    }
}
