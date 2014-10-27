package utils.ugliez;

import java.util.List;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import org.rosuda.javaGD.GDCanvas;

public class CallParamsDrawPlots extends CallParams {
    private GDCanvas canvasToUse;
    private int width;
    private int height;
    private List<Double> allDataCTS; //vsetky data, naozaj vsetky, neosekane podla fromTo!
    private int sizeDataWithoutFromToCrop;
    private int numForecasts;
    private List<TrainAndTestReportCrisp> reportsCTS; //obsahuje ako realne data uz odseknute data podla fromTo
    private List<TrainAndTestReportInterval> reportsITS;
    private int from;
    private int to;
    private String colname_CTS;

    public CallParamsDrawPlots(GDCanvas canvasToUse, int width, int height, List<Double> allDataCTS, int sizeDataWithoutFromToCrop, int numForecasts, List<TrainAndTestReportCrisp> reportsCTS, List<TrainAndTestReportInterval> reportsITS, int from, int to, String colname_CTS) {
        this.canvasToUse = canvasToUse;
        this.width = width;
        this.height = height;
        this.allDataCTS = allDataCTS;
        this.sizeDataWithoutFromToCrop = sizeDataWithoutFromToCrop;
        this.numForecasts = numForecasts;
        this.reportsCTS = reportsCTS;
        this.reportsITS = reportsITS;
        this.from = from;
        this.to = to;
        this.colname_CTS = colname_CTS;
    }

    public GDCanvas getCanvasToUse() {
        return canvasToUse;
    }

    public void setCanvasToUse(GDCanvas canvasToUse) {
        this.canvasToUse = canvasToUse;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Double> getAllDataCTS() {
        return allDataCTS;
    }

    public void setAllDataCTS(List<Double> allDataCTS) {
        this.allDataCTS = allDataCTS;
    }

    public int getSizeDataWithoutFromToCrop() {
        return sizeDataWithoutFromToCrop;
    }

    public void setSizeDataWithoutFromToCrop(int sizeDataWithoutFromToCrop) {
        this.sizeDataWithoutFromToCrop = sizeDataWithoutFromToCrop;
    }

    public int getNumForecasts() {
        return numForecasts;
    }

    public void setNumForecasts(int numForecasts) {
        this.numForecasts = numForecasts;
    }

    public List<TrainAndTestReportCrisp> getReportsCTS() {
        return reportsCTS;
    }

    public void setReportsCTS(List<TrainAndTestReportCrisp> reportsCTS) {
        this.reportsCTS = reportsCTS;
    }

    public List<TrainAndTestReportInterval> getReportsITS() {
        return reportsITS;
    }

    public void setReportsITS(List<TrainAndTestReportInterval> reportsITS) {
        this.reportsITS = reportsITS;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getColname_CTS() {
        return colname_CTS;
    }

    public void setColname_CTS(String colname_CTS) {
        this.colname_CTS = colname_CTS;
    }
}
