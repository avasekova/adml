package models;

import gui.DataTableModel;
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
import utils.imlp.ExplanatoryVariable;
import utils.imlp.Interval;
import utils.imlp.OutputVariable;
import utils.imlp.dist.DeCarvalhoDistance;
import utils.imlp.dist.HausdorffDistance;
import utils.imlp.dist.IchinoYaguchiDistance;
import utils.imlp.dist.WeightedEuclideanDistance;

public class IntervalMLPCcode implements ForecastableIntervals {
    
    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        List<TrainAndTestReportInterval> reports = new ArrayList<>();
        //train some number of networks
        //TODO later also keep their .out and other files! prevent overwriting the best!
        for (int i = 0; i < ((IntervalMLPCcodeParams)parameters).getNumNetworks(); i++) {
            reports.add((TrainAndTestReportInterval)(doTheActualForecast(dataTableModel, parameters)));
        }
        
        //and then determine which one is the best
        //TODO for now coverage+efficiency, later allow to customize
        TrainAndTestReportInterval bestReport = reports.get(0);
        double bestMeasures = computeCriterion(bestReport);
        if (reports.size() > 1) {
            for (int i = 1; i < reports.size(); i++) {
                double currentMeasures = computeCriterion(reports.get(i));
                if (currentMeasures > bestMeasures) {
                    bestMeasures = currentMeasures;
                    bestReport = reports.get(i);
                }
            }
        }
        
        return bestReport;
    }
    
    private double computeCriterion(TrainAndTestReportInterval report) {
        ErrorMeasuresInterval m = (ErrorMeasuresInterval)(report.getErrorMeasures());
        //sum of coverages and efficiencies
        return m.getMeanCoverageTest() + m.getMeanCoverageTrain() + m.getMeanEfficiencyTest() + m.getMeanEfficiencyTrain();
    }

    //to, co tu nacvicujem, je asi trochu zamotane, ale na papieri je vysvetlenie.
    private TrainAndTestReport doTheActualForecast(DataTableModel dataTableModel, Params parameters) {
        TrainAndTestReportInterval report = new TrainAndTestReportInterval("iMLP (C code)");
        IntervalMLPCcodeParams params = (IntervalMLPCcodeParams) parameters;
        
        //delete any previous files:
        File file = new File("config.res");
        file.delete();
        file = new File("config.out");
        file.delete();
        
        List<List<Double>> data = prepareData(dataTableModel, params.getExplVars(), 
                params.getOutVars(), params.getDataRangeFrom()-1, params.getDataRangeTo());
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*data.get(0).size());
        report.setNumTrainingEntries(numTrainingEntries);
        
