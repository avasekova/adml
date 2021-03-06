package models.avg;

import models.Model;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.*;
import utils.imlp.Interval;
import utils.imlp.dist.WeightedEuclideanDistance;

import java.util.*;


/*
To create a new Average implementation:
- create a class extending Average
- add checkboxes in the GUI (probably 4: CTS_perM, CTS, ITS_perM, ITS)
- add actionlisteners to maybe disable AVGONLY
- add this average to MainFrame.getAllAvgs
*/
public abstract class Average {
    private static final Logger logger = LoggerFactory.getLogger(Average.class);
    
    public abstract double getWeightForModelTrain(TrainAndTestReportCrisp report);
    public abstract double getWeightForModelTest(TrainAndTestReportCrisp report);
    public abstract double getWeightForModelFuture(TrainAndTestReportCrisp report);
    public abstract double getWeightForModelTrain(TrainAndTestReportInterval report);
    public abstract double getWeightForModelTest(TrainAndTestReportInterval report);
    public abstract double getWeightForModelFuture(TrainAndTestReportInterval report);
    public abstract String getName();
    
    protected Map<String, Double> weightsCrisp = new HashMap<>();
    protected Map<String, Double> weightsInterval = new HashMap<>();
    
    private boolean avgCTSperM;
    private boolean avgCTS;
    private boolean avgIntTSperM;
    private boolean avgIntTS;

    public Average(boolean avgCTSperM, boolean avgCTS, boolean avgIntTSperM, boolean avgIntTS) {
        this.avgCTSperM = avgCTSperM;
        this.avgCTS = avgCTS;
        this.avgIntTSperM = avgIntTSperM;
        this.avgIntTS = avgIntTS;
    }

    public List<TrainAndTestReportCrisp> computeAllCTSAvgs(List<TrainAndTestReportCrisp> reportsCTS) throws IllegalArgumentException {
        List<TrainAndTestReportCrisp> avgReports = new ArrayList<>();
        
        if (reportsCTS.isEmpty()) {
            return avgReports;
        }
        
        if (avgCTSperM) {
            avgReports.addAll(computeAvgCTSperM(reportsCTS));
        }
        if (avgCTS) {
            if (computeAvgCTS(reportsCTS) != null) {
                avgReports.add(computeAvgCTS(reportsCTS));
            } else {
                throw new IllegalArgumentException("not the same percentTrain");
            }
        }
        
        return avgReports;
    }

    public List<TrainAndTestReportInterval> computeAllIntTSAvgs(List<TrainAndTestReportInterval> reportsIntTS) throws IllegalArgumentException {
        List<TrainAndTestReportInterval> avgReports = new ArrayList<>();
        
        if (reportsIntTS.isEmpty()) {
            return avgReports;
        }
        
        if (avgIntTSperM) {
            avgReports.addAll(computeAvgIntTSperM(reportsIntTS));
        }
        if (avgIntTS) {
            if (computeAvgIntTS(reportsIntTS) != null) {
                avgReports.add(computeAvgIntTS(reportsIntTS));
            } else {
                throw new IllegalArgumentException("not the same percentTrain");
            }
        }
        
        return avgReports;
    }
    
    
    public List<TrainAndTestReportCrisp> computeAvgCTSperM(List<TrainAndTestReportCrisp> reportsCTS) {
        weightsCrisp.clear();
        weightsInterval.clear();
        
        Map<Model, List<TrainAndTestReportCrisp>> mapForAvg = new HashMap<>();
        for (TrainAndTestReportCrisp r : reportsCTS) {
            if (mapForAvg.containsKey(r.getModel())) {
                mapForAvg.get(r.getModel()).add(r);
            } else {
                List<TrainAndTestReportCrisp> l = new ArrayList<>();
                l.add(r);
                mapForAvg.put(r.getModel(), l);
            }
        }
        
        List<TrainAndTestReportCrisp> avgReports = new ArrayList<>();
        
        for (Model model : mapForAvg.keySet()) {
            List<TrainAndTestReportCrisp> l = mapForAvg.get(model);
            if (l.size() == 1) { //does not make sense to compute average over one series
                //do not compute anything
            } else {
                TrainAndTestReportCrisp thisAvgReport = computeAvgCTS(l, model);
                if (thisAvgReport != null) {
                    avgReports.add(thisAvgReport);
                } else { //should never happen for the same method
                    System.err.println("nerovnake percenttrain v ramci 1 modelu pri avg CTS per method :/");
                }
            }
        }
        
        return avgReports;
    }
    
