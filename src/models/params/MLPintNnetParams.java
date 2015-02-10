package models.params;

public class MLPintNnetParams extends MLPintParams {
    
    private NnetParams paramsCenter;
    private NnetParams paramsRadius;
    
    public NnetParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(NnetParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    public NnetParams getParamsRadius() {
        return paramsRadius;
    }
    
    public void setParamsRadius(NnetParams paramsRadius) {
        this.paramsRadius = paramsRadius;
    }
    
    @Override
    public MLPintNnetParams getClone() {
        MLPintNnetParams param = new MLPintNnetParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setDistance(this.getDistance());
        param.setNumForecasts(this.getNumForecasts());
        param.setSeasonality(this.getSeasonality());
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        param.setCriterion(this.getCriterion());
        param.setNumNetsToTrain(this.getNumNetsToTrain());
        param.setPercentTrain(this.getPercentTrain());
        
        return param;
    }

    @Override
    public String toString() {
        return "MLPintNnetParams{" + "paramsCenter=" + paramsCenter + ", paramsRadius=" + paramsRadius + ", distance=" + getDistance() + "}\n";
    }
}