package params;

import utils.imlp.dist.Distance;

public class HoltIntParams extends Params {
    
    private HoltParams paramsCenter;
    private HoltParams paramsRadius;
    private Distance distance;

    public HoltParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(HoltParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    public HoltParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(HoltParams paramsRadius) {
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
        HoltIntParams param = new HoltIntParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        param.setDistance(distance);
        
        return param;
    }

    @Override
    public String toString() {
        return "HoltIntParams{" + "paramsCenter=" + paramsCenter + ", paramsRadius=" + paramsRadius + ", distance=" + distance + "}\n";
    }
}
