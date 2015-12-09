package models;

import models.params.MAvgParams;
import models.params.Params;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MAvg implements Forecastable {
    private static final long serialVersionUID = 1L;

    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        
        MAvgParams params = (MAvgParams) parameters;
        
        List<Double> allData = dataTableModel.get(params.getColName());
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("forecast");
        int numTrainingEntries = dataToUse.size();
        
        List<Double> inputTrain = dataToUse;
        
        rengine.assign(INPUT_TRAIN, Utils.listToArray(inputTrain));
        
        rengine.eval(FIT + " <- forecast::ma(" + INPUT_TRAIN + ", order=" + params.getOrder() + ")");
        double[] fittedVals = rengine.evalAndReturnArray(FIT);
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Model.MAvg);
        report.setModelDescription(params.toString());
        
        report.setNumTrainingEntries(numTrainingEntries);
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(Utils.listToArray(new ArrayList<>()));
        report.setForecastValuesFuture(Utils.listToArray(new ArrayList<>()));
        report.setPlotCode("plot.ts(c(" + Utils.arrayToRVectorString(fittedVals) + "))"); //musi tam byt aj to c() obalovatko, aj ked je len jeden prvok... on ho totiz v PlotDraweri potom maze a chyba mu. TODO zmenit uz konecne
        report.setRealOutputsTrain(Utils.listToArray(inputTrain));
        report.setRealOutputsTest(Utils.listToArray(new ArrayList<>()));
        
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(inputTrain, new ArrayList<>(), 
                Utils.arrayToList(fittedVals), new ArrayList<>(), params.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        rengine.rm(INPUT_TRAIN, FIT);
        
        return report;
    }
}
