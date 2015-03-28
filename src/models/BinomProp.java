package models;

import gui.tablemodels.DataTableModel;
import models.params.BinomPropParams;
import models.params.Params;
import org.rosuda.JRI.Rengine;
import utils.Const;
import utils.MyRengine;

public class BinomProp implements Forecastable {

    @Override
    public TrainAndTestReport forecast(DataTableModel dataTableModel, Params parameters) {
        
        BinomPropParams params = (BinomPropParams) parameters;
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(forecast)");
        
        TrainAndTestReportCrisp report = new TrainAndTestReportCrisp(Const.BINOM_PROP);
        
        
        return report;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return ""; //TODO uz konecne odstranit toto z toho interfejsu :)
    }
    
}
