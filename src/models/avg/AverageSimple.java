package models.avg;

import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;

public class AverageSimple extends Average {

    public AverageSimple(boolean avgCTSperM, boolean avgCTS, boolean avgIntTSperM, boolean avgIntTS) {
        super(avgCTSperM, avgCTS, avgIntTSperM, avgIntTS);
    }
    
    @Override
    public String getName() {
        return "avg";
    }

    @Override
    public double getWeightForModelTrain(TrainAndTestReportCrisp report) {
        return 1;
    }

    @Override
    public double getWeightForModelTest(TrainAndTestReportCrisp report) {
        return 1;
    }

    @Override
    public double getWeightForModelFuture(TrainAndTestReportCrisp report) {
        return 1;
    }

    @Override
    public double getWeightForModelTrain(TrainAndTestReportInterval report) {
        return 1;
    }

    @Override
    public double getWeightForModelTest(TrainAndTestReportInterval report) {
        return 1;
    }

    @Override
    public double getWeightForModelFuture(TrainAndTestReportInterval report) {
        return 1;
    }
}
