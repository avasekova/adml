package gui;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import models.TrainAndTestReport;
import rcaller.RCaller;
import rcaller.RCode;
import utils.Utils;

public class PlotDrawer {
    
    public static ImageIcon drawPlots(List<TrainAndTestReport> reports) {
        //plot.ts(brent$Center, ylim=range(c(brent.center.nnet.4cast, brent$Center)), xlim=range(c(brent.center.nnet.4cast,brent$Center)), col="blue")
        if (reports.isEmpty()) {
            return null;
        }
        
        try {
            RCaller caller = Utils.getCleanRCaller();
            caller.deleteTempFiles();
            
            RCode code = new RCode();
            code.clear();
            
            File forecastPlotFile = code.startPlot();
            
            boolean next = false;
            for (TrainAndTestReport r : reports) {
                if (next) {
                    code.addRCode("par(new=TRUE)");
                } else {
                    next = true;
                }
//                String plotCode = r.getForecastPlotCode().substring(0, r.getForecastPlotCode().length() - 2);
//                plotCode += "ylim=range(c(brent.center.nnet.4cast, brent$Center)), xlim=range(c(brent.center.nnet.4cast,brent$Center)), col="red")"
                code.addRCode(r.getForecastPlotCode());
                //ylim=range(c(brent.center.nnet.4cast, brent$Center)), xlim=range(c(brent.center.nnet.4cast,brent$Center))
            }
            
            code.endPlot();
            caller.setRCode(code);
            caller.runOnly();
            return code.getPlot(forecastPlotFile);
            
        } catch (IOException ex) {
            Logger.getLogger(PlotDrawer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
}
