package models;

public class TrainAndTestReportBayes extends TrainAndTestReport {
    
    
    
    public TrainAndTestReportBayes(String modelName) {
        this(modelName, false);
    }

    public TrainAndTestReportBayes(String modelName, boolean average) {
        super(modelName, average);
    }
    
}
