package models;

public class TrainAndTestReportCrisp extends TrainAndTestReport {

    private double[] realOutputsTrain;
    private double[] realOutputsTest;
    private double[] fittedValues = new double[] {};
    private double[] forecastValuesTest = new double[] {};
    private double[] forecastValuesFuture = new double[] {};
    private String plotCode = ""; //TODO toto zmazat a kreslit to rucne, ale
    
    private double[] predictionIntervalsUppers = new double[] {};
    private double[] predictionIntervalsLowers = new double[] {};
    
    public TrainAndTestReportCrisp(String modelName) {
        this(modelName, false);
    }
    
    public TrainAndTestReportCrisp(String modelName, boolean average) {
        super(modelName, average);
    }

    public double[] getRealOutputsTrain() {
        return realOutputsTrain;
    }

    public void setRealOutputsTrain(double[] realOutputsTrain) {
        this.realOutputsTrain = realOutputsTrain;
    }

    public double[] getRealOutputsTest() {
        return realOutputsTest;
    }

    public void setRealOutputsTest(double[] realOutputsTest) {
        this.realOutputsTest = realOutputsTest;
    }
    
    public double[] getFittedValues() {
        return fittedValues;
    }

    public void setFittedValues(double[] fittedValues) {
        this.fittedValues = fittedValues;
    }

    public double[] getForecastValuesTest() {
        return forecastValuesTest;
    }

    public void setForecastValuesTest(double[] forecastValuesTest) {
        this.forecastValuesTest = forecastValuesTest;
    }

    public double[] getForecastValuesFuture() {
        return forecastValuesFuture;
    }

    public void setForecastValuesFuture(double[] forecastValuesFuture) {
        this.forecastValuesFuture = forecastValuesFuture;
    }
    
    public String getPlotCode() {
        return plotCode;
    }

    public void setPlotCode(String forecastPlot) {
        this.plotCode = forecastPlot;
    }

    public double[] getPredictionIntervalsUppers() {
        return predictionIntervalsUppers;
    }

    public void setPredictionIntervalsUppers(double[] predictionIntervalsUppers) {
        this.predictionIntervalsUppers = predictionIntervalsUppers;
    }

    public double[] getPredictionIntervalsLowers() {
        return predictionIntervalsLowers;
    }

    public void setPredictionIntervalsLowers(double[] predictionIntervalsLowers) {
        this.predictionIntervalsLowers = predictionIntervalsLowers;
    }
}
