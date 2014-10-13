package params;

import java.util.ArrayList;
import java.util.List;
import utils.imlp.ExplanatoryVariable;
import utils.imlp.OutputVariable;

public class IntervalMLPCcodeParams extends Params {
    
    private Integer numNodesHidden;
    private Integer numIterations;
    private List<ExplanatoryVariable> explVars = new ArrayList<>();
    private List<OutputVariable> outVars = new ArrayList<>();
    private String distanceFunction;
    private double distanceFunctionParam1; //TODO potom to spravit krajsie - urobit si entity pre vsetky distances?

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

    public List<OutputVariable> getOutVars() {
        return outVars;
    }

    public void setOutVars(List<OutputVariable> outVars) {
        this.outVars = outVars;
    }

    public String getDistanceFunction() {
        return distanceFunction;
    }

    public void setDistanceFunction(String distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    public double getDistanceFunctionParam1() {
        return distanceFunctionParam1;
    }

    public void setDistanceFunctionParam1(double distanceFunctionParam1) {
        this.distanceFunctionParam1 = distanceFunctionParam1;
    }
}
