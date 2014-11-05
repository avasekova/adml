package params;

import utils.imlp.dist.Distance;

public class RBFintParams extends Params {
    
    //zatial na tomto nic nezavisi, tj aj ked je to false, pocita sa s tym ako keby to bol center a radius
    private Boolean centerRadius; //TODO vymysliet krajsie?
    private RBFParams paramsCenter;
    private RBFParams paramsRadius;
    private Distance distance;
    
    public Boolean isCenterRadius() {
        return centerRadius;
    }

    public void setCenterRadius(Boolean centerRadius) {
        this.centerRadius = centerRadius;
    }

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

    @Override
    public Params getClone() {
        RBFintParams param = new RBFintParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        param.setCenterRadius(centerRadius);
        param.setDistance(distance);
        param.setPercentTrain(this.getPercentTrain());
        
        return param;
    }

    @Override
    public String toString() {
        return "RBFintParams{" + "paramsCenter=" + paramsCenter + ", paramsRadius=" + paramsRadius + "}\n";
    }
}
