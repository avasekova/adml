package utils.ugliez;

import org.rosuda.javaGD.JGDBufferedPanel;

import javax.swing.*;

public abstract class CallParams {
    
    private JList listPlotLegend;
    private JGDBufferedPanel canvasToUse;
    private int width;
    private int height;
    
    public CallParams(JList listPlotLegend, JGDBufferedPanel canvasToUse, int width, int height) {
        this.listPlotLegend = listPlotLegend;
        this.canvasToUse = canvasToUse;
        this.width = width;
        this.height = height;
    }
    
    public JList getListPlotLegend() {
        return listPlotLegend;
    }

    public void setListPlotLegend(JList listPlotLegend) {
        this.listPlotLegend = listPlotLegend;
    }

    public JGDBufferedPanel getCanvasToUse() {
        return canvasToUse;
    }

    public void setCanvasToUse(JGDBufferedPanel canvasToUse) {
        this.canvasToUse = canvasToUse;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
