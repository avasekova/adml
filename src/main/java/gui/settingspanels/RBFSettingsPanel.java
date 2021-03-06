package gui.settingspanels;

import gui.MainFrame;
import gui.dialogs.DialogAddCrispExplanatoryVar;
import gui.tablemodels.CrispVariablesTableModel;
import models.Model;
import models.params.Params;
import utils.CrispVariable;
import utils.FieldsParser;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class RBFSettingsPanel extends SettingsPanel {

    /**
     * Creates new form RBFSettingsPanel
     */
    public RBFSettingsPanel() {
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

        jLabel140 = new javax.swing.JLabel();
        textFieldNumHidden = new javax.swing.JTextField();
        jLabel162 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        buttonAddExplVar = new javax.swing.JButton();
        buttonRemoveExplVar = new javax.swing.JButton();
        scrollPaneExplVars = new javax.swing.JScrollPane();
        tableExplVars = new javax.swing.JTable();
        jLabel139 = new javax.swing.JLabel();
        comboBoxOutputVar = new javax.swing.JComboBox();
        jLabel142 = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        textFieldMaxIt = new javax.swing.JTextField();

        jLabel140.setText("Number of neurons in the hidden layer:");

        textFieldNumHidden.setText("2");

        jLabel162.setForeground(new java.awt.Color(255, 0, 0));
        jLabel162.setText("For some reason crashes with num=1.");

        jLabel137.setText("Explanatory variables:");

        buttonAddExplVar.setText("Add");
        buttonAddExplVar.setEnabled(false);
        buttonAddExplVar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddExplVarActionPerformed(evt);
            }
        });

        buttonRemoveExplVar.setText("Remove selected");
        buttonRemoveExplVar.setEnabled(false);
        buttonRemoveExplVar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveExplVarActionPerformed(evt);
            }
        });

        tableExplVars.setModel(new CrispVariablesTableModel());
        tableExplVars.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollPaneExplVars.setViewportView(tableExplVars);

        jLabel139.setText("Output variable:");
        jLabel139.setEnabled(false);

        comboBoxOutputVar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
        comboBoxOutputVar.setEnabled(false);

        jLabel142.setForeground(new java.awt.Color(255, 51, 0));
        jLabel142.setText("(Takes the one selected in CTS Run.)");

        jLabel141.setText("Maximum number of iterations:");

        textFieldMaxIt.setText("1000");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel137)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonAddExplVar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonRemoveExplVar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel139)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboBoxOutputVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel142))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel140)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldNumHidden, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel162))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel141)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldMaxIt, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(scrollPaneExplVars, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel140)
                    .addComponent(textFieldNumHidden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel162))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel137)
                    .addComponent(buttonAddExplVar)
                    .addComponent(buttonRemoveExplVar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneExplVars, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel139)
                    .addComponent(comboBoxOutputVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel142))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel141)
                    .addComponent(textFieldMaxIt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAddExplVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddExplVarActionPerformed
        DialogAddCrispExplanatoryVar dialogAddExplVar = new DialogAddCrispExplanatoryVar(((MainFrame)SwingUtilities.windowForComponent(this)), true);
        dialogAddExplVar.setExplVarsTableModel((CrispVariablesTableModel)(tableExplVars.getModel()));
        dialogAddExplVar.setVisible(true);
    }//GEN-LAST:event_buttonAddExplVarActionPerformed

    private void buttonRemoveExplVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveExplVarActionPerformed
        ((CrispVariablesTableModel)(tableExplVars.getModel())).removeRow(tableExplVars.getSelectedRow());
    }//GEN-LAST:event_buttonRemoveExplVarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAddExplVar;
    private javax.swing.JButton buttonRemoveExplVar;
    private javax.swing.JComboBox comboBoxOutputVar;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JScrollPane scrollPaneExplVars;
    private javax.swing.JTable tableExplVars;
    private javax.swing.JTextField textFieldMaxIt;
    private javax.swing.JTextField textFieldNumHidden;
    // End of variables declaration//GEN-END:variables

//    public List<CrispVariable> getOutVars() {
//        CrispVariable outVar = new CrispVariable(); //takes vals from CTS Run
//        outVar.setName(comboBoxOutputVar.getSelectedItem().toString() + comboBoxOutputVar.getSelectedIndex());
//        outVar.setFieldName(comboBoxOutputVar.getSelectedItem().toString());
//        List<CrispVariable> outVars = new ArrayList<>();
//        outVars.add(outVar);
//        return outVars;
//    }
    
    public List<CrispVariable> getExplVars() {
        return ((CrispVariablesTableModel)tableExplVars.getModel()).getVariables().stream()
                .map(e -> (CrispVariable) e)
                .collect(Collectors.toList());
    }
    
    public String getMaxIt() {
        return textFieldMaxIt.getText();
    }
    
    public String getNumHidden() {
        return textFieldNumHidden.getText();
    }

    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) throws IllegalArgumentException {
        if (getExplVars().isEmpty()) {
            JOptionPane.showMessageDialog(null, "At least one explanatory variable needs to be selected for the RBF to run.");
            throw new IllegalArgumentException(Model.RBF + " insufficient expl vars");
        }
        
        SettingsPanel.setSomethingList(classss, resultList, "setNumNodesHidden", Integer.class, FieldsParser.parseIntegers(getNumHidden()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setExplVars", List.class, getExplVars());
        //for now, OutVars are set in getParamsRBF. TODO move here?
//        CrispVariable outVar = new CrispVariable(); //takes vals from CTS Run
//        outVar.setName(comboBoxColnamesRun.getSelectedItem().toString() + comboBoxColnamesRun.getSelectedIndex());
//        outVar.setFieldName(comboBoxColnamesRun.getSelectedItem().toString());
//        List<CrispVariable> outVarList = new ArrayList<>();
//        outVarList.add(outVar);
//        SettingsPanel.setSomethingOneValue(classss, resultList, "setOutVars",
//                List.class, outVarList);
        SettingsPanel.setSomethingList(classss, resultList, "setMaxIterations", Integer.class, FieldsParser.parseIntegers(getMaxIt()));
    }
}
