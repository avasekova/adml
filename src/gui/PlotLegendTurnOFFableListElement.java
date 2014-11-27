package gui;

import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class PlotLegendTurnOFFableListElement extends JPanel {
    
    private final Plottable report;
    private final JCheckBox checkBox;
    
    public PlotLegendTurnOFFableListElement(Plottable report) {
        super();
        this.report = report;
        this.checkBox = new JCheckBox("", true); //no text, and selected
        checkBox.setBorder(null);
    }

    public Plottable getReport() {
        return report;
    }
    
    public Color getColour() {
        return Color.decode(report.getColourInPlot());
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBoxSelected(boolean selected) {
        this.checkBox.setSelected(selected);
    }
    
    @Override
    public String toString() {
        return report.toString();
    }
}
