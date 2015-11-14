package gui.filefilters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public abstract class RFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        } else {
            String extension = f.getName().substring(f.getName().lastIndexOf('.'));
            return getExtension().equals(extension);
        }
    }

    @Override
    public abstract String getDescription();
    public abstract String getExtension();
    public abstract String getDevice();
}
