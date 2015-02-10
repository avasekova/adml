package models.params;

public class MLPintNnetParams extends PseudoIntervalParams {
    
    private NnetParams paramsCenter;
    private NnetParams paramsRadius;
    private Integer numNetsToTrain;
    
    @Override
    public NnetParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(NnetParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    @Override
    public NnetParams getParamsRadius() {
        return paramsRadius;
    }
    
    public void setParamsRadius(NnetParams paramsRadius) {
        this.paramsRadius = paramsRadius;
    }
    
    public Integer getNumNetsToTrain() {
        return numNetsToTrain;
    }

    public void setNumNetsToTrain(Integer numNetsToTrain) {
        this.numNetsToTrain = numNetsToTrain;
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
}
