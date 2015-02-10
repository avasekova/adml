package models.params;

import utils.imlp.dist.Distance;

public class HybridParams extends PseudoIntervalParams {
    
    private Params paramsCenter;
    private Params paramsRadius;
    private Integer numNetsToTrain;

    @Override
    public Params getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(Params paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    @Override
    public Params getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(Params paramsRadius) {
        this.paramsRadius = paramsRadius;
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
        param.setSeasonality(this.getSeasonality());
        param.setDistance(this.getDistance());
        param.setNumNetsToTrain(numNetsToTrain);
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        
        return param;
    }
}
