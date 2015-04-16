package models.params;

public class KNNkknnParams extends Params {
    
    private String colName;
    
    private int maxNeighbours;
    private long bestNumNeighbours;
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

    public long getBestNumNeighbours() {
        return bestNumNeighbours;
    }

    public void setBestNumNeighbours(long bestNumNeighbours) {
        this.bestNumNeighbours = bestNumNeighbours;
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
        param.setSeasonality(this.getSeasonality());
        param.setLag(lag);
        param.setMaxNeighbours(maxNeighbours);
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setColName(colName);
        
        return param;
    }

    @Override
    public String toString() {
        return "max k=" + maxNeighbours + ",\n"
                + "best k=" + bestNumNeighbours + ",\n"
                + "lag=" + lag;
    }
}
