package models;

import gui.tablemodels.DataTableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.params.IntervalMLPCcodeParams;
import models.params.Params;
import utils.BestModelCriterionInterval;
import utils.Const;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.IntervalExplanatoryVariable;
import utils.IntervalOutputVariable;
import utils.Utils;
import utils.imlp.Interval;
import utils.imlp.IntervalCentreRadius;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;
import utils.imlp.dist.BertoluzzaDistance;
import utils.imlp.dist.DeCarvalhoDistance;
import utils.imlp.dist.HausdorffDistance;
import utils.imlp.dist.IchinoYaguchiDistance;
import utils.imlp.dist.WeightedEuclideanDistance;

public class IntervalMLPCcode implements Forecastable {
    
    private int maxLag = 0;
    
    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        List<TrainAndTestReportInterval> reports = new ArrayList<>();
        //train some number of networks
        for (int i = 0; i < ((IntervalMLPCcodeParams)parameters).getNumNetworks(); i++) {
            reports.add((TrainAndTestReportInterval)(doTheActualForecast(dataTableModel, parameters, "" + i)));
        }
        
        //and then determine which one is the best
        int bestReportNum = 0;
        TrainAndTestReportInterval bestReport = reports.get(0);
        double bestMeasures = BestModelCriterionInterval.computeCriterion(bestReport, ((IntervalMLPCcodeParams)parameters).getCriterion());
        if (reports.size() > 1) {
            for (int i = 1; i < reports.size(); i++) {
                double currentMeasures = BestModelCriterionInterval.computeCriterion(reports.get(i), ((IntervalMLPCcodeParams)parameters).getCriterion());
                if (BestModelCriterionInterval.isCurrentBetterThanBest(((IntervalMLPCcodeParams)parameters).getCriterion(), currentMeasures, bestMeasures)) {
                    bestMeasures = currentMeasures;
                    bestReport = reports.get(i);
                    bestReportNum = i;
                }
            }
        }
        
        //copy the best file to a file without suffix
        final String CONFIG = "config" + bestReportNum;
        final String CONFIG_WITHOUT = "config";
        final String CONFIG_NET = CONFIG + ".net";
        final String CONFIG_NET_WITHOUT = CONFIG_WITHOUT + ".net";
        final String CONFIG_RES = CONFIG + ".res";
        final String CONFIG_RES_WITHOUT = CONFIG_WITHOUT + ".res";
        final String CONFIG_OUT = CONFIG + ".out";
        final String CONFIG_OUT_WITHOUT = CONFIG_WITHOUT + ".out";
        final String CONFIG_WGT = CONFIG + ".wgt";
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
            String iCONFIG = "config" + i;
            String iCONFIG_NET = iCONFIG + ".net";
            String iCONFIG_RES = iCONFIG + ".res";
            String iCONFIG_OUT = iCONFIG + ".out";
            String iCONFIG_WGT = iCONFIG + ".wgt";
            String iTRAIN_FILE = "train" + i + ".dat";
            String iTEST_FILE = "test" + i + ".dat";
            
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
    
    //to, co tu nacvicujem, je asi trochu zamotane, ale na papieri je vysvetlenie.
    private TrainAndTestReport doTheActualForecast(DataTableModel dataTableModel, Params parameters, String fileSuffix) {
        final String CONFIG = "config" + fileSuffix;
        final String CONFIG_RES = CONFIG + ".res";
        final String CONFIG_OUT = CONFIG + ".out";
        final String CONFIG_NET = CONFIG + ".net";
        final String TRAIN_FILE = "train" + fileSuffix + ".dat";
        final String TEST_FILE = "test" + fileSuffix + ".dat";
        
        
        IntervalMLPCcodeParams params = (IntervalMLPCcodeParams) parameters;
        TrainAndTestReportInterval report = new TrainAndTestReportInterval(Const.INTERVAL_MLP_C_CODE);
        report.setModelDescription(params.toString());
        
        //delete any previous files:
        File file = new File(CONFIG_RES);
        file.delete();
        file = new File(CONFIG_OUT);
        file.delete();
        
        
        
        List<Interval> realData;
        if (params.getOutVars().get(0).getIntervalNames() instanceof IntervalNamesCentreRadius) {
            List<Double> centers = dataTableModel.getDataForColname(((IntervalNamesCentreRadius)params.getOutVars().get(0)
                    .getIntervalNames()).getCentre()).subList(params.getDataRangeFrom()-1, params.getDataRangeTo());
            List<Double> radii = dataTableModel.getDataForColname(((IntervalNamesCentreRadius)params.getOutVars().get(0)
                    .getIntervalNames()).getRadius()).subList(params.getDataRangeFrom()-1, params.getDataRangeTo());
            realData = Utils.zipCentersRadiiToIntervals(centers, radii);
        } else { //LB+UB
            List<Double> lowers = dataTableModel.getDataForColname(((IntervalNamesLowerUpper)params.getOutVars().get(0)
                    .getIntervalNames()).getLowerBound()).subList(params.getDataRangeFrom()-1, params.getDataRangeTo());
            List<Double> uppers = dataTableModel.getDataForColname(((IntervalNamesLowerUpper)params.getOutVars().get(0)
                    .getIntervalNames()).getUpperBound()).subList(params.getDataRangeFrom()-1, params.getDataRangeTo());
            realData = Utils.zipLowerUpperToIntervals(lowers, uppers);
        }
        report.setRealValues(realData);
        
        
        
        
        
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
        File fileTrain = new File(TRAIN_FILE);
        //ako "testovacie" data pouzijem vsetky (100% toho, co mam), aby som dostala fit/predikcie pre vsetko
        File fileTest = new File(TEST_FILE);
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
            //TODO neskor zabranit spustaniu viacerych veci naraz (disablovat Run button, kym neskonci aktualna)
            //TODO neskor zrusit to cierne okno, ale zatial sa to chova divne, ked ho vypnem :(
            //        toto vyzeralo, ze funguje: Process p = Runtime.getRuntime().exec("cmd /c call config.bat");
            //              - kde v config.bat je: "@ECHO OFF     c config"
            Process p = Runtime.getRuntime().exec("cmd /c start /wait c " + CONFIG);
            p.waitFor();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //zatial dummy
        report.setFittedValues(new ArrayList<Interval>());
        report.setForecastValuesTest(null);
        
        
        ErrorMeasuresInterval errorMeasures = new ErrorMeasuresInterval(params.getDistance());
        
        //TODO potom zmenit!
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
        
        //TODO add lag! resp. add lag do PlotDrawera, aby s tym vedel robit. nieco ako maxLag, tj zarezane spolocne
        return report;
    }
    
