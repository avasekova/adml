package gui.subpanels;

import gui.files.OverwriteFileChooser;
import gui.tablemodels.ForecastValsTableModel;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import utils.ExcelWriter;

public class ForecastValuesSubPanel extends javax.swing.JPanel {

    public ForecastValuesSubPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelForecastValsAll = new javax.swing.JPanel();
        buttonExportForecastValues = new javax.swing.JButton();
        panelForecastVals = new javax.swing.JPanel();
        scrollPaneForecastVals = new javax.swing.JScrollPane();
        buttonForecastValsShowHidden = new javax.swing.JButton();
        buttonForecastValsHideNoForecasts = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        buttonForecastValsHideAllButAvg = new javax.swing.JButton();

        panelForecastValsAll.setPreferredSize(new java.awt.Dimension(1361, 615));

        buttonExportForecastValues.setText("Export these values");
        buttonExportForecastValues.setEnabled(false);
        buttonExportForecastValues.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExportForecastValuesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelForecastValsLayout = new javax.swing.GroupLayout(panelForecastVals);
        panelForecastVals.setLayout(panelForecastValsLayout);
        panelForecastValsLayout.setHorizontalGroup(
            panelForecastValsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneForecastVals)
        );
        panelForecastValsLayout.setVerticalGroup(
            panelForecastValsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneForecastVals, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
        );

        buttonForecastValsShowHidden.setText("Show hidden columns");
        buttonForecastValsShowHidden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonForecastValsShowHiddenActionPerformed(evt);
            }
        });

        buttonForecastValsHideNoForecasts.setText("Hide models without forecasts");
        buttonForecastValsHideNoForecasts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonForecastValsHideNoForecastsActionPerformed(evt);
            }
        });

        jLabel14.setText("(double-click inside a column to hide it.)");

        buttonForecastValsHideAllButAvg.setText("Hide all except for average");
        buttonForecastValsHideAllButAvg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonForecastValsHideAllButAvgActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelForecastValsAllLayout = new javax.swing.GroupLayout(panelForecastValsAll);
        panelForecastValsAll.setLayout(panelForecastValsAllLayout);
        panelForecastValsAllLayout.setHorizontalGroup(
            panelForecastValsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelForecastValsAllLayout.createSequentialGroup()
                .addComponent(buttonExportForecastValues)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonForecastValsShowHidden)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonForecastValsHideNoForecasts)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonForecastValsHideAllButAvg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addGap(0, 561, Short.MAX_VALUE))
            .addComponent(panelForecastVals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelForecastValsAllLayout.setVerticalGroup(
            panelForecastValsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelForecastValsAllLayout.createSequentialGroup()
                .addGroup(panelForecastValsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonExportForecastValues)
                    .addComponent(buttonForecastValsShowHidden)
                    .addComponent(buttonForecastValsHideNoForecasts)
                    .addComponent(jLabel14)
                    .addComponent(buttonForecastValsHideAllButAvg))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelForecastVals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1374, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelForecastValsAll, javax.swing.GroupLayout.PREFERRED_SIZE, 1374, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelForecastValsAll, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonExportForecastValuesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExportForecastValuesActionPerformed
        JFileChooser fileChooser = new OverwriteFileChooser();
        fileChooser.setSelectedFile(new File("forecast_values.xls"));
        if (evt.getSource() == buttonExportForecastValues) {
            switch (fileChooser.showSaveDialog(this)) {
                case JFileChooser.APPROVE_OPTION:
                File forecastValuesFile = fileChooser.getSelectedFile();
                ExcelWriter.forecastJTableToExcel((ForecastValsTableModel)(forecastValuesLatest.getModel()), forecastValuesFile);
                break;
                case JFileChooser.CANCEL_OPTION:
                default:
                //nothing
            }
        }

        //a na zaver to disablovat, aby sa na to netukalo furt
        buttonExportForecastValues.setEnabled(false);
    }//GEN-LAST:event_buttonExportForecastValuesActionPerformed

    private void buttonForecastValsShowHiddenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonForecastValsShowHiddenActionPerformed
        ((ForecastValsTableModel)forecastValuesLatest.getModel()).showAllHiddenColumns();
    }//GEN-LAST:event_buttonForecastValsShowHiddenActionPerformed

    private void buttonForecastValsHideNoForecastsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonForecastValsHideNoForecastsActionPerformed
        ((ForecastValsTableModel)forecastValuesLatest.getModel()).hideNoForecasts();
    }//GEN-LAST:event_buttonForecastValsHideNoForecastsActionPerformed

    private void buttonForecastValsHideAllButAvgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonForecastValsHideAllButAvgActionPerformed
        ((ForecastValsTableModel)forecastValuesLatest.getModel()).hideAllButAvg();
    }//GEN-LAST:event_buttonForecastValsHideAllButAvgActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonExportForecastValues;
    private javax.swing.JButton buttonForecastValsHideAllButAvg;
    private javax.swing.JButton buttonForecastValsHideNoForecasts;
    private javax.swing.JButton buttonForecastValsShowHidden;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel panelForecastVals;
    private javax.swing.JPanel panelForecastValsAll;
    private javax.swing.JScrollPane scrollPaneForecastVals;
    // End of variables declaration//GEN-END:variables
    private JTable forecastValuesLatest;
    
    public JButton getButtonExportForecastValues() {
        return buttonExportForecastValues;
    }

    public JButton getButtonForecastValsHideAllButAvg() {
        return buttonForecastValsHideAllButAvg;
    }

    public JButton getButtonForecastValsHideNoForecasts() {
        return buttonForecastValsHideNoForecasts;
    }

    public JButton getButtonForecastValsShowHidden() {
        return buttonForecastValsShowHidden;
    }

    public JPanel getPanelForecastVals() {
        return panelForecastVals;
    }

    public JScrollPane getScrollPaneForecastVals() {
        return scrollPaneForecastVals;
    }

    public void setForecastValuesLatest(JTable forecastValuesLatest) {
        this.forecastValuesLatest = forecastValuesLatest;
    }
}