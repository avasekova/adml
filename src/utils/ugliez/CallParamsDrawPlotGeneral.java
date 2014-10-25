package utils.ugliez;

import java.util.List;
import org.rosuda.javaGD.GDCanvas;

public class CallParamsDrawPlotGeneral extends CallParams {
    
    private GDCanvas canvasToUse;
    private int width;
    private int height;
    private List<String> colnames;
    private String plotFunction;
    private String additionalArgs;

    public CallParamsDrawPlotGeneral(GDCanvas canvasToUse, int width, int height, List<String> colnames, String plotFunction, String additionalArgs) {
        this.canvasToUse = canvasToUse;
        this.width = width;
        this.height = height;
        this.colnames = colnames;
        this.plotFunction = plotFunction;
        this.additionalArgs = additionalArgs;
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

    public List<String> getColnames() {
        return colnames;
    }

    public void setColnames(List<String> colnames) {
        this.colnames = colnames;
    }

    public String getPlotFunction() {
        return plotFunction;
    }

    public void setPlotFunction(String plotFunction) {
        this.plotFunction = plotFunction;
    }

    public String getAdditionalArgs() {
        return additionalArgs;
    }

    public void setAdditionalArgs(String additionalArgs) {
        this.additionalArgs = additionalArgs;
    }
}
