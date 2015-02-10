package models.params;

public class NnetarParams extends Params {
    
    private String colName;
    
    private Integer numNodesHidden;
    private Integer numSeasonalLags;
    private Integer numNonSeasonalLags;
    private Integer numReps;
    private Double lambda;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }
    
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
        param.setSeasonality(this.getSeasonality());
        param.setLambda(lambda);
        param.setNumForecasts(this.getNumForecasts());
        param.setNumNodesHidden(numNodesHidden);
        param.setNumNonSeasonalLags(numNonSeasonalLags);
        param.setNumReps(numReps);
        param.setNumSeasonalLags(numSeasonalLags);
        param.setColName(colName);
        param.setPercentTrain(this.getPercentTrain());
        
        return param;
    }

    @Override
    public String toString() {
        return "number of hidden nodes = " + numNodesHidden + "\n" + 
               "number of seasonal lags = " + numSeasonalLags + "\n" + 
               "number of non-seasonal lags = " + numNonSeasonalLags + "\n" + 
               "number of repetitions = " + numReps + "\n" + 
               "lambda = " + lambda;
    }
}
