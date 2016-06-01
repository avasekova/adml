package gui.filefilters;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class RFileFilter extends FileFilter {

    private FileNameExtensionFilter fileFilter;
    private String device;

    public RFileFilter(String device, String description, String... extensions) {
        this.device = device;
        fileFilter = new FileNameExtensionFilter(description, extensions);
    }

    @Override
    public String getDescription() {
        return fileFilter.getDescription();
    }

    @Override
    public boolean accept(File f) {
        return fileFilter.accept(f);
    }

    public String getDevice() {
        return device;
    }

    public String getExtension() {
        return fileFilter.getExtensions()[0]; //extensions always nonempty
    }
}
