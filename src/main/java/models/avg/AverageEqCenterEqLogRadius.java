package models.avg;

import gui.ColourService;
import models.Model;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;
import utils.imlp.Interval;
import utils.imlp.dist.WeightedEuclideanDistance;

import java.util.List;

public class AverageEqCenterEqLogRadius extends Average {
    
    public AverageEqCenterEqLogRadius(boolean avgIntTSperM, boolean avgIntTS) {
        super(false, false, avgIntTSperM, avgIntTS);
    }
    
    @Override
    public String getName() {
        return "avg[C+logR]";
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
        return 0;
    }

    @Override
    public double getWeightForModelTest(TrainAndTestReportInterval report) {
        return 0;
    }

    @Override
    public double getWeightForModelFuture(TrainAndTestReportInterval report) {
        return 0;
    }
    
    public double getWeightForCenterTrain() {
        return 1;
    }
    
    public double getWeightForCenterTest() {
        return 1;
    }
    
    public double getWeightForCenterFuture() {
        return 1;
    }
    
    public double getWeightForRadiusTrain() {
        return 1;
    }
    
    public double getWeightForRadiusTest() {
        return 1;
    }
    
    public double getWeightForRadiusFuture() {
        return 1;
    }
    
    
    @Override
    public TrainAndTestReportInterval computeAvgIntTS(List<TrainAndTestReportInterval> reportsIntTS, Model model) {
        //first check if all of them have the same percentage of train data
        if (! allTheSamePercentTrain(reportsIntTS)) { //throw an error, we cannot compute it like this
            return null;
        } else {
            MyRengine rengine = MyRengine.getRengine();
            
            if (reportsIntTS.size() == 1) { //does not make sense to compute average over one series
                return reportsIntTS.get(0);
            } else {
                StringBuilder avgAllCentersTrain = new StringBuilder("(");
                StringBuilder avgAllCentersTest = new StringBuilder("(");
                StringBuilder avgAllCentersFuture = new StringBuilder("(");
                StringBuilder avgAllRadiiTrain = new StringBuilder("(");
                StringBuilder avgAllRadiiTest = new StringBuilder("(");
                StringBuilder avgAllRadiiFuture = new StringBuilder("(");
                StringBuilder sumWeightsTrain = new StringBuilder("(");
                StringBuilder sumWeightsTest = new StringBuilder("(");
                StringBuilder sumWeightsFuture = new StringBuilder("(");
                boolean next = false;
                for (TrainAndTestReportInterval r : reportsIntTS) {
                    if (next) {
                        avgAllCentersTrain .append(" + ");
                        avgAllCentersTest  .append(" + ");
                        avgAllCentersFuture.append(" + ");
                        
                        avgAllRadiiTrain   .append(" * ");
                        avgAllRadiiTest    .append(" * ");
                        avgAllRadiiFuture  .append(" * ");
                        
                        sumWeightsTrain.append(" + ");
                        sumWeightsTest.append(" + ");
                        sumWeightsFuture.append(" + ");
                    } else {
                        next = true;
                    }
                    
                    sumWeightsTrain.append("1"); //TODO vseobecne
                    sumWeightsTest.append("1");  //TODO vseobecne
                    
                    avgAllCentersTrain.append(getWeightForCenterTrain()).append("*")
                            .append(Utils.arrayToRVectorString(r.getFittedValuesCenters()));
                    avgAllCentersTest.append(getWeightForCenterTest()).append("*")
                            .append(Utils.arrayToRVectorString(r.getForecastValuesTestCenters()));

                    avgAllRadiiTrain.append(getWeightForRadiusTrain()).append("*")
                            .append(Utils.arrayToRVectorString(r.getFittedValuesRadii()));
                    avgAllRadiiTest.append(getWeightForRadiusTest()).append("*")
                            .append(Utils.arrayToRVectorString(r.getForecastValuesTestRadii()));
                    
                    avgAllCentersFuture.append(getWeightForCenterFuture()).append("*");
                    avgAllRadiiFuture.append(getWeightForRadiusFuture()).append("*");
                    if (r.getForecastValuesFuture().size() > 0) {
                        avgAllCentersFuture.append(Utils.arrayToRVectorString(r.getForecastValuesFutureCenters()));
                        avgAllRadiiFuture.append(Utils.arrayToRVectorString(r.getForecastValuesFutureRadii()));
                        
                        sumWeightsFuture.append("1"); //TODO vseobecne
                    } else {
                        avgAllCentersFuture.append("0");
                        avgAllRadiiFuture  .append("1");
                        
                        sumWeightsFuture.append("0"); //TODO vseobecne
                    }
                }
                
                sumWeightsTrain.append(")");
                sumWeightsTest.append(")");
                sumWeightsFuture.append(")");
                
                avgAllCentersTrain.append(")/").append(sumWeightsTrain);
                avgAllCentersTest.append(")/").append(sumWeightsTest);
                avgAllCentersFuture.append(")/").append(sumWeightsFuture);
                avgAllRadiiTrain.append(")^(1/(").append(sumWeightsTrain).append("))");
                avgAllRadiiTest.append(")^(1/(").append(sumWeightsTest).append("))");
                avgAllRadiiFuture.append(")^(1/(").append(sumWeightsFuture).append("))");

                rengine.eval("centerTrain <- " + avgAllCentersTrain.toString());
                rengine.eval("centerTest <- " + avgAllCentersTest.toString());
                rengine.eval("centerFuture <- " + avgAllCentersFuture.toString());
                rengine.eval("radiusTrain <- " + avgAllRadiiTrain.toString());
                rengine.eval("radiusTest <- " + avgAllRadiiTest.toString());
                rengine.eval("radiusFuture <- " + avgAllRadiiFuture.toString());
                
                //add report:
                List<Double> allCentersTrainList = rengine.evalAndReturnList("centerTrain");
                List<Double> allCentersTestList = rengine.evalAndReturnList("centerTest");
                List<Double> allRadiiTrainList = rengine.evalAndReturnList("radiusTrain");
                List<Double> allRadiiTestList = rengine.evalAndReturnList("radiusTest");
                List<Interval> allIntervalsTrain = Utils.zipCentersRadiiToIntervals(allCentersTrainList, allRadiiTrainList);
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
                reportAvgAllITS.setForecastValuesFuture(realValuesTest);

                List<Double> allCentersFutureList = rengine.evalAndReturnList("centerFuture");
                List<Double> allRadiiFutureList = rengine.evalAndReturnList("radiusFuture");
                List<Interval> allIntervalsFuture = Utils.zipCentersRadiiToIntervals(allCentersFutureList, allRadiiFutureList);
                reportAvgAllITS.setForecastValuesFuture(allIntervalsFuture);
                reportAvgAllITS.setNumTrainingEntries(reportsIntTS.get(0).getNumTrainingEntries());
                realValuesTrain.addAll(realValuesTest);
                reportAvgAllITS.setRealValues(realValuesTrain);
                
                rengine.rm("centerTrain", "centerTest", "centerFuture", "radiusTrain", "radiusTest", "radiusFuture");

                return reportAvgAllITS;
            }
        }
    }
}
