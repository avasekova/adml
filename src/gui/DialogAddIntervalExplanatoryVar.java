package gui;

import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SpinnerNumberModel;
import utils.IntervalExplanatoryVariable;
import utils.imlp.IntervalNames;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;

public class DialogAddIntervalExplanatoryVar extends javax.swing.JDialog {
    
    private IntervalExplVarsTableModel tableModel;
    private static List<String> colNames = new ArrayList<>();
    
    public DialogAddIntervalExplanatoryVar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        for (String c : DialogAddIntervalExplanatoryVar.colNames) {
            comboBoxAddExplVarLower.addItem(c);
            comboBoxAddExplVarUpper.addItem(c);
            comboBoxAddExplVarCenter.addItem(c);
            comboBoxAddExplVarRadius.addItem(c);
        }
    }
    
    public static void setColNames(List<String> colNames) {
        DialogAddIntervalExplanatoryVar.colNames = colNames;
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
        textFieldAddExplVarName = new javax.swing.JTextField();
        radioButtonAddExplVarCenterRadius = new javax.swing.JRadioButton();
        labelLB = new javax.swing.JLabel();
        labelUB = new javax.swing.JLabel();
        comboBoxAddExplVarLower = new javax.swing.JComboBox();
        comboBoxAddExplVarUpper = new javax.swing.JComboBox();
        labelCenter = new javax.swing.JLabel();
        labelRadius = new javax.swing.JLabel();
        comboBoxAddExplVarCenter = new javax.swing.JComboBox();
        comboBoxAddExplVarRadius = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        spinnerAddExplVarLag = new javax.swing.JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        buttonOKAddExplVar = new javax.swing.JButton();

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

        comboBoxAddExplVarLower.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));

        comboBoxAddExplVarUpper.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));

        labelCenter.setText("Center:");
        labelCenter.setEnabled(false);

        labelRadius.setText("Radius:");
        labelRadius.setEnabled(false);

        comboBoxAddExplVarCenter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
        comboBoxAddExplVarCenter.setEnabled(false);

        comboBoxAddExplVarRadius.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
        comboBoxAddExplVarRadius.setEnabled(false);

        jLabel6.setText("Lag:");

        jLabel7.setText("(t-");

        jLabel8.setText(")");

        buttonOKAddExplVar.setText("OK");
        buttonOKAddExplVar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOKAddExplVarActionPerformed(evt);
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                                .addGap(33, 33, 33))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldAddExplVarName)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(spinnerAddExplVarLag)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(comboBoxAddExplVarLower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBoxAddExplVarUpper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBoxAddExplVarCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBoxAddExplVarRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 117, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(buttonOKAddExplVar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textFieldAddExplVarName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                    .addComponent(comboBoxAddExplVarLower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelUB)
                                    .addComponent(comboBoxAddExplVarUpper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(labelCenter))
                            .addComponent(comboBoxAddExplVarCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelRadius)
                            .addComponent(comboBoxAddExplVarRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(spinnerAddExplVarLag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(buttonOKAddExplVar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void setExplVarsTableModel(IntervalExplVarsTableModel tableModel) {
        this.tableModel = tableModel;
    }
    
    private void radioButtonAddExplVarLBUBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonAddExplVarLBUBActionPerformed
        if (radioButtonAddExplVarLBUB.isSelected()) {
            labelLB.setEnabled(true);
            labelUB.setEnabled(true);
            comboBoxAddExplVarLower.setEnabled(true);
            comboBoxAddExplVarUpper.setEnabled(true);
            labelCenter.setEnabled(false);
            labelRadius.setEnabled(false);
            comboBoxAddExplVarCenter.setEnabled(false);
            comboBoxAddExplVarRadius.setEnabled(false);
        }
    }//GEN-LAST:event_radioButtonAddExplVarLBUBActionPerformed

    private void radioButtonAddExplVarCenterRadiusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonAddExplVarCenterRadiusActionPerformed
        if (radioButtonAddExplVarCenterRadius.isSelected()) {
            labelCenter.setEnabled(true);
            labelRadius.setEnabled(true);
            comboBoxAddExplVarCenter.setEnabled(true);
            comboBoxAddExplVarRadius.setEnabled(true);
            labelLB.setEnabled(false);
            labelUB.setEnabled(false);
            comboBoxAddExplVarLower.setEnabled(false);
            comboBoxAddExplVarUpper.setEnabled(false);
        }
    }//GEN-LAST:event_radioButtonAddExplVarCenterRadiusActionPerformed

    private void buttonOKAddExplVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOKAddExplVarActionPerformed
        IntervalExplanatoryVariable var = new IntervalExplanatoryVariable();
        var.setName(textFieldAddExplVarName.getText());
        var.setLag(Integer.parseInt(spinnerAddExplVarLag.getValue().toString()));
            
        if (radioButtonAddExplVarCenterRadius.isSelected()) {
            IntervalNames names = new IntervalNamesCentreRadius(comboBoxAddExplVarCenter.getSelectedItem().toString(),
                    comboBoxAddExplVarRadius.getSelectedItem().toString());
            var.setIntervalNames(names);
        } else {
            IntervalNames names = new IntervalNamesLowerUpper(comboBoxAddExplVarLower.getSelectedItem().toString(),
                    comboBoxAddExplVarUpper.getSelectedItem().toString());
            var.setIntervalNames(names);
        }
        
        tableModel.addVariable(var);
        
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_buttonOKAddExplVarActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogAddIntervalExplanatoryVar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogAddIntervalExplanatoryVar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogAddIntervalExplanatoryVar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogAddIntervalExplanatoryVar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogAddIntervalExplanatoryVar dialog = new DialogAddIntervalExplanatoryVar(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton buttonOKAddExplVar;
    private javax.swing.JComboBox comboBoxAddExplVarCenter;
    private javax.swing.JComboBox comboBoxAddExplVarLower;
    private javax.swing.JComboBox comboBoxAddExplVarRadius;
    private javax.swing.JComboBox comboBoxAddExplVarUpper;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel labelCenter;
    private javax.swing.JLabel labelLB;
    private javax.swing.JLabel labelRadius;
    private javax.swing.JLabel labelUB;
    private javax.swing.JRadioButton radioButtonAddExplVarCenterRadius;
    private javax.swing.JRadioButton radioButtonAddExplVarLBUB;
    private javax.swing.JSpinner spinnerAddExplVarLag;
    private javax.swing.JTextField textFieldAddExplVarName;
    // End of variables declaration//GEN-END:variables

}
