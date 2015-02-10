package models.params;

public class HoltWintersParams extends HoltParams {
    
    private String gamma;
    private String seasonalityAddMult;
    private String frequency;

    public String getGamma() {
        return gamma;
    }

    public void setGamma(String gamma) {
        this.gamma = gamma;
    }

    public String getSeasonalityAddMult() {
        return seasonalityAddMult;
    }

    public void setSeasonalityAddMult(String seasonalityAddMult) {
        this.seasonalityAddMult = seasonalityAddMult;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
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
        param.setSeasonalityAddMult(seasonalityAddMult);
        param.setFrequency(frequency);
        
        return param;
    }

    @Override
    public String toString() {
        return "alpha = " + getAlpha() + "\n" +
               "beta = " + getBeta() + "\n" + 
               "gamma = " + getGamma();
    }
}
