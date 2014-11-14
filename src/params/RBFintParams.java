package params;

import utils.imlp.dist.Distance;

public class RBFintParams extends Params {
    
    private RBFParams paramsCenter;
    private RBFParams paramsRadius;
    private Distance distance;
    private Integer numNetsToTrain;

    public RBFParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(RBFParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    public RBFParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(RBFParams paramsRadius) {
        this.paramsRadius = paramsRadius;
    }

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

    @Override
    public Params getClone() {
        RBFintParams param = new RBFintParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setSeasonality(this.getSeasonality());
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        param.setDistance(distance);
        param.setNumNetsToTrain(numNetsToTrain);
        param.setPercentTrain(this.getPercentTrain());
        
        return param;
    }

    @Override
    public String toString() {
        return "RBFintParams{" + "paramsCenter=" + paramsCenter + ", paramsRadius=" + paramsRadius + "}\n";
    }
}
