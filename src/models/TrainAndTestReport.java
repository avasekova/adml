package models;

import utils.ErrorMeasures;

public abstract class TrainAndTestReport { //TODO nejak vylepsit, *Interval a *Crisp fitted vals sa velmi podobaju, mohlo by sa s nimi
    //                                       dat pracovat jednotne...
    
    private final String modelName;
    private ErrorMeasures errorMeasures;
    private int numTrainingEntries;
    
    public TrainAndTestReport(String modelName) { 
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }
    
    public ErrorMeasures getErrorMeasures() {
        return errorMeasures;
    }

    public void setErrorMeasures(ErrorMeasures errorMeasures) {
        this.errorMeasures = errorMeasures;
    }

    public int getNumTrainingEntries() {
        return numTrainingEntries;
    }

    public void setNumTrainingEntries(int numTrainingEntries) {
        this.numTrainingEntries = numTrainingEntries;
    }
}
