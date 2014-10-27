package params;

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
        param.setDistanceFunction(this.getDistanceFunction());
        param.setNumForecasts(this.getNumForecasts());
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        param.setPercentTrain(this.getPercentTrain());
        
        return param;
    }

    @Override
    public String toString() {
        return "MLPintNnetParams{" + "paramsCenter=" + paramsCenter + ", paramsRadius=" + paramsRadius + ", distanceFunction=" + getDistanceFunction() + "}\n";
    }
}
