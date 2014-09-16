package models;

import java.util.List;
import params.NeuralnetParams;
import params.Params;

public class Neuralnet implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        TrainAndTestReport report = new TrainAndTestReport("neuralnet");

//        ////the magic comes here
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        NeuralnetParams params = (NeuralnetParams) parameters;
        StringBuilder optionalParams = new StringBuilder();
//        if (params.getNumNonSeasonalLags() != null) {
//            optionalParams.append(", p = ").append(params.getNumNonSeasonalLags());
//        }
//        
//        if (params.getNumSeasonalLags() != null) {
//            optionalParams.append(", P = ").append(params.getNumSeasonalLags());
//        }
//        
//        if (params.getNumNodesHidden() != null) {
//            optionalParams.append(", size = ").append(params.getNumNodesHidden());
//}
//        
//        if (params.getNumReps() != null) {
//            optionalParams.append(", repeats = ").append(params.getNumReps());
//        }
//        
//        if (params.getLambda() != null) {
//            optionalParams.append(", lambda = ").append(params.getLambda());
//        }
        
        return optionalParams.toString();
    }
    
}
