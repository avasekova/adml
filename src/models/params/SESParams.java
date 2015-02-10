package models.params;

public class SESParams extends Params {
    
    private String colName;
    private String alpha;

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
    
    @Override
    public SESParams getClone() {
        SESParams param = new SESParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setSeasonality(this.getSeasonality());
        param.setColName(colName);
        param.setAlpha(alpha);
        
        return param;
    }

    @Override
    public String toString() {
        return "SESParams{" + "colName=" + colName + ", alpha=" + alpha + "}\n";
    }    
}
