package gui.filefilters;

public class FileFilterPng extends RFileFilter {

    @Override
    public String getDescription() {
        return "Portable Network Graphics files (.png)";
    }

    @Override
    public String getExtension() {
        return "png";
    }

    @Override
    public String getDevice() {
        return "png";
    }
    
}
