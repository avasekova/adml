package analysis;

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
        simpleTransform(selectedValuesList, "log", "LOG");
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
    
    public static void aggregateToITS(List<String> selectedValuesList, int length) {
        MyRengine rengine = MyRengine.getRengine();
        
        final String VAR = Const.INPUT + Utils.getCounter();
        final String CHUNKS = Const.INPUT + Utils.getCounter();
        final String LOWERB = Const.INPUT + Utils.getCounter();
        final String UPPERB = Const.INPUT + Utils.getCounter();
        final String CENTERS = Const.INPUT + Utils.getCounter();
        final String RADII = Const.INPUT + Utils.getCounter();
        
        for (String selected : selectedValuesList) {
            rengine.assign(VAR, Utils.listToArray(DataTableModel.getInstance().getDataForColname(selected)));
            
            //found this on SO: http://stackoverflow.com/a/3321659
            //split into chunks of given length
            rengine.eval(CHUNKS + " <- split(" + VAR + ", ceiling(seq_along(" + VAR + ")/" + length + "))");
            int numChunks = rengine.eval("length(" + CHUNKS + ")").asIntArray()[0];
            
            StringBuilder mins = new StringBuilder("c(");
            StringBuilder maxs = new StringBuilder("c(");
            for (int i = 0; i < numChunks; i++) {
                if (i > 0) {
                    mins.append(",");
                    maxs.append(",");
                }
                
                mins.append("min(").append(CHUNKS).append("[[").append(i+1).append("]]").append(")");
                maxs.append("max(").append(CHUNKS).append("[[").append(i+1).append("]]").append(")");
            }
            mins.append(")");
            maxs.append(")");
            
            //vytvor LB a UB
            rengine.eval(LOWERB + " <- " + mins.toString());
            rengine.eval(UPPERB + " <- " + maxs.toString());
            
            //vytvor este C a R
            rengine.eval(CENTERS + " <- (" + UPPERB + " + " + LOWERB + ")/2");
            rengine.eval(RADII + " <- (" + UPPERB + " - " + LOWERB + ")/2");
            
            //pridaj vsetko medzi data
            DataTableModel.getInstance().addDataForColname("LB_" + length + "(" + selected + ")", Utils.arrayToList(rengine.eval(LOWERB).asDoubleArray()));
            DataTableModel.getInstance().addDataForColname("UB_" + length + "(" + selected + ")", Utils.arrayToList(rengine.eval(UPPERB).asDoubleArray()));
            DataTableModel.getInstance().addDataForColname("C_" + length + "(" + selected + ")", Utils.arrayToList(rengine.eval(CENTERS).asDoubleArray()));
            DataTableModel.getInstance().addDataForColname("R_" + length + "(" + selected + ")", Utils.arrayToList(rengine.eval(RADII).asDoubleArray()));
        }
        
        rengine.rm(VAR, CHUNKS, LOWERB, UPPERB, CENTERS, RADII);
    }
    
    public static void normalize(List<String> selectedValuesList) {
        simpleTransform(selectedValuesList, "MLPtoR.scale", "NORM");
    }
    
    private static void simpleTransform(List<String> selectedValuesList, String transformName, String outputVarName) {
        MyRengine rengine = MyRengine.getRengine();
        
        final String VAR = Const.INPUT + Utils.getCounter();
        
        for (String selected : selectedValuesList) {
            rengine.assign(VAR, Utils.listToArray(DataTableModel.getInstance().getDataForColname(selected)));
            rengine.eval(VAR + " <- " + transformName + "(" + VAR + ")");
            DataTableModel.getInstance().addDataForColname(outputVarName + "(" + selected + ")", 
                    Utils.arrayToList(rengine.eval(VAR).asDoubleArray()));
        }
        
        rengine.rm(VAR);
    }
}
