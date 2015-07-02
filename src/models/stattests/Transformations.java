package models.stattests;

import gui.tablemodels.DataTableModel;
import java.util.ArrayList;
import java.util.List;
import utils.Const;
import utils.MyRengine;
import utils.Utils;

public class Transformations {
    
    public static void difference(List<String> selectedValuesList) {
        MyRengine rengine = MyRengine.getRengine();
        
        final String VAR = Const.INPUT + Utils.getCounter();
        
        for (String selected : selectedValuesList) {
            rengine.assign(VAR, Utils.listToArray(DataTableModel.getInstance().getDataForColname(selected)));
            rengine.eval(VAR + " <- " + VAR + "[2:length(" + VAR + ")] - " + VAR + "[1:(length(" + VAR + ") - 1)]");
            List<Double> newData = new ArrayList<>();
            //newData.add(Double.NaN); //TODO vymysliet, ako to posunut doprava... teraz je to 'zarovnane' dolava, co je zle
            newData.addAll(Utils.arrayToList(rengine.eval(VAR).asDoubleArray()));
            DataTableModel.getInstance().addDataForColname("DIFF(" + selected + ")", newData);
        }
        
        rengine.rm(VAR);
    }
    
    public static void logTransform(List<String> selectedValuesList) {
        MyRengine rengine = MyRengine.getRengine();
        
        final String VAR = Const.INPUT + Utils.getCounter();
        
        for (String selected : selectedValuesList) {
            rengine.assign(VAR, Utils.listToArray(DataTableModel.getInstance().getDataForColname(selected)));
            rengine.eval(VAR + " <- log(" + VAR + ")");
            DataTableModel.getInstance().addDataForColname("LOG(" + selected + ")", Utils.arrayToList(rengine.eval(VAR).asDoubleArray()));
        }
        
        rengine.rm(VAR);
    }
    
    public static void removeTrend(List<String> selectedValuesList) {
        MyRengine rengine = MyRengine.getRengine();
        
        final String VAR = Const.INPUT + Utils.getCounter();
        final String DATA = Const.INPUT + Utils.getCounter();
        final String REG = Const.INPUT + Utils.getCounter();
        
        for (String selected : selectedValuesList) {
            rengine.assign(VAR, Utils.listToArray(DataTableModel.getInstance().getDataForColname(selected)));
            
            //najprv si k tomu zozeniem regresnu priamku:
            //k nej si potrebujem vyrobit ten frame:
            rengine.eval(DATA + " <- cbind(seq(1, length(" + VAR + ")), " + VAR + ")");
            //teraz mu premenujem stlpce
            rengine.eval("colnames(" + DATA + ") <- c(\"x\", \"y\")");
            //mozem poskladat rovnicu regresnej priamky:
            rengine.eval(REG + " <- lm(y ~ x, data = data.frame(" + DATA + "))");
            //z toho vytiahnem koeficienty a odcitam tuto priamku od povodnych dat, tj odstranim trend
            rengine.eval(VAR + " <- " + VAR + " - (" + REG + "$coefficients[1] + " 
                                                     + REG + "$coefficients[2]*seq(1, length(" + VAR + ")))"
//                             + " + mean(" + VAR + ")"               //pripadne odkomentovat toto, aby ta nova TS krizovala staru
                        );
            
            DataTableModel.getInstance().addDataForColname("NOTREND(" + selected + ")", Utils.arrayToList(rengine.eval(VAR).asDoubleArray()));
        }
        
        rengine.rm(VAR, DATA, REG);
    }
    
}
