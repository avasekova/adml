package gui.settingspanels;

import java.util.List;
import javax.swing.SpinnerNumberModel;
import models.params.Params;
import utils.FieldsParser;

public class KNNkknnSettingsPanel extends SettingsPanel {

    /**
     * Creates new form KNNkknnSettingsPanel
     */
    public KNNkknnSettingsPanel() {
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

        jLabel73 = new javax.swing.JLabel();
        spinnerNumNeighbours = new javax.swing.JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        jLabel75 = new javax.swing.JLabel();

        jLabel73.setText("Maximum number of neighbours considered:");

        jLabel75.setText("(optimizes the number of neighbours)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(spinnerNumNeighbours, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel75))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel73)
                .addComponent(spinnerNumNeighbours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel75))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JSpinner spinnerNumNeighbours;
    // End of variables declaration//GEN-END:variables

    public String getNumNeighbours() {
        return spinnerNumNeighbours.getValue().toString(); //TODO obmedzit v SpinnerNumberModele maximalnu hodnotu na nieco neprekracujuce velkost suboru
    }

    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) {
        SettingsPanel.setSomethingList(classss, resultList, "setMaxNeighbours", Integer.class, FieldsParser.parseIntegers(getNumNeighbours()));
    }

}
