package gui.renderers;

import gui.MainFrame;
import gui.Plottable;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import utils.Const;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.List;

public class PlotLegendTurnOFFableListElement extends JPanel implements RightClickable {
    
    private final Plottable report;
    private final JPopupMenu menu;
    
    public PlotLegendTurnOFFableListElement(Plottable report, final JList listPlotLegend) {
        super();
        this.report = report;
        
        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (e.getActionCommand()) {
                    case Const.ADD_TO_DATA:
                        PlotLegendTurnOFFableListElement selected = (PlotLegendTurnOFFableListElement) listPlotLegend.getSelectedValue();
                        if (selected.report instanceof TrainAndTestReportCrisp) {
                            TrainAndTestReportCrisp r = (TrainAndTestReportCrisp) selected.report;
                            MainFrame.getInstance().addReportToData(r);
                        } else if (selected.report instanceof TrainAndTestReportInterval) {
                            //chcem, aby to pridalo C, R, MIN, MAX
                            TrainAndTestReportInterval r = (TrainAndTestReportInterval) selected.report;
                            MainFrame.getInstance().addReportToData(r);
                        }
                        break;
                    case Const.CHANGE_COLOUR:
                        Color chosenColor = JColorChooser.showDialog(null, "Select Colour", Color.BLACK);
                        if (chosenColor != null) {
                            ((PlotLegendTurnOFFableListElement) listPlotLegend.getSelectedValue()).report
                                    .setColourInPlot(String.format("#%02X%02X%02X", chosenColor.getRed(), 
                                            chosenColor.getGreen(), chosenColor.getBlue())); //found this at SO
                            RightClickable.redrawPlots(listPlotLegend);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("unknown selection in menu");
                }
            }
        };
        
        menu = new JPopupMenu();
        
        JMenuItem item;
        item = new JMenuItem(Const.ADD_TO_DATA);
        item.addActionListener(menuListener);
        menu.add(item);
        
        menu.add(item = new JMenuItem(Const.CHANGE_COLOUR));
        item.addActionListener(menuListener);
        
        
        MouseListener mListener = new RightClickLegendPopupMouseListener(listPlotLegend, this);
        this.addMouseListener(mListener);
    }

    @Override
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
    
    @Override
    public JPopupMenu getMenu() {
        return menu;
    }
}