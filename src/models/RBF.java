package models;

import gui.DataTableModel;
import java.util.ArrayList;
import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.Params;
import params.RBFParams;
import utils.Const;
import utils.CrispExplanatoryVariable;
import utils.CrispOutputVariable;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;

public class RBF {
    
    private int maxLag = 0; //a stupid solution, but whatever

    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        final String SCALED_INPUT_TRAIN = "scaled." + Const.INPUT + Utils.getCounter();
        final String SCALED_INPUT_TEST = "scaled." + Const.INPUT + Utils.getCounter();
        final String OUTPUT = Const.OUTPUT + Utils.getCounter();
        final String SCALED_OUTPUT = Const.OUTPUT + Utils.getCounter();
        final String SCALED_OUTPUT_TRAIN = "scaled." + Const.OUTPUT + Utils.getCounter();
        final String SCALED_OUTPUT_TEST = "scaled." + Const.OUTPUT + Utils.getCounter();
        final String NNETWORK = Const.NNETWORK + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        final String UNSCALED_FIT = "unscaled." + FIT;
        final String FORECAST_TEST = Const.FORECAST_VALS + Utils.getCounter();
        final String UNSCALED_FORECAST_TEST = "unscaled." + FORECAST_TEST;
        
        RBFParams params = (RBFParams) parameters;
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp("RBF");
        report.setModelDescription("");
        
        List<List<Double>> data = prepareData(dataTableModel, params.getExplVars(), 
                params.getOutVars(), params.getDataRangeFrom()-1, params.getDataRangeTo());
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*data.get(0).size());
        report.setNumTrainingEntries(numTrainingEntries);
        numTrainingEntries -= maxLag;
        
        //most likely we will never allow more than 1... but whatever.
        if (params.getOutVars().size() == 1) {
            List<Double> allOutputs = data.get(data.size() - 1);
            
            List<List<Double>> allInputsScaled = getScaledInputs(data, 1);
            List<List<Double>> trainingInputsScaled = getInputsCut(allInputsScaled, 0, numTrainingEntries);
            List<List<Double>> testingInputsScaled = getInputsCut(allInputsScaled, numTrainingEntries, allInputsScaled.get(0).size());
            
            Rengine rengine = MyRengine.getRengine();
            rengine.eval("require(RSNNS)");
            
            ((MyRengine)rengine).assignMatrix(SCALED_INPUT_TRAIN, trainingInputsScaled);
            ((MyRengine)rengine).assignMatrix(SCALED_INPUT_TEST, testingInputsScaled);
            rengine.assign(OUTPUT, Utils.listToArray(allOutputs));
            rengine.eval(SCALED_OUTPUT + " <- MLPtoR.scale(" + OUTPUT + ")");
            rengine.eval(SCALED_OUTPUT_TRAIN + " <- " + SCALED_OUTPUT + "[1:" + numTrainingEntries + "]");
            rengine.eval(SCALED_OUTPUT_TEST + " <- " + SCALED_OUTPUT + "[" + (numTrainingEntries+1) + ":length(" + SCALED_OUTPUT + ")]");
            
            rengine.eval(NNETWORK + " <- RSNNS::rbf(" + SCALED_INPUT_TRAIN + ", " + SCALED_OUTPUT_TRAIN + ", size=" + params.getNumNodesHidden()
                          + ", maxit=" + params.getMaxIterations() + ", linOut=TRUE)");
            rengine.eval(FIT + " <- fitted(" + NNETWORK + ")");
            rengine.eval(UNSCALED_FIT + " <- MLPtoR.unscale(" + FIT + ", " + OUTPUT + ")");
            REXP getFittedVals = rengine.eval(UNSCALED_FIT);
            double[] fittedVals = getFittedVals.asDoubleArray();
            report.setFittedValues(fittedVals);
            
            rengine.eval(FORECAST_TEST + " <- predict(" + NNETWORK + ", data.frame(" + SCALED_INPUT_TEST + "))");
            rengine.eval(UNSCALED_FORECAST_TEST + " <- MLPtoR.unscale(" + FORECAST_TEST + ", " + OUTPUT + ")");
            REXP getForecastValsTest = rengine.eval(UNSCALED_FORECAST_TEST);
            double[] forecastValsTest = getForecastValsTest.asDoubleArray();
            report.setForecastValuesTest(forecastValsTest);
            
            report.setPlotCode("plot.ts(c(rep(NA, " + maxLag + "), " + UNSCALED_FIT + ", " + UNSCALED_FORECAST_TEST + "))");
            
            REXP getOutputsTrain = rengine.eval("MLPtoR.unscale(" + SCALED_OUTPUT_TRAIN + ", " + OUTPUT + ")");
            double[] trainingOutputs = getOutputsTrain.asDoubleArray();
            report.setRealOutputsTrain(trainingOutputs);
            REXP getOutputsTest = rengine.eval("MLPtoR.unscale(" + SCALED_OUTPUT_TEST + ", " + OUTPUT + ")");
            double[] testingOutputs = getOutputsTest.asDoubleArray();
            report.setRealOutputsTest(testingOutputs);
            
            ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(
                    Utils.arrayToList(trainingOutputs), Utils.arrayToList(testingOutputs), 
                    Utils.arrayToList(fittedVals), Utils.arrayToList(forecastValsTest));
            report.setErrorMeasures(errorMeasures);
            
            //future forecasts klasicky - prvy viem, a dalsie sa daju napocitat iterativne.
            //TODO doplnit, ked budem doplnat aj do iMLP C code, lebo to bude fungovat tak isto.
        }
        
        return report;
    }
    
    //podla vzoru iMLP C code
    private List<List<Double>> prepareData(DataTableModel dataTableModel, List<CrispExplanatoryVariable> explVars, 
                                                                          List<CrispOutputVariable> outVars,
                                                                          int from, int to) {
        List<List<Double>> data = new ArrayList<>();
        
        int maximumLag = 0;
        for (CrispExplanatoryVariable var : explVars) {
            List<Double> vals = dataTableModel.getDataForColname(var.getFieldName()).subList(from, to);
            data.add(IntervalMLPCcode.lagBy(var.getLag(), vals));
            
            maximumLag = Math.max(maximumLag, var.getLag());
        }
        
        this.maxLag = maximumLag;
        
        for (CrispOutputVariable var : outVars) {
            List<Double> vals = dataTableModel.getDataForColname(var.getFieldName()).subList(from, to);
            data.add(vals);
        }
        
        return IntervalMLPCcode.trimDataToRectangle(data, maximumLag);
    }

    private List<List<Double>> getInputsCut(List<List<Double>> data, int from, int to) {
        List<List<Double>> inputsTrainOrTest = new ArrayList<>();
        
        for (List<Double> l : data) {
            inputsTrainOrTest.add(l.subList(from, to));
        }
        
        return inputsTrainOrTest;
    }

    private List<List<Double>> getScaledInputs(List<List<Double>> data, int numOutVars) {
        final String SCALED = "scaled." + Utils.getCounter();
        final String LIST = Const.INPUT + Utils.getCounter();
        
        Rengine rengine = MyRengine.getRengine();
        
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
            REXP getScaled = rengine.eval(SCALED);
            double[] scaled = getScaled.asDoubleArray();
            inputsScaled.add(Utils.arrayToList(scaled));
        }
        
        return inputsScaled;
    }
}
