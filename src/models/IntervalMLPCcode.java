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
import utils.ErrorMeasures;
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
        TrainAndTestReport report = new TrainAndTestReport("iMLP (C code)");
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
        
        System.out.println("data prepared, run it");
        try {
            //TODO neskor zabranit spustaniu viacerych veci naraz (disablovat Run button, kym neskonci aktualna)
            //TODO neskor zrusit to cierne okno, ale zatial sa to chova divne, ked ho vypnem :(
            //        toto vyzeralo, ze funguje: Process p = Runtime.getRuntime().exec("cmd /c call config.bat");
            //              - kde v config.bat je: "@ECHO OFF     c config"
            Process p = Runtime.getRuntime().exec("cmd /c start /wait c config");
            p.waitFor();
            System.out.println("should be done now.");
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        List<Interval> forecasts = Utils.getForecastsFromOutFile(new File("config.out"));
        List<Interval> forecastsTrain = forecasts.subList(0, numTrainingEntries);
        List<Interval> forecastsTest = forecasts.subList(numTrainingEntries, forecasts.size());
        
        List<Double> errorsTrain = Utils.getErrorsForIntervals(trainingPortionOfCenter, trainingPortionOfRadius, 
                                                               forecastsTrain, new WeightedEuclideanDistance(0.5));
        List<Double> errorsTest = Utils.getErrorsForIntervals(testingPortionOfCenter, testingPortionOfRadius,
                                                              forecastsTest, new WeightedEuclideanDistance(0.5));
        //dummy vals for now! (in most cases) TODO opravit
        report.setForecastValues(Utils.listToArray(radiusData)); //TODO change for real values!
        report.setFittedValues(Utils.listToArray(testingPortionOfRadius)); //TODO change for real values!
        report.setFittedValuesPlotCode("plot.ts(sin(seq(0,2*pi,length=1000)))"); 
        List<Double> errorMeasures = new ArrayList<>();
        errorMeasures.add(ErrorMeasures.ME(errorsTrain)); //ME train
        errorMeasures.add(ErrorMeasures.ME(errorsTest)); //ME test
        errorMeasures.add(ErrorMeasures.RMSE(errorsTrain)); //RMSE train
        errorMeasures.add(ErrorMeasures.RMSE(errorsTest)); //RMSE test
        errorMeasures.add(ErrorMeasures.MAE(errorsTrain)); //MAE train
        errorMeasures.add(ErrorMeasures.MAE(errorsTest)); //MAE test
        report.setErrorMeasures(errorMeasures);
        
        
        System.out.println("leaving the training method");
        return report;
    }

    @Override
    public TrainAndTestReport forecastIntervalLowerUpper(List<Double> lowerData, List<Double> upperData, Params parameters) {
        List<Double> centerData = new ArrayList<>();
        List<Double> radiusData = new ArrayList<>();
        
        System.out.println("here before");
        //transform LB and UB to center and radius:
        for (int i = 0; i < lowerData.size(); i++) {
            double lower = lowerData.get(i);
            double upper = upperData.get(i);
            centerData.add((upper + lower)/2);
            radiusData.add((upper - lower)/2);
        }
        
        System.out.println("delegating");
        return forecastIntervalCenterRadius(centerData, radiusData, parameters);
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
    
}