    private List<List<Double>> prepareData(DataTableModel dataTableModel, List<IntervalExplanatoryVariable> explVars, 
                                                                          List<IntervalOutputVariable> outVars,
                                                                          int from, int to) {
        List<List<Double>> data = new ArrayList<>();
        
        for (IntervalExplanatoryVariable var : explVars) {
            List<Double> centers;
            List<Double> radii;
            
            if (var.getIntervalNames() instanceof IntervalNamesCentreRadius) {
                centers = dataTableModel.getDataForColname(((IntervalNamesCentreRadius)var.getIntervalNames()).getCentre()).subList(from, to);
                radii = dataTableModel.getDataForColname(((IntervalNamesCentreRadius)var.getIntervalNames()).getRadius()).subList(from, to);
            } else {
                //we have LB and UB
                List<Double> lowers = dataTableModel.getDataForColname(((IntervalNamesLowerUpper)var.getIntervalNames()).getLowerBound()).subList(from, to);
                List<Double> uppers = dataTableModel.getDataForColname(((IntervalNamesLowerUpper)var.getIntervalNames()).getUpperBound()).subList(from, to);
                centers = new ArrayList<>();
                radii = new ArrayList<>();
                for (int i = 0; i < lowers.size(); i++) {
                    centers.add((uppers.get(i) + lowers.get(i))/2);
                    radii.add((uppers.get(i) - lowers.get(i))/2);
                }
                
            }
            
            data.add(IntervalMLPCcode.lagBy(var.getLag(), centers));
            data.add(IntervalMLPCcode.lagBy(var.getLag(), radii));
            
            maxLag = Math.max(maxLag, var.getLag());
        }
        
        for (IntervalOutputVariable var : outVars) {
            List<Double> centers;
            List<Double> radii;
            
            if (var.getIntervalNames() instanceof IntervalNamesCentreRadius) {
                centers = dataTableModel.getDataForColname(((IntervalNamesCentreRadius)var.getIntervalNames()).getCentre()).subList(from, to);
                radii = dataTableModel.getDataForColname(((IntervalNamesCentreRadius)var.getIntervalNames()).getRadius()).subList(from, to);
            } else {
                //we have LB and UB
                List<Double> lowers = dataTableModel.getDataForColname(((IntervalNamesLowerUpper)var.getIntervalNames()).getLowerBound()).subList(from, to);
                List<Double> uppers = dataTableModel.getDataForColname(((IntervalNamesLowerUpper)var.getIntervalNames()).getUpperBound()).subList(from, to);
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
        
        return IntervalMLPCcode.trimDataToRectangle(data, maxLag);
    }

    public static List<List<Double>> trimDataToRectangle(List<List<Double>> data, int maxLag) {
        List<List<Double>> trimmedData = new ArrayList<>();
        
        for (List<Double> column : data) {
            trimmedData.add(column.subList(maxLag, column.size()));
        }
        
        return trimmedData;
    }

    public static List<Double> lagBy(int lag, List<Double> data) {
        //output v case t chcem predikovat na zaklade inputu v case t-lag.
        // takze inputy musim posunut "dopredu" (zahodim niekolko poslednych)
        //je to spravne, nemazat!
        List<Double> lagged = new ArrayList<>();
        
        for (int i = 0; i < lag; i++) {
            lagged.add(Double.NaN);
        }
        lagged.addAll(data.subList(0, data.size() - lag));
        
        return lagged;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
