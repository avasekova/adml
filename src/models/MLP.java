package models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rcaller.RCaller;
import rcaller.RCode;
import utils.Utils;

public class MLP {
    
    private static final String ACC = "acc";

    
    public static TrainAndTestReport trainAndTestNnetar(List<Double> allData, int percentTrain) {
        TrainAndTestReport report = new TrainAndTestReport();
        
        RCaller caller = new RCaller();
        caller.setRscriptExecutable(Utils.RSCRIPT_EXE);
        
        RCode code = new RCode();
        code.clear();
        
        code.R_require("forecast");
        int numTrainingEntries = Math.round(((float) percentTrain/100)*allData.size());
        List<Double> trainingPortionOfData = allData.subList(0, numTrainingEntries);
        List<Double> testingPortionOfData = allData.subList(numTrainingEntries, allData.size());
        
        code.addDoubleArray("traindata", Utils.listToArray(trainingPortionOfData));
        code.addRCode("nnetwork <- nnetar(traindata)");
        
        code.addRCode("forecasted <- forecast(nnetwork, " + testingPortionOfData.size() + ")");
        
        code.addDoubleArray("testdata", Utils.listToArray(testingPortionOfData));
        code.addRCode(ACC + " <- accuracy(forecasted, testdata)");
        
        caller.setRCode(code);
        
        caller.runAndReturnResult(ACC);
        
        double[] acc = caller.getParser().getAsDoubleArray(ACC); //pozor na poradie vysledkov, ochenta setenta...
        //vrati vysledky po stlpcoch, tj. ME train, ME test, RMSE train, RMSE test, MAE, MPE, MAPE, MASE
        
        //dalo by sa aj maticu, ale momentalne mi staci ten list:
        //double[][] acc2 = caller.getParser().getAsDoubleMatrix(ACC, 6, 2);
        
        System.out.println(Arrays.toString(acc));
        report.setErrorMeasures(Utils.arrayToList(acc));
        return report;
    }
}
