package params;

import java.util.ArrayList;
import java.util.List;
import utils.CrispExplanatoryVariable;
import utils.CrispOutputVariable;

public class RBFParams extends Params {
    
    private Integer numNodesHidden;
    private List<CrispExplanatoryVariable> explVars = new ArrayList<>();
    private List<CrispOutputVariable> outVars = new ArrayList<>();
    private Integer maxIterations;

    public Integer getNumNodesHidden() {
        return numNodesHidden;
    }

    public void setNumNodesHidden(Integer numNodesHidden) {
        this.numNodesHidden = numNodesHidden;
    }

    public List<CrispExplanatoryVariable> getExplVars() {
        return explVars;
    }

    public void setExplVars(List<CrispExplanatoryVariable> explVars) {
        this.explVars = explVars;
    }

    public List<CrispOutputVariable> getOutVars() {
        return outVars;
    }

    public void setOutVars(List<CrispOutputVariable> outVars) {
        this.outVars = outVars;
    }

    public Integer getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(Integer maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Override
    public RBFParams getClone() {
        RBFParams param = new RBFParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setExplVars(explVars);
        param.setNumForecasts(this.getNumForecasts());
        param.setNumNodesHidden(numNodesHidden);
        param.setOutVars(outVars);
        param.setPercentTrain(this.getPercentTrain());
        
        return param;
    }
    
}