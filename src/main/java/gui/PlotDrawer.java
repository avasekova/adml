package gui;

import gui.renderers.*;
import gui.tablemodels.DataTableModel;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import models.params.BasicStats;
import org.rosuda.JRI.REXP;
import org.rosuda.javaGD.JGDBufferedPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Const;
import utils.MyRengine;
import utils.Utils;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;
import utils.ugliez.CallParamsDrawPlotGeneral;
import utils.ugliez.CallParamsDrawPlots;
import utils.ugliez.CallParamsDrawPlotsITS;
import utils.ugliez.PlotStateKeeper;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static gui.tablemodels.DataTableModel.LABELS_AXIS_X;

public class PlotDrawer {
    //TODO the whole thing could use some cleanup and refactoring, maybe even into several classes
    //and add everything related to plotting here - e.g. DataTableModel contains sth, maybe MainFrame too, etc

    private static final Logger logger = LoggerFactory.getLogger(PlotDrawer.class);
    
    private static final int COLUMNS_DIAGRAMSNN = 3;
    private static final int ROWS_DIAGRAMSNN = 3;
    private static final int COLUMNS_BOXHIST = 2;
    private static final int ROWS_BOXHIST = 2;
    
    private static JGDBufferedPanel drawNowToThisGDBufferedPanel = new JGDBufferedPanel(0, 0);
    
    //drawNew is true if the max bounds of the picture are to change, i.e. drawn from Run, Plot CTS, Plot ITS, ACF, PACF
    //drawNew is false if the picture is only zoomed and not drawn anew, i.e. from Zoom CTS, Zoom ITS
    //refreshOnly is true if we are adding/removing plots from the legend. false, if run "clean" right after Run or Zoom
    public static List<JGDBufferedPanel> drawPlots(boolean drawNew, boolean refreshOnly, CallParamsDrawPlots par) {
        String rangeXCrisp = "";
        String rangeYCrisp = "";
        String rangeXInt = "";
        String rangeYInt = "";
        
        if (! par.getReportsCTS().isEmpty()) {
            rangeXCrisp = getRangeXCrisp(par.getAllDataCTS(), par.getNumForecasts(), par.getFrom(), par.getTo());
            rangeYCrisp = getRangeYCrisp(par.getAllDataCTS(), par.getReportsCTS().stream().filter(r -> !r.isAverage())
                    .collect(Collectors.toList())); //averages will always be in this range, so leave them out
        }
        
        if (! par.getReportsITS().isEmpty()) {
            rangeXInt = getRangeXInterval(par.getSizeDataWithoutFromToCrop(), par.getNumForecasts(), par.getFrom(), par.getTo());
            par.getReportsITS().removeIf(r -> (r.getFittedValues().isEmpty() && r.getForecastValuesTest().isEmpty() && r.getForecastValuesFuture().isEmpty()));
            rangeYInt = getRangeYInterval(par.getReportsITS().stream().filter(r -> !r.isAverage()).collect(Collectors.toList()));
        }
        
        return drawPlots(drawNew, refreshOnly, par, rangeXCrisp, rangeYCrisp, rangeXInt, rangeYInt);
    }
    
