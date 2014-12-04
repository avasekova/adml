package models.avg;

import java.util.List;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;

public class AverageMDE extends Average {
    
    private final double denominatorCTStrain;
    private final double denominatorCTStest;
    private final double denominatorIntTStrain;
    private final double denominatorIntTStest;

    public AverageMDE(boolean avgCTSperM, boolean avgCTS, boolean avgIntTSperM, boolean avgIntTS,
            List<TrainAndTestReportCrisp> reportsCTS, List<TrainAndTestReportInterval> reportsIntTS) {
        super(avgCTSperM, avgCTS, avgIntTSperM, avgIntTS);
        
        double sumErrorTrain = 0;
        double sumErrorTest = 0;
        for (TrainAndTestReportCrisp r : reportsCTS) {
            sumErrorTrain += Math.pow(r.getErrorMeasures().getMAEtrain(), -1);
            sumErrorTest  += Math.pow(r.getErrorMeasures().getMAEtest(), -1);
        }
        denominatorCTStrain = sumErrorTrain;
        denominatorCTStest = sumErrorTest;
        
        double meanErrorTrainInt = 0;
        double meanErrorTestInt = 0;
        for (TrainAndTestReportInterval r : reportsIntTS) {
            meanErrorTrainInt += Math.pow(r.getErrorMeasures().getMEtrain(), -1);
            meanErrorTestInt  += Math.pow(r.getErrorMeasures().getMEtest(), -1);
        }
        denominatorIntTStrain = meanErrorTrainInt;
        denominatorIntTStest = meanErrorTestInt;
    }

    @Override
    public double getWeightForModelTrain(TrainAndTestReportCrisp report) {
        return Math.pow(report.getErrorMeasures().getMEtrain(), -1)/denominatorCTStrain;
    }
    
    @Override
    public double getWeightForModelTest(TrainAndTestReportCrisp report) {
        return Math.pow(report.getErrorMeasures().getMEtest(), -1)/denominatorCTStest;
    }
    
    @Override
    public double getWeightForModelTrain(TrainAndTestReportInterval report) {
        return Math.pow(report.getErrorMeasures().getMEtrain(), -1)/denominatorIntTStrain;
    }
    
    @Override
    public double getWeightForModelTest(TrainAndTestReportInterval report) {
        return Math.pow(report.getErrorMeasures().getMEtest(), -1)/denominatorIntTStest;
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
