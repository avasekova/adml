package models;

import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import params.Params;
import utils.MyRengine;

public class KNNcustom implements Forecastable {

    @Override
    public TrainAndTestReport forecast(List<Double> allData, Params parameters) {
        
        Rengine rengine = MyRengine.getRengine();
//        rengine.eval("num <- abs.difference(15,10)");
//        REXP getNum = rengine.eval("num");
        REXP getNum = rengine.eval("abs.difference(15,10)");
        double[] num = getNum.asDoubleArray();
        System.out.println("vysledok: " + num[0]);
        
        
        
        return null;
    }

    @Override
    public String getOptionalParams(Params parameters) {
        return "";
    }
}
