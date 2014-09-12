package gui;

import java.awt.CardLayout;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import models.TrainAndTestReport;
import utils.Utils;

public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
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

        panelEverything = new javax.swing.JTabbedPane();
        panelChart = new javax.swing.JPanel();
        comboBoxColnames = new javax.swing.JComboBox();
        buttonPlotColname = new javax.swing.JButton();
        panelPlot = new javax.swing.JPanel();
        buttonACF = new javax.swing.JButton();
        buttonPACF = new javax.swing.JButton();
        panelData = new javax.swing.JPanel();
        scrollPaneData = new javax.swing.JScrollPane();
        jTableData = new javax.swing.JTable();
        panelAnalysisSettings = new javax.swing.JPanel();
        paneSettingsMethods = new javax.swing.JTabbedPane();
        paneSettingsMethodsMLP = new javax.swing.JPanel();
        jLabelRPkg = new javax.swing.JLabel();
        comboBoxRPackage = new javax.swing.JComboBox();
        panelSettingsMLPPackage = new javax.swing.JPanel();
        panelSettingsMLPPackage_nnetar = new javax.swing.JPanel();
        jLabelPercTrain = new javax.swing.JLabel();
        sliderPercentTrain = new javax.swing.JSlider();
        textFieldPercentTrain = new javax.swing.JTextField();
        jLabelPercentSign = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        textFieldNumNodesInHiddenSingleLayer = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        textFieldNumNonSeasonalLags = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        textFieldNumSeasonalLags = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        textFieldNumReps = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        textFieldLambda = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        textFieldNumForecasts = new javax.swing.JTextField();
        panelSettingsMLPPackage_neuralnet = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        paneSettingsMethodsARIMA = new javax.swing.JPanel();
        jLabelARIMA = new javax.swing.JLabel();
        panelRunOutside = new javax.swing.JPanel();
        comboBoxColnamesSummary = new javax.swing.JComboBox();
        buttonSummaryColname = new javax.swing.JButton();
        panelSummary = new javax.swing.JPanel();
        jLabelTrainingInfo = new javax.swing.JLabel();
        checkBoxRunMLP = new javax.swing.JCheckBox();
        checkBoxRunARIMA = new javax.swing.JCheckBox();
        menuBarMain = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuFileLoad = new javax.swing.JMenuItem();
        menuFileExit = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        comboBoxColnames.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));

        buttonPlotColname.setText("Plot data");
        buttonPlotColname.setEnabled(false);
        buttonPlotColname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotColnameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPlotLayout = new javax.swing.GroupLayout(panelPlot);
        panelPlot.setLayout(panelPlotLayout);
        panelPlotLayout.setHorizontalGroup(
            panelPlotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 766, Short.MAX_VALUE)
        );
        panelPlotLayout.setVerticalGroup(
            panelPlotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 441, Short.MAX_VALUE)
        );

        buttonACF.setText("Autocorrelation Plot");
        buttonACF.setEnabled(false);
        buttonACF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonACFActionPerformed(evt);
            }
        });

        buttonPACF.setText("Partial Autocorrelation Plot");
        buttonPACF.setEnabled(false);
        buttonPACF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPACFActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelChartLayout = new javax.swing.GroupLayout(panelChart);
        panelChart.setLayout(panelChartLayout);
        panelChartLayout.setHorizontalGroup(
            panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChartLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelChartLayout.createSequentialGroup()
                        .addComponent(comboBoxColnames, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonPlotColname)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonACF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonPACF))
                    .addComponent(panelPlot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        panelChartLayout.setVerticalGroup(
            panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChartLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxColnames, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonPlotColname)
                    .addComponent(buttonACF)
                    .addComponent(buttonPACF))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelPlot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelEverything.addTab("Chart", panelChart);

        jTableData.setModel(dataTableModel);
        scrollPaneData.setViewportView(jTableData);

        javax.swing.GroupLayout panelDataLayout = new javax.swing.GroupLayout(panelData);
        panelData.setLayout(panelDataLayout);
        panelDataLayout.setHorizontalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneData, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE)
        );
        panelDataLayout.setVerticalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneData, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
        );

        panelEverything.addTab("Data", panelData);

        paneSettingsMethods.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jLabelRPkg.setText("R package:");

        comboBoxRPackage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "nnetar", "neuralnet" }));
        comboBoxRPackage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxRPackageActionPerformed(evt);
            }
        });

        panelSettingsMLPPackage.setLayout(new java.awt.CardLayout());

        jLabelPercTrain.setText("Portion of data to use for training:");

        sliderPercentTrain.setPaintTicks(true);
        sliderPercentTrain.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderPercentTrainStateChanged(evt);
            }
        });

        textFieldPercentTrain.setText("" + sliderPercentTrain.getValue());
        textFieldPercentTrain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldPercentTrainActionPerformed(evt);
            }
        });

        jLabelPercentSign.setText("%");

        jLabel3.setText("Number of nodes in the (single) hidden layer:");

        textFieldNumNodesInHiddenSingleLayer.setText("<default>");

        jLabel1.setText("Number of non-seasonal lags to be used as inputs:");

        textFieldNumNonSeasonalLags.setText("<default>");
        textFieldNumNonSeasonalLags.setToolTipText("The default is the optimal number of lags (according to the AIC) for a linear AR(p) model.");

        jLabel2.setText("Number of seasonal lags to be used as inputs:");

        textFieldNumSeasonalLags.setText("<default>");

        jLabel4.setText("Number of repetitions:");

        textFieldNumReps.setText("<default>");

        jLabel5.setText("Lambda (Box-Cox transformation):");

        textFieldLambda.setText("<default>");

        jLabel7.setText("Number of forecasts:");

        textFieldNumForecasts.setText("1");

        javax.swing.GroupLayout panelSettingsMLPPackage_nnetarLayout = new javax.swing.GroupLayout(panelSettingsMLPPackage_nnetar);
        panelSettingsMLPPackage_nnetar.setLayout(panelSettingsMLPPackage_nnetarLayout);
        panelSettingsMLPPackage_nnetarLayout.setHorizontalGroup(
            panelSettingsMLPPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMLPPackage_nnetarLayout.createSequentialGroup()
                .addGroup(panelSettingsMLPPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelPercTrain)
                    .addGroup(panelSettingsMLPPackage_nnetarLayout.createSequentialGroup()
                        .addGroup(panelSettingsMLPPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addGroup(panelSettingsMLPPackage_nnetarLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(sliderPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(8, 8, 8)
                        .addGroup(panelSettingsMLPPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelSettingsMLPPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(textFieldNumForecasts)
                                .addComponent(textFieldLambda, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(textFieldNumReps, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(textFieldNumSeasonalLags, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(textFieldNumNodesInHiddenSingleLayer, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(textFieldNumNonSeasonalLags))
                            .addComponent(textFieldPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelPercentSign)))
                .addGap(0, 421, Short.MAX_VALUE))
        );
        panelSettingsMLPPackage_nnetarLayout.setVerticalGroup(
            panelSettingsMLPPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMLPPackage_nnetarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelPercTrain)
                .addGap(5, 5, 5)
                .addGroup(panelSettingsMLPPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSettingsMLPPackage_nnetarLayout.createSequentialGroup()
                        .addComponent(sliderPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelSettingsMLPPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(textFieldNumNodesInHiddenSingleLayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelSettingsMLPPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(textFieldNumNonSeasonalLags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelSettingsMLPPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(textFieldNumSeasonalLags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelSettingsMLPPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textFieldPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelPercentSign)))
                .addGap(18, 18, 18)
                .addGroup(panelSettingsMLPPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(textFieldNumReps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelSettingsMLPPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(textFieldLambda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelSettingsMLPPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(textFieldNumForecasts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(142, Short.MAX_VALUE))
        );

        panelSettingsMLPPackage.add(panelSettingsMLPPackage_nnetar, "panelSettingsMLPPackage_nnetar");

        jLabel6.setText("(TODO)");

        javax.swing.GroupLayout panelSettingsMLPPackage_neuralnetLayout = new javax.swing.GroupLayout(panelSettingsMLPPackage_neuralnet);
        panelSettingsMLPPackage_neuralnet.setLayout(panelSettingsMLPPackage_neuralnetLayout);
        panelSettingsMLPPackage_neuralnetLayout.setHorizontalGroup(
            panelSettingsMLPPackage_neuralnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMLPPackage_neuralnetLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(733, Short.MAX_VALUE))
        );
        panelSettingsMLPPackage_neuralnetLayout.setVerticalGroup(
            panelSettingsMLPPackage_neuralnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMLPPackage_neuralnetLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(394, Short.MAX_VALUE))
        );

        panelSettingsMLPPackage.add(panelSettingsMLPPackage_neuralnet, "panelSettingsMLPPackage_neuralnet");

        javax.swing.GroupLayout paneSettingsMethodsMLPLayout = new javax.swing.GroupLayout(paneSettingsMethodsMLP);
        paneSettingsMethodsMLP.setLayout(paneSettingsMethodsMLPLayout);
        paneSettingsMethodsMLPLayout.setHorizontalGroup(
            paneSettingsMethodsMLPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsMLPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsMLPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSettingsMLPPackage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(paneSettingsMethodsMLPLayout.createSequentialGroup()
                        .addGroup(paneSettingsMethodsMLPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelRPkg)
                            .addComponent(comboBoxRPackage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        paneSettingsMethodsMLPLayout.setVerticalGroup(
            paneSettingsMethodsMLPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsMLPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelRPkg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBoxRPackage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSettingsMLPPackage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        paneSettingsMethods.addTab("MLP", paneSettingsMethodsMLP);

        jLabelARIMA.setText("(TODO)");

        javax.swing.GroupLayout paneSettingsMethodsARIMALayout = new javax.swing.GroupLayout(paneSettingsMethodsARIMA);
        paneSettingsMethodsARIMA.setLayout(paneSettingsMethodsARIMALayout);
        paneSettingsMethodsARIMALayout.setHorizontalGroup(
            paneSettingsMethodsARIMALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsARIMALayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelARIMA)
                .addContainerGap(753, Short.MAX_VALUE))
        );
        paneSettingsMethodsARIMALayout.setVerticalGroup(
            paneSettingsMethodsARIMALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsARIMALayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelARIMA)
                .addContainerGap(462, Short.MAX_VALUE))
        );

        paneSettingsMethods.addTab("ARIMA", paneSettingsMethodsARIMA);

        javax.swing.GroupLayout panelAnalysisSettingsLayout = new javax.swing.GroupLayout(panelAnalysisSettings);
        panelAnalysisSettings.setLayout(panelAnalysisSettingsLayout);
        panelAnalysisSettingsLayout.setHorizontalGroup(
            panelAnalysisSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paneSettingsMethods)
        );
        panelAnalysisSettingsLayout.setVerticalGroup(
            panelAnalysisSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paneSettingsMethods)
        );

        panelEverything.addTab("Analysis settings", panelAnalysisSettings);

        comboBoxColnamesSummary.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));

        buttonSummaryColname.setText("Train and Test");
        buttonSummaryColname.setEnabled(false);
        buttonSummaryColname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSummaryColnameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSummaryLayout = new javax.swing.GroupLayout(panelSummary);
        panelSummary.setLayout(panelSummaryLayout);
        panelSummaryLayout.setHorizontalGroup(
            panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelSummaryLayout.setVerticalGroup(
            panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 441, Short.MAX_VALUE)
        );

        checkBoxRunMLP.setText("MLP");

        checkBoxRunARIMA.setText("ARIMA");

        javax.swing.GroupLayout panelRunOutsideLayout = new javax.swing.GroupLayout(panelRunOutside);
        panelRunOutside.setLayout(panelRunOutsideLayout);
        panelRunOutsideLayout.setHorizontalGroup(
            panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSummary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                        .addComponent(comboBoxColnamesSummary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonSummaryColname)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTrainingInfo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBoxRunMLP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBoxRunARIMA)
                        .addGap(0, 581, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelRunOutsideLayout.setVerticalGroup(
            panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxColnamesSummary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSummaryColname)
                    .addComponent(jLabelTrainingInfo)
                    .addComponent(checkBoxRunMLP)
                    .addComponent(checkBoxRunARIMA))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSummary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelEverything.addTab("Run", panelRunOutside);

        menuFile.setText("File");

        menuFileLoad.setText("Load");
        menuFileLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileLoadActionPerformed(evt);
            }
        });
        menuFile.add(menuFileLoad);

        menuFileExit.setText("Exit");
        menuFileExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileExitActionPerformed(evt);
            }
        });
        menuFile.add(menuFileExit);

        menuBarMain.add(menuFile);

        menuEdit.setText("Edit");
        menuBarMain.add(menuEdit);

        setJMenuBar(menuBarMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelEverything)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelEverything)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuFileLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileLoadActionPerformed

        //TODO odkomentovat------------------------------------------------------
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setMultiSelectionEnabled(false);
//        FileFilter fileFilterXLS = new FileFilter() {
//
//            @Override
//            public boolean accept(File f) {
//                if (f.isDirectory()) {
//                    return true;
//                } else {
//                    String extension = f.getName().substring(f.getName().lastIndexOf('.'));
//                    return ".xls".equals(extension) || ".xlsx".equals(extension);
//                }
//            }
//            
//            @Override
//            public String getDescription() {
//                return "MS Excel files (.xls, .xlsx)";
//            }
//        };
//        fileChooser.setFileFilter(fileFilterXLS);
//        if (evt.getSource() == menuFileLoad) {
//            switch (fileChooser.showOpenDialog(this)) {
//                case JFileChooser.APPROVE_OPTION:
//                    this.loadedFile = fileChooser.getSelectedFile();
                                               this.loadedFile = new File("C:\\Users\\Andrejka\\Documents\\fi_muni\\phd\\3d_semester-madrid\\w02\\javier redondo\\brent_prices_its_2000_2014.xlsx");
                    dataTableModel.openFile(loadedFile);
                    dataTableModel.fireTableStructureChanged();
                    for (String colname : dataTableModel.getColnames()) {
                        comboBoxColnames.addItem(colname);
                        comboBoxColnamesSummary.addItem(colname);
                    }
                    if (! dataTableModel.getColnames().isEmpty()) {
                        buttonPlotColname.setEnabled(true);
                        buttonSummaryColname.setEnabled(true);
                        buttonACF.setEnabled(true);
                        buttonPACF.setEnabled(true);
                    }
