package gui.filefilters;

public class FileFilterPdf extends RFileFilter {

    @Override
    public String getDescription() {
        return "Portable Document Format files (.pdf)";
    }

    @Override
    public String getExtension() {
        return "pdf";
    }

    @Override
    public String getDevice() {
        return "pdf, paper=\"USr\""; //pdf needs to have the size specified. here = A4 (landscape)
    }
}
