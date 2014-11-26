package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import org.rosuda.javaGD.JGDBufferedPanel;
import utils.Const;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;
import utils.imlp.Interval;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;
import utils.imlp.dist.WeightedEuclideanDistance;
import utils.ugliez.CallParamsDrawPlots;
import utils.ugliez.CallParamsDrawPlotsITS;
import utils.ugliez.PlotStateKeeper;

public class PlotDrawer {
    //TODO toto by cele chcelo upratat, prekopar, mozno refaktorovat do viacerych tried
    //a pridat sem vsetko, co sa tyka kreslenia - napr. i v DataTableModel je nieco, mozno v MainFrame, a tak.
    
    private static final int COLUMNS_DIAGRAMSNN = 3;
    private static final int ROWS_DIAGRAMSNN = 3;
    
    //drawNew je true, ak sa maju zmenit maximalne medze obrazku, tj kresli sa to z Run, Plot CTS, Plot ITS, ACF, PACF
    //drawNew je false, ak sa len zoomuje aktualny obrazok a nekresli sa novy, tj zo Zoom CTS, Zoom ITS
    //return ploty, ktore sa prave pridali, takze AVG
    public static List<TrainAndTestReport> drawPlots(boolean drawNew, CallParamsDrawPlots par) {
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
        
        return drawPlots(drawNew, par, rangeXCrisp, rangeYCrisp, rangeXInt, rangeYInt);
    }
    
    public static List<TrainAndTestReport> drawPlots(boolean drawNew, CallParamsDrawPlots par,
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
        boolean avgCTSperMethod = par.isPlotAvgCTSperMethod();
        boolean avgCTS = par.isPlotAvgCTS();
        boolean avgIntTSperMethod = par.isPlotAvgIntTSperMethod();
        boolean avgIntTS = par.isPlotAvgIntTS();
        boolean avgONLY = par.isPlotAvgONLY();
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(JavaGD)");
        
        MainFrame.drawNowToThisGDBufferedPanel = canvasToUse;
        rengine.eval("JavaGD()");
        
        if ((! reportsCTS.isEmpty()) && (! reportsIntTS.isEmpty())) { //budem vykreslovat oba naraz
            rengine.eval("par(mfrow=c(1,2))"); //daj dva grafy vedla seba. potom normalne zavolat dva ploty.
        }
        
        int colourNumber = 0;
        if (! reportsCTS.isEmpty()) { //plot CTS
            allDataCTS = allDataCTS.subList(from, Math.min(to+numForecasts, allDataCTS.size()));
            
            boolean next = false;
            Map<String, List<TrainAndTestReportCrisp>> mapForAvg = new HashMap<>();
            for (TrainAndTestReportCrisp r : reportsCTS) {
                if (next && !(avgONLY)) {
                    rengine.eval("par(new=TRUE)");
                } else {
                    next = true;
                }

                if (avgCTSperMethod) {
                    if (mapForAvg.containsKey(r.getModelName())) {
                        mapForAvg.get(r.getModelName()).add(r);
                    } else {
                        List<TrainAndTestReportCrisp> l = new ArrayList<>();
                        l.add(r);
                        mapForAvg.put(r.getModelName(), l);
                    }
                }

                StringBuilder plotCode = new StringBuilder(r.getPlotCode());
                plotCode.insert(r.getPlotCode().length() - 1, ", xlim = " + rangeXCrisp + ", ylim = " + rangeYCrisp + ", "
                        + "axes=FALSE, ann=FALSE, " //suppress axes names and labels, just draw them for the main data
                        + "lwd=4, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\"");
                plotCode.insert(10, "rep(NA, " + par.getFrom() + "), "); //hack - posunutie
                
                if (!(avgONLY)) {
                    rengine.eval(plotCode.toString());
                    
                    
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
                                   + "density=NA, col=" + getRGBColorStringForHEX(COLOURS[colourNumber % COLOURS.length], 30) 
                                   + ", border=NA)"); //TODO col podla aktualnej
                    }
                    
                    
                    
                    //add a dashed vertical line to separate test and train
                    rengine.eval("abline(v = " + (r.getNumTrainingEntries() + par.getFrom()) + ", lty = 2, lwd=2, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\")");
                    r.setColourInPlot(COLOURS[colourNumber % COLOURS.length]);
                }
                
                colourNumber++;
            }
            
