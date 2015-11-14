package gui.settingspanels;

import gui.dialogs.DialogAddIntervalExplanatoryVar;
import gui.dialogs.DialogAddIntervalOutputVar;
import gui.tablemodels.IntervalExplVarsTableModel;
import gui.tablemodels.IntervalOutVarsTableModel;
import gui.MainFrame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import models.params.Params;
import utils.Const;
import utils.FieldsParser;
import utils.Improvable;
import utils.IntervalExplanatoryVariable;
import utils.IntervalOutputVariable;
import utils.imlp.dist.Distance;

public class IntMLPCcodeSettingsPanel extends SettingsPanel {
    
    /**
     * Creates new form IntMLPCcodeSettingsPanel
     */
    public IntMLPCcodeSettingsPanel() {
        initComponents();
        
        setButtons(buttonAddDistance, buttonAddExplVar, buttonAddOutVar, 
                buttonRemoveDistances, buttonRemoveExplVar, buttonRemoveOutVar);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelDistance = new DistanceSettingsPanel();
        buttonAddDistance = new javax.swing.JButton();
        buttonRemoveDistances = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        buttonAddExplVar = new javax.swing.JButton();
        buttonRemoveExplVar = new javax.swing.JButton();
        scrollPaneExplVars = new javax.swing.JScrollPane();
        tableExplVars = new javax.swing.JTable();
        jLabel36 = new javax.swing.JLabel();
        textFieldNumHidden = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        buttonAddOutVar = new javax.swing.JButton();
        buttonRemoveOutVar = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        scrollPaneOutVars = new javax.swing.JScrollPane();
        tableOutVars = new javax.swing.JTable();
        jLabel38 = new javax.swing.JLabel();
        textFieldNumIterations = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        textFieldNumNetsToTrain = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listDistancesUsed = new javax.swing.JList();
        panelBestModelCriterion = new gui.settingspanels.BestModelCriterionIntervalSettingsPanel();

        buttonAddDistance.setText("-> Use this distance ->");
        buttonAddDistance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddDistanceActionPerformed(evt);
            }
        });

        buttonRemoveDistances.setText("Remove selected distance");
        buttonRemoveDistances.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveDistancesActionPerformed(evt);
            }
        });

        jLabel34.setText("Explanatory variables:");

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

        tableExplVars.setModel(new gui.tablemodels.IntervalExplVarsTableModel());
        tableExplVars.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollPaneExplVars.setViewportView(tableExplVars);

        jLabel36.setText("Number of neurons in the (single) hidden layer:");

        textFieldNumHidden.setText("1");

        jLabel33.setText("Output variables:");

        buttonAddOutVar.setText("Add");
        buttonAddOutVar.setEnabled(false);
        buttonAddOutVar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddOutVarActionPerformed(evt);
            }
        });

        buttonRemoveOutVar.setText("Remove selected");
        buttonRemoveOutVar.setEnabled(false);
        buttonRemoveOutVar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveOutVarActionPerformed(evt);
            }
        });

        jLabel45.setText("(Only one variable allowed. Can be extended to more.)");
        jLabel45.setEnabled(false);

        tableOutVars.setModel(new gui.tablemodels.IntervalOutVarsTableModel());
        scrollPaneOutVars.setViewportView(tableOutVars);

        jLabel38.setText("Number of iterations:");

        textFieldNumIterations.setText("500");

        jLabel58.setText("Number of networks to train:");

        textFieldNumNetsToTrain.setText("1");

        jLabel39.setText("Regularization term:");

        jLabel40.setText("(For now, disabled and set to 0.001)");
        jLabel40.setEnabled(false);

        listDistancesUsed.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(listDistancesUsed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(panelDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonAddDistance))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonAddExplVar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonRemoveExplVar)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonRemoveDistances))
                    .addComponent(scrollPaneExplVars, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel36)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldNumHidden, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addGap(31, 31, 31)
                        .addComponent(buttonAddOutVar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonRemoveOutVar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel45))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38)
                            .addComponent(jLabel39))
                        .addGap(142, 142, 142)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel40)
                            .addComponent(textFieldNumIterations, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(244, 244, 244)
                                    .addComponent(textFieldNumNetsToTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel58))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panelBestModelCriterion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(scrollPaneOutVars, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(panelDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(buttonAddDistance)))
                            .addGap(27, 27, 27)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel34)
                                .addComponent(buttonAddExplVar)
                                .addComponent(buttonRemoveExplVar)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(buttonRemoveDistances)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneExplVars, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(textFieldNumHidden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(buttonAddOutVar)
                    .addComponent(buttonRemoveOutVar)
                    .addComponent(jLabel45))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneOutVars, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38)
                    .addComponent(textFieldNumIterations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel58)
                            .addComponent(textFieldNumNetsToTrain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39)
                            .addComponent(jLabel40)))
                    .addComponent(panelBestModelCriterion, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAddDistanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddDistanceActionPerformed
        Distance distance = ((DistanceSettingsPanel)panelDistance).getSelectedDistance();
        ((DefaultListModel)(listDistancesUsed.getModel())).addElement(distance);
    }//GEN-LAST:event_buttonAddDistanceActionPerformed

    private void buttonRemoveDistancesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveDistancesActionPerformed
        List<Object> values = listDistancesUsed.getSelectedValuesList();
        for (Object val : values) {
            ((DefaultListModel)(listDistancesUsed.getModel())).removeElement(val);
        }
    }//GEN-LAST:event_buttonRemoveDistancesActionPerformed

    private void buttonAddExplVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddExplVarActionPerformed
        DialogAddIntervalExplanatoryVar dialogAddExplVar = new DialogAddIntervalExplanatoryVar(((MainFrame)SwingUtilities.windowForComponent(this)), true);
        dialogAddExplVar.setExplVarsTableModel((IntervalExplVarsTableModel)(tableExplVars.getModel()));
        dialogAddExplVar.setVisible(true);
    }//GEN-LAST:event_buttonAddExplVarActionPerformed

    private void buttonRemoveExplVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveExplVarActionPerformed
        ((IntervalExplVarsTableModel)(tableExplVars.getModel())).removeRow(tableExplVars.getSelectedRow());
    }//GEN-LAST:event_buttonRemoveExplVarActionPerformed

    private void buttonAddOutVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddOutVarActionPerformed
        DialogAddIntervalOutputVar dialogAddOutVar = new DialogAddIntervalOutputVar(((MainFrame)SwingUtilities.windowForComponent(this)), true);
        dialogAddOutVar.setOutVarsTableModel((IntervalOutVarsTableModel)(tableOutVars.getModel()));
        dialogAddOutVar.setVisible(true);
        buttonAddOutVar.setEnabled(false);
    }//GEN-LAST:event_buttonAddOutVarActionPerformed

    private void buttonRemoveOutVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveOutVarActionPerformed
        ((IntervalOutVarsTableModel)(tableOutVars.getModel())).removeRow(tableOutVars.getSelectedRow());
        if (((IntervalOutVarsTableModel)(tableOutVars.getModel())).getVariables().isEmpty()) {
            buttonAddOutVar.setEnabled(true);
        }
    }//GEN-LAST:event_buttonRemoveOutVarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAddDistance;
    private javax.swing.JButton buttonAddExplVar;
    private javax.swing.JButton buttonAddOutVar;
    private javax.swing.JButton buttonRemoveDistances;
    private javax.swing.JButton buttonRemoveExplVar;
    private javax.swing.JButton buttonRemoveOutVar;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList listDistancesUsed;
    private javax.swing.JPanel panelBestModelCriterion;
    private javax.swing.JPanel panelDistance;
    private javax.swing.JScrollPane scrollPaneExplVars;
    private javax.swing.JScrollPane scrollPaneOutVars;
    private javax.swing.JTable tableExplVars;
    private javax.swing.JTable tableOutVars;
    private javax.swing.JTextField textFieldNumHidden;
    private javax.swing.JTextField textFieldNumIterations;
    private javax.swing.JTextField textFieldNumNetsToTrain;
    // End of variables declaration//GEN-END:variables

    
    public List<Distance> getDistancesUsed() {
        List<Distance> distancesUsed = new ArrayList<>();
        for (Object obj : ((DefaultListModel)listDistancesUsed.getModel()).toArray()) {
            distancesUsed.add((Distance) obj);
        }
        
        return distancesUsed;
    }
    
