package params;

public class Params {
    
    private int percentTrain;
    private int numForecasts;
    
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

    public int getNumForecasts() {
        return numForecasts;
    }

    public void setNumForecasts(int numForecasts) {
        this.numForecasts = numForecasts;
    }
}
