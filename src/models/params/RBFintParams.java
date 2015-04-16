package models.params;

public class RBFintParams extends PseudoIntervalParams {
    
    private RBFParams paramsCenter;
    private RBFParams paramsRadius;
    private Integer numNetsToTrain;

    @Override
    public RBFParams getParamsCenter() {
        return paramsCenter;
    }

    public void setParamsCenter(RBFParams paramsCenter) {
        this.paramsCenter = paramsCenter;
    }

    @Override
    public RBFParams getParamsRadius() {
        return paramsRadius;
    }

    public void setParamsRadius(RBFParams paramsRadius) {
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
        RBFintParams param = new RBFintParams();
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

    @Override
    public String toString() {
        return super.toString() + "\nnumNetsToTrain=" + numNetsToTrain;
    }
}
