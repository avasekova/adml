package utils.imlp;

import gui.ErrorMeasuresTableModel_CTS;
import gui.ErrorMeasuresTableModel_ITS;
import java.io.File;
import java.io.IOException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelWriter { //TODO add information about the models, add formatting
    
    public static void errorJTablesToExcel(ErrorMeasuresTableModel_CTS tableCTS,
            ErrorMeasuresTableModel_ITS tableIntTS, File file) {
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            
            if (! tableCTS.isEmpty()) {
                WritableSheet sheet = workbook.createSheet("CTS", 0);
                for (int row = 0; row < tableCTS.getRowCount(); row++) {
                    for (int col = 0; col < tableCTS.getColumnCount(); col++) {
                        sheet.addCell(new Label(col, row, "" + tableCTS.getValueAt(row, col)));
                        //TODO mozno pridavat cisla ako cisla - teraz su to vsetko stringy. ale to by bolo zlozitejsie
                    }
                }
            }
            
            if (! tableIntTS.isEmpty()) {
                WritableSheet sheet = workbook.createSheet("ITS", 0);
                for (int row = 0; row < tableIntTS.getRowCount(); row++) {
                    for (int col = 0; col < tableIntTS.getColumnCount(); col++) {
                        sheet.addCell(new Label(col, row, "" + tableIntTS.getValueAt(row, col)));
                        //TODO mozno pridavat cisla ako cisla - teraz su to vsetko stringy. ale to by bolo zlozitejsie
                    }
                }
            }
            
            // All sheets and cells added. Now write out the workbook
            workbook.write();
            workbook.close();
        } catch (IOException | WriteException ex) {
            //TODO log
        }
    }
}
