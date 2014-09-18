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
        rengine.eval("library(JavaGD)");
        rengine.eval("JavaGD()");

        boolean next = false;
        for (TrainAndTestReport r : reports) {
            if (next) {
                rengine.eval("par(new=TRUE)");
            } else {
                next = true;
            }
//                String plotCode = r.getForecastPlotCode().substring(0, r.getForecastPlotCode().length() - 2);
//                plotCode += "ylim=range(c(brent.center.nnet.4cast, brent$Center)), xlim=range(c(brent.center.nnet.4cast,brent$Center)), col="red")"
            rengine.eval(r.getForecastPlotCode()); //TODO scale the axes
            //ylim=range(c(brent.center.nnet.4cast, brent$Center)), xlim=range(c(brent.center.nnet.4cast,brent$Center))
        }

        MainFrame.gdCanvas.setSize(new Dimension(width, height)); //TODO nechce sa zmensit pod urcitu velkost, vymysliet
        MainFrame.gdCanvas.initRefresh();
            
    }
    
}
