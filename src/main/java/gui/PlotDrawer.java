package gui;

import gui.renderers.PlotLegendSimpleListCellRenderer;
import gui.renderers.PlotLegendSimpleListElement;
import gui.renderers.PlotLegendTurnOFFableListCellRenderer;
import gui.renderers.PlotLegendTurnOFFableListElement;
import gui.tablemodels.DataTableModel;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import models.avg.Average;
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

import static gui.tablemodels.DataTableModel.LABELS_AXIS_X;

public class PlotDrawer {
    //TODO toto by cele chcelo upratat, prekopar, mozno refaktorovat do viacerych tried
    //a pridat sem vsetko, co sa tyka kreslenia - napr. i v DataTableModel je nieco, mozno v MainFrame, a tak.

    private static final Logger logger = LoggerFactory.getLogger(PlotDrawer.class);
    
    private static final int COLUMNS_DIAGRAMSNN = 3;
    private static final int ROWS_DIAGRAMSNN = 3;
    private static final int COLUMNS_BOXHIST = 2;
    private static final int ROWS_BOXHIST = 2;
    
    private static JGDBufferedPanel drawNowToThisGDBufferedPanel;
    
    //drawNew je true, ak sa maju zmenit maximalne medze obrazku, tj kresli sa to z Run, Plot CTS, Plot ITS, ACF, PACF
    //drawNew je false, ak sa len zoomuje aktualny obrazok a nekresli sa novy, tj zo Zoom CTS, Zoom ITS
    //refreshOnly je true, ak pridavam/mazem niektore ploty z legendy. false, ak sa to spusta "nacisto" hned po Run alebo Zoom
    //return ploty, ktore sa prave pridali, takze AVG
    public static List<TrainAndTestReport> drawPlots(boolean drawNew, boolean refreshOnly, CallParamsDrawPlots par) {
        String rangeXCrisp = "";
        String rangeYCrisp = "";
        String rangeXInt = "";
        String rangeYInt = "";
        
        if (! par.getReportsCTS().isEmpty()) {
            rangeXCrisp = getRangeXCrisp(par.getAllDataCTS(), par.getNumForecasts(), par.getFrom(), par.getTo());
            rangeYCrisp = getRangeYCrisp(par.getAllDataCTS(), par.getReportsCTS());
        }
        
        if (! par.getReportsITS().isEmpty()) {
            rangeXInt = getRangeXInterval(par.getSizeDataWithoutFromToCrop(), par.getNumForecasts(), par.getFrom(), par.getTo());
            List<TrainAndTestReportInterval> reportsIntTS = deleteEmpty(par.getReportsITS());
            par.setReportsITS(reportsIntTS);
            rangeYInt = getRangeYInterval(par.getReportsITS());
        }
        
        return drawPlots(drawNew, refreshOnly, par, rangeXCrisp, rangeYCrisp, rangeXInt, rangeYInt);
    }
    
