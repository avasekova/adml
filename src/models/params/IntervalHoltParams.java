package models.params;

import gui.MainFrame;
import gui.settingspanels.DistanceSettingsPanel;
import gui.settingspanels.IntHoltSettingsPanel;
import gui.settingspanels.PercentTrainSettingsPanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import utils.imlp.dist.BertoluzzaDistance;
import utils.imlp.dist.DeCarvalhoDistance;
import utils.imlp.dist.Distance;
import utils.imlp.dist.HausdorffDistance;
import utils.imlp.dist.IchinoYaguchiDistance;
import utils.imlp.dist.WeightedEuclideanDistance;

public class IntervalHoltParams extends Params {
    
    private String colNameCenter;
    private String colNameRadius;
    private String alpha;
    private String beta;
    private Distance distance;

    public String getColNameCenter() {
        return colNameCenter;
    }

    public void setColNameCenter(String colNameCenter) {
        this.colNameCenter = colNameCenter;
    }

    public String getColNameRadius() {
        return colNameRadius;
    }

    public void setColNameRadius(String colNameRadius) {
        this.colNameRadius = colNameRadius;
    }

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    public String getBeta() {
        return beta;
    }

    public void setBeta(String beta) {
        this.beta = beta;
    }

    public Distance getDistance() {
        return distance;
    }
    
    public String getDistanceId() {
        if (distance instanceof WeightedEuclideanDistance) {
            return "WE";
        } else if (distance instanceof HausdorffDistance) {
            return "H";
        } else if (distance instanceof DeCarvalhoDistance) {
            return "DC";
        } else if (distance instanceof IchinoYaguchiDistance) {
            return "IY";
        }
        
        return "Not supported";
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }
    
    @Override
    public IntervalHoltParams getClone() {
        IntervalHoltParams param = new IntervalHoltParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setNumForecasts(this.getNumForecasts());
        param.setPercentTrain(this.getPercentTrain());
        param.setSeasonality(this.getSeasonality());
        param.setColNameCenter(colNameCenter);
        param.setColNameRadius(colNameRadius);
        param.setAlpha(alpha);
        param.setBeta(beta);
        param.setDistance(distance);
        
        return param;
    }

    @Override
    public String toString() {
        return "Center:\n" + colNameCenter + "\n" +
               "Radius:\n" + colNameRadius + "\n" +
               "alpha = " + alpha + "\n" + 
               "beta = " + beta;
    }
    
    
    public static List<IntervalHoltParams> getParamsIntervalHolt(JPanel percentTrainSettingsPanel, JComboBox comboBoxCenter,
            JComboBox comboBoxRadius, JPanel distancePanel, JPanel panelSettingsHolt) {
        IntervalHoltParams par = new IntervalHoltParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColNameCenter(comboBoxCenter.getSelectedItem().toString()); //data
        par.setColNameRadius(comboBoxRadius.getSelectedItem().toString());
        
        List<IntervalHoltParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        MainFrame.getInstance().setParamsGeneral(IntervalHoltParams.class, resultList);
        ((IntHoltSettingsPanel)panelSettingsHolt).setSpecificParams(IntervalHoltParams.class, resultList);
        ((DistanceSettingsPanel)distancePanel).setSpecificParams(IntervalHoltParams.class, resultList);
        
        return resultList;
    }
}
