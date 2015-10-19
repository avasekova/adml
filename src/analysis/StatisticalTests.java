package analysis;

import gui.tablemodels.DataTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import utils.Const;
import utils.MyRengine;
import utils.Utils;
import utils.imlp.IntervalNames;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;

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
    
    public static List<String> stucturalBreaksTests(List<String> selectedValuesList, List<IntervalNames> selectedIntervalsList, 
            int breaks) {
        //TODO este zrefaktorovat. toto by malo tie veci len pocitat, ploty potom do plotovatka
        //TODO vymysliet, co s tym navratovym typom, je to nechutne, takto zamiesat: posledny prvok je strInfo, zbytok su ploty
        
        MyRengine rengine = MyRengine.getRengine();

        rengine.require("bfast");

        List<String> plots = new ArrayList<>();
        StringBuilder strBreaksInfo = new StringBuilder();
        
        final String DATA = Const.INPUT + Utils.getCounter();
        final String DATA_TS = DATA + "ts";
        final String FIT = Const.FIT + Utils.getCounter();
        
        //najprv vybavit jednoduche hodnoty
        for (String selectedVal : selectedValuesList) {
            rengine.assign(DATA, Utils.listToArray(DataTableModel.getInstance().getDataForColname(selectedVal)));
            rengine.eval(DATA_TS + " <- ts(" + DATA + ")");
            
            rengine.eval(FIT + " <- bfast(" + DATA_TS + ", h=10/length(" + DATA + "), season=\"none\", max.iter=1, breaks="
                    + breaks + ")");
            
            //draw the plot with str. breaks
            StringBuilder pl = new StringBuilder("plot.ts(");
            pl.append(DATA).append(", col=\"red\", ylab=\"").append(selectedVal).append("\");")
                    .append("lines(").append(FIT).append("$output[[1]]$Tt)").append(";")
                    .append("abline(v=").append(FIT).append("$output[[1]]$bp.Vt$breakpoints, lty=3, col=\"blue\")");
            plots.add(pl.toString());
            
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
        
        final String DATA1 = Const.INPUT + Utils.getCounter();
        final String DATA1_TS = DATA1 + "ts";
        final String DATA2 = Const.INPUT + Utils.getCounter();
        final String DATA2_TS = DATA2 + "ts";
        final String FIT1 = Const.FIT + Utils.getCounter();
        final String FIT2 = Const.FIT + Utils.getCounter();
        
        //potom intervaly:
        for (IntervalNames i : selectedIntervalsList) {
            String ylab1 = "";
            String ylab2 = "";
            if (i instanceof IntervalNamesCentreRadius) {
                ylab1 = ((IntervalNamesCentreRadius)i).getCentre();
                ylab2 = ((IntervalNamesCentreRadius)i).getRadius();
            } else {
                ylab1 = ((IntervalNamesLowerUpper)i).getLowerBound();
                ylab2 = ((IntervalNamesLowerUpper)i).getUpperBound();
            }
            rengine.assign(DATA1, Utils.listToArray(DataTableModel.getInstance().getDataForColname(ylab1)));
            rengine.assign(DATA2, Utils.listToArray(DataTableModel.getInstance().getDataForColname(ylab2)));
            rengine.eval(DATA1_TS + " <- ts(" + DATA1 + ")");
            rengine.eval(DATA2_TS + " <- ts(" + DATA2 + ")");
            
            rengine.eval(FIT1 + " <- bfast(" + DATA1_TS + ", h=10/length(" + DATA1 + "), season=\"none\", max.iter=1, breaks="
                    + breaks + ")");
            rengine.eval(FIT2 + " <- bfast(" + DATA2_TS + ", h=10/length(" + DATA2 + "), season=\"none\", max.iter=1, breaks="
                    + breaks + ")");
            
            //draw the plot with str. breaks - first component
            StringBuilder pl = new StringBuilder();
            pl.append("par(mfrow=c(2,1))");
            pl.append(";");
            pl.append("plot.ts(").append(DATA1).append(", col=\"red\", ylab=\"").append(ylab1).append("\");")
                    .append("lines(").append(FIT1).append("$output[[1]]$Tt)").append(";")
                    .append("abline(v=").append(FIT1).append("$output[[1]]$bp.Vt$breakpoints, lty=3, col=\"blue\")");
            pl.append(";");
            //draw the plot with str. breaks - second component
            pl.append("plot.ts(").append(DATA2).append(", col=\"red\", ylab=\"").append(ylab2).append("\");")
                    .append("lines(").append(FIT2).append("$output[[1]]$Tt)").append(";")
                    .append("abline(v=").append(FIT2).append("$output[[1]]$bp.Vt$breakpoints, lty=3, col=\"blue\")");
            plots.add(pl.toString());
            
            double[] breakpoints1 = null;
            if (rengine.eval(FIT1 + "$output[[1]]$bp.Vt$breakpoints") != null) {
                breakpoints1 = rengine.eval(FIT1 + "$output[[1]]$bp.Vt$breakpoints").asDoubleArray();
            }
            
            double[] breakpoints2 = null;
            if (rengine.eval(FIT2 + "$output[[1]]$bp.Vt$breakpoints") != null) {
                breakpoints2 = rengine.eval(FIT2 + "$output[[1]]$bp.Vt$breakpoints").asDoubleArray();
            }
            strBreaksInfo.append("-----\n").append("Structural breaks for ").append(i.toString()).append(":\n");
            if (breakpoints1 == null) {
                strBreaksInfo.append("(No structural breaks detected for the first component.)");
            } else {
                strBreaksInfo.append(Arrays.toString(breakpoints1));
            }
            strBreaksInfo.append("\n");
            if (breakpoints2 == null) {
                strBreaksInfo.append("(No structural breaks detected for the second component.)");
            } else {
                strBreaksInfo.append(Arrays.toString(breakpoints2));
            }
            strBreaksInfo.append("\n\n");
        }
        
        rengine.rm(DATA, DATA_TS, FIT, DATA1, DATA1_TS, DATA2, DATA2_TS, FIT1, FIT2);
        
        //TODO toto vyriesit, blee
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
}
