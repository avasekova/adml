package gui.subpanels;

import analysis.StatisticalTests;
import gui.MainFrame;
import gui.PlotContainer;
import gui.PlotDrawer;
import gui.files.Exporter;
import gui.tablemodels.DataTableModel;
import org.rosuda.javaGD.JGDBufferedPanel;

import javax.swing.*;
import java.util.List;

public class TestsSubPanel extends javax.swing.JPanel implements PlotContainer {

    public TestsSubPanel() {
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

        buttonNormalityTests = new javax.swing.JButton();
        buttonStationarityTest = new javax.swing.JButton();
        buttonStructBreaks = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        textFieldMaxStructBreaks = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        listColnamesTests = new javax.swing.JList<>();
        jSeparator15 = new javax.swing.JSeparator();
        tabbedPaneAnalysisPlotsTests = new javax.swing.JTabbedPane();
        buttonExportTextAreaTests = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        textAreaTests = new javax.swing.JTextArea();
        buttonExportTestsPlots = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        buttonKMOTest = new javax.swing.JButton();
        buttonBartlettsTest = new javax.swing.JButton();

        buttonNormalityTests.setText("Tests for normality");
        buttonNormalityTests.setEnabled(false);
        buttonNormalityTests.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNormalityTestsActionPerformed(evt);
            }
        });

        buttonStationarityTest.setText("Tests for stationarity");
        buttonStationarityTest.setEnabled(false);
        buttonStationarityTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStationarityTestActionPerformed(evt);
            }
        });

        buttonStructBreaks.setText("Find structural breaks");
        buttonStructBreaks.setEnabled(false);
        buttonStructBreaks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStructBreaksActionPerformed(evt);
            }
        });

        jLabel19.setText("Max");

        textFieldMaxStructBreaks.setText("2");

        jLabel20.setText("structural breaks");

        jLabel21.setForeground(new java.awt.Color(255, 0, 0));
        jLabel21.setText("TODO: seasonal");

        listColnamesTests.setModel(new DefaultListModel());
        jScrollPane9.setViewportView(listColnamesTests);

        jSeparator15.setOrientation(javax.swing.SwingConstants.VERTICAL);

        buttonExportTextAreaTests.setText("Save the contents of the box below:");
        buttonExportTextAreaTests.setEnabled(false);
        buttonExportTextAreaTests.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExportTextAreaTestsActionPerformed(evt);
            }
        });

        jScrollPane3.setPreferredSize(new java.awt.Dimension(164, 89));

        textAreaTests.setEditable(false);
        textAreaTests.setColumns(20);
        textAreaTests.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        textAreaTests.setLineWrap(true);
        textAreaTests.setRows(5);
        textAreaTests.setFocusable(false);
        textAreaTests.setMaximumSize(new java.awt.Dimension(32767, 32767));
        textAreaTests.setOpaque(false);
        jScrollPane3.setViewportView(textAreaTests);

        buttonExportTestsPlots.setText("Export currently shown plot");
        buttonExportTestsPlots.setEnabled(false);
        buttonExportTestsPlots.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExportTestsPlotsActionPerformed(evt);
            }
        });

        jLabel34.setText("(Broken - only exports the last tab.)");

        buttonKMOTest.setText("KMO test");
        buttonKMOTest.setEnabled(false);
        buttonKMOTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonKMOTestActionPerformed(evt);
            }
        });

        buttonBartlettsTest.setText("Bartlett's test");
        buttonBartlettsTest.setEnabled(false);
        buttonBartlettsTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBartlettsTestActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1374, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(buttonNormalityTests)
                                .addComponent(buttonStationarityTest)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(buttonStructBreaks)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel19)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(textFieldMaxStructBreaks, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel21)
                                        .addComponent(jLabel20)))
                                .addComponent(buttonKMOTest)
                                .addComponent(buttonBartlettsTest)))
                        .addComponent(buttonExportTextAreaTests)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 532, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tabbedPaneAnalysisPlotsTests)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(buttonExportTestsPlots)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel34)
                            .addGap(0, 440, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(buttonNormalityTests)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(buttonStationarityTest)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel21)
                                    .addGap(7, 7, 7)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonStructBreaks)
                                        .addComponent(jLabel19)
                                        .addComponent(textFieldMaxStructBreaks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel20))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(buttonKMOTest)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(buttonBartlettsTest)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(buttonExportTextAreaTests)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE))
                        .addComponent(jSeparator15, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonExportTestsPlots)
                                .addComponent(jLabel34))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tabbedPaneAnalysisPlotsTests)))
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonNormalityTestsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNormalityTestsActionPerformed
        textAreaTests.setText(StatisticalTests.normalProbabilityTests(listColnamesTests.getSelectedValuesList()));

        //and show the normal probability plots:
        List<JGDBufferedPanel> plots = PlotDrawer.drawSimpleFctionToGrid("qqnorm", listColnamesTests.getSelectedValuesList(), DataTableModel.getInstance(),
                tabbedPaneAnalysisPlotsTests.getWidth(), tabbedPaneAnalysisPlotsTests.getHeight());
        setPlots(plots);
        MainFrame.getInstance().setPlotRanges(0, 0);
    }//GEN-LAST:event_buttonNormalityTestsActionPerformed

    private void buttonStationarityTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStationarityTestActionPerformed
        textAreaTests.setText(StatisticalTests.stationarityTests(listColnamesTests.getSelectedValuesList()));
    }//GEN-LAST:event_buttonStationarityTestActionPerformed

    private void buttonStructBreaksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStructBreaksActionPerformed
        int breaks = 5;
        try {
            breaks = Integer.parseInt(textFieldMaxStructBreaks.getText());
        } catch (NumberFormatException e) {
            //logger.error("Exception", e); // TODO: review logging
        }

        //the last item is the info!
        List<String> plots = StatisticalTests.structuralBreaksTests(listColnamesTests.getSelectedValuesList(), breaks);
        //TODO fix this (in the method that returns it!)
            String structBreaksInfo = plots.get(plots.size() - 1);
            plots.remove(plots.size() - 1);

            List<JGDBufferedPanel> panels = PlotDrawer.drawToGrid(tabbedPaneAnalysisPlotsTests.getWidth(),
                tabbedPaneAnalysisPlotsTests.getHeight(), plots, 1, 1);
            setPlots(panels);

            //and print the info
            textAreaTests.setText(structBreaksInfo);

            MainFrame.getInstance().setPlotRanges(0, 0);
            buttonExportTestsPlots.setEnabled(true);
    }//GEN-LAST:event_buttonStructBreaksActionPerformed

    private void buttonExportTextAreaTestsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExportTextAreaTestsActionPerformed
        MainFrame.getInstance().exportTextArea(evt, buttonExportTextAreaTests, textAreaTests);
    }//GEN-LAST:event_buttonExportTextAreaTestsActionPerformed

    private void buttonExportTestsPlotsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExportTestsPlotsActionPerformed
        //TODO export all tabs

        if (evt.getSource() == buttonExportTestsPlots) {
            Exporter.exportPlot(tabbedPaneAnalysisPlotsTests);
        }
    }//GEN-LAST:event_buttonExportTestsPlotsActionPerformed

    private void buttonKMOTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonKMOTestActionPerformed
        textAreaTests.setText(StatisticalTests.KMOTest(listColnamesTests.getSelectedValuesList()));
    }//GEN-LAST:event_buttonKMOTestActionPerformed

    private void buttonBartlettsTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBartlettsTestActionPerformed
        textAreaTests.setText(StatisticalTests.bartlettsTest(listColnamesTests.getSelectedValuesList()));
    }//GEN-LAST:event_buttonBartlettsTestActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonBartlettsTest;
    private javax.swing.JButton buttonExportTestsPlots;
    private javax.swing.JButton buttonExportTextAreaTests;
    private javax.swing.JButton buttonKMOTest;
    private javax.swing.JButton buttonNormalityTests;
    private javax.swing.JButton buttonStationarityTest;
    private javax.swing.JButton buttonStructBreaks;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JList<String> listColnamesTests;
    private javax.swing.JTabbedPane tabbedPaneAnalysisPlotsTests;
    private javax.swing.JTextArea textAreaTests;
    private javax.swing.JTextField textFieldMaxStructBreaks;
    // End of variables declaration//GEN-END:variables

    public JButton getButtonBartlettsTest() {
        return buttonBartlettsTest;
    }

    public JButton getButtonExportTestsPlots() {
        return buttonExportTestsPlots;
    }

    public JButton getButtonExportTextAreaTests() {
        return buttonExportTextAreaTests;
    }

    public JButton getButtonKMOTest() {
        return buttonKMOTest;
    }

    public JButton getButtonNormalityTests() {
        return buttonNormalityTests;
    }

    public JButton getButtonStationarityTest() {
        return buttonStationarityTest;
    }

    public JButton getButtonStructBreaks() {
        return buttonStructBreaks;
    }

    public JScrollPane getjScrollPane3() {
        return jScrollPane3;
    }

    public JScrollPane getjScrollPane9() {
        return jScrollPane9;
    }

    public JList<String> getListColnamesTests() {
        return listColnamesTests;
    }

    public JPanel getPanelTestsOutside() {
        return this;
    }

    public JTextArea getTextAreaTests() {
        return textAreaTests;
    }

    public JTextField getTextFieldMaxStructBreaks() {
        return textFieldMaxStructBreaks;
    }

    @Override
    public void setPlots(List<JGDBufferedPanel> plots) {
        tabbedPaneAnalysisPlotsTests.removeAll();
        int i = 0;
        for (JGDBufferedPanel p : plots) {
            tabbedPaneAnalysisPlotsTests.addTab("Page "+(++i), p);
        }
        tabbedPaneAnalysisPlotsTests.repaint();
    }
}
