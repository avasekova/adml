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
}
