package params;

public class Params {
    
    private int percentTrain;
    
    public Params() { }
    
    public Params(int percentTrain) {
        this.percentTrain = percentTrain;
    }

    public int getPercentTrain() {
        return percentTrain;
    }

    public void setPercentTrain(int percentTrain) {
        this.percentTrain = percentTrain;
    }
}
