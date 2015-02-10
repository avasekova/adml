package models.params;

import utils.imlp.dist.Distance;

public class SESintParams extends Params {
    
    private SESParams paramsCenter;
    private SESParams paramsRadius;
    private Distance distance;

    public SESParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(SESParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    public SESParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(SESParams paramsRadius) {
        this.paramsRadius = paramsRadius;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
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
        param.setDistance(distance);
        
        return param;
    }

    @Override
    public String toString() {
        return "SESintParams{" + "paramsCenter=" + paramsCenter + ", paramsRadius=" + paramsRadius + ", distance=" + distance + "}\n";
    }
    
}
