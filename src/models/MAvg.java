package models;

import gui.tablemodels.DataTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.params.MAvgParams;
import models.params.Params;
import org.rosuda.JRI.REXP;
import utils.Const;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresUtils;
import utils.MyRengine;
import utils.Utils;

public class MAvg implements Forecastable {

    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String FIT = Const.FIT + Utils.getCounter();
        
        MAvgParams params = (MAvgParams) parameters;
        
        List<Double> allData = dataTableModel.getDataForColname(params.getColName());
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.eval("require(forecast)");
        int numTrainingEntries = dataToUse.size();
        
        List<Double> inputTrain = dataToUse;
        
        rengine.assign(INPUT_TRAIN, Utils.listToArray(inputTrain));
        
        rengine.eval(FIT + " <- forecast::ma(" + INPUT_TRAIN + ", order=" + params.getOrder() + ")");
        REXP getFittedVals = rengine.eval(FIT);
        double[] fittedVals = getFittedVals.asDoubleArray();
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Const.MAvg);
        report.setModelDescription(params.toString());
        
        report.setNumTrainingEntries(numTrainingEntries);
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(Utils.listToArray(new ArrayList<>()));
        report.setForecastValuesFuture(Utils.listToArray(new ArrayList<>()));
        report.setPlotCode("plot.ts(c(" + FIT + "))"); //musi tam byt aj to c() obalovatko, aj ked je len jeden prvok... on ho totiz v PlotDraweri potom maze a chyba mu. TODO zmenit uz konecne
        report.setRealOutputsTrain(Utils.listToArray(inputTrain));
        report.setRealOutputsTest(Utils.listToArray(new ArrayList<>()));
        
        ErrorMeasuresCrisp errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresCrisp(inputTrain, new ArrayList<>(), 
                Utils.arrayToList(fittedVals), new ArrayList<>(), params.getSeasonality());
        report.setErrorMeasures(errorMeasures);
        
        rengine.rm(INPUT_TRAIN); //POZOR - nemazat z plotu!
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}