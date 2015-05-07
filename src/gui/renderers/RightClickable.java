package gui.renderers;

import gui.MainFrame;
import gui.PlotDrawer;
import gui.Plottable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import models.avg.AveragesConfig;
import utils.Const;
import utils.ugliez.CallParamsDrawPlotGeneral;
import utils.ugliez.CallParamsDrawPlots;
import utils.ugliez.CallParamsDrawPlotsITS;
import utils.ugliez.PlotStateKeeper;

public interface RightClickable {
    
    Plottable getReport();
    JPopupMenu getMenu();
    
    static void redrawPlots(JList listPlotLegend, List<TrainAndTestReport> addedReports) {
        listPlotLegend.repaint();

        //and then redraw the plots:
        String rangeXCrisp = "range(c(" + PlotStateKeeper.getLastDrawnCrispXmin() + "," + PlotStateKeeper.getLastDrawnCrispXmax() + "))";
        String rangeYCrisp = "range(c(" + PlotStateKeeper.getLastDrawnCrispYmin() + "," + PlotStateKeeper.getLastDrawnCrispYmax() + "))";
        String rangeXInt = "range(c(" + PlotStateKeeper.getLastDrawnIntXmin() + "," + PlotStateKeeper.getLastDrawnIntXmax() + "))";
        String rangeYInt = "range(c(" + PlotStateKeeper.getLastDrawnIntYmin() + "," + PlotStateKeeper.getLastDrawnIntYmax() + "))";

        if (PlotStateKeeper.getLastCallParams() instanceof CallParamsDrawPlots) {
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
        } else if (PlotStateKeeper.getLastCallParams() instanceof CallParamsDrawPlotGeneral) {
            MainFrame.getInstance().drawPlotGeneral(true, "plot.ts", "", ((CallParamsDrawPlotGeneral)PlotStateKeeper.getLastCallParams()).getColnames());
            MainFrame.getInstance().setPlotRanges(1, 0);
        } else if (PlotStateKeeper.getLastCallParams() instanceof CallParamsDrawPlotsITS) {
            PlotDrawer.drawPlotsITS(false, (CallParamsDrawPlotsITS)(PlotStateKeeper.getLastCallParams()), rangeXInt, rangeYInt);
        }
    };
    
}
