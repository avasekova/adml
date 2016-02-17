package analysis;

import gui.tablemodels.DataTableModel;
import utils.Const;
import utils.MyRengine;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    
    public static List<String> stucturalBreaksTests(List<String> selectedValuesList, int breaks) {
        //TODO refactor: this should only compute the thing; plots by PlotDrawer
        //TODO return type: now the last item is strInfo, the rest are plots; ugly
        
        MyRengine rengine = MyRengine.getRengine();

        rengine.require("bfast");

        List<String> plots = new ArrayList<>();
        StringBuilder strBreaksInfo = new StringBuilder();
        
        final String DATA = Const.INPUT + Utils.getCounter();
        final String DATA_TS = DATA + "ts";
        final String FIT = Const.FIT + Utils.getCounter();
        
        for (String selectedVal : selectedValuesList) {
            rengine.assign(DATA, Utils.listToArray(DataTableModel.getInstance().getDataForColname(selectedVal)));
            rengine.eval(DATA_TS + " <- ts(" + DATA + ")");
            
            rengine.eval(FIT + " <- bfast(" + DATA_TS + ", h=10/length(" + DATA + "), season=\"none\", max.iter=1, breaks="
                    + breaks + ")");
            
            //draw the plot with str. breaks
            String pl = "plot.ts(" + DATA + ", col=\"red\", ylab=\"" + selectedVal + "\");" +
                    "lines(" + FIT + "$output[[1]]$Tt)" + ";" +
                    "abline(v=" + FIT + "$output[[1]]$bp.Vt$breakpoints, lty=3, col=\"blue\")";
            plots.add(pl);
            
            double[] breakpoints = null;
            if (rengine.eval(FIT + "$output[[1]]$bp.Vt$breakpoints") != null) {
                breakpoints = rengine.eval(FIT + "$output[[1]]$bp.Vt$breakpoints").asDoubleArray();
            }
            strBreaksInfo.append("-----\n").append("Structural breaks for ").append(selectedVal).append(":\n");
            if (breakpoints == null) {
                strBreaksInfo.append("(No structural breaks detected.)");
            } else {
                strBreaksInfo.append(Arrays.toString(breakpoints));
            }
            strBreaksInfo.append("\n\n");
        }
        
        rengine.rm(DATA, DATA_TS, FIT);
        
        //TODO chg this
        plots.add(strBreaksInfo.toString());
        
        return plots;
    }

    public static String KMOTest(List<String> selectedValuesList) {
        MyRengine rengine = MyRengine.getRengine();

        rengine.require("psych");
        
        final String INPUT = rengine.createDataFrame(selectedValuesList);
        
        final String RESULT = Const.OUTPUT + Utils.getCounter();
        rengine.eval(RESULT + " <- KMO(cor(" + INPUT + "))");
        
        double MSA = rengine.eval(RESULT + "$MSA").asDouble(); //The overall Measure of Sampling Adequacy
        double[] MSAi = rengine.eval(RESULT + "$MSAi").asDoubleArray(); //The measure of sampling adequacy for each item
        
        StringBuilder results = new StringBuilder("Results of the KMO test:\n");
        results.append("Overall MSA: ").append(Utils.valToDecPoints(MSA)).append("\n\n");
        results.append("MSA for each item:\n");
        
        for (int i = 0; i < MSAi.length; i++) {
            results.append(Utils.valToDecPoints(MSAi[i])).append(" [").append(selectedValuesList.get(i)).append("]").append("\n");
        }
        
        rengine.rm(INPUT);
        
        return results.toString();
    }

    public static String bartlettsTest(List<String> selectedValuesList) {
        MyRengine rengine = MyRengine.getRengine();

        rengine.require("psych");
        
        final String INPUT = rengine.createDataFrame(selectedValuesList);
        
        final String RESULT = Const.OUTPUT + Utils.getCounter();
        rengine.eval(RESULT + " <- cortest.bartlett(cor(" + INPUT + "), n=" + 
                DataTableModel.getInstance().getDataForColname(selectedValuesList.get(0)).size() + ")");
        
        double pValue = rengine.eval(RESULT + "$p.value").asDouble();
        
        rengine.rm(INPUT);
        
        return "Results of the Bartlett's test:\np value = " + Utils.valToDecPoints(pValue);
    }
}
