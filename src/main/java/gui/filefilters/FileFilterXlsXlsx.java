package gui.filefilters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

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
