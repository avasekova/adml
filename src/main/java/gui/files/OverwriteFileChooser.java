package gui.files;

import javax.swing.*;
import java.io.File;

public class OverwriteFileChooser extends JFileChooser {
    
    public OverwriteFileChooser() {
        super();
        setCurrentDirectory(null);
        setMultiSelectionEnabled(false);
    }
    
    @Override
    public void approveSelection() {
        approveSelection(getSelectedFile());
    }
    
    public void approveSelection(File f) {
        if (f.exists() && getDialogType() == SAVE_DIALOG) {
            int result = JOptionPane.showConfirmDialog(this, "The file '" + f.getName() + "' exists, overwrite?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
            switch (result) {
                case JOptionPane.YES_OPTION:
                    super.approveSelection();
                    return;
                case JOptionPane.NO_OPTION:
                    return;
                case JOptionPane.CLOSED_OPTION:
                    return;
                case JOptionPane.CANCEL_OPTION:
                    cancelSelection();
                    return;
            }
        }
        
        super.approveSelection();
    }
}
