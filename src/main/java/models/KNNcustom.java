package models;

import gui.tablemodels.DataTableModel;
import java.util.List;
import org.rosuda.JRI.REXP;
import models.params.KNNcustomParams;
import models.params.Params;
import utils.Const;
import utils.ErrorMeasuresCrisp;
import utils.MyRengine;
import utils.Utils;

//(not used, may contain dragons)
public class KNNcustom implements Forecastable {

    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        final String INPUT = Const.INPUT + Utils.getCounter();
        final String OUTPUT = Const.OUTPUT + Utils.getCounter();
        final String INPUT_TRAIN = Const.INPUT + Utils.getCounter();
        final String INPUT_TEST = Const.INPUT + Utils.getCounter();
        final String OUTPUT_TRAIN = Const.OUTPUT + Utils.getCounter();
        final String OUTPUT_TEST = Const.OUTPUT + Utils.getCounter();
        final String FORECAST = Const.FORECAST_VALS + Utils.getCounter();
        
        KNNcustomParams params = (KNNcustomParams) parameters;
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Const.KNN_CUSTOM);
        
        List<Double> allData = dataTableModel.getDataForColname(params.getColName());
        List<Double> dataToUse = allData.subList((params.getDataRangeFrom() - 1), params.getDataRangeTo());
        
        MyRengine rengine = MyRengine.getRengine();
        
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
        
        rengine.assign(INPUT, Utils.listToArray(dataToUse));
        rengine.assign(OUTPUT, Utils.listToArray(dataToUse));
        rengine.eval(INPUT + " <- " + INPUT + "[1:(length(" + INPUT + ") - " + params.getLag() + ")]"); //1:(length-lag)
        rengine.eval(OUTPUT + " <- " + OUTPUT + "[(1 + " + params.getLag() + "):length(" + OUTPUT + ")]"); //(1+lag):length
        
        int lengthInputOutput = dataToUse.size() - params.getLag();
        int numTrainingEntries = Math.round(((float) params.getPercentTrain()/100)*lengthInputOutput);
        report.setNumTrainingEntries(numTrainingEntries);
        
        rengine.eval(INPUT_TRAIN + " <- " + INPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(INPUT_TEST + " <- " + INPUT + "[" + (numTrainingEntries+1) + ":length(" + INPUT + ")]");
        rengine.eval(OUTPUT_TRAIN + " <- " + OUTPUT + "[1:" + numTrainingEntries + "]");
        rengine.eval(OUTPUT_TEST + " <- " + OUTPUT + "[" + (numTrainingEntries+1) + ":length(" + OUTPUT + ")]");
        
        
        rengine.eval(FORECAST + " <- predict.knn(" + params.getLag() + ", " + params.getNumNeighbours() + ", "
                                                   + params.getLengthHistory() + ", " + INPUT_TRAIN
                                                   + ", distance = " + distanceFunction
                                                   + ", combination = " + combinationFunction + ")");
        REXP getForecast = rengine.eval(FORECAST + "$oneforecast");
        double[] forecasts = getForecast.asDoubleArray();
        double forcast = forecasts[0]; //for now, only gives one forecast, TODO more
        System.out.println("the one and only forecast (kNN custom): " + forcast); //TODO ten forecast je okrem toho zle, pretoze predikuje prvu hodnotu za train datami a nie prvu buducu
        
        double[] dummyVals = new double[]{ 1,2,3,4,5,6,7,8,9,10,100 };
        report.setFittedValues(dummyVals); //TODO zmenit
        report.setForecastValuesTest(dummyVals); //TODO zmenit
        report.setPlotCode("plot.ts(sin(seq(0,2*pi))"); //potom pridat lag: rep(NA, " + params.getLag() + "), 
        
        ErrorMeasuresCrisp errorMeasures = new ErrorMeasuresCrisp();
        errorMeasures.setMAEtrain(0.0);
        errorMeasures.setMAEtest(0.0);
        errorMeasures.setMAPEtrain(0.0);
        errorMeasures.setMAPEtest(0.0);
        errorMeasures.setMASEtrain(0.0);
        errorMeasures.setMASEtest(0.0);
        errorMeasures.setMEtrain(0.0);
        errorMeasures.setMEtest(0.0);
        errorMeasures.setMPEtrain(0.0);
        errorMeasures.setMPEtest(0.0);
        errorMeasures.setMSEtrain(0.0);
        errorMeasures.setMSEtest(0.0);
        errorMeasures.setRMSEtrain(0.0);
        errorMeasures.setRMSEtest(0.0);
        errorMeasures.setTheilUtrain(0.0);
        errorMeasures.setTheilUtest(0.0);
        
        report.setErrorMeasures(errorMeasures);
        
        //TODO ako ziskat z mojej funkcie fitted vals?
        
        rengine.rm(INPUT, OUTPUT, INPUT_TRAIN, INPUT_TEST, OUTPUT_TRAIN, OUTPUT_TEST, FORECAST);
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}