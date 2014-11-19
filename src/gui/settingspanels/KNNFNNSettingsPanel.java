package gui.settingspanels;

import java.util.List;
import javax.swing.SpinnerNumberModel;
import params.Params;
import utils.FieldsParser;

public class KNNFNNSettingsPanel extends SettingsPanel {

    /**
     * Creates new form KNNFNNSettingsPanel
     */
    public KNNFNNSettingsPanel() {
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

        jLabel46 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        textFieldLag = new javax.swing.JTextField();
        spinnerNumNeighbours = new javax.swing.JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        jLabel46.setText("Number of neighbours considered:");

        jLabel7.setText("Lag:");

        textFieldLag.setText("1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel46)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spinnerNumNeighbours)
                    .addComponent(textFieldLag, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(spinnerNumNeighbours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(textFieldLag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSpinner spinnerNumNeighbours;
    private javax.swing.JTextField textFieldLag;
    // End of variables declaration//GEN-END:variables

    public String getNumNeighbours() {
        return spinnerNumNeighbours.getValue().toString();
    }
    
    public String getLag() {
        return textFieldLag.getText();
    }

    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) {
        SettingsPanel.setSomethingList(classss, resultList, "setNumNeighbours", Integer.class, FieldsParser.parseIntegers(getNumNeighbours()));
        SettingsPanel.setSomethingList(classss, resultList, "setLag", Integer.class, FieldsParser.parseIntegers(getLag()));
    }
}
