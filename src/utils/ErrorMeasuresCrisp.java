package utils;

public class ErrorMeasuresCrisp extends ErrorMeasures {
    
    private static final String[] NAMES = new String[]{ "RMSSE", "MPE", "MAPE", "MASE", "Theil's U" };
    
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
        return NAMES.length + ErrorMeasures.numberOfSupportedMeasures();
    }
    
    public static String[] namesOfSupportedMeasures() {
        String[] allnames = new String[ErrorMeasures.namesOfSupportedMeasures().length + NAMES.length];
        
        System.arraycopy(ErrorMeasures.namesOfSupportedMeasures(), 0, allnames, 0, ErrorMeasures.namesOfSupportedMeasures().length);
        System.arraycopy(NAMES, 0, allnames, ErrorMeasures.namesOfSupportedMeasures().length, NAMES.length);
        
        return allnames;
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
        double[] measures = new double[numberOfSupportedMeasures()*2 + ErrorMeasures.numberOfSupportedMeasures()*2];
        
        int i;
        for (i = 0; i < ErrorMeasures.numberOfSupportedMeasures()*2; i++) {
            measures[i] = super.serializeToArray()[i];
        }
        
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
