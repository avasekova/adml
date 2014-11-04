package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JScrollPane;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import org.rosuda.javaGD.JGDBufferedPanel;
import utils.Const;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;
import utils.ugliez.CallParamsDrawPlots;
import utils.ugliez.CallParamsDrawPlotsITS;
import utils.ugliez.PlotStateKeeper;

//TODO preco po vykresleni grafu ten obrazok blikne? niekde sa nieco kresli dvakrat.
public class PlotDrawer {
    //TODO pridat abline pre kazde oddelenie training a testing data! ptz rozne metody maju rozne pomery.
    //  (alebo nejak inak odlisit. trosku ina farba? iny styl ciary? ina hrubka? vsetko sa mi zda zle.)
    
    
    //TODO toto by cele chcelo upratat, prekopar, mozno refaktorovat do viacerych tried
    //a pridat sem vsetko, co sa tyka kreslenia - napr. i v DataTableModel je nieco, mozno v MainFrame, a tak.
    
    private static final int COLUMNS_DIAGRAMSNN = 3;
    
    //drawNew je true, ak sa maju zmenit maximalne medze obrazku, tj kresli sa to z Run, Plot CTS, Plot ITS, ACF, PACF
    //drawNew je false, ak sa len zoomuje aktualny obrazok a nekresli sa novy, tj zo Zoom CTS, Zoom ITS
    public static void drawPlots(boolean drawNew, CallParamsDrawPlots par) {
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
            rangeYInt = getRangeYInterval(par.getReportsITS());
        }
        
