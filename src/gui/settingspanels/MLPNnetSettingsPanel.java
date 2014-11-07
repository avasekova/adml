package gui.settingspanels;

import java.util.List;
import params.Params;
import utils.R_Bool;
import utils.Utils;

public class MLPNnetSettingsPanel extends SettingsPanel {

    /**
     * Creates new form MLPNnetSettingsPanel
     */
    public MLPNnetSettingsPanel() {
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

        buttonGroup_paramsNnetExclusive = new javax.swing.ButtonGroup();
        jLabel76 = new javax.swing.JLabel();
        textFieldLag = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        textFieldNumNodesHidden = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        radioButtonLogistic = new javax.swing.JRadioButton();
        radioButtonLeastSqrs = new javax.swing.JRadioButton();
        jLabel18 = new javax.swing.JLabel();
        radioButtonLoglinSoftmax = new javax.swing.JRadioButton();
        radioButtonCensoredOn = new javax.swing.JRadioButton();
        jLabel19 = new javax.swing.JLabel();
        checkBoxSkipConn = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        labelInitRangeMirror = new javax.swing.JLabel();
        textFieldInitRange = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        textFieldWeightDecay = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        textFieldMaxit = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        checkBoxTraceOptimization = new javax.swing.JCheckBox();
        jLabel27 = new javax.swing.JLabel();
        textFieldAbstol = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        textFieldReltol = new javax.swing.JTextField();

        buttonGroup_paramsNnetExclusive.add(radioButtonLogistic);
        buttonGroup_paramsNnetExclusive.add(radioButtonLeastSqrs);
        buttonGroup_paramsNnetExclusive.add(radioButtonLoglinSoftmax);
        buttonGroup_paramsNnetExclusive.add(radioButtonCensoredOn);

        jLabel76.setText("Lag:");

        textFieldLag.setText("1");

        jLabel10.setText("Weights:");

        jLabel11.setForeground(new java.awt.Color(255, 0, 0));
        jLabel11.setText("(not enabled yet)");

        jLabel25.setForeground(new java.awt.Color(255, 0, 0));
        jLabel25.setText("Hessian not included, as well as maxNumOfWeights");

        jLabel12.setText("Number of nodes in the (single) hidden layer:");

        textFieldNumNodesHidden.setText("1");

        jLabel29.setForeground(new java.awt.Color(255, 0, 0));
        jLabel29.setText("TODO: choose data for prediction");

        jLabel13.setText("Initial parameter vector:");

        jLabel15.setText("Which parameters to optimize:");

        jLabel14.setForeground(new java.awt.Color(255, 0, 0));
        jLabel14.setText("(not enabled yet)");

        jLabel16.setForeground(new java.awt.Color(255, 0, 0));
        jLabel16.setText("(not enabled yet. Default: all)");

        jLabel17.setText("Output units:");
        jLabel17.setEnabled(false);

        radioButtonLogistic.setSelected(true);
        radioButtonLogistic.setText("Logistic (if not selected, linear)");
        radioButtonLogistic.setEnabled(false);

        radioButtonLeastSqrs.setText("Maximum conditional likelihood fitting (if not selected, least squares fitting)");
        radioButtonLeastSqrs.setEnabled(false);

        jLabel18.setText("(not enabled yet)");

        radioButtonLoglinSoftmax.setText("Log-linear model (softmax) (if not selected, maximum conditional likelihood fitting)");
        radioButtonLoglinSoftmax.setEnabled(false);

        radioButtonCensoredOn.setText("censored on");
        radioButtonCensoredOn.setEnabled(false);

        jLabel19.setText("Add skip-layer connections from input to output:");

        jLabel20.setText("Initial random weights in range:");

        labelInitRangeMirror.setText("[-" + textFieldInitRange.getText() + ";");

        textFieldInitRange.setText("0.7");
        textFieldInitRange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldInitRangeActionPerformed(evt);
            }
        });

        jLabel23.setText("]");

        jLabel21.setText("Weight decay:");

        textFieldWeightDecay.setText("0");

        jLabel24.setText("Max iterations:");

        textFieldMaxit.setText("100");

        jLabel26.setText("Trace optimization:");

        checkBoxTraceOptimization.setSelected(true);

        jLabel27.setText("Stop if the fit criterion falls below");

        textFieldAbstol.setText("0.0001");

        jLabel22.setText("Stop if the optimizer cannot reduce the fit criterion by a factor of at least 1 -");

        textFieldReltol.setText("0.00000001");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel76)
                                .addGap(186, 186, 186)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(textFieldLag, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel10)
                            .addComponent(jLabel21)
                            .addComponent(jLabel24)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBoxTraceOptimization))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(228, 228, 228)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(textFieldMaxit, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                                    .addComponent(textFieldWeightDecay))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel14)
                                    .addComponent(textFieldNumNodesHidden, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBoxSkipConn))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(labelInitRangeMirror, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textFieldInitRange, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(212, 212, 212)
                                .addComponent(jLabel25))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel18))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(radioButtonLogistic)
                                    .addComponent(radioButtonLoglinSoftmax)
                                    .addComponent(radioButtonCensoredOn)
                                    .addComponent(radioButtonLeastSqrs)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textFieldAbstol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel29))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textFieldReltol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel76)
                    .addComponent(textFieldLag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(textFieldNumNodesHidden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(radioButtonLogistic))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioButtonLeastSqrs)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioButtonLoglinSoftmax)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioButtonCensoredOn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(labelInitRangeMirror)
                            .addComponent(textFieldInitRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel24))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(checkBoxSkipConn)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldWeightDecay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldMaxit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(jLabel25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(textFieldAbstol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29)))
                    .addComponent(checkBoxTraceOptimization))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(textFieldReltol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void textFieldInitRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldInitRangeActionPerformed
        labelInitRangeMirror.setText("[-" + textFieldInitRange.getText() + ";");
    }//GEN-LAST:event_textFieldInitRangeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup_paramsNnetExclusive;
    private javax.swing.JCheckBox checkBoxSkipConn;
    private javax.swing.JCheckBox checkBoxTraceOptimization;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel labelInitRangeMirror;
    private javax.swing.JRadioButton radioButtonCensoredOn;
    private javax.swing.JRadioButton radioButtonLeastSqrs;
    private javax.swing.JRadioButton radioButtonLogistic;
    private javax.swing.JRadioButton radioButtonLoglinSoftmax;
    private javax.swing.JTextField textFieldAbstol;
    private javax.swing.JTextField textFieldInitRange;
    private javax.swing.JTextField textFieldLag;
    private javax.swing.JTextField textFieldMaxit;
    private javax.swing.JTextField textFieldNumNodesHidden;
    private javax.swing.JTextField textFieldReltol;
    private javax.swing.JTextField textFieldWeightDecay;
    // End of variables declaration//GEN-END:variables


    public boolean isSkipConn() {
        return checkBoxSkipConn.isSelected();
    }
    
    public boolean isTraceOptimization() {
        return checkBoxTraceOptimization.isSelected();
    }
    
    public boolean isCensoredOn() {
        return radioButtonCensoredOn.isSelected();
    }
    
    public boolean isLeastSqrs() {
        return radioButtonLeastSqrs.isSelected();
    }
    
    public boolean isLogistic() {
        return radioButtonLogistic.isSelected();
    }
    
    public boolean isLoglinSoftmax() {
        return radioButtonLoglinSoftmax.isSelected();
    }
    
    public String getAbstol() {
        return textFieldAbstol.getText();
    }
    
    public String getInitRange() {
        return textFieldInitRange.getText();
    }
    
    public String getLag() {
        return textFieldLag.getText();
    }
    
    public String getMaxit() {
        return textFieldMaxit.getText();
    }

    public String getNumNodesHidden() {
        return textFieldNumNodesHidden.getText();
    }
    
    public String getReltol() {
        return textFieldReltol.getText();
    }
    
    public String getWeightDecay() {
        return textFieldWeightDecay.getText();
    }

    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) {
        SettingsPanel.setSomethingList(classss, resultList, "setLag", Integer.class, Utils.getIntegersOrDefault(getLag()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setAbstol", Double.class, Utils.getDoubleOrDefault(getAbstol()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setReltol", Double.class, Utils.getDoubleOrDefault(getReltol()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setSkipLayerConnections", R_Bool.class, Utils.booleanToRBool(isSkipConn()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setInitWeightsRange", Double.class, Utils.getDoubleOrDefault(getInitRange()));
        SettingsPanel.setSomethingList(classss, resultList, "setMaxIterations", Integer.class, Utils.getIntegersOrDefault(getMaxit()));
        SettingsPanel.setSomethingList(classss, resultList, "setNumNodesHiddenLayer", Integer.class, Utils.getIntegersOrDefault(getNumNodesHidden()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setLinearElseLogistic", R_Bool.class, Utils.booleanToRBool(isLogistic()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setLeastSqrsElseMaxCondLikelihood", R_Bool.class, Utils.booleanToRBool(isLeastSqrs()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setLoglinSoftmaxElseMaxCondLikelihood", R_Bool.class, Utils.booleanToRBool(isLoglinSoftmax()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setCensoredOnElseOff", R_Bool.class, Utils.booleanToRBool(isCensoredOn()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setWeightDecay", Double.class, Utils.getDoubleOrDefault(getWeightDecay()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setTraceOptimization", R_Bool.class, Utils.booleanToRBool(isTraceOptimization()));
        
    }

}
