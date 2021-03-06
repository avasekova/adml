package gui.files;

import gui.filefilters.RFileFilter;
import gui.tablemodels.ReportsTableModel;
import utils.ExcelWriter;
import utils.MyRengine;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Exporter {

    public static String exportPlot(Component plotToExport) { //TODO export all in case of tabbedPane (maybe a flag to plotting methods, and then redrawPlots)
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
                    //guess it has an extension
                    String extCurr = fileName.substring((fileName.lastIndexOf('.')+1), fileName.length()); //take the ext
                    if (extCurr.equals("eps") || extCurr.equals("ps") || extCurr.equals("png") || extCurr.equals("pdf")) {
                        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
                    } //else it was a different part of name after the dot
                }

                rengine.eval("dev.print(" + device + ", file=\"" + fileName + "." + ext + "\", width=" +
                        plotToExport.getWidth() + ", height=" + plotToExport.getHeight() + ")");

                return fileName;
            case JFileChooser.CANCEL_OPTION:
            default:
                //nothing
                return null;
        }
    }

    public static void exportValues(String title, String fileName, ReportsTableModel model) {
        JFileChooser fileChooser = new OverwriteFileChooser(fileName);
        switch (fileChooser.showSaveDialog(null)) {
            case JFileChooser.APPROVE_OPTION:
                File file = fileChooser.getSelectedFile();
                ExcelWriter.jTableToExcel(model, file, title);
                break;
            case JFileChooser.CANCEL_OPTION:
            default:
                //nothing
        }
    }
}
