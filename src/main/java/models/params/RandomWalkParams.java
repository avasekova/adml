package models.params;

import gui.MainFrame;

import java.util.ArrayList;
import java.util.List;

public class RandomWalkParams extends Params {

    private String colName;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    @Override
    public RandomWalkParams getClone() {
        RandomWalkParams param = new RandomWalkParams();
        param.setDataRangeFrom(this.getDataRangeFrom());
        param.setDataRangeTo(this.getDataRangeTo());
        param.setSeasonality(this.getSeasonality());
        param.setNumForecasts(this.getNumForecasts());
        param.setColName(colName);

        return param;
    }


    public static List<RandomWalkParams> getParamsRandomWalk(javax.swing.JComboBox comboBoxColName) {
        RandomWalkParams par = new RandomWalkParams();
        //get all params for the model:
        par.setColName(comboBoxColName.getSelectedItem().toString()); //data

        List<RandomWalkParams> resultList = new ArrayList<>();
        resultList.add(par);

        MainFrame.getInstance().setParamsGeneral(RandomWalkParams.class, resultList);

        return resultList;
    }
}
