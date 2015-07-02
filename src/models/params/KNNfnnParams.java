package models.params;

import gui.MainFrame;
import gui.settingspanels.KNNFNNSettingsPanel;
import gui.settingspanels.PercentTrainSettingsPanel;
import java.util.ArrayList;
import java.util.List;

public class KNNfnnParams extends Params {
    
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
    public KNNfnnParams getClone() {
        KNNfnnParams param = new KNNfnnParams();
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
    
    
    public static List<KNNfnnParams> getParamsKNNfnn(javax.swing.JPanel percentTrainSettingsPanel,
            javax.swing.JComboBox comboBoxColName, javax.swing.JPanel panelSettingsKNNfnn) {
        KNNfnnParams par = new KNNfnnParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColName(comboBoxColName.getSelectedItem().toString()); //data
        
        List<KNNfnnParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        MainFrame.getInstance().setParamsGeneral(KNNfnnParams.class, resultList);
        ((KNNFNNSettingsPanel)panelSettingsKNNfnn).setSpecificParams(KNNfnnParams.class, resultList);
        
        return resultList;
    }
}
