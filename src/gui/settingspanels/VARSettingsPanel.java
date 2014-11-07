package gui.settingspanels;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import params.Params;
import utils.Utils;

public class VARSettingsPanel extends SettingsPanel {
    
    private static List<String> colNames = new ArrayList<>();

    /**
     * Creates new form VARSettingsPanel
     */
    public VARSettingsPanel() {
        initComponents();
        for (String colname : VARSettingsPanel.colNames) {
            ((DefaultListModel<String>)(listEndogenousVars.getModel())).addElement(colname);
        }
        
    }
    
    public static void setColNames(List<String> colNames) {
        VARSettingsPanel.colNames = colNames;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel135 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        listEndogenousVars = new javax.swing.JList();
        jLabel134 = new javax.swing.JLabel();
        textFieldLag = new javax.swing.JTextField();
        jLabel136 = new javax.swing.JLabel();
        comboBoxType = new javax.swing.JComboBox();

        jLabel135.setText("Endogenous variables:");

        listEndogenousVars.setModel(new DefaultListModel());
        jScrollPane5.setViewportView(listEndogenousVars);

        jLabel134.setText("Lag:");

        textFieldLag.setText("1");

        jLabel136.setText("Type of deterministic regressors to include:");

        comboBoxType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "const", "trend", "both", "none" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel135)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel134)
                .addGap(4, 4, 4)
                .addComponent(textFieldLag, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel136)
                .addGap(4, 4, 4)
                .addComponent(comboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel135)
                .addGap(6, 6, 6)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
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
    private javax.swing.JComboBox comboBoxType;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JList listEndogenousVars;
    private javax.swing.JTextField textFieldLag;
    // End of variables declaration//GEN-END:variables


    public String getType() {
        return comboBoxType.getSelectedItem().toString();
    }
    
    public List<String> getEndogenousVars() {
        return listEndogenousVars.getSelectedValuesList();
    }
    
    public String getLag() {
        return textFieldLag.getText();
    }

    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) {
        if (getEndogenousVars().isEmpty()) {
            JOptionPane.showMessageDialog(null, "At least one endogenous variable needs to be selected for the VAR to run.");
            throw new IllegalArgumentException("VAR insufficient endogenous vars");
        }
        
        SettingsPanel.setSomethingOneValue(classss, resultList, "setEndogenousVars", List.class, getEndogenousVars());
        SettingsPanel.setSomethingList(classss, resultList, "setLag", Integer.class, Utils.getIntegersOrDefault(getLag()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setType", String.class, getType());
        
        //pozor, zbytok sa setuje v getParamsVAR
    }
}
