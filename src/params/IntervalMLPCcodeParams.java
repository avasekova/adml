package params;

import java.util.ArrayList;
import java.util.List;
import utils.IntervalExplanatoryVariable;
import utils.IntervalOutputVariable;
import utils.imlp.dist.Distance;

public class IntervalMLPCcodeParams extends Params {
    
    private Integer numNodesHidden;
    private Integer numIterations;
    private List<IntervalExplanatoryVariable> explVars = new ArrayList<>();
    private List<IntervalOutputVariable> outVars = new ArrayList<>();
    private Distance distanceFunction;
    private int numNetworks;
//    private String??? criterion; //error measure based on which it will choose the best network

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

    public Distance getDistanceFunction() {
        return distanceFunction;
    }

    public void setDistanceFunction(Distance distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    public int getNumNetworks() {
        return numNetworks;
    }

    public void setNumNetworks(Integer numNetworks) {
        this.numNetworks = numNetworks;
    }
    
    @Override
    public IntervalMLPCcodeParams getClone() {
        IntervalMLPCcodeParams param = new IntervalMLPCcodeParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setDistanceFunction(distanceFunction);
        param.setExplVars(explVars);
        param.setNumForecasts(this.getNumForecasts());
        param.setNumIterations(numIterations);
        param.setNumNetworks(numNetworks);
        param.setNumNodesHidden(numNodesHidden);
        param.setOutVars(outVars);
        param.setPercentTrain(this.getPercentTrain());
        
        return param;
    }
}
