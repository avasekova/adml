package utils;

import utils.imlp.dist.Distance;

public class ErrorMeasuresInterval extends ErrorMeasures {
    
    private static final String[] NAMES = new String[]{ "MDE", "RMSE", "MSE", "RMSSE_1", "RMSSE_2", "Mean coverage", "Mean efficiency", "Theil's U(i)", "ARV(i)" };
    
    private Distance distanceUsed;
    
    private double MDEtrain;
    private double MDEtest;
    private double RMSEtrain;
    private double RMSEtest;
    private double MSEtrain;
    private double MSEtest;
    
    private double meanCoverageTrain;
    private double meanCoverageTest;
    private double meanEfficiencyTrain;
    private double meanEfficiencyTest;
    private double theilsUintervalTrain;
    private double theilsUintervalTest;
    private double arvIntervalTrain;
    private double arvIntervalTest;
    private double RMSSEtrain_1; //LB or C
    private double RMSSEtest_1;  //UB or R
    private double RMSSEtrain_2; //LB or C
    private double RMSSEtest_2;  //UB or R
    
    public ErrorMeasuresInterval(Distance distance) {
        this.distanceUsed = distance;
    }

    public static int numberOfSupportedMeasures() {
        return NAMES.length;
    }
    
    public static String[] namesOfSupportedMeasures() {
        return NAMES;
    }

    public Distance getDistanceUsed() {
        return distanceUsed;
    }

    public void setDistanceUsed(Distance distanceUsed) {
        this.distanceUsed = distanceUsed;
    }

    public double getMDEtrain() {
        return MDEtrain;
    }

    public void setMDEtrain(double MDEtrain) {
        this.MDEtrain = MDEtrain;
    }

    public double getMDEtest() {
        return MDEtest;
    }

    public void setMDEtest(double MDEtest) {
        this.MDEtest = MDEtest;
    }

    public double getRMSEtrain() {
        return RMSEtrain;
    }

    public void setRMSEtrain(double RMSEtrain) {
        this.RMSEtrain = RMSEtrain;
    }

    public double getRMSEtest() {
        return RMSEtest;
    }

    public void setRMSEtest(double RMSEtest) {
        this.RMSEtest = RMSEtest;
    }

    public double getMSEtrain() {
        return MSEtrain;
    }

    public void setMSEtrain(double MSEtrain) {
        this.MSEtrain = MSEtrain;
    }

    public double getMSEtest() {
        return MSEtest;
    }

    public void setMSEtest(double MSEtest) {
        this.MSEtest = MSEtest;
    }
    
    public double getMeanCoverageTrain() {
        return meanCoverageTrain;
    }

    public void setMeanCoverageTrain(double meanCoverageTrain) {
        this.meanCoverageTrain = meanCoverageTrain;
    }

    public double getMeanCoverageTest() {
        return meanCoverageTest;
    }

    public void setMeanCoverageTest(double meanCoverageTest) {
        this.meanCoverageTest = meanCoverageTest;
    }

    public double getMeanEfficiencyTrain() {
        return meanEfficiencyTrain;
    }

    public void setMeanEfficiencyTrain(double meanEfficiencyTrain) {
        this.meanEfficiencyTrain = meanEfficiencyTrain;
    }

    public double getMeanEfficiencyTest() {
        return meanEfficiencyTest;
    }

    public void setMeanEfficiencyTest(double meanEfficiencyTest) {
        this.meanEfficiencyTest = meanEfficiencyTest;
    }
    
    public double getTheilsUintervalTrain() {
        return theilsUintervalTrain;
    }

    public void setTheilsUintervalTrain(double theilsUintervalTrain) {
        this.theilsUintervalTrain = theilsUintervalTrain;
    }

    public double getTheilsUintervalTest() {
        return theilsUintervalTest;
    }

    public void setTheilsUintervalTest(double theilsUintervalTest) {
        this.theilsUintervalTest = theilsUintervalTest;
    }

    public double getArvIntervalTrain() {
        return arvIntervalTrain;
    }

    public void setArvIntervalTrain(double arvIntervalTrain) {
        this.arvIntervalTrain = arvIntervalTrain;
    }

    public double getArvIntervalTest() {
        return arvIntervalTest;
    }

    public void setArvIntervalTest(double arvIntervalTest) {
        this.arvIntervalTest = arvIntervalTest;
    }

    public double getRMSSEtrain_1() {
        return RMSSEtrain_1;
    }

    public void setRMSSEtrain_1(double RMSSEtrain_1) {
        this.RMSSEtrain_1 = RMSSEtrain_1;
    }

    public double getRMSSEtest_1() {
        return RMSSEtest_1;
    }

    public void setRMSSEtest_1(double RMSSEtest_1) {
        this.RMSSEtest_1 = RMSSEtest_1;
    }

    public double getRMSSEtrain_2() {
        return RMSSEtrain_2;
    }

    public void setRMSSEtrain_2(double RMSSEtrain_2) {
        this.RMSSEtrain_2 = RMSSEtrain_2;
    }

    public double getRMSSEtest_2() {
        return RMSSEtest_2;
    }

    public void setRMSSEtest_2(double RMSSEtest_2) {
        this.RMSSEtest_2 = RMSSEtest_2;
    }
    
    @Override
    public double[] serializeToArray() {
        double[] measures = new double[NAMES.length*2];
        
        measures[0] = getMDEtrain();
        measures[1] = getMDEtest();
        measures[2] = getRMSEtrain();
        measures[3] = getRMSEtest();
        measures[4] = getMSEtrain();
        measures[5] = getMSEtest();
        
        int i = 6;
        
        measures[i] = RMSSEtrain_1; i++;
        measures[i] = RMSSEtest_1; i++;
        measures[i] = RMSSEtrain_2; i++;
        measures[i] = RMSSEtest_2; i++;
        measures[i] = meanCoverageTrain; i++;
        measures[i] = meanCoverageTest; i++;
        measures[i] = meanEfficiencyTrain; i++;
        measures[i] = meanEfficiencyTest; i++;
        measures[i] = theilsUintervalTrain; i++;
        measures[i] = theilsUintervalTest; i++;
        measures[i] = arvIntervalTrain; i++;
        measures[i] = arvIntervalTest;
        
        return measures;
    }
    
}
