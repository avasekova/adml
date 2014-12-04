package models.avg;

import java.util.List;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import utils.ErrorMeasuresInterval;

public class AverageCoverageEfficiency extends Average {
    
    private final double denominatorIntTStrain;
    private final double denominatorIntTStest;

    public AverageCoverageEfficiency(boolean avgIntTSperM, boolean avgIntTS, List<TrainAndTestReportInterval> reportsIntTS) {
        super(false, false, avgIntTSperM, avgIntTS);
        
        double meanErrorTrainInt = 0;
        double meanErrorTestInt = 0;
        for (TrainAndTestReportInterval r : reportsIntTS) {
            double coverage = ((ErrorMeasuresInterval)r.getErrorMeasures()).getMeanCoverageTrain();
            double efficiency = ((ErrorMeasuresInterval)r.getErrorMeasures()).getMeanEfficiencyTrain();
            double cvgeff = (coverage + efficiency)/2;
            meanErrorTrainInt += cvgeff;
            
            coverage = ((ErrorMeasuresInterval)r.getErrorMeasures()).getMeanCoverageTest();
            efficiency = ((ErrorMeasuresInterval)r.getErrorMeasures()).getMeanEfficiencyTest();
            cvgeff = (coverage + efficiency)/2;
            meanErrorTestInt  += cvgeff;
        }
        denominatorIntTStrain = meanErrorTrainInt;
        denominatorIntTStest = meanErrorTestInt;
    }

    @Override
    public double getWeightForModelTrain(TrainAndTestReportCrisp report) {
        return 0;
    }
    
    @Override
    public double getWeightForModelTest(TrainAndTestReportCrisp report) {
        return 0;
    }
    
    @Override
    public double getWeightForModelTrain(TrainAndTestReportInterval report) {
        double coverage = ((ErrorMeasuresInterval)report.getErrorMeasures()).getMeanCoverageTrain();
        double efficiency = ((ErrorMeasuresInterval)report.getErrorMeasures()).getMeanEfficiencyTrain();
        double cvgeff = (coverage + efficiency)/2;
        
        return cvgeff/denominatorIntTStrain;
    }
    
    @Override
    public double getWeightForModelTest(TrainAndTestReportInterval report) {
        double coverage = ((ErrorMeasuresInterval)report.getErrorMeasures()).getMeanCoverageTest();
        double efficiency = ((ErrorMeasuresInterval)report.getErrorMeasures()).getMeanEfficiencyTest();
        double cvgeff = (coverage + efficiency)/2;
        
        return cvgeff/denominatorIntTStest;
    }

    @Override
    public double getWeightForModelFuture(TrainAndTestReportCrisp report) {
        return getWeightForModelTest(report);
    }

    @Override
    public double getWeightForModelFuture(TrainAndTestReportInterval report) {
        return getWeightForModelTest(report);
    }
}
