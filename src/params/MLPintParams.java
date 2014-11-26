package params;

import utils.imlp.dist.Distance;

public abstract class MLPintParams extends Params {
    
    private Distance distance;
    private Integer numNetsToTrain;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }
    
    public Integer getNumNetsToTrain() {
        return numNetsToTrain;
    }

    public void setNumNetsToTrain(Integer numNetsToTrain) {
        this.numNetsToTrain = numNetsToTrain;
    }
}
