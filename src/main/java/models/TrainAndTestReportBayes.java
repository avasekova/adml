package models;

public class TrainAndTestReportBayes extends TrainAndTestReport {
    
    
    
    public TrainAndTestReportBayes(Model modelName) {
        this(modelName, false);
    }

    public TrainAndTestReportBayes(Model modelName, boolean average) {
        super(modelName, average);
    }
    
}
