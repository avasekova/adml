package models.params;

import gui.MainFrame;
import gui.settingspanels.KNNkknnSettingsPanel;
import gui.settingspanels.PercentTrainSettingsPanel;

import java.util.ArrayList;
import java.util.List;

public class KNNkknnParams extends Params {
    
    private String colName;
    
    private int maxNeighbours;
    private long bestNumNeighbours;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public int getMaxNeighbours() {
        return maxNeighbours;
    }

    public void setMaxNeighbours(Integer maxNeighbours) {
        this.maxNeighbours = maxNeighbours;
    }

    public long getBestNumNeighbours() {
        return bestNumNeighbours;
    }

    public void setBestNumNeighbours(long bestNumNeighbours) {
        this.bestNumNeighbours = bestNumNeighbours;
    }
    
    @Override
    public KNNkknnParams getClone() {
        KNNkknnParams param = new KNNkknnParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setSeasonality(this.getSeasonality());
        param.setMaxNeighbours(maxNeighbours);
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setColName(colName);
        
        return param;
    }

    @Override
    public String toString() {
        return "max k=" + maxNeighbours + ",\n"
                + "best k=" + bestNumNeighbours;
    }
    
    
    public static List<KNNkknnParams> getParamsKNNkknn(javax.swing.JPanel percentTrainSettingsPanel,
            javax.swing.JComboBox comboBoxColName, javax.swing.JPanel panelSettingsKNNkknn) {
        KNNkknnParams par = new KNNkknnParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColName(comboBoxColName.getSelectedItem().toString()); //data
        
        List<KNNkknnParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        MainFrame.getInstance().setParamsGeneral(KNNkknnParams.class, resultList);
        ((KNNkknnSettingsPanel)panelSettingsKNNkknn).setSpecificParams(KNNkknnParams.class, resultList);
        
        return resultList;
    }
}