        drawPlots(drawNew, par, rangeXCrisp, rangeYCrisp, rangeXInt, rangeYInt);
    }
    
    public static void drawPlots(boolean drawNew, CallParamsDrawPlots par,
                                 String rangeXCrisp, String rangeYCrisp, String rangeXInt, String rangeYInt) {
        if (par.getReportsCTS().isEmpty() && par.getReportsITS().isEmpty()) {
            return;
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
        
        MainFrame.drawNowToThisGDBufferedPanel = canvasToUse;
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(JavaGD)");
        rengine.eval("JavaGD()");
        
        if ((! reportsCTS.isEmpty()) && (! reportsIntTS.isEmpty())) { //budem vykreslovat oba naraz
            rengine.eval("par(mfrow=c(1,2))"); //daj dva grafy vedla seba. potom normalne zavolat dva ploty.
        }
        
        int colourNumber = 0;
        if (! reportsCTS.isEmpty()) { //plot CTS
            allDataCTS = allDataCTS.subList(from, Math.min(to+numForecasts, allDataCTS.size()));
            
            List<String> names = new ArrayList<>();
            List<String> colours = new ArrayList<>();
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
                    //add a dashed vertical line to separate test and train
                    rengine.eval("abline(v = " + (r.getNumTrainingEntries() + par.getFrom()) + ", lty = 2, lwd=2, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\")");
                    //remember these for the legend
                    names.add(r.getModelName() + r.getModelDescription());
                    colours.add(COLOURS[colourNumber % COLOURS.length]);
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

                        //pridat do legendy
                        names.add(name + "(avg)");
                        colours.add(COLOURS[colourNumber % COLOURS.length]);
                        colourNumber++;
                        
                        //vyrobit pre tento average novy report a pridat ho do reportsCTS:
                        TrainAndTestReportCrisp thisAvgReport = new TrainAndTestReportCrisp(name + "(avg)");
                        REXP getFittedValsAvg = rengine.eval(fittedValsAvgAll.toString());
                        double[] fittedValsAvg = getFittedValsAvg.asDoubleArray();
                        REXP getForecastValsTestAvg = rengine.eval(forecastValsTestAvgAll.toString());
                        double[] forecastValsTestAvg = getForecastValsTestAvg.asDoubleArray();
                        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils
                                .computeAllErrorMeasuresCrisp(Utils.arrayToList(l.get(0).getRealOutputsTrain()),
                                Utils.arrayToList(l.get(0).getRealOutputsTest()), Utils.arrayToList(fittedValsAvg),
                                Utils.arrayToList(forecastValsTestAvg));
                        thisAvgReport.setErrorMeasures(errorMeasures);
                        REXP getForecastValsFutureAvg = rengine.eval(forecastValsFutureAvgAll.toString());
                        double[] forecastValsFutureAvg = getForecastValsFutureAvg.asDoubleArray();
                        thisAvgReport.setForecastValuesFuture(forecastValsFutureAvg);
                        avgReportsToAdd.add(thisAvgReport);
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

                    //pridat do legendy
                    names.add("(avg)");
                    colours.add(COLOURS[colourNumber % COLOURS.length]);
                    colourNumber++;
                    
                    //a vyrobit pre tento average novy report a pridat ho do reportsCTS:
                    TrainAndTestReportCrisp thisAvgReport = new TrainAndTestReportCrisp("(avg)");
                    REXP getFittedValsAvg = rengine.eval(fittedValsAvgAll.toString());
                    double[] fittedValsAvg = getFittedValsAvg.asDoubleArray();
                    REXP getForecastValsTestAvg = rengine.eval(forecastValsTestAvgAll.toString());
                    double[] forecastValsTestAvg = getForecastValsTestAvg.asDoubleArray();
                    ErrorMeasuresCrisp errorMeasures = new ErrorMeasuresCrisp();
                    List<Double> allRealDataTrainAndTest = new ArrayList<>(); //will have the NA vals of the first report
                    //but they would've gotten erased anyway
                    allRealDataTrainAndTest.addAll(Utils.arrayToList(reportsCTS.get(0).getRealOutputsTrain()));
                    allRealDataTrainAndTest.addAll(Utils.arrayToList(reportsCTS.get(0).getRealOutputsTest()));
                    List<Double> allFitDataTrainAndTest = new ArrayList<>();
                    allFitDataTrainAndTest.addAll(Utils.arrayToList(fittedValsAvg));
                    allFitDataTrainAndTest.addAll(Utils.arrayToList(forecastValsTestAvg));
                    errorMeasures.setMEtrain(ErrorMeasuresUtils.ME(allRealDataTrainAndTest, allFitDataTrainAndTest));
                    errorMeasures.setMEtest(0.0);
                    errorMeasures.setRMSEtrain(ErrorMeasuresUtils.RMSE(allRealDataTrainAndTest, allFitDataTrainAndTest));
                    errorMeasures.setRMSEtest(0.0);
                    errorMeasures.setMAEtrain(ErrorMeasuresUtils.MAE(allRealDataTrainAndTest, allFitDataTrainAndTest));
                    errorMeasures.setMAEtest(0.0);
                    errorMeasures.setMPEtrain(ErrorMeasuresUtils.MPE(allRealDataTrainAndTest, allFitDataTrainAndTest));
                    errorMeasures.setMPEtest(0.0);
                    errorMeasures.setMAPEtrain(ErrorMeasuresUtils.MAPE(allRealDataTrainAndTest, allFitDataTrainAndTest));
                    errorMeasures.setMAPEtest(0.00);
                    errorMeasures.setMASEtrain(ErrorMeasuresUtils.MASE(allRealDataTrainAndTest, allFitDataTrainAndTest));
                    errorMeasures.setMASEtest(0.0);
                    errorMeasures.setMSEtrain(ErrorMeasuresUtils.MSE(allRealDataTrainAndTest, allFitDataTrainAndTest));
                    errorMeasures.setMSEtest(0.0);
                    errorMeasures.setTheilUtrain(ErrorMeasuresUtils.theilsU(allRealDataTrainAndTest, allFitDataTrainAndTest));
                    errorMeasures.setTheilUtest(0.0);
                    
                    thisAvgReport.setErrorMeasures(errorMeasures);
                    REXP getForecastValsFutureAvg = rengine.eval(forecastValsFutureAvgAll.toString());
                    double[] forecastValsFutureAvg = getForecastValsFutureAvg.asDoubleArray();
                    thisAvgReport.setForecastValuesFuture(forecastValsFutureAvg);
                    avgReportsToAdd.add(thisAvgReport);
                }
            }
            
            //na zaver slavnostne pridat nove reporty do reportsCTS, ked to uz neschaosi nic ine:
            reportsCTS.addAll(avgReportsToAdd);
            
            
            rengine.assign("all.data", Utils.listToArray(allDataCTS));
            rengine.eval("par(new=TRUE)");
            rengine.eval("plot.ts(c(rep(NA, " + par.getFrom() + "), all.data), xlim = " + rangeXCrisp + ", ylim = " + rangeYCrisp + ", "
                    + "ylab=\"" + colname_CTS + "\","
                    + "lwd=2, col=\"#444444\")");
            rengine.eval("abline(v = " + (to) + ", lty = 3)"); //dashed vertical line to separate forecasts
            
            //add legend
            rengine.eval("legend(\"topleft\", "      
                                + "inset = c(0,-0.11), "
                                + "legend = " + getRString(names) + ", "
                                + "fill = " + getRString(colours) + ", "
                                + "horiz = TRUE, "
                                + "box.lty = 0, "
                                + "cex = 0.8, "
                                + "text.width = 3, " //TODO pohrat sa s tymto, a urobit to nejak univerzalne, aby tam vzdy vosli vsetky nazvy
                                + "xpd = TRUE)");
            
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
            List<String> names = new ArrayList<>();
            List<String> colours = new ArrayList<>();
            boolean next = false;
            Map<String, List<TrainAndTestReportInterval>> mapForAvg = new HashMap<>();
            List<Double> listAllLowersEver = new ArrayList<>();
            List<Double> listAllUppersEver = new ArrayList<>();
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
                
                if (avgIntTS) { //nasysli si ciselka
                    listAllLowersEver.addAll(Utils.arrayToList(r.getFittedValuesLowers()));
                    listAllLowersEver.addAll(Utils.arrayToList(r.getForecastValuesTestLowers()));
                    listAllLowersEver.addAll(Utils.arrayToList(r.getForecastValuesFutureLowers()));
                    
                    listAllUppersEver.addAll(Utils.arrayToList(r.getFittedValuesUppers()));
                    listAllUppersEver.addAll(Utils.arrayToList(r.getForecastValuesTestUppers()));
                    listAllUppersEver.addAll(Utils.arrayToList(r.getForecastValuesFutureUppers()));
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
                    
                    //remember these for the legend
                    names.add(r.getModelName() + r.getModelDescription());
                    colours.add(COLOURS[colourNumber % COLOURS.length]);
                }
                
                colourNumber++;
            }
            
            //now draw the average series
            if (avgIntTSperMethod) {
                for (String name : mapForAvg.keySet()) {
                    List<TrainAndTestReportInterval> l = mapForAvg.get(name);
                    if (l.size() > 1) { //does not make sense to compute average over one series
                        StringBuilder avgAllLowers = new StringBuilder("(");
                        StringBuilder avgAllUppers = new StringBuilder("(");
                        next = false;
                        int size = 0;
                        for (TrainAndTestReportInterval r : l) {
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
                        avgAllLowers.append(")/").append(l.size());
                        avgAllUppers.append(")/").append(l.size());

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
                                + ", lwd=5, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\")");
                        rengine.eval("abline(v = " + (l.get(0).getNumTrainingEntries() + par.getFrom()) + ", lty = 2, lwd=2, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\")");
                        
                        //pridat do legendy
                        names.add(name + "(avg)");
                        colours.add(COLOURS[colourNumber % COLOURS.length]);
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
                    
                    //pridat do legendy
                    names.add("(avg)");
                    colours.add(COLOURS[colourNumber % COLOURS.length]);
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
            
            //add legend
            rengine.eval("legend(\"topleft\", "      
                                + "inset = c(0,-0.11), "
                                + "legend = " + getRString(names) + ", "
                                + "fill = " + getRString(colours) + ", "
                                + "horiz = TRUE, "
                                + "box.lty = 0, "
                                + "cex = 0.8, "
                                + "text.width = 3, " //TODO pohrat sa s tymto, a urobit to nejak univerzalne, aby tam vzdy vosli vsetky nazvy
                                + "xpd = TRUE)");
            
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
        //TODO kresli sa dvakrat! skusit http://stackoverflow.com/questions/8067844/paint-in-java-applet-is-called-twice-for-no-reason
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
        
        List<String> names = new ArrayList<>();
        List<String> colours = new ArrayList<>();
        int colourNumber = 0;
        
        boolean next = false;
        for (IntervalNamesCentreRadius interval : par.getListCentreRadius()) {
            //remember these for the legend
            names.add(interval.toString());
            colours.add(COLOURS[colourNumber % COLOURS.length]);
            
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
            names.add(interval.toString());
            colours.add(COLOURS[colourNumber % COLOURS.length]);
            
            String lineStyle = ", lwd=4, col=\"" + COLOURS[colourNumber % COLOURS.length] + "\"";
            drawPlotITS_LBUB(par.getWidth(), par.getHeight(), par.getDataTableModel().getDataForColname(interval.getLowerBound()),
                    par.getDataTableModel().getDataForColname(interval.getUpperBound()), next, lineStyle, rangeX, rangeY);
            if (! next) {
                next = true;
            }
            
            colourNumber++;
        }
        
        if ((! par.getListCentreRadius().isEmpty()) || (! par.getListLowerUpper().isEmpty())) {
            //add legend
            rengine.eval("legend(\"topleft\", "      
                                + "inset = c(0,-0.11), "
                                + "legend = " + getRString(names) + ", "
                                + "fill = " + getRString(colours) + ", "
                                + "horiz = TRUE, "
                                + "box.lty = 0, "
                                + "cex = 0.8, "
                                + "text.width = 15, " //TODO pohrat sa s tymto, a urobit to nejak univerzalne, aby tam vzdy vosli vsetky nazvy
                                + "xpd = TRUE)");
        }
        
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
            rengine.eval("plot.ts(" + UPPER + ", " + lim + ", col=\"white\", ylab=\"" +      "<<TODO ylab>>"      + "\")");
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
    
    private List<Color> getNColours(int n) {
        //spocitat farby z HSV color space, resp. spocitat si prvych X (kde X je nejake rozumne cislo, napriklad 15), a potom
        //  pouzivat zase tych istych 15 farieb, akurat s inym typom ciary
        //ale pre menej ako 15 farieb vypocitat len N farieb, ktore budu od seba dost vzdialene
        return null;
    }
    
    //alebo tu su nejake farby: od zaciatku si z nich brat, a mali by byt vzdy dost vzdialene
    
    public static final String[] COLOURS = new String[]{ //TODO vybrat sem nejake pekne! (rucne) - a hlavne viac
        //TODO urgentne pridat viac farieb, aby to nebolo treba modulit!
        "magenta",
        "blue",
        "green3",
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
            if (next) {
                rangesY.append(", ");
            } else {
                next = true;
            }
            rangesY.append(Utils.minArray(r.getFittedValues())).append(", ");
            rangesY.append(Utils.maxArray(r.getFittedValues()));
            
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
        }
        //a zahrnut aj povodne data:
        rangesY.append(", ").append(Utils.minList(allData)).append(", ").append(Utils.maxList(allData));
        rangesY.append("))");
        
        return rangesY.toString();
    }
    
    private static String getRangeYInterval(List<TrainAndTestReportInterval> reports) {
        StringBuilder rangesY = new StringBuilder("range(c(");
        boolean next = false;
        for (TrainAndTestReportInterval r : reports) {
            if (next) {
                rangesY.append(", ");
            } else {
                next = true;
            }
            rangesY.append(Utils.minArray(r.getFittedValuesLowers())).append(", ");
            rangesY.append(Utils.maxArray(r.getFittedValuesLowers())).append(", ");
            rangesY.append(Utils.minArray(r.getFittedValuesUppers())).append(", ");
            rangesY.append(Utils.maxArray(r.getFittedValuesUppers())).append(", ");
            rangesY.append(Utils.minArray(r.getForecastValuesTestLowers())).append(", ");
            rangesY.append(Utils.maxArray(r.getForecastValuesTestLowers())).append(", ");
            rangesY.append(Utils.minArray(r.getForecastValuesTestUppers())).append(", ");
            rangesY.append(Utils.maxArray(r.getForecastValuesTestUppers()));
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
            rangesY.append(", ").append(Utils.minList(realDataLower)).append(", ").append(Utils.maxList(realDataLower));
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
    
    public static void drawDiagrams(JGDBufferedPanel canvasToUse, int width, int height, List<TrainAndTestReport> reports) {
        if (reports.isEmpty()) {
            return;
        }
        
        MainFrame.drawNowToThisGDBufferedPanel = canvasToUse;
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(JavaGD)");
        rengine.eval("JavaGD()");
        
        List<String> diagramPlots = new ArrayList<>();
        for (TrainAndTestReport r : reports) {
            if (! "".equals(r.getNnDiagramPlotCode())) {
                if (r.getNnDiagramPlotCode().contains(";")) {
                    //fuj, hack, TODO spravit lepsie ako split na dva, resp. prisposobovat v buducnosti
                    String firstPlot = r.getNnDiagramPlotCode().split(";")[0];
                    StringBuilder diagramPlotCode = new StringBuilder(firstPlot);
                    diagramPlotCode.insert(firstPlot.length() - 1, ", main=\"" + r.getModelName() + r.getModelDescription() + " (Center)\"");
                    diagramPlots.add(diagramPlotCode.toString());
                    
                    String secondPlot = r.getNnDiagramPlotCode().split(";")[1];
                    diagramPlotCode = new StringBuilder(secondPlot);
                    diagramPlotCode.insert(secondPlot.length() - 1, ", main=\"" + r.getModelName() + r.getModelDescription() + " (Radius)\"");
                    diagramPlots.add(diagramPlotCode.toString());
                } else {
                    StringBuilder diagramPlotCode = new StringBuilder(r.getNnDiagramPlotCode());
                    diagramPlotCode.insert(r.getNnDiagramPlotCode().length() - 1, ", main=\"" + r.getModelName() + r.getModelDescription() + "\"");
                    diagramPlots.add(diagramPlotCode.toString());
                }
            }
        }
        
        if (! diagramPlots.isEmpty()) {
        int rows = diagramPlots.size()/COLUMNS_DIAGRAMSNN + 1;
            rengine.eval("par(mfrow=c(" + rows + "," + COLUMNS_DIAGRAMSNN + "))"); //narobim si mriezku
            
            //a teraz idem postupne vyplnat mriezku diagramami:
            for (String dPlot : diagramPlots) {
                rengine.eval(dPlot);
            }
            
            //nakoniec doplnim prazdne policka do mriezky plotov, lebo to na ne caka a mohlo by to nerobit dobrotu
            for (int i = 0; i < (rows*COLUMNS_DIAGRAMSNN)-diagramPlots.size(); i++) { //kolko mam este nevyplnenych
                rengine.eval("plot.new()");
            }
        }
        
        int numRows = diagramPlots.size()/COLUMNS_DIAGRAMSNN + 1;
        MainFrame.drawNowToThisGDBufferedPanel.setSize(new Dimension(width, (height/3)*numRows));
        MainFrame.drawNowToThisGDBufferedPanel.initRefresh();
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
