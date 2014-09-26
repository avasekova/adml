package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import models.TrainAndTestReport;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import utils.Const;
import utils.MyRengine;
import utils.Utils;

public class PlotDrawer {
    
    //TODO generovat i legendu do toho vysledneho grafu!
    public static void drawPlots(int width, int height, List<Double> allData, int numForecasts,
                                 List<TrainAndTestReport> reportsCTS, List<TrainAndTestReport> reportsITS) {
        if (reportsCTS.isEmpty() && reportsITS.isEmpty()) {
            return;
        }
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(JavaGD)");
        rengine.eval("JavaGD()");
        
        if ((! reportsCTS.isEmpty()) && (! reportsITS.isEmpty())) { //budem vykreslovat oba naraz
            rengine.eval("par(mfrow=c(1,2))"); //daj dva grafy vedla seba. potom normalne zavolat dva ploty.
        }
        
        //TODO colours!
        if (! reportsCTS.isEmpty()) { //plot CTS
            int numTrainingEntries_CTS = reportsCTS.get(0).getNumTrainingEntries();
            String rangeY_CTS = getRangeY(allData, reportsCTS);
            String rangeX = getRangeX(allData, numForecasts);
            
            boolean next = false;
            for (TrainAndTestReport r : reportsCTS) {
                if (next) {
                    rengine.eval("par(new=TRUE)");
                } else {
                    next = true;
                }

                StringBuilder plotCode = new StringBuilder(r.getFittedValuesPlotCode());
                plotCode.insert(r.getFittedValuesPlotCode().length() - 1, ", xlim = " + rangeX + ", ylim = " + rangeY_CTS + ", col=\"blue\"");
                rengine.eval(plotCode.toString());
            }

            rengine.assign("all.data", Utils.listToArray(allData));
            rengine.eval("par(new=TRUE)");
            rengine.eval("plot.ts(all.data, xlim = " + rangeX + ", ylim = " + rangeY_CTS + ")");
            rengine.eval("abline(v = " + numTrainingEntries_CTS + ", lty = 3)"); //add a dashed vertical line to separate TRAIN and TEST
            rengine.eval("abline(v = " + allData.size() + ", lty = 3)");
        }
        
        if (! reportsITS.isEmpty()) { //plot ITS
            int numTrainingEntries_ITS = reportsITS.get(0).getNumTrainingEntries();
            String rangeY_ITS = getRangeY(allData, reportsITS);
            String rangeX = getRangeX(allData, numForecasts);
            
            boolean next = false;
            for (TrainAndTestReport r : reportsITS) {
                if (next) {
                    rengine.eval("par(new=TRUE)");
                } else {
                    next = true;
                }

                StringBuilder plotCode = new StringBuilder(r.getFittedValuesPlotCode());
                plotCode.insert(r.getFittedValuesPlotCode().length() - 1, ", xlim = " + rangeX + ", ylim = " + rangeY_ITS + ", col=\"green\"");
                rengine.eval(plotCode.toString());
            }

            rengine.assign("all.data", Utils.listToArray(allData)); //TODO plot this as intervals! and plot it first, aby to nezakryvalo ten zbytok!
            rengine.eval("par(new=TRUE)");
            rengine.eval("plot.ts(all.data, xlim = " + rangeX + ", ylim = " + rangeY_ITS + ")");
            rengine.eval("abline(v = " + numTrainingEntries_ITS + ", lty = 3)"); //add a dashed vertical line to separate TRAIN and TEST
            rengine.eval("abline(v = " + allData.size() + ", lty = 3)");
        }

        MainFrame.gdCanvas.setSize(new Dimension(width, height)); //TODO nechce sa zmensit pod urcitu velkost, vymysliet
        MainFrame.gdCanvas.initRefresh();
    }
    
    public static void drawPlotITS_LBUB(int width, int height, List<Double> lowerBound, List<Double> upperBound) {
        final String LOWER = Const.INPUT + Utils.getCounter();
        final String UPPER = Const.INPUT + Utils.getCounter();
        final int count = lowerBound.size();
        
        Rengine rengine = MyRengine.getRengine();
        rengine.assign(LOWER, Utils.listToArray(lowerBound));
        rengine.assign(UPPER, Utils.listToArray(upperBound));
        
        drawPlotITSNow(width, height, LOWER, UPPER, count);
    }
    
    public static void drawPlotITS_CenterRadius(int width, int height, List<Double> center, List<Double> radius) {
        final String CENTER = Const.INPUT + Utils.getCounter();
        final String RADIUS = Const.INPUT + Utils.getCounter();
        final String LOWER = Const.INPUT + Utils.getCounter();
        final String UPPER = Const.INPUT + Utils.getCounter();
        final int count = center.size();
        
        Rengine rengine = MyRengine.getRengine();
        rengine.assign(CENTER, Utils.listToArray(center));
        rengine.assign(RADIUS, Utils.listToArray(radius));
        REXP getLower = rengine.eval(CENTER + " - " + RADIUS);
        REXP Upper = rengine.eval(CENTER + " + " + RADIUS);
        rengine.assign(LOWER, getLower);
        rengine.assign(UPPER, Upper);
        
        drawPlotITSNow(width, height, LOWER, UPPER, count);
    }
    
