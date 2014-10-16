package models;

public class TrainAndTestReportCrisp extends TrainAndTestReport {

    private double[] fittedValues;
    private double[] forecastValues = new double[] {};
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

    public double[] getForecastValues() {
        return forecastValues;
    }

    public void setForecastValues(double[] forecastValues) {
        this.forecastValues = forecastValues;
    }
    
    public String getPlotCode() {
        return plotCode;
    }

    public void setPlotCode(String forecastPlot) {
        this.plotCode = forecastPlot;
    }
}
