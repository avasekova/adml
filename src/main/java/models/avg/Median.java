package models.avg;

import gui.ColourService;
import models.Model;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import utils.*;
import utils.imlp.Interval;
import utils.imlp.dist.WeightedEuclideanDistance;

import java.util.List;
import java.util.function.Function;

public class Median extends Average { //well...

    public Median(boolean avgCTSperM, boolean avgCTS, boolean avgIntTSperM, boolean avgIntTS) {
        super(avgCTSperM, avgCTS, avgIntTSperM, avgIntTS);
    }

    @Override
    public double getWeightForModelTrain(TrainAndTestReportCrisp report) {
        return 1;
    }

    @Override
    public double getWeightForModelTest(TrainAndTestReportCrisp report) {
        return 1;
    }

    @Override
    public double getWeightForModelFuture(TrainAndTestReportCrisp report) {
        return 1;
    }

    @Override
    public double getWeightForModelTrain(TrainAndTestReportInterval report) {
        return 1;
    }

    @Override
    public double getWeightForModelTest(TrainAndTestReportInterval report) {
        return 1;
    }

    @Override
    public double getWeightForModelFuture(TrainAndTestReportInterval report) {
        return 1;
    }

    @Override
    public String getName() {
        return "[median]";
    }
    
    @Override
    public TrainAndTestReportCrisp computeAvgCTS(List<TrainAndTestReportCrisp> reportsCTS, Model model) {
        if (reportsCTS.size() == 1) { //does not make sense to compute average over one series
            return reportsCTS.get(0);
        } else {
            //first check if all of them have the same percentage of train data
            boolean allTheSame = true;
            int numTrainAll = reportsCTS.get(0).getNumTrainingEntries();
            
            for (TrainAndTestReportCrisp r : reportsCTS) {
                //iba vyuzijem tento loop na nasyslenie weights, aj tak su vsetky 1
                weightsCrisp.put(r.toString(), 1.0);
                
                if (r.getNumTrainingEntries() != numTrainAll) {
                    allTheSame = false;
                    break;
                }
            }

            if (! allTheSame) {
                return null;
            } else {

                String fittedValsAvgAll = getMedians(reportsCTS, reportsCTS.get(0).getFittedValues().length, TrainAndTestReportCrisp::getFittedValues);
                String forecastValsTestAvgAll = getMedians(reportsCTS, reportsCTS.get(0).getForecastValuesTest().length, TrainAndTestReportCrisp::getForecastValuesTest);
                String forecastValsFutureAvgAll = getMedians(reportsCTS, reportsCTS.get(0).getForecastValuesFuture().length, TrainAndTestReportCrisp::getForecastValuesFuture);
                
                String avgAll = "c(" + fittedValsAvgAll + "," + forecastValsTestAvgAll + "," + forecastValsFutureAvgAll + ")";
                
                MyRengine rengine = MyRengine.getRengine();
                //a vyrobit pre tento average novy report a pridat ho do reportsCTS:
                TrainAndTestReportCrisp thisAvgReport = new TrainAndTestReportCrisp(model, "(" + getName() + ")", true);
                double[] fittedValsAvg = rengine.evalAndReturnArray(fittedValsAvgAll.toString());
                double[] forecastValsTestAvg = rengine.evalAndReturnArray(forecastValsTestAvgAll.toString());

                ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(
                        Utils.arrayToList(reportsCTS.get(0).getRealOutputsTrain()), 
                        Utils.arrayToList(reportsCTS.get(0).getRealOutputsTest()),
                        Utils.arrayToList(fittedValsAvg), Utils.arrayToList(forecastValsTestAvg), 0);

                thisAvgReport.setErrorMeasures(errorMeasures);
                thisAvgReport.setForecastValuesFuture(rengine.evalAndReturnArray(forecastValsFutureAvgAll.toString()));
                thisAvgReport.setColourInPlot(ColourService.getService().getNewColour());
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
    
    
    @Override
    public TrainAndTestReportInterval computeAvgIntTS(List<TrainAndTestReportInterval> reportsIntTS, Model model) { //TODO fix - cele to spadne
        //first check if all of them have the same percentage of train data
        boolean allTheSame = true;
        int numTrainAll = reportsIntTS.get(0).getNumTrainingEntries();
        for (TrainAndTestReportInterval r : reportsIntTS) {
            //iba vyuzijem tento loop na nasyslenie weights, aj tak su vsetky 1
            weightsInterval.put(r.toString(), 1.0);
            
            if (r.getNumTrainingEntries() != numTrainAll) {
                allTheSame = false;
                break;
            }
        }

        if (! allTheSame) { //throw an error, we cannot compute it like this
            return null;
        } else {
            MyRengine rengine = MyRengine.getRengine();
            
            if (reportsIntTS.size() == 1) { //does not make sense to compute average over one series
                return reportsIntTS.get(0);
            } else {

                String avgAllCentersTrain = getMedians(reportsIntTS, reportsIntTS.get(0).getFittedValues().size(), TrainAndTestReportInterval::getFittedValuesCenters);
                String avgAllRadiiTrain = getMedians(reportsIntTS, reportsIntTS.get(0).getFittedValues().size(), TrainAndTestReportInterval::getFittedValuesRadii);

                String avgAllCentersTest = getMedians(reportsIntTS, reportsIntTS.get(0).getForecastValuesTest().size(), TrainAndTestReportInterval::getForecastValuesTestCenters);
                String avgAllRadiiTest = getMedians(reportsIntTS, reportsIntTS.get(0).getForecastValuesTest().size(), TrainAndTestReportInterval::getForecastValuesTestRadii);

                List<Double> allCentersTrainList = rengine.evalAndReturnList(avgAllCentersTrain);
                List<Double> allRadiiTrainList = rengine.evalAndReturnList(avgAllRadiiTrain);
                List<Interval> allIntervalsTrain = Utils.zipCentersRadiiToIntervals(allCentersTrainList, allRadiiTrainList);

                List<Double> allCentersTestList = rengine.evalAndReturnList(avgAllCentersTest);
                List<Double> allRadiiTestList = rengine.evalAndReturnList(avgAllRadiiTest);
                List<Interval> allIntervalsTest = Utils.zipCentersRadiiToIntervals(allCentersTestList, allRadiiTestList);

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

                TrainAndTestReportInterval reportAvgAllITS = new TrainAndTestReportInterval(model, "_int(" + getName() + ")", true);
                reportAvgAllITS.setErrorMeasures(errorMeasures);
                reportAvgAllITS.setColourInPlot(ColourService.getService().getNewColour());
                reportAvgAllITS.setFittedValues(allIntervalsTrain);
                reportAvgAllITS.setForecastValuesTest(allIntervalsTest);

                String avgAllCentersFuture = getMedians(reportsIntTS, reportsIntTS.get(0).getForecastValuesFuture().size(), TrainAndTestReportInterval::getForecastValuesFutureCenters);
                String avgAllRadiiFuture = getMedians(reportsIntTS, reportsIntTS.get(0).getForecastValuesFuture().size(), TrainAndTestReportInterval::getForecastValuesFutureRadii);

                List<Double> allCentersFutureList = rengine.evalAndReturnList(avgAllCentersFuture);
                List<Double> allRadiiFutureList = rengine.evalAndReturnList(avgAllRadiiFuture);
                List<Interval> allIntervalsFuture = Utils.zipCentersRadiiToIntervals(allCentersFutureList, allRadiiFutureList);

                reportAvgAllITS.setForecastValuesFuture(allIntervalsFuture);
                reportAvgAllITS.setNumTrainingEntries(reportsIntTS.get(0).getNumTrainingEntries());
                realValuesTrain.addAll(realValuesTest);
                reportAvgAllITS.setRealValues(realValuesTrain);

                return reportAvgAllITS;
            }
        }
    }

    public <T extends TrainAndTestReport> String getMedians(List<T> reportsIntTS, int size, Function<T, double[]> getter) { //size = reportsIntTS.get(0).getFittedValues().size()
        StringBuilder avgAll = new StringBuilder("c(");

        boolean nextOuter = false;
        for (int i = 0; i < size; i++) {
            if (nextOuter) {
                avgAll.append(",");
            } else {
                nextOuter = true;
            }

            avgAll.append("median(c(");
            //idem po jednotlivych prvkoch fitted
            boolean nextInner = false;
            for (T r : reportsIntTS) {
                if (!Double.isNaN(getter.apply(r)[i])) { //if (!Double.isNaN(r.getFittedValuesCenters()[i])) {
                    //a pre kazdy report si tam dam ciselko do medianu
                    if (nextInner) {
                        avgAll.append(",");
                    } else {
                        nextInner = true;
                    }
                    avgAll.append(getter.apply(r)[i]); //avgAll.append(r.getFittedValuesCenters()[i]);
                }
            }
            avgAll.append("))");
        }
        avgAll.append(")");

        return avgAll.toString();
    }

}
