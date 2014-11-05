package models;

import gui.DataTableModel;
import java.util.List;
import params.Params;
import params.RBFParams;
import params.RBFintParams;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.Utils;
import utils.imlp.Interval;
import utils.imlp.dist.WeightedEuclideanDistance;

public class RBFint {
    
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        RBFParams paramsCenter = ((RBFintParams)parameters).getParamsCenter();
        RBFParams paramsRadius = ((RBFintParams)parameters).getParamsRadius();
        
        //bez ohladu na to, ci mam Center a Radius alebo LB a UB (tj ci isCenterRadius je true alebo false),
        //  pocita sa s tym ako s Center a Radius. takze nijak neupravujem data.
        
        RBF rbf = new RBF();
        TrainAndTestReportCrisp reportCenter = (TrainAndTestReportCrisp) rbf.forecast(dataTableModel, paramsCenter);
        TrainAndTestReportCrisp reportRadius = (TrainAndTestReportCrisp) rbf.forecast(dataTableModel, paramsRadius);
        
        List<Interval> realOutputsIntervalTrain = Utils.zipCentersRadiiToIntervals(
                Utils.arrayToList(reportCenter.getRealOutputsTrain()), Utils.arrayToList(reportRadius.getRealOutputsTrain()));
        List<Interval> realOutputsIntervalTest = Utils.zipCentersRadiiToIntervals(
                Utils.arrayToList(reportCenter.getRealOutputsTest()), Utils.arrayToList(reportRadius.getRealOutputsTest()));
        List<Interval> fittedVals = Utils.zipCentersRadiiToIntervals(Utils.arrayToList(reportCenter.getFittedValues()),
                Utils.arrayToList(reportRadius.getFittedValues()));
        List<Interval> forecastsTest = Utils.zipCentersRadiiToIntervals(Utils.arrayToList(reportCenter.getForecastValuesTest()),
                Utils.arrayToList(reportRadius.getForecastValuesTest()));
        List<Interval> forecastsFuture = Utils.zipCentersRadiiToIntervals(Utils.arrayToList(reportCenter.getForecastValuesFuture()),
                Utils.arrayToList(reportRadius.getForecastValuesFuture()));
        
        //TODO nechat vybrat distance! ((RBFintParams)parameters).getDistanceFunction()
        List<Double> errorsTrain = Utils.getErrorsForIntervals(realOutputsIntervalTrain, fittedVals, new WeightedEuclideanDistance(0.5));
        List<Double> errorsTest = Utils.getErrorsForIntervals(realOutputsIntervalTest, forecastsTest, new WeightedEuclideanDistance(0.5));
        
        ErrorMeasuresInterval errorMeasures = new ErrorMeasuresInterval();
        errorMeasures.setMEtrain(ErrorMeasuresUtils.ME(errorsTrain));
        errorMeasures.setMEtest(ErrorMeasuresUtils.ME(errorsTest));
        errorMeasures.setRMSEtrain(ErrorMeasuresUtils.RMSE(errorsTrain));
        errorMeasures.setRMSEtest(ErrorMeasuresUtils.RMSE(errorsTest));
        errorMeasures.setMAEtrain(ErrorMeasuresUtils.MAE(errorsTrain));
        errorMeasures.setMAEtest(ErrorMeasuresUtils.MAE(errorsTest));
        errorMeasures.setMSEtrain(ErrorMeasuresUtils.MSE(errorsTrain));
        errorMeasures.setMSEtest(ErrorMeasuresUtils.MSE(errorsTest));
        errorMeasures.setMeanCoverageTrain(ErrorMeasuresUtils.meanCoverage(realOutputsIntervalTrain, fittedVals));
        errorMeasures.setMeanCoverageTest(ErrorMeasuresUtils.meanCoverage(realOutputsIntervalTest, forecastsTest));
        errorMeasures.setMeanEfficiencyTrain(ErrorMeasuresUtils.meanEfficiency(realOutputsIntervalTrain, fittedVals));
        errorMeasures.setMeanEfficiencyTest(ErrorMeasuresUtils.meanEfficiency(realOutputsIntervalTest, forecastsTest));
        errorMeasures.setTheilsUintervalTrain(ErrorMeasuresUtils.theilsUInterval(realOutputsIntervalTrain, fittedVals));
        errorMeasures.setTheilsUintervalTest(ErrorMeasuresUtils.theilsUInterval(realOutputsIntervalTest, forecastsTest));
        errorMeasures.setArvIntervalTrain(ErrorMeasuresUtils.ARVinterval(realOutputsIntervalTrain, fittedVals));
        errorMeasures.setArvIntervalTest(ErrorMeasuresUtils.ARVinterval(realOutputsIntervalTest, forecastsTest));

        TrainAndTestReportInterval report = new TrainAndTestReportInterval("RBF(i)");
//        report.setModelDescription("(" + ((RBFintParams)parameters).getDistanceFunction() + ")");
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
