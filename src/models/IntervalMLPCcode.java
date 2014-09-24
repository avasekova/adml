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
        
        //create the train and test input files:
        File file = new File("train.dat");
        try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < trainingPortionOfCenter.size(); i++) {
                fw.write(((i%12)+1) + "\t" + (i/12) + "\t"
                        + trainingPortionOfCenter.get(i) + "\t" + trainingPortionOfRadius.get(i));
                fw.newLine();
            }
            fw.flush();
        } catch (IOException ex) {
            Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        file = new File("test.dat");
        try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < testingPortionOfCenter.size(); i++) {
                //pozor na hodnoty i! chcem, aby cas nadvazoval na train, takze takto:
                int monthYear = i + trainingPortionOfCenter.size();
                fw.write(((monthYear%12)+1) + "\t" + (monthYear/12) + "\t"
                        + testingPortionOfCenter.get(i) + "\t" + testingPortionOfRadius.get(i));
                fw.newLine();
            }
            fw.flush();
        } catch (IOException ex) {
            Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //create the config file:
        file = new File("config.net");
        try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
            fw.write("mp(1," + params.getNumNodesHidden() + ",1)");
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
        
        try {
            //TODO neskor zabranit spustaniu viacerych veci naraz (disablovat Run button, kym neskonci aktualna)
            Process p = Runtime.getRuntime().exec("cmd /c call c config");
            p.waitFor();
            System.out.println("should be done now.");
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(IntervalMLPCcode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //TODO opravit tieto dummy veci
        report.setForecastValues(new double[]{});
        report.setFittedValues(new double[]{});
        report.setFittedValuesPlotCode("plot.ts(sin(seq(0,2*pi,length=1000)))"); 
        List<Double> dummyErrorMeasures = new ArrayList<>();
        dummyErrorMeasures.add(0.0);dummyErrorMeasures.add(0.1);
        dummyErrorMeasures.add(0.2);dummyErrorMeasures.add(0.3);
        dummyErrorMeasures.add(0.4);dummyErrorMeasures.add(0.5);
        dummyErrorMeasures.add(0.6);dummyErrorMeasures.add(0.7);
        dummyErrorMeasures.add(0.8);dummyErrorMeasures.add(0.9);
        dummyErrorMeasures.add(1.0);dummyErrorMeasures.add(1.1);
        report.setErrorMeasures(dummyErrorMeasures);
        
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
