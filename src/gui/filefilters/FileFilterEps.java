package gui.filefilters;


public class FileFilterEps extends RFileFilter {

    @Override
    public String getDescription() {
        return "Encapsulated PostScript files (.eps)";
    }
    
    @Override
    public String getExtension() {
        return "eps";
    }
    
    @Override
    public String getDevice() {
        return "postscript";
    }
}
