package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import utils.Const;
import utils.MyRengine;
import utils.Utils;

public class PlotDrawer {
    
    //TODO generovat i legendu do toho vysledneho grafu!
    public static void drawPlots(int width, int height, List<Double> allDataCTS, boolean isCenterRadiusITS, 
                                 List<Double> firstITS, List<Double> secondITS, int numForecasts,
                                 List<TrainAndTestReportCrisp> reportsCTS, List<TrainAndTestReportInterval> reportsITS) {
        if (reportsCTS.isEmpty() && reportsITS.isEmpty()) {
            return;
        }
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(JavaGD)");
        rengine.eval("JavaGD()");
        
        if ((! reportsCTS.isEmpty()) && (! reportsITS.isEmpty())) { //budem vykreslovat oba naraz
            rengine.eval("par(mfrow=c(1,2))"); //daj dva grafy vedla seba. potom normalne zavolat dva ploty.
        }
        
        int colourNumber = 0;
        if (! reportsCTS.isEmpty()) { //plot CTS
            int numTrainingEntries_CTS = reportsCTS.get(0).getNumTrainingEntries();
            String rangeY_CTS = getRangeYCrisp(allDataCTS, reportsCTS);
            String rangeX = getRangeX(allDataCTS, numForecasts);
            
            boolean next = false;
            for (TrainAndTestReport r : reportsCTS) {
                if (next) {
                    rengine.eval("par(new=TRUE)");
                } else {
                    next = true;
                }

                StringBuilder plotCode = new StringBuilder(r.getFittedValuesPlotCode());
                plotCode.insert(r.getFittedValuesPlotCode().length() - 1, ", xlim = " + rangeX + ", ylim = " + rangeY_CTS + ", col=\"" + COLOURS[colourNumber] + "\"");
                rengine.eval(plotCode.toString());
                colourNumber++;
            }

            rengine.assign("all.data", Utils.listToArray(allDataCTS));
            rengine.eval("par(new=TRUE)");
            rengine.eval("plot.ts(all.data, xlim = " + rangeX + ", ylim = " + rangeY_CTS + ")");
            rengine.eval("abline(v = " + numTrainingEntries_CTS + ", lty = 3)"); //add a dashed vertical line to separate TRAIN and TEST
            rengine.eval("abline(v = " + allDataCTS.size() + ", lty = 3)");
        }
        
        if (! reportsITS.isEmpty()) { //plot ITS
            List<Double> lowerITS;
            List<Double> upperITS;
            if (isCenterRadiusITS) {
                List<Double> lowersUppers = Utils.getLowersUppersFromCentersRadii(firstITS, secondITS);
                lowerITS = lowersUppers.subList(0, lowersUppers.size()/2);
                upperITS = lowersUppers.subList(lowersUppers.size()/2, lowersUppers.size());
            } else {
                lowerITS = firstITS;
                upperITS = secondITS;
            }
            
            int numTrainingEntries_ITS = reportsITS.get(0).getNumTrainingEntries();
            String rangeY_ITS_lower = getRangeYInterval(lowerITS, reportsITS); //TODO change this! to something reasonable
            String rangeY_ITS_upper = getRangeYInterval(upperITS, reportsITS);
            String rangeY_ITS = "range(" + rangeY_ITS_lower + ", " + rangeY_ITS_upper + ")";
            String rangeX_lower = getRangeX(lowerITS, numForecasts);
            String rangeX_upper = getRangeX(upperITS, numForecasts);
            String rangeX = "range(" + rangeX_lower + ", " + rangeX_upper + ")";
            
            boolean next = false;
            for (TrainAndTestReportInterval r : reportsITS) {
                if (next) {
                    rengine.eval("par(new=TRUE)");
                } else {
                    next = true;
                }

                //TODO neskor plotovat samozrejme aj forecast values!
                rengine.assign("lower", r.getFittedValuesLowers());
                rengine.assign("upper", r.getFittedValuesUppers());
                rengine.eval("plot.ts(lower, xlim = " + rangeX + ", ylim = " + rangeY_ITS + ", col=\"white\")");
                rengine.eval("par(new=TRUE)");
                rengine.eval("plot.ts(upper, xlim = " + rangeX + ", ylim = " + rangeY_ITS + ", col=\"white\")");
                rengine.eval("segments(1:" + lowerITS.size() + ", lower, 1:" + lowerITS.size() + ", upper, xlim = " + rangeX + ", ylim = " + rangeY_ITS + ", col=\"" + COLOURS[colourNumber] + "\")");
                
                colourNumber++;
            }

            rengine.assign("all.lower", Utils.listToArray(lowerITS));
            rengine.assign("all.upper", Utils.listToArray(upperITS));
            
            //TODO este sa pohrat s tymi "range" hodnotami, lebo mi to nejak divne zarovnava
            rengine.eval("par(new=TRUE)");
            rengine.eval("plot.ts(all.lower, xlim = " + rangeX + ", ylim = " + rangeY_ITS + ", col=\"white\")");
            rengine.eval("par(new=TRUE)");
            rengine.eval("plot.ts(all.upper, xlim = " + rangeX + ", ylim = " + rangeY_ITS + ", col=\"white\")");
            rengine.eval("segments(1:" + lowerITS.size() + ", all.lower, 1:" + lowerITS.size() + ", all.upper, xlim = " + rangeX + ", ylim = " + rangeY_ITS + ", col=\"black\")");
            
            rengine.eval("abline(v = " + numTrainingEntries_ITS + ", lty = 3)"); //add a dashed vertical line to separate TRAIN and TEST
            rengine.eval("abline(v = " + lowerITS.size() + ", lty = 3)");
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
    
    private static final String[] COLOURS = new String[]{ //TODO vybrat sem nejake pekne! (rucne) - a hlavne viac
        "#42E99D", //tyrkysova
        "#FF7A4B", //oranzova
        "#EA0D5B", //ruzova
        "#1B60C5", //modra
        "#FFEA48", //zlta
        "#44F04F", //zelena
        "#820000", //hneda
        "#05787E" //ocelova
    };
    
    
    private static String getRangeYCrisp(List<Double> allData, List<TrainAndTestReportCrisp> reports) {
        StringBuilder rangesY = new StringBuilder("range(c(");
        boolean next = false;
        for (TrainAndTestReportCrisp r : reports) {
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
    
    //TODO potom vymysliet menej nechutne :/ mohlo by sa s tym dat pracovat jednotne
    private static String getRangeYInterval(List<Double> allData, List<TrainAndTestReportInterval> reports) {
        StringBuilder rangesY = new StringBuilder("range(c(");
        boolean next = false;
        for (TrainAndTestReportInterval r : reports) {
            if (next) {
                rangesY.append(", ");
            } else {
                next = true;
            }
            rangesY.append(Utils.minArray(r.getFittedValuesLowers())).append(", ");
            rangesY.append(Utils.maxArray(r.getFittedValuesLowers())).append(", ");
            rangesY.append(Utils.minArray(r.getFittedValuesUppers())).append(", ");
            rangesY.append(Utils.maxArray(r.getFittedValuesUppers())).append(", ");
            rangesY.append(Utils.minArray(r.getForecastValuesLowers())).append(", ");
            rangesY.append(Utils.maxArray(r.getForecastValuesLowers())).append(", ");
            rangesY.append(Utils.minArray(r.getForecastValuesUppers())).append(", ");
            rangesY.append(Utils.maxArray(r.getForecastValuesUppers()));
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
