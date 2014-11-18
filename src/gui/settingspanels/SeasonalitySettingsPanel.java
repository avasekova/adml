package gui.settingspanels;

import java.util.List;
import params.Params;

public class SeasonalitySettingsPanel extends SettingsPanel {

    /**
     * Creates new form SeasonalitySettingsPanel
     */
    public SeasonalitySettingsPanel() {
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

        comboBoxTypeSeasonality = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        comboBoxTypeSeasonality.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Daily", "Weekly", "Monthly", "Quarterly", "Yearly" }));

        jLabel1.setText("data");

        jLabel2.setText("(works only sometimes...)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(comboBoxTypeSeasonality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1))
                    .addComponent(jLabel2))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboBoxTypeSeasonality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jLabel2))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboBoxTypeSeasonality;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables

    //TODO spravit krajsie!
    public String getFrequency() {
        switch (comboBoxTypeSeasonality.getSelectedItem().toString()) {
            case "Daily":
                return "frequency=7";
            case "Weekly":
                return "frequency=52";
            case "Monthly":
                return "frequency=12";
            case "Quarterly":
                return "frequency=4";
            case "Yearly":
                return "frequency=1";
        }
        
        return "frequency=1";
    }
    
    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) {
        SettingsPanel.setSomethingOneValue(classss, resultList, "setFrequency", String.class, getFrequency());
    }
}