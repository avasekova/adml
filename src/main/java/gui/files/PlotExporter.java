package gui.files;

import gui.filefilters.RFileFilter;
import utils.MyRengine;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class PlotExporter {

    public static String export(Component plotToExport) {
        JFileChooser fileChooser = new PlotExtensionFileChooser();
        switch (fileChooser.showSaveDialog(null)) {
            case JFileChooser.APPROVE_OPTION:
                File plotFile = fileChooser.getSelectedFile();
                MyRengine rengine = MyRengine.getRengine();

                String device = "";
                String ext = "";
                if (fileChooser.getFileFilter() instanceof RFileFilter) {
                    device = ((RFileFilter)fileChooser.getFileFilter()).getDevice();
                    ext = ((RFileFilter)fileChooser.getFileFilter()).getExtension();
                }

                String fileName = plotFile.getPath().replace("\\", "\\\\");
                if (fileName.contains(".") && (fileName.lastIndexOf('.') < (fileName.length()-1))) {
                    //tipnem si, ze je tam pripona
                    String extCurr = fileName.substring((fileName.lastIndexOf('.')+1), fileName.length()); //vezmem si priponu
                    if (extCurr.equals("eps") || extCurr.equals("ps") || extCurr.equals("png") || extCurr.equals("pdf")) {
                        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
                    } //else to bola nejaka ina cast mena za bodkou
                }

                rengine.eval("dev.print(" + device + ", file=\"" + fileName + "." + ext + "\", width=" +
                        plotToExport.getWidth() + ", height=" + plotToExport.getHeight() + ")");
                // rengine.eval("dev.off()"); //z nejakeho dovodu to "nerefreshuje" nasledujuce ploty, ked to vypnem.

                return fileName;
            case JFileChooser.CANCEL_OPTION:
            default:
                //nothing
                return null;
        }
    }
}
