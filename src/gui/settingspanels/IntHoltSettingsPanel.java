package gui.settingspanels;

import java.util.List;
import javax.swing.JOptionPane;
import models.params.Params;
import utils.Const;
import utils.Utils;

public class IntHoltSettingsPanel extends SettingsPanel {

    /**
     * Creates new form IntHoltSettingsPanel
     */
    public IntHoltSettingsPanel() {
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
        textFieldBetaRow0Col0 = new javax.swing.JTextField();
        textFieldAlphaRow0Col0 = new javax.swing.JTextField();
        checkBoxOptimizeAlpha = new javax.swing.JCheckBox();
        checkBoxOptimizeBeta = new javax.swing.JCheckBox();
        textFieldAlphaRow0Col1 = new javax.swing.JTextField();
        textFieldAlphaRow1Col0 = new javax.swing.JTextField();
        textFieldAlphaRow1Col1 = new javax.swing.JTextField();
        textFieldBetaRow0Col1 = new javax.swing.JTextField();
        textFieldBetaRow1Col0 = new javax.swing.JTextField();
        textFieldBetaRow1Col1 = new javax.swing.JTextField();

        jLabel1.setText("Alpha (level):");

        jLabel2.setText("Beta (trend):");

        checkBoxOptimizeAlpha.setText("optimize");
        checkBoxOptimizeAlpha.setEnabled(false);
        checkBoxOptimizeAlpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxOptimizeAlphaActionPerformed(evt);
            }
        });

        checkBoxOptimizeBeta.setText("optimize");
        checkBoxOptimizeBeta.setEnabled(false);
        checkBoxOptimizeBeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxOptimizeBetaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(textFieldBetaRow1Col0, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textFieldBetaRow0Col0, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldBetaRow0Col1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldBetaRow1Col1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldAlphaRow1Col0, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldAlphaRow0Col0, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldAlphaRow0Col1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldAlphaRow1Col1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(checkBoxOptimizeBeta)
                    .addComponent(checkBoxOptimizeAlpha)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(textFieldAlphaRow0Col0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldAlphaRow0Col1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldAlphaRow1Col0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldAlphaRow1Col1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(textFieldBetaRow0Col0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldBetaRow0Col1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(checkBoxOptimizeAlpha)
                        .addGap(34, 34, 34)
                        .addComponent(checkBoxOptimizeBeta)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldBetaRow1Col0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldBetaRow1Col1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void checkBoxOptimizeAlphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxOptimizeAlphaActionPerformed
        if (checkBoxOptimizeAlpha.isSelected()) {
            textFieldAlphaRow0Col0.setEnabled(false);
        } else {
            textFieldAlphaRow0Col0.setEnabled(true);
        }
    }//GEN-LAST:event_checkBoxOptimizeAlphaActionPerformed

    private void checkBoxOptimizeBetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxOptimizeBetaActionPerformed
        if (checkBoxOptimizeBeta.isSelected()) {
            textFieldBetaRow0Col0.setEnabled(false);
        } else {
            textFieldBetaRow0Col0.setEnabled(true);
        }
    }//GEN-LAST:event_checkBoxOptimizeBetaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkBoxOptimizeAlpha;
    private javax.swing.JCheckBox checkBoxOptimizeBeta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField textFieldAlphaRow0Col0;
    private javax.swing.JTextField textFieldAlphaRow0Col1;
    private javax.swing.JTextField textFieldAlphaRow1Col0;
    private javax.swing.JTextField textFieldAlphaRow1Col1;
    private javax.swing.JTextField textFieldBetaRow0Col0;
    private javax.swing.JTextField textFieldBetaRow0Col1;
    private javax.swing.JTextField textFieldBetaRow1Col0;
    private javax.swing.JTextField textFieldBetaRow1Col1;
    // End of variables declaration//GEN-END:variables

    public String getAlpha() {
        if (checkBoxOptimizeAlpha.isSelected() || textFieldAlphaRow0Col0.getText().isEmpty() ||
                textFieldAlphaRow0Col1.getText().isEmpty() || textFieldAlphaRow1Col0.getText().isEmpty() ||
                textFieldAlphaRow1Col1.getText().isEmpty()) {
            return "NULL";
        } else {
            return "c(" + textFieldAlphaRow0Col0.getText() + "," + textFieldAlphaRow1Col0.getText()
                    + "," + textFieldAlphaRow0Col1.getText() + "," + textFieldAlphaRow1Col1.getText() + ")";
        }
    }
    
    public String getBeta() {
        if (checkBoxOptimizeBeta.isSelected() || textFieldBetaRow0Col0.getText().isEmpty() ||
                textFieldBetaRow0Col1.getText().isEmpty() || textFieldBetaRow1Col0.getText().isEmpty() ||
                textFieldBetaRow1Col1.getText().isEmpty()) {
            return "NULL";
        } else {
            return "c(" + textFieldBetaRow0Col0.getText() + "," + textFieldBetaRow1Col0.getText()
                    + "," + textFieldBetaRow0Col1.getText() + "," + textFieldBetaRow1Col1.getText() + ")";
        }
    }
    
    private boolean isAlphaBetaOK() {
        //all in range <0;1>
        //and beta piecewise LEQ alpha
        Double alpha00 = Utils.getDoubleOrDefault(textFieldAlphaRow0Col0);
        Double alpha01 = Utils.getDoubleOrDefault(textFieldAlphaRow0Col1);
        Double alpha10 = Utils.getDoubleOrDefault(textFieldAlphaRow1Col0);
        Double alpha11 = Utils.getDoubleOrDefault(textFieldAlphaRow1Col1);
        Double beta00 = Utils.getDoubleOrDefault(textFieldBetaRow0Col0);
        Double beta01 = Utils.getDoubleOrDefault(textFieldBetaRow0Col1);
        Double beta10 = Utils.getDoubleOrDefault(textFieldBetaRow1Col0);
        Double beta11 = Utils.getDoubleOrDefault(textFieldBetaRow1Col1);
        return ((alpha00 >= 0) && (alpha00 <= 1) &&
                (alpha01 >= 0) && (alpha01 <= 1) &&
                (alpha10 >= 0) && (alpha10 <= 1) &&
                (alpha11 >= 0) && (alpha11 <= 1) &&
                (beta00 >= 0) && (beta00 <= 1) &&
                (beta01 >= 0) && (beta01 <= 1) &&
                (beta10 >= 0) && (beta10 <= 1) &&
                (beta11 >= 0) && (beta11 <= 1) &&
                //dunno why, ale v povodnej impl bolo ze beta<=alpha, inak to padalo
                //tak snad je toto korektne prepisanie tej podmienky:
                (beta00 <= alpha00) && (beta01 <= alpha01) && (beta10 <= alpha10) && (beta11 <= alpha11));
    }

    @Override
    public void enableAllElements(boolean trueFalse) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) {
        if ("NULL".equals(getAlpha()) || "NULL".equals(getBeta())) {
            JOptionPane.showMessageDialog(null, Const.HOLT + " will not run (optimization of params not supported)");
            throw new IllegalArgumentException(Const.HOLT + " params optimize error");
        }
        if ((! "NULL".equals(getAlpha())) && (! "NULL".equals(getBeta())) && (!isAlphaBetaOK())) {
            JOptionPane.showMessageDialog(null, Const.HOLT + " will not run (wrong params)");
            throw new IllegalArgumentException(Const.HOLT + " params error");
        }
        
        SettingsPanel.setSomethingOneValue(classss, resultList, "setAlpha", String.class, getAlpha());
        SettingsPanel.setSomethingOneValue(classss, resultList, "setBeta", String.class, getBeta());
    }
}