            List<TrainAndTestReportCrisp> avgReportsToAdd = new ArrayList<>();
            //now draw the average series
            if (avgCTSperMethod) {
                for (String name : mapForAvg.keySet()) {
                    List<TrainAndTestReportCrisp> l = mapForAvg.get(name);
                    if (l.size() > 1) { //does not make sense to compute average over one series
                        StringBuilder avgAll = new StringBuilder("("); //mozno neskor zrusit toto a poskladat to z tych troch?
                        StringBuilder fittedValsAvgAll = new StringBuilder("(");
                        StringBuilder forecastValsTestAvgAll = new StringBuilder("(");
                        StringBuilder forecastValsFutureAvgAll = new StringBuilder("(");
                        next = false;
                        int numForecastsAvg = 0;
                        for (TrainAndTestReportCrisp r : l) {
                            if (next) {
                                avgAll.append(" + ");
                                fittedValsAvgAll.append(" + ");
                                forecastValsTestAvgAll.append(" + ");
                                forecastValsFutureAvgAll.append(" + ");
                            } else {
                                next = true;
                            }
                            //this will take the vector: c(rep(NA, something), fit, test, future)
                            //TODO tu sa nemoze brat aj future! niektore to maju a niektore nie, tak sa priemeruje zle...
                            String justData = r.getPlotCode().substring(8, r.getPlotCode().length() - 1);
                            avgAll.append(justData);
                            fittedValsAvgAll.append(Utils.arrayToRVectorString(r.getFittedValues()));
                            forecastValsTestAvgAll.append(Utils.arrayToRVectorString(r.getForecastValuesTest()));
                            
                            if (r.getForecastValuesFuture().length > 0) {
                                forecastValsFutureAvgAll.append(Utils.arrayToRVectorString(r.getForecastValuesFuture()));
                                numForecastsAvg++;
                            } else {
                                forecastValsFutureAvgAll.append("0");
                            }
                        }
                        avgAll.append(")/").append(l.size());
                        fittedValsAvgAll.append(")/").append(l.size());
                        forecastValsTestAvgAll.append(")/").append(l.size());
                        forecastValsFutureAvgAll.append(")/").append(numForecastsAvg);

                        //aaaand draw the average
                        rengine.eval("par(new=TRUE)");
                        rengine.eval("plot.ts(" + avgAll + ", xlim = " + rangeXCrisp + ", ylim = " + rangeYCrisp + ", "
                                + "axes=FALSE, ann=FALSE, " //suppress axes names and labels, just draw them for the main data
                                + "lty=2, lwd=5, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\")");
                        //add a dashed vertical line to separate test and train
                        rengine.eval("abline(v = " + (l.get(0).getNumTrainingEntries() + par.getFrom()) + ", lty = 2, lwd=2, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\")");

                        //vyrobit pre tento average novy report a pridat ho do reportsCTS:
                        TrainAndTestReportCrisp thisAvgReport = new TrainAndTestReportCrisp(name + "(avg)");
                        REXP getFittedValsAvg = rengine.eval(fittedValsAvgAll.toString());
                        double[] fittedValsAvg = getFittedValsAvg.asDoubleArray();
                        REXP getForecastValsTestAvg = rengine.eval(forecastValsTestAvgAll.toString());
                        double[] forecastValsTestAvg = getForecastValsTestAvg.asDoubleArray();
                        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils
                                .computeAllErrorMeasuresCrisp(Utils.arrayToList(l.get(0).getRealOutputsTrain()),
                                Utils.arrayToList(l.get(0).getRealOutputsTest()), Utils.arrayToList(fittedValsAvg),
                                Utils.arrayToList(forecastValsTestAvg), 0);
                        thisAvgReport.setErrorMeasures(errorMeasures);
                        REXP getForecastValsFutureAvg = rengine.eval(forecastValsFutureAvgAll.toString());
                        double[] forecastValsFutureAvg = getForecastValsFutureAvg.asDoubleArray();
                        thisAvgReport.setForecastValuesFuture(forecastValsFutureAvg);
                        thisAvgReport.setColourInPlot(COLOURS[colourNumber % COLOURS.length]);
                        thisAvgReport.setPlotCode("plot.ts(" + avgAll + ")");
                        thisAvgReport.setFittedValues(fittedValsAvg);
                        thisAvgReport.setForecastValuesTest(forecastValsTestAvg);
                        thisAvgReport.setNumTrainingEntries(fittedValsAvg.length);
                        thisAvgReport.setRealOutputsTrain(l.get(0).getRealOutputsTrain());
                        thisAvgReport.setRealOutputsTest(l.get(0).getRealOutputsTest());
                        avgReportsToAdd.add(thisAvgReport);
                        
                        colourNumber++;
                    }
                }
            }
            
