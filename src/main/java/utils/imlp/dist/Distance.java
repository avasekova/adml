package utils.imlp.dist;

import utils.imlp.Interval;

public interface Distance {
    
    double getDistance(Interval forecast, Interval real);
    @Override String toString();
    
}
