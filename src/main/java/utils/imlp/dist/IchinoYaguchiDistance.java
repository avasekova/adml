package utils.imlp.dist;

import utils.ErrorMeasuresUtils;
import utils.imlp.Interval;

public class IchinoYaguchiDistance implements Distance {
    
    private final double gamma;

    public IchinoYaguchiDistance(double gamma) {
        this.gamma = gamma;
    }

    @Override
    public double getDistance(Interval forecast, Interval real) {
        return ErrorMeasuresUtils.widthUnion(forecast, real) - ErrorMeasuresUtils.widthIntersection(forecast, real)
                + gamma * (2*ErrorMeasuresUtils.widthIntersection(forecast, real)
                           - ErrorMeasuresUtils.width(forecast) - ErrorMeasuresUtils.width(real));
    }
    
    public double getGamma() {
        return gamma;
    }
    
    @Override
    public String toString() {
        return "Ichino-Yaguchi distance\n" + 
               "gamma = " + gamma;
    }
}
