package gui;

public class AveragesConfig {
    
    //simple average
    private boolean avgSimpleCTSperM;
    private boolean avgSimpleCTS;
    private boolean avgSimpleIntTSperM;
    private boolean avgSimpleIntTS;
    
    //average with weights inversely propotional to MDE
    private boolean avgMDeCTSperM;
    private boolean avgMDeCTS;
    private boolean avgMDeIntTSperM;
    private boolean avgMDeIntTS;
    
    private boolean avgONLY;

    public AveragesConfig(boolean avgSimpleCTSperM, boolean avgSimpleCTS, boolean avgSimpleIntTSperM, boolean avgSimpleIntTS,
            boolean avgMDeCTSperM, boolean avgMDeCTS, boolean avgMDeIntTSperM, boolean avgMDeIntTS, boolean avgONLY) {
        this.avgSimpleCTSperM = avgSimpleCTSperM;
        this.avgSimpleCTS = avgSimpleCTS;
        this.avgSimpleIntTSperM = avgSimpleIntTSperM;
        this.avgSimpleIntTS = avgSimpleIntTS;
        this.avgMDeCTSperM = avgMDeCTSperM;
        this.avgMDeCTS = avgMDeCTS;
        this.avgMDeIntTSperM = avgMDeIntTSperM;
        this.avgMDeIntTS = avgMDeIntTS;
        this.avgONLY = avgONLY;
    }
    
    
    public boolean isAvgSimpleCTSperM() {
        return avgSimpleCTSperM;
    }

    public void setAvgSimpleCTSperM(boolean avgSimpleCTSperM) {
        this.avgSimpleCTSperM = avgSimpleCTSperM;
    }

    public boolean isAvgSimpleCTS() {
        return avgSimpleCTS;
    }

    public void setAvgSimpleCTS(boolean avgSimpleCTS) {
        this.avgSimpleCTS = avgSimpleCTS;
    }

    public boolean isAvgSimpleIntTSperM() {
        return avgSimpleIntTSperM;
    }

    public void setAvgSimpleIntTSperM(boolean avgSimpleIntTSperM) {
        this.avgSimpleIntTSperM = avgSimpleIntTSperM;
    }

    public boolean isAvgSimpleIntTS() {
        return avgSimpleIntTS;
    }

    public void setAvgSimpleIntTS(boolean avgSimpleIntTS) {
        this.avgSimpleIntTS = avgSimpleIntTS;
    }

    public boolean isAvgMDeCTSperM() {
        return avgMDeCTSperM;
    }

    public void setAvgMDeCTSperM(boolean avgMDeCTSperM) {
        this.avgMDeCTSperM = avgMDeCTSperM;
    }

    public boolean isAvgMDeCTS() {
        return avgMDeCTS;
    }

    public void setAvgMDeCTS(boolean avgMDeCTS) {
        this.avgMDeCTS = avgMDeCTS;
    }

    public boolean isAvgMDeIntTSperM() {
        return avgMDeIntTSperM;
    }

    public void setAvgMDeIntTSperM(boolean avgMDeIntTSperM) {
        this.avgMDeIntTSperM = avgMDeIntTSperM;
    }

    public boolean isAvgMDeIntTS() {
        return avgMDeIntTS;
    }

    public void setAvgMDeIntTS(boolean avgMDeIntTS) {
        this.avgMDeIntTS = avgMDeIntTS;
    }

    public boolean isAvgONLY() {
        return avgONLY;
    }

    public void setAvgONLY(boolean avgONLY) {
        this.avgONLY = avgONLY;
    }
}
