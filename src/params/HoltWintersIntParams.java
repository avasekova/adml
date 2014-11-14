package params;

import utils.imlp.dist.Distance;

public class HoltWintersIntParams extends Params {
    
    private HoltWintersParams paramsCenter;
    private HoltWintersParams paramsRadius;
    private Distance distance;

    public HoltWintersParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(HoltWintersParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    public HoltWintersParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(HoltWintersParams paramsRadius) {
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
        HoltWintersIntParams param = new HoltWintersIntParams();
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
        return "HoltWintersIntParams{" + "paramsCenter=" + paramsCenter + ", paramsRadius=" + paramsRadius + ", distance=" + distance + "}\n";
    }
}
