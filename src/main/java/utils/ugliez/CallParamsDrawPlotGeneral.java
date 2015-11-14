package utils.ugliez;

import gui.DefaultPlottable;
import java.util.List;
import javax.swing.JList;
import org.rosuda.javaGD.JGDBufferedPanel;

public class CallParamsDrawPlotGeneral extends CallParams {
    
    private List<DefaultPlottable> plottables;
    private String plotFunction;
    private String additionalArgs;

    public CallParamsDrawPlotGeneral(JList listPlotLegend, JGDBufferedPanel canvasToUse, int width, int height, 
            List<DefaultPlottable> plottables, String plotFunction, String additionalArgs) {
        super(listPlotLegend, canvasToUse, width, height);
        this.plottables = plottables;
        this.plotFunction = plotFunction;
        this.additionalArgs = additionalArgs;
    }
    
    public List<DefaultPlottable> getColnames() {
        return plottables;
    }

    public void setColnames(List<DefaultPlottable> plottables) {
        this.plottables = plottables;
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
