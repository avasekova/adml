package models;

import java.util.ArrayList;
import java.util.List;

public abstract class TrainAndTestReport { //TODO nejak vylepsit, *Interval a *Crisp fitted vals sa velmi podobaju, mohlo by sa s nimi
    //                                       dat pracovat jednotne...
    
    private final String modelName;
    private List<Double> errorMeasures = new ArrayList<>(); //TODO Map<String, Double> fixne metriky pre vsetkych
                                                            //resp. specialnu entitu na metriky crisp a interval (zvlast)
    private int numTrainingEntries;
    private String fittedValuesPlotCode = ""; //TODO toto zmazat a kreslit to rucne, ale
    
    public TrainAndTestReport(String modelName) { 
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }
    
    public List<Double> getErrorMeasures() {
        return errorMeasures;
    }

    public void setErrorMeasures(List<Double> errorMeasures) {
        this.errorMeasures = errorMeasures;
    }

    public int getNumTrainingEntries() {
        return numTrainingEntries;
    }

    public void setNumTrainingEntries(int numTrainingEntries) {
        this.numTrainingEntries = numTrainingEntries;
    }
    
    public String getFittedValuesPlotCode() { //TODO nepouzivat pre intervalove - pouzivat iba fitted vals atd. a kreslit sama
        return fittedValuesPlotCode;
    }

    public void setFittedValuesPlotCode(String forecastPlot) {
        this.fittedValuesPlotCode = forecastPlot;
    }
}
