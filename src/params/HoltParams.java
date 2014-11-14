package params;

public class HoltParams extends SESParams {
    
    private String beta;

    public String getBeta() {
        return beta;
    }

    public void setBeta(String beta) {
        this.beta = beta;
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
        
        return param;
    }

    @Override
    public String toString() {
        return "HoltParams{" + "alpha=" + getAlpha() + ", beta=" + beta + "}\n";
    }
}
