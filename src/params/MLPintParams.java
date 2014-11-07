package params;

import utils.imlp.dist.Distance;

public abstract class MLPintParams extends Params {
    
    private Distance distance;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }
}
