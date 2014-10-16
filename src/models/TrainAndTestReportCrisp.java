package models;

public class TrainAndTestReportCrisp extends TrainAndTestReport {

    private double[] fittedValues;
    private double[] forecastValuesTest = new double[] {};
    private String plotCode = ""; //TODO toto zmazat a kreslit to rucne, ale
    
    public TrainAndTestReportCrisp(String modelName) {
        super(modelName);
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
    
    public String getPlotCode() {
        return plotCode;
    }

    public void setPlotCode(String forecastPlot) {
        this.plotCode = forecastPlot;
    }
}
