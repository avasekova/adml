package gui;

public class DefaultPlottable implements Plottable {

    private String text;
    private String colourInPlot;
    private boolean visible;
    
    public DefaultPlottable(String colourInPlot, String text) {
        this.colourInPlot = colourInPlot;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
    
    @Override
    public String toString() {
        return text;
    }
}
