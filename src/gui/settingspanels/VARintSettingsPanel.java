package gui.settingspanels;

import java.util.ArrayList;
import java.util.List;
import params.Params;
import utils.Utils;

public class VARintSettingsPanel extends SettingsPanel {

    private static List<String> colNames = new ArrayList<>();
    
    public VARintSettingsPanel() {
        initComponents();
        for (String colname : VARintSettingsPanel.colNames) {
            comboBoxCenter.addItem(colname);
            comboBoxRadius.addItem(colname);
        }
    }
    
    public static void setColNames(List<String> colNames) {
        VARintSettingsPanel.colNames = colNames;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel134 = new javax.swing.JLabel();
        textFieldLag = new javax.swing.JTextField();
        jLabel136 = new javax.swing.JLabel();
        comboBoxType = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        comboBoxCenter = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        comboBoxRadius = new javax.swing.JComboBox();

        jLabel134.setText("Lag:");

        textFieldLag.setText("1");

        jLabel136.setText("Type of deterministic regressors to include:");

        comboBoxType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "const", "trend", "both", "none" }));

        jLabel1.setText("Center:");

        comboBoxCenter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));

        jLabel2.setText("Radius:");

        comboBoxRadius.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel134)
                .addGap(4, 4, 4)
                .addComponent(textFieldLag, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel136)
                .addGap(4, 4, 4)
                .addComponent(comboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(comboBoxRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(comboBoxCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboBoxCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboBoxRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel134))
                    .addComponent(textFieldLag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel136))
                    .addComponent(comboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboBoxCenter;
    private javax.swing.JComboBox comboBoxRadius;
    private javax.swing.JComboBox comboBoxType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField textFieldLag;
    // End of variables declaration//GEN-END:variables

    public String getType() {
        return comboBoxType.getSelectedItem().toString();
    }
    
    public String getCenter() {
        return comboBoxCenter.getSelectedItem().toString();
    }
    
    public String getRadius() {
        return comboBoxRadius.getSelectedItem().toString();
    }
    
    public String getLag() {
        return textFieldLag.getText();
    }

    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) {
        SettingsPanel.setSomethingOneValue(classss, resultList, "setCenter", String.class, getCenter());
        SettingsPanel.setSomethingOneValue(classss, resultList, "setRadius", String.class, getRadius());
        SettingsPanel.setSomethingList(classss, resultList, "setLag", Integer.class, Utils.getIntegersOrDefault(getLag()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setType", String.class, getType());
    }
}
