package models;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcaller.RCaller;
import rcaller.RCode;
import utils.Utils;

public class MLP {
    
    private static final String ACC = "acc";

    
    public static TrainAndTestReport trainAndTestNnetar(List<Double> allData, int percentTrain) {
        TrainAndTestReport report = new TrainAndTestReport();

        RCaller caller = Utils.getCleanRCaller();
        caller.deleteTempFiles();

        RCode code = new RCode();
        code.clear();

        code.R_require("forecast");
        int numTrainingEntries = Math.round(((float) percentTrain/100)*allData.size());
        List<Double> trainingPortionOfData = allData.subList(0, numTrainingEntries);
        report.setTrainData(trainingPortionOfData);
        List<Double> testingPortionOfData = allData.subList(numTrainingEntries, allData.size());
        report.setTestData(testingPortionOfData);

        code.addDoubleArray("traindata", Utils.listToArray(trainingPortionOfData));
        code.addRCode("nnetwork <- nnetar(traindata)");

        code.addRCode("forecastedModel <- forecast(nnetwork, " + testingPortionOfData.size() + ")");
        //skoro ma svihlo, kym som na to prisla, ale:
        //1. vo "forecastedModel" je strasne vela heterogennych informacii, neda sa to len tak poslat cele Jave
        //2. takze ked chcem len tie forecastedValues, ziskam ich ako "forecastedModel$mean[1:8]", kde 8 je ich pocet...
        code.addRCode("forecastedVals <- forecastedModel$mean[1:" + testingPortionOfData.size() + "]");

        caller.setRCode(code);
        caller.runAndReturnResult("forecastedVals");
        double[] forecasted = caller.getParser().getAsDoubleArray("forecastedVals");
        report.setForecastData(Utils.arrayToList(forecasted));
            
        caller = Utils.getCleanRCaller();
        code.addDoubleArray("testdata", Utils.listToArray(testingPortionOfData));
        code.addRCode(ACC + " <- accuracy(forecastedModel, testdata)");

        caller.setRCode(code);
        caller.runAndReturnResult(ACC);

        double[] acc = caller.getParser().getAsDoubleArray(ACC); //pozor na poradie vysledkov, ochenta setenta...
        //vrati vysledky po stlpcoch, tj. ME train, ME test, RMSE train, RMSE test, MAE, MPE, MAPE, MASE

        //dalo by sa aj maticu, ale momentalne mi staci ten list:
        //double[][] acc2 = caller.getParser().getAsDoubleMatrix(ACC, 6, 2);

        report.setErrorMeasures(Utils.arrayToList(acc));
        
        try {
            caller = Utils.getCleanRCaller();
            File forecastPlotFile = code.startPlot();
            code.addRCode("plot(forecastedModel)");
            code.endPlot();
            caller.setRCode(code);
            caller.runOnly();
            report.setForecastPlot(code.getPlot(forecastPlotFile));
        } catch (IOException ex) {
            Logger.getLogger(MLP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("It's here.");
        
        return report;
    }
}
