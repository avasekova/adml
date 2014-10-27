package params;

public class KNNfnnParams extends Params {
    
    private int numNeighbours;
    private int lag;

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
        
        return param;
    }
}
