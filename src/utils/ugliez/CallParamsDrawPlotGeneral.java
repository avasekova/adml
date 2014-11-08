package utils.ugliez;

import java.util.List;
import javax.swing.JList;
import org.rosuda.javaGD.JGDBufferedPanel;

public class CallParamsDrawPlotGeneral extends CallParams {
    
    private List<String> colnames;
    private String plotFunction;
    private String additionalArgs;

    public CallParamsDrawPlotGeneral(JList listPlotLegend, JGDBufferedPanel canvasToUse, int width, int height, 
            List<String> colnames, String plotFunction, String additionalArgs) {
        super(listPlotLegend, canvasToUse, width, height);
        this.colnames = colnames;
        this.plotFunction = plotFunction;
        this.additionalArgs = additionalArgs;
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
