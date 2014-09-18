package gui;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import models.TrainAndTestReport;
import org.rosuda.JRI.Rengine;
import rcaller.RCaller;
import rcaller.RCode;
import utils.Const;
import utils.MyRengine;

public class PlotDrawer {
    
    public static ImageIcon drawPlots(List<TrainAndTestReport> reports) {
        //plot.ts(brent$Center, ylim=range(c(brent.center.nnet.4cast, brent$Center)), xlim=range(c(brent.center.nnet.4cast,brent$Center)), col="blue")
        if (reports.isEmpty()) {
            return null;
        }
        
        try {
            Rengine rengine = MyRengine.getRengine();
            /*
            REXP abc = re.eval("seq(1,10)");
            re.assign("a", abc);
            REXP def = re.eval("a + 3");
            double[] result = def.asDoubleArray();
            */
            RCaller caller = new RCaller();
            caller.setRExecutable(Const.REXECUTABLE);
            caller.setRscriptExecutable(Const.RSCRIPT_EXE);
            
            RCode code = new RCode();
            
            
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
