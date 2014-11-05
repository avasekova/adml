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
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String INPUT_TEST = Const.INPUT + Utils.getCounter();
        final String OUTPUT_TRAIN = Const.OUTPUT + Utils.getCounter();
        final String OUTPUT_TEST = Const.OUTPUT + Utils.getCounter();
        final String NNETWORK = Const.NNETWORK + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        final String FORECAST_TEST = Const.FORECAST_VALS + Utils.getCounter();
        
        RBFParams params = (RBFParams) parameters;
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp("RBF");
        report.setModelDescription("");
        
        List<List<Double>> data = prepareData(dataTableModel, params.getExplVars(), 
                params.getOutVars(), params.getDataRangeFrom()-1, params.getDataRangeTo());
        
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*data.get(0).size());
        report.setNumTrainingEntries(numTrainingEntries);
        
        //most likely we will never allow more than 1... but whatever.
        if (params.getOutVars().size() == 1) {
            List<Double> trainingOutputs = data.get(data.size() - 1).subList(0, numTrainingEntries);
            List<Double> testingOutputs = data.get(data.size() - 1).subList(numTrainingEntries, data.get(0).size());
            
            List<List<Double>> trainingInputs = getInputs(data, 1, 0, numTrainingEntries);
            List<List<Double>> testingInputs = getInputs(data, 1, numTrainingEntries, data.get(0).size());
            
            Rengine rengine = MyRengine.getRengine();
            rengine.eval("require(RSNNS)");
            
            ((MyRengine)rengine).assignMatrix(INPUT_TRAIN, trainingInputs);
            ((MyRengine)rengine).assignMatrix(INPUT_TEST, testingInputs);
            rengine.assign(OUTPUT_TRAIN, Utils.listToArray(trainingOutputs));
            rengine.assign(OUTPUT_TEST, Utils.listToArray(testingOutputs));
            
            rengine.eval(NNETWORK + " <- RSNNS::rbf(" + INPUT_TRAIN + ", " + OUTPUT_TRAIN + ", size=" + params.getNumNodesHidden()
                          + ", maxit=" + params.getMaxIterations() + ", linOut=TRUE)");
            rengine.eval(FIT + " <- fitted(" + NNETWORK + ")");
            REXP getFittedVals = rengine.eval(FIT);
            double[] fittedVals = getFittedVals.asDoubleArray();
            report.setFittedValues(fittedVals);
            
            rengine.eval(FORECAST_TEST + " <- predict(" + NNETWORK + ", data.frame(" + INPUT_TEST + "))");
            REXP getForecastValsTest = rengine.eval(FORECAST_TEST);
            double[] forecastValsTest = getForecastValsTest.asDoubleArray();
            report.setForecastValuesTest(forecastValsTest);
            
            report.setPlotCode("plot.ts(c(rep(NA, " + maxLag + "), " + FIT + ", " + FORECAST_TEST + "))");
            
            report.setRealOutputsTrain(Utils.listToArray(trainingOutputs));
            report.setRealOutputsTest(Utils.listToArray(testingOutputs));
            
            ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(
                    trainingOutputs, testingOutputs, Utils.arrayToList(fittedVals), Utils.arrayToList(forecastValsTest));
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

    private List<List<Double>> getInputs(List<List<Double>> data, int numOutVars, int from, int to) {
        List<List<Double>> inputsTrainOrTest = new ArrayList<>();
        
        for (int i = 0; i < (data.size() - numOutVars); i++) {
            inputsTrainOrTest.add(data.get(i).subList(from, to));
        }
        
        return inputsTrainOrTest;
    }
}
