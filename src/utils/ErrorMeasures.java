package utils;

import static utils.ErrorMeasuresInterval.numberOfSupportedMeasures;

public abstract class ErrorMeasures {
    
    private static final String[] NAMES = new String[]{ "ME", "RMSE", "MAE", "MSE" };
    
    private double MEtrain;
    private double MEtest;
    private double RMSEtrain;
    private double RMSEtest;
    private double MAEtrain;
    private double MAEtest;
    private double MSEtrain;
    private double MSEtest;
    
    public static int numberOfSupportedMeasures() {
        return NAMES.length;
    }
    
    public static String[] namesOfSupportedMeasures() {
        return NAMES;
    }
    
    public double getMEtrain() {
        return MEtrain;
    }

    public void setMEtrain(double MEtrain) {
        this.MEtrain = MEtrain;
    }

    public double getMEtest() {
        return MEtest;
    }

    public void setMEtest(double MEtest) {
        this.MEtest = MEtest;
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

    public double getMAEtrain() {
        return MAEtrain;
    }

    public void setMAEtrain(double MAEtrain) {
        this.MAEtrain = MAEtrain;
    }

    public double getMAEtest() {
        return MAEtest;
    }

    public void setMAEtest(double MAEtest) {
        this.MAEtest = MAEtest;
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
    
    public double[] serializeToArray() {
        double[] measures = new double[numberOfSupportedMeasures()*2];
        measures[0] = getMEtrain();
        measures[1] = getMEtest();
        measures[2] = getRMSEtrain();
        measures[3] = getRMSEtest();
        measures[4] = getMAEtrain();
        measures[5] = getMAEtest();
        measures[6] = getMSEtrain();
        measures[7] = getMSEtest();
        
        return measures;
    }
}
