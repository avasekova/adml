package params;

public class ArimaParams extends Params {
    
    private int nonSeasPotato;
    private int nonSeasDonkey;
    private int nonSeasQuark;
    private int seasPotato;
    private int seasDonkey;
    private int seasQuark;
    private boolean optimize;
    private boolean withConstant;

    public int getNonSeasPotato() {
        return nonSeasPotato;
    }

    public void setNonSeasPotato(Integer nonSeasPotato) {
        this.nonSeasPotato = nonSeasPotato;
    }

    public int getNonSeasDonkey() {
        return nonSeasDonkey;
    }

    public void setNonSeasDonkey(Integer nonSeasDonkey) {
        this.nonSeasDonkey = nonSeasDonkey;
    }

    public int getNonSeasQuark() {
        return nonSeasQuark;
    }

    public void setNonSeasQuark(Integer nonSeasQuark) {
        this.nonSeasQuark = nonSeasQuark;
    }

    public int getSeasPotato() {
        return seasPotato;
    }

    public void setSeasPotato(Integer seasPotato) {
        this.seasPotato = seasPotato;
    }

    public int getSeasDonkey() {
        return seasDonkey;
    }

    public void setSeasDonkey(Integer seasDonkey) {
        this.seasDonkey = seasDonkey;
    }

    public int getSeasQuark() {
        return seasQuark;
    }

    public void setSeasQuark(Integer seasQuark) {
        this.seasQuark = seasQuark;
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
        param.setNonSeasDonkey(nonSeasDonkey);
        param.setNonSeasPotato(nonSeasPotato);
        param.setNonSeasQuark(nonSeasQuark);
        param.setNumForecasts(this.getNumForecasts());
        param.setOptimize(optimize);
        param.setPercentTrain(this.getPercentTrain());
        param.setSeasDonkey(seasDonkey);
        param.setSeasPotato(seasPotato);
        param.setSeasQuark(seasQuark);
        param.setWithConstant(withConstant);
        
        return param;
    }

    @Override
    public String toString() {
        return "ArimaParams{" + "nonSeasP=" + nonSeasPotato + ", nonSeasD=" + nonSeasDonkey + ", nonSeasQ=" + nonSeasQuark + ", seasP=" + seasPotato + ", seasD=" + seasDonkey + ", seasQ=" + seasQuark + ", optimize=" + optimize + ", withConstant=" + withConstant + "}\n";
    }
}
