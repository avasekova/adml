package utils.imlp;

import gui.Plottable;

public abstract class IntervalNames implements Plottable {
    
    private String colourInPlot = "#FFFFFF";
    
    @Override
    public abstract String toString();
    
    @Override
    public String getColourInPlot() {
        return colourInPlot;
    }
    
    @Override
    public void setColourInPlot(String colourInPlot) {
        this.colourInPlot = colourInPlot;
    }
    
}
