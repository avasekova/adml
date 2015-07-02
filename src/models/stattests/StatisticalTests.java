package models.stattests;

import gui.tablemodels.DataTableModel;
import java.util.ArrayList;
import java.util.List;
import utils.Const;
import utils.MyRengine;
import utils.Utils;

public class StatisticalTests {
    
    public static String normalProbabilityTests(List<String> selectedValuesList) {
        StringBuilder results = new StringBuilder();
        
        MyRengine rengine = MyRengine.getRengine();

        rengine.require("nortest");

        String DATA = Const.INPUT + Utils.getCounter();

        for (String selectedVal : selectedValuesList) {
            rengine.assign(DATA, Utils.listToArray(DataTableModel.getInstance().getDataForColname(selectedVal)));

            results.append("------------\n").append("Testing ").append(selectedVal).append(" for normality:\n\n");

            results.append("Anderson-Darling test for normality:\n");
            results.append("   - p-value: ");
            results.append(rengine.eval("ad.test(" + DATA + ")$p.value").asDouble()).append("\n");

            results.append("Cramer-von Mises test for normality:\n");
            results.append("   - p-value: ");
            results.append(rengine.eval("cvm.test(" + DATA + ")$p.value").asDouble()).append("\n");

            results.append("Lilliefors (Kolmogorov-Smirnov) test for normality:\n");
            results.append("   - p-value: ");
            results.append(rengine.eval("lillie.test(" + DATA + ")$p.value").asDouble()).append("\n");

            results.append("Pearson chi-square test for normality:\n");
            results.append("   - p-value: ");
            results.append(rengine.eval("pearson.test(" + DATA + ")$p.value").asDouble()).append("\n");

            results.append("Shapiro-Francia test for normality:\n");
            results.append("   - p-value: ");
            results.append(rengine.eval("sf.test(" + DATA + ")$p.value").asDouble()).append("\n");

            results.append("\n");
        }
        rengine.rm(DATA);
        
        return results.toString();
    }
    
    public static String stationarityTests(List<String> selectedValuesList) {
        MyRengine rengine = MyRengine.getRengine();

        rengine.require("tseries");

        StringBuilder results = new StringBuilder();
        String DATA = Const.INPUT + Utils.getCounter();

        for (String selectedVal : selectedValuesList) {
            rengine.assign(DATA, Utils.listToArray(DataTableModel.getInstance().getDataForColname(selectedVal)));

            results.append("------------\n").append("Testing ").append(selectedVal).append(" for stationarity:\n\n");

            results.append("Ljung-Box test:\n");
            results.append("   - p-value: ");
            results.append(rengine.eval("Box.test(" + DATA + ", lag=20, type=\"Ljung-Box\")$p.value").asDouble()).append("\n");

            results.append("Augmented Dickey–Fuller test:\n");
            results.append("   - p-value: ");
            results.append(rengine.eval("adf.test(" + DATA + ", alternative=\"stationary\")$p.value").asDouble()).append("\n");

            results.append("Kwiatkowski-Phillips-Schmidt-Shin test:\n");
            results.append("   - p-value: ");
            results.append(rengine.eval("kpss.test(" + DATA + ")$p.value").asDouble()).append("\n");

            results.append("\n");
        }
        
        rengine.rm(DATA);
        
        return results.toString();
    }
}
