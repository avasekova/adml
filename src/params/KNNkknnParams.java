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
}
