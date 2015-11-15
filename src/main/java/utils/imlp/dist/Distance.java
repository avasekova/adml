package utils.imlp.dist;

import utils.imlp.Interval;

import java.io.Serializable;

public interface Distance extends Serializable {
    
    double getDistance(Interval forecast, Interval real);
    @Override String toString();
    
}
