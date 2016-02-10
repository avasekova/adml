package utils.ugliez;


import javax.swing.*;

public abstract class CallParams {
    
    private JList listPlotLegend;
    private int width;
    private int height;
    
    public CallParams(JList listPlotLegend, int width, int height) {
        this.listPlotLegend = listPlotLegend;
        this.width = width;
        this.height = height;
    }
    
    public JList getListPlotLegend() {
        return listPlotLegend;
    }

    public void setListPlotLegend(JList listPlotLegend) {
        this.listPlotLegend = listPlotLegend;
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