//                    break;
//                case JFileChooser.CANCEL_OPTION:
//                default:
//                    this.loadedFile = null;
//            }
//        }
        
        //this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_menuFileLoadActionPerformed

    private void menuFileExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileExitActionPerformed
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_menuFileExitActionPerformed

    private void buttonPlotColnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotColnameActionPerformed
        outputPlotGeneral("plot.ts", "");
    }//GEN-LAST:event_buttonPlotColnameActionPerformed

    private void textFieldPercentTrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldPercentTrainActionPerformed
        try {
            int val = Integer.parseInt(textFieldPercentTrain.getText());
            sliderPercentTrain.setValue(val);
        } catch (NumberFormatException e) {
            //TODO log
        }
    }//GEN-LAST:event_textFieldPercentTrainActionPerformed

    private void sliderPercentTrainStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderPercentTrainStateChanged
        textFieldPercentTrain.setText("" + sliderPercentTrain.getValue());
    }//GEN-LAST:event_sliderPercentTrainStateChanged

    private void buttonSummaryColnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSummaryColnameActionPerformed
        String colname = comboBoxColnamesSummary.getSelectedItem().toString();
        
        //zohnat vsetky parametre pre dany model:
        //TODO: vymysliet nejak vseobecne! zatial je to natvrdo pre nnetar
        Map<String, Integer> params = new HashMap<>();
        params.put("percentTrain", sliderPercentTrain.getValue());
        //TODO chytat vynimky, resp. validator na cisla
        params.put("numNodesHidden", Utils.getIntegerOrDefault(textFieldNumNodesInHiddenSingleLayer));
        params.put("numSeasonalLags", Utils.getIntegerOrDefault(textFieldNumSeasonalLags));
        params.put("numNonSeasonalLags", Utils.getIntegerOrDefault(textFieldNumNonSeasonalLags));
        params.put("numReps", Utils.getIntegerOrDefault(textFieldNumReps));
        params.put("lambda", Utils.getIntegerOrDefault(textFieldLambda));
        params.put("numForecasts", Utils.getIntegerOrDefault(textFieldNumForecasts)); //tieto sa pripocitaju k testovacim forecasts!
        
        //tu treba natrenovat a spocitat a zobrazit vsetko
        TrainAndTestReport report = dataTableModel.trainAndTest(colname, comboBoxRPackage.getSelectedItem().toString(), params);
        
        panelSummary.removeAll();
        JTable errorMeasuresTable = new JTable();
        errorMeasuresTable.setSize(panelSummary.getWidth(), panelSummary.getHeight());
        errorMeasuresTable.setModel(new ErrorMeasuresTableModel(report.getErrorMeasures()));
        errorMeasuresTable.setDefaultRenderer(Object.class, new TableBothHeadersCellColorRenderer());
        errorMeasuresTable.setVisible(true);
        panelSummary.add(errorMeasuresTable);
        
        
        //show Forecast plot
        panelPlot.removeAll();
        JPanel frame = new JPanel();
        frame.setSize(report.getForecastPlot().getIconWidth(), report.getForecastPlot().getIconHeight());
        JLabel label = new JLabel(report.getForecastPlot());
        frame.add(label);
        frame.setVisible(true);
        panelPlot.add(frame);
        this.repaint();
    }//GEN-LAST:event_buttonSummaryColnameActionPerformed

    private void comboBoxRPackageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxRPackageActionPerformed
        CardLayout card = (CardLayout)panelSettingsMLPPackage.getLayout();
        card.show(panelSettingsMLPPackage, "panelSettingsMLPPackage_" + comboBoxRPackage.getSelectedItem().toString());
        panelSettingsMLPPackage.repaint();
    }//GEN-LAST:event_comboBoxRPackageActionPerformed

    private void buttonACFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonACFActionPerformed
        outputPlotGeneral("acf", "");
    }//GEN-LAST:event_buttonACFActionPerformed

    private void buttonPACFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPACFActionPerformed
        outputPlotGeneral("pacf", "");
    }//GEN-LAST:event_buttonPACFActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonACF;
    private javax.swing.JButton buttonPACF;
    private javax.swing.JButton buttonPlotColname;
    private javax.swing.JButton buttonSummaryColname;
    private javax.swing.JCheckBox checkBoxRunARIMA;
    private javax.swing.JCheckBox checkBoxRunMLP;
    private javax.swing.JComboBox comboBoxColnames;
    private javax.swing.JComboBox comboBoxColnamesSummary;
    private javax.swing.JComboBox comboBoxRPackage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelARIMA;
    private javax.swing.JLabel jLabelPercTrain;
    private javax.swing.JLabel jLabelPercentSign;
    private javax.swing.JLabel jLabelRPkg;
    private javax.swing.JLabel jLabelTrainingInfo;
    private javax.swing.JTable jTableData;
    private javax.swing.JMenuBar menuBarMain;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem menuFileExit;
    private javax.swing.JMenuItem menuFileLoad;
    private javax.swing.JTabbedPane paneSettingsMethods;
    private javax.swing.JPanel paneSettingsMethodsARIMA;
    private javax.swing.JPanel paneSettingsMethodsMLP;
    private javax.swing.JPanel panelAnalysisSettings;
    private javax.swing.JPanel panelChart;
    private javax.swing.JPanel panelData;
    private javax.swing.JTabbedPane panelEverything;
    private javax.swing.JPanel panelPlot;
    private javax.swing.JPanel panelRunOutside;
    private javax.swing.JPanel panelSettingsMLPPackage;
    private javax.swing.JPanel panelSettingsMLPPackage_neuralnet;
    private javax.swing.JPanel panelSettingsMLPPackage_nnetar;
    private javax.swing.JPanel panelSummary;
    private javax.swing.JScrollPane scrollPaneData;
    private javax.swing.JSlider sliderPercentTrain;
    private javax.swing.JTextField textFieldLambda;
    private javax.swing.JTextField textFieldNumForecasts;
    private javax.swing.JTextField textFieldNumNodesInHiddenSingleLayer;
    private javax.swing.JTextField textFieldNumNonSeasonalLags;
    private javax.swing.JTextField textFieldNumReps;
    private javax.swing.JTextField textFieldNumSeasonalLags;
    private javax.swing.JTextField textFieldPercentTrain;
    // End of variables declaration//GEN-END:variables

    private File loadedFile;
    private final DataTableModel dataTableModel = new DataTableModel();

    private void outputPlotGeneral(String plotFunction, String additionalArgs) {
        String colname = comboBoxColnames.getSelectedItem().toString();
        
        ImageIcon plotImage = dataTableModel.producePlotGeneral(colname, plotFunction, additionalArgs);
        
        panelPlot.removeAll();
        JPanel frame = new JPanel();
        frame.setSize(plotImage.getIconWidth(), plotImage.getIconHeight());
        JLabel label = new JLabel(plotImage);
        frame.add(label);
        frame.setVisible(true);
        panelPlot.add(frame);
        this.repaint(); //aby sa tam zobrazil ten obrazok hned, a nie o tri roky
    }

}
