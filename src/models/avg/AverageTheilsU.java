package models.avg;

import java.util.List;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import utils.ErrorMeasuresCrisp;
import utils.ErrorMeasuresInterval;

public class AverageTheilsU extends Average {
    
    private final double denominatorCTStrain;
    private final double denominatorCTStest;
    private final double denominatorIntTStrain;
    private final double denominatorIntTStest;

    public AverageTheilsU(boolean avgCTSperM, boolean avgCTS, boolean avgIntTSperM, boolean avgIntTS,
            List<TrainAndTestReportCrisp> reportsCTS, List<TrainAndTestReportInterval> reportsIntTS) {
        super(avgCTSperM, avgCTS, avgIntTSperM, avgIntTS);
        
        double sumErrorTrain = 0;
        double sumErrorTest = 0;
        for (TrainAndTestReportCrisp r : reportsCTS) {
            sumErrorTrain += Math.pow(((ErrorMeasuresCrisp)r.getErrorMeasures()).getTheilUtrain(), -1);
            sumErrorTest  += Math.pow(((ErrorMeasuresCrisp)r.getErrorMeasures()).getTheilUtest(), -1);
        }
        denominatorCTStrain = sumErrorTrain;
        denominatorCTStest = sumErrorTest;
        
        double sumErrorTrainInt = 0;
        double sumErrorTestInt = 0;
        for (TrainAndTestReportInterval r : reportsIntTS) {
            sumErrorTrainInt += Math.pow(((ErrorMeasuresInterval)r.getErrorMeasures()).getTheilsUintervalTrain(), -1);
            sumErrorTestInt  += Math.pow(((ErrorMeasuresInterval)r.getErrorMeasures()).getTheilsUintervalTest(), -1);
        }
        denominatorIntTStrain = sumErrorTrainInt;
        denominatorIntTStest = sumErrorTestInt;
    }

    @Override
    public double getWeightForModelTrain(TrainAndTestReportCrisp report) {
        return Math.pow(((ErrorMeasuresCrisp)report.getErrorMeasures()).getTheilUtrain(), -1)/denominatorCTStrain;
    }
    
    @Override
    public double getWeightForModelTest(TrainAndTestReportCrisp report) {
        return Math.pow(((ErrorMeasuresCrisp)report.getErrorMeasures()).getTheilUtest(), -1)/denominatorCTStest;
    }
    
    @Override
    public double getWeightForModelTrain(TrainAndTestReportInterval report) {
        return Math.pow(((ErrorMeasuresInterval)report.getErrorMeasures()).getTheilsUintervalTrain(), -1)/denominatorIntTStrain;
    }
    
    @Override
    public double getWeightForModelTest(TrainAndTestReportInterval report) {
        return Math.pow(((ErrorMeasuresInterval)report.getErrorMeasures()).getTheilsUintervalTest(), -1)/denominatorIntTStest;
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