//    public Distance getSelectedDistance() {
//        return ((DistanceSettingsPanel) panelDistance).getSelectedDistance();
//    }
    
    public List<IntervalExplanatoryVariable> getExplVars() {
        return ((IntervalExplVarsTableModel) tableExplVars.getModel()).getVariables();
    }
    
    public List<IntervalOutputVariable> getOutVars() {
        return ((IntervalOutVarsTableModel) tableOutVars.getModel()).getVariables();
    }
    
    public String getNumHidden() {
        return textFieldNumHidden.getText();
    }
    
    public String getNumIterations() {
        return textFieldNumIterations.getText();
    }
    
    public String getNumNetsToTrain() {
        return textFieldNumNetsToTrain.getText();
    }
    
    public Improvable getBestModelCriterion() {
        return ((BestModelCriterionIntervalSettingsPanel)panelBestModelCriterion).getBestModelCriterion();
    }

    @Override
    public void enableAllElements(boolean trueFalse) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) throws IllegalArgumentException {
        if (getExplVars().isEmpty() || getOutVars().isEmpty() || getDistancesUsed().isEmpty()) {
            JOptionPane.showMessageDialog(null, "At least one explanatory, one output variable and one distance need to be selected for the " + Const.INTERVAL_MLP_C_CODE + " to run.");
            throw new IllegalArgumentException(Const.INTERVAL_MLP_C_CODE + " insufficient expl/out/dist params");
        }
        
        SettingsPanel.setSomethingList(classss, resultList, "setDistance", Distance.class, getDistancesUsed());
        SettingsPanel.setSomethingList(classss, resultList, "setNumNodesHidden", Integer.class, FieldsParser.parseIntegers(getNumHidden()));
        SettingsPanel.setSomethingList(classss, resultList, "setNumIterations", Integer.class, FieldsParser.parseIntegers(getNumIterations()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setExplVars", List.class, getExplVars());
        SettingsPanel.setSomethingOneValue(classss, resultList, "setOutVars", List.class, getOutVars());
        SettingsPanel.setSomethingList(classss, resultList, "setNumNetworks", Integer.class, FieldsParser.parseIntegers(getNumNetsToTrain()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setCriterion", Improvable.class, getBestModelCriterion());
    }
}