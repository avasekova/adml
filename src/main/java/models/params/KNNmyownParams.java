package models.params;

import gui.MainFrame;
import gui.settingspanels.KNNmyownSettingsPanel;
import gui.settingspanels.PercentTrainSettingsPanel;
import java.util.ArrayList;
import java.util.List;

public class KNNmyownParams extends Params {
    
    private String colName;
    
    private int numNeighbours;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }
    
    public int getNumNeighbours() {
        return numNeighbours;
    }

    public void setNumNeighbours(Integer numNeighbours) {
        this.numNeighbours = numNeighbours;
    }
    
    @Override
    public KNNmyownParams getClone() {
        KNNmyownParams param = new KNNmyownParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setSeasonality(this.getSeasonality());
        param.setNumForecasts(this.getNumForecasts());
        param.setNumNeighbours(numNeighbours);
        param.setPercentTrain(this.getPercentTrain());
        param.setColName(colName);
        
        return param;
    }

    @Override
    public String toString() {
        return "numNeighbours=" + numNeighbours;
    }
    
    
    public static List<KNNmyownParams> getParamsKNNmyown(javax.swing.JPanel percentTrainSettingsPanel,
            javax.swing.JComboBox comboBoxColName, javax.swing.JPanel panelSettingsKNNmyown) {
        KNNmyownParams par = new KNNmyownParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColName(comboBoxColName.getSelectedItem().toString()); //data
        
        List<KNNmyownParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        MainFrame.getInstance().setParamsGeneral(KNNmyownParams.class, resultList);
        ((KNNmyownSettingsPanel)panelSettingsKNNmyown).setSpecificParams(KNNmyownParams.class, resultList);
        
        return resultList;
    }
}
