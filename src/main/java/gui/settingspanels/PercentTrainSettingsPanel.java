package gui.settingspanels;

import models.params.Params;

import java.util.List;

public class PercentTrainSettingsPanel extends SettingsPanel {

    /**
     * Creates new form PercentTrainSettingsPanel
     */
    public PercentTrainSettingsPanel() {
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

        jLabelPercTrain = new javax.swing.JLabel();
        sliderPercentTrain = new javax.swing.JSlider();
        textFieldPercentTrain = new javax.swing.JTextField();
        jLabelPercentSign = new javax.swing.JLabel();

        jLabelPercTrain.setText("Portion of data to use for training:");

        sliderPercentTrain.setMaximum(99);
        sliderPercentTrain.setMinimum(1);
        sliderPercentTrain.setPaintTicks(true);
        sliderPercentTrain.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderPercentTrainStateChanged(evt);
            }
        });

        textFieldPercentTrain.setText("" + sliderPercentTrain.getValue());
        textFieldPercentTrain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldPercentTrainActionPerformed(evt);
            }
        });

        jLabelPercentSign.setText("%");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabelPercTrain)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sliderPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelPercentSign))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(textFieldPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabelPercentSign))
            .addComponent(sliderPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelPercTrain)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sliderPercentTrainStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderPercentTrainStateChanged
        textFieldPercentTrain.setText("" + sliderPercentTrain.getValue());
    }//GEN-LAST:event_sliderPercentTrainStateChanged

    private void textFieldPercentTrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldPercentTrainActionPerformed
        try {
            int val = Integer.parseInt(textFieldPercentTrain.getText());
            sliderPercentTrain.setValue(val);
        } catch (NumberFormatException e) {
            //TODO log
        }
    }//GEN-LAST:event_textFieldPercentTrainActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelPercTrain;
    private javax.swing.JLabel jLabelPercentSign;
    private javax.swing.JSlider sliderPercentTrain;
    private javax.swing.JTextField textFieldPercentTrain;
    // End of variables declaration//GEN-END:variables

    public String getPercentTrain() {
        return textFieldPercentTrain.getText();
    }

    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
