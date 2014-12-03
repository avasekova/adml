package models.avg;

import static gui.PlotDrawer.COLOURS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;
import utils.imlp.Interval;
import utils.imlp.dist.WeightedEuclideanDistance;

public class AverageSimple extends Average {

    public AverageSimple(boolean avgCTSperM, boolean avgCTS, boolean avgIntTSperM, boolean avgIntTS) {
        super(avgCTSperM, avgCTS, avgIntTSperM, avgIntTS);
    }

    @Override
    public List<TrainAndTestReportCrisp> computeAllCTSAvgs(List<TrainAndTestReportCrisp> reportsCTS) throws IllegalArgumentException {
        List<TrainAndTestReportCrisp> avgReports = new ArrayList<>();
        
        if (reportsCTS.isEmpty()) {
            return avgReports;
        }
        
        if (isAvgCTSperM()) {
            avgReports.addAll(computeAvgCTSperM(reportsCTS));
        }
        if (isAvgCTS()) {
            if (computeAvgCTS(reportsCTS) != null) {
                avgReports.add(computeAvgCTS(reportsCTS));
            } else {
                throw new IllegalArgumentException("not the same percentTrain");
            }
        }
        
        return avgReports;
    }

    @Override
    public List<TrainAndTestReportInterval> computeAllIntTSAvgs(List<TrainAndTestReportInterval> reportsIntTS) throws IllegalArgumentException {
        List<TrainAndTestReportInterval> avgReports = new ArrayList<>();
        
        if (reportsIntTS.isEmpty()) {
            return avgReports;
        }
        
        if (isAvgIntTSperM()) {
            avgReports.addAll(computeAvgIntTSperM(reportsIntTS));
        }
        if (isAvgIntTS()) {
            if (computeAvgIntTS(reportsIntTS) != null) {
                avgReports.add(computeAvgIntTS(reportsIntTS));
            } else {
                throw new IllegalArgumentException("not the same percentTrain");
            }
        }
        
        return avgReports;
    }
    
    
    private List<TrainAndTestReportCrisp> computeAvgCTSperM(List<TrainAndTestReportCrisp> reportsCTS) {
        Map<String, List<TrainAndTestReportCrisp>> mapForAvg = new HashMap<>();
        for (TrainAndTestReportCrisp r : reportsCTS) {
            if (mapForAvg.containsKey(r.getModelName())) {
                mapForAvg.get(r.getModelName()).add(r);
            } else {
                List<TrainAndTestReportCrisp> l = new ArrayList<>();
                l.add(r);
                mapForAvg.put(r.getModelName(), l);
            }
        }
        
        List<TrainAndTestReportCrisp> avgReports = new ArrayList<>();
        Rengine rengine = MyRengine.getRengine();
        
        int colourNumber = reportsCTS.size();
        
        for (String name : mapForAvg.keySet()) {
            List<TrainAndTestReportCrisp> l = mapForAvg.get(name);
            if (l.size() == 1) { //does not make sense to compute average over one series
                //do not compute anything
            } else {
                StringBuilder fittedValsAvgAll = new StringBuilder("(");
                StringBuilder forecastValsTestAvgAll = new StringBuilder("(");
                StringBuilder forecastValsFutureAvgAll = new StringBuilder("(");
                boolean next = false;
                int numForecastsAvg = 0;
                for (TrainAndTestReportCrisp r : l) {
                    if (next) {
                        fittedValsAvgAll.append(" + ");
                        forecastValsTestAvgAll.append(" + ");
                        forecastValsFutureAvgAll.append(" + ");
                    } else {
                        next = true;
                    }
                    fittedValsAvgAll.append(Utils.arrayToRVectorString(r.getFittedValues()));
                    forecastValsTestAvgAll.append(Utils.arrayToRVectorString(r.getForecastValuesTest()));

                    if (r.getForecastValuesFuture().length > 0) {
                        forecastValsFutureAvgAll.append(Utils.arrayToRVectorString(r.getForecastValuesFuture()));
                        numForecastsAvg++;
                    } else {
                        forecastValsFutureAvgAll.append("0");
                    }
                }
                fittedValsAvgAll.append(")/").append(l.size());
                forecastValsTestAvgAll.append(")/").append(l.size());
                forecastValsFutureAvgAll.append(")/").append(numForecastsAvg);

                String avgAll = "c(" + fittedValsAvgAll + "," + forecastValsTestAvgAll + "," + forecastValsFutureAvgAll + ")";
                
                //vyrobit pre tento average novy report a pridat ho do reportsCTS:
                TrainAndTestReportCrisp thisAvgReport = new TrainAndTestReportCrisp(name + "(avg)", false);
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
                thisAvgReport.setPlotCode("plot.ts(" + avgAll + ", lty=2)");
                thisAvgReport.setFittedValues(fittedValsAvg);
                thisAvgReport.setForecastValuesTest(forecastValsTestAvg);
                thisAvgReport.setNumTrainingEntries(fittedValsAvg.length);
                thisAvgReport.setRealOutputsTrain(l.get(0).getRealOutputsTrain());
                thisAvgReport.setRealOutputsTest(l.get(0).getRealOutputsTest());
                avgReports.add(thisAvgReport);

                colourNumber--;
            }
        }
        
        return avgReports;
    }

    
    private TrainAndTestReportCrisp computeAvgCTS(List<TrainAndTestReportCrisp> reportsCTS) {
        if (reportsCTS.size() == 1) { //does not make sense to compute average over one series
            return reportsCTS.get(0);
        } else {
            //first check if all of them have the same percentage of train data
            boolean allTheSame = true;
            int numTrainAll = reportsCTS.get(0).getNumTrainingEntries();
            for (TrainAndTestReportCrisp r : reportsCTS) {
                if (r.getNumTrainingEntries() != numTrainAll) {
                    allTheSame = false;
                    break;
                }
            }

            if (! allTheSame) { //TODO throw an error, we cannot compute it like this
                return null;
            } else {
                StringBuilder fittedValsAvgAll = new StringBuilder("(");
                StringBuilder forecastValsTestAvgAll = new StringBuilder("(");
                StringBuilder forecastValsFutureAvgAll = new StringBuilder("(");
                boolean next = false;
                int numForecastsAvg = 0;
                for (TrainAndTestReportCrisp r : reportsCTS) {
                    if (next) {
                        fittedValsAvgAll.append(" + ");
                        forecastValsTestAvgAll.append(" + ");
                        forecastValsFutureAvgAll.append(" + ");
                    } else {
                        next = true;
                    }
                    fittedValsAvgAll.append(Utils.arrayToRVectorString(r.getFittedValues()));
                    forecastValsTestAvgAll.append(Utils.arrayToRVectorString(r.getForecastValuesTest()));

                    if (r.getForecastValuesFuture().length > 0) {
                        forecastValsFutureAvgAll.append(Utils.arrayToRVectorString(r.getForecastValuesFuture()));
                        numForecastsAvg++;
                    } else {
                        forecastValsFutureAvgAll.append("0");
                    }

                }
                fittedValsAvgAll.append(")/").append(reportsCTS.size());
                forecastValsTestAvgAll.append(")/").append(reportsCTS.size());
                forecastValsFutureAvgAll.append(")/").append(numForecastsAvg);

                String avgAll = "c(" + fittedValsAvgAll + "," + forecastValsTestAvgAll + "," + forecastValsFutureAvgAll + ")";
                
                Rengine rengine = MyRengine.getRengine();
                //a vyrobit pre tento average novy report a pridat ho do reportsCTS:
                TrainAndTestReportCrisp thisAvgReport = new TrainAndTestReportCrisp("(avg)", false);
                REXP getFittedValsAvg = rengine.eval(fittedValsAvgAll.toString());
                double[] fittedValsAvg = getFittedValsAvg.asDoubleArray();
                REXP getForecastValsTestAvg = rengine.eval(forecastValsTestAvgAll.toString());
                double[] forecastValsTestAvg = getForecastValsTestAvg.asDoubleArray();

                ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(
                        Utils.arrayToList(reportsCTS.get(0).getRealOutputsTrain()), 
                        Utils.arrayToList(reportsCTS.get(0).getRealOutputsTest()),
                        Utils.arrayToList(fittedValsAvg), Utils.arrayToList(forecastValsTestAvg), 0);

                thisAvgReport.setErrorMeasures(errorMeasures);
                REXP getForecastValsFutureAvg = rengine.eval(forecastValsFutureAvgAll.toString());
                double[] forecastValsFutureAvg = getForecastValsFutureAvg.asDoubleArray();
                thisAvgReport.setForecastValuesFuture(forecastValsFutureAvg);
                thisAvgReport.setColourInPlot(COLOURS[COLOURS.length-1]);
                thisAvgReport.setPlotCode("plot.ts(" + avgAll + ", lty=2)");
                thisAvgReport.setFittedValues(fittedValsAvg);
                thisAvgReport.setForecastValuesTest(forecastValsTestAvg);
                thisAvgReport.setNumTrainingEntries(fittedValsAvg.length);
                thisAvgReport.setRealOutputsTrain(reportsCTS.get(0).getRealOutputsTrain());
                thisAvgReport.setRealOutputsTest(reportsCTS.get(0).getRealOutputsTest());

                return thisAvgReport;
            }
        }
    }

    
    private List<TrainAndTestReportInterval> computeAvgIntTSperM(List<TrainAndTestReportInterval> reportsIntTS) {
        Map<String, List<TrainAndTestReportInterval>> mapForAvg = new HashMap<>();
        //first go through all the reports once to determine how many of each kind there are:
        for (TrainAndTestReportInterval r : reportsIntTS) {
            if (mapForAvg.containsKey(r.getModelName())) {
                mapForAvg.get(r.getModelName()).add(r);
                List<TrainAndTestReportInterval> l = new ArrayList<>();
                l.add(r);
                mapForAvg.put(r.getModelName(), l);
            }
        }
        
        List<TrainAndTestReportInterval> avgReports = new ArrayList<>();
        Rengine rengine = MyRengine.getRengine();
        
        int colourNumber = reportsIntTS.size();
        
        for (String name : mapForAvg.keySet()) {
            List<TrainAndTestReportInterval> l = mapForAvg.get(name);
            if (l.size() > 1) { //does not make sense to compute average over one series
                StringBuilder avgAllLowersTrain = new StringBuilder("(");
                StringBuilder avgAllLowersTest = new StringBuilder("(");
                StringBuilder avgAllLowersFuture = new StringBuilder("(");
                StringBuilder avgAllUppersTrain = new StringBuilder("(");
                StringBuilder avgAllUppersTest = new StringBuilder("(");
                StringBuilder avgAllUppersFuture = new StringBuilder("(");
                boolean next = false;
                int sizeTrain = 0;
                int sizeTest = 0;
                int sizeFuture = 0;
                int numForecastsFutureAvg = 0;
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

                    avgAllUppersTrain.append(Utils.arrayToRVectorString(r.getFittedValuesUppers()));
                    avgAllUppersTest.append(Utils.arrayToRVectorString(r.getForecastValuesTestUppers()));
                    if (r.getForecastValuesFuture().size() > 0) {
                        avgAllLowersFuture.append(Utils.arrayToRVectorString(r.getForecastValuesFutureLowers()));
                        avgAllUppersFuture.append(Utils.arrayToRVectorString(r.getForecastValuesFutureUppers()));
                        numForecastsFutureAvg++;
                    } else {
                        avgAllLowersFuture.append("0");
                        avgAllUppersFuture.append("0");
                    }

                    sizeTrain = Math.max(sizeTrain, r.getFittedValues().size());
                    sizeTest = Math.max(sizeTest, r.getForecastValuesTest().size());
                    sizeFuture = Math.max(sizeFuture, r.getForecastValuesFuture().size());
                }
                avgAllLowersTrain.append(")/").append(l.size());
                avgAllLowersTest.append(")/").append(l.size());
                avgAllLowersFuture.append(")/").append(numForecastsFutureAvg);
                avgAllUppersTrain.append(")/").append(l.size());
                avgAllUppersTest.append(")/").append(l.size());
                avgAllUppersFuture.append(")/").append(numForecastsFutureAvg);

                rengine.eval("lowerTrain <- " + avgAllLowersTrain.toString());
                rengine.eval("lowerTest <- " + avgAllLowersTest.toString());
                rengine.eval("lowerFuture <- " + avgAllLowersFuture.toString());
                rengine.eval("upperTrain <- " + avgAllUppersTrain.toString());
                rengine.eval("upperTest <- " + avgAllUppersTest.toString());
                rengine.eval("upperFuture <- " + avgAllUppersFuture.toString());
                
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
                //       - maju. musia mat. neprerabat.
                List<Double> realValuesLowers = l.get(0).getRealValuesLowers();
                List<Double> realValuesUppers = l.get(0).getRealValuesUppers();
                List<Double> realValuesLowersTrain = realValuesLowers.subList(0, l.get(0).getNumTrainingEntries());
                List<Double> realValuesUppersTrain = realValuesUppers.subList(0, l.get(0).getNumTrainingEntries());
                List<Double> realValuesLowersTest = realValuesLowers.subList(l.get(0).getNumTrainingEntries(), realValuesLowers.size());
                List<Double> realValuesUppersTest = realValuesUppers.subList(l.get(0).getNumTrainingEntries(), realValuesUppers.size());

                List<Interval> realValuesTrain = Utils.zipLowerUpperToIntervals(realValuesLowersTrain, realValuesUppersTrain);
                List<Interval> realValuesTest = Utils.zipLowerUpperToIntervals(realValuesLowersTest, realValuesUppersTest);

                ErrorMeasuresInterval errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresInterval(realValuesTrain, 
                        realValuesTest, allIntervalsTrain, allIntervalsTest, new WeightedEuclideanDistance(0.5), 0);
                //TODO zmenit! zatial sa to pocita WeightedEuclid, ale dat tam hocijaku distance!

                TrainAndTestReportInterval reportAvgMethod = new TrainAndTestReportInterval(l.get(0).getModelName() + "(avg)", false);
                reportAvgMethod.setErrorMeasures(errorMeasures);
                reportAvgMethod.setColourInPlot(COLOURS[colourNumber % COLOURS.length]);
                reportAvgMethod.setFittedValues(allIntervalsTrain);
                reportAvgMethod.setForecastValuesTest(allIntervalsTest);
                reportAvgMethod.setForecastValuesFuture(realValuesTest);

                REXP getAllLowersFuture = rengine.eval("lowerFuture");
                List<Double> allLowersFutureList = Utils.arrayToList(getAllLowersFuture.asDoubleArray());
                REXP getAllUppersFuture = rengine.eval("upperFuture");
                List<Double> allUppersFutureList = Utils.arrayToList(getAllUppersFuture.asDoubleArray());
                List<Interval> allIntervalsFuture = Utils.zipLowerUpperToIntervals(allLowersFutureList, allUppersFutureList);
                reportAvgMethod.setForecastValuesFuture(allIntervalsFuture);
                reportAvgMethod.setNumTrainingEntries(reportsIntTS.get(0).getNumTrainingEntries());
                realValuesTrain.addAll(realValuesTest);
                reportAvgMethod.setRealValues(realValuesTrain);

                avgReports.add(reportAvgMethod);

                colourNumber--;
            }
        }
        
