package models;

import models.params.IntervalMLPCcodeParams;
import models.params.Params;
import utils.*;
import utils.imlp.Interval;
import utils.imlp.IntervalCentreRadius;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;
import utils.imlp.dist.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IntervalMLPCcode implements Forecastable {
    private static final long serialVersionUID = 1L;
    
    private int maxLag = 0;
    
    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        String UID = parameters.getUID(); //TODO find a better way so you don't forget to update all filenames -.-

        List<TrainAndTestReportInterval> reports = new ArrayList<>();
        //train some number of networks
        for (int i = 0; i < ((IntervalMLPCcodeParams)parameters).getNumNetworks(); i++) {
            reports.add((TrainAndTestReportInterval)(doTheActualForecast(dataTableModel, parameters, UID + "_" + i)));
        }
        
        //and then determine which one is the best
        int bestReportNum = 0;
        TrainAndTestReportInterval bestReport = reports.get(0);
        double bestMeasures = BestModelCriterionInterval.computeCriterion(bestReport, parameters.getCriterion());
        if (reports.size() > 1) {
            for (int i = 1; i < reports.size(); i++) {
                double currentMeasures = BestModelCriterionInterval.computeCriterion(reports.get(i), parameters.getCriterion());
                if (BestModelCriterionInterval.isCurrentBetterThanBest(parameters.getCriterion(), currentMeasures, bestMeasures)) {
                    bestMeasures = currentMeasures;
                    bestReport = reports.get(i);
                    bestReportNum = i;
                }
            }
        }
        
        //copy the best file to a file without suffix
        final String CONFIG = "config_" + UID + "_" + bestReportNum;
        final String CONFIG_NET = CONFIG + ".net";
        final String CONFIG_RES = CONFIG + ".res";
        final String CONFIG_OUT = CONFIG + ".out";
        final String CONFIG_WGT = CONFIG + ".wgt";
        final String CONFIG_WITHOUT = "config-final_" + UID;
        final String CONFIG_NET_WITHOUT = CONFIG_WITHOUT + ".net";
        final String CONFIG_RES_WITHOUT = CONFIG_WITHOUT + ".res";
        final String CONFIG_OUT_WITHOUT = CONFIG_WITHOUT + ".out";
        final String CONFIG_WGT_WITHOUT = CONFIG_WITHOUT + ".wgt";
        
        try {
            Files.copy(new File(CONFIG_NET).toPath(), new File(CONFIG_NET_WITHOUT).toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new File(CONFIG_RES).toPath(), new File(CONFIG_RES_WITHOUT).toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new File(CONFIG_OUT).toPath(), new File(CONFIG_OUT_WITHOUT).toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new File(CONFIG_WGT).toPath(), new File(CONFIG_WGT_WITHOUT).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //and delete all those arbitrary files with suffixes
        for (int i = 0; i < ((IntervalMLPCcodeParams)parameters).getNumNetworks(); i++) {
            String iCONFIG = "config_" + UID + "_" + i;
            String iCONFIG_NET = iCONFIG + ".net";
            String iCONFIG_RES = iCONFIG + ".res";
            String iCONFIG_OUT = iCONFIG + ".out";
            String iCONFIG_WGT = iCONFIG + ".wgt";
            String iTRAIN_FILE = "train_" + UID + "_" + i + ".dat";
            String iTEST_FILE = "test_" + UID + "_" + i + ".dat";
            
            try {
                Files.delete(new File(iCONFIG_NET).toPath());
                Files.delete(new File(iCONFIG_RES).toPath());
                Files.delete(new File(iCONFIG_OUT).toPath());
                Files.delete(new File(iCONFIG_WGT).toPath());
                Files.delete(new File(iTRAIN_FILE).toPath());
                Files.delete(new File(iTEST_FILE).toPath());
            } catch (IOException ex) {
                Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        
        //now it would be possible to extract weights if we wanted to... the "the only" remaining file is safe as long
        //  as we do not run this again, e.g. as a part of a bigger batch containing multiple iMLP settings.
        
        return bestReport;
    }
    
    //this might seem a little confusing. it's explained in my paper notes.
    private TrainAndTestReport doTheActualForecast(Map<String, List<Double>> dataTableModel, Params parameters, String fileSuffix) {
        final String CONFIG = "config_" + fileSuffix;
        final String CONFIG_RES = CONFIG + ".res";
        final String CONFIG_OUT = CONFIG + ".out";
        final String CONFIG_NET = CONFIG + ".net";
        final String TRAIN_FILE = "train_" + fileSuffix + ".dat";
        final String TEST_FILE = "test_" + fileSuffix + ".dat";
        
        
        IntervalMLPCcodeParams params = (IntervalMLPCcodeParams) parameters;
        TrainAndTestReportInterval report = new TrainAndTestReportInterval(Model.INTERVAL_MLP_C_CODE);
        report.setModelDescription(params.toString());
        
        //delete any previous files:
        File file = new File(CONFIG_RES);
        file.delete();
        file = new File(CONFIG_OUT);
        file.delete();
        
        
        
        List<Interval> realData;
        if (params.getOutVars().get(0).getIntervalNames() instanceof IntervalNamesCentreRadius) {
            List<Double> centers = dataTableModel.get(((IntervalNamesCentreRadius)params.getOutVars().get(0)
                    .getIntervalNames()).getCentre()).subList(params.getDataRangeFrom()-1, params.getDataRangeTo());
            List<Double> radii = dataTableModel.get(((IntervalNamesCentreRadius)params.getOutVars().get(0)
                    .getIntervalNames()).getRadius()).subList(params.getDataRangeFrom()-1, params.getDataRangeTo());
            realData = Utils.zipCentersRadiiToIntervals(centers, radii);
        } else { //LB+UB
            List<Double> lowers = dataTableModel.get(((IntervalNamesLowerUpper)params.getOutVars().get(0)
                    .getIntervalNames()).getLowerBound()).subList(params.getDataRangeFrom()-1, params.getDataRangeTo());
            List<Double> uppers = dataTableModel.get(((IntervalNamesLowerUpper)params.getOutVars().get(0)
                    .getIntervalNames()).getUpperBound()).subList(params.getDataRangeFrom()-1, params.getDataRangeTo());
            realData = Utils.zipLowerUpperToIntervals(lowers, uppers);
        }
        report.setRealValues(realData);
        
        
        
        
        
        List<List<Double>> data = prepareData(dataTableModel, params.getExplVars(), 
                params.getOutVars(), params.getDataRangeFrom()-1, params.getDataRangeTo());
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*(params.getDataRangeTo()-(params.getDataRangeFrom()-1)));
        report.setNumTrainingEntries(numTrainingEntries);
        
//        List<Double> trainingPortionOfCenter = centerData.subList(0, numTrainingEntries);
//        List<Double> testingPortionOfCenter = centerData.subList(numTrainingEntries, centerData.size());
//        List<Double> trainingPortionOfRadius = radiusData.subList(0, numTrainingEntries);
//        List<Double> testingPortionOfRadius = radiusData.subList(numTrainingEntries, radiusData.size());
        
        //create the train and test input files:
        //training data (i.e. just the part selected in the settings):
        File fileTrain = new File(TRAIN_FILE);
        //use all data for "testing" (100%), so we get fit/forecast for all of them
        File fileTest = new File(TEST_FILE);
        try (BufferedWriter fwTrain = new BufferedWriter(new FileWriter(fileTrain));
             BufferedWriter fwTest = new BufferedWriter(new FileWriter(fileTest))) {
            for (int i = 0; i < numTrainingEntries - maxLag; i++) {
                for (List<Double> column : data) {
                    fwTrain.write(column.get(i) + "\t");
                    fwTest.write(column.get(i) + "\t");
                }
                fwTrain.newLine();
                fwTest.newLine();
            }
            fwTrain.flush();
            
            //and add the rest to Test
            for (int i = numTrainingEntries - maxLag; i < data.get(0).size(); i++) {
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
        file = new File(CONFIG_NET);
        try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
            fw.write("mp(" + params.getExplVars().size() + "," + params.getNumNodesHidden() + "," + params.getOutVars().size() + ")");
            fw.newLine();
            if (params.getDistance() instanceof BertoluzzaDistance) {
                fw.write("bertoluzza(" + ((BertoluzzaDistance)(params.getDistance())).getBeta() + ")");
            } else if (params.getDistance() instanceof WeightedEuclideanDistance) {
                fw.write("euclid(" + ((WeightedEuclideanDistance)(params.getDistance())).getBeta() + ")");
            } else if (params.getDistance() instanceof HausdorffDistance) {
                fw.write("hausdorff");
            } else if (params.getDistance() instanceof DeCarvalhoDistance) {
                fw.write("decarvalho(" + ((DeCarvalhoDistance)(params.getDistance())).getGamma() + ")");
            } else if (params.getDistance() instanceof IchinoYaguchiDistance) {
                fw.write("ichino(" + ((IchinoYaguchiDistance)(params.getDistance())).getGamma() + ")");
            }
            fw.newLine();
            fw.write("learn");
            fw.newLine();
            fw.write("nco(" + params.getNumIterations() + ")");
            fw.newLine();
            fw.write("wd(0.00001)");
            fw.newLine();
            fw.write("ftrain(" + TRAIN_FILE + ")");
            fw.newLine();
            fw.write("ftest(" + TEST_FILE + ")");
            fw.flush();
        } catch (IOException ex) {
            Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            //TODO later disable the Run button until the current batch finishes
            Process p = new ProcessBuilder("c", CONFIG).start();
            p.waitFor();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //dummy for now
        report.setFittedValues(new ArrayList<>());
        report.setForecastValuesTest(null);
        
        
        ErrorMeasuresInterval errorMeasures = new ErrorMeasuresInterval(params.getDistance());
        
        //TODO chg
        if (params.getOutVars().size() == 1) {
            List<Interval> forecasts = Utils.getForecastsFromOutFile(new File(CONFIG_OUT));
            List<Interval> forecastsTrain = forecasts.subList(0, numTrainingEntries);
            List<Interval> forecastsTest = forecasts.subList(numTrainingEntries, forecasts.size());
            
            List<Double> trainingPortionOfCenter = data.get(data.size() - 2).subList(0, numTrainingEntries);
            List<Double> testingPortionOfCenter = data.get(data.size() - 2).subList(numTrainingEntries, data.get(data.size() - 2).size());
            List<Double> trainingPortionOfRadius = data.get(data.size() - 1).subList(0, numTrainingEntries);
            List<Double> testingPortionOfRadius = data.get(data.size() - 1).subList(numTrainingEntries, data.get(data.size() - 1).size());
            
            List<Interval> trainingIntervals = Utils.zipCentersRadiiToIntervals(trainingPortionOfCenter, trainingPortionOfRadius);
            List<Interval> testingIntervals = Utils.zipCentersRadiiToIntervals(testingPortionOfCenter, testingPortionOfRadius);
            
            
            
            
            List<Interval> newForecastsTrain = new ArrayList<>();
            for (int i = 0; i < maxLag; i++) {
                newForecastsTrain.add(new IntervalCentreRadius(Double.NaN, Double.NaN));
            }
            newForecastsTrain.addAll(forecastsTrain.subList(0, forecastsTrain.size()-maxLag));
            
            List<Interval> newForecastsTest = new ArrayList<>();
            newForecastsTest.addAll(forecastsTrain.subList(forecastsTrain.size()-maxLag, forecastsTrain.size()));
            newForecastsTest.addAll(forecastsTest.subList(0, forecastsTest.size()));
            
            
            
            report.setFittedValues(newForecastsTrain);
            report.setForecastValuesTest(newForecastsTest);
            
            
            
//            report.setForecastValuesFuture(); //nothing yet
            
            errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresInterval(realData.subList(0, numTrainingEntries),
                    realData.subList(numTrainingEntries, realData.size()),
                    newForecastsTrain, newForecastsTest, params.getDistance(), parameters.getSeasonality());
        }
        
        report.setErrorMeasures(errorMeasures);
        
        //real data: the last two columns in data are Center and Radius of real data.
//        report.setRealValues(data.get(data.size() - 2), data.get(data.size() - 1));
        
        //TODO add lag! or add lag to PlotDrawer, so it could work with it, sth like maxLag, i.e. cut all at the same point
        return report;
    }
    
    private List<List<Double>> prepareData(Map<String, List<Double>> dataMap, List<IntervalVariable> explVars,
                                                                          List<IntervalVariable> outVars,
                                                                          int from, int to) {
        List<List<Double>> data = new ArrayList<>();
        
        data.addAll(doThisWhatever(dataMap, explVars, from, to));
        data.addAll(doThisWhatever(dataMap, outVars, from, to));

        return IntervalMLPCcode.trimDataToRectangle(data, maxLag);
    }

    private List<List<Double>> doThisWhatever(Map<String, List<Double>> dataMap, List<IntervalVariable> vars, int from, int to) {
        List<List<Double>> data = new ArrayList<>();
        for (IntervalVariable var : vars) {
            List<Double> centers;
            List<Double> radii;

            if (var.getIntervalNames() instanceof IntervalNamesCentreRadius) {
                centers = dataMap.get(((IntervalNamesCentreRadius)var.getIntervalNames()).getCentre()).subList(from, to);
                radii = dataMap.get(((IntervalNamesCentreRadius)var.getIntervalNames()).getRadius()).subList(from, to);
            } else {
                //we have LB and UB
                List<Double> lowers = dataMap.get(((IntervalNamesLowerUpper)var.getIntervalNames()).getLowerBound()).subList(from, to);
                List<Double> uppers = dataMap.get(((IntervalNamesLowerUpper)var.getIntervalNames()).getUpperBound()).subList(from, to);
                centers = new ArrayList<>();
                radii = new ArrayList<>();
                for (int i = 0; i < lowers.size(); i++) {
                    centers.add((uppers.get(i) + lowers.get(i))/2);
                    radii.add((uppers.get(i) - lowers.get(i))/2);
                }
            }

            data.add(IntervalMLPCcode.lagBy(var.getLag(), centers)); //do not lag outVars -> lag=0, OK
            data.add(IntervalMLPCcode.lagBy(var.getLag(), radii));   //do not lag outVars -> lag=0, OK

            maxLag = Math.max(maxLag, var.getLag());
        }

        return data;
    }

    public static List<List<Double>> trimDataToRectangle(List<List<Double>> data, int maxLag) {
        List<List<Double>> trimmedData = new ArrayList<>();
        
        for (List<Double> column : data) {
            trimmedData.add(column.subList(maxLag, column.size()));
        }
        
        return trimmedData;
    }

    public static List<Double> lagBy(int lag, List<Double> data) {
        //I want to predict the output in time t based on the inputu in t-lag.
        // so I need to shift the inputs forward (throw away several of the last ones)
        //note for self: correct, do not fix!
        List<Double> lagged = new ArrayList<>();
        
        for (int i = 0; i < lag; i++) {
            lagged.add(Double.NaN);
        }
        lagged.addAll(data.subList(0, data.size() - lag));
        
        return lagged;
    }
}
