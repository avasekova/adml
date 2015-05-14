package models.params;

public class MAvgParams extends Params {
    
    private String colName;
    private Integer order;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
    
    @Override
    public MAvgParams getClone() {
        MAvgParams param = new MAvgParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setSeasonality(this.getSeasonality());
        param.setColName(colName);
        param.setOrder(order);
        
        return param;
    }

    @Override
    public String toString() {
        return "time series = " + colName + "\n" + 
               "order = " + order;
    }    
}
