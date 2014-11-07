package params;

public class KNNkknnParams extends Params {
    
    private String colName;
    
    private int maxNeighbours;
    private int lag;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public int getMaxNeighbours() {
        return maxNeighbours;
    }

    public void setMaxNeighbours(Integer maxNeighbours) {
        this.maxNeighbours = maxNeighbours;
    }

    public int getLag() {
        return lag;
    }

    public void setLag(Integer lag) {
        this.lag = lag;
    }
    
    @Override
    public KNNkknnParams getClone() {
        KNNkknnParams param = new KNNkknnParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setLag(lag);
        param.setMaxNeighbours(maxNeighbours);
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setColName(colName);
        
        return param;
    }
}
