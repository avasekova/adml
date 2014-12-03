package models.avg;

import java.util.ArrayList;
import java.util.List;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;

public abstract class Average {
    
    private boolean avgCTSperM;
    private boolean avgCTS;
    private boolean avgIntTSperM;
    private boolean avgIntTS;
    private List<Double> weights = new ArrayList<>();

    public Average(boolean avgCTSperM, boolean avgCTS, boolean avgIntTSperM, boolean avgIntTS) {
        this.avgCTSperM = avgCTSperM;
        this.avgCTS = avgCTS;
        this.avgIntTSperM = avgIntTSperM;
        this.avgIntTS = avgIntTS;
    }
    
    public Average(boolean avgCTSperM, boolean avgCTS, boolean avgIntTSperM, boolean avgIntTS, List<Double> weights) {
        this.avgCTSperM = avgCTSperM;
        this.avgCTS = avgCTS;
        this.avgIntTSperM = avgIntTSperM;
        this.avgIntTS = avgIntTS;
        this.weights = weights;
    }

    public boolean isAvgCTSperM() {
        return avgCTSperM;
    }

    public void setAvgCTSperM(boolean avgCTSperM) {
        this.avgCTSperM = avgCTSperM;
    }

    public boolean isAvgCTS() {
        return avgCTS;
    }

    public void setAvgCTS(boolean avgCTS) {
        this.avgCTS = avgCTS;
    }

    public boolean isAvgIntTSperM() {
        return avgIntTSperM;
    }

    public void setAvgIntTSperM(boolean avgIntTSperM) {
        this.avgIntTSperM = avgIntTSperM;
    }

    public boolean isAvgIntTS() {
        return avgIntTS;
    }

    public void setAvgIntTS(boolean avgIntTS) {
        this.avgIntTS = avgIntTS;
    }

    public List<Double> getWeights() {
        return weights;
    }

    public void setWeights(List<Double> weights) {
        this.weights = weights;
    }
    
    public abstract List<TrainAndTestReportCrisp> computeAllCTSAvgs(List<TrainAndTestReportCrisp> reportsCTS) throws IllegalArgumentException;
    public abstract List<TrainAndTestReportInterval> computeAllIntTSAvgs(List<TrainAndTestReportInterval> reportsIntTS) throws IllegalArgumentException;
//    public abstract Map<String,List<TrainAndTestReportCrisp>> computeAvgCTSperM(List<TrainAndTestReportCrisp> reports);
//    public abstract TrainAndTestReportCrisp computeAvgCTS(List<TrainAndTestReportCrisp> reports);
//    public abstract Map<String,List<TrainAndTestReportInterval>> computeAvgIntTSperM(List<TrainAndTestReportInterval> reports);
//    public abstract TrainAndTestReportInterval computeAvgIntTS(List<TrainAndTestReportInterval> reports);
}
