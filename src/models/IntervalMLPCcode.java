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
import utils.imlp.WeightedEuclideanDistance;

public class IntervalMLPCcode implements ForecastableIntervals {
    
    //to, co tu nacvicujem, je asi trochu zamotane, ale na papieri je vysvetlenie.

    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        TrainAndTestReportInterval report = new TrainAndTestReportInterval("iMLP (C code)");
        IntervalMLPCcodeParams params = (IntervalMLPCcodeParams) parameters;
        
        //delete any previous files:
        File file = new File("config.res");
        file.delete();
        file = new File("config.out");
        file.delete();
        
        List<List<Double>> data = prepareData(dataTableModel, params.getExplVars(), params.getOutVars());
        
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
        report.setFittedValues(new ArrayList<Interval>());
        
        //TODO plot
        report.setFittedValuesPlotCode("plot.ts(seq(1,170))");  //a new dummy plot, yaay
        
        
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
            report.setForecastValues(forecastsTest);

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
        }
        
        report.setErrorMeasures(errorMeasures);
        
        return report;
    }

    private List<List<Double>> prepareData(DataTableModel dataTableModel, List<ExplanatoryVariable> explVars, 
                                                                          List<OutputVariable> outVars) {
        List<List<Double>> data = new ArrayList<>();
        
        int maxLag = 0;
        for (ExplanatoryVariable var : explVars) {
            List<Double> centers;
            List<Double> radii;
            
            if (var.isCenterRadius()) {
                centers = dataTableModel.getDataForColname(var.getFirst());
                radii = dataTableModel.getDataForColname(var.getSecond());
            } else {
                //we have LB and UB
                List<Double> lowers = dataTableModel.getDataForColname(var.getFirst());
                List<Double> uppers = dataTableModel.getDataForColname(var.getSecond());
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
                centers = dataTableModel.getDataForColname(var.getFirst());
                radii = dataTableModel.getDataForColname(var.getSecond());
            } else {
                //we have LB and UB
                List<Double> lowers = dataTableModel.getDataForColname(var.getFirst());
                List<Double> uppers = dataTableModel.getDataForColname(var.getSecond());
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
