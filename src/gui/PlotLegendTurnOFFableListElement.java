package gui;

import java.awt.Color;
import javax.swing.JPanel;

public class PlotLegendTurnOFFableListElement extends JPanel {
    
    private final Plottable report;
    
    public PlotLegendTurnOFFableListElement(Plottable report) {
        super();
        this.report = report;
    }

    public Plottable getReport() {
        return report;
    }
    
    public Color getColour() {
        return Color.decode(report.getColourInPlot());
    }
    
    @Override
    public String toString() {
        return report.toString();
    }
}