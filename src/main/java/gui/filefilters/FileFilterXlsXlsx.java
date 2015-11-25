package gui.filefilters;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileFilterXlsXlsx extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        } else {
            String extension = f.getName().substring(f.getName().lastIndexOf('.'));
            return ".xls".equals(extension) || ".xlsx".equals(extension);
        }
    }

    @Override
    public String getDescription() {
        return "MS Excel files (.xls, .xlsx)";
    }
    
}
