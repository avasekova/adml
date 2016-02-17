package models;

import gui.Plottable;
import utils.ErrorMeasures;

import java.io.Serializable;

public abstract class TrainAndTestReport implements Plottable, Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private final Model model;
    private String averageName = "";
    private String modelDescription = "";
    private ErrorMeasures errorMeasures;
    private int numTrainingEntries = 1;
    private String nnDiagramPlotCode = "";
    private boolean visible = true;
    private final boolean average;
    
    private String colourInPlot; //the name of R colour used in the last plot for this Report
    
    public TrainAndTestReport(Model model, boolean average) {
        this.model = model;
        this.average = average;
    }

    public TrainAndTestReport(Model model, String averageName, boolean average) {
        this.model = model;
        this.averageName = averageName;
        this.average = average;
    }
    
    public int getID() {
        return id;
    }
    
    public void setID(int id) {
        this.id = id;
    }

    public Model getModel() {
        return model;
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

    //TODO fix - for now does not work because of (not) sharing the R env - only the variable name is sent
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
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public boolean isAverage() {
        return average;
    }
    
    @Override
    public String toString() {
        return model + " " + averageName + "(" + id + ")"; //+ modelDescription;
    }
}
