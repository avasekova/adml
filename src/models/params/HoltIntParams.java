package models.params;

public class HoltIntParams extends PseudoIntervalParams {
    
    private HoltParams paramsCenter;
    private HoltParams paramsRadius;

    @Override
    public HoltParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(HoltParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    @Override
    public HoltParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(HoltParams paramsRadius) {
        this.paramsRadius = paramsRadius;
    }
    
    @Override
    public Params getClone() {
        HoltIntParams param = new HoltIntParams();
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
