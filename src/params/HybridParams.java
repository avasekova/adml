package params;

import utils.imlp.dist.Distance;

public class HybridParams extends Params {
    
    private Params paramsCenter;
    private Params paramsRadius;
    private Distance distance;
    private Integer numNetsToTrain;

    public Params getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(Params paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    public Params getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(Params paramsRadius) {
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
    public HybridParams getClone() {
        HybridParams param = new HybridParams();
        param.setDataRangeFrom(getDataRangeFrom());
        param.setDataRangeTo(getDataRangeTo());
        param.setNumForecasts(getNumForecasts());
        param.setPercentTrain(getPercentTrain());
        param.setDistance(distance);
        param.setNumNetsToTrain(numNetsToTrain);
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        
        return param;
    }
}
