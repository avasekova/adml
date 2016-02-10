package models;

import models.params.*;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.Utils;
import utils.imlp.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Hybrid implements Forecastable {
    private static final long serialVersionUID = 1L;
    
    //TODO napisat aj prirucku na pridavanie veci do Hybrid - kde vsade to treba pridat, bo su to asi 3-4 miesta v kode
    @Override
    public TrainAndTestReport forecast(Map<String, List<Double>> dataTableModel, Params parameters) {
        Params paramsCenter = ((HybridParams)parameters).getParamsCenter();
        Params paramsRadius = ((HybridParams)parameters).getParamsRadius();
        
        //bez ohladu na to, ci mam Center a Radius alebo LB a UB (tj ci isCenterRadius je true alebo false),
        //  pocita sa s tym ako s Center a Radius. takze nijak neupravujem data.
        
        TrainAndTestReportCrisp reportCenter = null;
        TrainAndTestReportCrisp reportRadius = null;
        
        //center
        if (paramsCenter instanceof NnetarParams) {
            Nnetar nnetar = new Nnetar();
            reportCenter = (TrainAndTestReportCrisp) nnetar.forecast(dataTableModel, paramsCenter);
        } else if (paramsCenter instanceof NnetParams) {
            Nnet nnet = new Nnet();
            reportCenter = (TrainAndTestReportCrisp) nnet.forecast(dataTableModel, paramsCenter);
        } else if (paramsCenter instanceof RBFParams) {
            RBF rbf = new RBF();
            reportCenter = (TrainAndTestReportCrisp) rbf.forecast(dataTableModel, paramsCenter);
        } else if (paramsCenter instanceof ArimaParams) {
            Arima arima = new Arima();
            reportCenter = (TrainAndTestReportCrisp) arima.forecast(dataTableModel, paramsCenter);
        } else if (paramsCenter instanceof KNNfnnParams) {
            KNNfnn knnFnn = new KNNfnn();
            reportCenter = (TrainAndTestReportCrisp) knnFnn.forecast(dataTableModel, paramsCenter);
        } else if (paramsCenter instanceof KNNkknnParams) {
            KNNkknn knnKknn = new KNNkknn();
            reportCenter = (TrainAndTestReportCrisp) knnKknn.forecast(dataTableModel, paramsCenter);
        } else if (paramsCenter instanceof HoltParams) {
            Holt holt = new Holt();
            reportCenter = (TrainAndTestReportCrisp) holt.forecast(dataTableModel, paramsCenter);
        } else if (paramsCenter instanceof SESParams) {
            SES ses = new SES();
            reportCenter = (TrainAndTestReportCrisp) ses.forecast(dataTableModel, paramsCenter);
        } else if (paramsCenter instanceof BNNParams) {
            BNN bnn = new BNN();
            reportCenter = (TrainAndTestReportCrisp) bnn.forecast(dataTableModel, paramsCenter);
        }
        
        //radius
        if (paramsRadius instanceof NnetarParams) {
            Nnetar nnetar = new Nnetar();
            reportRadius = (TrainAndTestReportCrisp) nnetar.forecast(dataTableModel, paramsRadius);
        } else if (paramsRadius instanceof NnetParams) {
            Nnet nnet = new Nnet();
            reportRadius = (TrainAndTestReportCrisp) nnet.forecast(dataTableModel, paramsRadius);
        } else if (paramsRadius instanceof RBFParams) {
            RBF rbf = new RBF();
            reportRadius = (TrainAndTestReportCrisp) rbf.forecast(dataTableModel, paramsRadius);
        } else if (paramsRadius instanceof ArimaParams) {
            Arima arima = new Arima();
            reportRadius = (TrainAndTestReportCrisp) arima.forecast(dataTableModel, paramsRadius);
        } else if (paramsRadius instanceof KNNfnnParams) {
            KNNfnn knnFnn = new KNNfnn();
            reportRadius = (TrainAndTestReportCrisp) knnFnn.forecast(dataTableModel, paramsRadius);
        } else if (paramsRadius instanceof KNNkknnParams) {
            KNNkknn knnKknn = new KNNkknn();
            reportRadius = (TrainAndTestReportCrisp) knnKknn.forecast(dataTableModel, paramsRadius);
        } else if (paramsRadius instanceof HoltParams) {
            Holt holt = new Holt();
            reportRadius = (TrainAndTestReportCrisp) holt.forecast(dataTableModel, paramsRadius);
        } else if (paramsRadius instanceof SESParams) {
            SES ses = new SES();
            reportRadius = (TrainAndTestReportCrisp) ses.forecast(dataTableModel, paramsRadius);
        } else if (paramsRadius instanceof BNNParams) {
            BNN bnn = new BNN();
            reportRadius = (TrainAndTestReportCrisp) bnn.forecast(dataTableModel, paramsRadius);
        }
        
        //zbytok je potom taky isty
        List<Interval> realOutputsIntervalTrain = Utils.zipCentersRadiiToIntervals(
                Utils.arrayToList(reportCenter.getRealOutputsTrain()), Utils.arrayToList(reportRadius.getRealOutputsTrain()));
        List<Interval> realOutputsIntervalTest = Utils.zipCentersRadiiToIntervals(
                Utils.arrayToList(reportCenter.getRealOutputsTest()), Utils.arrayToList(reportRadius.getRealOutputsTest()));
        List<Interval> fittedVals = Utils.zipCentersRadiiToIntervals(Utils.arrayToList(reportCenter.getFittedValues()),
                Utils.arrayToList(reportRadius.getFittedValues()));
        List<Interval> forecastsTest = Utils.zipCentersRadiiToIntervals(Utils.arrayToList(reportCenter.getForecastValuesTest()),
                Utils.arrayToList(reportRadius.getForecastValuesTest()));
        
        List<Interval> forecastsFuture = new ArrayList<>();
        if ((reportCenter.getForecastValuesFuture().length != 0) && (reportRadius.getForecastValuesFuture().length != 0)) {
            forecastsFuture = Utils.zipCentersRadiiToIntervals(Utils.arrayToList(reportCenter.getForecastValuesFuture()),
                Utils.arrayToList(reportRadius.getForecastValuesFuture()));
        }
        
        
        ErrorMeasuresInterval errorMeasures = ErrorMeasuresUtils.computeAllErrorMeasuresInterval(realOutputsIntervalTrain, 
                realOutputsIntervalTest, fittedVals, forecastsTest, ((HybridParams)parameters).getDistance(), parameters.getSeasonality());
        
        TrainAndTestReportInterval report = new TrainAndTestReportInterval(Model.HYBRID);
        report.setModelDescription(parameters.toString());
        report.setNumTrainingEntries(reportCenter.getNumTrainingEntries());
        
        
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(forecastsTest);
        report.setForecastValuesFuture(forecastsFuture);
        
        report.setErrorMeasures(errorMeasures);
        
        //hack, aby sme mohli mat oba ploty v jednej premennej
        //TODO produce NN diagram?
//        report.setNnDiagramPlotCode(reportCenter.getNnDiagramPlotCode() + "; " + reportRadius.getNnDiagramPlotCode());
        
        realOutputsIntervalTrain.addAll(realOutputsIntervalTest);
        report.setRealValues(realOutputsIntervalTrain);
        
        return report;
    }

}