        return avgReports;
    }

    
    private TrainAndTestReportInterval computeAvgIntTS(List<TrainAndTestReportInterval> reportsIntTS) {
        //first check if all of them have the same percentage of train data
        boolean allTheSame = true;
        int numTrainAll = reportsIntTS.get(0).getNumTrainingEntries();
        for (TrainAndTestReportInterval r : reportsIntTS) {
            if (r.getNumTrainingEntries() != numTrainAll) {
                allTheSame = false;
                break;
            }
        }

        if (! allTheSame) { //throw an error, we cannot compute it like this
            return null;
        } else {
            Rengine rengine = MyRengine.getRengine();
            
            if (reportsIntTS.size() == 1) { //does not make sense to compute average over one series
                return reportsIntTS.get(0);
            } else {
                StringBuilder avgAllLowersTrain = new StringBuilder("(");
                StringBuilder avgAllLowersTest = new StringBuilder("(");
                StringBuilder avgAllLowersFuture = new StringBuilder("(");
                StringBuilder avgAllUppersTrain = new StringBuilder("(");
                StringBuilder avgAllUppersTest = new StringBuilder("(");
                StringBuilder avgAllUppersFuture = new StringBuilder("(");
                boolean next = false;
                int sizeTrain = 0;
                int sizeTest = 0;
                int sizeFuture = 0;
                int numForecastsFutureAvg = 0;
                for (TrainAndTestReportInterval r : reportsIntTS) {
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

                    avgAllUppersTrain.append(Utils.arrayToRVectorString(r.getFittedValuesUppers()));
                    avgAllUppersTest.append(Utils.arrayToRVectorString(r.getForecastValuesTestUppers()));
                    if (r.getForecastValuesFuture().size() > 0) {
                        avgAllLowersFuture.append(Utils.arrayToRVectorString(r.getForecastValuesFutureLowers()));
                        avgAllUppersFuture.append(Utils.arrayToRVectorString(r.getForecastValuesFutureUppers()));
                        numForecastsFutureAvg++;
                    } else {
                        avgAllLowersFuture.append("0");
                        avgAllUppersFuture.append("0");
                    }

                    sizeTrain = Math.max(sizeTrain, r.getFittedValues().size());
                    sizeTest = Math.max(sizeTest, r.getForecastValuesTest().size());
                    sizeFuture = Math.max(sizeFuture, r.getForecastValuesFuture().size());
                }
                avgAllLowersTrain.append(")/").append(reportsIntTS.size());
                avgAllLowersTest.append(")/").append(reportsIntTS.size());
                avgAllLowersFuture.append(")/").append(numForecastsFutureAvg);
                avgAllUppersTrain.append(")/").append(reportsIntTS.size());
                avgAllUppersTest.append(")/").append(reportsIntTS.size());
                avgAllUppersFuture.append(")/").append(numForecastsFutureAvg);

                rengine.eval("lowerTrain <- " + avgAllLowersTrain.toString());
                rengine.eval("lowerTest <- " + avgAllLowersTest.toString());
                rengine.eval("lowerFuture <- " + avgAllLowersFuture.toString());
                rengine.eval("upperTrain <- " + avgAllUppersTrain.toString());
                rengine.eval("upperTest <- " + avgAllUppersTest.toString());
                rengine.eval("upperFuture <- " + avgAllUppersFuture.toString());
                
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

                List<Double> realValuesLowers = reportsIntTS.get(0).getRealValuesLowers();
                List<Double> realValuesUppers = reportsIntTS.get(0).getRealValuesUppers();
                List<Double> realValuesLowersTrain = realValuesLowers.subList(0, reportsIntTS.get(0).getNumTrainingEntries());
                List<Double> realValuesUppersTrain = realValuesUppers.subList(0, reportsIntTS.get(0).getNumTrainingEntries());
                List<Double> realValuesLowersTest = realValuesLowers.subList(reportsIntTS.get(0).getNumTrainingEntries(), realValuesLowers.size());
                List<Double> realValuesUppersTest = realValuesUppers.subList(reportsIntTS.get(0).getNumTrainingEntries(), realValuesUppers.size());

                List<Interval> realValuesTrain = Utils.zipLowerUpperToIntervals(realValuesLowersTrain, realValuesUppersTrain);
                List<Interval> realValuesTest = Utils.zipLowerUpperToIntervals(realValuesLowersTest, realValuesUppersTest);

                ErrorMeasuresInterval errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresInterval(realValuesTrain, 
                        realValuesTest, allIntervalsTrain, allIntervalsTest, new WeightedEuclideanDistance(0.5), 0);
                //TODO zmenit! zatial sa to pocita WeightedEuclid, ale dat tam hocijaku distance!

                TrainAndTestReportInterval reportAvgAllITS = new TrainAndTestReportInterval("(avg int)", false);
                reportAvgAllITS.setErrorMeasures(errorMeasures);
                reportAvgAllITS.setColourInPlot(COLOURS[COLOURS.length-1]);
                reportAvgAllITS.setFittedValues(allIntervalsTrain);
                reportAvgAllITS.setForecastValuesTest(allIntervalsTest);
                reportAvgAllITS.setForecastValuesFuture(realValuesTest);

                REXP getAllLowersFuture = rengine.eval("lowerFuture");
                List<Double> allLowersFutureList = Utils.arrayToList(getAllLowersFuture.asDoubleArray());
                REXP getAllUppersFuture = rengine.eval("upperFuture");
                List<Double> allUppersFutureList = Utils.arrayToList(getAllUppersFuture.asDoubleArray());
                List<Interval> allIntervalsFuture = Utils.zipLowerUpperToIntervals(allLowersFutureList, allUppersFutureList);
                reportAvgAllITS.setForecastValuesFuture(allIntervalsFuture);
                reportAvgAllITS.setNumTrainingEntries(reportsIntTS.get(0).getNumTrainingEntries());
                realValuesTrain.addAll(realValuesTest);
                reportAvgAllITS.setRealValues(realValuesTrain);

                return reportAvgAllITS;
            }
        }
    }
}
