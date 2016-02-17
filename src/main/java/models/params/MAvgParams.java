package models.params;

import gui.MainFrame;
import gui.settingspanels.MAvgSettingsPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MAvgParams extends Params {
    
    private String colName;
    private Integer order;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
    
    @Override
    public MAvgParams getClone() {
        MAvgParams param = new MAvgParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setSeasonality(this.getSeasonality());
        param.setColName(colName);
        param.setOrder(order);
        
        return param;
    }

    @Override
    public String toString() {
        return "time series = " + colName + "\n" + 
               "order = " + order;
    }    
    
    public static List<MAvgParams> getParamsMAvg(JComboBox comboBoxColname, JPanel main) {
        MAvgParams par = new MAvgParams();
        //get all params for the model:
        par.setPercentTrain(100);
        par.setColName(comboBoxColname.getSelectedItem().toString()); //data
        
        List<MAvgParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        MainFrame.getInstance().setParamsGeneral(MAvgParams.class, resultList);
        ((MAvgSettingsPanel)main).setSpecificParams(MAvgParams.class, resultList);
        
        return resultList;
    }
}
