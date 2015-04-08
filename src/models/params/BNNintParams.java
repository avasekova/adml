package models.params;

public class BNNintParams extends PseudoIntervalParams {
    
    private BNNParams paramsCenter;
    private BNNParams paramsRadius;
    private Integer numNetsToTrain;

    @Override
    public BNNParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(BNNParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    @Override
    public BNNParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(BNNParams paramsRadius) {
        this.paramsRadius = paramsRadius;
    }

    public Integer getNumNetsToTrain() {
        return numNetsToTrain;
    }

    public void setNumNetsToTrain(Integer numNetsToTrain) {
        this.numNetsToTrain = numNetsToTrain;
    }

    @Override
    public Params getClone() {
        BNNintParams param = new BNNintParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setSeasonality(this.getSeasonality());
        param.setParamsCenter(paramsCenter);
        param.setParamsRadius(paramsRadius);
        param.setDistance(this.getDistance());
        param.setNumNetsToTrain(numNetsToTrain);
        param.setPercentTrain(this.getPercentTrain());
        
        return param;
    }
    
}
