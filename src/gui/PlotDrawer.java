package gui;

import java.awt.Dimension;
import java.util.List;
import models.TrainAndTestReport;
import org.rosuda.JRI.Rengine;
import utils.MyRengine;

public class PlotDrawer {
    
    public static void drawPlots(int width, int height, List<TrainAndTestReport> reports) {
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
            rangesY.append(r.getRangeMin()).append(", ");
            rangesY.append(r.getRangeMax());
        }
        rangesY.append("))");

        //TODO colours!
        next = false;
        for (TrainAndTestReport r : reports) {
            if (next) {
                rengine.eval("par(new=TRUE)");
            } else {
                next = true;
            }
            
            StringBuilder plotCode = new StringBuilder(r.getForecastPlotCode());
            plotCode.insert(r.getForecastPlotCode().length() - 1, ", xlim = range(0, " + (r.getTrainData().size() + r.getForecastData().size()) + "), ylim = " + rangesY.toString());
            //TODO opravit ten xlim: (r.getTrainData().size() + r.getForecastData().size()) je zle. vypocitavat to podobne ako range
            //TODO neskor zarovnavat forecasty doprava, pretoze niekto tam zobrazuje aj realne data a niekto nie, ALEBO zobrazit u vsetkych aj realne data aj forecast
            rengine.eval(plotCode.toString());
        }

        MainFrame.gdCanvas.setSize(new Dimension(width, height)); //TODO nechce sa zmensit pod urcitu velkost, vymysliet
        MainFrame.gdCanvas.initRefresh();
            
    }
    
}
