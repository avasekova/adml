package gui.settingspanels;

import models.params.Params;
import utils.Improvable;

import java.util.List;

public class BestModelCriterionIntervalSettingsPanel extends SettingsPanel {

    /**
     * Creates new form BestModelCriterionSettingsPanel
     */
    public BestModelCriterionIntervalSettingsPanel() {
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

        jLabel1 = new javax.swing.JLabel();
        comboBoxCriteria = new javax.swing.JComboBox();

        jLabel1.setText("Choose the best according to:");

        comboBoxCriteria.setModel(new javax.swing.DefaultComboBoxModel(utils.BestModelCriterionInterval.getValues()));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBoxCriteria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(comboBoxCriteria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboBoxCriteria;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    public Improvable getBestModelCriterion() {
        return (Improvable)(comboBoxCriteria.getSelectedItem());
    }

    @Override
    public void enableAllElements(boolean trueFalse) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) {
        SettingsPanel.setSomethingOneValue(classss, resultList, "setCriterion", Improvable.class, getBestModelCriterion());
    }
}
