package gui.filefilters;

public class FileFilterPs extends RFileFilter {

    @Override
    public String getDescription() {
        return "PostScript files (.ps)";
    }

    @Override
    public String getExtension() {
        return "ps";
    }

    @Override
    public String getDevice() {
        return "postscript";
    }
}
