package utils;

import gui.tablemodels.*;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;

public class ExcelWriter { //TODO add information about the models, add formatting

    private static final Logger logger = LoggerFactory.getLogger(ExcelWriter.class);
    
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
            logger.debug("error writing results to Excel", ex);
        }
    }
    
    public static void jTableToExcel(ReportsTableModel values, File file, String title) { //TODO merge with errorJTablesToExcel
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            
            if (! values.isEmpty()) {
                WritableSheet sheet = workbook.createSheet(title, 0);
                
                //najprv zapisat nadpisy
                for (int col = 0; col < values.getColumnCount(); col++) {
                    sheet.addCell(new Label(col, 0, "" + values.getColumnName(col)));
                }
                
                //pozor na cislovanie row! kvoli headerom
                for (int row = 1; row < values.getRowCount()+1; row++) {
                    for (int col = 0; col < values.getColumnCount(); col++) {
                        sheet.addCell(new Label(col, row, "" + values.getValueAt(row-1, col)));
                        //TODO mozno pridavat cisla ako cisla - teraz su to vsetko stringy. ale to by bolo zlozitejsie
                    }
                }
            }
            
            // All sheets and cells added. Now write out the workbook
            workbook.write();
            workbook.close();
        } catch (IOException | WriteException ex) {
            logger.debug("error writing results to Excel", ex);
        }
    }
}