            //and draw the average of all CTS methods that were run
            if (avgCTS) {
                if (reportsCTS.size() > 1) { //does not make sense to compute average over one series
                    StringBuilder avgAll = new StringBuilder("(");
                    StringBuilder fittedValsAvgAll = new StringBuilder("(");
                    StringBuilder forecastValsTestAvgAll = new StringBuilder("(");
                    StringBuilder forecastValsFutureAvgAll = new StringBuilder("(");
                    next = false;
                    int numForecastsAvg = 0;
                    for (TrainAndTestReportCrisp r : reportsCTS) {
                        if (next) {
                            avgAll.append(" + ");
                            fittedValsAvgAll.append(" + ");
                            forecastValsTestAvgAll.append(" + ");
                            forecastValsFutureAvgAll.append(" + ");
                        } else {
                            next = true;
                        }
                        //this will take the vector: c(rep(NA, something), fit, test, future)
                        String justData = r.getPlotCode().substring(8, r.getPlotCode().length() - 1);
                        avgAll.append(justData);
                        fittedValsAvgAll.append(Utils.arrayToRVectorString(r.getFittedValues()));
                        forecastValsTestAvgAll.append(Utils.arrayToRVectorString(r.getForecastValuesTest()));
                        
                        if (r.getForecastValuesFuture().length > 0) {
                            forecastValsFutureAvgAll.append(Utils.arrayToRVectorString(r.getForecastValuesFuture()));
                            numForecastsAvg++;
                        } else {
                            forecastValsFutureAvgAll.append("0");
                        }
                       
                    }
                    avgAll.append(")/").append(reportsCTS.size());
                    fittedValsAvgAll.append(")/").append(reportsCTS.size());
                    forecastValsTestAvgAll.append(")/").append(reportsCTS.size());
                    forecastValsFutureAvgAll.append(")/").append(numForecastsAvg);

                    //aaaand draw the average
                    rengine.eval("par(new=TRUE)");
                    rengine.eval("plot.ts(" + avgAll + ", xlim = " + rangeXCrisp + ", ylim = " + rangeYCrisp + ", "
                            + "axes=FALSE, ann=FALSE, " //suppress axes names and labels, just draw them for the main data
                            + "lty=2, lwd=6, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\")");
                    //add a dashed vertical line to separate test and train
                    //not really possible cause the methods may have different train-test ratio
                    //rengine.eval("abline(v = " + (reportsCTS.get(0).getNumTrainingEntries() + par.getFrom()) + ", lty = 2, lwd=2, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\")");

                    //a vyrobit pre tento average novy report a pridat ho do reportsCTS:
                    TrainAndTestReportCrisp thisAvgReport = new TrainAndTestReportCrisp("(avg)");
                    REXP getFittedValsAvg = rengine.eval(fittedValsAvgAll.toString());
                    double[] fittedValsAvg = getFittedValsAvg.asDoubleArray();
                    REXP getForecastValsTestAvg = rengine.eval(forecastValsTestAvgAll.toString());
                    double[] forecastValsTestAvg = getForecastValsTestAvg.asDoubleArray();
                    
                    List<Double> allRealDataTrainAndTest = new ArrayList<>(); //will have the NA vals of the first report
                    //but they would've gotten erased anyway
                    allRealDataTrainAndTest.addAll(Utils.arrayToList(reportsCTS.get(0).getRealOutputsTrain()));
                    allRealDataTrainAndTest.addAll(Utils.arrayToList(reportsCTS.get(0).getRealOutputsTest()));
                    List<Double> allFitDataTrainAndTest = new ArrayList<>();
                    allFitDataTrainAndTest.addAll(Utils.arrayToList(fittedValsAvg));
                    allFitDataTrainAndTest.addAll(Utils.arrayToList(forecastValsTestAvg));
                    
//                    System.out.println(allRealDataTrainAndTest.subList(0, 5) + ", " + allRealDataTrainAndTest.size());
//                    System.out.println(allFitDataTrainAndTest.subList(0, 5) + ", " + allFitDataTrainAndTest.size()
//                           + " (" + fittedValsAvg.length + ", " + forecastValsTestAvg.length + ")");
                    
//                    ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(allRealDataTrainAndTest, 
//                            new ArrayList<Double>(), allFitDataTrainAndTest, new ArrayList<Double>(), 0);
                    ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(new ArrayList<Double>(), 
                            new ArrayList<Double>(), new ArrayList<Double>(), new ArrayList<Double>(), 0);
                    
                    thisAvgReport.setErrorMeasures(errorMeasures);
                    REXP getForecastValsFutureAvg = rengine.eval(forecastValsFutureAvgAll.toString());
                    double[] forecastValsFutureAvg = getForecastValsFutureAvg.asDoubleArray();
                    thisAvgReport.setForecastValuesFuture(forecastValsFutureAvg);
                    thisAvgReport.setColourInPlot(COLOURS[colourNumber % COLOURS.length]);
                    avgReportsToAdd.add(thisAvgReport);
                    thisAvgReport.setPlotCode("plot.ts(" + avgAll + ")");
                    thisAvgReport.setFittedValues(fittedValsAvg);
                    thisAvgReport.setForecastValuesTest(forecastValsTestAvg);
                    thisAvgReport.setNumTrainingEntries(fittedValsAvg.length);
                    //TODO
//                    thisAvgReport.setRealOutputsTrain(l.get(0).getRealOutputsTrain());
//                    thisAvgReport.setRealOutputsTest(l.get(0).getRealOutputsTest());
                    
                    colourNumber++;
                }
            }
            
