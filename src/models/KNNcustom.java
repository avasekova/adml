package models;

import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.KNNcustomParams;
import params.KNNfnnParams;
import params.Params;
import utils.MyRengine;

public class KNNcustom implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        
        KNNcustomParams params = (KNNcustomParams) parameters;
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp("kNN (custom)");
        
        Rengine rengine = MyRengine.getRengine();
        
        String distanceFunction = "abs.difference"; //default
        switch (params.getDistanceMethodName()) {
            case "absolute difference":
                distanceFunction = "abs.difference";
                break;
            //add more here
        }
        
        String combinationFunction = "mean"; //default
        switch (params.getCombinationMethodName()) {
            case "average":
                combinationFunction = "mean";
                break;
            //add more here
        }
        
        
        //predict.knn(lag, k, len, data, distance = abs.difference, combination = mean)
        
        
        
        return null;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
