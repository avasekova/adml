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

public class PlotLegendTurnOFFableListElement extends PlotLegendSimpleListElement implements RightClickable {
    
    public PlotLegendTurnOFFableListElement(Plottable report, final JList<RightClickable> listPlotLegend) {
        super(report, listPlotLegend);

        JMenuItem item = new JMenuItem(Const.ADD_TO_DATA);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RightClickable selected = listPlotLegend.getSelectedValue();
                if (selected.getReport() instanceof TrainAndTestReportCrisp) {
                    TrainAndTestReportCrisp r = (TrainAndTestReportCrisp) selected.getReport();
                    MainFrame.getInstance().addReportToData(r);
                } else if (selected.getReport() instanceof TrainAndTestReportInterval) {
                    //chcem, aby to pridalo C, R, MIN, MAX
                    TrainAndTestReportInterval r = (TrainAndTestReportInterval) selected.getReport();
                    MainFrame.getInstance().addReportToData(r);
                }
            }
        });
        getMenu().add(item);
    }
}