package params;

public class KNNfnnParams extends Params {
    
    private String colName;
    
    private int numNeighbours;
    private int lag;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }
    
    public int getNumNeighbours() {
        return numNeighbours;
    }

    public void setNumNeighbours(Integer numNeighbours) {
        this.numNeighbours = numNeighbours;
    }

    public int getLag() {
        return lag;
    }

    public void setLag(Integer lag) {
        this.lag = lag;
    }
    
    @Override
    public KNNfnnParams getClone() {
        KNNfnnParams param = new KNNfnnParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setLag(lag);
        param.setNumForecasts(this.getNumForecasts());
        param.setNumNeighbours(numNeighbours);
        param.setPercentTrain(this.getPercentTrain());
        param.setColName(colName);
        
        return param;
    }
}
