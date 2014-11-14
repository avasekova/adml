package params;

import utils.R_Bool;

public class HoltParams extends SESParams {
    
    private String beta;
    private R_Bool damped;

    public String getBeta() {
        return beta;
    }

    public void setBeta(String beta) {
        this.beta = beta;
    }

    public R_Bool getDamped() {
        return damped;
    }

    public void setDamped(R_Bool damped) {
        this.damped = damped;
    }
    
    @Override
    public HoltParams getClone() {
        HoltParams param = new HoltParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setSeasonality(this.getSeasonality());
        param.setColName(this.getColName());
        param.setAlpha(this.getAlpha());
        param.setBeta(beta);
        param.setDamped(damped);
        
        return param;
    }

    @Override
    public String toString() {
        return "HoltParams{" + "alpha=" + getAlpha() + ", beta=" + beta + "}\n";
    }
}
