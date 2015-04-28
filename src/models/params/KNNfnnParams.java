package models.params;

public class KNNfnnParams extends Params {
    
    private String colName;
    
    private int numNeighbours;

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
    
    @Override
    public KNNfnnParams getClone() {
        KNNfnnParams param = new KNNfnnParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setSeasonality(this.getSeasonality());
        param.setNumForecasts(this.getNumForecasts());
        param.setNumNeighbours(numNeighbours);
        param.setPercentTrain(this.getPercentTrain());
        param.setColName(colName);
        
        return param;
    }

    @Override
    public String toString() {
        return "numNeighbours=" + numNeighbours;
    }
}