    private static void drawPlotITSNow(int width, int height, final String LOWER, final String UPPER, final int count) {
        Rengine rengine = MyRengine.getRengine();
        
        rengine.eval("require(JavaGD)");
        rengine.eval("JavaGD()");
        
        String ylim = "ylim=range(" + LOWER + "," + UPPER + ")";
        rengine.eval("plot.ts(" + LOWER + ", " + ylim + ", col=\"white\")"); //hack
        rengine.eval("par(new=TRUE)");
        rengine.eval("plot.ts(" + UPPER + ", " + ylim + ", col=\"white\")");
        rengine.eval("segments(1:" + count + ", " + LOWER + ", 1:" + count + ", " + UPPER + ", " + ylim + ", col=\"blue\")");
        
        MainFrame.gdCanvas.setSize(new Dimension(width, height));
        MainFrame.gdCanvas.initRefresh();
    }
    
    private List<Color> getNColours(int n) {
        //spocitat farby z HSV color space, resp. spocitat si prvych X (kde X je nejake rozumne cislo, napriklad 15), a potom
        //  pouzivat zase tych istych 15 farieb, akurat s inym typom ciary
        //ale pre menej ako 15 farieb vypocitat len N farieb, ktore budu od seba dost vzdialene
        return null;
    }
    
    //alebo tu su nejake farby: od zaciatku si z nich brat, a mali by byt vzdy dost vzdialene
    
//    private static final String[] indexcolors = new String[]{
//        "#000000", "#FFFF00", "#1CE6FF", "#FF34FF", "#FF4A46", "#008941", "#006FA6", "#A30059",
//        "#FFDBE5", "#7A4900", "#0000A6", "#63FFAC", "#B79762", "#004D43", "#8FB0FF", "#997D87",
//        "#5A0007", "#809693", "#FEFFE6", "#1B4400", "#4FC601", "#3B5DFF", "#4A3B53", "#FF2F80",
//        "#61615A", "#BA0900", "#6B7900", "#00C2A0", "#FFAA92", "#FF90C9", "#B903AA", "#D16100",
//        "#DDEFFF", "#000035", "#7B4F4B", "#A1C299", "#300018", "#0AA6D8", "#013349", "#00846F",
//        "#372101", "#FFB500", "#C2FFED", "#A079BF", "#CC0744", "#C0B9B2", "#C2FF99", "#001E09",
//        "#00489C", "#6F0062", "#0CBD66", "#EEC3FF", "#456D75", "#B77B68", "#7A87A1", "#788D66",
//        "#885578", "#FAD09F", "#FF8A9A", "#D157A0", "#BEC459", "#456648", "#0086ED", "#886F4C",
//
//        "#34362D", "#B4A8BD", "#00A6AA", "#452C2C", "#636375", "#A3C8C9", "#FF913F", "#938A81",
//        "#575329", "#00FECF", "#B05B6F", "#8CD0FF", "#3B9700", "#04F757", "#C8A1A1", "#1E6E00",
//        "#7900D7", "#A77500", "#6367A9", "#A05837", "#6B002C", "#772600", "#D790FF", "#9B9700",
//        "#549E79", "#FFF69F", "#201625", "#72418F", "#BC23FF", "#99ADC0", "#3A2465", "#922329",
//        "#5B4534", "#FDE8DC", "#404E55", "#0089A3", "#CB7E98", "#A4E804", "#324E72", "#6A3A4C",
//        "#83AB58", "#001C1E", "#D1F7CE", "#004B28", "#C8D0F6", "#A3A489", "#806C66", "#222800",
//        "#BF5650", "#E83000", "#66796D", "#DA007C", "#FF1A59", "#8ADBB4", "#1E0200", "#5B4E51",
//        "#C895C5", "#320033", "#FF6832", "#66E1D3", "#CFCDAC", "#D0AC94", "#7ED379", "#012C58"; }
    
    
    private static String getRangeY(List<Double> allData, List<TrainAndTestReport> reports) {
        StringBuilder rangesY = new StringBuilder("range(c(");
        boolean next = false;
        for (TrainAndTestReport r : reports) {
            if (next) {
                rangesY.append(", ");
            } else {
                next = true;
            }
            rangesY.append(Utils.minArray(r.getFittedValues())).append(", ");
            rangesY.append(Utils.maxArray(r.getFittedValues())).append(", ");
            rangesY.append(Utils.minArray(r.getForecastValues())).append(", ");
            rangesY.append(Utils.maxArray(r.getForecastValues()));
        }
        //a zahrnut aj povodne data:
        rangesY.append(", ").append(Utils.minList(allData)).append(", ").append(Utils.maxList(allData));
        rangesY.append("))");
        
        return rangesY.toString();
    }
    
    private static String getRangeX(List<Double> allData, int numForecasts) {
        return "range(c(0, " + (allData.size() + numForecasts) + "))";
    }
}
