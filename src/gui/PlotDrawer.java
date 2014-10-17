package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import org.rosuda.javaGD.GDCanvas;
import utils.Const;
import utils.MyRengine;
import utils.Utils;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;

//TODO preco po vykresleni grafu ten obrazok blikne? niekde sa nieco kresli dvakrat.
public class PlotDrawer {
    //TODO pridat abline pre kazde oddelenie training a testing data! ptz rozne metody maju rozne pomery.
    //  (alebo nejak inak odlisit. trosku ina farba? iny styl ciary? ina hrubka? vsetko sa mi zda zle.)
    
    private static final int COLUMNS_DIAGRAMSNN = 3;
    
    public static void drawPlots(GDCanvas canvasToUse, int width, int height, List<Double> allDataCTS, int numForecasts,
                                 List<TrainAndTestReportCrisp> reportsCTS, List<TrainAndTestReportInterval> reportsITS,
                                 int from, int to, String colname_CTS) { //the range of data that is considered
        if (reportsCTS.isEmpty() && reportsITS.isEmpty()) {
            return;
        }
        
        MainFrame.drawNowToThisGDCanvas = canvasToUse;
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(JavaGD)");
        rengine.eval("JavaGD()");
        
        if ((! reportsCTS.isEmpty()) && (! reportsITS.isEmpty())) { //budem vykreslovat oba naraz
            rengine.eval("par(mfrow=c(1,2))"); //daj dva grafy vedla seba. potom normalne zavolat dva ploty.
        }
        
        int colourNumber = 0;
        if (! reportsCTS.isEmpty()) { //plot CTS
            int numTrainingEntries_CTS = reportsCTS.get(0).getNumTrainingEntries();
            allDataCTS = allDataCTS.subList(from, to);
            String rangeY_CTS = getRangeYCrisp(allDataCTS, reportsCTS);
            String rangeX = getRangeXCrisp(allDataCTS, numForecasts);
            
            List<String> names = new ArrayList<>();
            List<String> colours = new ArrayList<>();
            boolean next = false;
            for (TrainAndTestReportCrisp r : reportsCTS) {
                if (next) {
                    rengine.eval("par(new=TRUE)");
                } else {
                    next = true;
                }

                //remember these for the legend
                names.add(r.getModelName());
                colours.add(COLOURS[colourNumber]);
                
                StringBuilder plotCode = new StringBuilder(r.getPlotCode());
                plotCode.insert(r.getPlotCode().length() - 1, ", xlim = " + rangeX + ", ylim = " + rangeY_CTS + ", "
                        + "axes=FALSE, ann=FALSE, " //suppress axes names and labels, just draw them for the main data
                        + "lwd=4, col=\"" + COLOURS[colourNumber] + "\"");
                rengine.eval(plotCode.toString());
                colourNumber++;
            }
            
            rengine.assign("all.data", Utils.listToArray(allDataCTS));
            rengine.eval("par(new=TRUE)");
            rengine.eval("plot.ts(all.data, xlim = " + rangeX + ", ylim = " + rangeY_CTS + ", "
                    + "ylab=\"" + colname_CTS + "\","
                    + "lwd=2, col=\"#444444\")");
            rengine.eval("abline(v = " + numTrainingEntries_CTS + ", lty = 3)"); //add a dashed vertical line to separate TRAIN and TEST
            rengine.eval("abline(v = " + allDataCTS.size() + ", lty = 3)");
            
            //add legend
            rengine.eval("legend(\"topleft\", "      
                                + "inset = c(0,-0.11), "
                                + "legend = " + getRString(names) + ", "
                                + "fill = " + getRString(colours) + ", "
                                + "horiz = TRUE, "
                                + "box.lty = 0, "
                                + "cex = 0.8, "
                                + "text.width = 3, " //TODO pohrat sa s tymto, a urobit to nejak univerzalne, aby tam vzdy vosli vsetky nazvy
                                + "xpd = TRUE)");
            
        }
        
        if (! reportsITS.isEmpty()) { //plot ITS
            //first go through all the reports to find a common rangeX and rangeY for the one big plot:
            int numTrainingEntries_ITS = reportsITS.get(reportsITS.size() - 1).getNumTrainingEntries(); //hack, pouzivam posledny report, pretoze ked pustim iMLP (C code) a MLP(i), tak iMLP ma menej entries...
            //final int NUM_REAL_ENTRIES = reportsITS.get(reportsITS.size() - 1).getRealValuesLowers().size();
            String rangeY_ITS = getRangeYInterval(reportsITS);
            String rangeX_ITS = getRangeXInterval(reportsITS, numForecasts);
            
            //the plot all reports, the underlying data first! and then the fitted and forecasted vals:
            List<String> names = new ArrayList<>();
            List<String> colours = new ArrayList<>();
            boolean next = false;
            for (TrainAndTestReportInterval r : reportsITS) {
                if (next) {
                    rengine.eval("par(new=TRUE)");
                } else {
                    next = true;
                }
                
                //remember these for the legend
                names.add(r.getModelName());
                colours.add(COLOURS[colourNumber]);
                
                //naplotovat fitted values:
                final int sizeFitted = r.getFittedValues().size();
                rengine.assign("lower", r.getFittedValuesLowers());
                rengine.assign("upper", r.getFittedValuesUppers());
                rengine.eval("plot.ts(lower, type=\"n\", xlim = " + rangeX_ITS + ", ylim = " + rangeY_ITS + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                rengine.eval("par(new=TRUE)");
                rengine.eval("plot.ts(upper, type=\"n\", xlim = " + rangeX_ITS + ", ylim = " + rangeY_ITS + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                rengine.eval("segments(1:" + sizeFitted + ", lower, 1:" + sizeFitted + ", upper, xlim = "
                        + rangeX_ITS + ", ylim = " + rangeY_ITS + ", lwd=4, col=\"" + COLOURS[colourNumber] + "\")");
                
                //naplotovat fitted values pre training data:
                final int sizeForecastTest = r.getForecastValuesTest().size();
                rengine.eval("par(new=TRUE)");
                rengine.assign("lower", r.getForecastValuesTestLowers());
                rengine.assign("upper", r.getForecastValuesTestUppers());
                rengine.eval("plot.ts(lower, type=\"n\", xlim = " + rangeX_ITS + ", ylim = " + rangeY_ITS + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                rengine.eval("par(new=TRUE)");
                rengine.eval("plot.ts(upper, type=\"n\", xlim = " + rangeX_ITS + ", ylim = " + rangeY_ITS + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                rengine.eval("segments(" + (sizeFitted+1) + ":" + (sizeFitted+sizeForecastTest) + ", lower, "
                        + (sizeFitted+1) + ":" + (sizeFitted+sizeForecastTest) + ", upper, xlim = " + rangeX_ITS
                        + ", ylim = " + rangeY_ITS + ", lwd=4, col=\"" + COLOURS[colourNumber] + "\")");
                
                //naplotovat forecasty buduce:
                final int sizeForecastFuture = r.getForecastValuesFuture().size();
                rengine.eval("par(new=TRUE)");
                rengine.assign("lower", r.getForecastValuesFutureLowers());
                rengine.assign("upper", r.getForecastValuesFutureUppers());
                rengine.eval("plot.ts(lower, type=\"n\", xlim = " + rangeX_ITS + ", ylim = " + rangeY_ITS + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                rengine.eval("par(new=TRUE)");
                rengine.eval("plot.ts(upper, type=\"n\", xlim = " + rangeX_ITS + ", ylim = " + rangeY_ITS + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels, just draw them for the main data
                rengine.eval("segments(" + (sizeFitted+sizeForecastTest+1) + ":"
                        + (sizeFitted+sizeForecastTest+sizeForecastFuture) + ", lower, "
                        + (sizeFitted+sizeForecastTest+1) + ":" + (sizeFitted+sizeForecastTest+sizeForecastFuture)
                        + ", upper, xlim = " + rangeX_ITS + ", ylim = " + rangeY_ITS
                        + ", lwd=4, col=\"" + COLOURS[colourNumber] + "\")");
                
                colourNumber++;
            }
            
            rengine.eval("par(new=TRUE)");
                
            //a na ne vsetky naplotovat realne data:
            //TODO hack, zatial beriem data z prveho reportu. potom nejak vymysliet :(
            int size = reportsITS.get(reportsITS.size() - 1).getRealValuesLowers().size();
            //tu ich uz nesublistuj! v realData v reporte je to uz orezane podla range zadaneho na vstupe
            rengine.assign("all.lower", Utils.listToArray(reportsITS.get(reportsITS.size() - 1).getRealValuesLowers()));
            rengine.assign("all.upper", Utils.listToArray(reportsITS.get(reportsITS.size() - 1).getRealValuesUppers()));

            //TODO este sa pohrat s tymi "range" hodnotami, lebo mi to nejak divne zarovnava
            rengine.eval("plot.ts(all.lower, type=\"n\", xlim = " + rangeX_ITS + ", ylim = " + rangeY_ITS + ", "
                        + "axes=FALSE, ann=FALSE)"); //suppress axes names and labels
            rengine.eval("par(new=TRUE)");
            rengine.eval("plot.ts(all.upper, type=\"n\", xlim = " + rangeX_ITS + ", ylim = " + rangeY_ITS + ", "
                    + "ylab=\"" +       "<<add the interval.toString() here>>"      + "\")");
            rengine.eval("segments(1:" + size + ", all.lower, 1:" + size + ", all.upper, xlim = " + rangeX_ITS + ", ylim = " + rangeY_ITS + ", lwd=2, col=\"#444444\")");
            
            rengine.eval("abline(v = " + numTrainingEntries_ITS + ", lty = 3)"); //add a dashed vertical line to separate TRAIN and TEST
            //TODO potom tam dat oznacenie na vsetky ablines:     rengine.eval("axis(1, at=" + numTrainingEntries_ITS + ", labels = " + numTrainingEntries_ITS + ")");
            //TODO asi bude treba dat viacero takychto ciar - pre kazdy report jednu, lebo percentTrain sa lisi
            
            rengine.eval("abline(v = " + size + ", lty = 3)");
            
            //add legend
            rengine.eval("legend(\"topleft\", "      
                                + "inset = c(0,-0.11), "
                                + "legend = " + getRString(names) + ", "
                                + "fill = " + getRString(colours) + ", "
                                + "horiz = TRUE, "
                                + "box.lty = 0, "
                                + "cex = 0.8, "
                                + "text.width = 3, " //TODO pohrat sa s tymto, a urobit to nejak univerzalne, aby tam vzdy vosli vsetky nazvy
                                + "xpd = TRUE)");
        }

        MainFrame.drawNowToThisGDCanvas.setSize(new Dimension(width, height)); //TODO nechce sa zmensit pod urcitu velkost, vymysliet
        MainFrame.drawNowToThisGDCanvas.initRefresh();
    }
    
    public static void drawPlotITS(GDCanvas canvasToUse, int width, int height, DataTableModel dataTableModel,
                List<IntervalNamesCentreRadius> listCentreRadius, List<IntervalNamesLowerUpper> listLowerUpper) {
        MainFrame.drawNowToThisGDCanvas = canvasToUse;
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(JavaGD)");
        rengine.eval("JavaGD()");
        
        List<String> names = new ArrayList<>();
        List<String> colours = new ArrayList<>();
        int colourNumber = 0;
        
        
        boolean next = false;
        for (IntervalNamesCentreRadius interval : listCentreRadius) {
            //remember these for the legend
            names.add(interval.toString());
            colours.add(COLOURS[colourNumber]);
            
            String lineStyle = ", lwd=4, col=\"" + COLOURS[colourNumber] + "\"";
            drawPlotITS_CenterRadius(width, height, dataTableModel.getDataForColname(interval.getCentre()),
                    dataTableModel.getDataForColname(interval.getRadius()), next, lineStyle);
            if (! next) {
                next = true;
            }
            
            colourNumber++;
        }
        
        next = (! listCentreRadius.isEmpty()) && (! listLowerUpper.isEmpty()); //true ak je nieco v CenRad aj v LBUB
        
        for (IntervalNamesLowerUpper interval : listLowerUpper) {
            names.add(interval.toString());
            colours.add(COLOURS[colourNumber]);
            
            String lineStyle = ", lwd=4, col=\"" + COLOURS[colourNumber] + "\"";
            drawPlotITS_LBUB(width, height, dataTableModel.getDataForColname(interval.getLowerBound()),
                    dataTableModel.getDataForColname(interval.getUpperBound()), next, lineStyle);
            if (! next) {
                next = true;
            }
            
            colourNumber++;
        }
        
        if ((! listCentreRadius.isEmpty()) || (! listLowerUpper.isEmpty())) {
            //add legend
            rengine.eval("legend(\"topleft\", "      
                                + "inset = c(0,-0.11), "
                                + "legend = " + getRString(names) + ", "
                                + "fill = " + getRString(colours) + ", "
                                + "horiz = TRUE, "
                                + "box.lty = 0, "
                                + "cex = 0.8, "
                                + "text.width = 15, " //TODO pohrat sa s tymto, a urobit to nejak univerzalne, aby tam vzdy vosli vsetky nazvy
                                + "xpd = TRUE)");
        }
    }
    
    private static void drawPlotITS_LBUB(int width, int height, List<Double> lowerBound, List<Double> upperBound,
            boolean par, String lineStyle) {
        final String LOWER = Const.INPUT + Utils.getCounter();
        final String UPPER = Const.INPUT + Utils.getCounter();
        final int count = lowerBound.size();
        
        Rengine rengine = MyRengine.getRengine();
        rengine.assign(LOWER, Utils.listToArray(lowerBound));
        rengine.assign(UPPER, Utils.listToArray(upperBound));
        
        drawPlotITSNow(width, height, LOWER, UPPER, count, par, lineStyle);
    }
    
    private static void drawPlotITS_CenterRadius(int width, int height, List<Double> center, List<Double> radius,
            boolean par, String lineStyle) {
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
        
        drawPlotITSNow(width, height, LOWER, UPPER, count, par, lineStyle);
    }
    
    private static void drawPlotITSNow(int width, int height, final String LOWER, final String UPPER, final int count,
            boolean par, String lineStyle) {
        Rengine rengine = MyRengine.getRengine();
        
        if (par) { //continue from the previous plot
            rengine.eval("par(new=TRUE)");
            String ylim = "ylim=range(" + LOWER + "," + UPPER + ")";
            rengine.eval("plot.ts(" + LOWER + ", " + ylim + ", col=\"white\", axes=FALSE, ann=FALSE)"); //hack
            rengine.eval("par(new=TRUE)");
            rengine.eval("plot.ts(" + UPPER + ", " + ylim + ", col=\"white\", ylab=\"" +      "<<TODO ylab>>"      + "\")");
            rengine.eval("segments(1:" + count + ", " + LOWER + ", 1:" + count + ", " + UPPER + ", " + ylim + lineStyle + ")");
        } else { //start a new plot
            rengine.eval("require(JavaGD)");
            rengine.eval("JavaGD()"); // zacne novy plot
            String ylim = "ylim=range(" + LOWER + "," + UPPER + ")";
            //dont draw axes
            rengine.eval("plot.ts(" + LOWER + ", " + ylim + ", col=\"white\", axes=FALSE, ann=FALSE)"); //hack
            rengine.eval("par(new=TRUE)");
            //here either
            rengine.eval("plot.ts(" + UPPER + ", " + ylim + ", col=\"white\", axes=FALSE, ann=FALSE)");
            rengine.eval("segments(1:" + count + ", " + LOWER + ", 1:" + count + ", " + UPPER + ", " + ylim + lineStyle + ")");
        }
        
        
        
        MainFrame.drawNowToThisGDCanvas.setSize(new Dimension(width, height));
        MainFrame.drawNowToThisGDCanvas.initRefresh();
    }
    
    private List<Color> getNColours(int n) {
        //spocitat farby z HSV color space, resp. spocitat si prvych X (kde X je nejake rozumne cislo, napriklad 15), a potom
        //  pouzivat zase tych istych 15 farieb, akurat s inym typom ciary
        //ale pre menej ako 15 farieb vypocitat len N farieb, ktore budu od seba dost vzdialene
        return null;
    }
    
    //alebo tu su nejake farby: od zaciatku si z nich brat, a mali by byt vzdy dost vzdialene
    
    public static final String[] COLOURS = new String[]{ //TODO vybrat sem nejake pekne! (rucne) - a hlavne viac
        "magenta",
        "blue",
        "green3",
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
            rangesY.append(Utils.minArray(r.getForecastValuesTest())).append(", ");
            rangesY.append(Utils.maxArray(r.getForecastValuesTest()));
            if (r.getForecastValuesFuture().length != 0) {
                rangesY.append(", ");
                rangesY.append(Utils.minArray(r.getForecastValuesFuture())).append(", ");
                rangesY.append(Utils.maxArray(r.getForecastValuesFuture()));
            }
        }
        //a zahrnut aj povodne data:
        rangesY.append(", ").append(Utils.minList(allData)).append(", ").append(Utils.maxList(allData));
        rangesY.append("))");
        
        return rangesY.toString();
    }
    
    private static String getRangeYInterval(List<TrainAndTestReportInterval> reports) {
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
            rangesY.append(Utils.minArray(r.getForecastValuesTestLowers())).append(", ");
            rangesY.append(Utils.maxArray(r.getForecastValuesTestLowers())).append(", ");
            rangesY.append(Utils.minArray(r.getForecastValuesTestUppers())).append(", ");
            rangesY.append(Utils.maxArray(r.getForecastValuesTestUppers()));
            if (! r.getForecastValuesFuture().isEmpty()) {
                rangesY.append(", ");
                rangesY.append(Utils.minArray(r.getForecastValuesFutureLowers())).append(", ");
                rangesY.append(Utils.maxArray(r.getForecastValuesFutureLowers())).append(", ");
                rangesY.append(Utils.minArray(r.getForecastValuesFutureUppers())).append(", ");
                rangesY.append(Utils.maxArray(r.getForecastValuesFutureUppers()));
            }
            
            //a zahrnut aj povodne data:
            List<Double> realDataLower = r.getRealValuesLowers();
            List<Double> realDataUpper = r.getRealValuesUppers();
            rangesY.append(", ").append(Utils.minList(realDataLower)).append(", ").append(Utils.maxList(realDataLower));
            rangesY.append(", ").append(Utils.minList(realDataUpper)).append(", ").append(Utils.maxList(realDataUpper));
        }
        
        rangesY.append("))");
        
        return rangesY.toString();
    }
    
    private static String getRangeXCrisp(List<Double> allData, int numForecasts) {
        return "range(c(0, " + (allData.size() + numForecasts) + "))";
    }
    
    private static String getRangeXInterval(List<TrainAndTestReportInterval> reports, int numForecasts) {
        StringBuilder rangesX = new StringBuilder("range(c(0, ");
        boolean next = false;
        for (TrainAndTestReportInterval r : reports) {
            if (next) {
                rangesX.append(", ");
            } else {
                next = true;
            }
            
            rangesX.append((r.getRealValuesLowers().size() + numForecasts));
        }
        
        rangesX.append("))");
        
        return rangesX.toString();
    }
    
    public static String getRString(List<String> list) {
        StringBuilder rString = new StringBuilder();
        rString.append("c(");
        boolean next = false;
        for (String s : list) {
            if (next) {
                rString.append(", ");
            } else {
                next = true;
            }
            rString.append("\"").append(s).append("\"");
        }
        rString.append(")");
        return rString.toString();
    }
    
    public static void drawDiagrams(GDCanvas canvasToUse, int width, int height, List<TrainAndTestReport> reports) {
        if (reports.isEmpty()) {
            return;
        }
        
        MainFrame.drawNowToThisGDCanvas = canvasToUse;
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(JavaGD)");
        rengine.eval("JavaGD()");
        
        List<String> diagramPlots = new ArrayList<>();
        for (TrainAndTestReport r : reports) {
            if (! "".equals(r.getNnDiagramPlotCode())) {
                diagramPlots.add(r.getNnDiagramPlotCode());
            }
        }
        
        if (! diagramPlots.isEmpty()) {
            int rows = diagramPlots.size()/COLUMNS_DIAGRAMSNN + 1;
            rengine.eval("par(mfrow=c(" + rows + "," + COLUMNS_DIAGRAMSNN + "))"); //narobim si mriezku
            
            //a teraz idem postupne vyplnat mriezku diagramami:
            for (String dPlot : diagramPlots) {
                rengine.eval(dPlot);
            }
            
            //nakoniec doplnim prazdne policka do mriezky plotov, lebo to na ne caka a mohlo by to nerobit dobrotu
            for (int i = 0; i < (rows*COLUMNS_DIAGRAMSNN)-diagramPlots.size(); i++) { //kolko mam este nevyplnenych
                rengine.eval("plot.new()");
            }
        }
        
        MainFrame.drawNowToThisGDCanvas.setSize(new Dimension(width, height));
        MainFrame.drawNowToThisGDCanvas.initRefresh();
    }
    
//    private static String getStrWidth(List<String> list) {
//        StringBuilder rString = new StringBuilder();
//        rString.append("max(c(");
//        boolean next = false;
//        for (String s : list) {
//            if (next) {
//                rString.append(", ");
//            } else {
//                next = true;
//            }
//            rString.append("strwidth(\"").append(s).append("\")");
//        }
//        rString.append("))");
//        return rString.toString();
//    }
}
