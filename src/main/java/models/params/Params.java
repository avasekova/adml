package models.params;

import utils.Improvable;

import java.io.Serializable;

public class Params implements Serializable { //do not add futureForecastValues to params - no need to. just keep all forecasts (test+future) in one var

    private static final long serialVersionUID = 0L;

    private int UID; //unique for this Run and a particular model   //practically used only for iMLP files, TODO a better way?

    private int percentTrain;
    private int numForecasts;
    private int dataRangeFrom;
    private int dataRangeTo;
    private Integer seasonality = 0;
    private Improvable criterion;
    
    public Params() { }
    
    public Params(Integer percentTrain) {
        this.percentTrain = percentTrain;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
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

    public Integer getSeasonality() {
        return seasonality;
    }

    public void setSeasonality(Integer seasonality) {
        this.seasonality = seasonality;
    }

    public Improvable getCriterion() {
        return criterion;
    }

    public void setCriterion(Improvable criterion) {
        this.criterion = criterion;
    }
    
    public Params getClone() {
        Params param = new Params();
        param.setDataRangeFrom(dataRangeFrom);
        param.setDataRangeTo(dataRangeTo);
        param.setNumForecasts(numForecasts);
        param.setPercentTrain(percentTrain);
        param.setSeasonality(seasonality);
        param.setCriterion(criterion);
        
        return param;
    }
}
