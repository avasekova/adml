package models.params;

import utils.imlp.dist.Distance;

public class IntervalHoltParams extends Params {
    
    private String colNameCenter;
    private String colNameRadius;
    private String alpha;
    private String beta;
    private Distance distance;

    public String getColNameCenter() {
        return colNameCenter;
    }

    public void setColNameCenter(String colNameCenter) {
        this.colNameCenter = colNameCenter;
    }

    public String getColNameRadius() {
        return colNameRadius;
    }

    public void setColNameRadius(String colNameRadius) {
        this.colNameRadius = colNameRadius;
    }

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    public String getBeta() {
        return beta;
    }

    public void setBeta(String beta) {
        this.beta = beta;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }
    
    @Override
    public IntervalHoltParams getClone() {
        IntervalHoltParams param = new IntervalHoltParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setSeasonality(this.getSeasonality());
        param.setColNameCenter(colNameCenter);
        param.setColNameRadius(colNameRadius);
        param.setAlpha(alpha);
        param.setBeta(beta);
        param.setDistance(distance);
        
        return param;
    }

    @Override
    public String toString() {
        return "IntervalHoltParams{" + "colNameCenter=" + colNameCenter + ", colNameRadius=" + colNameRadius + ", alpha=" + alpha + ", beta=" + beta + "}\n";
    }
}
