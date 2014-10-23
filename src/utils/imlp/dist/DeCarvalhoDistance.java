package utils.imlp.dist;

import utils.ErrorMeasuresUtils;
import utils.imlp.Interval;

public class DeCarvalhoDistance extends IchinoYaguchiDistance {

    public DeCarvalhoDistance(double gamma) {
        super(gamma);
    }
    
    @Override
    public double getDistance(Interval forecast, Interval real) {
        return super.getDistance(forecast, real) / ErrorMeasuresUtils.widthUnion(forecast, real);
    }
    
    @Override
    public String toString() {
        return "DeCarvalho";
    }
}
