package params;

public class HoltWintersParams extends HoltParams {
    
    private String gamma;

    public String getGamma() {
        return gamma;
    }

    public void setGamma(String gamma) {
        this.gamma = gamma;
    }
    
    @Override
    public HoltWintersParams getClone() {
        HoltWintersParams param = new HoltWintersParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setSeasonality(this.getSeasonality());
        param.setColName(this.getColName());
        param.setAlpha(this.getAlpha());
        param.setBeta(this.getBeta());
        param.setDamped(this.getDamped());
        param.setGamma(gamma);
        
        return param;
    }

    @Override
    public String toString() {
        return "HoltWintersParams{" + "alpha=" + getAlpha() + ", beta=" + getBeta() + ", gamma=" + getGamma() + "}\n";
    }
}
