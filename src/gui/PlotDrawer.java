package gui;

import java.awt.Dimension;
import java.util.List;
import models.TrainAndTestReport;
import org.rosuda.JRI.Rengine;
import utils.MyRengine;
import utils.Utils;

public class PlotDrawer {
    
    //TODO generovat i legendu do toho vysledneho grafu!
    public static void drawPlots(int width, int height, List<Double> allData, int numForecasts, List<TrainAndTestReport> reports) {
        //plot.ts(brent$Center, ylim=range(c(brent.center.nnet.4cast, brent$Center)), xlim=range(c(brent.center.nnet.4cast,brent$Center)), col="blue")
        if (reports.isEmpty()) {
            return;
        }
        
        Rengine rengine = MyRengine.getRengine();
        rengine.eval("require(JavaGD)");
        rengine.eval("JavaGD()");
        
        StringBuilder rangesY = new StringBuilder("range(c(");
        boolean next = false;
        for (TrainAndTestReport r : reports) {
            if (next) {
                rangesY.append(", ");
            } else {
                next = true;
            }
            rangesY.append(Utils.minArray(r.getFittedValues())).append(", ");
            rangesY.append(Utils.maxArray(r.getFittedValues()));
        }
        //a zahrnut aj povodne data:
        rangesY.append(", ").append(Utils.minList(allData)).append(", ").append(Utils.maxList(allData));
        rangesY.append("))");
        
        String rangesX = "range(c(0, " + (allData.size() + numForecasts) + "))";

        //TODO colours!
        next = false;
        for (TrainAndTestReport r : reports) {
            if (next) {
                rengine.eval("par(new=TRUE)");
            } else {
                next = true;
            }
            
            StringBuilder plotCode = new StringBuilder(r.getFittedValuesPlotCode());
            plotCode.insert(r.getFittedValuesPlotCode().length() - 1, ", xlim = " + rangesX + ", ylim = " + rangesY + ", col=\"blue\"");
            //TODO neskor zarovnavat forecasty doprava, pretoze niekto tam zobrazuje aj realne data a niekto nie, ALEBO zobrazit u vsetkych aj realne data aj forecast
            rengine.eval(plotCode.toString());
        }
        
        rengine.assign("all.data", Utils.listToArray(allData));
        rengine.eval("par(new=TRUE)");
        rengine.eval("plot.ts(all.data, xlim = " + rangesX + ", ylim = " + rangesY + ")");

        MainFrame.gdCanvas.setSize(new Dimension(width, height)); //TODO nechce sa zmensit pod urcitu velkost, vymysliet
        MainFrame.gdCanvas.initRefresh();
            
    }
    
}
