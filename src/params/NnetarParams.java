package params;

public class NnetarParams extends Params {
    
    private Integer numNodesHidden;
    private Integer numSeasonalLags;
    private Integer numNonSeasonalLags;
    private Integer numReps;
    private Double lambda;
    
    public Integer getNumNodesHidden() {
        return numNodesHidden;
    }

    public void setNumNodesHidden(Integer numNodesHidden) {
        this.numNodesHidden = numNodesHidden;
    }

    public Integer getNumSeasonalLags() {
        return numSeasonalLags;
    }

    public void setNumSeasonalLags(Integer numSeasonalLags) {
        this.numSeasonalLags = numSeasonalLags;
    }
    
    public Integer getNumNonSeasonalLags() {
        return numNonSeasonalLags;
    }

    public void setNumNonSeasonalLags(Integer numNonSeasonalLags) {
        this.numNonSeasonalLags = numNonSeasonalLags;
    }

    public Integer getNumReps() {
        return numReps;
    }

    public void setNumReps(Integer numReps) {
        this.numReps = numReps;
    }

    public Double getLambda() {
        return lambda;
    }

    public void setLambda(Double lambda) {
        this.lambda = lambda;
    }

    @Override
    public NnetarParams getClone() {
        NnetarParams param = new NnetarParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setLambda(lambda);
        param.setNumForecasts(this.getNumForecasts());
        param.setNumNodesHidden(numNodesHidden);
        param.setNumNonSeasonalLags(numNonSeasonalLags);
        param.setNumReps(numReps);
        param.setNumSeasonalLags(numSeasonalLags);
        param.setPercentTrain(this.getPercentTrain());
        
        return param;
    }

    @Override
    public String toString() {
        return "NnetarParams{" + "numNodesHidden=" + numNodesHidden + ", numSeasonalLags=" + numSeasonalLags + ", numNonSeasonalLags=" + numNonSeasonalLags + ", numReps=" + numReps + ", lambda=" + lambda + "}\n";
    }
}
