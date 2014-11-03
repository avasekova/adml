package utils.ugliez;

import java.util.List;
import org.rosuda.javaGD.JGDBufferedPanel;

public class CallParamsDrawPlotGeneral extends CallParams {
    
    private JGDBufferedPanel canvasToUse;
    private int width;
    private int height;
    private List<String> colnames;
    private String plotFunction;
    private String additionalArgs;

    public CallParamsDrawPlotGeneral(JGDBufferedPanel canvasToUse, int width, int height, List<String> colnames, String plotFunction, String additionalArgs) {
        this.canvasToUse = canvasToUse;
        this.width = width;
        this.height = height;
        this.colnames = colnames;
        this.plotFunction = plotFunction;
        this.additionalArgs = additionalArgs;
    }
    
    public JGDBufferedPanel getGDBufferedPanel() {
        return canvasToUse;
    }

    public void setCanvasToUse(JGDBufferedPanel canvasToUse) {
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
