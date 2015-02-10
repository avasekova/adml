package models.params;

public class HoltWintersIntParams extends PseudoIntervalParams {
    
    private HoltWintersParams paramsCenter;
    private HoltWintersParams paramsRadius;
    
    @Override
    public HoltWintersParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(HoltWintersParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    @Override
    public HoltWintersParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(HoltWintersParams paramsRadius) {
        this.paramsRadius = paramsRadius;
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
        param.setDistance(this.getDistance());
        
        return param;
    }
}
