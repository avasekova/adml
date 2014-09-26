package gui;

import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class DialogLbUbCenterRadius extends javax.swing.JDialog {
    
    private static DialogLbUbCenterRadius INSTANCE = null;
    
    public static DialogLbUbCenterRadius getInstance(java.awt.Frame parent, boolean modal) {
        if (INSTANCE == null) {
            INSTANCE = new DialogLbUbCenterRadius(parent, modal);
        }
        
        return INSTANCE;
    }
    
    /**
     * Creates new form DialogLbUbCenterRadius
     * @param parent
     * @param modal
     */
    private DialogLbUbCenterRadius(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
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

        buttonGroupLbUbCenterRadius = new javax.swing.ButtonGroup();
        panelDialogLbUbCenterRadius = new javax.swing.JPanel();
        labelLB = new javax.swing.JLabel();
        comboBoxLowerBound = new javax.swing.JComboBox();
        labelUB = new javax.swing.JLabel();
        comboBoxUpperBound = new javax.swing.JComboBox();
        radioButtonLbUb = new javax.swing.JRadioButton();
        radioButtonCenterRadius = new javax.swing.JRadioButton();
        labelCenter = new javax.swing.JLabel();
        labelRadius = new javax.swing.JLabel();
        comboBoxCenter = new javax.swing.JComboBox();
        comboBoxRadius = new javax.swing.JComboBox();
        buttonOKPlotITS = new javax.swing.JButton();

        buttonGroupLbUbCenterRadius.add(radioButtonLbUb);
        buttonGroupLbUbCenterRadius.add(radioButtonCenterRadius);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        labelLB.setText("Lower Bound:");
        labelLB.setEnabled(false);

        comboBoxLowerBound.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
        comboBoxLowerBound.setEnabled(false);

        labelUB.setText("Upper Bound:");
        labelUB.setEnabled(false);

        comboBoxUpperBound.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
        comboBoxUpperBound.setEnabled(false);

        radioButtonLbUb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonLbUbActionPerformed(evt);
            }
        });

        radioButtonCenterRadius.setSelected(true);
        radioButtonCenterRadius.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonCenterRadiusActionPerformed(evt);
            }
        });

        labelCenter.setText("Center:");

        labelRadius.setText("Radius:");

        comboBoxCenter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));

        comboBoxRadius.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));

        buttonOKPlotITS.setText("OK");
        buttonOKPlotITS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOKPlotITSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDialogLbUbCenterRadiusLayout = new javax.swing.GroupLayout(panelDialogLbUbCenterRadius);
        panelDialogLbUbCenterRadius.setLayout(panelDialogLbUbCenterRadiusLayout);
        panelDialogLbUbCenterRadiusLayout.setHorizontalGroup(
            panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                        .addComponent(radioButtonLbUb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelUB)
                            .addComponent(labelLB)))
                    .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                        .addComponent(radioButtonCenterRadius)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelCenter)
                            .addComponent(labelRadius)
                            .addComponent(buttonOKPlotITS))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxRadius, 0, 87, Short.MAX_VALUE)
                    .addComponent(comboBoxCenter, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboBoxUpperBound, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboBoxLowerBound, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        panelDialogLbUbCenterRadiusLayout.setVerticalGroup(
            panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelLB)
                            .addComponent(comboBoxLowerBound, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelUB)
                            .addComponent(comboBoxUpperBound, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(radioButtonLbUb)))
                .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelCenter)
                            .addComponent(comboBoxCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelRadius)
                            .addComponent(comboBoxRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(radioButtonCenterRadius)))
                .addGap(29, 29, 29)
                .addComponent(buttonOKPlotITS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelDialogLbUbCenterRadius, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelDialogLbUbCenterRadius, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void radioButtonLbUbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonLbUbActionPerformed
        if (radioButtonLbUb.isSelected()) {
            labelLB.setEnabled(true);
            labelUB.setEnabled(true);
            comboBoxLowerBound.setEnabled(true);
            comboBoxUpperBound.setEnabled(true);
            labelCenter.setEnabled(false);
            labelRadius.setEnabled(false);
            comboBoxCenter.setEnabled(false);
            comboBoxRadius.setEnabled(false);
        }
    }//GEN-LAST:event_radioButtonLbUbActionPerformed

    private void radioButtonCenterRadiusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonCenterRadiusActionPerformed
        if (radioButtonCenterRadius.isSelected()) {
            labelCenter.setEnabled(true);
            labelRadius.setEnabled(true);
            comboBoxCenter.setEnabled(true);
            comboBoxRadius.setEnabled(true);
            labelLB.setEnabled(false);
            labelUB.setEnabled(false);
            comboBoxLowerBound.setEnabled(false);
            comboBoxUpperBound.setEnabled(false);
        }
    }//GEN-LAST:event_radioButtonCenterRadiusActionPerformed

    private void buttonOKPlotITSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOKPlotITSActionPerformed
        if (radioButtonCenterRadius.isSelected()) {
            ((MainFrame)(this.getParent())).
                    drawPlotITS_CenterRadius_currentPanelPlot(comboBoxCenter.getSelectedItem().toString(),
                                                              comboBoxRadius.getSelectedItem().toString());
        } else {
            ((MainFrame)(this.getParent())).
                    drawPlotITS_LBUB_currentPanelPlot(comboBoxLowerBound.getSelectedItem().toString(),
                                                      comboBoxUpperBound.getSelectedItem().toString());
        }
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_buttonOKPlotITSActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogLbUbCenterRadius.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogLbUbCenterRadius.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogLbUbCenterRadius.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogLbUbCenterRadius.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                DialogLbUbCenterRadius dialog = new DialogLbUbCenterRadius(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup buttonGroupLbUbCenterRadius;
    private javax.swing.JButton buttonOKPlotITS;
    private javax.swing.JComboBox comboBoxCenter;
    private javax.swing.JComboBox comboBoxLowerBound;
    private javax.swing.JComboBox comboBoxRadius;
    private javax.swing.JComboBox comboBoxUpperBound;
    private javax.swing.JLabel labelCenter;
    private javax.swing.JLabel labelLB;
    private javax.swing.JLabel labelRadius;
    private javax.swing.JLabel labelUB;
    private javax.swing.JPanel panelDialogLbUbCenterRadius;
    private javax.swing.JRadioButton radioButtonCenterRadius;
    private javax.swing.JRadioButton radioButtonLbUb;
    // End of variables declaration//GEN-END:variables

    public void setColnames(List<String> colnames) {
        List<String> colnamesCopy = new ArrayList<>(colnames);
        boolean containsAll = true;
        for (int i = 0; i < comboBoxLowerBound.getItemCount(); i++) { //prejdem vsetky, co su v comboBoxe...
            if (! colnamesCopy.contains(comboBoxLowerBound.getItemAt(i).toString())) {
                //assuming all four comboBoxes have the same values, we only need to check one of them
                containsAll = false;
                break;
            } else { //...a ak su vsetky v tom zozname, vsetky ich z neho zmazem...
                colnamesCopy.remove(comboBoxLowerBound.getItemAt(i).toString());
            }
        }
        
        if (! colnamesCopy.isEmpty()) { //...ak ale nieco v zozname ostalo navyse, bol iny
            containsAll = false;
        }
        
        if (! containsAll) {
            comboBoxLowerBound.removeAllItems();
            comboBoxUpperBound.removeAllItems();
            comboBoxCenter.removeAllItems();
            comboBoxRadius.removeAllItems();
            
            for (String c : colnames) {
                comboBoxLowerBound.addItem(c);
                comboBoxUpperBound.addItem(c);
                comboBoxCenter.addItem(c);
                comboBoxRadius.addItem(c);
            }
        }
    }
}
