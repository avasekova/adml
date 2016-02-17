package models.params;

import gui.MainFrame;
import gui.settingspanels.IntMLPCcodeSettingsPanel;
import gui.settingspanels.PercentTrainSettingsPanel;
import utils.IntervalVariable;
import utils.imlp.dist.Distance;

import java.util.ArrayList;
import java.util.List;

public class IntervalMLPCcodeParams extends Params {
    
    private Integer numNodesHidden;
    private Integer numIterations;
    private List<IntervalVariable> explVars = new ArrayList<>();
    private List<IntervalVariable> outVars = new ArrayList<>();
    private Distance distance;
    private int numNetworks;

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

    public List<IntervalVariable> getExplVars() {
        return explVars;
    }

    public void setExplVars(List<IntervalVariable> explVars) {
        this.explVars = explVars;
    }

    public List<IntervalVariable> getOutVars() {
        return outVars;
    }

    public void setOutVars(List<IntervalVariable> outVars) {
        this.outVars = outVars;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
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
        param.setSeasonality(this.getSeasonality());
        param.setDistance(distance);
        param.setExplVars(explVars);
        param.setNumForecasts(this.getNumForecasts());
        param.setNumIterations(numIterations);
        param.setNumNetworks(numNetworks);
        param.setNumNodesHidden(numNodesHidden);
        param.setOutVars(outVars);
        param.setPercentTrain(this.getPercentTrain());
        param.setCriterion(this.getCriterion());
        
        return param;
    }

    @Override
    public String toString() {
        return "numNodesHidden=" + numNodesHidden + ",\n"
                + "numIterations=" + numIterations + ",\n"
                + "explVars=" + explVars + ",\n"
                + "outVars=" + outVars + ",\n"
                + "distance=" + distance + ",\n"
                + "numNetworks=" + numNetworks;
    }
    
    public static List<IntervalMLPCcodeParams> getParamsIntervalMLPCcode(javax.swing.JPanel percentTrainSettingsPanel,
            javax.swing.JPanel panelSettingsIMLPCcode) throws IllegalArgumentException {
        IntervalMLPCcodeParams par = new IntervalMLPCcodeParams();
        //get all params for the model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        
        List<IntervalMLPCcodeParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        MainFrame.getInstance().setParamsGeneral(IntervalMLPCcodeParams.class, resultList);
        ((IntMLPCcodeSettingsPanel)panelSettingsIMLPCcode).setSpecificParams(IntervalMLPCcodeParams.class, resultList);
        
        //TODO add the criterion here
        
        return resultList;
    }
}
