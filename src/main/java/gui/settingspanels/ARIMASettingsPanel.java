package gui.settingspanels;

import models.params.Params;
import utils.FieldsParser;

import java.util.List;

public class ARIMASettingsPanel extends SettingsPanel {

    /**
     * Creates new form ARIMASettingsPanel
     */
    public ARIMASettingsPanel() {
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

        checkBoxOptimize = new javax.swing.JCheckBox();
        labelSettingsARIMAnonseas = new javax.swing.JLabel();
        labelSettingsARIMAseas = new javax.swing.JLabel();
        checkBoxConstant = new javax.swing.JCheckBox();
        labelSeasUppercasePotato = new javax.swing.JLabel();
        labelNonseasLowercasePotato = new javax.swing.JLabel();
        textFieldNonseasLowercasePotato = new javax.swing.JTextField();
        textFieldSeasUppercasePotato = new javax.swing.JTextField();
        labelNonseasLowercaseDonkey = new javax.swing.JLabel();
        labelSeasUppercaseDonkey = new javax.swing.JLabel();
        textFieldNonseasLowercaseDonkey = new javax.swing.JTextField();
        textFieldSeasUppercaseDonkey = new javax.swing.JTextField();
        labelNonseasLowercaseQuark = new javax.swing.JLabel();
        labelSeasUppercaseQuark = new javax.swing.JLabel();
        textFieldNonseasLowercaseQuark = new javax.swing.JTextField();
        textFieldSeasUppercaseQuark = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        checkBoxComputePredInts = new javax.swing.JCheckBox();
        labelPercentSign = new javax.swing.JLabel();
        textFieldPredictionIntervalPercent = new javax.swing.JTextField();

        checkBoxOptimize.setText("optimize the parameters");
        checkBoxOptimize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxOptimizeActionPerformed(evt);
            }
        });

        labelSettingsARIMAnonseas.setText("Non-seasonal part:");

        labelSettingsARIMAseas.setText("Seasonal part:");

        checkBoxConstant.setText("include constant");

        labelSeasUppercasePotato.setText("P =");

        labelNonseasLowercasePotato.setText("p =");

        textFieldNonseasLowercasePotato.setText("0");

        textFieldSeasUppercasePotato.setText("0");

        labelNonseasLowercaseDonkey.setText("d =");

        labelSeasUppercaseDonkey.setText("D =");

        textFieldNonseasLowercaseDonkey.setText("0");

        textFieldSeasUppercaseDonkey.setText("0");

        labelNonseasLowercaseQuark.setText("q =");

        labelSeasUppercaseQuark.setText("Q =");

        textFieldNonseasLowercaseQuark.setText("0");

        textFieldSeasUppercaseQuark.setText("0");

        jLabel62.setForeground(new java.awt.Color(255, 0, 0));
        jLabel62.setText("TODO other params of stats.arima + add <default> labels");

        checkBoxComputePredInts.setText("compute prediction intervals for the forecasts:");
        checkBoxComputePredInts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxComputePredIntsActionPerformed(evt);
            }
        });

        labelPercentSign.setText("%");
        labelPercentSign.setEnabled(false);

        textFieldPredictionIntervalPercent.setText("95");
        textFieldPredictionIntervalPercent.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(checkBoxComputePredInts)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textFieldPredictionIntervalPercent, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelPercentSign))
            .addComponent(checkBoxOptimize)
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelSettingsARIMAnonseas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelNonseasLowercasePotato)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldNonseasLowercasePotato, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelSettingsARIMAseas)
                .addGap(28, 28, 28)
                .addComponent(labelSeasUppercasePotato)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldSeasUppercasePotato, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelNonseasLowercaseDonkey)
                        .addGap(5, 5, 5)
                        .addComponent(textFieldNonseasLowercaseDonkey, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelNonseasLowercaseQuark)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldNonseasLowercaseQuark, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelSeasUppercaseDonkey)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldSeasUppercaseDonkey, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelSeasUppercaseQuark)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldSeasUppercaseQuark, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(layout.createSequentialGroup()
                .addComponent(checkBoxConstant)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel62))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(checkBoxOptimize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSettingsARIMAnonseas)
                    .addComponent(labelNonseasLowercasePotato)
                    .addComponent(textFieldNonseasLowercasePotato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldNonseasLowercaseDonkey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNonseasLowercaseDonkey)
                    .addComponent(labelNonseasLowercaseQuark)
                    .addComponent(textFieldNonseasLowercaseQuark, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSettingsARIMAseas)
                    .addComponent(textFieldSeasUppercasePotato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSeasUppercasePotato)
                    .addComponent(labelSeasUppercaseDonkey)
                    .addComponent(textFieldSeasUppercaseDonkey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSeasUppercaseQuark)
                    .addComponent(textFieldSeasUppercaseQuark, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkBoxConstant)
                    .addComponent(jLabel62))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkBoxComputePredInts)
                    .addComponent(textFieldPredictionIntervalPercent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPercentSign)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void checkBoxOptimizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxOptimizeActionPerformed
        if (checkBoxOptimize.isSelected()) {
            labelSettingsARIMAnonseas.setEnabled(false);
            labelNonseasLowercasePotato.setEnabled(false);
            labelNonseasLowercaseDonkey.setEnabled(false);
            labelNonseasLowercaseQuark.setEnabled(false);
            labelSettingsARIMAseas.setEnabled(false);
            labelSeasUppercasePotato.setEnabled(false);
            labelSeasUppercaseDonkey.setEnabled(false);
            labelSeasUppercaseQuark.setEnabled(false);
            textFieldNonseasLowercasePotato.setEnabled(false);
            textFieldNonseasLowercaseDonkey.setEnabled(false);
            textFieldNonseasLowercaseQuark.setEnabled(false);
            textFieldSeasUppercasePotato.setEnabled(false);
            textFieldSeasUppercaseDonkey.setEnabled(false);
            textFieldSeasUppercaseQuark.setEnabled(false);
            checkBoxConstant.setEnabled(false);
        } else {
            labelSettingsARIMAnonseas.setEnabled(true);
            labelNonseasLowercasePotato.setEnabled(true);
            labelNonseasLowercaseDonkey.setEnabled(true);
            labelNonseasLowercaseQuark.setEnabled(true);
            labelSettingsARIMAseas.setEnabled(true);
            labelSeasUppercasePotato.setEnabled(true);
            labelSeasUppercaseDonkey.setEnabled(true);
            labelSeasUppercaseQuark.setEnabled(true);
            textFieldNonseasLowercasePotato.setEnabled(true);
            textFieldNonseasLowercaseDonkey.setEnabled(true);
            textFieldNonseasLowercaseQuark.setEnabled(true);
            textFieldSeasUppercasePotato.setEnabled(true);
            textFieldSeasUppercaseDonkey.setEnabled(true);
            textFieldSeasUppercaseQuark.setEnabled(true);
            checkBoxConstant.setEnabled(true);
        }
    }//GEN-LAST:event_checkBoxOptimizeActionPerformed

    private void checkBoxComputePredIntsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxComputePredIntsActionPerformed
        textFieldPredictionIntervalPercent.setEnabled(checkBoxComputePredInts.isSelected());
        labelPercentSign.setEnabled(checkBoxComputePredInts.isSelected());
    }//GEN-LAST:event_checkBoxComputePredIntsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkBoxComputePredInts;
    private javax.swing.JCheckBox checkBoxConstant;
    private javax.swing.JCheckBox checkBoxOptimize;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel labelNonseasLowercaseDonkey;
    private javax.swing.JLabel labelNonseasLowercasePotato;
    private javax.swing.JLabel labelNonseasLowercaseQuark;
    private javax.swing.JLabel labelPercentSign;
    private javax.swing.JLabel labelSeasUppercaseDonkey;
    private javax.swing.JLabel labelSeasUppercasePotato;
    private javax.swing.JLabel labelSeasUppercaseQuark;
    private javax.swing.JLabel labelSettingsARIMAnonseas;
    private javax.swing.JLabel labelSettingsARIMAseas;
    private javax.swing.JTextField textFieldNonseasLowercaseDonkey;
    private javax.swing.JTextField textFieldNonseasLowercasePotato;
    private javax.swing.JTextField textFieldNonseasLowercaseQuark;
    private javax.swing.JTextField textFieldPredictionIntervalPercent;
    private javax.swing.JTextField textFieldSeasUppercaseDonkey;
    private javax.swing.JTextField textFieldSeasUppercasePotato;
    private javax.swing.JTextField textFieldSeasUppercaseQuark;
    // End of variables declaration//GEN-END:variables

    public boolean isConstant() {
        return checkBoxConstant.isSelected();
    }
    
    public boolean isOptimize() {
        return checkBoxOptimize.isSelected();
    }
    
    public String getNonSeasLowercaseDonkey() {
        return textFieldNonseasLowercaseDonkey.getText();
    }
    
    public String getNonSeasLowercasePotato() {
        return textFieldNonseasLowercasePotato.getText();
    }
    
    public String getNonSeasLowercaseQuark() {
        return textFieldNonseasLowercaseQuark.getText();
    }
    
    public String getSeasUppercaseDonkey() {
        return textFieldSeasUppercaseDonkey.getText();
    }
    
    public String getSeasUppercasePotato() {
        return textFieldSeasUppercasePotato.getText();
    }
    
    public String getSeasUppercaseQuark() {
        return textFieldSeasUppercaseQuark.getText();
    }
    
    public String getPredIntPercent() {
        if (textFieldPredictionIntervalPercent.getText().isEmpty() || (!checkBoxComputePredInts.isSelected())) {
            return "0";
        } else {
            return textFieldPredictionIntervalPercent.getText();
        }
    }

    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) {
        SettingsPanel.setSomethingOneValue(classss, resultList, "setOptimize", Boolean.class, isOptimize());
        SettingsPanel.setSomethingList(classss, resultList, "setNonSeasPotato", Integer.class, FieldsParser.parseIntegers(getNonSeasLowercasePotato()));
        SettingsPanel.setSomethingList(classss, resultList, "setNonSeasDonkey", Integer.class, FieldsParser.parseIntegers(getNonSeasLowercaseDonkey()));
        SettingsPanel.setSomethingList(classss, resultList, "setNonSeasQuark", Integer.class, FieldsParser.parseIntegers(getNonSeasLowercaseQuark()));
        SettingsPanel.setSomethingList(classss, resultList, "setSeasPotato", Integer.class, FieldsParser.parseIntegers(getSeasUppercasePotato()));
        SettingsPanel.setSomethingList(classss, resultList, "setSeasDonkey", Integer.class, FieldsParser.parseIntegers(getSeasUppercaseDonkey()));
        SettingsPanel.setSomethingList(classss, resultList, "setSeasQuark", Integer.class, FieldsParser.parseIntegers(getSeasUppercaseQuark()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setWithConstant", Boolean.class, isConstant());
        SettingsPanel.setSomethingOneValue(classss, resultList, "setPredIntPercent", Integer.class, FieldsParser.parseIntegers(getPredIntPercent()).get(0));
    }
}
