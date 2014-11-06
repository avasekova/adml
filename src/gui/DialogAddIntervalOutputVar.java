package gui;

import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import utils.imlp.IntervalNames;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;
import utils.IntervalOutputVariable;

public class DialogAddIntervalOutputVar extends javax.swing.JDialog {
    
    private IntervalOutVarsTableModel tableModel;
    private static List<String> colNames = new ArrayList<>();
    
    public DialogAddIntervalOutputVar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        for (String c : DialogAddIntervalOutputVar.colNames) {
            comboBoxAddOutVarLower.addItem(c);
            comboBoxAddOutVarUpper.addItem(c);
            comboBoxAddOutVarCenter.addItem(c);
            comboBoxAddOutVarRadius.addItem(c);
        }
    }

    public static void setColNames(List<String> colNames) {
        DialogAddIntervalOutputVar.colNames = colNames;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupAddExplVar = new javax.swing.ButtonGroup();
        radioButtonAddExplVarLBUB = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        textFieldAddOutVarName = new javax.swing.JTextField();
        radioButtonAddExplVarCenterRadius = new javax.swing.JRadioButton();
        labelLB = new javax.swing.JLabel();
        labelUB = new javax.swing.JLabel();
        comboBoxAddOutVarLower = new javax.swing.JComboBox();
        comboBoxAddOutVarUpper = new javax.swing.JComboBox();
        labelCenter = new javax.swing.JLabel();
        labelRadius = new javax.swing.JLabel();
        comboBoxAddOutVarCenter = new javax.swing.JComboBox();
        comboBoxAddOutVarRadius = new javax.swing.JComboBox();
        buttonOKAddOutVar = new javax.swing.JButton();

        buttonGroupAddExplVar.add(radioButtonAddExplVarLBUB);
        buttonGroupAddExplVar.add(radioButtonAddExplVarCenterRadius);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        radioButtonAddExplVarLBUB.setSelected(true);
        radioButtonAddExplVarLBUB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonAddExplVarLBUBActionPerformed(evt);
            }
        });

        jLabel1.setText("Name:");

        radioButtonAddExplVarCenterRadius.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonAddExplVarCenterRadiusActionPerformed(evt);
            }
        });

        labelLB.setText("Lower bound:");

        labelUB.setText("Upper bound:");

        comboBoxAddOutVarLower.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));

        comboBoxAddOutVarUpper.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));

        labelCenter.setText("Center:");
        labelCenter.setEnabled(false);

        labelRadius.setText("Radius:");
        labelRadius.setEnabled(false);

        comboBoxAddOutVarCenter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
        comboBoxAddOutVarCenter.setEnabled(false);

        comboBoxAddOutVarRadius.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
        comboBoxAddOutVarRadius.setEnabled(false);

        buttonOKAddOutVar.setText("OK");
        buttonOKAddOutVar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOKAddOutVarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(radioButtonAddExplVarCenterRadius)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelCenter)
                                    .addComponent(labelRadius)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(radioButtonAddExplVarLBUB)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelLB)
                                    .addComponent(labelUB))))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldAddOutVarName)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comboBoxAddOutVarLower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboBoxAddOutVarUpper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboBoxAddOutVarCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboBoxAddOutVarRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 117, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(buttonOKAddOutVar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textFieldAddOutVarName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(radioButtonAddExplVarLBUB)
                        .addGap(44, 44, 44)
                        .addComponent(radioButtonAddExplVarCenterRadius))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelLB)
                                    .addComponent(comboBoxAddOutVarLower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelUB)
                                    .addComponent(comboBoxAddOutVarUpper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(labelCenter))
                            .addComponent(comboBoxAddOutVarCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelRadius)
                            .addComponent(comboBoxAddOutVarRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                .addComponent(buttonOKAddOutVar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void setOutVarsTableModel(IntervalOutVarsTableModel tableModel) {
        this.tableModel = tableModel;
    }
    
    private void radioButtonAddExplVarLBUBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonAddExplVarLBUBActionPerformed
        if (radioButtonAddExplVarLBUB.isSelected()) {
            labelLB.setEnabled(true);
            labelUB.setEnabled(true);
            comboBoxAddOutVarLower.setEnabled(true);
            comboBoxAddOutVarUpper.setEnabled(true);
            labelCenter.setEnabled(false);
            labelRadius.setEnabled(false);
            comboBoxAddOutVarCenter.setEnabled(false);
            comboBoxAddOutVarRadius.setEnabled(false);
        }
    }//GEN-LAST:event_radioButtonAddExplVarLBUBActionPerformed

    private void radioButtonAddExplVarCenterRadiusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonAddExplVarCenterRadiusActionPerformed
        if (radioButtonAddExplVarCenterRadius.isSelected()) {
            labelCenter.setEnabled(true);
            labelRadius.setEnabled(true);
            comboBoxAddOutVarCenter.setEnabled(true);
            comboBoxAddOutVarRadius.setEnabled(true);
            labelLB.setEnabled(false);
            labelUB.setEnabled(false);
            comboBoxAddOutVarLower.setEnabled(false);
            comboBoxAddOutVarUpper.setEnabled(false);
        }
    }//GEN-LAST:event_radioButtonAddExplVarCenterRadiusActionPerformed

    private void buttonOKAddOutVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOKAddOutVarActionPerformed
        IntervalOutputVariable var = new IntervalOutputVariable();
        var.setName(textFieldAddOutVarName.getText());
            
        if (radioButtonAddExplVarCenterRadius.isSelected()) {
            IntervalNames names = new IntervalNamesCentreRadius(comboBoxAddOutVarCenter.getSelectedItem().toString(),
                    comboBoxAddOutVarRadius.getSelectedItem().toString());
            var.setIntervalNames(names);
        } else {
            IntervalNames names = new IntervalNamesLowerUpper(comboBoxAddOutVarLower.getSelectedItem().toString(),
                    comboBoxAddOutVarUpper.getSelectedItem().toString());
            var.setIntervalNames(names);
        }
        
        tableModel.addVariable(var);
        
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_buttonOKAddOutVarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogAddIntervalOutputVar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                DialogAddIntervalOutputVar dialog = new DialogAddIntervalOutputVar(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupAddExplVar;
    private javax.swing.JButton buttonOKAddOutVar;
    private javax.swing.JComboBox comboBoxAddOutVarCenter;
    private javax.swing.JComboBox comboBoxAddOutVarLower;
    private javax.swing.JComboBox comboBoxAddOutVarRadius;
    private javax.swing.JComboBox comboBoxAddOutVarUpper;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel labelCenter;
    private javax.swing.JLabel labelLB;
    private javax.swing.JLabel labelRadius;
    private javax.swing.JLabel labelUB;
    private javax.swing.JRadioButton radioButtonAddExplVarCenterRadius;
    private javax.swing.JRadioButton radioButtonAddExplVarLBUB;
    private javax.swing.JTextField textFieldAddOutVarName;
    // End of variables declaration//GEN-END:variables

}
