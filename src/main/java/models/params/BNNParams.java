package models.params;

import gui.MainFrame;
import gui.settingspanels.BNNSettingsPanel;
import gui.settingspanels.PercentTrainSettingsPanel;
import gui.settingspanels.SettingsPanel;
import java.util.ArrayList;
import java.util.List;
import utils.CrispExplanatoryVariable;
import utils.CrispOutputVariable;

public class BNNParams extends Params {
    
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
    public BNNParams getClone() {
        BNNParams param = new BNNParams();
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
        return "numNodesHidden = " + numNodesHidden + ",\n"
                + "explVars=" + explVars + ",\n"
                + "outVars=" + outVars + ",\n"
                + "maxIterations=" + maxIterations;
    }
    
    
    public static List<BNNParams> getParamsBNN(javax.swing.JPanel percentTrainSettingsPanel,
            javax.swing.JComboBox comboBoxColName, javax.swing.JPanel panelSettingsBNN) throws IllegalArgumentException {
        BNNParams par = new BNNParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        
        List<BNNParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        MainFrame.getInstance().setParamsGeneral(BNNParams.class, resultList);
        ((BNNSettingsPanel)panelSettingsBNN).setSpecificParams(BNNParams.class, resultList);
        //POZOR, OutVars sa nastavuju az tu vonku! TODO prerobit
        CrispOutputVariable outVar = new CrispOutputVariable(); //berie hodnoty z CTS Run
        outVar.setName(comboBoxColName.getSelectedItem().toString() + comboBoxColName.getSelectedIndex());
        outVar.setFieldName(comboBoxColName.getSelectedItem().toString());
        List<CrispOutputVariable> outVarList = new ArrayList<>();
        outVarList.add(outVar);
        SettingsPanel.setSomethingOneValue(BNNParams.class, resultList, "setOutVars", List.class, outVarList);
        
        return resultList;
    }
}
