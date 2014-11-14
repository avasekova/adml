package params;

public class HoltParams extends Params {
    
    private String colName;
    private String alpha;
    private String beta;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }
    
    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

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
        param.setColName(colName);
        param.setAlpha(alpha);
        param.setBeta(beta);
        
        return param;
    }

    @Override
    public String toString() {
        return "HoltParams{" + "alpha=" + alpha + ", beta=" + beta + "}\n";
    }
}
