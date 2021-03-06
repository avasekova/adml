package utils.imlp;

import gui.Plottable;

import java.io.Serializable;

public abstract class IntervalNames implements Plottable, Serializable {
    private static final long serialVersionUID = 1L;
    
    private String colourInPlot;
    private boolean visible;
    
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

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
