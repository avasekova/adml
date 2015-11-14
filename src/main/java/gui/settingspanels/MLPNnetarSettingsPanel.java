package gui.settingspanels;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import models.params.Params;
import utils.Utils;
import utils.FieldsParser;

public class MLPNnetarSettingsPanel extends SettingsPanel {

    public MLPNnetarSettingsPanel() {
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        textFieldLambda = new javax.swing.JTextField();
        textFieldNumReps = new javax.swing.JTextField();
        textFieldNumSeasonalLags = new javax.swing.JTextField();
        textFieldNumNonSeasonalLags = new javax.swing.JTextField();
        textFieldNumNodesHidden = new javax.swing.JTextField();

        jLabel1.setText("Number of nodes in the (single) hidden layer:");

        jLabel2.setText("Number of non-seasonal lags to be used as inputs:");

        jLabel3.setText("Number of seasonal lags to be used as inputs:");

        jLabel4.setText("Number of repetitions:");

        jLabel5.setText("Lambda (Box-Cox transformation):");

        textFieldLambda.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
                if ("<default>".equals(textFieldLambda.getText())) {
                    textFieldLambda.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                if (textFieldLambda.getText().length() < 1) {
                    textFieldLambda.setText("<default>");
                }
            }
        });
        textFieldLambda.setText("<default>");

        textFieldNumReps.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
                if ("<default>".equals(textFieldNumReps.getText())) {
                    textFieldNumReps.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                if (textFieldNumReps.getText().length() < 1) {
                    textFieldNumReps.setText("<default>");
                }
            }
        });
        textFieldNumReps.setText("20");

        textFieldNumSeasonalLags.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
                if ("<default>".equals(textFieldNumSeasonalLags.getText())) {
                    textFieldNumSeasonalLags.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                if (textFieldNumSeasonalLags.getText().length() < 1) {
                    textFieldNumSeasonalLags.setText("<default>");
                }
            }
        });
        textFieldNumSeasonalLags.setText("<default>");

        textFieldNumNonSeasonalLags.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
                if ("<default>".equals(textFieldNumNonSeasonalLags.getText())) {
                    textFieldNumNonSeasonalLags.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                if (textFieldNumNonSeasonalLags.getText().length() < 1) {
                    textFieldNumNonSeasonalLags.setText("<default>");
                }
            }
        });
        textFieldNumNonSeasonalLags.setText("1");
        textFieldNumNonSeasonalLags.setToolTipText("The default is the optimal number of lags (according to the AIC) for a linear AR(p) model.");

        textFieldNumNodesHidden.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
                if ("<default>".equals(textFieldNumNodesHidden.getText())) {
                    textFieldNumNodesHidden.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                if (textFieldNumNodesHidden.getText().length() < 1) {
                    textFieldNumNodesHidden.setText("<default>");
                }
            }
        });
        textFieldNumNodesHidden.setText("<default>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(textFieldNumNodesHidden, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textFieldNumNonSeasonalLags, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textFieldNumSeasonalLags, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textFieldNumReps, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textFieldLambda, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel1))
                    .addComponent(textFieldNumNodesHidden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel2))
                    .addComponent(textFieldNumNonSeasonalLags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(textFieldNumSeasonalLags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(textFieldNumReps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(textFieldLambda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField textFieldLambda;
    private javax.swing.JTextField textFieldNumNodesHidden;
    private javax.swing.JTextField textFieldNumNonSeasonalLags;
    private javax.swing.JTextField textFieldNumReps;
    private javax.swing.JTextField textFieldNumSeasonalLags;
    // End of variables declaration//GEN-END:variables

    public String getLambda() {
        return textFieldLambda.getText();
    }
    
    public String getNumNodesHidden() {
        return textFieldNumNodesHidden.getText();
    }
    
    public String getNumNonSeasonalLags() {
        return textFieldNumNonSeasonalLags.getText();
    }
    
    public String getNumReps() {
        return textFieldNumReps.getText();
    }
    
    public String getNumSeasonalLags() {
        return textFieldNumSeasonalLags.getText();
    }
    
    @Override
    public void enableAllElements(boolean trueFalse) {
        jLabel1.setEnabled(trueFalse);
        jLabel2.setEnabled(trueFalse);
        jLabel3.setEnabled(trueFalse);
        jLabel4.setEnabled(trueFalse);
        jLabel5.setEnabled(trueFalse);
        textFieldLambda.setEnabled(trueFalse);
        textFieldNumNodesHidden.setEnabled(trueFalse);
        textFieldNumNonSeasonalLags.setEnabled(trueFalse);
        textFieldNumReps.setEnabled(trueFalse);
        textFieldNumSeasonalLags.setEnabled(trueFalse);
    }

    @Override
    public <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList) {
        //TODO toto mozno vymysliet inak :( classss by mala byt furt NnetarParams, a neviem, ci je dobre alebo zle, ze
        //   je tu tym padom povolene dodat aj nieco ine.
        SettingsPanel.setSomethingList(classss, resultList, "setNumNodesHidden", Integer.class, FieldsParser.parseIntegers(getNumNodesHidden()));
        SettingsPanel.setSomethingList(classss, resultList, "setNumSeasonalLags", Integer.class, FieldsParser.parseIntegers(getNumSeasonalLags()));
        SettingsPanel.setSomethingList(classss, resultList, "setNumNonSeasonalLags", Integer.class, FieldsParser.parseIntegers(getNumNonSeasonalLags()));
        SettingsPanel.setSomethingList(classss, resultList, "setNumReps", Integer.class, FieldsParser.parseIntegers(getNumReps()));
        SettingsPanel.setSomethingOneValue(classss, resultList, "setLambda", Double.class, Utils.getDoubleOrDefault(getLambda()));
    }
}