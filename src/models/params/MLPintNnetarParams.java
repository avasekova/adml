package models.params;

public class MLPintNnetarParams extends MLPintParams {
    
    private NnetarParams paramsCenter;
    private NnetarParams paramsRadius;
    
    public NnetarParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(NnetarParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    public NnetarParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(NnetarParams paramsRadius) {
        this.paramsRadius = paramsRadius;
    }

    @Override
    public MLPintNnetarParams getClone() {
        MLPintNnetarParams param = new MLPintNnetarParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setDistance(this.getDistance());
        param.setSeasonality(this.getSeasonality());
        param.setNumForecasts(this.getNumForecasts());
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        param.setPercentTrain(this.getPercentTrain());
        param.setNumNetsToTrain(this.getNumNetsToTrain());
        param.setCriterion(this.getCriterion());
        
        return param;
    }
    
    @Override
    public String toString() {
        return "MLPintNnetarParams{" + "paramsCenter=" + paramsCenter + ", paramsRadius=" + paramsRadius + ", distance=" + getDistance() + "}\n";
    }
}