package gui.settingspanels;

import java.util.List;
import models.params.Params;

public class SESSettingsPanel extends SettingsPanel {

    /**
     * Creates new form SESSettingsPanel
     */
    public SESSettingsPanel() {
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

        jLabel1.setText("Alpha:");

        checkBoxOptimizeAlpha.setText("optimize");
        checkBoxOptimizeAlpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxOptimizeAlphaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textFieldAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(checkBoxOptimizeAlpha))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(textFieldAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(checkBoxOptimizeAlpha))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void checkBoxOptimizeAlphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxOptimizeAlphaActionPerformed
        if (checkBoxOptimizeAlpha.isSelected()) {
            textFieldAlpha.setEnabled(false);
        } else {
            textFieldAlpha.setEnabled(true);
        }
    }//GEN-LAST:event_checkBoxOptimizeAlphaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkBoxOptimizeAlpha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField textFieldAlpha;
    // End of variables declaration//GEN-END:variables

    public String getAlpha() {
        if (checkBoxOptimizeAlpha.isSelected() || textFieldAlpha.getText().isEmpty()) {
            return "NULL";
        } else {
            return textFieldAlpha.getText();
        }
    }
    
    @Override
    public void enableAllElements(boolean trueFalse) {
        checkBoxOptimizeAlpha.setEnabled(trueFalse);
        jLabel1.setEnabled(trueFalse);
        textFieldAlpha.setEnabled(trueFalse);
    }
    
    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) {
        SettingsPanel.setSomethingOneValue(classss, resultList, "setAlpha", String.class, getAlpha());
    }
}
