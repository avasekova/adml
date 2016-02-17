package gui.dialogs;

import gui.MainFrame;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;

import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class DialogLbUbCenterRadius extends javax.swing.JDialog {
    
    private static DialogLbUbCenterRadius INSTANCE = null;

    private static boolean converter;
    
    public static DialogLbUbCenterRadius getInstance(java.awt.Frame parent, boolean modal, boolean converter) {
        if (INSTANCE == null) {
            INSTANCE = new DialogLbUbCenterRadius(parent, modal);
        }

        DialogLbUbCenterRadius.converter = converter;
        if (converter) {
            buttonOKPlotITS.setText("Convert to C/R");
        } else {
            buttonOKPlotITS.setText("OK");
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
        buttonCancelPlotITS = new javax.swing.JButton();

        buttonGroupLbUbCenterRadius.add(radioButtonLbUb);
        buttonGroupLbUbCenterRadius.add(radioButtonCenterRadius);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        labelLB.setText("Lower Bound:");

        comboBoxLowerBound.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));

        labelUB.setText("Upper Bound:");

        comboBoxUpperBound.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));

        radioButtonLbUb.setSelected(true);
        radioButtonLbUb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonLbUbActionPerformed(evt);
            }
        });

        radioButtonCenterRadius.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonCenterRadiusActionPerformed(evt);
            }
        });

        labelCenter.setText("Center:");
        labelCenter.setEnabled(false);

        labelRadius.setText("Radius:");
        labelRadius.setEnabled(false);

        comboBoxCenter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
        comboBoxCenter.setEnabled(false);

        comboBoxRadius.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
        comboBoxRadius.setEnabled(false);

        buttonOKPlotITS.setText("OK");
        buttonOKPlotITS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOKPlotITSActionPerformed(evt);
            }
        });

        buttonCancelPlotITS.setText("Cancel");
        buttonCancelPlotITS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelPlotITSActionPerformed(evt);
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
                        .addComponent(radioButtonCenterRadius)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelCenter)
                            .addComponent(labelRadius))
                        .addGap(33, 33, 33)
                        .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboBoxCenter, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboBoxRadius, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(buttonOKPlotITS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonCancelPlotITS)
                        .addGap(0, 55, Short.MAX_VALUE))
                    .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                        .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                                .addComponent(radioButtonLbUb)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(labelLB))
                            .addComponent(labelUB))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboBoxUpperBound, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboBoxLowerBound, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panelDialogLbUbCenterRadiusLayout.setVerticalGroup(
            panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboBoxLowerBound, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelLB))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboBoxUpperBound, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelUB)))
                    .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(radioButtonLbUb)))
                .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                        .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboBoxCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelCenter))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboBoxRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelRadius)))
                    .addGroup(panelDialogLbUbCenterRadiusLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(radioButtonCenterRadius)))
                .addGap(26, 26, 26)
                .addGroup(panelDialogLbUbCenterRadiusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonOKPlotITS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonCancelPlotITS))
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
        toggleComponents(radioButtonLbUb.isSelected());

        if (converter && radioButtonLbUb.isSelected()) {
            buttonOKPlotITS.setText("Convert to C/R");
        }
    }//GEN-LAST:event_radioButtonLbUbActionPerformed

    private void radioButtonCenterRadiusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonCenterRadiusActionPerformed
        toggleComponents(radioButtonLbUb.isSelected());

        if (converter && radioButtonCenterRadius.isSelected()) {
            buttonOKPlotITS.setText("Convert to LB/UB");
        }
    }//GEN-LAST:event_radioButtonCenterRadiusActionPerformed

    private void buttonOKPlotITSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOKPlotITSActionPerformed
        if (converter) {
            if (radioButtonCenterRadius.isSelected()) {
                ((MainFrame)(this.getParent())).
                        convertITStoLBUB(new IntervalNamesCentreRadius(comboBoxCenter.getSelectedItem().toString(),
                                comboBoxRadius.getSelectedItem().toString()));
            } else {
                ((MainFrame)(this.getParent())).
                        convertITStoCR(new IntervalNamesLowerUpper(comboBoxLowerBound.getSelectedItem().toString(),
                                comboBoxUpperBound.getSelectedItem().toString()));
            }
        } else {
            if (radioButtonCenterRadius.isSelected()) {
                ((MainFrame)(this.getParent())).
                        addPlotITS_CentreRadius(new IntervalNamesCentreRadius(comboBoxCenter.getSelectedItem().toString(),
                                comboBoxRadius.getSelectedItem().toString()));
            } else {
                ((MainFrame)(this.getParent())).
                        addPlotITS_LowerUpper(new IntervalNamesLowerUpper(comboBoxLowerBound.getSelectedItem().toString(),
                                comboBoxUpperBound.getSelectedItem().toString()));
            }
        }

        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_buttonOKPlotITSActionPerformed

    private void buttonCancelPlotITSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelPlotITSActionPerformed
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_buttonCancelPlotITSActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancelPlotITS;
    private javax.swing.ButtonGroup buttonGroupLbUbCenterRadius;
    private static javax.swing.JButton buttonOKPlotITS;
    private javax.swing.JComboBox<String> comboBoxCenter;
    private javax.swing.JComboBox<String> comboBoxLowerBound;
    private javax.swing.JComboBox<String> comboBoxRadius;
    private javax.swing.JComboBox<String> comboBoxUpperBound;
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
        for (int i = 0; i < comboBoxLowerBound.getItemCount(); i++) {
            if (! colnamesCopy.contains(comboBoxLowerBound.getItemAt(i))) {
                //assuming all four comboBoxes have the same values, we only need to check one of them
                containsAll = false;
                break;
            } else {
                colnamesCopy.remove(comboBoxLowerBound.getItemAt(i));
            }
        }
        
        if (! colnamesCopy.isEmpty()) {
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

    private void toggleComponents(boolean trueFalse) {
        labelCenter.setEnabled(! trueFalse);
        labelRadius.setEnabled(! trueFalse);
        comboBoxCenter.setEnabled(! trueFalse);
        comboBoxRadius.setEnabled(! trueFalse);
        labelLB.setEnabled(trueFalse);
        labelUB.setEnabled(trueFalse);
        comboBoxLowerBound.setEnabled(trueFalse);
        comboBoxUpperBound.setEnabled(trueFalse);
    }
}
