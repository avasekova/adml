package models;

import java.util.List;
import params.RandomWalkIntervalParams;
import utils.ErrorMeasuresInterval;
import utils.ErrorMeasuresUtils;
import utils.imlp.Interval;

public class RandomWalkInterval {

    public TrainAndTestReport forecast(List<Interval> allData, RandomWalkIntervalParams params) {
        TrainAndTestReportInterval report = new TrainAndTestReportInterval("random walk (i)");
        
        ErrorMeasuresInterval errRandomWalk = ErrorMeasuresUtils
                .computeErrorMeasuresIntervalRandomWalk(allData, params.getPercentTrain(), params.getDistance());
        report.setErrorMeasures(errRandomWalk);
        
        return report;
    }
}
