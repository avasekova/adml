package params;

public class KNNkknnParams extends Params {
    
    private int maxNeighbours;
    private int lag;

    public int getMaxNeighbours() {
        return maxNeighbours;
    }

    public void setMaxNeighbours(int maxNeighbours) {
        this.maxNeighbours = maxNeighbours;
    }

    public int getLag() {
        return lag;
    }

    public void setLag(int lag) {
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
        
        return param;
    }
}
