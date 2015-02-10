package models.params;

import utils.imlp.dist.Distance;

public abstract class PseudoIntervalParams extends Params {
    
    private Distance distance;
    
    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }
    
    public abstract Params getParamsCenter();
    public abstract Params getParamsRadius();
    
    @Override
    public String toString() {
        return "Center:\n" + getParamsCenter() + "\n" + 
               "Radius:\n" + getParamsRadius() + "\n" + 
               "Distance: " + distance;
    }
}
