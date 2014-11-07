package params;

import utils.imlp.dist.Distance;

public abstract class MLPintParams extends Params {
    
    private Distance distanceFunction;

    public Distance getDistanceFunction() {
        return distanceFunction;
    }

    public void setDistanceFunction(Distance distanceFunction) {
        this.distanceFunction = distanceFunction;
    }
}
