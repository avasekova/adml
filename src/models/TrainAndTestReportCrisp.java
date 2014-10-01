package models;

public class TrainAndTestReportCrisp extends TrainAndTestReport {

    private double[] fittedValues;
    private double[] forecastValues = new double[] {};
    private String fittedValuesPlotCode = ""; //TODO toto zmazat a kreslit to rucne, ale
    
    public TrainAndTestReportCrisp(String modelName) {
        super(modelName);
    }
    
    public double[] getFittedValues() {
        return fittedValues;
    }

    public void setFittedValues(double[] fittedValues) {
        this.fittedValues = fittedValues;
    }

    public double[] getForecastValues() {
        return forecastValues;
    }

    public void setForecastValues(double[] forecastValues) {
        this.forecastValues = forecastValues;
    }
    
    public String getFittedValuesPlotCode() {
        return fittedValuesPlotCode;
    }

    public void setFittedValuesPlotCode(String forecastPlot) {
        this.fittedValuesPlotCode = forecastPlot;
    }
}
