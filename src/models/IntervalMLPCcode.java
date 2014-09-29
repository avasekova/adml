package models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import params.IntervalMLPCcodeParams;
import params.Params;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.Utils;
import utils.imlp.Interval;
import utils.imlp.WeightedEuclideanDistance;

public class IntervalMLPCcode implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        throw new UnsupportedOperationException("Not supported for interval data.");
    }

    @Override
    public TrainAndTestReport forecastIntervalCenterRadius(List<Double> centerData, List<Double> radiusData, Params parameters) {
        TrainAndTestReportInterval report = new TrainAndTestReportInterval("iMLP (C code)");
        IntervalMLPCcodeParams params = (IntervalMLPCcodeParams) parameters;
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*centerData.size());
        report.setNumTrainingEntries(numTrainingEntries);
        
        List<Double> trainingPortionOfCenter = centerData.subList(0, numTrainingEntries);
        List<Double> testingPortionOfCenter = centerData.subList(numTrainingEntries, centerData.size());
        List<Double> trainingPortionOfRadius = radiusData.subList(0, numTrainingEntries);
        List<Double> testingPortionOfRadius = radiusData.subList(numTrainingEntries, radiusData.size());
        
        //delete any previous files:
        File file = new File("config.res");
        file.delete();
        file = new File("config.out");
        file.delete();
        
        //for now, ugly like this:
        switch (params.getExplVarString()) {
            case "Month and Year":
                //create the train and test input files:
                file = new File("train.dat");
                try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
                    for (int i = 0; i < trainingPortionOfCenter.size(); i++) {
                        fw.write(((i%12)+1) + "\t" + "0" + "\t" + (i/12) + "\t" + "0" + "\t"
                                + trainingPortionOfCenter.get(i) + "\t" + trainingPortionOfRadius.get(i));
                        fw.newLine();
                    }
                    fw.flush();
                } catch (IOException ex) {
                    Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
                }

                //ako "testovacie" data pouzijem vsetky (100% toho, co mam), aby som dostala fit/predikcie pre vsetko
                file = new File("test.dat");
                try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
                    for (int i = 0; i < centerData.size(); i++) {
                        fw.write(((i%12)+1) + "\t" + "0" + "\t" + (i/12) + "\t" + "0" + "\t"
                                + centerData.get(i) + "\t" + radiusData.get(i));
                        fw.newLine();
                    }
                    fw.flush();
                } catch (IOException ex) {
                    Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
                }

                //create the config file:
                file = new File("config.net");
                try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
                    fw.write("mp(2," + params.getNumNodesHidden() + ",1)");
                    fw.newLine();
                    fw.write("learn");
                    fw.newLine();
                    fw.write("nco(" + params.getNumIterations() + ")");
                    fw.newLine();
                    fw.write("wd(0.001)");
                    fw.newLine();
                    fw.write("ftrain(train.dat)");
                    fw.newLine();
                    fw.write("ftest(test.dat)");
                    fw.flush();
                } catch (IOException ex) {
                    Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                break;
            case "<selectedVariable>(t-1)":
                //TODO get the variable which is selected (getOptionalParams?)
                
                // then move it to lag 1 (basicall start the writing loop somewhere else; I hope
                
                //then do this: create the train and test files, create the config file.
                // don't forget to change the structure: mlp(1..) instead of mlp(2..)
                
                
                //create the train and test input files:
                file = new File("train.dat");
                try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
                    for (int i = 0; i < trainingPortionOfCenter.size(); i++) {
                        fw.write(((i%12)+1) + "\t" + "0" + "\t" + (i/12) + "\t" + "0" + "\t"
                                + trainingPortionOfCenter.get(i) + "\t" + trainingPortionOfRadius.get(i));
                        fw.newLine();
                    }
                    fw.flush();
                } catch (IOException ex) {
                    Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
                }

                //ako "testovacie" data pouzijem vsetky (100% toho, co mam), aby som dostala fit/predikcie pre vsetko
                file = new File("test.dat");
                try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
                    for (int i = 0; i < centerData.size(); i++) {
                        fw.write(((i%12)+1) + "\t" + "0" + "\t" + (i/12) + "\t" + "0" + "\t"
                                + centerData.get(i) + "\t" + radiusData.get(i));
                        fw.newLine();
                    }
                    fw.flush();
                } catch (IOException ex) {
                    Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
                }

                //create the config file:
                file = new File("config.net");
                try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
                    fw.write("mp(2," + params.getNumNodesHidden() + ",1)");
                    fw.newLine();
                    fw.write("learn");
                    fw.newLine();
                    fw.write("nco(" + params.getNumIterations() + ")");
                    fw.newLine();
                    fw.write("wd(0.001)");
                    fw.newLine();
                    fw.write("ftrain(train.dat)");
                    fw.newLine();
                    fw.write("ftest(test.dat)");
                    fw.flush();
                } catch (IOException ex) {
                    Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                break;
        }
        
        try {
            //TODO neskor zabranit spustaniu viacerych veci naraz (disablovat Run button, kym neskonci aktualna)
            //TODO neskor zrusit to cierne okno, ale zatial sa to chova divne, ked ho vypnem :(
            //        toto vyzeralo, ze funguje: Process p = Runtime.getRuntime().exec("cmd /c call config.bat");
            //              - kde v config.bat je: "@ECHO OFF     c config"
            Process p = Runtime.getRuntime().exec("cmd /c start /wait c config");
            p.waitFor();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        List<Interval> forecasts = Utils.getForecastsFromOutFile(new File("config.out"));
        List<Interval> forecastsTrain = forecasts.subList(0, numTrainingEntries);
        List<Interval> forecastsTest = forecasts.subList(numTrainingEntries, forecasts.size());
        
        List<Interval> trainingIntervals = Utils.zipCentersRadiiToIntervals(trainingPortionOfCenter, trainingPortionOfRadius);
        List<Double> errorsTrain = Utils.getErrorsForIntervals(trainingIntervals, forecastsTrain, new WeightedEuclideanDistance(0.5));
        List<Interval> testingIntervals = Utils.zipCentersRadiiToIntervals(testingPortionOfCenter, testingPortionOfRadius);
        List<Double> errorsTest = Utils.getErrorsForIntervals(testingIntervals, forecastsTest, new WeightedEuclideanDistance(0.5));
        
        report.setFittedValues(forecastsTrain);
        report.setForecastValues(forecastsTest);
        
        //TODO plot
        report.setFittedValuesPlotCode("plot.ts(seq(" + centerData.size()/2 + ", " + centerData.size()/2 + ", length=" + centerData.size() + "))");  //a new dummy plot, yaay
        
        
        ErrorMeasuresInterval errorMeasures = new ErrorMeasuresInterval();
        errorMeasures.setMEtrain(ErrorMeasuresUtils.ME(errorsTrain));
        errorMeasures.setMEtest(ErrorMeasuresUtils.ME(errorsTest));
        errorMeasures.setRMSEtrain(ErrorMeasuresUtils.RMSE(errorsTrain));
        errorMeasures.setRMSEtest(ErrorMeasuresUtils.RMSE(errorsTest));
        errorMeasures.setMAEtrain(ErrorMeasuresUtils.MAE(errorsTrain));
        errorMeasures.setMAEtest(ErrorMeasuresUtils.MAE(errorsTest));
        errorMeasures.setMeanCoverageTrain(ErrorMeasuresUtils.meanCoverage(trainingIntervals, forecastsTrain));
        errorMeasures.setMeanCoverageTest(ErrorMeasuresUtils.meanCoverage(testingIntervals, forecastsTest));
        errorMeasures.setMeanEfficiencyTrain(ErrorMeasuresUtils.meanEfficiency(trainingIntervals, forecastsTrain));
        errorMeasures.setMeanEfficiencyTest(ErrorMeasuresUtils.meanEfficiency(testingIntervals, forecastsTest));
        report.setErrorMeasures(errorMeasures);
        
        return report;
    }

    @Override
    public TrainAndTestReport forecastIntervalLowerUpper(List<Double> lowerData, List<Double> upperData, Params parameters) {
        List<Double> centerData = new ArrayList<>();
        List<Double> radiusData = new ArrayList<>();
        
        //transform LB and UB to center and radius:
        for (int i = 0; i < lowerData.size(); i++) {
            double lower = lowerData.get(i);
            double upper = upperData.get(i);
            centerData.add((upper + lower)/2);
            radiusData.add((upper - lower)/2);
        }
        
        return forecastIntervalCenterRadius(centerData, radiusData, parameters);
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
    
}
