package models.avg;

import java.util.List;

public class AveragesConfig {
    
    private List<Average> avgs;
    private boolean avgONLY;
    
    public AveragesConfig(List<Average> avgs, boolean avgONLY) {
        this.avgs = avgs;
        this.avgONLY = avgONLY;
    }

    public List<Average> getAvgs() {
        return avgs;
    }

    public void setAvgs(List<Average> avgs) {
        this.avgs = avgs;
    }

    public boolean isAvgONLY() {
        return avgONLY;
    }

    public void setAvgONLY(boolean avgONLY) {
        this.avgONLY = avgONLY;
    }
}
