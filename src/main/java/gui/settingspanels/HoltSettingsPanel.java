package gui.settingspanels;

import models.Model;
import models.params.Params;
import utils.FieldsParser;
import utils.R_Bool;
import utils.Utils;

import javax.swing.*;
import java.util.List;

public class HoltSettingsPanel extends SettingsPanel {

    /**
     * Creates new form HoltSettingsPanel
     */
    public HoltSettingsPanel() {
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        textFieldAlpha = new javax.swing.JTextField();
        textFieldBeta = new javax.swing.JTextField();
        checkBoxOptimizeAlpha = new javax.swing.JCheckBox();
        checkBoxOptimizeBeta = new javax.swing.JCheckBox();
        checkBoxDamped = new javax.swing.JCheckBox();
        checkBoxComputePredInts = new javax.swing.JCheckBox();
        textFieldPredIntsPercent = new javax.swing.JTextField();
        labelPercentSign = new javax.swing.JLabel();

        jLabel1.setText("Alpha (level):");

        jLabel2.setText("Beta (trend):");

        checkBoxOptimizeAlpha.setText("optimize");
        checkBoxOptimizeAlpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxOptimizeAlphaActionPerformed(evt);
            }
        });

        checkBoxOptimizeBeta.setText("optimize");
        checkBoxOptimizeBeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxOptimizeBetaActionPerformed(evt);
            }
        });

        checkBoxDamped.setText("damped");

        checkBoxComputePredInts.setText("compute prediction intervals for the forecasts:");
        checkBoxComputePredInts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxComputePredIntsActionPerformed(evt);
            }
        });

        textFieldPredIntsPercent.setText("95");
        textFieldPredIntsPercent.setEnabled(false);

        labelPercentSign.setText("%");
        labelPercentSign.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textFieldAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textFieldBeta, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBoxOptimizeAlpha)
                    .addComponent(checkBoxOptimizeBeta)))
            .addComponent(checkBoxDamped)
            .addGroup(layout.createSequentialGroup()
                .addComponent(checkBoxComputePredInts)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textFieldPredIntsPercent, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelPercentSign))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textFieldAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkBoxOptimizeAlpha))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(textFieldBeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkBoxOptimizeBeta))
                .addGap(18, 18, 18)
                .addComponent(checkBoxDamped)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkBoxComputePredInts)
                    .addComponent(textFieldPredIntsPercent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPercentSign)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void checkBoxOptimizeAlphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxOptimizeAlphaActionPerformed
        if (checkBoxOptimizeAlpha.isSelected()) {
            textFieldAlpha.setEnabled(false);
        } else {
            textFieldAlpha.setEnabled(true);
        }
    }//GEN-LAST:event_checkBoxOptimizeAlphaActionPerformed

    private void checkBoxOptimizeBetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxOptimizeBetaActionPerformed
        if (checkBoxOptimizeBeta.isSelected()) {
            textFieldBeta.setEnabled(false);
        } else {
            textFieldBeta.setEnabled(true);
        }
    }//GEN-LAST:event_checkBoxOptimizeBetaActionPerformed

    private void checkBoxComputePredIntsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxComputePredIntsActionPerformed
        textFieldPredIntsPercent.setEnabled(checkBoxComputePredInts.isSelected());
        labelPercentSign.setEnabled(checkBoxComputePredInts.isSelected());
    }//GEN-LAST:event_checkBoxComputePredIntsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkBoxComputePredInts;
    private javax.swing.JCheckBox checkBoxDamped;
    private javax.swing.JCheckBox checkBoxOptimizeAlpha;
    private javax.swing.JCheckBox checkBoxOptimizeBeta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel labelPercentSign;
    private javax.swing.JTextField textFieldAlpha;
    private javax.swing.JTextField textFieldBeta;
    private javax.swing.JTextField textFieldPredIntsPercent;
    // End of variables declaration//GEN-END:variables

    public String getAlpha() {
        if (checkBoxOptimizeAlpha.isSelected() || textFieldAlpha.getText().isEmpty()) {
            return "NULL";
        } else {
            return textFieldAlpha.getText();
        }
    }
    
    public String getBeta() {
        if (checkBoxOptimizeBeta.isSelected() || textFieldBeta.getText().isEmpty()) {
            return "NULL";
        } else {
            return textFieldBeta.getText();
        }
    }
    
    public R_Bool isDamped() {
        return Utils.booleanToRBool(checkBoxDamped.isSelected());
    }
    
    public String getPredIntPercent() {
        if (textFieldPredIntsPercent.getText().isEmpty() || (!checkBoxComputePredInts.isSelected())) {
            return "0";
        } else {
            return textFieldPredIntsPercent.getText();
        }
    }
    
    @Override
    public void enableAllElements(boolean trueFalse) {
        checkBoxComputePredInts.setEnabled(trueFalse);
        checkBoxDamped.setEnabled(trueFalse);
        checkBoxOptimizeAlpha.setEnabled(trueFalse);
        checkBoxOptimizeBeta.setEnabled(trueFalse);
        jLabel1.setEnabled(trueFalse);
        jLabel2.setEnabled(trueFalse);
        labelPercentSign.setEnabled(trueFalse);
        textFieldAlpha.setEnabled(trueFalse);
        textFieldBeta.setEnabled(trueFalse);
        textFieldPredIntsPercent.setEnabled(trueFalse);
    }
    
    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) {
        if ((! "NULL".equals(getAlpha())) && (! "NULL".equals(getBeta())) && 
            (Utils.getDoubleOrDefault(getBeta()) > Utils.getDoubleOrDefault(getAlpha()))) {
            JOptionPane.showMessageDialog(null, Model.HOLT + " will not run (Beta cannot be greater than Alpha)");
            throw new IllegalArgumentException(Model.HOLT + " params error");
        }
        
        if ("NULL".equals(getAlpha())) {
            SettingsPanel.setSomethingOneValue(classss, resultList, "setAlpha", String.class, getAlpha());
        } else {
            SettingsPanel.setSomethingList(classss, resultList, "setAlpha", String.class, Utils.doublesToStringsEew(FieldsParser.parseDoubles(getAlpha())));
        }
        
        if ("NULL".equals(getBeta())) {
            SettingsPanel.setSomethingOneValue(classss, resultList, "setBeta", String.class, getBeta());
        } else {
            SettingsPanel.setSomethingList(classss, resultList, "setBeta", String.class, Utils.doublesToStringsEew(FieldsParser.parseDoubles(getBeta())));
        }
        
        SettingsPanel.setSomethingOneValue(classss, resultList, "setDamped", R_Bool.class, isDamped());
        SettingsPanel.setSomethingOneValue(classss, resultList, "setPredIntPercent", Integer.class, FieldsParser.parseIntegers(getPredIntPercent()).get(0));
    }
}
