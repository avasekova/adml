package utils;

public class ErrorMeasuresInterval extends ErrorMeasures {
    
    private static final String[] NAMES = new String[]{ "Mean coverage", "Mean efficiency", "Theil's U(i)" };
    
    private double meanCoverageTrain;
    private double meanCoverageTest;
    private double meanEfficiencyTrain;
    private double meanEfficiencyTest;
    private double theilsUintervalTrain;
    private double theilsUintervalTest;

    public static int numberOfSupportedMeasures() {
        return NAMES.length + ErrorMeasures.numberOfSupportedMeasures();
    }
    
    public static String[] namesOfSupportedMeasures() {
        String[] allnames = new String[ErrorMeasures.namesOfSupportedMeasures().length + NAMES.length];
        
        System.arraycopy(ErrorMeasures.namesOfSupportedMeasures(), 0, allnames, 0, ErrorMeasures.namesOfSupportedMeasures().length);
        System.arraycopy(NAMES, 0, allnames, ErrorMeasures.namesOfSupportedMeasures().length, NAMES.length);
        
        return allnames;
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
    
    @Override
    public double[] serializeToArray() {
        double[] measures = new double[numberOfSupportedMeasures()*2 + ErrorMeasures.numberOfSupportedMeasures()*2];
        
        int i;
        for (i = 0; i < ErrorMeasures.numberOfSupportedMeasures()*2; i++) {
            measures[i] = super.serializeToArray()[i];
        }
        
        measures[i] = meanCoverageTrain; i++;
        measures[i] = meanCoverageTest; i++;
        measures[i] = meanEfficiencyTrain; i++;
        measures[i] = meanEfficiencyTest; i++;
        measures[i] = theilsUintervalTrain; i++;
        measures[i] = theilsUintervalTest;
        
        return measures;
    }
    
}