            //na zaver slavnostne pridat nove reporty do reportsCTS, ked to uz neschaosi nic ine:
            addedReports.addAll(avgReportsToAdd);
            
            
            rengine.assign("all.data", Utils.listToArray(allDataCTS));
            rengine.eval("par(new=TRUE)");
            rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), all.data), xlim = " + rangeXCrisp + ", ylim = " + rangeYCrisp + ", "
                    + "ylab=\"" + colname_CTS + "\","
                    + "lwd=2, col=\"#444444\")");
            rengine.eval("abline(v = " + (to) + ", lty = 3)"); //dashed vertical line to separate forecasts
            
            REXP getX = rengine.eval(rangeXCrisp);
            double[] rangeX = getX.asDoubleArray();
            REXP getY = rengine.eval(rangeYCrisp);
            double[] rangeY = getY.asDoubleArray();
            PlotStateKeeper.setLastDrawnCrispXmin(rangeX[0]);
            PlotStateKeeper.setLastDrawnCrispXmax(rangeX[1]);
            PlotStateKeeper.setLastDrawnCrispYmin(rangeY[0]);
            PlotStateKeeper.setLastDrawnCrispYmax(rangeY[1]);
            if (drawNew) {
                PlotStateKeeper.setCrispXmax(par.getSizeDataWithoutFromToCrop() + numForecasts);
                PlotStateKeeper.setCrispYmax(rangeY[1]);
            }
        }
        
        if (! reportsIntTS.isEmpty()) { //plot ITS
            boolean next = false;
            Map<String, List<TrainAndTestReportInterval>> mapForAvg = new HashMap<>();
            for (TrainAndTestReportInterval r : reportsIntTS) {
                if (next && !(avgONLY)) {
                    rengine.eval("par(new=TRUE)");
                } else {
                    next = true;
                }
                
                if (avgIntTSperMethod) {
                    if (mapForAvg.containsKey(r.getModelName())) {
                        mapForAvg.get(r.getModelName()).add(r);
                    } else {
                        List<TrainAndTestReportInterval> l = new ArrayList<>();
                        l.add(r);
                        mapForAvg.put(r.getModelName(), l);
                    }
                }
                
                if (!(avgONLY)) {
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
                            + rangeXInt + ", ylim = " + rangeYInt + ", lwd=4, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\")");

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
                            + ", ylim = " + rangeYInt + ", lwd=4, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\")");

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
                                + ", lwd=4, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\")");
                    }

                    //add a dashed vertical line to separate test and train
                    rengine.eval("abline(v = " + (sizeFitted+par.getFrom()) + ", lty = 2, lwd=2, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\")");
                    
                    r.setColourInPlot(COLOURS[colourNumber % COLOURS.length]);
                }
                
                colourNumber++;
            }
            
            //now draw the average series
            if (avgIntTSperMethod) {
                for (String name : mapForAvg.keySet()) {
                    List<TrainAndTestReportInterval> l = mapForAvg.get(name);
                    if (l.size() > 1) { //does not make sense to compute average over one series
                        StringBuilder avgAllLowersTrain = new StringBuilder("(");
                        StringBuilder avgAllLowersTest = new StringBuilder("(");
                        StringBuilder avgAllLowersFuture = new StringBuilder("(");
                        StringBuilder avgAllUppersTrain = new StringBuilder("(");
                        StringBuilder avgAllUppersTest = new StringBuilder("(");
                        StringBuilder avgAllUppersFuture = new StringBuilder("(");
                        next = false;
                        int sizeTrain = 0;
                        int sizeTest = 0;
                        int sizeFuture = 0;
                        for (TrainAndTestReportInterval r : l) {
                            if (next) {
                                avgAllLowersTrain.append(" + ");
                                avgAllLowersTest.append(" + ");
                                avgAllLowersFuture.append(" + ");
                                avgAllUppersTrain.append(" + ");
                                avgAllUppersTest.append(" + ");
                                avgAllUppersFuture.append(" + ");
                            } else {
                                next = true;
                            }
                            
                            avgAllLowersTrain.append(Utils.arrayToRVectorString(r.getFittedValuesLowers()));
                            avgAllLowersTest.append(Utils.arrayToRVectorString(r.getForecastValuesTestLowers()));
                            avgAllLowersFuture.append(Utils.arrayToRVectorString(r.getForecastValuesFutureLowers()));
                            
                            avgAllUppersTrain.append(Utils.arrayToRVectorString(r.getFittedValuesUppers()));
                            avgAllUppersTest.append(Utils.arrayToRVectorString(r.getForecastValuesTestUppers()));
                            avgAllUppersFuture.append(Utils.arrayToRVectorString(r.getForecastValuesFutureUppers()));
                            
                            sizeTrain = Math.max(sizeTrain, r.getFittedValues().size());
                            sizeTest = Math.max(sizeTest, r.getForecastValuesTest().size());
                            sizeFuture = Math.max(sizeFuture, r.getForecastValuesFuture().size());
                        }
                        avgAllLowersTrain.append(")/").append(l.size());
                        avgAllLowersTest.append(")/").append(l.size());
                        avgAllLowersFuture.append(")/").append(l.size());
                        avgAllUppersTrain.append(")/").append(l.size());
                        avgAllUppersTest.append(")/").append(l.size());
                        avgAllUppersFuture.append(")/").append(l.size());
                        
                        //aaaand draw the average
                        rengine.eval("par(new=TRUE)");
                        rengine.eval("lowerTrain <- " + avgAllLowersTrain.toString());
                        rengine.eval("lowerTest <- " + avgAllLowersTest.toString());
                        rengine.eval("lowerFuture <- " + avgAllLowersFuture);
                        rengine.eval("lower <- c(lowerTrain, lowerTest, lowerFuture)");
                        rengine.eval("upperTrain <- " + avgAllUppersTrain.toString());
                        rengine.eval("upperTest <- " + avgAllUppersTest.toString());
                        rengine.eval("upperFuture <- " + avgAllUppersFuture);
                        rengine.eval("upper <- c(upperTrain, upperTest, upperFuture)");
                        rengine.eval("plot.ts(lower, type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                                + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                        rengine.eval("par(new=TRUE)");
                        rengine.eval("plot.ts(upper, type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                                + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                        rengine.eval("segments(" + par.getFrom() + ":" + (sizeTrain+sizeTest+sizeFuture+par.getFrom()) + ", lower, "
                                + par.getFrom() + ":" + (sizeTrain+sizeTest+sizeFuture+par.getFrom())
                                + ", upper, xlim = " + rangeXInt + ", ylim = " + rangeYInt
                                + ", lwd=5, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\")");
                        rengine.eval("abline(v = " + (l.get(0).getNumTrainingEntries() + par.getFrom()) + ", lty = 2, lwd=2, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\")");
                        
                        
                        //add report:
                        REXP getAllLowersTrain = rengine.eval("lowerTrain");
                        double[] allLowersTrain = getAllLowersTrain.asDoubleArray();
                        List<Double> allLowersTrainList = Utils.arrayToList(allLowersTrain);
                        REXP getAllLowersTest = rengine.eval("lowerTest");
                        double[] allLowersTest = getAllLowersTest.asDoubleArray();
                        List<Double> allLowersTestList = Utils.arrayToList(allLowersTest);
                        REXP getAllUppersTrain = rengine.eval("upperTrain");
                        double[] allUppersTrain = getAllUppersTrain.asDoubleArray();
                        List<Double> allUppersTrainList = Utils.arrayToList(allUppersTrain);
                        REXP getAllUppersTest = rengine.eval("upperTest");
                        double[] allUppersTest = getAllUppersTest.asDoubleArray();
                        List<Double> allUppersTestList = Utils.arrayToList(allUppersTest);
                        List<Interval> allIntervalsTrain = Utils.zipLowerUpperToIntervals(allLowersTrainList, allUppersTrainList);
                        List<Interval> allIntervalsTest = Utils.zipLowerUpperToIntervals(allLowersTestList, allUppersTestList);
                        
                        //TODO mozno prerobit? zatial to rata s tym, ze vsetky v ramci 1 metody maju rovnake realValues
                        List<Double> realValuesLowersTrain = l.get(0).getRealValuesLowers()
                                .subList(0, l.get(0).getFittedValues().size());
                        List<Double> realValuesUppersTrain = l.get(0).getRealValuesUppers()
                                .subList(0, l.get(0).getFittedValues().size());
                        List<Double> realValuesLowersTest = l.get(0).getRealValuesLowers()
                                .subList(l.get(0).getFittedValues().size(), 
                                         l.get(0).getRealValuesLowers().size() - l.get(0).getForecastValuesFuture().size());
                        List<Double> realValuesUppersTest = l.get(0).getRealValuesUppers()
                                .subList(l.get(0).getFittedValues().size(), 
                                         l.get(0).getRealValuesUppers().size() - l.get(0).getForecastValuesFuture().size());
                        List<Interval> realValuesTrain = Utils.zipLowerUpperToIntervals(realValuesLowersTrain, realValuesUppersTrain);
                        List<Interval> realValuesTest = Utils.zipLowerUpperToIntervals(realValuesLowersTest, realValuesUppersTest);

                        ErrorMeasuresInterval errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresInterval(realValuesTrain, 
                                realValuesTest, allIntervalsTrain, allIntervalsTest, new WeightedEuclideanDistance(0.5), 0);
                        //TODO zmenit! zatial sa to pocita WeightedEuclid, ale dat tam hocijaku distance!
                        
                        TrainAndTestReportInterval reportAvgMethod = new TrainAndTestReportInterval(l.get(0).getModelName() + "(avg)");
                        reportAvgMethod.setErrorMeasures(errorMeasures);
                        reportAvgMethod.setColourInPlot(COLOURS[colourNumber % COLOURS.length]);
                        
                        reportsIntTS.add(reportAvgMethod);
                        
                        colourNumber++;
                    }
                }
            }
            
            //and draw the average of all ITS methods that were run
            if (avgIntTS) {
                if (reportsIntTS.size() > 1) { //does not make sense to compute average over one series
                    StringBuilder avgAllLowers = new StringBuilder("(");
                    StringBuilder avgAllUppers = new StringBuilder("(");
                    next = false;
                    int size = 0;
                    for (TrainAndTestReportInterval r : reportsIntTS) {
                        if (next) {
                            avgAllLowers.append(" + ");
                            avgAllUppers.append(" + ");
                        } else {
                            next = true;
                        }
                        
                        avgAllLowers.append("c(");
                        avgAllLowers.append(Utils.arrayToRVectorString(r.getFittedValuesLowers()));
                        avgAllLowers.append(",");
                        avgAllLowers.append(Utils.arrayToRVectorString(r.getForecastValuesTestLowers()));
                        avgAllLowers.append(",");
                        avgAllLowers.append(Utils.arrayToRVectorString(r.getForecastValuesFutureLowers()));
                        avgAllLowers.append(")");

                        avgAllUppers.append("c(");
                        avgAllUppers.append(Utils.arrayToRVectorString(r.getFittedValuesUppers()));
                        avgAllUppers.append(",");
                        avgAllUppers.append(Utils.arrayToRVectorString(r.getForecastValuesTestUppers()));
                        avgAllUppers.append(",");
                        avgAllUppers.append(Utils.arrayToRVectorString(r.getForecastValuesFutureUppers()));
                        avgAllUppers.append(")");
                        
                        size = Math.max(size, r.getFittedValues().size()+r.getForecastValuesTest().size()+r.getForecastValuesFuture().size());
                    }
                    avgAllLowers.append(")/").append(reportsIntTS.size());
                    avgAllUppers.append(")/").append(reportsIntTS.size());

                    //aaaand draw the average
                    rengine.eval("par(new=TRUE)");
                    rengine.eval("lower <- " + avgAllLowers.toString());
                    rengine.eval("upper <- " + avgAllUppers.toString());
                    rengine.eval("plot.ts(lower, type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                            + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                    rengine.eval("par(new=TRUE)");
                    rengine.eval("plot.ts(upper, type=\"n\", xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", "
                            + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                    rengine.eval("segments(" + par.getFrom() + ":" + (size+par.getFrom()) + ", lower, "
                            + par.getFrom() + ":" + (size+par.getFrom())
                            + ", upper, xlim = " + rangeXInt + ", ylim = " + rangeYInt
                            + ", lwd=6, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\")");
                    
                    /////////TODO
//                    avgReport.setColourInPlot(COLOURS[colourNumber % COLOURS.length]);
                    colourNumber++;
                }
            }
            
            rengine.eval("par(new=TRUE)");
                
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
                    + "ylab=\"" +       "<<add the interval.toString() here>>"      + "\")");
            rengine.eval("segments(" + (1+par.getFrom()) + ":" + (size+par.getFrom()) + ", all.lower, " + (1+par.getFrom()) + ":" + (size+par.getFrom()) + ", all.upper, xlim = " + rangeXInt + ", ylim = " + rangeYInt + ", lwd=2, col=\"#444444\")");
            //add a line separating real data from forecasts
            rengine.eval("abline(v = " + (size+par.getFrom()) + ", lty = 3)");
            
            REXP getX = rengine.eval(rangeXInt);
            double[] rangeX = getX.asDoubleArray();
            REXP getY = rengine.eval(rangeYInt);
            double[] rangeY = getY.asDoubleArray();
            PlotStateKeeper.setLastDrawnIntXmin(rangeX[0]);
            PlotStateKeeper.setLastDrawnIntXmax(rangeX[1]);
            PlotStateKeeper.setLastDrawnIntYmin(rangeY[0]);
            PlotStateKeeper.setLastDrawnIntYmax(rangeY[1]);
            if (drawNew) {
                PlotStateKeeper.setIntXmax(par.getSizeDataWithoutFromToCrop() + numForecasts);
                PlotStateKeeper.setIntYmax(rangeY[1]);
            }
        }
        
        PlotStateKeeper.setLastCallParams(par);

        MainFrame.drawNowToThisGDBufferedPanel.setSize(new Dimension(width, height)); //TODO nechce sa zmensit pod urcitu velkost, vymysliet
        MainFrame.drawNowToThisGDBufferedPanel.initRefresh();
        
        //TODO avgITS nie je myslim dokoncene! minimalne sa teda nepridavaju reporty do reportov.
        
        //and draw the legend
        List<Plottable> allReports = new ArrayList<>();
        allReports.addAll(reportsCTS);
        allReports.addAll(reportsIntTS);
        allReports.addAll(addedReports);
        drawLegend(par.getListPlotLegend(), allReports);
        
        return addedReports;
    }
    
    public static void drawPlotsITS(boolean drawNew, CallParamsDrawPlotsITS par) {
        List<Double> allVals = getAllVals(par.getDataTableModel(), par.getListCentreRadius(), par.getListLowerUpper());
//        String rangeX = ; //predpokladajme, ze vsetky maju rovnaky pocet pozorovani
        String rangeY = getRangeYMultipleInterval(allVals);
        String rangeX = "range(c(0," + par.getDataTableModel().getRowCount() + "))";
        
        drawPlotsITS(drawNew, par, rangeX, rangeY);
    }
    
    public static void drawPlotsITS(boolean drawNew, CallParamsDrawPlotsITS par, String rangeX, String rangeY) {
        MainFrame.drawNowToThisGDBufferedPanel = par.getCanvasToUse();
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(JavaGD)");
        rengine.eval("JavaGD()");
        
        int colourNumber = 0;
        
        boolean next = false;
        for (IntervalNamesCentreRadius interval : par.getListCentreRadius()) {
            //remember the colour for the legend
            interval.setColourInPlot(COLOURS[colourNumber % COLOURS.length]);
            
            String lineStyle = ", lwd=4, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\"";
            drawPlotITS_CenterRadius(par.getWidth(), par.getHeight(), par.getDataTableModel().getDataForColname(interval.getCentre()),
                    par.getDataTableModel().getDataForColname(interval.getRadius()), next, lineStyle, rangeX, rangeY);
            if (! next) {
                next = true;
            }
            
            colourNumber++;
        }
        
        next = (! par.getListCentreRadius().isEmpty()) && (! par.getListLowerUpper().isEmpty()); //true ak je nieco v CenRad aj v LBUB
        
        for (IntervalNamesLowerUpper interval : par.getListLowerUpper()) {
            interval.setColourInPlot(COLOURS[colourNumber % COLOURS.length]);
            
            String lineStyle = ", lwd=4, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\"";
            drawPlotITS_LBUB(par.getWidth(), par.getHeight(), par.getDataTableModel().getDataForColname(interval.getLowerBound()),
                    par.getDataTableModel().getDataForColname(interval.getUpperBound()), next, lineStyle, rangeX, rangeY);
            if (! next) {
                next = true;
            }
            
            colourNumber++;
        }
        
        //draw legend
        List<Plottable> plots = new ArrayList<>();
        plots.addAll(par.getListCentreRadius());
        plots.addAll(par.getListLowerUpper());
        drawLegend(par.getListPlotLegend(), plots);
        
        REXP getRangeX = rengine.eval(rangeX);
        double[] ranX = getRangeX.asDoubleArray();
        REXP getRangeY = rengine.eval(rangeY);
        double[] ranY = getRangeY.asDoubleArray();
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
        
        Rengine rengine = MyRengine.getRengine();
        rengine.assign(LOWER, Utils.listToArray(lowerBound));
        rengine.assign(UPPER, Utils.listToArray(upperBound));
        
        drawPlotITSNow(width, height, LOWER, UPPER, count, par, lineStyle, rangeX, rangeY);
    }
    
    private static void drawPlotITS_CenterRadius(int width, int height, List<Double> center, List<Double> radius,
            boolean par, String lineStyle, String rangeX, String rangeY) {
        final String CENTER = Const.INPUT + Utils.getCounter();
        final String RADIUS = Const.INPUT + Utils.getCounter();
        final String LOWER = Const.INPUT + Utils.getCounter();
        final String UPPER = Const.INPUT + Utils.getCounter();
        final int count = center.size();
        
        Rengine rengine = MyRengine.getRengine();
        rengine.assign(CENTER, Utils.listToArray(center));
        rengine.assign(RADIUS, Utils.listToArray(radius));
        REXP getLower = rengine.eval(CENTER + " - " + RADIUS);
        REXP Upper = rengine.eval(CENTER + " + " + RADIUS);
        rengine.assign(LOWER, getLower);
        rengine.assign(UPPER, Upper);
        
        drawPlotITSNow(width, height, LOWER, UPPER, count, par, lineStyle, rangeX, rangeY);
    }
    
    private static void drawPlotITSNow(int width, int height, final String LOWER, final String UPPER, final int count,
            boolean par, String lineStyle, String rangeX, String rangeY) {
        Rengine rengine = MyRengine.getRengine();
        String lim = "xlim = " + rangeX + ", ylim = " + rangeY;
        
        if (par) { //continue from the previous plot
            rengine.eval("par(new=TRUE)");
            //dont draw axes
            rengine.eval("plot.ts(" + LOWER + ", " + lim + ", col=\"white\", axes=FALSE, ann=FALSE)"); //hack
            rengine.eval("par(new=TRUE)");
            //here either
            rengine.eval("plot.ts(" + UPPER + ", " + lim + ", col=\"white\", axes=FALSE, ann=FALSE)");
            rengine.eval("segments(1:" + count + ", " + LOWER + ", 1:" + count + ", " + UPPER + ", " + lim + lineStyle + ")");
        } else { //start a new plot
            rengine.eval("require(JavaGD)");
            rengine.eval("JavaGD()"); // zacne novy plot
            rengine.eval("plot.ts(" + LOWER + ", " + lim + ", col=\"white\", axes=FALSE, ann=FALSE)"); //hack
            rengine.eval("par(new=TRUE)");
            //draw axes, since this is the first plot
            rengine.eval("plot.ts(" + UPPER + ", " + lim + ", col=\"white\", ylab=\"" +      ""      + "\")");
            rengine.eval("segments(1:" + count + ", " + LOWER + ", 1:" + count + ", " + UPPER + ", " + lim + lineStyle + ")");
        }
        
        
        
        MainFrame.drawNowToThisGDBufferedPanel.setSize(new Dimension(width, height));
        MainFrame.drawNowToThisGDBufferedPanel.initRefresh();
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
    
    private List<Color> getNColours(int n) {
        //spocitat farby z HSV color space, resp. spocitat si prvych X (kde X je nejake rozumne cislo, napriklad 15), a potom
        //  pouzivat zase tych istych 15 farieb, akurat s inym typom ciary
        //ale pre menej ako 15 farieb vypocitat len N farieb, ktore budu od seba dost vzdialene
        return null;
    }
    
    //alebo tu su nejake farby: od zaciatku si z nich brat, a mali by byt vzdy dost vzdialene
    
    public static final String[] COLOURS = new String[]{ //TODO vybrat sem nejake pekne! (rucne) - a hlavne viac
        //TODO urgentne pridat viac farieb, aby to nebolo treba modulit!
        "#FF00FF",
        "#0000FF",
        "#00CD00",
        "#42E99D", //tyrkysova
        "#FF7A4B", //oranzova
        "#EA0D5B", //ruzova
        "#1B60C5", //modra
        "#FFEA48", //zlta
        "#44F04F", //zelena
        "#820000", //hneda
        "#05787E" //ocelova
            
            
        //desperate times call for desperate measures, zopakovane tie farby:
        ,"#FF00FF",
        "#0000FF",
        "#00CD00",
        "#42E99D", //tyrkysova
        "#FF7A4B", //oranzova
        "#EA0D5B", //ruzova
        "#1B60C5", //modra
        "#FFEA48", //zlta
        "#44F04F", //zelena
        "#820000", //hneda
        "#05787E" //ocelova
    };
    
    
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
        String rangeX = "range(c(0, " + (allData.size() + numForecasts) + "))";
        //works with allData.size, because allData isn't cropped by fromTo
        String rangeXrestrictedFromTo = "range(c(max(" + from + "," + rangeX + "[1]), min(" + (to+numForecasts) + "," + rangeX + "[2])))";
        return rangeXrestrictedFromTo;
    }
    
    private static String getRangeXInterval(int sizeDataWithoutFromToCrop, int numForecasts, int from, int to) {
        String rangesX = "range(c(0, " + (sizeDataWithoutFromToCrop + numForecasts) + "))";
        
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
        List<JGDBufferedPanel> panelsList = new ArrayList<>();
        
        if (reports.isEmpty()) {
            return panelsList;
        }
        
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
        
        //potom ich kresli
        if (! diagramPlots.isEmpty()) {
            Rengine rengine = MyRengine.getRengine();
            rengine.eval("require(JavaGD)");
            
            int currentIndex = 0;
            while (currentIndex < diagramPlots.size()) {
                JGDBufferedPanel panel = new JGDBufferedPanel(width, height);
                MainFrame.drawNowToThisGDBufferedPanel = panel;
                rengine.eval("JavaGD()");
                rengine.eval("par(mfrow=c(" + ROWS_DIAGRAMSNN + "," + COLUMNS_DIAGRAMSNN + "))"); //narobim si mriezku
                
                int counter = COLUMNS_DIAGRAMSNN*ROWS_DIAGRAMSNN;
                while ((counter > 0) && (currentIndex < diagramPlots.size())) {
                    rengine.eval(diagramPlots.get(currentIndex));
                    currentIndex++;
                    counter--;
                }
                
                while ((counter > 0) && (currentIndex == diagramPlots.size())) { //dosli mi diagramy a este nie je plna mriezka
                    rengine.eval("plot.new()");
                    counter--;
                }
                
                MainFrame.drawNowToThisGDBufferedPanel.setSize(new Dimension(width, height));
                MainFrame.drawNowToThisGDBufferedPanel.initRefresh();
                
                panelsList.add(panel);
            }
        }
        
        return panelsList;
    }

    public static void drawLegend(JList listPlotLegend, List<Plottable> plots) {
        ((DefaultListModel)(listPlotLegend.getModel())).removeAllElements();
        for (Plottable p : plots) {
            if (! "#FFFFFF".equals(p.getColourInPlot())) {
                ((DefaultListModel)(listPlotLegend.getModel())).addElement(p);
            }
        }
        listPlotLegend.repaint();
    }
    
//    private static String getStrWidth(List<String> list) {
//        StringBuilder rString = new StringBuilder();
//        rString.append("max(c(");
//        boolean next = false;
//        for (String s : list) {
//            if (next) {
//                rString.append(", ");
//            } else {
//                next = true;
//            }
//            rString.append("strwidth(\"").append(s).append("\")");
//        }
//        rString.append("))");
//        return rString.toString();
//    }
    
}
