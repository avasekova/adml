package gui.settingspanels;

import java.util.List;
import javax.swing.SpinnerNumberModel;
import models.params.Params;

public class KNNCustomSettingsPanel extends SettingsPanel {

    /**
     * Creates new form KNNCustomSettingsPanel
     */
    public KNNCustomSettingsPanel() {
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

        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        comboBoxCombination = new javax.swing.JComboBox();
        comboBoxDistance = new javax.swing.JComboBox();
        textFieldLag = new javax.swing.JTextField();
        textFieldLengthHistory = new javax.swing.JTextField();
        spinnerNumNeighbours = new javax.swing.JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        jLabel68 = new javax.swing.JLabel();

        jLabel65.setText("Number of neighbours considered:");

        jLabel66.setText("Length of history considered:");

        jLabel67.setText("Lag:");

        jLabel69.setText("Distance measure:");

        jLabel70.setText("Neighbour combination function:");

        comboBoxCombination.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "average" }));

        comboBoxDistance.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "absolute difference" }));

        textFieldLag.setText("1");

        textFieldLengthHistory.setText("1");

        jLabel68.setForeground(new java.awt.Color(255, 0, 0));
        jLabel68.setText("so far only one expl. var. and only autoregression");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel65)
                    .addComponent(jLabel66)
                    .addComponent(jLabel67)
                    .addComponent(jLabel69)
                    .addComponent(jLabel70))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(spinnerNumNeighbours)
                            .addComponent(textFieldLengthHistory)
                            .addComponent(textFieldLag, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel68))
                    .addComponent(comboBoxDistance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxCombination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65)
                    .addComponent(spinnerNumNeighbours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(textFieldLengthHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67)
                    .addComponent(textFieldLag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel68))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel69)
                    .addComponent(comboBoxDistance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxCombination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel70)))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboBoxCombination;
    private javax.swing.JComboBox comboBoxDistance;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JSpinner spinnerNumNeighbours;
    private javax.swing.JTextField textFieldLag;
    private javax.swing.JTextField textFieldLengthHistory;
    // End of variables declaration//GEN-END:variables

    public String getCombination() {
        return comboBoxCombination.getSelectedItem().toString();
    }
    
    public String getDistance() {
        return comboBoxDistance.getSelectedItem().toString();
    }
    
    public String getNumNeighbours() {
        return spinnerNumNeighbours.getValue().toString();
    }
    
    public String getLag() {
        return textFieldLag.getText();
    }
    
    public String getLengthHistory() {
        return textFieldLengthHistory.getText();
    }

    @Override
    public void enableAllElements(boolean trueFalse) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) {
//        params.setNumNeighbours(Integer.parseInt(((KNNCustomSettingsPanel)panelSettingsKNNoptions_custom).getNumNeighbours()));
//        params.setLengthHistory(Integer.parseInt(((KNNCustomSettingsPanel)panelSettingsKNNoptions_custom).getLengthHistory()));
//        params.setLag(Integer.parseInt(((KNNCustomSettingsPanel)panelSettingsKNNoptions_custom).getLag()));
//        params.setDistanceMethodName(((KNNCustomSettingsPanel)panelSettingsKNNoptions_custom).getDistance());
//        params.setCombinationMethodName(((KNNCustomSettingsPanel)panelSettingsKNNoptions_custom).getCombination());
    }
}
