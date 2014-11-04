package params;

import java.util.ArrayList;
import java.util.List;
import utils.IntervalExplanatoryVariable;
import utils.IntervalOutputVariable;

public class RBFParams extends Params {
    
    private Integer numNodesHidden;
    private List<IntervalExplanatoryVariable> explVars = new ArrayList<>();
    private List<IntervalOutputVariable> outVars = new ArrayList<>();

    public Integer getNumNodesHidden() {
        return numNodesHidden;
    }

    public void setNumNodesHidden(Integer numNodesHidden) {
        this.numNodesHidden = numNodesHidden;
    }

    public List<IntervalExplanatoryVariable> getExplVars() {
        return explVars;
    }

    public void setExplVars(List<IntervalExplanatoryVariable> explVars) {
        this.explVars = explVars;
    }

    public List<IntervalOutputVariable> getOutVars() {
        return outVars;
    }

    public void setOutVars(List<IntervalOutputVariable> outVars) {
        this.outVars = outVars;
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
