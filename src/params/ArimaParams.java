package params;

public class ArimaParams extends Params {
    
    private int nonSeasP;
    private int nonSeasD;
    private int nonSeasQ;
    private int seasP;
    private int seasD;
    private int seasQ;
    private boolean optimize;
    private boolean withConstant;

    public int getNonSeasP() {
        return nonSeasP;
    }

    public void setNonSeasP(Integer nonSeasP) {
        this.nonSeasP = nonSeasP;
    }

    public int getNonSeasD() {
        return nonSeasD;
    }

    public void setNonSeasD(Integer nonSeasD) {
        this.nonSeasD = nonSeasD;
    }

    public int getNonSeasQ() {
        return nonSeasQ;
    }

    public void setNonSeasQ(Integer nonSeasQ) {
        this.nonSeasQ = nonSeasQ;
    }

    public int getSeasP() {
        return seasP;
    }

    public void setSeasP(Integer seasP) {
        this.seasP = seasP;
    }

    public int getSeasD() {
        return seasD;
    }

    public void setSeasD(Integer seasD) {
        this.seasD = seasD;
    }

    public int getSeasQ() {
        return seasQ;
    }

    public void setSeasQ(Integer seasQ) {
        this.seasQ = seasQ;
    }

    public boolean isOptimize() {
        return optimize;
    }

    public void setOptimize(Boolean optimize) {
        this.optimize = optimize;
    }

    public boolean isWithConstant() {
        return withConstant;
    }

    public void setWithConstant(Boolean withConstant) {
        this.withConstant = withConstant;
    }
    
    @Override
    public ArimaParams getClone() {
        ArimaParams param = new ArimaParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNonSeasD(nonSeasD);
        param.setNonSeasP(nonSeasP);
        param.setNonSeasQ(nonSeasQ);
        param.setNumForecasts(this.getNumForecasts());
        param.setOptimize(optimize);
        param.setPercentTrain(this.getPercentTrain());
        param.setSeasD(seasD);
        param.setSeasP(seasP);
        param.setSeasQ(seasQ);
        param.setWithConstant(withConstant);
        
        return param;
    }

    @Override
    public String toString() {
        return "ArimaParams{" + "nonSeasP=" + nonSeasP + ", nonSeasD=" + nonSeasD + ", nonSeasQ=" + nonSeasQ + ", seasP=" + seasP + ", seasD=" + seasD + ", seasQ=" + seasQ + ", optimize=" + optimize + ", withConstant=" + withConstant + "}\n";
    }
}