//        List<Double> trainingPortionOfCenter = centerData.subList(0, numTrainingEntries);
//        List<Double> testingPortionOfCenter = centerData.subList(numTrainingEntries, centerData.size());
//        List<Double> trainingPortionOfRadius = radiusData.subList(0, numTrainingEntries);
//        List<Double> testingPortionOfRadius = radiusData.subList(numTrainingEntries, radiusData.size());
        
        //create the train and test input files:
        //training data (i.e. just the part selected in the settings):
        File fileTrain = new File("train.dat");
        //ako "testovacie" data pouzijem vsetky (100% toho, co mam), aby som dostala fit/predikcie pre vsetko
        File fileTest = new File("test.dat");
        try (BufferedWriter fwTrain = new BufferedWriter(new FileWriter(fileTrain));
             BufferedWriter fwTest = new BufferedWriter(new FileWriter(fileTest))) {
            for (int i = 0; i < numTrainingEntries; i++) {
                for (List<Double> column : data) {
                    fwTrain.write(column.get(i) + "\t");
                    fwTest.write(column.get(i) + "\t");
                }
                fwTrain.newLine();
                fwTest.newLine();
            }
            fwTrain.flush();
            
            //a dopisat zvysok do Testu
            for (int i = numTrainingEntries; i < data.get(0).size(); i++) {
                for (List<Double> column : data) {
                    fwTest.write(column.get(i) + "\t");
                }
                fwTest.newLine();
            }
            fwTest.flush();
        } catch (IOException ex) {
            Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //create the config file:
        file = new File("config.net");
        try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
            fw.write("mp(" + params.getExplVars().size() + "," + params.getNumNodesHidden() + "," + params.getOutVars().size() + ")");
            fw.newLine();
            if (params.getDistanceFunction() instanceof WeightedEuclideanDistance) {
                fw.write("euclid(" + ((WeightedEuclideanDistance)(params.getDistanceFunction())).getBeta() + ")");
            } else if (params.getDistanceFunction() instanceof HausdorffDistance) {
                fw.write("hausdorff");
            } else if (params.getDistanceFunction() instanceof DeCarvalhoDistance) {
                fw.write("decarvalho(" + ((DeCarvalhoDistance)(params.getDistanceFunction())).getGamma() + ")");
            } else if (params.getDistanceFunction() instanceof IchinoYaguchiDistance) {
                fw.write("ichino(" + ((IchinoYaguchiDistance)(params.getDistanceFunction())).getGamma() + ")");
            }
            fw.newLine();
            fw.write("learn");
            fw.newLine();
            fw.write("nco(" + params.getNumIterations() + ")");
            fw.newLine();
            fw.write("wd(0.00001)");
            fw.newLine();
            fw.write("ftrain(train.dat)");
            fw.newLine();
            fw.write("ftest(test.dat)");
            fw.flush();
        } catch (IOException ex) {
            Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
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
        
        
        //zatial dummy
        report.setFittedValues(new ArrayList<Interval>());
        report.setForecastValuesTest(null);
        
        
        ErrorMeasuresInterval errorMeasures = new ErrorMeasuresInterval();
        
        //TODO potom zmenit!
        if (params.getOutVars().size() == 1) {
            List<Interval> forecasts = Utils.getForecastsFromOutFile(new File("config.out"));
            List<Interval> forecastsTrain = forecasts.subList(0, numTrainingEntries);
            List<Interval> forecastsTest = forecasts.subList(numTrainingEntries, forecasts.size());
            
            List<Double> trainingPortionOfCenter = data.get(data.size() - 2).subList(0, numTrainingEntries);
            List<Double> testingPortionOfCenter = data.get(data.size() - 2).subList(numTrainingEntries, data.get(data.size() - 2).size());
            List<Double> trainingPortionOfRadius = data.get(data.size() - 1).subList(0, numTrainingEntries);
            List<Double> testingPortionOfRadius = data.get(data.size() - 1).subList(numTrainingEntries, data.get(data.size() - 1).size());
            
            List<Interval> trainingIntervals = Utils.zipCentersRadiiToIntervals(trainingPortionOfCenter, trainingPortionOfRadius);
            List<Double> errorsTrain = Utils.getErrorsForIntervals(trainingIntervals, forecastsTrain, new WeightedEuclideanDistance(0.5));
            List<Interval> testingIntervals = Utils.zipCentersRadiiToIntervals(testingPortionOfCenter, testingPortionOfRadius);
            List<Double> errorsTest = Utils.getErrorsForIntervals(testingIntervals, forecastsTest, new WeightedEuclideanDistance(0.5));

            report.setFittedValues(forecastsTrain);
            report.setForecastValuesTest(forecastsTest);

            errorMeasures.setMEtrain(ErrorMeasuresUtils.ME(errorsTrain));
            errorMeasures.setMEtest(ErrorMeasuresUtils.ME(errorsTest));
            errorMeasures.setRMSEtrain(ErrorMeasuresUtils.RMSE(errorsTrain));
            errorMeasures.setRMSEtest(ErrorMeasuresUtils.RMSE(errorsTest));
            errorMeasures.setMAEtrain(ErrorMeasuresUtils.MAE(errorsTrain));
            errorMeasures.setMAEtest(ErrorMeasuresUtils.MAE(errorsTest));
            errorMeasures.setMSEtrain(ErrorMeasuresUtils.MSE(errorsTrain));
            errorMeasures.setMSEtest(ErrorMeasuresUtils.MSE(errorsTest));
            errorMeasures.setMeanCoverageTrain(ErrorMeasuresUtils.meanCoverage(trainingIntervals, forecastsTrain));
            errorMeasures.setMeanCoverageTest(ErrorMeasuresUtils.meanCoverage(testingIntervals, forecastsTest));
            errorMeasures.setMeanEfficiencyTrain(ErrorMeasuresUtils.meanEfficiency(trainingIntervals, forecastsTrain));
            errorMeasures.setMeanEfficiencyTest(ErrorMeasuresUtils.meanEfficiency(testingIntervals, forecastsTest));
        }
        
        report.setErrorMeasures(errorMeasures);
        
        //real data: the last two columns in data are Center and Radius of real data.
        report.setRealValues(data.get(data.size() - 2), data.get(data.size() - 1));
        
        return report;
    }
    
    private List<List<Double>> prepareData(DataTableModel dataTableModel, List<ExplanatoryVariable> explVars, 
                                                                          List<OutputVariable> outVars,
                                                                          int from, int to) {
        List<List<Double>> data = new ArrayList<>();
        
        int maxLag = 0;
        for (ExplanatoryVariable var : explVars) {
            List<Double> centers;
            List<Double> radii;
            
            if (var.isCenterRadius()) {
                centers = dataTableModel.getDataForColname(var.getFirst()).subList(from, to);
                radii = dataTableModel.getDataForColname(var.getSecond()).subList(from, to);
            } else {
                //we have LB and UB
                List<Double> lowers = dataTableModel.getDataForColname(var.getFirst()).subList(from, to);
                List<Double> uppers = dataTableModel.getDataForColname(var.getSecond()).subList(from, to);
                centers = new ArrayList<>();
                radii = new ArrayList<>();
                for (int i = 0; i < lowers.size(); i++) {
                    centers.add((uppers.get(i) + lowers.get(i))/2);
                    radii.add((uppers.get(i) - lowers.get(i))/2);
                }
                
            }
            
            data.add(lagBy(var.getLag(), centers));
            data.add(lagBy(var.getLag(), radii));
            
            maxLag = Math.max(maxLag, var.getLag());
        }
        
        for (OutputVariable var : outVars) {
            List<Double> centers;
            List<Double> radii;
            
            if (var.isCenterRadius()) {
                centers = dataTableModel.getDataForColname(var.getFirst()).subList(from, to);
                radii = dataTableModel.getDataForColname(var.getSecond()).subList(from, to);
            } else {
                //we have LB and UB
                List<Double> lowers = dataTableModel.getDataForColname(var.getFirst()).subList(from, to);
                List<Double> uppers = dataTableModel.getDataForColname(var.getSecond()).subList(from, to);
                centers = new ArrayList<>();
                radii = new ArrayList<>();
                for (int i = 0; i < lowers.size(); i++) {
                    centers.add((uppers.get(i) + lowers.get(i))/2);
                    radii.add((uppers.get(i) - lowers.get(i))/2);
                }
                
            }
            
            data.add(centers);
            data.add(radii);
        }
        
        return trimDataToRectangle(data, maxLag);
    }

    private List<List<Double>> trimDataToRectangle(List<List<Double>> data, int maxLag) {
        List<List<Double>> trimmedData = new ArrayList<>();
        
        for (List<Double> column : data) {
            trimmedData.add(column.subList(maxLag, column.size()));
        }
        
        return trimmedData;
    }

    private List<Double> lagBy(int lag, List<Double> data) {
        //output v case t chcem predikovat na zaklade inputu v case t-lag.
        // takze inputy musim posunut "dopredu" (zahodim niekolko poslednych)
        //je to spravne, nemazat!
        List<Double> lagged = new ArrayList<>();
        
        for (int i = 0; i < lag; i++) {
            lagged.add(null);
        }
        lagged.addAll(data.subList(0, data.size() - lag));
        
        return lagged;
    }
}
