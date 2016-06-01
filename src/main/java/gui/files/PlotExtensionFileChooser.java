package gui.files;

import gui.filefilters.RFileFilter;

import java.io.File;

public class PlotExtensionFileChooser extends OverwriteFileChooser {

    public PlotExtensionFileChooser() {
        super("plotExport.eps");

        setAcceptAllFileFilterUsed(false); //do not allow "All files"
        addChoosableFileFilter(new RFileFilter("postscript", "Encapsulated PostScript files (.eps)", "eps"));
        addChoosableFileFilter(new RFileFilter("postscript", "PostScript files (.ps)", "ps"));
        addChoosableFileFilter(new RFileFilter("png", "Portable Network Graphics files (.png)", "png"));
        addChoosableFileFilter(new RFileFilter("pdf, paper=\"USr\"", "Portable Document Format files (.pdf)", "pdf"));
        //device: pdf, paper="USr"  // pdf needs to have the size specified. here = A4 (landscape)
    }
    
    @Override
    public void approveSelection() {
        File f = getSelectedFile();

        //now set the extension, if it has one:
        String fileName = f.getPath().replace("\\", "\\\\");
        if (fileName.contains(".") && (fileName.lastIndexOf('.') < (fileName.length()-1))) {
            //guess it has an extension
            String ext = fileName.substring((fileName.lastIndexOf('.')+1), fileName.length()); //take the ext
            String fileNameOnly = fileName.substring(0, fileName.lastIndexOf('.'));
            if (ext.equals("eps") || ext.equals("ps") || ext.equals("png") || ext.equals("pdf")) {
                f = new File(fileNameOnly + "." + ((RFileFilter)getFileFilter()).getExtension());
            }
        }

        super.approveSelection(f);
    }
}
