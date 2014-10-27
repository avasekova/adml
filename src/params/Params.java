package params;

public abstract class Params { //do not add futureForecastValues to params - no need to. just keep all forecasts (test+future) in one var
    
    private int percentTrain;
    private int numForecasts;
    private int dataRangeFrom;
    private int dataRangeTo;
    
    public Params() { }
    
    public Params(Integer percentTrain) {
        this.percentTrain = percentTrain;
    }

    public int getPercentTrain() {
        return percentTrain;
    }

    public void setPercentTrain(Integer percentTrain) {
        this.percentTrain = percentTrain;
    }

    public int getNumForecasts() {
        return numForecasts;
    }

    public void setNumForecasts(Integer numForecasts) {
        this.numForecasts = numForecasts;
    }

    public int getDataRangeFrom() {
        return dataRangeFrom;
    }

    public void setDataRangeFrom(Integer dataRangeFrom) {
        this.dataRangeFrom = dataRangeFrom;
    }

    public int getDataRangeTo() {
        return dataRangeTo;
    }

    public void setDataRangeTo(Integer dataRangeTo) {
        this.dataRangeTo = dataRangeTo;
    }
    
    public abstract Params getClone();
}
