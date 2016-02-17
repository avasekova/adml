package models;

import models.params.BNNParams;
import models.params.Params;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BNN implements Forecastable {
    private static final long serialVersionUID = 1L;
    
    private int maxLag = 0; //a stupid solution, but whatever

    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String INPUT_TEST = Const.INPUT + Utils.getCounter();
        final String OUTPUT = Const.OUTPUT + Utils.getCounter();
        final String OUTPUT_TRAIN = Const.OUTPUT + Utils.getCounter();
        final String OUTPUT_TEST = Const.OUTPUT + Utils.getCounter();
//        final String DATA_TRAIN = Const.INPUT + Utils.getCounter();
//        final String DATA_TEST = Const.INPUT + Utils.getCounter();
        final String NNETWORK = Const.NNETWORK + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        final String FORECAST_TEST = Const.FORECAST_VALS + Utils.getCounter();
        
        BNNParams params = (BNNParams) parameters;
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Model.BNN);
        report.setModelDescription(params.toString());
        
        List<List<Double>> data = prepareData(dataTableModel, params.getExplVars(), 
                params.getOutVars(), params.getDataRangeFrom()-1, params.getDataRangeTo());
        
        //int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*(data.get(0).size() + maxLag));
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*(dataTableModel.get(params.getExplVars().get(0).getFieldName()).size()));
        report.setNumTrainingEntries(numTrainingEntries);
        numTrainingEntries -= maxLag; //from now on we will compute with the "rectangle" data anyway
        
        //most likely we will never allow more than 1... but whatever.
        if (params.getOutVars().size() == 1) {
            List<Double> allOutputs = data.get(data.size() - 1);
            
            List<List<Double>> allInputs = new ArrayList<>();
            allInputs.addAll(data);
            allInputs.remove(data.size() - 1); //without the last one, i.e. without the output var
            List<List<Double>> trainingInputs = RBF.getInputsCut(allInputs, 0, numTrainingEntries);
            List<List<Double>> testingInputs = RBF.getInputsCut(allInputs, numTrainingEntries, allInputs.get(0).size());
            
            MyRengine rengine = MyRengine.getRengine();
            rengine.require("brnn");
            
            rengine.assignMatrix(INPUT_TRAIN, trainingInputs);
            rengine.assignMatrix(INPUT_TEST, testingInputs);
            rengine.assign(OUTPUT, Utils.listToArray(allOutputs));
            rengine.eval(OUTPUT_TRAIN + " <- " + OUTPUT + "[1:" + numTrainingEntries + "]");
            rengine.eval(OUTPUT_TEST + " <- " + OUTPUT + "[" + (numTrainingEntries+1) + ":length(" + OUTPUT + ")]");
            
            rengine.eval(NNETWORK + " <- brnn::brnn(" + INPUT_TRAIN + ", " + OUTPUT_TRAIN + ")");
            
            rengine.eval(FIT + " <- predict(" + NNETWORK + ")");
            double[] fittedVals = rengine.evalAndReturnArray("c(rep(NA, " + maxLag + "), " + FIT + ")");
            report.setFittedValues(fittedVals);
            
            rengine.eval(FORECAST_TEST + " <- predict(" + NNETWORK + ", " + INPUT_TEST + ")");

            double[] forecastValsTest = rengine.evalAndReturnArray(FORECAST_TEST);
            report.setForecastValuesTest(forecastValsTest);
            
            report.setPlotCode("plot.ts(c(" + Utils.arrayToRVectorString(fittedVals) + ", " + Utils.arrayToRVectorString(forecastValsTest) + "))");
            
            //real outputs train and test are just the original data (used only for plotting)
            //assuming we only have one OutVar:
            List<Double> realOutputs = dataTableModel.get(params.getOutVars().get(0).getFieldName()).subList(params.getDataRangeFrom()-1, params.getDataRangeTo());
            List<Double> realOutputsTrain = realOutputs.subList(0, numTrainingEntries+maxLag);
            List<Double> realOutputsTest = realOutputs.subList(numTrainingEntries+maxLag, realOutputs.size());
            report.setRealOutputsTrain(Utils.listToArray(realOutputsTrain));
            report.setRealOutputsTest(Utils.listToArray(realOutputsTest));
            
            ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(realOutputsTrain, realOutputsTest, 
                    Utils.arrayToList(fittedVals), Utils.arrayToList(forecastValsTest), parameters.getSeasonality());
            report.setErrorMeasures(errorMeasures);
            
            //future forecasts as usual - I know the first and the rest can be computed iteratively
            //TODO add when I am adding this to the iMLP C code, it's the same thing.
            
            rengine.rm(INPUT_TRAIN, INPUT_TEST, OUTPUT, OUTPUT_TRAIN, OUTPUT_TEST, NNETWORK); //! do not delete FIT, FORECAST_TEST
        }
        
        return report;
    }
    
    //as in iMLP C code //TODO de-duplicate
    private List<List<Double>> prepareData(Map<String, List<Double>> dataTableModel, List<CrispVariable> explVars,
                                           List<CrispVariable> outVars,
                                           int from, int to) {
        List<List<Double>> data = new ArrayList<>();
        
        int maximumLag = 0;
        for (CrispVariable var : explVars) {
            List<Double> vals = dataTableModel.get(var.getFieldName()).subList(from, to);
            data.add(IntervalMLPCcode.lagBy(var.getLag(), vals));
            
            maximumLag = Math.max(maximumLag, var.getLag());
        }
        
        this.maxLag = maximumLag;
        
        for (CrispVariable var : outVars) {
            List<Double> vals = dataTableModel.get(var.getFieldName()).subList(from, to);
            data.add(vals);
        }
        
        return IntervalMLPCcode.trimDataToRectangle(data, maximumLag);
        //here we've got "rectangle" data without any NaNs at the beginning.
    }

    public static List<List<Double>> getInputsCut(List<List<Double>> data, int from, int to) {
        List<List<Double>> inputsTrainOrTest = new ArrayList<>();
        
        for (List<Double> l : data) {
            inputsTrainOrTest.add(l.subList(from, to));
        }
        
        return inputsTrainOrTest;
    }

    public static List<List<Double>> getScaledInputs(List<List<Double>> data, int numOutVars) {
        final String SCALED = "scaled." + Utils.getCounter();
        final String LIST = Const.INPUT + Utils.getCounter();
        
        MyRengine rengine = MyRengine.getRengine();
        
        List<List<Double>> inputs = new ArrayList<>();
        
        //first cut off the output vars
        for (int i = 0; i < (data.size()-numOutVars); i++) {
            inputs.add(data.get(i));
        }
        
        //then go column by column and scale the vals:
        //there's nothing wrong with scaling each column separately instead of scaling the whole matrix, is there?
        //the only important thing is to scale the whole column, i.e. before separating the train and test data.
        List<List<Double>> inputsScaled = new ArrayList<>();
        for (List<Double> unscaled : inputs) {
            rengine.assign(LIST, Utils.listToArray(unscaled));
            rengine.eval(SCALED + " <- MLPtoR.scale(" + LIST + ")");
            inputsScaled.add(rengine.evalAndReturnList(SCALED));
        }
        
        return inputsScaled;
    }
}
