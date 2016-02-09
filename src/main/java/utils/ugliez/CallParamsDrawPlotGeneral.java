package utils.ugliez;

import gui.DefaultPlottable;

import javax.swing.*;
import java.util.List;

public class CallParamsDrawPlotGeneral extends CallParams {
    
    private List<DefaultPlottable> plottables;
    private String plotFunction;
    private String additionalArgs;

    public CallParamsDrawPlotGeneral(JList listPlotLegend, int width, int height, 
            List<DefaultPlottable> plottables, String plotFunction, String additionalArgs) {
        super(listPlotLegend, width, height);
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
