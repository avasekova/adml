package models.avg;

import java.util.ArrayList;
import java.util.List;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;

public class AverageMDE extends Average {

    //TODO!
    public AverageMDE(boolean avgCTSperM, boolean avgCTS, boolean avgIntTSperM, boolean avgIntTS) {
        super(avgCTSperM, avgCTS, avgIntTSperM, avgIntTS);
    }

    @Override
    public List<TrainAndTestReportCrisp> computeAllCTSAvgs(List<TrainAndTestReportCrisp> reportsCTS) {
        return new ArrayList<>();
    }

    @Override
    public List<TrainAndTestReportInterval> computeAllIntTSAvgs(List<TrainAndTestReportInterval> reportsIntTS) {
        return new ArrayList<>();
    }
}
