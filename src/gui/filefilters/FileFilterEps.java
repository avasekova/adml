package gui.filefilters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class FileFilterEps extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        } else {
            String extension = f.getName().substring(f.getName().lastIndexOf('.'));
            return ".eps".equals(extension);
        }
    }

    @Override
    public String getDescription() {
        return "Encapsulated PostScript files (.eps)";
    }
    
}
