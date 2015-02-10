package models.params;

import utils.imlp.dist.Distance;

public class SESintParams extends PseudoIntervalParams {
    
    private SESParams paramsCenter;
    private SESParams paramsRadius;

    @Override
    public SESParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(SESParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    @Override
    public SESParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(SESParams paramsRadius) {
        this.paramsRadius = paramsRadius;
    }

    @Override
    public Params getClone() {
        SESintParams param = new SESintParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setSeasonality(this.getSeasonality());
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        param.setDistance(this.getDistance());
        
        return param;
    }
}
