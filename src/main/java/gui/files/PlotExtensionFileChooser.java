package gui.files;

import gui.filefilters.*;

import java.io.File;

public class PlotExtensionFileChooser extends OverwriteFileChooser {

    public PlotExtensionFileChooser() {
        super("plotExport.eps");

        setAcceptAllFileFilterUsed(false); //do not allow "All files"
        addChoosableFileFilter(new FileFilterEps());
        addChoosableFileFilter(new FileFilterPs());
        addChoosableFileFilter(new FileFilterPng());
        addChoosableFileFilter(new FileFilterPdf());
    }
    
    @Override
    public void approveSelection() {
        File f = getSelectedFile();

        //teraz tomu prestavit priponu, ak ma priponu:
        String fileName = f.getPath().replace("\\", "\\\\");
        if (fileName.contains(".") && (fileName.lastIndexOf('.') < (fileName.length()-1))) {
            //tipnem si, ze je tam pripona, a odrezem ju
            String ext = fileName.substring((fileName.lastIndexOf('.')+1), fileName.length()); //vezmem si priponu
            String fileNameOnly = fileName.substring(0, fileName.lastIndexOf('.'));
            if (ext.equals("eps") || ext.equals("ps") || ext.equals("png") || ext.equals("pdf")) {
                f = new File(fileNameOnly + "." + ((RFileFilter)getFileFilter()).getExtension());
            }
        }

        super.approveSelection(f);
    }
}
