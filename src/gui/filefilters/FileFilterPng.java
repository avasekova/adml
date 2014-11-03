package gui.filefilters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class FileFilterPng extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        } else {
            String extension = f.getName().substring(f.getName().lastIndexOf('.'));
            return ".png".equals(extension);
        }
    }

    @Override
    public String getDescription() {
        return "Portable Network Graphics files (.png)";
    }
    
}
