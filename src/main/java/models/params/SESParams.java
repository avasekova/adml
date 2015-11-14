package models.params;

import gui.MainFrame;
import gui.settingspanels.PercentTrainSettingsPanel;
import gui.settingspanels.SESSettingsPanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class SESParams extends Params {
    
    private String colName;
    private String alpha;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }
    
    @Override
    public SESParams getClone() {
        SESParams param = new SESParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setSeasonality(this.getSeasonality());
        param.setColName(colName);
        param.setAlpha(alpha);
        
        return param;
    }

    @Override
    public String toString() {
        return "time series = " + colName + "\n" + 
               "alpha = " + alpha;
    }    
    
    
    public static List<SESParams> getParamsSES(JPanel percentTrainSettingsPanel, JComboBox comboBoxColname, JPanel main) {
        SESParams par = new SESParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColName(comboBoxColname.getSelectedItem().toString()); //data
        
        List<SESParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        MainFrame.getInstance().setParamsGeneral(SESParams.class, resultList);
        ((SESSettingsPanel)main).setSpecificParams(SESParams.class, resultList);
        
        return resultList;
    }
}