    public static List<TrainAndTestReport> drawPlots(boolean drawNew, final boolean refreshOnly, CallParamsDrawPlots par,
                                 String rangeXCrisp, String rangeYCrisp, String rangeXInt, String rangeYInt) {
        List<TrainAndTestReport> addedReports = new ArrayList<>();
        
        if (par.getReportsCTS().isEmpty() && par.getReportsITS().isEmpty()) {
            return addedReports;
        }
        
        JGDBufferedPanel canvasToUse = par.getCanvasToUse();
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
        
        //compute all averages:
        logger.info("--------Computing averages");
        List<TrainAndTestReportCrisp> avgReportsCrisp = new ArrayList<>();
        List<TrainAndTestReportInterval> avgReportsInt = new ArrayList<>();
        try {
            for (Average av : par.getAvgConfig().getAvgs()) {
                avgReportsCrisp.addAll(av.computeAllCTSAvgs(reportsCTS));
                avgReportsInt.addAll(av.computeAllIntTSAvgs(reportsIntTS));
            }
        } catch (IllegalArgumentException e) {
            if (! refreshOnly) {
                JOptionPane.showMessageDialog(null, "The average of all methods will not be computed due to the differences in training and testing sets among the methods.");
            } //otherwise they've already seen the error and it'd be annoying
        }
        addedReports.addAll(avgReportsCrisp);
        addedReports.addAll(avgReportsInt);
        
        //then proceed to drawing
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("JavaGD");
        
        drawNowToThisGDBufferedPanel = canvasToUse;
        rengine.eval("JavaGD()");
        
        if ((! reportsCTS.isEmpty()) && (! reportsIntTS.isEmpty())) { //budem vykreslovat oba naraz
            rengine.eval("par(mfrow=c(1,2))"); //daj dva grafy vedla seba. potom normalne zavolat dva ploty.
        }
        
        if (! reportsCTS.isEmpty()) { //plot CTS
            boolean wasSthDrawnCrisp = false;
            
            List<TrainAndTestReportCrisp> whatToDrawNow = new ArrayList<>();
            if (par.getAvgConfig().isAvgONLY()) {
                whatToDrawNow = avgReportsCrisp;
            } else {
                whatToDrawNow.addAll(reportsCTS);
                whatToDrawNow.addAll(avgReportsCrisp);
            }
            //teraz nakresli vsetko
            for (TrainAndTestReportCrisp r : whatToDrawNow) {
                if (! r.isVisible()) { //skip those that are turned off
                    if (r.getColourInPlot() == null) {
                        ColourService.getService().getNewColour(); //but discard the colour as if you drew them so that the avg had the same colour
                    }
                    //do not discard the colour in case it was given, i.e. no new colour would have been asked for anyway
                    continue;
                }
                
                if (wasSthDrawnCrisp) {
                    rengine.eval("par(new=TRUE)");
                }
                
                String colourToUseNow;
                if (refreshOnly || (r.getColourInPlot() != null)) { //get the colour that was used previously
                    colourToUseNow = r.getColourInPlot();
                } else { //else get a new one
                    colourToUseNow = ColourService.getService().getNewColour();
                }
                
                StringBuilder plotCode = new StringBuilder(r.getPlotCode());
                plotCode.insert(r.getPlotCode().length() - 1, ", xlim = " + rangeXCrisp + ", ylim = " + rangeYCrisp + ", "
                        + "axes=FALSE, ann=FALSE, " //suppress axes names and labels, just draw them for the main data
                        + "lwd=4, col=\"" + colourToUseNow + "\"");
                plotCode.insert(10, "rep(NA, " + par.getFrom() + "), "); //hack - posunutie

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
                               + "density=NA, col=" + getRGBColorStringForHEX(colourToUseNow, 30) 
                               + ", border=NA)"); //TODO col podla aktualnej
                    
                    rengine.rm(UPPERS, LOWERS_REVERSE);
                }

                //add a dashed vertical line to separate test and train
                rengine.eval("abline(v = " + (r.getNumTrainingEntries() + par.getFrom()) + ", lty = 2, lwd=2, col=\"" + colourToUseNow + "\")");
                if ((! refreshOnly) && (r.getColourInPlot() == null)) {
                    r.setColourInPlot(colourToUseNow);
                }
            }
            
