package params;

public class KNNcustomParams extends Params {
    
    private int numNeighbours;
    private int lengthHistory;
    private int lag;
    private String distanceMethodName;
    private String combinationMethodName;

    public int getNumNeighbours() {
        return numNeighbours;
    }

    public void setNumNeighbours(int numNeighbours) {
        this.numNeighbours = numNeighbours;
    }

    public int getLengthHistory() {
        return lengthHistory;
    }

    public void setLengthHistory(int lengthHistory) {
        this.lengthHistory = lengthHistory;
    }

    public int getLag() {
        return lag;
    }

    public void setLag(int lag) {
        this.lag = lag;
    }

    public String getDistanceMethodName() {
        return distanceMethodName;
    }

    public void setDistanceMethodName(String distanceMethodName) {
        this.distanceMethodName = distanceMethodName;
    }

    public String getCombinationMethodName() {
        return combinationMethodName;
    }

    public void setCombinationMethodName(String combinationMethodName) {
        this.combinationMethodName = combinationMethodName;
    }
    
    @Override
    public KNNcustomParams getClone() {
        KNNcustomParams param = new KNNcustomParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setCombinationMethodName(combinationMethodName);
        param.setDistanceMethodName(distanceMethodName);
        param.setLag(lag);
        param.setLengthHistory(lengthHistory);
        param.setNumForecasts(this.getNumForecasts());
        param.setNumNeighbours(numNeighbours);
        param.setPercentTrain(this.getPercentTrain());
        
        return param;
    }
}
