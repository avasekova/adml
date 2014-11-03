package params;

import utils.imlp.dist.Distance;

public class RandomWalkIntervalParams extends Params {

    private Distance distance;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }
    
    @Override
    public Params getClone() {
        throw new UnsupportedOperationException("Not supported for random walk.");
    }
}
