package models;

import gui.tablemodels.DataTableModel;
import models.params.BinomPropParams;
import models.params.Params;
import utils.Const;
import utils.MyRengine;

public class BinomProp implements Forecastable {

    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        
        BinomPropParams params = (BinomPropParams) parameters;
        
        MyRengine rengine = MyRengine.getRengine();
        rengine.require("forecast");
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Const.BINOM_PROP);
        
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return ""; //TODO uz konecne odstranit toto z toho interfejsu :)
    }
    
}