    public static List<JGDBufferedPanel> drawPlots(boolean drawNew, final boolean refreshOnly, CallParamsDrawPlots par,
                                 String rangeXCrisp, String rangeYCrisp, String rangeXInt, String rangeYInt) {
        if (par.getReportsCTS().isEmpty() && par.getReportsITS().isEmpty()) {
            return new ArrayList<>();
        }

        int width = par.getWidth();
        int height = par.getHeight();
        List<Double> allDataCTS = par.getAllDataCTS();
        int numForecasts = par.getNumForecasts();
        List<TrainAndTestReportCrisp> reportsCTS = par.getReportsCTS();
        List<TrainAndTestReportInterval> reportsIntTS = par.getReportsITS();
        int from = par.getFrom();
        int to = par.getTo();
        String colname_CTS = par.getColname_CTS();
        
        ColourService.getService().resetCounter();
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("JavaGD");

        drawNowToThisGDBufferedPanel = new JGDBufferedPanel(width, height);
        rengine.eval("JavaGD()");
        
        if ((! reportsCTS.isEmpty()) && (! reportsIntTS.isEmpty())) { //both will be drawn
            rengine.eval("par(mfrow=c(1,2))"); //two plots side by side. then just call 'plot' twice
        }
        
        if (! reportsCTS.isEmpty()) { //plot CTS
            boolean wasSthDrawnCrisp = false;
            
            //draw all
            for (TrainAndTestReportCrisp r : reportsCTS) {
                //if only averages are to be drawn and this is not one, skip it
                if (par.getAvgConfig().isAvgONLY() && (! r.isAverage())) {
                    continue;
                }

                if (r.getColourInPlot() == null) {
                    r.setColourInPlot(ColourService.getService().getNewColour());
                }

                //make sure this is called _after_ the colour has been assigned
                if (! r.isVisible()) { //skip those that are turned off
                    continue;
                }
                
                if (wasSthDrawnCrisp) {
                    rengine.eval("par(new=TRUE)");
                }
                
                StringBuilder plotCode = new StringBuilder(r.getPlotCode());
                plotCode.insert(r.getPlotCode().length() - 1, ", xlim = " + rangeXCrisp + ", ylim = " + rangeYCrisp + ", "
                        + "axes=FALSE, ann=FALSE, " //suppress axes names and labels, just draw them for the main data
                        + "lwd=4, col=\"" + r.getColourInPlot() + "\"");
                plotCode.insert(10, "rep(NA, " + par.getFrom() + "), "); //hack - shift

                rengine.eval(plotCode.toString());
                wasSthDrawnCrisp = true;


                //draw prediction intervals, if it has them:
                if ((numForecasts > 0) && (r.getPredictionIntervalsLowers().length > 0)) {
                    final String UPPERS = "uppers" + Utils.getCounter();
                    final String LOWERS_REVERSE = "lowersReverse" + Utils.getCounter();
                    List<Double> uppersOnlyFuture = Utils.arrayToList(r.getPredictionIntervalsUppers())
                            .subList(r.getPredictionIntervalsUppers().length - numForecasts, r.getPredictionIntervalsUppers().length);
                    rengine.assign(UPPERS, Utils.listToArray(uppersOnlyFuture));
                    List<Double> lowersOnlyFutureReverse = Utils.arrayToList(r.getPredictionIntervalsLowers())
                            .subList(r.getPredictionIntervalsLowers().length - numForecasts, r.getPredictionIntervalsLowers().length);
                    Collections.reverse(lowersOnlyFutureReverse);
                    rengine.assign(LOWERS_REVERSE, Utils.listToArray(lowersOnlyFutureReverse));
                    int startingPoint = par.getFrom() + r.getNumTrainingEntries() + r.getRealOutputsTest().length + 1;
                    int endingPoint = startingPoint + numForecasts - 1;
                    //(startingpoint-1) because I want to draw them from the last known (forecast test) value
                    rengine.eval("polygon(c(seq(" + startingPoint + "," + endingPoint + "),"
                                         + "seq(" + endingPoint + "," + (startingPoint-1) + ")),"
                                       + "c(" + UPPERS + "," + LOWERS_REVERSE + ","
                                              //and add the last "known" (forecast test) value
                                              + r.getForecastValuesTest()[r.getForecastValuesTest().length-1] + "),"
                               + "density=NA, col=" + getRGBColorStringForHEX(r.getColourInPlot(), 30)
                               + ", border=NA)");
                    
                    rengine.rm(UPPERS, LOWERS_REVERSE);
                }

                //add a dashed vertical line to separate test and train
                rengine.eval("abline(v = " + (r.getNumTrainingEntries() + par.getFrom()) + ", lty = 2, lwd=2, col=\"" + r.getColourInPlot() + "\")");
            }
            
            //all lines and avgs should be drawn now
            
            allDataCTS = allDataCTS.subList(from, Math.min(to+numForecasts, allDataCTS.size()));
            rengine.assign("all.data", Utils.listToArray(allDataCTS));
            if (wasSthDrawnCrisp) {
                rengine.eval("par(new=TRUE)");
            }
            rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), all.data), xlim = " + rangeXCrisp + ", ylim = " + rangeYCrisp + ", "
                    + "ylab=\"" + colname_CTS + "\","
                    + "xaxt=\"n\", " //suppress axis X
                    + "lwd=2, col=\"#444444\")");
            rengine.eval("axis(1,at=seq(1,length(" + DataTableModel.LABELS_AXIS_X + ")),labels=" + DataTableModel.LABELS_AXIS_X 
                    + ",las=2)");
            rengine.eval("abline(v = " + (to) + ", lty = 3)"); //dashed vertical line to separate forecasts
            
            double[] rangeX = rengine.evalAndReturnArray(rangeXCrisp);
            double[] rangeY = rengine.evalAndReturnArray(rangeYCrisp);
            PlotStateKeeper.setLastDrawnCrispXmin(rangeX[0]);
            PlotStateKeeper.setLastDrawnCrispXmax(rangeX[1]);
            PlotStateKeeper.setLastDrawnCrispYmin(rangeY[0]);
            PlotStateKeeper.setLastDrawnCrispYmax(rangeY[1]);
            if (drawNew) {
                PlotStateKeeper.setCrispXmin(rangeX[0]);
                PlotStateKeeper.setCrispXmax(rangeX[1]);
                PlotStateKeeper.setCrispYmin(rangeY[0]);
                PlotStateKeeper.setCrispYmax(rangeY[1]);
            }
            
            rengine.rm("all.data");
        }

        if (! reportsIntTS.isEmpty()) { //plot ITS
            boolean wasSthDrawnIntTS = false;
            
            //now draw
            for (TrainAndTestReportInterval r : reportsIntTS) {
                //if only averages are to be drawn and this is not one, skip it
                if (par.getAvgConfig().isAvgONLY() && (! r.isAverage())) {
                    continue;
                }

                if (r.getColourInPlot() == null) {
                    r.setColourInPlot(ColourService.getService().getNewColour());
                }

                //make sure this is called _after_ the colour has been assigned
                if (! r.isVisible()) { //skip those that are turned off
                    continue;
                }
                
                if (wasSthDrawnIntTS) {
                    rengine.eval("par(new=TRUE)");
                }

                //plot fitted values:
                final int sizeFitted = r.getFittedValues().size();
                rengine.assign("lower", r.getFittedValuesLowers());
                rengine.assign("upper", r.getFittedValuesUppers());
                //hack - shift
                rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), lower), type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                rengine.eval("par(new=TRUE)");
                //hack - shift
                rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), upper), type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                rengine.eval("segments(" + (1+par.getFrom()) + ":" + (sizeFitted+par.getFrom()) + ", lower, " + (1+par.getFrom()) + ":" + (sizeFitted+par.getFrom()) + ", upper, xlim = "
                        + rangeXInt + ", ylim = " + rangeYInt + ", lwd=4, col=\"" + r.getColourInPlot() + "\")");

                //plot fitted values for training data:
                final int sizeForecastTest = r.getForecastValuesTest().size();
                rengine.eval("par(new=TRUE)");
                rengine.assign("lower", r.getForecastValuesTestLowers());
                rengine.assign("upper", r.getForecastValuesTestUppers());
                //hack - shift
                rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), lower), type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                rengine.eval("par(new=TRUE)");
                //hack - shift
                rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), upper), type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                rengine.eval("segments(" + (sizeFitted+1+par.getFrom()) + ":" + (sizeFitted+sizeForecastTest+par.getFrom()) + ", lower, "
                        + (sizeFitted+1+par.getFrom()) + ":" + (sizeFitted+sizeForecastTest+par.getFrom()) + ", upper, xlim = " + rangeXInt
                        + ", ylim = " + rangeYInt + ", lwd=4, col=\"" + r.getColourInPlot() + "\")");

                //plot forecasts future
                final int sizeForecastFuture = r.getForecastValuesFuture().size();
                if (sizeForecastFuture > 0) {
                    rengine.eval("par(new=TRUE)");
                    rengine.assign("lower", r.getForecastValuesFutureLowers());
                    rengine.assign("upper", r.getForecastValuesFutureUppers());
                    //hack - shift
                    rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), lower), type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                            + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                    rengine.eval("par(new=TRUE)");
                    //hack - shift
                    rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), upper), type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                            + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                    rengine.eval("segments(" + (sizeFitted+sizeForecastTest+1+par.getFrom()) + ":"
                            + (sizeFitted+sizeForecastTest+sizeForecastFuture+par.getFrom()) + ", lower, "
                            + (sizeFitted+sizeForecastTest+1+par.getFrom()) + ":" + (sizeFitted+sizeForecastTest+sizeForecastFuture+par.getFrom())
                            + ", upper, xlim = " + rangeXInt + ", ylim = " + rangeYInt
                            + ", lwd=4, col=\"" + r.getColourInPlot() + "\")");
                }

                //add a dashed vertical line to separate test and train
                rengine.eval("abline(v = " + (sizeFitted+par.getFrom()) + ", lty = 2, lwd=2, col=\"" + r.getColourInPlot() + "\")");

                wasSthDrawnIntTS = true;
            }
            
            rengine.rm("lower", "upper");
            
            //all ITS and avgs should be drawn now
            
            if (wasSthDrawnIntTS) { //otherwise it can fall into the CTS plot
                rengine.eval("par(new=TRUE)");
            }
                
            //and plot the real data over it all:
            //TODO hack, for now we take the data from the 1st report. rethink
            int size = reportsIntTS.get(reportsIntTS.size() - 1).getRealValuesLowers().size();
            //do not sublist it again! the realData in the report is already sublisted bsd on the range from the Run settings
            rengine.assign("all.lower", Utils.listToArray(reportsIntTS.get(reportsIntTS.size() - 1).getRealValuesLowers()));
            rengine.assign("all.upper", Utils.listToArray(reportsIntTS.get(reportsIntTS.size() - 1).getRealValuesUppers()));

            //TODO play around with the range, see if they are being 'justified' correctly
            rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), all.lower), type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels
            rengine.eval("par(new=TRUE)");
            rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), all.upper), type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                    + "xaxt=\"n\", " //suppress axis X
                    + "ylab=\"" +       "<<add the interval.toString() here>>"      + "\")");
            rengine.eval("axis(1,at=seq(1,length(" + DataTableModel.LABELS_AXIS_X + ")),labels=" + DataTableModel.LABELS_AXIS_X 
                    + ",las=2)");
            rengine.eval("segments(" + (1+par.getFrom()) + ":" + (size+par.getFrom()) + ", all.lower, " + (1+par.getFrom()) + ":" + (size+par.getFrom()) + ", all.upper, xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", lwd=2, col=\"#444444\")");
            //add a line separating real data from forecasts
            rengine.eval("abline(v = " + (size+par.getFrom()) + ", lty = 3)");
            
            double[] rangeX = rengine.evalAndReturnArray(rangeXInt);
            double[] rangeY = rengine.evalAndReturnArray(rangeYInt);
            PlotStateKeeper.setLastDrawnIntXmin(rangeX[0]);
            PlotStateKeeper.setLastDrawnIntXmax(rangeX[1]);
            PlotStateKeeper.setLastDrawnIntYmin(rangeY[0]);
            PlotStateKeeper.setLastDrawnIntYmax(rangeY[1]);
            if (drawNew) {
                PlotStateKeeper.setIntXmin(rangeX[0]);
                PlotStateKeeper.setIntXmax(rangeX[1]);
                PlotStateKeeper.setIntYmin(rangeY[0]);
                PlotStateKeeper.setIntYmax(rangeY[1]);
            }
            
            rengine.rm("all.lower", "all.upper");
        }


        drawNowToThisGDBufferedPanel.setSize(new Dimension(width, height));
        drawNowToThisGDBufferedPanel.initRefresh();
        
        if (! refreshOnly) {
            PlotStateKeeper.setLastCallParams(par);
            //and draw the legend
            List<Plottable> allReports = new ArrayList<>();
            allReports.addAll(reportsCTS);
            allReports.addAll(reportsIntTS);
            drawLegend(par.getListPlotLegend(), allReports, new PlotLegendTurnOFFableListCellRenderer());
        }

        return Arrays.asList(drawNowToThisGDBufferedPanel);
    }
    
    public static List<BasicStats> drawPlotsResiduals(Map<String, List<Double>> residuals, JList listPlotLegendResiduals, 
            JGDBufferedPanel canvasToUse, int width, int height, String plotFunction) {
        //TODO refactor... this is again just copy+paste+change
        List<BasicStats> basicStatss = new ArrayList<>();
        
        if (residuals.isEmpty()) {
            return basicStatss;
        }

        Set<String> keys = residuals.keySet();
        String rangeXCrisp = "range(c(1, " + (residuals.get(keys.toArray(new String[keys.size()])[0]).size()) + "))"; //hack
        String rangeYCrisp = getRangeYCrisp(residuals.values());
        
        ColourService.getService().resetCounter();
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("JavaGD");
        
        drawNowToThisGDBufferedPanel = canvasToUse;
        rengine.eval("JavaGD()");
        
        ((DefaultListModel)(listPlotLegendResiduals.getModel())).removeAllElements();
        
        boolean next = false;
        for (String key : residuals.keySet()) {
            final String colourToUseNow = ColourService.getService().getNewColour();
            //add to legend
            ((DefaultListModel)(listPlotLegendResiduals.getModel())).addElement(new DefaultPlottable(null, colourToUseNow, key));
            
            String rVectorString = Utils.listToRVectorString(residuals.get(key));
            
            StringBuilder plotCode = new StringBuilder(plotFunction);
            if (next) {
                rengine.eval("par(new=TRUE)");
                plotCode.append("(").append(rVectorString)
                        .append(", xlim = ").append(rangeXCrisp).append(", ylim = ").append(rangeYCrisp)
                        .append(", axes=FALSE, ann=FALSE, ") //suppress axes names and labels, just draw them for the first one
                        .append("lwd=4, col=\"").append(colourToUseNow).append("\")");
            } else {
                next = true;
                plotCode.append("(").append(rVectorString)
                        .append(", xlim = ").append(rangeXCrisp).append(", ylim = ").append(rangeYCrisp)
                        .append(", xlab=\"Time\", ylab=\"Residuals\", ")
                        .append("lwd=4, col=\"").append(colourToUseNow).append("\")");
            }
            
            rengine.eval(plotCode.toString());

            //add a dashed vertical line to separate test and train
            //possible later when I replace String keys with TrainAndTestReport keys in the residuals map
//            rengine.eval("abline(v = " + (r.getNumTrainingEntries() + par.getFrom()) + ", lty = 2, lwd=2, col=\"" + colourToUseNow + "\")");
//            if ((! refreshOnly) && (r.getColourInPlot().equals("....."))) {
//                r.setColourInPlot(colourToUseNow);
//            }
            
            double mean = rengine.evalAndReturnArray("mean(na.omit(" + rVectorString + "))")[0];
            double stDev = rengine.evalAndReturnArray("sd(na.omit(" + rVectorString + "))")[0];
            double median = rengine.evalAndReturnArray("median(na.omit(" + rVectorString + "))")[0];
            BasicStats basicStats = new BasicStats("Residuals for " + key);
            basicStats.setMean(mean);
            basicStats.setStdDev(stDev);
            basicStats.setMedian(median);
            basicStatss.add(basicStats);
        }

        drawNowToThisGDBufferedPanel.setSize(new Dimension(width, height)); //won't shrink under a threshold, TODO
        drawNowToThisGDBufferedPanel.initRefresh();
        
        listPlotLegendResiduals.setCellRenderer(new PlotLegendSimpleListCellRenderer());
        listPlotLegendResiduals.repaint();
        
        return basicStatss;
    }
    
    public static List<JGDBufferedPanel> drawPlotsITS(boolean drawNew, CallParamsDrawPlotsITS par) {
        List<Double> allVals = getAllVals(par.getDataTableModel(), par.getListCentreRadius(), par.getListLowerUpper());
//        String rangeX = ; //let's say they all have the same num of observations
        String rangeY = getRangeYMultipleInterval(allVals);
        String rangeX = getRangeXMultipleInterval(par.getDataTableModel(), par.getListCentreRadius(), par.getListLowerUpper());
        
        return drawPlotsITS(drawNew, par, rangeX, rangeY);
    }
    
    public static List<JGDBufferedPanel> drawPlotsITS(boolean drawNew, CallParamsDrawPlotsITS par, String rangeX, String rangeY) {
        drawNowToThisGDBufferedPanel = new JGDBufferedPanel(par.getWidth(), par.getHeight());
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("JavaGD");
        rengine.eval("JavaGD()");
        
        ColourService.getService().resetCounter();
        
        
        boolean next = false;
        for (IntervalNamesCentreRadius interval : par.getListCentreRadius()) {
            //remember the colour for the legend
            if (interval.getColourInPlot() == null) {
                interval.setColourInPlot(ColourService.getService().getNewColour());
            }
            
            String lineStyle = ", lwd=4, col=\"" + interval.getColourInPlot() + "\"";
            drawPlotITS_CenterRadius(par.getWidth(), par.getHeight(), par.getDataTableModel().getDataForColname(interval.getCentre()),
                    par.getDataTableModel().getDataForColname(interval.getRadius()), next, lineStyle, rangeX, rangeY);
            if (! next) {
                next = true;
            }
        }
        
        next = (! par.getListCentreRadius().isEmpty()) && (! par.getListLowerUpper().isEmpty());
        
        for (IntervalNamesLowerUpper interval : par.getListLowerUpper()) {
            if (interval.getColourInPlot() == null) {
                interval.setColourInPlot(ColourService.getService().getNewColour());
            }
            
            String lineStyle = ", lwd=4, col=\"" + interval.getColourInPlot() + "\"";
            drawPlotITS_LBUB(par.getWidth(), par.getHeight(), par.getDataTableModel().getDataForColname(interval.getLowerBound()),
                    par.getDataTableModel().getDataForColname(interval.getUpperBound()), next, lineStyle, rangeX, rangeY);
            if (! next) {
                next = true;
            }
        }
        
        rengine.eval("axis(1,at=seq(1,length(" + LABELS_AXIS_X + ")),labels=" + LABELS_AXIS_X + ",las=2)");
        
        //draw legend
        List<Plottable> plots = new ArrayList<>();
        plots.addAll(par.getListCentreRadius());
        plots.addAll(par.getListLowerUpper());
        drawLegend(par.getListPlotLegend(), plots, new PlotLegendSimpleListCellRenderer());
        
        double[] ranX = rengine.evalAndReturnArray(rangeX);
        double[] ranY = rengine.evalAndReturnArray(rangeY);
        PlotStateKeeper.setLastDrawnIntXmin(ranX[0]);
        PlotStateKeeper.setLastDrawnIntXmax(ranX[1]);
        PlotStateKeeper.setLastDrawnIntYmin(ranY[0]);
        PlotStateKeeper.setLastDrawnIntYmax(ranY[1]);
        
        if (drawNew) {
            PlotStateKeeper.setIntXmax(ranX[1]);
            PlotStateKeeper.setIntYmax(ranY[1]);
        }
        
        PlotStateKeeper.setLastCallParams(par);

        return Arrays.asList(drawNowToThisGDBufferedPanel);
    }
    
    private static void drawPlotITS_LBUB(int width, int height, List<Double> lowerBound, List<Double> upperBound,
            boolean par, String lineStyle, String rangeX, String rangeY) {
        final String LOWER = Const.INPUT + Utils.getCounter();
        final String UPPER = Const.INPUT + Utils.getCounter();
        final int count = lowerBound.size();
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.assign(LOWER, Utils.listToArray(lowerBound));
        rengine.assign(UPPER, Utils.listToArray(upperBound));
        
        drawPlotITSNow(width, height, LOWER, UPPER, count, par, lineStyle, rangeX, rangeY);
        
        rengine.rm(LOWER, UPPER);
    }
    
    private static void drawPlotITS_CenterRadius(int width, int height, List<Double> center, List<Double> radius,
            boolean par, String lineStyle, String rangeX, String rangeY) {
        final String CENTER = Const.INPUT + Utils.getCounter();
        final String RADIUS = Const.INPUT + Utils.getCounter();
        final String LOWER = Const.INPUT + Utils.getCounter();
        final String UPPER = Const.INPUT + Utils.getCounter();
        final int count = center.size();
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.assign(CENTER, Utils.listToArray(center));
        rengine.assign(RADIUS, Utils.listToArray(radius));
        REXP getLower = rengine.eval(CENTER + " - " + RADIUS);
        REXP getUpper = rengine.eval(CENTER + " + " + RADIUS);
        rengine.assign(LOWER, getLower);
        rengine.assign(UPPER, getUpper);
        
        drawPlotITSNow(width, height, LOWER, UPPER, count, par, lineStyle, rangeX, rangeY);
        
        rengine.rm(CENTER, RADIUS, LOWER, UPPER);
    }
    
    private static void drawPlotITSNow(int width, int height, final String LOWER, final String UPPER, final int count,
            boolean par, String lineStyle, String rangeX, String rangeY) {
        MyRengine rengine = MyRengine.getRengine();
        String lim = "xlim = " + rangeX + ", ylim = " + rangeY;
        
        if (par) { //continue from the previous plot
            rengine.eval("par(new=TRUE)");
            //dont draw axes
            rengine.eval("plot.ts(" + LOWER + ", " + lim + ", type=\"n\", axes=FALSE, ann=FALSE, xaxt=\"n\")"); //hack
            rengine.eval("par(new=TRUE)");
            //here either
            rengine.eval("plot.ts(" + UPPER + ", " + lim + ", type=\"n\", axes=FALSE, ann=FALSE, xaxt=\"n\")");
            rengine.eval("segments(1:" + count + ", " + LOWER + ", 1:" + count + ", " + UPPER + ", " + lim + lineStyle + ")");
        } else { //start a new plot
            rengine.require("JavaGD");
            rengine.eval("JavaGD()"); // zacne novy plot
            rengine.eval("plot.ts(" + LOWER + ", " + lim + ", type=\"n\", axes=FALSE, ann=FALSE, xaxt=\"n\")"); //hack
            rengine.eval("par(new=TRUE)");
            //draw axes, since this is the first plot
            rengine.eval("plot.ts(" + UPPER + ", " + lim + ", type=\"n\", ylab=\"" +      ""      + "\", xaxt=\"n\")");
            rengine.eval("segments(1:" + count + ", " + LOWER + ", 1:" + count + ", " + UPPER + ", " + lim + lineStyle + ")");
        }
        
        
        
        drawNowToThisGDBufferedPanel.setSize(new Dimension(width, height));
        drawNowToThisGDBufferedPanel.initRefresh();
    }
    
    //TODO refactor - merge with drawPlotsITS, if possible
    public static List<JGDBufferedPanel> drawScatterPlotsITS(boolean drawNew, CallParamsDrawPlotsITS par) {
        final String LOWER = Const.INPUT + Utils.getCounter();
        final String UPPER = Const.INPUT + Utils.getCounter();
        final String CENTER = Const.INPUT + Utils.getCounter();
        final String RADIUS = Const.INPUT + Utils.getCounter();
        
        MyRengine rengine = MyRengine.getRengine();
        ColourService.getService().resetCounter();
        
        //first transform all LU(names) to CR(numbers):
        List<Double> allValsCenter = new ArrayList<>();
        List<Double> allValsRadius = new ArrayList<>();
        for (IntervalNamesLowerUpper namesLU : par.getListLowerUpper()) {
            List<Double> lowers = par.getDataTableModel().getDataForColname(namesLU.getLowerBound());
            List<Double> uppers = par.getDataTableModel().getDataForColname(namesLU.getUpperBound());
            rengine.assign(LOWER, Utils.listToArray(lowers));
            rengine.assign(UPPER, Utils.listToArray(uppers));
            rengine.eval(CENTER + " <- (" + UPPER + " + " + LOWER + ")/2");
            rengine.eval(RADIUS + " <- (" + UPPER + " - " + LOWER + ")/2");
            List<Double> centers = rengine.evalAndReturnList(CENTER);
            List<Double> radii = rengine.evalAndReturnList(RADIUS);
            allValsCenter.addAll(centers);
            allValsRadius.addAll(radii);
        }
        //then transform all CR names to CR numbers as well:
        for (IntervalNamesCentreRadius namesCR : par.getListCentreRadius()) {
            List<Double> centers = par.getDataTableModel().getDataForColname(namesCR.getCentre());
            List<Double> radii = par.getDataTableModel().getDataForColname(namesCR.getRadius());
            allValsCenter.addAll(centers);
            allValsRadius.addAll(radii);
        }
        
        //now we can compute the range
        String rangeCenter = getRangeYMultipleInterval(allValsCenter);
        String rangeRadius = getRangeYMultipleInterval(allValsRadius);
        
        return drawScatterPlotsITS(drawNew, par, rangeCenter, rangeRadius);
    }
    
    public static List<JGDBufferedPanel> drawScatterPlotsITS(boolean drawNew, CallParamsDrawPlotsITS par, String rangeCenter, String rangeRadius) {
        drawNowToThisGDBufferedPanel = new JGDBufferedPanel(par.getWidth(), par.getHeight());
        boolean next = false;
        for (IntervalNamesCentreRadius interval : par.getListCentreRadius()) {
            if (interval.getColourInPlot() == null) {
                interval.setColourInPlot(ColourService.getService().getNewColour());
            }
            
            String lineStyle = "col=\"" + interval.getColourInPlot() + "\"";
            drawScatterPlotITS_CenterRadius(par.getWidth(), par.getHeight(), par.getDataTableModel().getDataForColname(interval.getCentre()),
                    par.getDataTableModel().getDataForColname(interval.getRadius()), next, lineStyle, rangeCenter, rangeRadius);
            if (! next) {
                next = true;
            }
        }
        
        next = (! par.getListCentreRadius().isEmpty()) && (! par.getListLowerUpper().isEmpty());
        
        for (IntervalNamesLowerUpper interval : par.getListLowerUpper()) {
            if (interval.getColourInPlot() == null) {
                interval.setColourInPlot(ColourService.getService().getNewColour());
            }
            
            String lineStyle = "col=\"" + interval.getColourInPlot() + "\"";
            drawScatterPlotITS_LBUB(par.getWidth(), par.getHeight(), par.getDataTableModel().getDataForColname(interval.getLowerBound()),
                    par.getDataTableModel().getDataForColname(interval.getUpperBound()), next, lineStyle, rangeCenter, rangeRadius);
            if (! next) {
                next = true;
            }
        }
        
        //draw legend
        List<Plottable> plots = new ArrayList<>();
        plots.addAll(par.getListCentreRadius());
        plots.addAll(par.getListLowerUpper());
        drawLegend(par.getListPlotLegend(), plots, new PlotLegendSimpleListCellRenderer());
        
        MyRengine rengine = MyRengine.getRengine();
        double[] ranX = rengine.evalAndReturnArray(rangeCenter);
        double[] ranY = rengine.evalAndReturnArray(rangeRadius);
        PlotStateKeeper.setLastDrawnIntXmin(ranX[0]);
        PlotStateKeeper.setLastDrawnIntXmax(ranX[1]);
        PlotStateKeeper.setLastDrawnIntYmin(ranY[0]);
        PlotStateKeeper.setLastDrawnIntYmax(ranY[1]);
        
        if (drawNew) {
            PlotStateKeeper.setIntXmax(ranX[1]);
            PlotStateKeeper.setIntYmax(ranY[1]);
        }
        
        PlotStateKeeper.setLastCallParams(par);

        return Arrays.asList(drawNowToThisGDBufferedPanel);
    }
    
    private static void drawScatterPlotITS_LBUB(int width, int height, List<Double> lowerBound, List<Double> upperBound,
            boolean par, String lineStyle, String rangeX, String rangeY) {
        final String LOWER = Const.INPUT + Utils.getCounter();
        final String UPPER = Const.INPUT + Utils.getCounter();
        final String CENTER = Const.INPUT + Utils.getCounter();
        final String RADIUS = Const.INPUT + Utils.getCounter();
        final int count = lowerBound.size();
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.assign(LOWER, Utils.listToArray(lowerBound));
        rengine.assign(UPPER, Utils.listToArray(upperBound));
        rengine.eval(CENTER + " <- (" + UPPER + " + " + LOWER + ")/2");
        rengine.eval(RADIUS + " <- (" + UPPER + " - " + LOWER + ")/2");
        
        drawScatterPlotITSNow(width, height, CENTER, RADIUS, count, par, lineStyle, rangeX, rangeY);
    }
    
    private static void drawScatterPlotITS_CenterRadius(int width, int height, List<Double> center, List<Double> radius,
            boolean par, String lineStyle, String rangeX, String rangeY) {
        final String CENTER = Const.INPUT + Utils.getCounter();
        final String RADIUS = Const.INPUT + Utils.getCounter();
        final int count = center.size();
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.assign(CENTER, Utils.listToArray(center));
        rengine.assign(RADIUS, Utils.listToArray(radius));
        
        drawScatterPlotITSNow(width, height, CENTER, RADIUS, count, par, lineStyle, rangeX, rangeY);
    }
    
    private static void drawScatterPlotITSNow(int width, int height, final String CENTER, final String RADIUS, final int count,
            boolean par, String lineColour, String rangeX, String rangeY) {
        MyRengine rengine = MyRengine.getRengine();
        
        String lim = "xlim = " + rangeX + ", ylim = " + rangeY;
        
        if (par) { //continue from the previous plot
            rengine.eval("par(new=TRUE)");
            //don't draw axes
            rengine.eval("plot(" + CENTER + ", " + RADIUS + ", " + lim + ", " + lineColour + ", axes=FALSE, ann=FALSE)");
        } else { //start a new plot
            rengine.require("JavaGD");
            rengine.eval("JavaGD()"); // starts a new plot
            rengine.eval("plot(" + CENTER + ", " + RADIUS + ", " + lim + ", " + lineColour + ", xlab=\"Center\", ylab=\"Radius\")");
        }
        
        drawNowToThisGDBufferedPanel.setSize(new Dimension(width, height));
        drawNowToThisGDBufferedPanel.initRefresh();
    }
    
    
    
    //TODO refactor: merge with draw and drawScatterPlot?
    public static List<JGDBufferedPanel> drawScatterPlotMatrixITS(boolean drawNew, CallParamsDrawPlotsITS par) {
        final int start = Utils.getCounter();
        int counter = 0;
        MyRengine rengine = MyRengine.getRengine();
        StringBuilder labels = new StringBuilder();
        StringBuilder df = new StringBuilder();
        boolean next = false;
        for (IntervalNamesCentreRadius interval : par.getListCentreRadius()) {
            if (next) {
                labels.append(",");
                df.append(",");
            } else {
                next = true;
            }
            
            String CENTER = Const.INPUT + ".center." + (start+counter);
            String RADIUS = Const.INPUT + ".radius." + (start+counter);
            rengine.assign(CENTER, Utils.listToArray(par.getDataTableModel().getDataForColname(interval.getCentre())));
            rengine.assign(RADIUS, Utils.listToArray(par.getDataTableModel().getDataForColname(interval.getRadius())));

            labels.append("\"").append(interval.getCentre()).append("\"");
            labels.append(",");
            labels.append("\"").append(interval.getRadius()).append("\"");

            df.append(Const.INPUT).append(".center.").append(start+counter);
            df.append(",");
            df.append(Const.INPUT).append(".radius.").append(start+counter);
            
            counter++;
        }
        
        for (IntervalNamesLowerUpper interval : par.getListLowerUpper()) {
            if (next) {
                labels.append(",");
                df.append(",");
            } else {
                next = true;
            }

            String LOWER = Const.INPUT + ".lower." + (start+counter);
            String UPPER = Const.INPUT + ".upper." + (start+counter);
            rengine.assign(LOWER, Utils.listToArray(par.getDataTableModel().getDataForColname(interval.getLowerBound())));
            rengine.assign(UPPER, Utils.listToArray(par.getDataTableModel().getDataForColname(interval.getUpperBound())));

            labels.append("\"").append(interval.getLowerBound()).append("\"");
            labels.append(",");
            labels.append("\"").append(interval.getUpperBound()).append("\"");

            df.append(Const.INPUT).append(".lower.").append(start+counter);
            df.append(",");
            df.append(Const.INPUT).append(".upper.").append(start+counter);

            counter++;
        }
        
        drawNowToThisGDBufferedPanel = new JGDBufferedPanel(par.getWidth(), par.getHeight());
        rengine.require("JavaGD");
        rengine.require("psych");
        rengine.eval("JavaGD()"); // starts a new plot
        rengine.eval("pairs.panels(data.frame(" + df.toString() + ")"
                + ", labels=c(" + labels + "), smooth=FALSE, scale=FALSE, ellipses=FALSE, "
                + "hist.col=\"#777777\", col=\"#444444\", rug=FALSE)");
        
        drawNowToThisGDBufferedPanel.setSize(new Dimension(par.getWidth(), par.getHeight()));
        drawNowToThisGDBufferedPanel.initRefresh();
        
        PlotStateKeeper.setLastCallParams(par);

        return Arrays.asList(drawNowToThisGDBufferedPanel);
    }

    private static String getRangeYMultipleInterval(List<Double> allVals) {
        return "range(c(" + allVals.stream().map(d -> d.toString()).collect(Collectors.joining(", ")) + "))";
    }
    
    private static String getRangeXMultipleInterval(DataTableModel dataTableModel, List<IntervalNamesCentreRadius> intCRs, List<IntervalNamesLowerUpper> intLBUBs) {
        int maxLength = 0;
        for (IntervalNamesCentreRadius ncr : intCRs) {
            if (dataTableModel.getDataForColname(ncr.getCentre()).size() > maxLength) {
                maxLength = dataTableModel.getDataForColname(ncr.getCentre()).size();
            }
        }
        
        for (IntervalNamesLowerUpper nlu : intLBUBs) {
            if (dataTableModel.getDataForColname(nlu.getLowerBound()).size() > maxLength) {
                maxLength = dataTableModel.getDataForColname(nlu.getLowerBound()).size();
            }
        }
        
        return "range(c(1," + maxLength + "))";
    }

    private static List<Double> getAllVals(DataTableModel dataTableModel, List<IntervalNamesCentreRadius> listCentreRadius, List<IntervalNamesLowerUpper> listLowerUpper) {
        //TODO maybe later "optimize" a bit - no need to take all data from dataTableModel, just unique names... etc
        
        List<Double> allVals = new ArrayList<>();
        
        for (IntervalNamesCentreRadius cr : listCentreRadius) {
            List<Double> centers = dataTableModel.getDataForColname(cr.getCentre());
            List<Double> radii = dataTableModel.getDataForColname(cr.getRadius());
            
            for (int i = 0; i < centers.size(); i++) {
                allVals.add(centers.get(i) + radii.get(i));
                allVals.add(centers.get(i) - radii.get(i));
            }
        }
        
        for (IntervalNamesLowerUpper lu : listLowerUpper) {
            List<Double> lowers = dataTableModel.getDataForColname(lu.getLowerBound());
            List<Double> uppers = dataTableModel.getDataForColname(lu.getUpperBound());
            allVals.addAll(lowers);
            allVals.addAll(uppers);
        }
        
        return allVals;
    }
    
    private static String getRGBColorStringForHEX(String hexRColor, int alpha) {
        String base = "col2rgb(\"" + hexRColor + "\")";
        String red = base + "[\"red\",]";
        String green = base + "[\"green\",]";
        String blue = base + "[\"blue\",]";
        
        return "rgb(" + red + ", " + green + ", " + blue + ", alpha=" + alpha + ", maxColorValue=255)";
    }
    
    private static String getRangeYCrisp(List<Double> allData, List<TrainAndTestReportCrisp> reports) {
        StringBuilder rangesY = new StringBuilder("range(c(");
        boolean next = false;
        for (TrainAndTestReportCrisp r : reports) {
            if (next && (r.getFittedValues().length > 0)) {
                rangesY.append(", ");
            } else {
                next = true;
            }
            
            if (r.getFittedValues().length > 0) {
                rangesY.append(Utils.minArray(r.getFittedValues())).append(", ");
                rangesY.append(Utils.maxArray(r.getFittedValues()));
            }
            
            if (r.getForecastValuesTest().length > 0) {
                rangesY.append(", ");
                rangesY.append(Utils.minArray(r.getForecastValuesTest())).append(", ");
                rangesY.append(Utils.maxArray(r.getForecastValuesTest()));
            }
            
            if (r.getForecastValuesFuture().length > 0) {
                rangesY.append(", ");
                rangesY.append(Utils.minArray(r.getForecastValuesFuture())).append(", ");
                rangesY.append(Utils.maxArray(r.getForecastValuesFuture()));
            }
            
            if (r.getPredictionIntervalsLowers().length > 0) {
                rangesY.append(", ");
                rangesY.append(Utils.minArray(r.getPredictionIntervalsLowers())).append(", ");
                rangesY.append(Utils.maxArray(r.getPredictionIntervalsLowers()));
            }
            
            if (r.getPredictionIntervalsUppers().length > 0) {
                rangesY.append(", ");
                rangesY.append(Utils.minArray(r.getPredictionIntervalsUppers())).append(", ");
                rangesY.append(Utils.maxArray(r.getPredictionIntervalsUppers()));
            }
                    
        }
        //include the original data as well:
        if (rangesY.length() > 8) { //a very stupid and ugly way to see if nothing is there
            rangesY.append(", ");
        }
        rangesY.append(Utils.minList(allData)).append(", ").append(Utils.maxList(allData));
        rangesY.append("))");
        
        return rangesY.toString();
    }
    
    private static String getRangeYInterval(List<TrainAndTestReportInterval> reports) {
        StringBuilder rangesY = new StringBuilder("range(c(");
        boolean next = false;
        for (TrainAndTestReportInterval r : reports) {
            if (next && (! r.getFittedValues().isEmpty())) {
                rangesY.append(", ");
            } else {
                next = true;
            }
            
            if (! r.getFittedValues().isEmpty()) {
                rangesY.append(Utils.minArray(r.getFittedValuesLowers())).append(", ");
                rangesY.append(Utils.maxArray(r.getFittedValuesLowers())).append(", ");
                rangesY.append(Utils.minArray(r.getFittedValuesUppers())).append(", ");
                rangesY.append(Utils.maxArray(r.getFittedValuesUppers()));
            }
            
            if (! r.getForecastValuesTest().isEmpty()) {
                rangesY.append(", ");
                rangesY.append(Utils.minArray(r.getForecastValuesTestLowers())).append(", ");
                rangesY.append(Utils.maxArray(r.getForecastValuesTestLowers())).append(", ");
                rangesY.append(Utils.minArray(r.getForecastValuesTestUppers())).append(", ");
                rangesY.append(Utils.maxArray(r.getForecastValuesTestUppers()));
            }
            if (! r.getForecastValuesFuture().isEmpty()) {
                rangesY.append(", ");
                rangesY.append(Utils.minArray(r.getForecastValuesFutureLowers())).append(", ");
                rangesY.append(Utils.maxArray(r.getForecastValuesFutureLowers())).append(", ");
                rangesY.append(Utils.minArray(r.getForecastValuesFutureUppers())).append(", ");
                rangesY.append(Utils.maxArray(r.getForecastValuesFutureUppers()));
            }
            
            //include the original data as well:
            List<Double> realDataLower = r.getRealValuesLowers();
            List<Double> realDataUpper = r.getRealValuesUppers();
            
            if (rangesY.length() > 8) { //a very stupid and ugly way to see if nothing is there
                rangesY.append(", ");
            }
            
            rangesY.append(Utils.minList(realDataLower)).append(", ").append(Utils.maxList(realDataLower));
            rangesY.append(", ").append(Utils.minList(realDataUpper)).append(", ").append(Utils.maxList(realDataUpper));
        }
        
        rangesY.append("))");
        
        return rangesY.toString();
    }
    
    private static String getRangeXCrisp(List<Double> allData, int numForecasts, int from, int to) {
        String rangeX = "range(c(1, " + (allData.size() + numForecasts) + "))";
        //works with allData.size, because allData isn't cropped by fromTo
        return "range(c(max(" + from + "," + rangeX + "[1]), min(" + (to+numForecasts) + "," + rangeX + "[2])))";
    }
    
    private static String getRangeXInterval(int sizeDataWithoutFromToCrop, int numForecasts, int from, int to) {
        String rangesX = "range(c(1, " + (sizeDataWithoutFromToCrop + numForecasts) + "))";

        return "range(c(max(" + from + "," + rangesX + "[1]), min(" + (to+numForecasts) + "," + rangesX + "[2])))";
    }
    
    public static String getRString(List<String> list) {
        return "c(" + list.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")) + ")";
    }
    
    public static List<JGDBufferedPanel> drawDiagrams(int width, int height, List<TrainAndTestReport> reports) {
        //first get all the diagrams
        List<String> diagramPlots = new ArrayList<>();
        for (TrainAndTestReport r : reports) {
            if (! "".equals(r.getNnDiagramPlotCode())) {
                if (r.getNnDiagramPlotCode().contains(";")) {
                    //hack TODO chg
                    String firstPlot = r.getNnDiagramPlotCode().split(";")[0];
                    StringBuilder diagramPlotCode = new StringBuilder(firstPlot);
                    diagramPlotCode.insert(firstPlot.length() - 1, ", main=\"" + r.toString() + " (Center)\"");
                    diagramPlots.add(diagramPlotCode.toString());
                    
                    String secondPlot = r.getNnDiagramPlotCode().split(";")[1];
                    diagramPlotCode = new StringBuilder(secondPlot);
                    diagramPlotCode.insert(secondPlot.length() - 1, ", main=\"" + r.toString() + " (Radius)\"");
                    diagramPlots.add(diagramPlotCode.toString());
                } else {
                    StringBuilder diagramPlotCode = new StringBuilder(r.getNnDiagramPlotCode());
                    diagramPlotCode.insert(r.getNnDiagramPlotCode().length() - 1, ", main=\"" + r.toString() + "\"");
                    diagramPlots.add(diagramPlotCode.toString());
                }
            }
        }
        
        return drawToGrid(width, height, diagramPlots, COLUMNS_DIAGRAMSNN, ROWS_DIAGRAMSNN);
    }
    
    public static List<JGDBufferedPanel> drawToGrid(int width, int height, List<String> plots, int maxCol, int maxRow) {
        List<JGDBufferedPanel> panelsList = new ArrayList<>();
        
        //TODO chg so that the last grid, if not full, shows bigger plots?
        //i.e. I'm left with n plots. x = ceil(sqrt(n)). then the resulting rectgl = x*(x-1) if enough for n, else x*x

        if (plots.isEmpty()) {
            return panelsList;
        } else {
            MyRengine rengine = MyRengine.getRengine();
            rengine.require("JavaGD");
            
            int currentIndex = 0;
            while (currentIndex < plots.size()) {
                JGDBufferedPanel panel = new JGDBufferedPanel(width, height);
                drawNowToThisGDBufferedPanel = panel;
                rengine.eval("JavaGD()");
                rengine.eval("par(mfrow=c(" + maxRow + "," + maxCol + "))"); //create the grid
                
                int counter = maxCol*maxRow;
                while ((counter > 0) && (currentIndex < plots.size())) {
                    rengine.eval(plots.get(currentIndex));
                    
                    currentIndex++;
                    counter--;
                }
                
                        
                
                while ((counter > 0) && (currentIndex == plots.size())) { //I'm out of diagrams and the grid is not full yet
                    rengine.eval("plot.new()");
                    counter--;
                }
                
                drawNowToThisGDBufferedPanel.setSize(new Dimension(width, height));
                drawNowToThisGDBufferedPanel.initRefresh();
                
                panelsList.add(panel);
            }
        }
        
        return panelsList;
    }
    
    public static void drawLegend(final JList<RightClickable> listPlotLegend, List<Plottable> plots, ListCellRenderer cellRenderer) {
        ((DefaultListModel)(listPlotLegend.getModel())).removeAllElements();
        
        listPlotLegend.setCellRenderer(cellRenderer);

        for (Plottable p : plots) {
            if (p.getColourInPlot() != null) {
                RightClickable element;
                if (cellRenderer instanceof PlotLegendTurnOFFableListCellRenderer) {
                    element = new PlotLegendTurnOFFableListElement(p, listPlotLegend);
                } else {
                    element = new PlotLegendSimpleListElement(p, listPlotLegend);
                }

                ((DefaultListModel<RightClickable>)(listPlotLegend.getModel())).addElement(element);
            }
        }
        
        listPlotLegend.repaint();
    }

    public static List<JGDBufferedPanel> drawSimpleFctionToGrid(String plottingFunction, List<String> selectedValuesList,
            DataTableModel dataTableModel, int width, int height) throws IllegalArgumentException {
        //first get all the diagrams
        List<String> diagramsPlots = new ArrayList<>();
        MyRengine rengine = MyRengine.getRengine();
        
        for (String selectedVal : selectedValuesList) {
            String NAME = "myplots." + Utils.getCounter();
            rengine.assign(NAME, Utils.listToArray(dataTableModel.getDataForColname(selectedVal)));
            String plotFunction;
            if ("qqnorm".equals(plottingFunction)) {
                plotFunction = plottingFunction + "(" + NAME + ", main=\"" + selectedVal + "\")";
                plotFunction += ";qqline(" + NAME + ")";
            } else {
                plotFunction = plottingFunction + "(" + NAME + ", xlab=\"" + selectedVal + "\", main=\"\")";
            }
            diagramsPlots.add(plotFunction);
        }
        
        //then output them to a grid and return it
        return drawToGrid(width, height, diagramsPlots, COLUMNS_BOXHIST, ROWS_BOXHIST);
    }

    public static List<JGDBufferedPanel> drawBayesToGrid(List<String> diagramsPlots, int width, int height) throws IllegalArgumentException {
        //output the plots to a grid
        return drawToGrid(width, height, diagramsPlots, 1, 1);
    }

    private static String getRangeYCrisp(Collection<List<Double>> values) {
        StringBuilder rangesY = new StringBuilder("range(c(");
        boolean next = false;
        for (List<Double> l : values) {
            if (next && (l.size() > 0)) {
                rangesY.append(", ");
            } else {
                next = true;
            }
            
            rangesY.append(Utils.minArray(Utils.listToArray(l))).append(", ");
            rangesY.append(Utils.maxArray(Utils.listToArray(l)));
        }
        rangesY.append("))");
        
        return rangesY.toString();
    }
    
    private static String getRangeXCrisp(DataTableModel dataTableModel, List<IntervalNamesCentreRadius> intCRs, List<IntervalNamesLowerUpper> intLBUBs) {
        int maxLength = 0;
        for (IntervalNamesCentreRadius ncr : intCRs) {
            if (dataTableModel.getDataForColname(ncr.getCentre()).size() > maxLength) {
                maxLength = dataTableModel.getDataForColname(ncr.getCentre()).size();
            }
        }
        
        for (IntervalNamesLowerUpper nlu : intLBUBs) {
            if (dataTableModel.getDataForColname(nlu.getLowerBound()).size() > maxLength) {
                maxLength = dataTableModel.getDataForColname(nlu.getLowerBound()).size();
            }
        }
        
        return "range(c(1," + maxLength + "))";
    }


    public static List<JGDBufferedPanel> drawPlotGeneral(boolean drawNew, String plotFunction, String additionalArgs,
                                                         List<DefaultPlottable> plottables, JList<RightClickable> listPlotLegend,
                                                         int width, int height) {
        //get the Y range first (assuming X is the same)
        StringBuilder rangeYStringBuilder = new StringBuilder("range(c(");
        List<String> chunks = new ArrayList<>();
        for (DefaultPlottable col : plottables) {
            String chunk = DataTableModel.getInstance().getDataForColname(col.getColname())
                    .stream()
                    .filter(d -> !(d.isNaN()))
                    .map(d -> d.toString())
                    .collect(Collectors.joining(", "));
            chunks.add(chunk);
        }
        rangeYStringBuilder.append(chunks.stream().collect(Collectors.joining(", ")));
        rangeYStringBuilder.append("))");
        String rangeY = rangeYStringBuilder.toString();
        String rangeX = "range(c(0, " + DataTableModel.getInstance().getRowCount() + "))";

        if ("acf".equals(plotFunction) || "pacf".equals(plotFunction)) {
            rangeY = "range(c(-1,1))";

            //String rangeX = "range(c(0, " + getRowCount() + "))";
            //default lagmax: 10*log10(N)
            rangeX = "range(c(0,10*log10(" + DataTableModel.getInstance().getRowCount() + ")))";
        }

        return drawPlotGeneral(drawNew, new CallParamsDrawPlotGeneral(
                listPlotLegend, width, height, plottables, plotFunction, additionalArgs), rangeX, rangeY);
    }
    
    public static List<JGDBufferedPanel> drawPlotGeneral(boolean drawNew, CallParamsDrawPlotGeneral par, String rangeX, String rangeY) {
        drawNowToThisGDBufferedPanel = new JGDBufferedPanel(par.getWidth(), par.getHeight());
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("JavaGD");
        rengine.eval("JavaGD()");
        
        ColourService.getService().resetCounter();
        
        boolean next = false;
        List<Plottable> plots = new ArrayList<>();
        for (DefaultPlottable col : par.getColnames()) {
            if (col.getColourInPlot() == null) {
                col.setColourInPlot(ColourService.getService().getNewColour());
            }
            
            List<Double> data = DataTableModel.getInstance().getDataForColname(col.getColname());
            final String TRAINDATA = Const.TRAINDATA + Utils.getCounter();
            rengine.assign(TRAINDATA, Utils.listToArray(data));
            if (next) {
                rengine.eval("par(new=TRUE)");
                rengine.eval(par.getPlotFunction() + "(" + TRAINDATA + par.getAdditionalArgs() + ", "
                        + "axes=FALSE, ann=FALSE, "
                        + "xlim=" + rangeX + ", ylim=" + rangeY + ", lwd=2, col=\"" + col.getColourInPlot() + "\")");
            } else {
                next = true;
                String plot = par.getPlotFunction() + "(" + TRAINDATA + par.getAdditionalArgs() + ", " + "ylab=NULL, ";
                if ((! par.getPlotFunction().equals("acf")) && (! par.getPlotFunction().equals("pacf"))) {
                    plot += "xaxt=\"n\", "; //suppress X axis
                }
                plot += "xlim=" + rangeX + ", ylim=" + rangeY + ", lwd=2, col=\"" + col.getColourInPlot() + "\")";
                
                rengine.eval(plot);
                
                if ((! par.getPlotFunction().equals("acf")) && (! par.getPlotFunction().equals("pacf"))) {
                    rengine.eval("axis(1,at=seq(1,length(" + LABELS_AXIS_X + ")),labels=" + LABELS_AXIS_X + ",las=2)");
                }
            }
            
            plots.add(col);
        }
        
        //add legend
        PlotDrawer.drawLegend(par.getListPlotLegend(), plots, new PlotLegendSimpleListCellRenderer());
        
        double[] ranX = rengine.evalAndReturnArray(rangeX);
        double[] ranY = rengine.evalAndReturnArray(rangeY);
        PlotStateKeeper.setLastDrawnCrispXmin(ranX[0]);
        PlotStateKeeper.setLastDrawnCrispXmax(ranX[1]);
        PlotStateKeeper.setLastDrawnCrispYmin(ranY[0]);
        PlotStateKeeper.setLastDrawnCrispYmax(ranY[1]);
        
        if (drawNew) {
            PlotStateKeeper.setCrispXmax(ranX[1]);
            PlotStateKeeper.setCrispYmax(ranY[1]);
        }
        
        PlotStateKeeper.setLastCallParams(par);
        
        // R always draws a plot of a default size to the JavaGD device.
        // But our GDBufferedPanel is supposed to have a different size, so
        // we have to resize it back to the size we want it to have.
        drawNowToThisGDBufferedPanel.setSize(new Dimension(par.getWidth(), par.getHeight())); //TODO won't shrink under a threshold, fix
        drawNowToThisGDBufferedPanel.initRefresh();

        return Arrays.asList(drawNowToThisGDBufferedPanel);
    }

    public static List<JGDBufferedPanel> drawScreePlot(List<String> selectedValuesList, int width, int height) {
        MyRengine rengine = MyRengine.getRengine();

        rengine.require("psych");
        
        final String INPUT = rengine.createDataFrame(selectedValuesList);
        
        List<String> plots = new ArrayList<>();
        plots.add("scree(cor(" + INPUT + "), factors=FALSE)");
        
        List<JGDBufferedPanel> panels = drawToGrid(width, height, plots, 1, 1);

        rengine.rm(INPUT);

        return panels;
    }

    public static JGDBufferedPanel getDrawNowToThisGDBufferedPanel() {
        return drawNowToThisGDBufferedPanel;
    }
}
