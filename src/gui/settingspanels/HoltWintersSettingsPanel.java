package gui.settingspanels;

import java.util.List;
import javax.swing.JOptionPane;
import params.Params;
import utils.R_Bool;
import utils.Utils;

public class HoltWintersSettingsPanel extends SettingsPanel {

    /**
     * Creates new form HoltWintersSettingsPanel
     */
    public HoltWintersSettingsPanel() {
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
        textFieldAlpha = new javax.swing.JTextField();
        checkBoxOptimizeAlpha = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        textFieldBeta = new javax.swing.JTextField();
        checkBoxOptimizeBeta = new javax.swing.JCheckBox();
        checkBoxDamped = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        textFieldGamma = new javax.swing.JTextField();
        checkBoxOptimizeGamma = new javax.swing.JCheckBox();
        panelSeasonality = new SeasonalitySettingsPanel();

        jLabel1.setText("Alpha (level):");

        checkBoxOptimizeAlpha.setText("optimize");
        checkBoxOptimizeAlpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxOptimizeAlphaActionPerformed(evt);
            }
        });

        jLabel2.setText("Beta (trend):");

        checkBoxOptimizeBeta.setText("optimize");
        checkBoxOptimizeBeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxOptimizeBetaActionPerformed(evt);
            }
        });

        checkBoxDamped.setText("damped");

        jLabel3.setText("Gamma (seasonal component):");

        checkBoxOptimizeGamma.setText("optimize");
        checkBoxOptimizeGamma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxOptimizeGammaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBoxDamped)
                    .addComponent(jLabel3)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
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
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(textFieldGamma, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(checkBoxOptimizeGamma))))
                .addGap(0, 45, Short.MAX_VALUE))
            .addComponent(panelSeasonality, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldGamma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkBoxOptimizeGamma))
                .addGap(18, 18, 18)
                .addComponent(checkBoxDamped)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelSeasonality, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE))
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

    private void checkBoxOptimizeGammaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxOptimizeGammaActionPerformed
        if (checkBoxOptimizeGamma.isSelected()) {
            textFieldGamma.setEnabled(false);
        } else {
            textFieldGamma.setEnabled(true);
        }
    }//GEN-LAST:event_checkBoxOptimizeGammaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkBoxDamped;
    private javax.swing.JCheckBox checkBoxOptimizeAlpha;
    private javax.swing.JCheckBox checkBoxOptimizeBeta;
    private javax.swing.JCheckBox checkBoxOptimizeGamma;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel panelSeasonality;
    private javax.swing.JTextField textFieldAlpha;
    private javax.swing.JTextField textFieldBeta;
    private javax.swing.JTextField textFieldGamma;
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
    
    public String getGamma() {
        if (checkBoxOptimizeGamma.isSelected() || textFieldGamma.getText().isEmpty()) {
            return "NULL";
        } else {
            return textFieldGamma.getText();
        }
    }
    
    public R_Bool isDamped() {
        return Utils.booleanToRBool(checkBoxDamped.isSelected());
    }
    
    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) {
        if ((! "NULL".equals(getAlpha())) && (! "NULL".equals(getBeta())) && 
            (Utils.getDoubleOrDefault(getBeta()) > Utils.getDoubleOrDefault(getAlpha()))) {
            JOptionPane.showMessageDialog(null, "Holt-Winters will not run (Beta cannot be greater than Alpha)");
            throw new IllegalArgumentException("Holt-Winters params error");
        }
        
        SettingsPanel.setSomethingOneValue(classss, resultList, "setAlpha", String.class, getAlpha());
        SettingsPanel.setSomethingOneValue(classss, resultList, "setBeta", String.class, getBeta());
        SettingsPanel.setSomethingOneValue(classss, resultList, "setGamma", String.class, getGamma());
        SettingsPanel.setSomethingOneValue(classss, resultList, "setDamped", R_Bool.class, isDamped());
        SettingsPanel.setSomethingOneValue(classss, resultList, "setFrequency", String.class, ((SeasonalitySettingsPanel)panelSeasonality).getFrequency());
    }
}