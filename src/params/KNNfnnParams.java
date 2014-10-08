package params;

public class KNNfnnParams extends Params {
    
    private int numNeighbours;
    private int lag;

    public int getNumNeighbours() {
        return numNeighbours;
    }

    public void setNumNeighbours(int numNeighbours) {
        this.numNeighbours = numNeighbours;
    }

    public int getLag() {
        return lag;
    }

    public void setLag(int lag) {
        this.lag = lag;
    }
}
