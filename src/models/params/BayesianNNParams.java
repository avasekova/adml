package models.params;

import java.util.ArrayList;
import java.util.List;
import utils.CrispExplanatoryVariable;
import utils.CrispOutputVariable;

public class BayesianNNParams extends Params {
    
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
    public BayesianNNParams getClone() {
        BayesianNNParams param = new BayesianNNParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setSeasonality(this.getSeasonality());
        param.setExplVars(explVars);
        param.setNumForecasts(this.getNumForecasts());
        param.setNumNodesHidden(numNodesHidden);
        param.setOutVars(outVars);
        param.setMaxIterations(maxIterations);
        param.setPercentTrain(this.getPercentTrain());
        
        return param;
    }
    
    @Override
    public String toString() {
        return "(to be added)";
    }
}
