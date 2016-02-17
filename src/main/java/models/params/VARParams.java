package models.params;

import gui.MainFrame;
import gui.settingspanels.SettingsPanel;
import gui.settingspanels.VARSettingsPanel;
import gui.tablemodels.DataTableModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VARParams extends Params {
    
    private List<String> endogenousVars = new ArrayList<>();
    private Integer lag;
    private String type;
    private Map<String, List<Double>> data = new HashMap<>();
    private String outputVarName;
    private List<Double> outputVarVals;

    public List<String> getEndogenousVars() {
        return endogenousVars;
    }

    public void setEndogenousVars(List<String> endogenousVars) {
        this.endogenousVars = endogenousVars;
    }

    public Integer getLag() {
        return lag;
    }

    public void setLag(Integer lag) {
        this.lag = lag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, List<Double>> getData() {
        return data;
    }

    public void setData(Map<String, List<Double>> data) {
        this.data = data;
    }

    public String getOutputVarName() {
        return outputVarName;
    }

    public void setOutputVarName(String outputVarName) {
        this.outputVarName = outputVarName;
    }

    public List<Double> getOutputVarVals() {
        return outputVarVals;
    }

    public void setOutputVarVals(List<Double> outputVarVals) {
        this.outputVarVals = outputVarVals;
    }

    @Override
    public VARParams getClone() {
        VARParams param = new VARParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setSeasonality(this.getSeasonality());
        param.setEndogenousVars(endogenousVars);
        param.setLag(lag);
        param.setType(type);
        param.setData(data);
        param.setOutputVarName(outputVarName);
        param.setOutputVarVals(outputVarVals);
        
        return param;
    }

    @Override
    public String toString() {
        return "endogenous variables = " + endogenousVars + "\n" + 
               "lag = " + lag + "\n" + 
               "type = " + type;
    }
    
    
    public static List<VARParams> getParamsVAR(javax.swing.JComboBox comboBoxColName, javax.swing.JPanel panelSettingsVAR) throws IllegalArgumentException {
        VARParams par = new VARParams();
        //get all params for the model:
        par.setPercentTrain(100); //uses all data for training
        
        List<VARParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        MainFrame.getInstance().setParamsGeneral(VARParams.class, resultList);
        ((VARSettingsPanel)panelSettingsVAR).setSpecificParams(VARParams.class, resultList);
        //TODO move inside?
        SettingsPanel.setSomethingOneValue(VARParams.class, resultList, "setOutputVarName",
                String.class, comboBoxColName.getSelectedItem().toString());
        SettingsPanel.setSomethingOneValue(VARParams.class, resultList, "setOutputVarVals",
                List.class, DataTableModel.getInstance().getDataForColname(comboBoxColName.getSelectedItem().toString()));
        
        Map<String, List<Double>> data = new HashMap<>();
        for (Object var : ((VARSettingsPanel)panelSettingsVAR).getEndogenousVars()) {
            data.put(var.toString(), DataTableModel.getInstance().getDataForColname(var.toString()));
        }
        SettingsPanel.setSomethingOneValue(VARParams.class, resultList, "setData", Map.class, data);
        
        return resultList;
    }
}
