package models.avg;

import gui.ColourService;
import java.util.ArrayList;
import java.util.List;
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
    public TrainAndTestReportCrisp computeAvgCTS(List<TrainAndTestReportCrisp> reportsCTS, String name) {
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
                StringBuilder fittedValsAvgAll = new StringBuilder("c(");
                StringBuilder forecastValsTestAvgAll = new StringBuilder("c(");
                StringBuilder forecastValsFutureAvgAll = new StringBuilder("c(");
                
                //fitted
                boolean nextOuter = false;
                for (int i = 0; i < reportsCTS.get(0).getFittedValues().length; i++) {
                    if (nextOuter) {
                        fittedValsAvgAll.append(",");
                    } else {
                        nextOuter = true;
                    }
                    
                    fittedValsAvgAll.append("median(c(");
                    //idem po jednotlivych prvkoch fitted
                    boolean nextInner = false;
                    for (TrainAndTestReportCrisp r : reportsCTS) {
                        if (! Double.isNaN(r.getFittedValues()[i])) {
                            //a pre kazdy report si tam dam ciselko do medianu
                            if (nextInner) {
                                fittedValsAvgAll.append(",");
                            } else {
                                nextInner = true;
                            }
                            fittedValsAvgAll.append(r.getFittedValues()[i]);
                        }
                    }
                    fittedValsAvgAll.append("))");
                }
                
                
                //forecast test---------------------------------
                //sort podla dlzky:
                //(bubblesort ftw)
                List<TrainAndTestReportCrisp> reportsCTSforForecastTest = new ArrayList<>();
                reportsCTSforForecastTest.addAll(reportsCTS);
                for (int i = 0; i < reportsCTSforForecastTest.size(); i++) {
                    for (int j = i + 1; j < reportsCTSforForecastTest.size(); j++) {
                        if (reportsCTSforForecastTest.get(i).getForecastValuesTest().length > reportsCTSforForecastTest.get(j).getForecastValuesTest().length) {
                            double[] pom = reportsCTSforForecastTest.get(i).getForecastValuesTest();
                            reportsCTSforForecastTest.get(i).setForecastValuesTest(reportsCTSforForecastTest.get(j).getForecastValuesTest());
                            reportsCTSforForecastTest.get(j).setForecastValuesTest(pom);
                        }
                    }
                }
                
                int countAll = 0;
                nextOuter = false;
                while (! reportsCTSforForecastTest.isEmpty()) {
                    for (int i = countAll; i < reportsCTSforForecastTest.get(0).getForecastValuesTest().length; i++) {
                        if (nextOuter) {
                            forecastValsTestAvgAll.append(",");
                        } else {
                            nextOuter = true;
                        }

                        forecastValsTestAvgAll.append("median(c(");
                        //idem po jednotlivych prvkoch fitted
                        boolean nextInner = false;
                        for (TrainAndTestReportCrisp r : reportsCTSforForecastTest) {
                            if (! Double.isNaN(r.getForecastValuesTest()[i])) {
                                //a pre kazdy report si tam dam ciselko do medianu
                                if (nextInner) {
                                    forecastValsTestAvgAll.append(",");
                                } else {
                                    nextInner = true;
                                }
                                forecastValsTestAvgAll.append(r.getForecastValuesTest()[i]);
                            }
                        }
                        forecastValsTestAvgAll.append("))");
                    }
                    countAll = reportsCTSforForecastTest.get(0).getForecastValuesTest().length;
                    reportsCTSforForecastTest.remove(0);
                }
                
                //forecast future---------------------------------
                //sort podla dlzky:
                //(bubblesort ftw)
                List<TrainAndTestReportCrisp> reportsCTSforForecastFuture = new ArrayList<>();
                reportsCTSforForecastFuture.addAll(reportsCTS);
                for (int i = 0; i < reportsCTSforForecastFuture.size(); i++) {
                    for (int j = i + 1; j < reportsCTSforForecastFuture.size(); j++) {
                        if (reportsCTSforForecastFuture.get(i).getForecastValuesFuture().length > reportsCTSforForecastFuture.get(j).getForecastValuesFuture().length) {
                            double[] pom = reportsCTSforForecastFuture.get(i).getForecastValuesFuture();
                            reportsCTSforForecastFuture.get(i).setForecastValuesFuture(reportsCTSforForecastFuture.get(j).getForecastValuesFuture());
                            reportsCTSforForecastFuture.get(j).setForecastValuesFuture(pom);
                        }
                    }
                }
                
                countAll = 0;
                nextOuter = false;
                while (! reportsCTSforForecastFuture.isEmpty()) {
                    for (int i = countAll; i < reportsCTSforForecastFuture.get(0).getForecastValuesFuture().length; i++) {
                        if (nextOuter) {
                            forecastValsFutureAvgAll.append(",");
                        } else {
                            nextOuter = true;
                        }

                        forecastValsFutureAvgAll.append("median(c(");
                        //idem po jednotlivych prvkoch fitted
                        boolean nextInner = false;
                        for (TrainAndTestReportCrisp r : reportsCTSforForecastFuture) {
                            if (! Double.isNaN(r.getForecastValuesFuture()[i])) {
                                //a pre kazdy report si tam dam ciselko do medianu
                                if (nextInner) {
                                    forecastValsFutureAvgAll.append(",");
                                } else {
                                    nextInner = true;
                                }
                                forecastValsFutureAvgAll.append(r.getForecastValuesFuture()[i]);
                            }
                        }
                        forecastValsFutureAvgAll.append("))");
                    }
                    countAll = reportsCTSforForecastFuture.get(0).getForecastValuesFuture().length;
                    reportsCTSforForecastFuture.remove(0);
                }
                
                fittedValsAvgAll.append(")");
                forecastValsTestAvgAll.append(")");
                forecastValsFutureAvgAll.append(")");
                
                String avgAll = "c(" + fittedValsAvgAll + "," + forecastValsTestAvgAll + "," + forecastValsFutureAvgAll + ")";
                
                Rengine rengine = MyRengine.getRengine();
                //a vyrobit pre tento average novy report a pridat ho do reportsCTS:
                TrainAndTestReportCrisp thisAvgReport = new TrainAndTestReportCrisp(name + "(" + getName() + ")", true);
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
    public TrainAndTestReportInterval computeAvgIntTS(List<TrainAndTestReportInterval> reportsIntTS, String name) {
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
            Rengine rengine = MyRengine.getRengine();
            
            if (reportsIntTS.size() == 1) { //does not make sense to compute average over one series
                return reportsIntTS.get(0);
            } else {
                StringBuilder avgAllCentersTrain = new StringBuilder("c(");
                StringBuilder avgAllCentersTest = new StringBuilder("c(");
                StringBuilder avgAllCentersFuture = new StringBuilder("c(");
                StringBuilder avgAllRadiiTrain = new StringBuilder("c(");
                StringBuilder avgAllRadiiTest = new StringBuilder("c(");
                StringBuilder avgAllRadiiFuture = new StringBuilder("c(");
                
                boolean nextOuter = false;
                for (int i = 0; i < reportsIntTS.get(0).getFittedValues().size(); i++) {
                    if (nextOuter) {
                        avgAllCentersTrain.append(",");
                        avgAllRadiiTrain.append(",");
                    } else {
                        nextOuter = true;
                    }
                    
                    avgAllCentersTrain.append("median(c(");
                    avgAllCentersTest.append("median(c(");
                    //idem po jednotlivych prvkoch fitted
                    boolean nextInnerCenter = false;
                    boolean nextInnerRadius = false;
                    for (TrainAndTestReportInterval r : reportsIntTS) {
                        if (! Double.isNaN(r.getFittedValuesCenters()[i])) {
                            //a pre kazdy report si tam dam ciselko do medianu
                            if (nextInnerCenter) {
                                avgAllCentersTrain.append(",");
                            } else {
                                nextInnerCenter = true;
                            }
                            avgAllCentersTrain.append(r.getFittedValuesCenters()[i]);
                        }
                        
                        if (! Double.isNaN(r.getFittedValuesRadii()[i])) {
                            if (nextInnerRadius) {
                                avgAllRadiiTrain.append(",");
                            } else {
                                nextInnerRadius = true;
                            }
                            avgAllRadiiTrain.append(r.getFittedValuesRadii()[i]);
                        }
                    }
                    avgAllCentersTrain.append("))");
                    avgAllRadiiTrain.append("))");
                }
                
                
                nextOuter = false;
                for (int i = 0; i < reportsIntTS.get(0).getForecastValuesTest().size(); i++) {
                    if (nextOuter) {
                        avgAllCentersTest.append(",");
                        avgAllRadiiTest.append(",");
                    } else {
                        nextOuter = true;
                    }
                    
                    avgAllCentersTest.append("median(c(");
                    avgAllRadiiTest.append("median(c(");
                    //idem po jednotlivych prvkoch fitted
                    boolean nextInnerCenter = false;
                    boolean nextInnerRadius = false;
                    for (TrainAndTestReportInterval r : reportsIntTS) {
                        if (! Double.isNaN(r.getForecastValuesTestCenters()[i])) {
                            //a pre kazdy report si tam dam ciselko do medianu
                            if (nextInnerCenter) {
                                avgAllCentersTrain.append(",");
                            } else {
                                nextInnerCenter = true;
                            }
                            avgAllCentersTrain.append(r.getForecastValuesTestCenters()[i]);
                        }
                        
                        if (! Double.isNaN(r.getForecastValuesTestRadii()[i])) {
                            if (nextInnerRadius) {
                                avgAllRadiiTrain.append(",");
                            } else {
                                nextInnerRadius = true;
                            }
                            avgAllRadiiTrain.append(r.getForecastValuesTestRadii()[i]);
                        }
                    }
                    avgAllCentersTest.append("))");
                    avgAllRadiiTest.append("))");
                }
                
                nextOuter = false;
                for (int i = 0; i < reportsIntTS.get(0).getForecastValuesFuture().size(); i++) {
                    if (nextOuter) {
                        avgAllCentersFuture.append(",");
                        avgAllRadiiFuture.append(",");
                    } else {
                        nextOuter = true;
                    }
                    
                    avgAllCentersFuture.append("median(c(");
                    avgAllRadiiFuture.append("median(c(");
                    //idem po jednotlivych prvkoch fitted
                    boolean nextInnerCenter = false;
                    boolean nextInnerRadius = false;
                    for (TrainAndTestReportInterval r : reportsIntTS) {
                        if (! Double.isNaN(r.getForecastValuesFutureCenters()[i])) {
                            //a pre kazdy report si tam dam ciselko do medianu
                            if (nextInnerCenter) {
                                avgAllCentersTrain.append(",");
                            } else {
                                nextInnerCenter = true;
                            }
                            avgAllCentersTrain.append(r.getForecastValuesFutureCenters()[i]);
                        }
                        
                        if (! Double.isNaN(r.getForecastValuesFutureRadii()[i])) {
                            if (nextInnerRadius) {
                                avgAllRadiiTrain.append(",");
                            } else {
                                nextInnerRadius = true;
                            }
                            avgAllRadiiTrain.append(r.getForecastValuesFutureRadii()[i]);
                        }
                    }
                    avgAllCentersFuture.append("))");
                    avgAllRadiiFuture.append("))");
                }
                
                avgAllCentersTrain.append(")");
                avgAllCentersTest.append(")");
                avgAllCentersFuture.append(")");
                avgAllRadiiTrain.append(")");
                avgAllRadiiTest.append(")");
                avgAllRadiiFuture.append(")");

                rengine.eval("centerTrain <- " + avgAllCentersTrain.toString());
                rengine.eval("centerTest <- " + avgAllCentersTest.toString());
                rengine.eval("centerFuture <- " + avgAllCentersFuture.toString());
                rengine.eval("radiusTrain <- " + avgAllRadiiTrain.toString());
                rengine.eval("radiusTest <- " + avgAllRadiiTest.toString());
                rengine.eval("radiusFuture <- " + avgAllRadiiFuture.toString());
                
                //add report:
                REXP getAllCentersTrain = rengine.eval("centerTrain");
                double[] allCentersTrain = getAllCentersTrain.asDoubleArray();
                List<Double> allCentersTrainList = Utils.arrayToList(allCentersTrain);
                REXP getAllCentersTest = rengine.eval("centerTest");
                double[] allCentersTest = getAllCentersTest.asDoubleArray();
                List<Double> allCentersTestList = Utils.arrayToList(allCentersTest);
                REXP getAllRadiiTrain = rengine.eval("radiusTrain");
                double[] allRadiiTrain = getAllRadiiTrain.asDoubleArray();
                List<Double> allRadiiTrainList = Utils.arrayToList(allRadiiTrain);
                REXP getAllRadiiTest = rengine.eval("radiusTest");
                double[] allRadiiTest = getAllRadiiTest.asDoubleArray();
                List<Double> allRadiiTestList = Utils.arrayToList(allRadiiTest);
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

                TrainAndTestReportInterval reportAvgAllITS = new TrainAndTestReportInterval(name + "_int(" + getName() + ")", true);
                reportAvgAllITS.setErrorMeasures(errorMeasures);
                reportAvgAllITS.setColourInPlot(ColourService.getService().getNewColour());
                reportAvgAllITS.setFittedValues(allIntervalsTrain);
                reportAvgAllITS.setForecastValuesTest(allIntervalsTest);
                reportAvgAllITS.setForecastValuesFuture(realValuesTest);

                REXP getAllCentersFuture = rengine.eval("centerFuture");
                List<Double> allCentersFutureList = Utils.arrayToList(getAllCentersFuture.asDoubleArray());
                REXP getAllRadiiFuture = rengine.eval("radiusFuture");
                List<Double> allRadiiFutureList = Utils.arrayToList(getAllRadiiFuture.asDoubleArray());
                List<Interval> allIntervalsFuture = Utils.zipCentersRadiiToIntervals(allCentersFutureList, allRadiiFutureList);
                reportAvgAllITS.setForecastValuesFuture(allIntervalsFuture);
                reportAvgAllITS.setNumTrainingEntries(reportsIntTS.get(0).getNumTrainingEntries());
                realValuesTrain.addAll(realValuesTest);
                reportAvgAllITS.setRealValues(realValuesTrain);

                return reportAvgAllITS;
            }
        }
    }
}
