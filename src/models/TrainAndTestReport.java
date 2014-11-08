package models;

import gui.Plottable;
import utils.ErrorMeasures;

public abstract class TrainAndTestReport implements Plottable {
    //TODO nejak vylepsit, *Interval a *Crisp fitted vals sa velmi podobaju, mohlo by sa s nimi
    //                                       dat pracovat jednotne...
    
    private final String modelName;
    private String modelDescription = "";
    private ErrorMeasures errorMeasures;
    private int numTrainingEntries = 1;
    private String nnDiagramPlotCode = "";
    
    private String colourInPlot = "#FFFFFF"; //the name of R colour used in the last plot for this Report
    
    public TrainAndTestReport(String modelName) { 
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }

    public String getModelDescription() {
        return modelDescription;
    }

    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
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

    public String getNnDiagramPlotCode() {
        return nnDiagramPlotCode;
    }

    public void setNnDiagramPlotCode(String nnDiagramPlotCode) {
        this.nnDiagramPlotCode = nnDiagramPlotCode;
    }

    @Override
    public String getColourInPlot() {
        return colourInPlot;
    }

    @Override
    public void setColourInPlot(String colourInPlot) {
        this.colourInPlot = colourInPlot;
    }
    
    @Override
    public String toString() {
        return modelName + modelDescription;
    }
}
