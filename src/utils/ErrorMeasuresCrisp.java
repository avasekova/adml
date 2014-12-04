package utils;

public class ErrorMeasuresCrisp extends ErrorMeasures {
    
    private static final String[] NAMES = new String[]{ "ME", "RMSE", "MAE", "MSE", "RMSSE", "MPE", "MAPE", "MASE", "Theil's U" };
    
    private double MEtrain;
    private double MEtest;
    private double RMSEtrain;
    private double RMSEtest;
    private double MAEtrain;
    private double MAEtest;
    private double MSEtrain;
    private double MSEtest;
    
    private double MPEtrain;
    private double MPEtest;
    private double MAPEtrain;
    private double MAPEtest;
    private double MASEtrain;
    private double MASEtest;
    private double theilUtrain;
    private double theilUtest;
    private double RMSSEtrain;
    private double RMSSEtest;
    
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

    public double getMPEtrain() {
        return MPEtrain;
    }

    public void setMPEtrain(double MPEtrain) {
        this.MPEtrain = MPEtrain;
    }

    public double getMPEtest() {
        return MPEtest;
    }

    public void setMPEtest(double MPEtest) {
        this.MPEtest = MPEtest;
    }

    public double getMAPEtrain() {
        return MAPEtrain;
    }

    public void setMAPEtrain(double MAPEtrain) {
        this.MAPEtrain = MAPEtrain;
    }

    public double getMAPEtest() {
        return MAPEtest;
    }

    public void setMAPEtest(double MAPEtest) {
        this.MAPEtest = MAPEtest;
    }

    public double getMASEtrain() {
        return MASEtrain;
    }

    public void setMASEtrain(double MASEtrain) {
        this.MASEtrain = MASEtrain;
    }

    public double getMASEtest() {
        return MASEtest;
    }

    public void setMASEtest(double MASEtest) {
        this.MASEtest = MASEtest;
    }

    public double getTheilUtrain() {
        return theilUtrain;
    }

    public void setTheilUtrain(double theilUtrain) {
        this.theilUtrain = theilUtrain;
    }

    public double getTheilUtest() {
        return theilUtest;
    }

    public void setTheilUtest(double theilUtest) {
        this.theilUtest = theilUtest;
    }

    public double getRMSSEtrain() {
        return RMSSEtrain;
    }

    public void setRMSSEtrain(double RMSSEtrain) {
        this.RMSSEtrain = RMSSEtrain;
    }

    public double getRMSSEtest() {
        return RMSSEtest;
    }

    public void setRMSSEtest(double RMSSEtest) {
        this.RMSSEtest = RMSSEtest;
    }
    
    @Override
    public double[] serializeToArray() {
        double[] measures = new double[NAMES.length*2];
        
        measures[0] = getMEtrain();
        measures[1] = getMEtest();
        measures[2] = getRMSEtrain();
        measures[3] = getRMSEtest();
        measures[4] = getMAEtrain();
        measures[5] = getMAEtest();
        measures[6] = getMSEtrain();
        measures[7] = getMSEtest();
        
        int i = 8;
        
        measures[i] = RMSSEtrain; i++;
        measures[i] = RMSSEtest; i++;
        measures[i] = MPEtrain; i++;
        measures[i] = MPEtest; i++;
        measures[i] = MAPEtrain; i++;
        measures[i] = MAPEtest; i++;
        measures[i] = MASEtrain; i++;
        measures[i] = MASEtest; i++;
        measures[i] = theilUtrain; i++;
        measures[i] = theilUtest;
        
        return measures;
    }
}
