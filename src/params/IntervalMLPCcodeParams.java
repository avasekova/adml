package params;

import java.util.ArrayList;
import java.util.List;
import utils.imlp.ExplanatoryVariable;

public class IntervalMLPCcodeParams extends Params {
    
    private Integer numNodesHidden;
    private Integer numIterations;
    private List<ExplanatoryVariable> explVars = new ArrayList<>();

    public Integer getNumNodesHidden() {
        return numNodesHidden;
    }

    public void setNumNodesHidden(Integer numNodesHidden) {
        this.numNodesHidden = numNodesHidden;
    }

    public Integer getNumIterations() {
        return numIterations;
    }

    public void setNumIterations(Integer numIterations) {
        this.numIterations = numIterations;
    }

    public List<ExplanatoryVariable> getExplVars() {
        return explVars;
    }

    public void setExplVars(List<ExplanatoryVariable> explVars) {
        this.explVars = explVars;
    }
}
