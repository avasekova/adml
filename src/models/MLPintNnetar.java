package models;

import gui.DataTableModel;
import java.util.List;
import params.MLPintNnetarParams;
import params.NnetarParams;
import params.Params;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.Utils;
import utils.imlp.Interval;

public class MLPintNnetar implements Forecastable {

    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        NnetarParams paramsCenter = ((MLPintNnetarParams)parameters).getParamsCenter();
        NnetarParams paramsRadius = ((MLPintNnetarParams)parameters).getParamsRadius();
        
        Nnetar nnetar = new Nnetar();
        TrainAndTestReportCrisp reportCenter = (TrainAndTestReportCrisp) nnetar.forecast(dataTableModel, paramsCenter);
        TrainAndTestReportCrisp reportRadius = (TrainAndTestReportCrisp) nnetar.forecast(dataTableModel, paramsRadius);
        
        //sublistovat tie centers a radii az tu, ked uz to neovplyvni nnetar
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
        
        //TODO nechat vybrat distance!
        List<Double> errorsTrain = Utils.getErrorsForIntervals(realOutputsIntervalTrain, fittedVals, ((MLPintNnetarParams)parameters).getDistance());
        List<Double> errorsTest = Utils.getErrorsForIntervals(realOutputsIntervalTest, forecastsTest, ((MLPintNnetarParams)parameters).getDistance());
        
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

        TrainAndTestReportInterval report = new TrainAndTestReportInterval("MLP(i) (nnetar)");
        report.setModelDescription("(" + ((MLPintNnetarParams)parameters).getDistance() + ")");
        report.setNumTrainingEntries(reportCenter.getNumTrainingEntries());
        
        
        report.setFittedValues(fittedVals);
        report.setForecastValuesTest(forecastsTest);
        report.setForecastValuesFuture(forecastsFuture);
        
        report.setErrorMeasures(errorMeasures);
        
        //hack, aby sme mohli mat oba ploty v jednej premennej
        report.setNnDiagramPlotCode(reportCenter.getNnDiagramPlotCode() + "; " + reportRadius.getNnDiagramPlotCode());
        
        realOutputsIntervalTrain.addAll(realOutputsIntervalTest);
        report.setRealValues(realOutputsIntervalTrain);
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
