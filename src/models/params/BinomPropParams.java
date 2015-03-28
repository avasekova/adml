package models.params;

public class BinomPropParams extends Params {
    
    private int quantileOne;
    private double quantileOneValue;
    private int quantileTwo;
    private double quantileTwoValue;
    private int numObservations;
    private int numSuccesses;

    public int getQuantileOne() {
        return quantileOne;
    }

    public void setQuantileOne(Integer quantileOne) {
        this.quantileOne = quantileOne;
    }

    public double getQuantileOneValue() {
        return quantileOneValue;
    }

    public void setQuantileOneValue(Double quantileOneValue) {
        this.quantileOneValue = quantileOneValue;
    }

    public int getQuantileTwo() {
        return quantileTwo;
    }

    public void setQuantileTwo(Integer quantileTwo) {
        this.quantileTwo = quantileTwo;
    }

    public double getQuantileTwoValue() {
        return quantileTwoValue;
    }

    public void setQuantileTwoValue(Double quantileTwoValue) {
        this.quantileTwoValue = quantileTwoValue;
    }

    public int getNumObservations() {
        return numObservations;
    }

    public void setNumObservations(Integer numObservations) {
        this.numObservations = numObservations;
    }

    public int getNumSuccesses() {
        return numSuccesses;
    }

    public void setNumSuccesses(Integer numSuccesses) {
        this.numSuccesses = numSuccesses;
    }
    
    @Override
    public BinomPropParams getClone() {
        BinomPropParams param = new BinomPropParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setSeasonality(this.getSeasonality());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setQuantileOne(quantileOne);
        param.setQuantileOneValue(quantileOneValue);
        param.setQuantileTwo(quantileTwo);
        param.setQuantileTwoValue(quantileTwoValue);
        param.setNumObservations(numObservations);
        param.setNumSuccesses(numSuccesses);
        
        return param;
    }

    @Override
    public String toString() {
        return "BinomPropParams{" + "quantileOne=" + quantileOne + ", quantileOneValue=" + quantileOneValue + ", quantileTwo=" + quantileTwo + ", quantileTwoValue=" + quantileTwoValue + ", numObservations=" + numObservations + ", numSuccesses=" + numSuccesses + '}';
    }
}