    public TrainAndTestReportCrisp computeAvgCTS(List<TrainAndTestReportCrisp> reportsCTS) {
        return computeAvgCTS(reportsCTS, Model.ALL);
    }
    
    public TrainAndTestReportCrisp computeAvgCTS(List<TrainAndTestReportCrisp> reportsCTS, Model model) {
        if (reportsCTS.size() == 1) { //does not make sense to compute average over one series
            return reportsCTS.get(0); //TODO do not return anything (but take care of it on the receiving end), because otherwise it draws twice. but then problems with "drawOnlyAVG"
        } else {
            if (! allTheSamePercentTrain(reportsCTS)) {
                return null;
            } else {
                StringBuilder fittedValsAvgAll = new StringBuilder("(");
                StringBuilder forecastValsTestAvgAll = new StringBuilder("(");
                StringBuilder forecastValsFutureAvgAll = new StringBuilder("(");
                StringBuilder sumWeightsTrain = new StringBuilder("(");
                StringBuilder sumWeightsTest = new StringBuilder("(");
                StringBuilder sumWeightsFuture = new StringBuilder("(");
                boolean next = false;
                for (TrainAndTestReportCrisp r : reportsCTS) {
                    if (next) {
                        fittedValsAvgAll.append(" + ");
                        forecastValsTestAvgAll.append(" + ");
                        forecastValsFutureAvgAll.append(" + ");
                        sumWeightsTrain.append(" + ");
                        sumWeightsTest.append(" + ");
                        sumWeightsFuture.append(" + ");
                    } else {
                        next = true;
                    }
                    
                    double weightTrain = getWeightForModelTrain(r);
                    double weightTest = getWeightForModelTest(r);
                    double weightFuture = getWeightForModelFuture(r);
                    weightsCrisp.put(r.toString(), weightFuture);
                    
                    sumWeightsTrain.append(weightTrain);
                    sumWeightsTest.append(weightTest);
                    
                    fittedValsAvgAll.append(weightTrain).append("*")
                            .append(Utils.arrayToRVectorString(r.getFittedValues()));
                    forecastValsTestAvgAll.append(weightTest).append("*")
                            .append(Utils.arrayToRVectorString(r.getForecastValuesTest()));

                    forecastValsFutureAvgAll.append(weightFuture).append("*");
                    if (r.getForecastValuesFuture().length > 0) {
                        forecastValsFutureAvgAll.append(Utils.arrayToRVectorString(r.getForecastValuesFuture()));
                        
                        sumWeightsFuture.append(weightFuture);
                    } else {
                        forecastValsFutureAvgAll.append("0");
                        
                        sumWeightsFuture.append("0");
                    }

                }
                sumWeightsTrain.append(")");
                sumWeightsTest.append(")");
                sumWeightsFuture.append(")");
                
                fittedValsAvgAll.append(")/").append(sumWeightsTrain);
                forecastValsTestAvgAll.append(")/").append(sumWeightsTest);
                forecastValsFutureAvgAll.append(")/").append(sumWeightsFuture);

                String avgAll = "c(" + fittedValsAvgAll + "," + forecastValsTestAvgAll + "," + forecastValsFutureAvgAll + ")";
                
                MyRengine rengine = MyRengine.getRengine();
                //and create a new report for this avg and add it to reportsCTS:
                TrainAndTestReportCrisp thisAvgReport = new TrainAndTestReportCrisp(model, "(" + getName() + ")", true);
                double[] fittedValsAvg = rengine.evalAndReturnArray(fittedValsAvgAll.toString());
                double[] forecastValsTestAvg = rengine.evalAndReturnArray(forecastValsTestAvgAll.toString());

                ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(
                        Utils.arrayToList(reportsCTS.get(0).getRealOutputsTrain()), 
                        Utils.arrayToList(reportsCTS.get(0).getRealOutputsTest()),
                        Utils.arrayToList(fittedValsAvg), Utils.arrayToList(forecastValsTestAvg), 0);

                thisAvgReport.setErrorMeasures(errorMeasures);
                double[] forecastValsFutureAvg = rengine.evalAndReturnArray(forecastValsFutureAvgAll.toString());
                thisAvgReport.setForecastValuesFuture(forecastValsFutureAvg);
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

    public static <T extends TrainAndTestReport> boolean allTheSamePercentTrain(List<T> reports) { //TODO a nicer way? :)
        int numTrainAll = -1;
        for (TrainAndTestReport r : reports) {
            if (! Model.FLEXIBLE_PERCENTTRAIN_MODELS.contains(r.getModel())) {
                numTrainAll = r.getNumTrainingEntries();
                break;
            }
        }

        if (numTrainAll > -1) {
            for (TrainAndTestReport r : reports) {
                if ((! Model.FLEXIBLE_PERCENTTRAIN_MODELS.contains(r.getModel())) && (r.getNumTrainingEntries() != numTrainAll)) {
                    return false; //TODO maybe then say which one was wrong etc.
                }
            }

            for (TrainAndTestReport r : reports) {
                if (Model.FLEXIBLE_PERCENTTRAIN_MODELS.contains(r.getModel())) {
                    if (r.getNumTrainingEntries() != numTrainAll) {
                        r.setNumTrainingEntries(numTrainAll);
                        recomputeTrainTestSets(r);
                    }
                }
            }
        }

        return true;
    }


    private static void recomputeTrainTestSets(TrainAndTestReport r) { //TODO a nicer way? :)
        if (r instanceof TrainAndTestReportCrisp) {
            TrainAndTestReportCrisp rep = (TrainAndTestReportCrisp) r;
            double[] newFittedValues = Arrays.copyOfRange(rep.getFittedValues(), 0, rep.getNumTrainingEntries());
            double[] newForecastValsTest = Arrays.copyOfRange(rep.getFittedValues(), rep.getNumTrainingEntries(), rep.getFittedValues().length);
            rep.setFittedValues(newFittedValues);
            rep.setForecastValuesTest(newForecastValsTest);
            double[] newRealTrain = Arrays.copyOfRange(rep.getRealOutputsTrain(), 0, rep.getNumTrainingEntries());
            double[] newRealTest = Arrays.copyOfRange(rep.getRealOutputsTrain(), rep.getNumTrainingEntries(), rep.getRealOutputsTrain().length);
            rep.setRealOutputsTrain(newRealTrain);
            rep.setRealOutputsTest(newRealTest);
            rep.setErrorMeasures(ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(Utils.arrayToList(newRealTrain), Utils.arrayToList(newRealTest),
                    Utils.arrayToList(newFittedValues), Utils.arrayToList(newForecastValsTest), 0)); //TODO I hope the 0 is not a problem
        } else if (r instanceof TrainAndTestReportInterval) {
            TrainAndTestReportInterval rep = (TrainAndTestReportInterval) r;
            List<Interval> newFittedValues = new ArrayList<>(rep.getFittedValues().subList(0, rep.getNumTrainingEntries()));
            List<Interval> newForecastValsTest = new ArrayList<>(rep.getFittedValues().subList(rep.getNumTrainingEntries(), rep.getFittedValues().size()));
            rep.setFittedValues(newFittedValues);
            rep.setForecastValuesTest(newForecastValsTest);

            List<Interval> realVals = Utils.zipLowerUpperToIntervals(rep.getRealValuesLowers(), rep.getRealValuesUppers());
            List<Interval> realValsTrain = new ArrayList<>(realVals.subList(0, rep.getNumTrainingEntries()));
            List<Interval> realValsTest = new ArrayList<>(realVals.subList(rep.getNumTrainingEntries(), realVals.size()));

            //TODO somehow add the actual distance and seasonality from params
            rep.setErrorMeasures(ErrorMeasuresUtils.computeAllErrorMeasuresInterval(realValsTrain, realValsTest, newFittedValues, newForecastValsTest,
                    new WeightedEuclideanDistance(0.5), 0));
        }
    }


    public List<TrainAndTestReportInterval> computeAvgIntTSperM(List<TrainAndTestReportInterval> reportsIntTS) {
        Map<Model, List<TrainAndTestReportInterval>> mapForAvg = new HashMap<>();
        //first go through all the reports once to determine how many of each kind there are:
        for (TrainAndTestReportInterval r : reportsIntTS) {
            if (mapForAvg.containsKey(r.getModel())) {
                mapForAvg.get(r.getModel()).add(r);
                List<TrainAndTestReportInterval> l = new ArrayList<>();
                l.add(r);
                mapForAvg.put(r.getModel(), l);
            }
        }
        
        List<TrainAndTestReportInterval> avgReports = new ArrayList<>();
        for (Model model : mapForAvg.keySet()) {
            List<TrainAndTestReportInterval> l = mapForAvg.get(model);
            if (l.size() > 1) { //does not make sense to compute average over one series
                TrainAndTestReportInterval reportAvgMethod = computeAvgIntTS(l, model);
                if (reportAvgMethod != null) {
                    avgReports.add(reportAvgMethod);
                } else { //should never happen for the same method
                    System.err.println("not equal percenttrain for 1 model (avg ITS per method)");
                }
            }
        }
        
        return avgReports;
    }

    public TrainAndTestReportInterval computeAvgIntTS(List<TrainAndTestReportInterval> reportsIntTS) {
        return computeAvgIntTS(reportsIntTS, Model.ALL);
    }
    
    public TrainAndTestReportInterval computeAvgIntTS(List<TrainAndTestReportInterval> reportsIntTS, Model model) {
        if (! allTheSamePercentTrain(reportsIntTS)) { //throw an error, we cannot compute it like this
            return null;
        } else {
            MyRengine rengine = MyRengine.getRengine();
            
            if (reportsIntTS.size() == 1) { //does not make sense to compute average over one series
                return reportsIntTS.get(0);
            } else {
                StringBuilder avgAllLowersTrain = new StringBuilder("(");
                StringBuilder avgAllLowersTest = new StringBuilder("(");
                StringBuilder avgAllLowersFuture = new StringBuilder("(");
                StringBuilder avgAllUppersTrain = new StringBuilder("(");
                StringBuilder avgAllUppersTest = new StringBuilder("(");
                StringBuilder avgAllUppersFuture = new StringBuilder("(");
                StringBuilder sumWeightsTrain = new StringBuilder("(");
                StringBuilder sumWeightsTest = new StringBuilder("(");
                StringBuilder sumWeightsFuture = new StringBuilder("(");
                boolean next = false;
                for (TrainAndTestReportInterval r : reportsIntTS) {
                    if (next) {
                        avgAllLowersTrain.append(" + ");
                        avgAllLowersTest.append(" + ");
                        avgAllLowersFuture.append(" + ");
                        avgAllUppersTrain.append(" + ");
                        avgAllUppersTest.append(" + ");
                        avgAllUppersFuture.append(" + ");
                        sumWeightsTrain.append(" + ");
                        sumWeightsTest.append(" + ");
                        sumWeightsFuture.append(" + ");
                    } else {
                        next = true;
                    }
                    
                    double weightTrain = getWeightForModelTrain(r);
                    double weightTest = getWeightForModelTest(r);
                    double weightFuture = getWeightForModelFuture(r);
                    weightsInterval.put(r.toString(), weightFuture);
                    
                    sumWeightsTrain.append(weightTrain);
                    sumWeightsTest.append(weightTest);
                    
                    avgAllLowersTrain.append(weightTrain).append("*")
                            .append(Utils.arrayToRVectorString(r.getFittedValuesLowers()));
                    avgAllLowersTest.append(weightTest).append("*")
                            .append(Utils.arrayToRVectorString(r.getForecastValuesTestLowers()));

                    avgAllUppersTrain.append(weightTrain).append("*")
                            .append(Utils.arrayToRVectorString(r.getFittedValuesUppers()));
                    avgAllUppersTest.append(weightTest).append("*")
                            .append(Utils.arrayToRVectorString(r.getForecastValuesTestUppers()));
                    
                    avgAllLowersFuture.append(weightFuture).append("*");
                    avgAllUppersFuture.append(weightFuture).append("*");
                    if (r.getForecastValuesFuture().size() > 0) {
                        avgAllLowersFuture.append(Utils.arrayToRVectorString(r.getForecastValuesFutureLowers()));
                        avgAllUppersFuture.append(Utils.arrayToRVectorString(r.getForecastValuesFutureUppers()));
                        
                        sumWeightsFuture.append(weightFuture);
                    } else {
                        avgAllLowersFuture.append("0");
                        avgAllUppersFuture.append("0");
                        
                        sumWeightsFuture.append("0");
                    }
                }
                sumWeightsTrain.append(")");
                sumWeightsTest.append(")");
                sumWeightsFuture.append(")");
                
                avgAllLowersTrain.append(")/").append(sumWeightsTrain);
                avgAllLowersTest.append(")/").append(sumWeightsTest);
                avgAllLowersFuture.append(")/").append(sumWeightsFuture);
                avgAllUppersTrain.append(")/").append(sumWeightsTrain);
                avgAllUppersTest.append(")/").append(sumWeightsTest);
                avgAllUppersFuture.append(")/").append(sumWeightsFuture);

                rengine.eval("lowerTrain <- " + avgAllLowersTrain.toString());
                rengine.eval("lowerTest <- " + avgAllLowersTest.toString());
                rengine.eval("lowerFuture <- " + avgAllLowersFuture.toString());
                rengine.eval("upperTrain <- " + avgAllUppersTrain.toString());
                rengine.eval("upperTest <- " + avgAllUppersTest.toString());
                rengine.eval("upperFuture <- " + avgAllUppersFuture.toString());
                
                //add report:
                List<Double> allLowersTrainList = rengine.evalAndReturnList("lowerTrain");
                List<Double> allLowersTestList = rengine.evalAndReturnList("lowerTest");
                List<Double> allUppersTrainList = rengine.evalAndReturnList("upperTrain");
                List<Double> allUppersTestList = rengine.evalAndReturnList("upperTest");
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
                //TODO chg; for now takes WeightedEuclid, but allow any distance

                TrainAndTestReportInterval reportAvgAllITS = new TrainAndTestReportInterval(model, "_int(" + getName() + ")", true);
                reportAvgAllITS.setErrorMeasures(errorMeasures);
                reportAvgAllITS.setFittedValues(allIntervalsTrain);
                reportAvgAllITS.setForecastValuesTest(allIntervalsTest);

                List<Double> allLowersFutureList = rengine.evalAndReturnList("lowerFuture");
                List<Double> allUppersFutureList = rengine.evalAndReturnList("upperFuture");
                List<Interval> allIntervalsFuture = Utils.zipLowerUpperToIntervals(allLowersFutureList, allUppersFutureList);
                reportAvgAllITS.setForecastValuesFuture(allIntervalsFuture);
                reportAvgAllITS.setNumTrainingEntries(reportsIntTS.get(0).getNumTrainingEntries());
                realValuesTrain.addAll(realValuesTest);
                reportAvgAllITS.setRealValues(realValuesTrain);
                
                rengine.rm("lowerTrain", "lowerTest", "lowerFuture", "upperTrain", "upperTest", "upperFuture");

                return reportAvgAllITS;
            }
        }
    }
    
    public boolean isAvgCTSperM() {
        return avgCTSperM;
    }

    public void setAvgCTSperM(boolean avgCTSperM) {
        this.avgCTSperM = avgCTSperM;
    }

    public boolean isAvgCTS() {
        return avgCTS;
    }

    public void setAvgCTS(boolean avgCTS) {
        this.avgCTS = avgCTS;
    }

    public boolean isAvgIntTSperM() {
        return avgIntTSperM;
    }

    public void setAvgIntTSperM(boolean avgIntTSperM) {
        this.avgIntTSperM = avgIntTSperM;
    }

    public boolean isAvgIntTS() {
        return avgIntTS;
    }

    public void setAvgIntTS(boolean avgIntTS) {
        this.avgIntTS = avgIntTS;
    }
    
    
    public Map<String, Double> getAllWeightsCrisp() {
        return weightsCrisp;
    }
    
    
    public Map<String, Double> getAllWeightsInterval() {
        return weightsInterval;
    }
}
