package gui.renderers;

import gui.MainFrame;
import gui.PlotDrawer;
import gui.Plottable;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JColorChooser;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import models.avg.Average;
import models.avg.AveragesConfig;
import utils.Const;
import utils.ugliez.CallParamsDrawPlots;
import utils.ugliez.PlotStateKeeper;

public class PlotLegendTurnOFFableListElement extends JPanel {
    
    private final Plottable report;
    private final JPopupMenu menu;
    
    public PlotLegendTurnOFFableListElement(Plottable report, final JList listPlotLegend, final List<TrainAndTestReport> addedReports) {
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
                            
                        }
                        break;
                    case Const.CHANGE_COLOUR:
                        Color chosenColor = JColorChooser.showDialog(null, "Select Colour", Color.BLACK);
                        if (chosenColor != null) {
                            ((PlotLegendTurnOFFableListElement) listPlotLegend.getSelectedValue()).report
                                    .setColourInPlot(String.format("#%02X%02X%02X", chosenColor.getRed(), 
                                            chosenColor.getGreen(), chosenColor.getBlue())); //found this at SO
                            redrawPlots(listPlotLegend, addedReports);
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
        
        
        MouseListener mListener = new RightClickLegendPopupMouseListener(listPlotLegend, addedReports);
        this.addMouseListener(mListener);
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
    
    class RightClickLegendPopupMouseListener extends MouseAdapter {
        
        private final JList listPlotLegend;
        private final List<TrainAndTestReport> addedReports;
        
        public RightClickLegendPopupMouseListener(final JList listPlotLegend, final List<TrainAndTestReport> addedReports) {
            this.listPlotLegend = listPlotLegend;
            this.addedReports = addedReports;
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            //tu nerob nic, az v Released
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (! displayPopup(e)) {
                //invert the selection state of the checkbox
                getReport().setVisible(! getReport().isVisible());
                redrawPlots(listPlotLegend, addedReports);
            }
        }
        
        @Override
        public void mouseEntered(MouseEvent e) { displayPopup(e); }
        @Override
        public void mouseExited(MouseEvent e) { displayPopup(e); }
        @Override
        public void mouseClicked(MouseEvent e) { displayPopup(e); }

        private boolean displayPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                listPlotLegend.setSelectedIndex(listPlotLegend.locationToIndex(e.getPoint())); //select the item
                menu.show(listPlotLegend.getParent(), e.getX(), e.getY());
            }

            return e.isPopupTrigger();
        }
    }
    
    private void redrawPlots(JList listPlotLegend, List<TrainAndTestReport> addedReports) {
        listPlotLegend.repaint();

        //and then redraw the plots:
        String rangeXCrisp = "range(c(" + PlotStateKeeper.getLastDrawnCrispXmin() + "," + PlotStateKeeper.getLastDrawnCrispXmax() + "))";
        String rangeYCrisp = "range(c(" + PlotStateKeeper.getLastDrawnCrispYmin() + "," + PlotStateKeeper.getLastDrawnCrispYmax() + "))";
        String rangeXInt = "range(c(" + PlotStateKeeper.getLastDrawnIntXmin() + "," + PlotStateKeeper.getLastDrawnIntXmax() + "))";
        String rangeYInt = "range(c(" + PlotStateKeeper.getLastDrawnIntYmin() + "," + PlotStateKeeper.getLastDrawnIntYmax() + "))";

        List<TrainAndTestReportCrisp> updatedReportsCTS = new ArrayList<>();
        updatedReportsCTS.addAll(((CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams())).getReportsCTS());
        List<TrainAndTestReportInterval> updatedReportsIntTS = new ArrayList<>();
        updatedReportsIntTS.addAll(((CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams())).getReportsITS());

        for (TrainAndTestReport rep : addedReports) {
            if (rep instanceof TrainAndTestReportCrisp) {
                updatedReportsCTS.add((TrainAndTestReportCrisp)rep);
            } else {
                if (rep instanceof TrainAndTestReportInterval) {
                    updatedReportsIntTS.add((TrainAndTestReportInterval) rep);
                }
            }
        }

        ((CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams())).setAvgConfig(
                new AveragesConfig(new ArrayList<>(),  //clear the avgs
            ((CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams())).getAvgConfig().isAvgONLY()));
        ((CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams())).setReportsCTS(updatedReportsCTS);
        ((CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams())).setReportsITS(updatedReportsIntTS);

        PlotDrawer.drawPlots(Const.MODE_DRAW_NEW, Const.MODE_REFRESH_ONLY, 
                (CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams()), 
                rangeXCrisp , rangeYCrisp, rangeXInt, rangeYInt);
    }
}