            //teraz by mali byt nakreslene vsetky ciarky aj priemery
            
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
                PlotStateKeeper.setCrispXmin(par.getFrom());
                PlotStateKeeper.setCrispXmax(par.getSizeDataWithoutFromToCrop() + numForecasts);
                PlotStateKeeper.setCrispYmin(rangeY[0]);
                PlotStateKeeper.setCrispYmax(rangeY[1]);
            }
            
            rengine.rm("all.data");
        }
        
        if (! reportsIntTS.isEmpty()) { //plot ITS
            boolean wasSthDrawnIntTS = false;
            
            List<TrainAndTestReportInterval> whatToDrawNow = new ArrayList<>();
            if (par.getAvgConfig().isAvgONLY()) {
                whatToDrawNow = avgReportsInt;
            } else {
                whatToDrawNow.addAll(reportsIntTS);
                whatToDrawNow.addAll(avgReportsInt);
            }
            
            //now draw
            for (TrainAndTestReportInterval r : whatToDrawNow) {
                if (! r.isVisible()) { //skip those that are turned off
                    if (r.getColourInPlot() == null) {
                        ColourService.getService().getNewColour(); //but discard the colour as if you drew them so that the avg had the same colour
                    }
                    //do not discard the colour in case it was given, i.e. no new colour would have been asked for anyway
                    continue;
                }
                
                if (wasSthDrawnIntTS) {
                    rengine.eval("par(new=TRUE)");
                }
                
                String colourToUseNow;
                if (refreshOnly || (r.getColourInPlot() != null)) { //get the colour that was used previously
                    colourToUseNow = r.getColourInPlot();
                } else { //else get a new one
                    colourToUseNow = ColourService.getService().getNewColour();
                }

                //naplotovat fitted values:
                final int sizeFitted = r.getFittedValues().size();
                rengine.assign("lower", r.getFittedValuesLowers());
                rengine.assign("upper", r.getFittedValuesUppers());
                //hack - posunutie
                rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), lower), type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                rengine.eval("par(new=TRUE)");
                //hack - posunutie
                rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), upper), type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                rengine.eval("segments(" + (1+par.getFrom()) + ":" + (sizeFitted+par.getFrom()) + ", lower, " + (1+par.getFrom()) + ":" + (sizeFitted+par.getFrom()) + ", upper, xlim = "
                        + rangeXInt + ", ylim = " + rangeYInt + ", lwd=4, col=\"" + colourToUseNow + "\")");

                //naplotovat fitted values pre training data:
                final int sizeForecastTest = r.getForecastValuesTest().size();
                rengine.eval("par(new=TRUE)");
                rengine.assign("lower", r.getForecastValuesTestLowers());
                rengine.assign("upper", r.getForecastValuesTestUppers());
                //hack - posunutie
                rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), lower), type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                rengine.eval("par(new=TRUE)");
                //hack - posunutie
                rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), upper), type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                rengine.eval("segments(" + (sizeFitted+1+par.getFrom()) + ":" + (sizeFitted+sizeForecastTest+par.getFrom()) + ", lower, "
                        + (sizeFitted+1+par.getFrom()) + ":" + (sizeFitted+sizeForecastTest+par.getFrom()) + ", upper, xlim = " + rangeXInt
                        + ", ylim = " + rangeYInt + ", lwd=4, col=\"" + colourToUseNow + "\")");

                //naplotovat forecasty buduce:
                final int sizeForecastFuture = r.getForecastValuesFuture().size();
                if (sizeForecastFuture > 0) {
                    rengine.eval("par(new=TRUE)");
                    rengine.assign("lower", r.getForecastValuesFutureLowers());
                    rengine.assign("upper", r.getForecastValuesFutureUppers());
                    //hack - posunutie
                    rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), lower), type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                            + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                    rengine.eval("par(new=TRUE)");
                    //hack - posunutie
                    rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), upper), type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                            + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                    rengine.eval("segments(" + (sizeFitted+sizeForecastTest+1+par.getFrom()) + ":"
                            + (sizeFitted+sizeForecastTest+sizeForecastFuture+par.getFrom()) + ", lower, "
                            + (sizeFitted+sizeForecastTest+1+par.getFrom()) + ":" + (sizeFitted+sizeForecastTest+sizeForecastFuture+par.getFrom())
                            + ", upper, xlim = " + rangeXInt + ", ylim = " + rangeYInt
                            + ", lwd=4, col=\"" + colourToUseNow + "\")");
                }

                //add a dashed vertical line to separate test and train
                rengine.eval("abline(v = " + (sizeFitted+par.getFrom()) + ", lty = 2, lwd=2, col=\"" + colourToUseNow + "\")");

                wasSthDrawnIntTS = true;

                if ((! refreshOnly) && (r.getColourInPlot() == null)) {
                    r.setColourInPlot(colourToUseNow);
                }
            }
            
            rengine.rm("lower", "upper");
            
            //tu by mali byt nakreslene vsetky ITS aj s priemermi
            
            if (wasSthDrawnIntTS) { //inak to moze skocit vedla do CTS plotu
                rengine.eval("par(new=TRUE)");
            }
                
            //a na ne vsetky naplotovat realne data:
            //TODO hack, zatial beriem data z prveho reportu. potom nejak vymysliet :(
            int size = reportsIntTS.get(reportsIntTS.size() - 1).getRealValuesLowers().size();
            //tu ich uz nesublistuj! v realData v reporte je to uz orezane podla range zadaneho na vstupe
            rengine.assign("all.lower", Utils.listToArray(reportsIntTS.get(reportsIntTS.size() - 1).getRealValuesLowers()));
            rengine.assign("all.upper", Utils.listToArray(reportsIntTS.get(reportsIntTS.size() - 1).getRealValuesUppers()));

            //TODO este sa pohrat s tymi "range" hodnotami, lebo mi to nejak divne zarovnava
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
                PlotStateKeeper.setIntXmin(par.getFrom());
                PlotStateKeeper.setIntXmax(par.getSizeDataWithoutFromToCrop() + numForecasts);
                PlotStateKeeper.setIntYmin(rangeY[0]);
                PlotStateKeeper.setIntYmax(rangeY[1]);
            }
            
            rengine.rm("all.lower", "all.upper");
        }
        
        drawNowToThisGDBufferedPanel.setSize(new Dimension(width, height)); //TODO nechce sa zmensit pod urcitu velkost, vymysliet
        drawNowToThisGDBufferedPanel.initRefresh();
        
        if (! refreshOnly) {
            PlotStateKeeper.setLastCallParams(par);
            //and draw the legend
            List<Plottable> allReports = new ArrayList<>();
            allReports.addAll(reportsCTS);
            allReports.addAll(reportsIntTS);
            allReports.addAll(addedReports);
            drawLegend(par.getListPlotLegend(), allReports, addedReports, new PlotLegendTurnOFFableListCellRenderer(),
                    rangeXCrisp, rangeYCrisp, rangeXInt, rangeYInt);
        }
        
        return addedReports;
    }
    
    public static List<BasicStats> drawPlotsResiduals(Map<String, List<Double>> residuals, JList listPlotLegendResiduals, 
            JGDBufferedPanel canvasToUse, int width, int height, String plotFunction) {
        //TODO refactor... this is again just copy+paste+change
        List<BasicStats> basicStatss = new ArrayList<>();
        
        if (residuals.isEmpty()) {
            return basicStatss;
        }
        
        String rangeXCrisp = "range(c(1, " + (residuals.get(residuals.keySet().toArray(new String[]{})[0]).size()) + "))"; //hack jak svina
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

        drawNowToThisGDBufferedPanel.setSize(new Dimension(width, height)); //TODO nechce sa zmensit pod urcitu velkost, vymysliet
        drawNowToThisGDBufferedPanel.initRefresh();
        
        listPlotLegendResiduals.setCellRenderer(new PlotLegendSimpleListCellRenderer());
        listPlotLegendResiduals.repaint();
        
        return basicStatss;
    }
    
    public static void drawPlotsITS(boolean drawNew, CallParamsDrawPlotsITS par) {
        List<Double> allVals = getAllVals(par.getDataTableModel(), par.getListCentreRadius(), par.getListLowerUpper());
//        String rangeX = ; //predpokladajme, ze vsetky maju rovnaky pocet pozorovani
        String rangeY = getRangeYMultipleInterval(allVals);
        String rangeX = getRangeXMultipleInterval(par.getDataTableModel(), par.getListCentreRadius(), par.getListLowerUpper());
        
        drawPlotsITS(drawNew, par, rangeX, rangeY);
    }
    
    public static void drawPlotsITS(boolean drawNew, CallParamsDrawPlotsITS par, String rangeX, String rangeY) {
        drawNowToThisGDBufferedPanel = par.getCanvasToUse();
        
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
        
        next = (! par.getListCentreRadius().isEmpty()) && (! par.getListLowerUpper().isEmpty()); //true ak je nieco v CenRad aj v LBUB
        
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
    
    //TODO refaktor - spojit nejak s drawPlotsITS, ak to ide?
    public static void drawScatterPlotsITS(boolean drawNew, CallParamsDrawPlotsITS par) {
        final String LOWER = Const.INPUT + Utils.getCounter();
        final String UPPER = Const.INPUT + Utils.getCounter();
        final String CENTER = Const.INPUT + Utils.getCounter();
        final String RADIUS = Const.INPUT + Utils.getCounter();
        
        MyRengine rengine = MyRengine.getRengine();
        ColourService.getService().resetCounter();
        
        //ako prve prerobit vsetky LU(mena) na CR(cisla):
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
        //potom prerobit aj vsetky CR mena na CR cisla:
        for (IntervalNamesCentreRadius namesCR : par.getListCentreRadius()) {
            List<Double> centers = par.getDataTableModel().getDataForColname(namesCR.getCentre());
            List<Double> radii = par.getDataTableModel().getDataForColname(namesCR.getRadius());
            allValsCenter.addAll(centers);
            allValsRadius.addAll(radii);
        }
        
        //teraz viem spocitat range
        String rangeCenter = getRangeYMultipleInterval(allValsCenter);
        String rangeRadius = getRangeYMultipleInterval(allValsRadius);
        
        drawScatterPlotsITS(drawNew, par, rangeCenter, rangeRadius);
    }
    
    public static void drawScatterPlotsITS(boolean drawNew, CallParamsDrawPlotsITS par, String rangeCenter, String rangeRadius) {
        drawNowToThisGDBufferedPanel = par.getCanvasToUse();
        boolean next = false;
        for (IntervalNamesCentreRadius interval : par.getListCentreRadius()) {
            String colour = ColourService.getService().getNewColour();
            //remember the colour for the legend
            interval.setColourInPlot(colour);
            
            String lineStyle = "col=\"" + colour + "\"";
            drawScatterPlotITS_CenterRadius(par.getWidth(), par.getHeight(), par.getDataTableModel().getDataForColname(interval.getCentre()),
                    par.getDataTableModel().getDataForColname(interval.getRadius()), next, lineStyle, rangeCenter, rangeRadius);
            if (! next) {
                next = true;
            }
        }
        
        next = (! par.getListCentreRadius().isEmpty()) && (! par.getListLowerUpper().isEmpty()); //true ak je nieco v CenRad aj v LBUB
        
        for (IntervalNamesLowerUpper interval : par.getListLowerUpper()) {
            String colour = ColourService.getService().getNewColour();
            interval.setColourInPlot(colour);
            
            String lineStyle = "col=\"" + colour + "\"";
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
            rengine.eval("JavaGD()"); // zacne novy plot
            rengine.eval("plot(" + CENTER + ", " + RADIUS + ", " + lim + ", " + lineColour + ", xlab=\"Center\", ylab=\"Radius\")");
        }
        
        drawNowToThisGDBufferedPanel.setSize(new Dimension(width, height));
        drawNowToThisGDBufferedPanel.initRefresh();
    }
    
    
    
    //TODO refaktor: spojit s draw a drawScatterPlot (iba nejaky prepinac..)
    public static void drawScatterPlotMatrixITS(boolean drawNew, CallParamsDrawPlotsITS par) {
        final int start = Utils.getCounter();
        int counter = 0;
        MyRengine rengine = MyRengine.getRengine();
        StringBuilder formula = new StringBuilder("~");
        StringBuilder labels = new StringBuilder("labels=c(");
        boolean next = false;
        for (IntervalNamesCentreRadius interval : par.getListCentreRadius()) {
            if (next) {
                formula.append("+");
                labels.append(",");
            } else {
                next = true;
            }
            
            String CENTER = Const.INPUT + ".center." + (start+counter);
            String RADIUS = Const.INPUT + ".radius." + (start+counter);
            rengine.assign(CENTER, Utils.listToArray(par.getDataTableModel().getDataForColname(interval.getCentre())));
            rengine.assign(RADIUS, Utils.listToArray(par.getDataTableModel().getDataForColname(interval.getRadius())));
            
            formula.append(Const.INPUT).append(".center.").append(start+counter);
            formula.append("+");
            formula.append(Const.INPUT).append(".radius.").append(start+counter);
            
            labels.append("\"").append(interval.getCentre()).append("\"");
            labels.append(",");
            labels.append("\"").append(interval.getRadius()).append("\"");
            
            counter++;
        }
        labels.append(")");
        
        for (IntervalNamesLowerUpper interval : par.getListLowerUpper()) {
            //TODO dokoncit; zatial ich ignoruje
            
//            String colour = ColourService.getService().getNewColour();
//            interval.setColourInPlot(colour);
//            
//            String lineStyle = "col=\"" + colour + "\"";
//            drawScatterPlotMatrixITS_LBUB(par.getWidth(), par.getHeight(), par.getDataTableModel().getDataForColname(interval.getLowerBound()),
//                    par.getDataTableModel().getDataForColname(interval.getUpperBound()), next, lineStyle, rangeCenter, rangeRadius);
//            if (! next) {
//                next = true;
//            }
        }
        
        drawNowToThisGDBufferedPanel = par.getCanvasToUse();
        rengine.require("JavaGD");
        rengine.require("psych");
        rengine.eval("JavaGD()"); // zacne novy plot
        rengine.eval("pairs.panels(" + formula + ", " + labels + ", smooth=FALSE, scale=FALSE, ellipses=FALSE, "
                + "hist.col=\"#777777\", col=\"#444444\", rug=FALSE)");
        
        drawNowToThisGDBufferedPanel.setSize(new Dimension(par.getCanvasToUse().getWidth(), par.getCanvasToUse().getHeight()));
        drawNowToThisGDBufferedPanel.initRefresh();
        
        PlotStateKeeper.setLastCallParams(par); //povodne par
    }

    private static String getRangeYMultipleInterval(List<Double> allVals) {
        StringBuilder rangeY = new StringBuilder("range(c(");
        boolean next = false;
        for (Double d : allVals) {
            if (next) {
                rangeY.append(", ");
            } else {
                next = true;
            }
            rangeY.append(d);
        }
        
        rangeY.append("))");
        return rangeY.toString();
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
        //TODO maybe later "optimize" a bit - netreba tahat vsetky data z dataTableModela, iba unique mena... a tak.
        
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

    private static List<TrainAndTestReportInterval> deleteEmpty(List<TrainAndTestReportInterval> reportsITS) {
        List<TrainAndTestReportInterval> toDelete = new ArrayList<>();
        
        for (TrainAndTestReportInterval r : reportsITS) {
            if (r.getFittedValues().isEmpty() && r.getForecastValuesTest().isEmpty() && r.getForecastValuesFuture().isEmpty()) {
                toDelete.add(r);
            }
        }
        
        reportsITS.removeAll(toDelete);
        return reportsITS;
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
        //a zahrnut aj povodne data:
        if (rangesY.length() > 8) { //velmi hlupy a ohavny sposob, ako zistovat, ze tam nic neni
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
            
            //a zahrnut aj povodne data:
            List<Double> realDataLower = r.getRealValuesLowers();
            List<Double> realDataUpper = r.getRealValuesUppers();
            
            //a zahrnut aj povodne data:
            if (rangesY.length() > 8) { //velmi hlupy a ohavny sposob, ako zistovat, ze tam nic neni
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
        String rangeXrestrictedFromTo = "range(c(max(" + from + "," + rangeX + "[1]), min(" + (to+numForecasts) + "," + rangeX + "[2])))";
        return rangeXrestrictedFromTo;
    }
    
    private static String getRangeXInterval(int sizeDataWithoutFromToCrop, int numForecasts, int from, int to) {
        String rangesX = "range(c(1, " + (sizeDataWithoutFromToCrop + numForecasts) + "))";
        
        String rangesXrestrictedFromTo = "range(c(max(" + from + "," + rangesX + "[1]), min(" + (to+numForecasts) + "," + rangesX + "[2])))";
        return rangesXrestrictedFromTo;
    }
    
    public static String getRString(List<String> list) {
        StringBuilder rString = new StringBuilder();
        rString.append("c(");
        boolean next = false;
        for (String s : list) {
            if (next) {
                rString.append(", ");
            } else {
                next = true;
            }
            rString.append("\"").append(s).append("\"");
        }
        rString.append(")");
        return rString.toString();
    }
    
    public static List<JGDBufferedPanel> drawDiagrams(int width, int height, List<TrainAndTestReport> reports) {
        //najprv si nasysli vsetky diagramy
        List<String> diagramPlots = new ArrayList<>();
        for (TrainAndTestReport r : reports) {
            if (! "".equals(r.getNnDiagramPlotCode())) {
                if (r.getNnDiagramPlotCode().contains(";")) {
                    //fuj, hack, TODO spravit lepsie ako split na dva, resp. prisposobovat v buducnosti
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
        
        //TODO prerobit to, aby sa v tej poslednej mriezke, ak nie je plna, zvacovali grafy?
        //tj ostane mi n grafov. x = horna cela cast z odmocniny z n. a potom vysledny obdlznik je x(x-1) ak to staci na n, inak x.x
        
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
                rengine.eval("par(mfrow=c(" + maxRow + "," + maxCol + "))"); //narobim si mriezku
                
                int counter = maxCol*maxRow;
                while ((counter > 0) && (currentIndex < plots.size())) {
                    rengine.eval(plots.get(currentIndex));
                    
                    currentIndex++;
                    counter--;
                }
                
                        
                
                while ((counter > 0) && (currentIndex == plots.size())) { //dosli mi diagramy a este nie je plna mriezka
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

    public static void drawLegend(final JList listPlotLegend, final List<Plottable> plots,
            final List<TrainAndTestReport> addedReports, ListCellRenderer cellRenderer,
            final String rangeXCrisp, final String rangeYCrisp, final String rangeXInt, final String rangeYInt) {
        ((DefaultListModel)(listPlotLegend.getModel())).removeAllElements();
        
        listPlotLegend.setCellRenderer(cellRenderer); //musi byt nastaveny pred vytvaranim TurnoOFFableElt! tam je repaint
        
        if (cellRenderer instanceof PlotLegendTurnOFFableListCellRenderer) {
            for (Plottable p : plots) {
                if (p.getColourInPlot() != null) {
                    final PlotLegendTurnOFFableListElement element = new PlotLegendTurnOFFableListElement(p, listPlotLegend, addedReports);
                    ((DefaultListModel)(listPlotLegend.getModel())).addElement(element);
                }
            }
        } else {
            //TODO - should never happen
        }
        
        listPlotLegend.repaint();
    }
    
    
    public static void drawLegend(final JList listPlotLegend, List<Plottable> plots, ListCellRenderer cellRenderer) {
        ((DefaultListModel)(listPlotLegend.getModel())).removeAllElements();
        
        listPlotLegend.setCellRenderer(cellRenderer);
        
        if (cellRenderer instanceof PlotLegendTurnOFFableListCellRenderer) {
            //TODO - should never happen
        } else {
            for (Plottable p : plots) {
                if (p.getColourInPlot() != null) {
                    final PlotLegendSimpleListElement element = new PlotLegendSimpleListElement(p, listPlotLegend);
                    ((DefaultListModel)(listPlotLegend.getModel())).addElement(element);
                }
            }
        }
        
        listPlotLegend.repaint();
    }

    public static void drawSimpleFctionToGrid(String plottingFunction, List<String> selectedValuesList,
            DataTableModel dataTableModel, JTabbedPane tabbedPaneAnalysisPlots) throws IllegalArgumentException {
        //najprv si nasysli vsetky diagramy
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
        
        //potom ich nechaj vyplut do mriezky
        List<JGDBufferedPanel> panels = drawToGrid(tabbedPaneAnalysisPlots.getWidth(), tabbedPaneAnalysisPlots.getHeight(),
                diagramsPlots, COLUMNS_BOXHIST, ROWS_BOXHIST);
        
        //a tu mriezku nakresli
        tabbedPaneAnalysisPlots.removeAll();
        int i = 0;
        for (JGDBufferedPanel p : panels) {
            tabbedPaneAnalysisPlots.addTab("Page "+(++i), p);
        }

        tabbedPaneAnalysisPlots.repaint();
    }
    
    //TODO refactor vsetky tieto drawXXXtoGrid - aby pouzivali nejaky spolocny zaklad. vsetky su rovnake.
    public static void drawBayesToGrid(List<String> diagramsPlots, JTabbedPane tabbedPanePlots) throws IllegalArgumentException {
        //nechaj tie ploty vyplut do mriezky
        List<JGDBufferedPanel> panels = drawToGrid(tabbedPanePlots.getWidth(), tabbedPanePlots.getHeight(),
                diagramsPlots, 1, 1);
        
        //a tu mriezku nakresli
        tabbedPanePlots.removeAll();
        int i = 0;
        for (JGDBufferedPanel p : panels) {
            tabbedPanePlots.addTab("Page "+(++i), p);
        }

        tabbedPanePlots.repaint();
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
    
    
    public static void drawPlotGeneral(boolean drawNew, CallParamsDrawPlotGeneral par) {
        //get the Y range first (assuming X is the same)
        StringBuilder rangeYStringBuilder = new StringBuilder("range(c(");
        boolean next = false;
        for (DefaultPlottable col : par.getColnames()) {
            for (Double d : DataTableModel.getInstance().getDataForColname(col.getColname())) {
                if (d.isNaN()) {
                    continue;
                }
                
                if (next) {
                    rangeYStringBuilder.append(", ");
                } else {
                    next = true;
                }
                rangeYStringBuilder.append(d);
            }
        }
        rangeYStringBuilder.append("))");
        String rangeY = rangeYStringBuilder.toString();
        String rangeX = "range(c(0, " + DataTableModel.getInstance().getRowCount() + "))";
        
        if ("acf".equals(par.getPlotFunction()) || "pacf".equals(par.getPlotFunction())) {
            rangeY = "range(c(-1,1))";
            
            //String rangeX = "range(c(0, " + getRowCount() + "))";
            //default lagmax: 10*log10(N)
            rangeX = "range(c(0,10*log10(" + DataTableModel.getInstance().getRowCount() + ")))";
        }
        
        drawPlotGeneral(drawNew, par, rangeX, rangeY);
    }
    
    public static void drawPlotGeneral(boolean drawNew, CallParamsDrawPlotGeneral par, String rangeX, String rangeY) {
        drawNowToThisGDBufferedPanel = par.getCanvasToUse();
        
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
        drawNowToThisGDBufferedPanel.setSize(new Dimension(par.getWidth(), par.getHeight())); //TODO nechce sa zmensit pod urcitu velkost, vymysliet
        drawNowToThisGDBufferedPanel.initRefresh();
    }

    public static void drawScreePlot(List<String> selectedValuesList, JTabbedPane tabbedPaneAnalysisPlots) {
        MyRengine rengine = MyRengine.getRengine();

        rengine.require("psych");
        
        final String INPUT = rengine.createDataFrame(selectedValuesList);
        
        List<String> plots = new ArrayList<>();
        plots.add("scree(cor(" + INPUT + "), factors=FALSE)");
        
        List<JGDBufferedPanel> panels = drawToGrid(tabbedPaneAnalysisPlots.getWidth(), tabbedPaneAnalysisPlots.getHeight(),
                plots, 1, 1);
        
        tabbedPaneAnalysisPlots.removeAll();
        int i = 0;
        for (JGDBufferedPanel p : panels) {
            tabbedPaneAnalysisPlots.addTab("Page "+(++i), p);
        }

        tabbedPaneAnalysisPlots.repaint();
        
        rengine.rm(INPUT);
    }

    public static JGDBufferedPanel getDrawNowToThisGDBufferedPanel() {
        return drawNowToThisGDBufferedPanel;
    }

    public static void setDrawNowToThisGDBufferedPanel(JGDBufferedPanel drawNowToThisGDBufferedPanel) {
        PlotDrawer.drawNowToThisGDBufferedPanel = drawNowToThisGDBufferedPanel;
    }
}
