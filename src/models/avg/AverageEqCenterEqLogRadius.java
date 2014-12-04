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
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;
import utils.imlp.Interval;
import utils.imlp.dist.WeightedEuclideanDistance;

public class AverageEqCenterEqLogRadius extends Average {
    
    public AverageEqCenterEqLogRadius(boolean avgIntTSperM, boolean avgIntTS) {
        super(false, false, avgIntTSperM, avgIntTS);
    }

    @Override
    public double getWeightForModelTrain(TrainAndTestReportCrisp report) {
        return 0;
    }

    @Override
    public double getWeightForModelTest(TrainAndTestReportCrisp report) {
        return 0;
    }

    @Override
    public double getWeightForModelFuture(TrainAndTestReportCrisp report) {
        return 0;
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
    public TrainAndTestReportInterval computeAvgIntTS(List<TrainAndTestReportInterval> reportsIntTS, String name) {
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

                TrainAndTestReportInterval reportAvgAllITS = new TrainAndTestReportInterval(name + "(avg int)", false);
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
