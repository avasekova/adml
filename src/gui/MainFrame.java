package gui;

import analysis.AnalysisUtils;
import gui.dialogs.DialogAddCrispExplanatoryVar;
import gui.dialogs.DialogAddIntervalExplanatoryVar;
import gui.dialogs.DialogAddIntervalOutputVar;
import gui.dialogs.DialogConvertLbUbCenterRadius;
import gui.dialogs.DialogLbUbCenterRadius;
import gui.dialogs.DialogTooManyModels;
import gui.filefilters.FileFilterEps;
import gui.filefilters.FileFilterPdf;
import gui.filefilters.FileFilterPng;
import gui.filefilters.FileFilterPs;
import gui.filefilters.FileFilterXlsXlsxCsv;
import gui.filefilters.RFileFilter;
import gui.renderers.PlotLegendTurnOFFableListCellRenderer;
import gui.renderers.PlotLegendTurnOFFableListElement;
import gui.renderers.ErrorTableCellRenderer;
import gui.renderers.PlotLegendSimpleListElement;
import gui.settingspanels.ARIMASettingsPanel;
import gui.settingspanels.BNNSettingsPanel;
import gui.settingspanels.BestModelCriterionIntervalSettingsPanel;
import gui.settingspanels.BinomPropSettingsPanel;
import gui.settingspanels.DistanceSettingsPanel;
import gui.settingspanels.HoltSettingsPanel;
import gui.settingspanels.HoltWintersSettingsPanel;
import gui.settingspanels.IntHoltSettingsPanel;
import gui.settingspanels.IntMLPCcodeSettingsPanel;
import gui.settingspanels.KNNCustomSettingsPanel;
import gui.settingspanels.KNNFNNSettingsPanel;
import gui.settingspanels.KNNkknnSettingsPanel;
import gui.settingspanels.KNNmyownSettingsPanel;
import gui.settingspanels.MAvgSettingsPanel;
import gui.settingspanels.MLPNnetSettingsPanel;
import gui.settingspanels.MLPNnetarSettingsPanel;
import gui.settingspanels.PercentTrainSettingsPanel;
import gui.settingspanels.RBFSettingsPanel;
import gui.settingspanels.SESSettingsPanel;
import gui.settingspanels.SettingsPanel;
import gui.settingspanels.VARSettingsPanel;
import gui.settingspanels.VARintSettingsPanel;
import gui.tablemodels.AnalysisBatchTableModel;
import gui.tablemodels.CombinationWeightsTableModel;
import gui.tablemodels.DataTableModel;
import gui.tablemodels.ErrorMeasuresTableModel_CTS;
import gui.tablemodels.ErrorMeasuresTableModel_ITS;
import gui.tablemodels.ForecastValsTableModel;
import gui.tablemodels.PredictionIntsTableModel;
import gui.tablemodels.ResidualsTableModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.SAVE_DIALOG;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;
import models.Arima;
import models.BNN;
import models.BNNint;
import models.Forecastable;
import models.Holt;
import models.HoltInt;
import models.HoltWinters;
import models.HoltWintersInt;
import models.Hybrid;
import models.IntervalHolt;
import models.IntervalMLPCcode;
import models.KNNfnn;
import models.KNNkknn;
import models.KNNmyown;
import models.MAvg;
import models.MLPintNnet;
import models.MLPintNnetar;
import models.Neuralnet;
import models.Nnet;
import models.Nnetar;
import models.RBF;
import models.RBFint;
import models.RandomWalk;
import models.RandomWalkInterval;
import models.SES;
import models.SESint;
import models.TrainAndTestReport;
import models.TrainAndTestReportCrisp;
import models.TrainAndTestReportInterval;
import models.VARint;
import models.avg.Average;
import models.avg.AverageCoverageEfficiency;
import models.avg.AverageEqCenterEqLogRadius;
import models.avg.AverageMDE;
import models.avg.AverageSimple;
import models.avg.AverageTheilsU;
import models.avg.AveragesConfig;
import models.avg.Median;
import models.params.AnalysisBatchLine;
import models.params.ArimaParams;
import models.params.BasicStats;
import models.params.BNNParams;
import models.params.BNNintParams;
import models.params.BinomPropParams;
import models.params.HoltIntParams;
import models.params.HoltParams;
import models.params.HoltWintersIntParams;
import models.params.HoltWintersParams;
import models.params.HybridParams;
import models.params.IntervalHoltParams;
import models.params.IntervalMLPCcodeParams;
import models.params.KNNfnnParams;
import models.params.KNNkknnParams;
import models.params.KNNmyownParams;
import models.params.MAvgParams;
import models.params.MLPintNnetParams;
import models.params.MLPintNnetarParams;
import models.params.NeuralnetParams;
import models.params.NnetParams;
import models.params.NnetarParams;
import models.params.Params;
import models.params.RBFParams;
import models.params.RBFintParams;
import models.params.RandomWalkIntervalParams;
import models.params.RandomWalkParams;
import models.params.SESParams;
import models.params.SESintParams;
import models.params.VARParams;
import models.params.VARintParams;
import analysis.StatisticalTests;
import analysis.Transformations;
import gui.files.OverwriteFileChooser;
import gui.files.PlotExtensionFileChooser;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.BinomProp;
import org.rosuda.JRI.REXP;
import org.rosuda.javaGD.JGDBufferedPanel;
import utils.Const;
import utils.CrispOutputVariable;
import utils.ExcelWriter;
import utils.FieldsParser;
import utils.Improvable;
import utils.MyRengine;
import utils.Utils;
import utils.imlp.Interval;
import utils.imlp.IntervalNames;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;
import utils.imlp.dist.Distance;
import utils.ugliez.CallParamsDrawPlotGeneral;
import utils.ugliez.CallParamsDrawPlots;
import utils.ugliez.CallParamsDrawPlotsITS;
import utils.ugliez.PlotStateKeeper;


public class MainFrame extends javax.swing.JFrame {

    private static MainFrame INSTANCE = null; //created in main()
    
    private MainFrame() {
        initComponents();
        addComponentsToGroups();
    }
    
    public static synchronized MainFrame getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MainFrame();
        }
        
        return INSTANCE;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup_runFakeIntCRLBUB = new javax.swing.ButtonGroup();
        panelEverything = new javax.swing.JTabbedPane();
        panelCTS = new javax.swing.JPanel();
        buttonPlotColname = new javax.swing.JButton();
        buttonACF = new javax.swing.JButton();
        buttonPACF = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listColnames = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        textAreaPlotBasicStats = new javax.swing.JTextArea();
        jSeparator2 = new javax.swing.JSeparator();
        buttonBoxplots = new javax.swing.JButton();
        buttonHistograms = new javax.swing.JButton();
        tabbedPaneAnalysisPlotsCTS = new javax.swing.JTabbedPane();
        buttonNormProbPlot = new javax.swing.JButton();
        buttonExportAnalysisPlotsCTS = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        buttonExportAnalysisText = new javax.swing.JButton();
        buttonBasicStats = new javax.swing.JButton();
        buttonCTSclearSelection = new javax.swing.JButton();
        panelTransform = new javax.swing.JPanel();
        buttonLogTransformSeries = new javax.swing.JButton();
        buttonDiffSeries = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        listColnamesTransform = new javax.swing.JList();
        buttonRemoveTrend = new javax.swing.JButton();
        buttonAggregateToITS = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        textFieldAggregateToITSevery = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        buttonConvertITSLBUBCR1 = new javax.swing.JButton();
        buttonNormalize = new javax.swing.JButton();
        panelIntTS = new javax.swing.JPanel();
        buttonPlotAddITS = new javax.swing.JButton();
        buttonPlotRemoveITS = new javax.swing.JButton();
        buttonPlotSelectedITS = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        listPlotITSspecs = new javax.swing.JList();
        buttonPlotAllITSScatterplot = new javax.swing.JButton();
        buttonPlotAllITSScatterplotMatrix = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        buttonConvertITSLBUBCR = new javax.swing.JButton();
        buttonITSclearSelection = new javax.swing.JButton();
        panelPlotImage = new javax.swing.JPanel();
        buttonPlotExportPlot = new javax.swing.JButton();
        panelPlot = new javax.swing.JPanel();
        jLabel89 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        textFieldPlotRangeCTSXfrom = new javax.swing.JTextField();
        textFieldPlotRangeCTSYfrom = new javax.swing.JTextField();
        textFieldPlotRangeCTSXto = new javax.swing.JTextField();
        textFieldPlotRangeCTSYto = new javax.swing.JTextField();
        jLabel126 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        buttonPlotRestoreCTSRangeX = new javax.swing.JButton();
        buttonPlotRestoreCTSRangeY = new javax.swing.JButton();
        buttonPlotZoomCTS = new javax.swing.JButton();
        jLabel129 = new javax.swing.JLabel();
        jLabel130 = new javax.swing.JLabel();
        textFieldPlotRangeIntTSXfrom = new javax.swing.JTextField();
        textFieldPlotRangeIntTSYfrom = new javax.swing.JTextField();
        jLabel131 = new javax.swing.JLabel();
        jLabel132 = new javax.swing.JLabel();
        textFieldPlotRangeIntTSXto = new javax.swing.JTextField();
        textFieldPlotRangeIntTSYto = new javax.swing.JTextField();
        buttonPlotRestoreIntTSRangeX = new javax.swing.JButton();
        buttonPlotRestoreIntTSRangeY = new javax.swing.JButton();
        buttonPlotZoomIntTS = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JSeparator();
        scrollPaneListPlotLegend = new javax.swing.JScrollPane();
        listPlotLegend = new javax.swing.JList();
        buttonLegendSelectAll = new javax.swing.JButton();
        buttonLegendSelectNone = new javax.swing.JButton();
        panelTestsOutside = new javax.swing.JPanel();
        buttonNormalityTests = new javax.swing.JButton();
        buttonStationarityTest = new javax.swing.JButton();
        buttonStructBreaks = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        textFieldMaxStructBreaks = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        listColnamesTests = new javax.swing.JList();
        jSeparator15 = new javax.swing.JSeparator();
        tabbedPaneAnalysisPlotsTests = new javax.swing.JTabbedPane();
        buttonExportTextAreaTests = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        textAreaTests = new javax.swing.JTextArea();
        buttonExportTestsPlots = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        panelData = new javax.swing.JPanel();
        scrollPaneData = new javax.swing.JScrollPane();
        jTableData = new javax.swing.JTable();
        panelAnalysisSettings = new javax.swing.JPanel();
        paneSettingsMethods = new javax.swing.JTabbedPane();
        paneSettingsMethodsMLP = new javax.swing.JPanel();
        jLabelRPkg = new javax.swing.JLabel();
        comboBoxRPackage = new javax.swing.JComboBox();
        panelSettingsMLPPackage = new javax.swing.JPanel();
        scrollPane_panelSettingsMLPPackage_nnet = new javax.swing.JScrollPane();
        panelSettingsMLPPackage_nnet = new MLPNnetSettingsPanel();
        panelSettingsMLPPackage_nnetar = new MLPNnetarSettingsPanel();
        panelSettingsMLPPackage_neuralnet = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        panelMLPPercentTrain = new PercentTrainSettingsPanel();
        buttonSettingsAddToBatch_MLP = new javax.swing.JButton();
        paneSettingsMethodsMLPint = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        panelSettingsMLPintPackage = new javax.swing.JPanel();
        panelSettingsMLPintPackage_nnet = new javax.swing.JPanel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel100 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        scrollPane_panelSettingsMLPintPackage_nnet_center = new javax.swing.JScrollPane();
        panelSettingsMLPintPackage_nnet_center = new MLPNnetSettingsPanel();
        scrollPane_panelSettingsMLPintPackage_nnet_radius = new javax.swing.JScrollPane();
        panelSettingsMLPintPackage_nnet_radius = new MLPNnetSettingsPanel();
        panelSettingsMLPintPackage_nnetar = new javax.swing.JPanel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        panelSettingsMLPintPackage_nnetar_center = new MLPNnetarSettingsPanel();
        panelSettingsMLPintPackage_nnetar_radius = new MLPNnetarSettingsPanel();
        jLabelRPkg1 = new javax.swing.JLabel();
        comboBoxRPackageMLPint = new javax.swing.JComboBox();
        panelMLPintSettingsDistance = new DistanceSettingsPanel();
        panelMLPintPercentTrain = new PercentTrainSettingsPanel();
        buttonSettingsAddToBatch_MLPint = new javax.swing.JButton();
        panelBestModelCriterionMLPint = new gui.settingspanels.BestModelCriterionIntervalSettingsPanel();
        jLabel12 = new javax.swing.JLabel();
        textFieldNumNetsToTrainMLPint = new javax.swing.JTextField();
        paneSettingsMethodsIntervalMLP = new javax.swing.JPanel();
        panelSettingsIntervalMLPMode = new javax.swing.JPanel();
        panelSettingsIntervalMLPModeCcode = new IntMLPCcodeSettingsPanel();
        panelSettingsIntervalMLPModeNeuralnet = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        comboBoxIntervalMLPMode = new javax.swing.JComboBox();
        panelIntMLPPercentTrain = new PercentTrainSettingsPanel();
        buttonSettingsAddToBatch_intMLP = new javax.swing.JButton();
        paneSettingsMethodsRBF = new javax.swing.JPanel();
        panelSettingsRBFMain = new RBFSettingsPanel();
        panelRBFPercentTrain = new PercentTrainSettingsPanel();
        buttonSettingsAddToBatch_RBF = new javax.swing.JButton();
        paneSettingsMethodsRBFint = new javax.swing.JPanel();
        jLabel143 = new javax.swing.JLabel();
        jLabel150 = new javax.swing.JLabel();
        jLabel151 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel156 = new javax.swing.JLabel();
        jLabel157 = new javax.swing.JLabel();
        jLabel138 = new javax.swing.JLabel();
        textFieldNumNetworksToTrainRBFint = new javax.swing.JTextField();
        panelRBFintSettingsDistance = new DistanceSettingsPanel();
        panelSettingsRBFint_center = new RBFSettingsPanel();
        panelSettingsRBFint_radius = new RBFSettingsPanel();
        panelRBFintPercentTrain = new PercentTrainSettingsPanel();
        buttonSettingsAddToBatch_RBFint = new javax.swing.JButton();
        panelBestModelCriterionRBFint = new gui.settingspanels.BestModelCriterionIntervalSettingsPanel();
        paneSettingsMethodsARIMA = new javax.swing.JPanel();
        panelSettingsARIMAMain = new ARIMASettingsPanel();
        panelARIMAPercTrain = new PercentTrainSettingsPanel();
        buttonSettingsAddToBatch_ARIMA = new javax.swing.JButton();
        paneSettingsMethodsKNN = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        panelSettingsKNNoptions = new javax.swing.JPanel();
        panelSettingsKNNoptions_FNN = new KNNFNNSettingsPanel();
        panelSettingsKNNoptions_custom = new KNNCustomSettingsPanel();
        panelSettingsKNNoptions_kknn = new KNNkknnSettingsPanel();
        panelSettingsKNNoptions_myown = new KNNmyownSettingsPanel();
        comboBoxKNNoptions = new javax.swing.JComboBox();
        panelKNNPercTrain = new PercentTrainSettingsPanel();
        buttonSettingsAddToBatch_KNN = new javax.swing.JButton();
        panelSettingsMethodsVARint = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        panelVARintInside = new javax.swing.JPanel();
        panelVARintInsideBecause = new VARintSettingsPanel();
        panelVARintPercentTrain = new PercentTrainSettingsPanel();
        panelVARintDistance = new DistanceSettingsPanel();
        buttonSettingsAddToBatch_VARint = new javax.swing.JButton();
        panelSettingsMethodsSES = new javax.swing.JPanel();
        panelSESpercentTrain = new PercentTrainSettingsPanel();
        panelSESmain = new SESSettingsPanel();
        buttonSettingsAddToBatch_SES = new javax.swing.JButton();
        panelSettingsMethodsSESint = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        panelSESintPercentTrain = new PercentTrainSettingsPanel();
        panelSESint_center = new SESSettingsPanel();
        jLabel154 = new javax.swing.JLabel();
        jLabel155 = new javax.swing.JLabel();
        panelSESint_radius = new SESSettingsPanel();
        jSeparator7 = new javax.swing.JSeparator();
        panelSESintDistance = new DistanceSettingsPanel();
        buttonSettingsAddToBatch_SESint = new javax.swing.JButton();
        panelSettingsMethodsMAvg = new javax.swing.JPanel();
        buttonSettingsAddToBatch_MAvg = new javax.swing.JButton();
        panelMAvgMain = new MAvgSettingsPanel();
        panelSettingsMethodsHolt = new javax.swing.JPanel();
        panelHoltPercentTrain = new PercentTrainSettingsPanel();
        panelHoltInside = new HoltSettingsPanel();
        buttonSettingsAddToBatch_Holt = new javax.swing.JButton();
        panelSettingsMethodsHoltInt = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        panelHoltIntPercentTrain = new PercentTrainSettingsPanel();
        panelHoltIntDistance = new DistanceSettingsPanel();
        panelHoltInt_center = new HoltSettingsPanel();
        jLabel152 = new javax.swing.JLabel();
        jLabel153 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        panelHoltInt_radius = new HoltSettingsPanel();
        buttonSettingsAddToBatch_Holtint = new javax.swing.JButton();
        panelSettingsMethodsIntervalHolt = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        panelIntervalHoltPercentTrain = new PercentTrainSettingsPanel();
        panelIntervalHoltDistance = new DistanceSettingsPanel();
        panelIntervalHoltMain = new IntHoltSettingsPanel();
        buttonSettingsAddToBatch_IntervalHolt = new javax.swing.JButton();
        panelSettingsMethodsHoltWinters = new javax.swing.JPanel();
        panelHoltWintersPercentTrain = new PercentTrainSettingsPanel();
        panelHoltWintersInside = new HoltWintersSettingsPanel();
        buttonSettingsAddToBatch_HoltWinters = new javax.swing.JButton();
        panelSettingsMethodsHoltWintersInt = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        panelHoltWintersIntPercentTrain = new PercentTrainSettingsPanel();
        panelHoltWintersIntDistance = new DistanceSettingsPanel();
        jLabel158 = new javax.swing.JLabel();
        panelHoltWintersInt_center = new HoltWintersSettingsPanel();
        jSeparator8 = new javax.swing.JSeparator();
        panelHoltWintersInt_radius = new HoltWintersSettingsPanel();
        jLabel159 = new javax.swing.JLabel();
        buttonSettingsAddToBatch_HoltWintersInt = new javax.swing.JButton();
        paneSettingsMethodsHybrid = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        comboBoxSettingsHybridMethod_center = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        comboBoxSettingsHybridMethod_radius = new javax.swing.JComboBox();
        panelSettingsHybrid_centerMain = new javax.swing.JPanel();
        scrollPane_panelSettingsHybrid_centerMain_MLPnnet = new javax.swing.JScrollPane();
        panelSettingsHybrid_centerMain_MLPnnet = new MLPNnetSettingsPanel();
        panelSettingsHybrid_centerMain_MLPnnetar = new MLPNnetarSettingsPanel();
        panelSettingsHybrid_centerMain_RBF = new RBFSettingsPanel();
        panelSettingsHybrid_centerMain_ARIMA = new ARIMASettingsPanel();
        panelSettingsHybrid_centerMain_KNNFNN = new KNNFNNSettingsPanel();
        panelSettingsHybrid_centerMain_KNNkknn = new KNNkknnSettingsPanel();
        panelSettingsHybrid_centerMain_SES = new SESSettingsPanel();
        panelSettingsHybrid_centerMain_Holt = new HoltSettingsPanel();
        panelSettingsHybrid_centerMain_BNN = new BNNSettingsPanel();
        jSeparator3 = new javax.swing.JSeparator();
        panelSettingsHybrid_radiusMain = new javax.swing.JPanel();
        scrollPane_panelSettingsHybrid_radiusMain_MLPnnet = new javax.swing.JScrollPane();
        panelSettingsHybrid_radiusMain_MLPnnet = new MLPNnetSettingsPanel();
        panelSettingsHybrid_radiusMain_MLPnnetar = new MLPNnetarSettingsPanel();
        panelSettingsHybrid_radiusMain_RBF = new RBFSettingsPanel();
        panelSettingsHybrid_radiusMain_ARIMA = new ARIMASettingsPanel();
        panelSettingsHybrid_radiusMain_KNNFNN = new KNNFNNSettingsPanel();
        panelSettingsHybrid_radiusMain_KNNkknn = new KNNkknnSettingsPanel();
        panelSettingsHybrid_radiusMain_SES = new SESSettingsPanel();
        panelSettingsHybrid_radiusMain_Holt = new HoltSettingsPanel();
        panelSettingsHybrid_radiusMain_BNN = new BNNSettingsPanel();
        panelSettingsHybridPercentTrain = new PercentTrainSettingsPanel();
        panelSettingsHybridDistance = new DistanceSettingsPanel();
        buttonSettingsAddToBatch_Hybrid = new javax.swing.JButton();
        paneSettingsMethodsBNN = new javax.swing.JPanel();
        buttonSettingsAddToBatch_BNN = new javax.swing.JButton();
        panelBNNPercentTrain = new PercentTrainSettingsPanel();
        panelSettingsBNNinside = new gui.settingspanels.BNNSettingsPanel();
        paneSettingsMethodsBNNint = new javax.swing.JPanel();
        buttonSettingsAddToBatch_BNNint = new javax.swing.JButton();
        panelBNNintPercentTrain = new PercentTrainSettingsPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel160 = new javax.swing.JLabel();
        panelBNNintSettingsDistance = new DistanceSettingsPanel();
        jLabel139 = new javax.swing.JLabel();
        textFieldNumNetworksToTrainBNNint = new javax.swing.JTextField();
        panelBestModelCriterionBNNint = new gui.settingspanels.BestModelCriterionIntervalSettingsPanel();
        panelSettingsBNNint_center = new BNNSettingsPanel();
        panelSettingsBNNint_radius = new BNNSettingsPanel();
        jSeparator13 = new javax.swing.JSeparator();
        panelRunOutside = new javax.swing.JPanel();
        comboBoxColnamesRun = new javax.swing.JComboBox();
        jLabelTrainingInfo = new javax.swing.JLabel();
        checkBoxRunMLPnnetar = new javax.swing.JCheckBox();
        checkBoxRunARIMA = new javax.swing.JCheckBox();
        checkBoxRunMLPnnet = new javax.swing.JCheckBox();
        checkBoxRunMLPneuralnet = new javax.swing.JCheckBox();
        checkBoxRunIntervalMLPCcode = new javax.swing.JCheckBox();
        checkBoxRunIntervalMLPneuralnet = new javax.swing.JCheckBox();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        buttonTrainAndTest = new javax.swing.JButton();
        checkBoxRunKNNfnn = new javax.swing.JCheckBox();
        jLabel49 = new javax.swing.JLabel();
        labelRunFakeIntLower = new javax.swing.JLabel();
        comboBoxRunFakeIntCenter = new javax.swing.JComboBox();
        comboBoxRunFakeIntRadius = new javax.swing.JComboBox();
        labelRunFakeIntUpper = new javax.swing.JLabel();
        checkBoxRunMLPintNnetar = new javax.swing.JCheckBox();
        checkBoxRunKNNinterval = new javax.swing.JCheckBox();
        checkBoxRunKNNcustom = new javax.swing.JCheckBox();
        jLabel71 = new javax.swing.JLabel();
        textFieldRunNumForecasts = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        checkBoxRunKNNkknn = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        textFieldRunDataRangeFrom = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        textFieldRunDataRangeTo = new javax.swing.JTextField();
        labelRunFakeIntCenter = new javax.swing.JLabel();
        labelRunFakeIntRadius = new javax.swing.JLabel();
        comboBoxRunFakeIntLower = new javax.swing.JComboBox();
        comboBoxRunFakeIntUpper = new javax.swing.JComboBox();
        radioButtonRunFakeIntLowerUpper = new javax.swing.JRadioButton();
        radioButtonRunFakeIntCenterRadius = new javax.swing.JRadioButton();
        checkBoxRunMLPintNnet = new javax.swing.JCheckBox();
        buttonRunRestoreRangeAll = new javax.swing.JButton();
        checkBoxAvgSimpleCTSperM = new javax.swing.JCheckBox();
        checkBoxAvgSimpleIntTSperM = new javax.swing.JCheckBox();
        checkBoxAvgONLY = new javax.swing.JCheckBox();
        checkBoxAvgSimpleCTS = new javax.swing.JCheckBox();
        checkBoxAvgSimpleIntTS = new javax.swing.JCheckBox();
        checkBoxRunIntervalRandomWalk = new javax.swing.JCheckBox();
        jLabel133 = new javax.swing.JLabel();
        checkBoxRunVAR = new javax.swing.JCheckBox();
        checkBoxRunRBF = new javax.swing.JCheckBox();
        checkBoxRunRBFint = new javax.swing.JCheckBox();
        checkBoxRunHybrid = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        checkBoxRunVARint = new javax.swing.JCheckBox();
        checkBoxRunHolt = new javax.swing.JCheckBox();
        checkBoxRunHoltInt = new javax.swing.JCheckBox();
        checkBoxRunIntervalHolt = new javax.swing.JCheckBox();
        checkBoxRunIncludeRMSSE = new javax.swing.JCheckBox();
        textFieldRunRMSSESeasonality = new javax.swing.JTextField();
        checkBoxRunSES = new javax.swing.JCheckBox();
        checkBoxRunSESint = new javax.swing.JCheckBox();
        checkBoxRunHoltWinters = new javax.swing.JCheckBox();
        checkBoxRunHoltWintersInt = new javax.swing.JCheckBox();
        checkBoxRunRandomWalkCTS = new javax.swing.JCheckBox();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        checkBoxAvgMDeCTSperM = new javax.swing.JCheckBox();
        checkBoxAvgMDeCTS = new javax.swing.JCheckBox();
        checkBoxAvgMDeIntTSperM = new javax.swing.JCheckBox();
        checkBoxAvgMDeIntTS = new javax.swing.JCheckBox();
        jLabel45 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        checkBoxAvgTheilsuCTSperM = new javax.swing.JCheckBox();
        checkBoxAvgTheilsuCTS = new javax.swing.JCheckBox();
        checkBoxAvgTheilsuIntTSperM = new javax.swing.JCheckBox();
        checkBoxAvgTheilsuIntTS = new javax.swing.JCheckBox();
        checkBoxAvgCvgEffIntTSperM = new javax.swing.JCheckBox();
        checkBoxAvgCvgEffIntTS = new javax.swing.JCheckBox();
        checkBoxAvgCenterLogRadiusIntTSperM = new javax.swing.JCheckBox();
        checkBoxAvgCenterLogRadiusIntTS = new javax.swing.JCheckBox();
        checkBoxAvgMedianCTSperM = new javax.swing.JCheckBox();
        checkBoxAvgMedianCTS = new javax.swing.JCheckBox();
        checkBoxAvgMedianIntTSperM = new javax.swing.JCheckBox();
        checkBoxAvgMedianIntTS = new javax.swing.JCheckBox();
        checkBoxRunBNN = new javax.swing.JCheckBox();
        checkBoxRunBNNInt = new javax.swing.JCheckBox();
        checkBoxRunKNNmyown = new javax.swing.JCheckBox();
        checkBoxRunMAvg = new javax.swing.JCheckBox();
        panelErrorMeasuresAll = new javax.swing.JPanel();
        panelErrorMeasures = new javax.swing.JPanel();
        buttonRunShowHiddenErrorMeasures = new javax.swing.JButton();
        buttonHideAllErrorsExceptAvg = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        buttonRunExportErrorMeasures = new javax.swing.JButton();
        panelResidualsAll = new javax.swing.JPanel();
        panelResidualsPlot = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        textAreaResidualsBasicStats = new javax.swing.JTextArea();
        panelResidualsLeft = new javax.swing.JPanel();
        buttonExportResiduals = new javax.swing.JButton();
        panelResiduals = new javax.swing.JPanel();
        scrollPaneResiduals = new javax.swing.JScrollPane();
        panelResidualsRightButtons = new javax.swing.JPanel();
        buttonPlotResiduals = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        listPlotResidualsLegend = new javax.swing.JList();
        panelForecastValsAll = new javax.swing.JPanel();
        buttonExportForecastValues = new javax.swing.JButton();
        panelForecastVals = new javax.swing.JPanel();
        scrollPaneForecastVals = new javax.swing.JScrollPane();
        buttonForecastValsShowHidden = new javax.swing.JButton();
        buttonForecastValsHideNoForecasts = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        buttonForecastValsHideAllButAvg = new javax.swing.JButton();
        panelDiagramsNNs = new javax.swing.JPanel();
        panelDiagramsNNsInside = new javax.swing.JPanel();
        buttonExportDiagramsNN = new javax.swing.JButton();
        tabbedPaneDiagramsNNs = new javax.swing.JTabbedPane();
        panelPredictionIntervalsAll = new javax.swing.JPanel();
        buttonExportPredictionIntervals = new javax.swing.JButton();
        panelPredictionIntervals = new javax.swing.JPanel();
        scrollPanePredictionIntervals = new javax.swing.JScrollPane();
        panelAnalysisBatch = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        buttonAnalysisBatchRemoveSelectedRows = new javax.swing.JButton();
        scrollPaneAnalysisBatchInside = new javax.swing.JScrollPane();
        tableAnalysisBatch = new javax.swing.JTable();
        buttonRunAnalysisBatch = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        panelCombinationWeightsAll = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        panelCombinationWeights = new javax.swing.JPanel();
        panelModelDescriptionsAll = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        textAreaModelsInfo = new javax.swing.JTextArea();
        panelBayesianAll = new javax.swing.JPanel();
        panelBayesianSettings = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        panelBinomPropPlot = new javax.swing.JPanel();
        tabbedPaneBinomPropPlot = new javax.swing.JTabbedPane();
        panelBinomPropSettings = new BinomPropSettingsPanel();
        panelBinomPropInfo = new javax.swing.JScrollPane();
        textAreaBinomPropInfo = new javax.swing.JTextArea();
        buttonBinomPropComputePosterior = new javax.swing.JButton();
        buttonBinomPropSimulate = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        textFieldBinomPropPercProbInterval = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        buttonBinomPropPredict = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        textFieldBinomPropNumFutureObs = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        menuBarMain = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuFileLoad = new javax.swing.JMenuItem();
        menuFileExit = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();

        buttonGroup_runFakeIntCRLBUB.add(radioButtonRunFakeIntLowerUpper);
        buttonGroup_runFakeIntCRLBUB.add(radioButtonRunFakeIntCenterRadius);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelEverything.setPreferredSize(new java.awt.Dimension(1361, 687));

        panelCTS.setPreferredSize(new java.awt.Dimension(1361, 614));

        buttonPlotColname.setText("Plot selected time series");
        buttonPlotColname.setEnabled(false);
        buttonPlotColname.setEnabled(false);
        buttonPlotColname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotColnameActionPerformed(evt);
            }
        });

        buttonACF.setText("Autocorrelation Plot");
        buttonACF.setEnabled(false);
        buttonACF.setEnabled(false);
        buttonACF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonACFActionPerformed(evt);
            }
        });

        buttonPACF.setText("Partial Autocorrelation Plot");
        buttonPACF.setEnabled(false);
        buttonPACF.setEnabled(false);
        buttonPACF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPACFActionPerformed(evt);
            }
        });

        listColnames.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(listColnames);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(164, 89));

        textAreaPlotBasicStats.setEditable(false);
        textAreaPlotBasicStats.setColumns(20);
        textAreaPlotBasicStats.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        textAreaPlotBasicStats.setLineWrap(true);
        textAreaPlotBasicStats.setRows(5);
        textAreaPlotBasicStats.setFocusable(false);
        textAreaPlotBasicStats.setMaximumSize(new java.awt.Dimension(32767, 32767));
        textAreaPlotBasicStats.setOpaque(false);
        jScrollPane2.setViewportView(textAreaPlotBasicStats);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        buttonBoxplots.setText("Boxplots");
        buttonBoxplots.setEnabled(false);
        buttonBoxplots.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBoxplotsActionPerformed(evt);
            }
        });

        buttonHistograms.setText("Histograms");
        buttonHistograms.setEnabled(false);
        buttonHistograms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonHistogramsActionPerformed(evt);
            }
        });

        buttonNormProbPlot.setText("Normal probability plot");
        buttonNormProbPlot.setEnabled(false);
        buttonNormProbPlot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNormProbPlotActionPerformed(evt);
            }
        });

        buttonExportAnalysisPlotsCTS.setText("Export currently shown plot");
        buttonExportAnalysisPlotsCTS.setEnabled(false);
        buttonExportAnalysisPlotsCTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExportAnalysisPlotsCTSActionPerformed(evt);
            }
        });

        jLabel22.setText("(Broken - only exports the last tab.)");

        buttonExportAnalysisText.setText("Save the contents of the box below:");
        buttonExportAnalysisText.setEnabled(false);
        buttonExportAnalysisText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExportAnalysisTextActionPerformed(evt);
            }
        });

        buttonBasicStats.setText("Basic statistics");
        buttonBasicStats.setEnabled(false);
        buttonBasicStats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBasicStatsActionPerformed(evt);
            }
        });

        buttonCTSclearSelection.setText("(Clear selection)");
        buttonCTSclearSelection.setEnabled(false);
        buttonCTSclearSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCTSclearSelectionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCTSLayout = new javax.swing.GroupLayout(panelCTS);
        panelCTS.setLayout(panelCTSLayout);
        panelCTSLayout.setHorizontalGroup(
            panelCTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCTSLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCTSLayout.createSequentialGroup()
                        .addGroup(panelCTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonExportAnalysisText))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelCTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCTSLayout.createSequentialGroup()
                                .addComponent(buttonPlotColname)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonCTSclearSelection))
                            .addComponent(buttonHistograms)
                            .addComponent(buttonBoxplots)
                            .addComponent(buttonNormProbPlot)
                            .addGroup(panelCTSLayout.createSequentialGroup()
                                .addComponent(buttonACF)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonPACF))
                            .addComponent(buttonBasicStats))
                        .addGap(0, 10, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabbedPaneAnalysisPlotsCTS)
                    .addGroup(panelCTSLayout.createSequentialGroup()
                        .addComponent(buttonExportAnalysisPlotsCTS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)
                        .addGap(0, 429, Short.MAX_VALUE)))
                .addGap(16, 16, 16))
        );
        panelCTSLayout.setVerticalGroup(
            panelCTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCTSLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(panelCTSLayout.createSequentialGroup()
                        .addGroup(panelCTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCTSLayout.createSequentialGroup()
                                .addGroup(panelCTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(buttonPlotColname)
                                    .addComponent(buttonCTSclearSelection))
                                .addGap(18, 18, 18)
                                .addGroup(panelCTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(buttonACF)
                                    .addComponent(buttonPACF))
                                .addGap(18, 18, 18)
                                .addComponent(buttonBoxplots)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonHistograms)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonNormProbPlot)
                                .addGap(18, 18, 18)
                                .addComponent(buttonBasicStats))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addComponent(buttonExportAnalysisText)
                        .addGap(9, 9, 9)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE))
                    .addGroup(panelCTSLayout.createSequentialGroup()
                        .addGroup(panelCTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonExportAnalysisPlotsCTS)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tabbedPaneAnalysisPlotsCTS)))
                .addContainerGap())
        );

        panelEverything.addTab("CTS", panelCTS);

        buttonLogTransformSeries.setText("Log-transform selected");
        buttonLogTransformSeries.setEnabled(false);
        buttonLogTransformSeries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLogTransformSeriesActionPerformed(evt);
            }
        });

        buttonDiffSeries.setText("Difference selected");
        buttonDiffSeries.setEnabled(false);
        buttonDiffSeries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDiffSeriesActionPerformed(evt);
            }
        });

        listColnamesTransform.setModel(new DefaultListModel());
        jScrollPane7.setViewportView(listColnamesTransform);

        buttonRemoveTrend.setText("Remove trend");
        buttonRemoveTrend.setEnabled(false);
        buttonRemoveTrend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveTrendActionPerformed(evt);
            }
        });

        buttonAggregateToITS.setText("Aggregate to ITS");
        buttonAggregateToITS.setEnabled(false);
        buttonAggregateToITS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAggregateToITSActionPerformed(evt);
            }
        });

        jLabel24.setText("every");

        textFieldAggregateToITSevery.setText("24");

        jLabel25.setText("observations");

        buttonConvertITSLBUBCR1.setText("Convert LB/UB <-> C/R");
        buttonConvertITSLBUBCR1.setEnabled(false);
        buttonConvertITSLBUBCR1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonConvertITSLBUBCRActionPerformed(evt);
            }
        });

        buttonNormalize.setText("Normalize");
        buttonNormalize.setEnabled(false);
        buttonNormalize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNormalizeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTransformLayout = new javax.swing.GroupLayout(panelTransform);
        panelTransform.setLayout(panelTransformLayout);
        panelTransformLayout.setHorizontalGroup(
            panelTransformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTransformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonLogTransformSeries)
                    .addComponent(buttonDiffSeries)
                    .addComponent(buttonRemoveTrend)
                    .addGroup(panelTransformLayout.createSequentialGroup()
                        .addComponent(buttonAggregateToITS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldAggregateToITSevery, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25))
                    .addComponent(buttonNormalize))
                .addContainerGap(875, Short.MAX_VALUE))
        );
        panelTransformLayout.setVerticalGroup(
            panelTransformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTransformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTransformLayout.createSequentialGroup()
                        .addComponent(buttonDiffSeries)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonLogTransformSeries, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(buttonNormalize)
                        .addGap(18, 18, 18)
                        .addComponent(buttonRemoveTrend)
                        .addGap(18, 18, 18)
                        .addGroup(panelTransformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonAggregateToITS)
                            .addComponent(jLabel24)
                            .addComponent(textFieldAggregateToITSevery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25)))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(390, Short.MAX_VALUE))
        );

        panelEverything.addTab("CTS transformation", panelTransform);

        panelIntTS.setName(""); // NOI18N

        buttonPlotAddITS.setText("Specify new ITS");
        buttonPlotAddITS.setEnabled(false);
        buttonPlotAddITS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotAddITSActionPerformed(evt);
            }
        });

        buttonPlotRemoveITS.setText("Remove selected ITS");
        buttonPlotRemoveITS.setEnabled(false);
        buttonPlotRemoveITS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotRemoveITSActionPerformed(evt);
            }
        });

        buttonPlotSelectedITS.setText("Plot all selected ITS");
        buttonPlotSelectedITS.setEnabled(false);
        buttonPlotSelectedITS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotSelectedITSActionPerformed(evt);
            }
        });

        jScrollPane4.setPreferredSize(new java.awt.Dimension(247, 130));

        listPlotITSspecs.setModel(new DefaultListModel());
        jScrollPane4.setViewportView(listPlotITSspecs);

        buttonPlotAllITSScatterplot.setText("Scatterplots all in one");
        buttonPlotAllITSScatterplot.setEnabled(false);
        buttonPlotAllITSScatterplot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotAllITSScatterplotActionPerformed(evt);
            }
        });

        buttonPlotAllITSScatterplotMatrix.setText("Scatterplot matrix");
        buttonPlotAllITSScatterplotMatrix.setEnabled(false);
        buttonPlotAllITSScatterplotMatrix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotAllITSScatterplotMatrixActionPerformed(evt);
            }
        });

        jLabel18.setText("Note: for now ignores ITS specified as [LB, UB]");

        jSeparator14.setOrientation(javax.swing.SwingConstants.VERTICAL);

        buttonConvertITSLBUBCR.setText("Convert LB/UB <-> C/R");
        buttonConvertITSLBUBCR.setEnabled(false);
        buttonConvertITSLBUBCR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonConvertITSLBUBCRActionPerformed(evt);
            }
        });

        buttonITSclearSelection.setText("(Clear selection)");
        buttonITSclearSelection.setEnabled(false);
        buttonITSclearSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonITSclearSelectionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelIntTSLayout = new javax.swing.GroupLayout(panelIntTS);
        panelIntTS.setLayout(panelIntTSLayout);
        panelIntTSLayout.setHorizontalGroup(
            panelIntTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelIntTSLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelIntTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelIntTSLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelIntTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonPlotSelectedITS)
                            .addComponent(buttonPlotRemoveITS)
                            .addComponent(buttonPlotAddITS)
                            .addComponent(buttonITSclearSelection)))
                    .addComponent(buttonPlotAllITSScatterplot)
                    .addGroup(panelIntTSLayout.createSequentialGroup()
                        .addComponent(buttonPlotAllITSScatterplotMatrix)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonConvertITSLBUBCR)
                .addContainerGap(705, Short.MAX_VALUE))
        );
        panelIntTSLayout.setVerticalGroup(
            panelIntTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelIntTSLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelIntTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator14)
                    .addGroup(panelIntTSLayout.createSequentialGroup()
                        .addGroup(panelIntTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonConvertITSLBUBCR)
                            .addGroup(panelIntTSLayout.createSequentialGroup()
                                .addGroup(panelIntTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelIntTSLayout.createSequentialGroup()
                                        .addComponent(buttonPlotAddITS)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonPlotRemoveITS)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonPlotSelectedITS)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonITSclearSelection))
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonPlotAllITSScatterplot)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelIntTSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(buttonPlotAllITSScatterplotMatrix)
                                    .addComponent(jLabel18))))
                        .addGap(0, 327, Short.MAX_VALUE)))
                .addContainerGap())
        );

        panelEverything.addTab("ITS", panelIntTS);
        panelIntTS.getAccessibleContext().setAccessibleName("");

        buttonPlotExportPlot.setText("Save currently shown plot");
        buttonPlotExportPlot.setEnabled(false);
        buttonPlotExportPlot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotExportPlotActionPerformed(evt);
            }
        });

        gdBufferedPanelPlot = new JGDBufferedPanel(panelPlot.getWidth(), panelPlot.getHeight());
        panelPlot.add(gdBufferedPanelPlot, BorderLayout.CENTER);
        panelPlot.setLayout(new java.awt.BorderLayout());

        jLabel89.setText("Zoom CTS axis x: from");

        jLabel127.setText("Zoom CTS axis y: from");

        textFieldPlotRangeCTSXfrom.setText("0");
        textFieldPlotRangeCTSXfrom.setEnabled(false);

        textFieldPlotRangeCTSYfrom.setText("0");
        textFieldPlotRangeCTSYfrom.setEnabled(false);

        textFieldPlotRangeCTSXto.setText("0");
        textFieldPlotRangeCTSXto.setEnabled(false);

        textFieldPlotRangeCTSYto.setText("0");
        textFieldPlotRangeCTSYto.setEnabled(false);

        jLabel126.setText("to");

        jLabel128.setText("to");

        buttonPlotRestoreCTSRangeX.setText("<-restore all");
        buttonPlotRestoreCTSRangeX.setEnabled(false);
        buttonPlotRestoreCTSRangeX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotRestoreCTSRangeXActionPerformed(evt);
            }
        });

        buttonPlotRestoreCTSRangeY.setText("<-restore all");
        buttonPlotRestoreCTSRangeY.setEnabled(false);
        buttonPlotRestoreCTSRangeY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotRestoreCTSRangeYActionPerformed(evt);
            }
        });

        buttonPlotZoomCTS.setText("Zoom CTS");
        buttonPlotZoomCTS.setEnabled(false);
        buttonPlotZoomCTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotZoomCTSActionPerformed(evt);
            }
        });

        jLabel129.setText("Zoom ITS axis x: from");

        jLabel130.setText("Zoom ITS axis y: from");

        textFieldPlotRangeIntTSXfrom.setText("0");
        textFieldPlotRangeIntTSXfrom.setEnabled(false);

        textFieldPlotRangeIntTSYfrom.setText("0");
        textFieldPlotRangeIntTSYfrom.setEnabled(false);

        jLabel131.setText("to");

        jLabel132.setText("to");

        textFieldPlotRangeIntTSXto.setText("0");
        textFieldPlotRangeIntTSXto.setEnabled(false);

        textFieldPlotRangeIntTSYto.setText("0");
        textFieldPlotRangeIntTSYto.setEnabled(false);

        buttonPlotRestoreIntTSRangeX.setText("<-restore all");
        buttonPlotRestoreIntTSRangeX.setEnabled(false);
        buttonPlotRestoreIntTSRangeX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotRestoreIntTSRangeXActionPerformed(evt);
            }
        });

        buttonPlotRestoreIntTSRangeY.setText("<-restore all");
        buttonPlotRestoreIntTSRangeY.setEnabled(false);
        buttonPlotRestoreIntTSRangeY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotRestoreIntTSRangeYActionPerformed(evt);
            }
        });

        buttonPlotZoomIntTS.setText("Zoom ITS");
        buttonPlotZoomIntTS.setEnabled(false);
        buttonPlotZoomIntTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotZoomIntTSActionPerformed(evt);
            }
        });

        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);

        scrollPaneListPlotLegend.setPreferredSize(new java.awt.Dimension(300, 130));

        listPlotLegend.setModel(new DefaultListModel());
        listPlotLegend.setCellRenderer(new gui.renderers.PlotLegendSimpleListCellRenderer());
        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                //nerob inak vobec nic
                if (listPlotLegend.getSelectedIndex() == -1) {
                    listPlotLegend.setSelectedIndex(listPlotLegend.locationToIndex(e.getPoint()));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (((DefaultListModel)listPlotLegend.getModel())
                    .getElementAt(listPlotLegend.getSelectedIndex()) instanceof PlotLegendTurnOFFableListElement) {
                    ((PlotLegendTurnOFFableListElement)((DefaultListModel)listPlotLegend.getModel())
                        .getElementAt(listPlotLegend.getSelectedIndex())).dispatchEvent(e);
                } else {
                    if (((DefaultListModel)listPlotLegend.getModel())
                        .getElementAt(listPlotLegend.getSelectedIndex()) instanceof PlotLegendSimpleListElement) {
                        ((PlotLegendSimpleListElement)((DefaultListModel)listPlotLegend.getModel())
                            .getElementAt(listPlotLegend.getSelectedIndex())).dispatchEvent(e);
                    }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
            @Override
            public void mouseClicked(MouseEvent e) { }
        };
        listPlotLegend.addMouseListener(mouseListener);
        scrollPaneListPlotLegend.setViewportView(listPlotLegend);

        buttonLegendSelectAll.setText("Select all");
        buttonLegendSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLegendSelectAllActionPerformed(evt);
            }
        });

        buttonLegendSelectNone.setText("Unselect all");
        buttonLegendSelectNone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLegendSelectNoneActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPlotImageLayout = new javax.swing.GroupLayout(panelPlotImage);
        panelPlotImage.setLayout(panelPlotImageLayout);
        panelPlotImageLayout.setHorizontalGroup(
            panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlotImageLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPlotImageLayout.createSequentialGroup()
                        .addComponent(buttonPlotExportPlot)
                        .addGap(18, 18, 18)
                        .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel127)
                            .addComponent(jLabel89))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textFieldPlotRangeCTSXfrom)
                            .addComponent(textFieldPlotRangeCTSYfrom, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel126)
                            .addComponent(jLabel128))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textFieldPlotRangeCTSYto)
                            .addComponent(textFieldPlotRangeCTSXto, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonPlotRestoreCTSRangeX)
                            .addComponent(buttonPlotRestoreCTSRangeY))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonPlotZoomCTS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel130)
                            .addComponent(jLabel129))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textFieldPlotRangeIntTSXfrom)
                            .addComponent(textFieldPlotRangeIntTSYfrom, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel131)
                            .addComponent(jLabel132))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textFieldPlotRangeIntTSYto)
                            .addComponent(textFieldPlotRangeIntTSXto, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonPlotRestoreIntTSRangeX)
                            .addComponent(buttonPlotRestoreIntTSRangeY))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonPlotZoomIntTS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonLegendSelectNone, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlotImageLayout.createSequentialGroup()
                                .addComponent(scrollPaneListPlotLegend, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonLegendSelectAll)
                                .addGap(12, 12, 12))))
                    .addComponent(panelPlot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelPlotImageLayout.setVerticalGroup(
            panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlotImageLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelPlotImageLayout.createSequentialGroup()
                        .addComponent(buttonLegendSelectAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonLegendSelectNone))
                    .addComponent(buttonPlotExportPlot)
                    .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelPlotImageLayout.createSequentialGroup()
                            .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonPlotRestoreCTSRangeX)
                                .addComponent(textFieldPlotRangeCTSXto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonPlotRestoreCTSRangeY)
                                .addComponent(jLabel127)
                                .addComponent(textFieldPlotRangeCTSYfrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel128)
                                .addComponent(textFieldPlotRangeCTSYto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlotImageLayout.createSequentialGroup()
                            .addGap(11, 11, 11)
                            .addComponent(buttonPlotZoomCTS)
                            .addGap(15, 15, 15)))
                    .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldPlotRangeCTSXfrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel126))
                    .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelPlotImageLayout.createSequentialGroup()
                        .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonPlotRestoreIntTSRangeX)
                            .addComponent(textFieldPlotRangeIntTSXto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldPlotRangeIntTSXfrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel131)
                            .addComponent(jLabel129, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPlotImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonPlotRestoreIntTSRangeY)
                            .addComponent(textFieldPlotRangeIntTSYto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldPlotRangeIntTSYfrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel132)
                            .addComponent(jLabel130)))
                    .addGroup(panelPlotImageLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(buttonPlotZoomIntTS))
                    .addComponent(scrollPaneListPlotLegend, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelPlot, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelEverything.addTab("Plot", panelPlotImage);

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

        javax.swing.GroupLayout panelTestsOutsideLayout = new javax.swing.GroupLayout(panelTestsOutside);
        panelTestsOutside.setLayout(panelTestsOutsideLayout);
        panelTestsOutsideLayout.setHorizontalGroup(
            panelTestsOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTestsOutsideLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTestsOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTestsOutsideLayout.createSequentialGroup()
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelTestsOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonNormalityTests)
                            .addComponent(buttonStationarityTest)
                            .addGroup(panelTestsOutsideLayout.createSequentialGroup()
                                .addComponent(buttonStructBreaks)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textFieldMaxStructBreaks, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelTestsOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel20)))))
                    .addComponent(buttonExportTextAreaTests)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 532, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTestsOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabbedPaneAnalysisPlotsTests)
                    .addGroup(panelTestsOutsideLayout.createSequentialGroup()
                        .addComponent(buttonExportTestsPlots)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel34)
                        .addGap(0, 470, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelTestsOutsideLayout.setVerticalGroup(
            panelTestsOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTestsOutsideLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTestsOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTestsOutsideLayout.createSequentialGroup()
                        .addGroup(panelTestsOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelTestsOutsideLayout.createSequentialGroup()
                                .addComponent(buttonNormalityTests)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonStationarityTest)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel21)
                                .addGap(7, 7, 7)
                                .addGroup(panelTestsOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(buttonStructBreaks)
                                    .addComponent(jLabel19)
                                    .addComponent(textFieldMaxStructBreaks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonExportTextAreaTests)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE))
                    .addComponent(jSeparator15, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTestsOutsideLayout.createSequentialGroup()
                        .addGroup(panelTestsOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonExportTestsPlots)
                            .addComponent(jLabel34))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tabbedPaneAnalysisPlotsTests)))
                .addContainerGap())
        );

        panelEverything.addTab("Tests", panelTestsOutside);

        jTableData.setModel(DataTableModel.getInstance());
        scrollPaneData.setViewportView(jTableData);

        javax.swing.GroupLayout panelDataLayout = new javax.swing.GroupLayout(panelData);
        panelData.setLayout(panelDataLayout);
        panelDataLayout.setHorizontalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneData, javax.swing.GroupLayout.DEFAULT_SIZE, 1404, Short.MAX_VALUE)
        );
        panelDataLayout.setVerticalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneData, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
        );

        panelEverything.addTab("Data", panelData);

        panelAnalysisSettings.setPreferredSize(new java.awt.Dimension(1361, 615));

        paneSettingsMethods.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        paneSettingsMethods.setPreferredSize(new java.awt.Dimension(1361, 615));

        jLabelRPkg.setText("R package:");

        comboBoxRPackage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { Const.NNET, Const.NNETAR, Const.NEURALNET }));
        comboBoxRPackage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxRPackageActionPerformed(evt);
            }
        });

        panelSettingsMLPPackage.setLayout(new java.awt.CardLayout());

        scrollPane_panelSettingsMLPPackage_nnet.setViewportView(panelSettingsMLPPackage_nnet);

        panelSettingsMLPPackage.add(scrollPane_panelSettingsMLPPackage_nnet, "panelSettingsMLPPackage_nnet");
        panelSettingsMLPPackage.add(panelSettingsMLPPackage_nnetar, "panelSettingsMLPPackage_nnetar");

        jLabel6.setText("(TODO)");

        javax.swing.GroupLayout panelSettingsMLPPackage_neuralnetLayout = new javax.swing.GroupLayout(panelSettingsMLPPackage_neuralnet);
        panelSettingsMLPPackage_neuralnet.setLayout(panelSettingsMLPPackage_neuralnetLayout);
        panelSettingsMLPPackage_neuralnetLayout.setHorizontalGroup(
            panelSettingsMLPPackage_neuralnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6)
        );
        panelSettingsMLPPackage_neuralnetLayout.setVerticalGroup(
            panelSettingsMLPPackage_neuralnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6)
        );

        panelSettingsMLPPackage.add(panelSettingsMLPPackage_neuralnet, "panelSettingsMLPPackage_neuralnet");

        buttonSettingsAddToBatch_MLP.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_MLP.setEnabled(false);
        buttonSettingsAddToBatch_MLP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_MLPActionPerformed(evt);
            }
        });

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
                            .addComponent(comboBoxRPackage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelRPkg))
                        .addGap(18, 18, 18)
                        .addComponent(panelMLPPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(paneSettingsMethodsMLPLayout.createSequentialGroup()
                        .addComponent(buttonSettingsAddToBatch_MLP)
                        .addGap(0, 1156, Short.MAX_VALUE)))
                .addContainerGap())
        );
        paneSettingsMethodsMLPLayout.setVerticalGroup(
            paneSettingsMethodsMLPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsMLPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonSettingsAddToBatch_MLP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneSettingsMethodsMLPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(paneSettingsMethodsMLPLayout.createSequentialGroup()
                        .addComponent(jLabelRPkg)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxRPackage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelMLPPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSettingsMLPPackage, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
                .addContainerGap())
        );

        paneSettingsMethods.addTab("MLP", paneSettingsMethodsMLP);

        jLabel47.setText("Imitates iMLP by constructing an interval out of two separate forecasts for Center and Radius.");

        jLabel48.setText("Distance to use for computing the error measures:");

        panelSettingsMLPintPackage.setLayout(new java.awt.CardLayout());

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel100.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel100.setText("Center:");

        jLabel101.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel101.setText("Radius:");

        scrollPane_panelSettingsMLPintPackage_nnet_center.setViewportView(panelSettingsMLPintPackage_nnet_center);

        scrollPane_panelSettingsMLPintPackage_nnet_radius.setViewportView(panelSettingsMLPintPackage_nnet_radius);

        javax.swing.GroupLayout panelSettingsMLPintPackage_nnetLayout = new javax.swing.GroupLayout(panelSettingsMLPintPackage_nnet);
        panelSettingsMLPintPackage_nnet.setLayout(panelSettingsMLPintPackage_nnetLayout);
        panelSettingsMLPintPackage_nnetLayout.setHorizontalGroup(
            panelSettingsMLPintPackage_nnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMLPintPackage_nnetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMLPintPackage_nnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel100)
                    .addComponent(scrollPane_panelSettingsMLPintPackage_nnet_center, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSettingsMLPintPackage_nnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSettingsMLPintPackage_nnetLayout.createSequentialGroup()
                        .addComponent(jLabel101)
                        .addGap(547, 650, Short.MAX_VALUE))
                    .addGroup(panelSettingsMLPintPackage_nnetLayout.createSequentialGroup()
                        .addComponent(scrollPane_panelSettingsMLPintPackage_nnet_radius)
                        .addContainerGap())))
        );
        panelSettingsMLPintPackage_nnetLayout.setVerticalGroup(
            panelSettingsMLPintPackage_nnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMLPintPackage_nnetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMLPintPackage_nnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4)
                    .addGroup(panelSettingsMLPintPackage_nnetLayout.createSequentialGroup()
                        .addGroup(panelSettingsMLPintPackage_nnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel101)
                            .addComponent(jLabel100))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelSettingsMLPintPackage_nnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrollPane_panelSettingsMLPintPackage_nnet_center, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                            .addComponent(scrollPane_panelSettingsMLPintPackage_nnet_radius))))
                .addContainerGap())
        );

        panelSettingsMLPintPackage.add(panelSettingsMLPintPackage_nnet, "panelSettingsMLPintPackage_nnet");

        jLabel87.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel87.setText("Center:");

        jLabel88.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel88.setText("Radius:");

        javax.swing.GroupLayout panelSettingsMLPintPackage_nnetarLayout = new javax.swing.GroupLayout(panelSettingsMLPintPackage_nnetar);
        panelSettingsMLPintPackage_nnetar.setLayout(panelSettingsMLPintPackage_nnetarLayout);
        panelSettingsMLPintPackage_nnetarLayout.setHorizontalGroup(
            panelSettingsMLPintPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMLPintPackage_nnetarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMLPintPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel87)
                    .addComponent(panelSettingsMLPintPackage_nnetar_center, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSettingsMLPintPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSettingsMLPintPackage_nnetarLayout.createSequentialGroup()
                        .addComponent(jLabel88)
                        .addGap(0, 665, Short.MAX_VALUE))
                    .addGroup(panelSettingsMLPintPackage_nnetarLayout.createSequentialGroup()
                        .addComponent(panelSettingsMLPintPackage_nnetar_radius, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        panelSettingsMLPintPackage_nnetarLayout.setVerticalGroup(
            panelSettingsMLPintPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMLPintPackage_nnetarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMLPintPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87)
                    .addComponent(jLabel88))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSettingsMLPintPackage_nnetarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSettingsMLPintPackage_nnetar_center, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                    .addComponent(panelSettingsMLPintPackage_nnetar_radius, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        panelSettingsMLPintPackage.add(panelSettingsMLPintPackage_nnetar, "panelSettingsMLPintPackage_nnetar");

        jLabelRPkg1.setText("R package:");

        comboBoxRPackageMLPint.setModel(new javax.swing.DefaultComboBoxModel(new String[] { Const.NNET, Const.NNETAR }));
        comboBoxRPackageMLPint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxRPackageMLPintActionPerformed(evt);
            }
        });

        buttonSettingsAddToBatch_MLPint.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_MLPint.setEnabled(false);
        buttonSettingsAddToBatch_MLPint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_MLPintActionPerformed(evt);
            }
        });

        jLabel12.setText("Number of networks to train:");

        textFieldNumNetsToTrainMLPint.setText("1");

        javax.swing.GroupLayout paneSettingsMethodsMLPintLayout = new javax.swing.GroupLayout(paneSettingsMethodsMLPint);
        paneSettingsMethodsMLPint.setLayout(paneSettingsMethodsMLPintLayout);
        paneSettingsMethodsMLPintLayout.setHorizontalGroup(
            paneSettingsMethodsMLPintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsMLPintLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsMLPintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneSettingsMethodsMLPintLayout.createSequentialGroup()
                        .addGroup(paneSettingsMethodsMLPintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonSettingsAddToBatch_MLPint)
                            .addComponent(jLabel47))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(paneSettingsMethodsMLPintLayout.createSequentialGroup()
                        .addGroup(paneSettingsMethodsMLPintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelSettingsMLPintPackage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneSettingsMethodsMLPintLayout.createSequentialGroup()
                                .addComponent(panelMLPintSettingsDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textFieldNumNetsToTrainMLPint, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelBestModelCriterionMLPint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(paneSettingsMethodsMLPintLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(comboBoxRPackageMLPint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(panelMLPintPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(paneSettingsMethodsMLPintLayout.createSequentialGroup()
                                .addGroup(paneSettingsMethodsMLPintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel48)
                                    .addComponent(jLabelRPkg1))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
            .addGroup(paneSettingsMethodsMLPintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 1313, Short.MAX_VALUE))
        );
        paneSettingsMethodsMLPintLayout.setVerticalGroup(
            paneSettingsMethodsMLPintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsMLPintLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonSettingsAddToBatch_MLPint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel47)
                .addGap(18, 18, 18)
                .addGroup(paneSettingsMethodsMLPintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(paneSettingsMethodsMLPintLayout.createSequentialGroup()
                        .addComponent(jLabelRPkg1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxRPackageMLPint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelMLPintPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSettingsMLPintPackage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel48)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneSettingsMethodsMLPintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneSettingsMethodsMLPintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(panelBestModelCriterionMLPint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelMLPintSettingsDistance, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                    .addGroup(paneSettingsMethodsMLPintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(textFieldNumNetsToTrainMLPint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(paneSettingsMethodsMLPintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 654, Short.MAX_VALUE))
        );

        paneSettingsMethods.addTab("MLP(i)", paneSettingsMethodsMLPint);

        panelSettingsIntervalMLPMode.setLayout(new java.awt.CardLayout());
        panelSettingsIntervalMLPMode.add(panelSettingsIntervalMLPModeCcode, "panelSettingsIntervalMLPModeCcode");

        jLabel32.setText("neuralnet - to be finished");

        javax.swing.GroupLayout panelSettingsIntervalMLPModeNeuralnetLayout = new javax.swing.GroupLayout(panelSettingsIntervalMLPModeNeuralnet);
        panelSettingsIntervalMLPModeNeuralnet.setLayout(panelSettingsIntervalMLPModeNeuralnetLayout);
        panelSettingsIntervalMLPModeNeuralnetLayout.setHorizontalGroup(
            panelSettingsIntervalMLPModeNeuralnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel32)
        );
        panelSettingsIntervalMLPModeNeuralnetLayout.setVerticalGroup(
            panelSettingsIntervalMLPModeNeuralnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel32)
        );

        panelSettingsIntervalMLPMode.add(panelSettingsIntervalMLPModeNeuralnet, "panelSettingsIntervalMLPModeNeuralnet");

        jLabel31.setText("(Mode)");

        comboBoxIntervalMLPMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { Const.INTERVAL_MLP_C_CODE, Const.NEURALNET }));
        comboBoxIntervalMLPMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxIntervalMLPModeActionPerformed(evt);
            }
        });

        buttonSettingsAddToBatch_intMLP.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_intMLP.setEnabled(false);
        buttonSettingsAddToBatch_intMLP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_intMLPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneSettingsMethodsIntervalMLPLayout = new javax.swing.GroupLayout(paneSettingsMethodsIntervalMLP);
        paneSettingsMethodsIntervalMLP.setLayout(paneSettingsMethodsIntervalMLPLayout);
        paneSettingsMethodsIntervalMLPLayout.setHorizontalGroup(
            paneSettingsMethodsIntervalMLPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsIntervalMLPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsIntervalMLPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneSettingsMethodsIntervalMLPLayout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(18, 18, 18)
                        .addComponent(comboBoxIntervalMLPMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(panelIntMLPPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(panelSettingsIntervalMLPMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(paneSettingsMethodsIntervalMLPLayout.createSequentialGroup()
                        .addComponent(buttonSettingsAddToBatch_intMLP)
                        .addGap(0, 1156, Short.MAX_VALUE)))
                .addContainerGap())
        );
        paneSettingsMethodsIntervalMLPLayout.setVerticalGroup(
            paneSettingsMethodsIntervalMLPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsIntervalMLPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonSettingsAddToBatch_intMLP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneSettingsMethodsIntervalMLPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelIntMLPPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(paneSettingsMethodsIntervalMLPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel31)
                        .addComponent(comboBoxIntervalMLPMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelSettingsIntervalMLPMode, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        paneSettingsMethods.addTab("iMLP", paneSettingsMethodsIntervalMLP);

        buttonSettingsAddToBatch_RBF.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_RBF.setEnabled(false);
        buttonSettingsAddToBatch_RBF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_RBFActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneSettingsMethodsRBFLayout = new javax.swing.GroupLayout(paneSettingsMethodsRBF);
        paneSettingsMethodsRBF.setLayout(paneSettingsMethodsRBFLayout);
        paneSettingsMethodsRBFLayout.setHorizontalGroup(
            paneSettingsMethodsRBFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsRBFLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsRBFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelRBFPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(paneSettingsMethodsRBFLayout.createSequentialGroup()
                        .addComponent(buttonSettingsAddToBatch_RBF)
                        .addGap(0, 1156, Short.MAX_VALUE))
                    .addComponent(panelSettingsRBFMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        paneSettingsMethodsRBFLayout.setVerticalGroup(
            paneSettingsMethodsRBFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsRBFLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonSettingsAddToBatch_RBF)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelRBFPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelSettingsRBFMain, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
                .addContainerGap())
        );

        paneSettingsMethods.addTab("RBF", paneSettingsMethodsRBF);

        jLabel143.setText("Imitates iRBF by constructing an interval out of two separate forecasts for Center and Radius.");

        jLabel150.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel150.setText("Center:");

        jLabel151.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel151.setText("Radius:");

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel156.setForeground(new java.awt.Color(255, 0, 0));
        jLabel156.setText("(Only center and radius.)");

        jLabel157.setText("Distance to use for computing the error measures:");

        jLabel138.setText("Num of networks to train:");

        textFieldNumNetworksToTrainRBFint.setText("1");

        buttonSettingsAddToBatch_RBFint.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_RBFint.setEnabled(false);
        buttonSettingsAddToBatch_RBFint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_RBFintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneSettingsMethodsRBFintLayout = new javax.swing.GroupLayout(paneSettingsMethodsRBFint);
        paneSettingsMethodsRBFint.setLayout(paneSettingsMethodsRBFintLayout);
        paneSettingsMethodsRBFintLayout.setHorizontalGroup(
            paneSettingsMethodsRBFintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsRBFintLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsRBFintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneSettingsMethodsRBFintLayout.createSequentialGroup()
                        .addGroup(paneSettingsMethodsRBFintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel157)
                            .addComponent(panelRBFintSettingsDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel150)
                            .addGroup(paneSettingsMethodsRBFintLayout.createSequentialGroup()
                                .addGroup(paneSettingsMethodsRBFintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, paneSettingsMethodsRBFintLayout.createSequentialGroup()
                                        .addComponent(jLabel138)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(textFieldNumNetworksToTrainRBFint, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(panelBestModelCriterionRBFint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(panelSettingsRBFint_center, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(paneSettingsMethodsRBFintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneSettingsMethodsRBFintLayout.createSequentialGroup()
                                .addComponent(jLabel151)
                                .addGap(0, 723, Short.MAX_VALUE))
                            .addGroup(paneSettingsMethodsRBFintLayout.createSequentialGroup()
                                .addComponent(panelSettingsRBFint_radius, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())))
                    .addGroup(paneSettingsMethodsRBFintLayout.createSequentialGroup()
                        .addGroup(paneSettingsMethodsRBFintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneSettingsMethodsRBFintLayout.createSequentialGroup()
                                .addComponent(jLabel143)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel156)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(paneSettingsMethodsRBFintLayout.createSequentialGroup()
                                .addComponent(buttonSettingsAddToBatch_RBFint)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(panelRBFintPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        paneSettingsMethodsRBFintLayout.setVerticalGroup(
            paneSettingsMethodsRBFintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsRBFintLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsRBFintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel143)
                    .addComponent(jLabel156))
                .addGroup(paneSettingsMethodsRBFintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneSettingsMethodsRBFintLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(panelRBFintPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paneSettingsMethodsRBFintLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonSettingsAddToBatch_RBFint)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(paneSettingsMethodsRBFintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneSettingsMethodsRBFintLayout.createSequentialGroup()
                        .addComponent(jLabel151)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelSettingsRBFint_radius, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(paneSettingsMethodsRBFintLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel150)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paneSettingsMethodsRBFintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator5)
                            .addComponent(panelSettingsRBFint_center, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel157)
                .addGap(4, 4, 4)
                .addComponent(panelRBFintSettingsDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneSettingsMethodsRBFintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(paneSettingsMethodsRBFintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel138)
                        .addComponent(textFieldNumNetworksToTrainRBFint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelBestModelCriterionRBFint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(82, 82, 82))
        );

        paneSettingsMethods.addTab("RBF(i)", paneSettingsMethodsRBFint);

        buttonSettingsAddToBatch_ARIMA.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_ARIMA.setEnabled(false);
        buttonSettingsAddToBatch_ARIMA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_ARIMAActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneSettingsMethodsARIMALayout = new javax.swing.GroupLayout(paneSettingsMethodsARIMA);
        paneSettingsMethodsARIMA.setLayout(paneSettingsMethodsARIMALayout);
        paneSettingsMethodsARIMALayout.setHorizontalGroup(
            paneSettingsMethodsARIMALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsARIMALayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsARIMALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSettingsARIMAMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(paneSettingsMethodsARIMALayout.createSequentialGroup()
                        .addComponent(buttonSettingsAddToBatch_ARIMA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelARIMAPercTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 1146, Short.MAX_VALUE)))
                .addContainerGap())
        );
        paneSettingsMethodsARIMALayout.setVerticalGroup(
            paneSettingsMethodsARIMALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsARIMALayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsARIMALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelARIMAPercTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSettingsAddToBatch_ARIMA))
                .addGap(18, 18, 18)
                .addComponent(panelSettingsARIMAMain, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                .addContainerGap())
        );

        paneSettingsMethods.addTab("ARIMA", paneSettingsMethodsARIMA);

        jLabel64.setText("Select method:");

        panelSettingsKNNoptions.setLayout(new java.awt.CardLayout());
        panelSettingsKNNoptions.add(panelSettingsKNNoptions_FNN, "panelSettingsKNNoptions_FNN");
        panelSettingsKNNoptions.add(panelSettingsKNNoptions_custom, "panelSettingsKNNoptions_custom");
        panelSettingsKNNoptions.add(panelSettingsKNNoptions_kknn, "panelSettingsKNNoptions_kknn");
        panelSettingsKNNoptions.add(panelSettingsKNNoptions_myown, "card5");

        comboBoxKNNoptions.setModel(new javax.swing.DefaultComboBoxModel(new String[] { Const.KNN_FNN, Const.KNN_KKNN/*, Const.KNN_CUSTOM*/, Const.KNN_MYOWN }));
        comboBoxKNNoptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxKNNoptionsActionPerformed(evt);
            }
        });

        buttonSettingsAddToBatch_KNN.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_KNN.setEnabled(false);
        buttonSettingsAddToBatch_KNN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_KNNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneSettingsMethodsKNNLayout = new javax.swing.GroupLayout(paneSettingsMethodsKNN);
        paneSettingsMethodsKNN.setLayout(paneSettingsMethodsKNNLayout);
        paneSettingsMethodsKNNLayout.setHorizontalGroup(
            paneSettingsMethodsKNNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsKNNLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsKNNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSettingsKNNoptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(paneSettingsMethodsKNNLayout.createSequentialGroup()
                        .addComponent(jLabel64)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboBoxKNNoptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1183, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneSettingsMethodsKNNLayout.createSequentialGroup()
                        .addComponent(buttonSettingsAddToBatch_KNN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelKNNPercTrain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        paneSettingsMethodsKNNLayout.setVerticalGroup(
            paneSettingsMethodsKNNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsKNNLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsKNNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelKNNPercTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSettingsAddToBatch_KNN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(paneSettingsMethodsKNNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(comboBoxKNNoptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(panelSettingsKNNoptions, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE))
        );

        paneSettingsMethods.addTab("kNN", paneSettingsMethodsKNN);

        jLabel5.setText("Uses VAR with only two variables - center and radius - and produces interval results.");

        panelVARintInside.setLayout(new java.awt.BorderLayout());
        panelVARintInside.add(panelVARintInsideBecause, java.awt.BorderLayout.CENTER);

        buttonSettingsAddToBatch_VARint.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_VARint.setEnabled(false);
        buttonSettingsAddToBatch_VARint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_VARintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSettingsMethodsVARintLayout = new javax.swing.GroupLayout(panelSettingsMethodsVARint);
        panelSettingsMethodsVARint.setLayout(panelSettingsMethodsVARintLayout);
        panelSettingsMethodsVARintLayout.setHorizontalGroup(
            panelSettingsMethodsVARintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsVARintLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMethodsVARintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelVARintInside, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelSettingsMethodsVARintLayout.createSequentialGroup()
                        .addGroup(panelSettingsMethodsVARintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(panelSettingsMethodsVARintLayout.createSequentialGroup()
                                .addComponent(buttonSettingsAddToBatch_VARint)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(panelVARintPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)))
                        .addGap(10, 10, 10)
                        .addComponent(panelVARintDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelSettingsMethodsVARintLayout.setVerticalGroup(
            panelSettingsMethodsVARintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsVARintLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSettingsMethodsVARintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSettingsMethodsVARintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(panelVARintDistance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelVARintPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                    .addComponent(buttonSettingsAddToBatch_VARint))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelVARintInside, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
                .addContainerGap())
        );

        paneSettingsMethods.addTab("VAR(i)", panelSettingsMethodsVARint);

        buttonSettingsAddToBatch_SES.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_SES.setEnabled(false);
        buttonSettingsAddToBatch_SES.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_SESActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSettingsMethodsSESLayout = new javax.swing.GroupLayout(panelSettingsMethodsSES);
        panelSettingsMethodsSES.setLayout(panelSettingsMethodsSESLayout);
        panelSettingsMethodsSESLayout.setHorizontalGroup(
            panelSettingsMethodsSESLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsSESLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMethodsSESLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSESmain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelSettingsMethodsSESLayout.createSequentialGroup()
                        .addComponent(buttonSettingsAddToBatch_SES)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelSESpercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 1150, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelSettingsMethodsSESLayout.setVerticalGroup(
            panelSettingsMethodsSESLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsSESLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMethodsSESLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSESpercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSettingsAddToBatch_SES))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelSESmain, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                .addContainerGap())
        );

        paneSettingsMethods.addTab("SES", panelSettingsMethodsSES);

        jLabel51.setText("Constructs an interval out of two separate forecasts for Center and Radius.");

        jLabel154.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel154.setText("Center:");

        jLabel155.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel155.setText("Radius:");

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);

        buttonSettingsAddToBatch_SESint.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_SESint.setEnabled(false);
        buttonSettingsAddToBatch_SESint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_SESintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSettingsMethodsSESintLayout = new javax.swing.GroupLayout(panelSettingsMethodsSESint);
        panelSettingsMethodsSESint.setLayout(panelSettingsMethodsSESintLayout);
        panelSettingsMethodsSESintLayout.setHorizontalGroup(
            panelSettingsMethodsSESintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsSESintLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMethodsSESintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSettingsMethodsSESintLayout.createSequentialGroup()
                        .addGroup(panelSettingsMethodsSESintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelSettingsMethodsSESintLayout.createSequentialGroup()
                                .addComponent(jLabel154)
                                .addGap(0, 603, Short.MAX_VALUE))
                            .addComponent(panelSESint_center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelSettingsMethodsSESintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel155)
                            .addComponent(panelSESint_radius, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelSettingsMethodsSESintLayout.createSequentialGroup()
                        .addGroup(panelSettingsMethodsSESintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel51)
                            .addGroup(panelSettingsMethodsSESintLayout.createSequentialGroup()
                                .addComponent(buttonSettingsAddToBatch_SESint)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(panelSESintPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(10, 10, 10)
                        .addComponent(panelSESintDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelSettingsMethodsSESintLayout.setVerticalGroup(
            panelSettingsMethodsSESintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsSESintLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSettingsMethodsSESintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSettingsMethodsSESintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(panelSESintDistance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelSESintPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                    .addComponent(buttonSettingsAddToBatch_SESint))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSettingsMethodsSESintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSettingsMethodsSESintLayout.createSequentialGroup()
                        .addGroup(panelSettingsMethodsSESintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel154)
                            .addComponent(jLabel155))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelSettingsMethodsSESintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelSESint_center, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
                            .addComponent(panelSESint_radius, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jSeparator7))
                .addContainerGap())
        );

        paneSettingsMethods.addTab("SES(i)", panelSettingsMethodsSESint);

        buttonSettingsAddToBatch_MAvg.setText("Add to Analysis Batch");
        buttonSettingsAddToBatch_MAvg.setEnabled(false);
        buttonSettingsAddToBatch_MAvg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_MAvgActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSettingsMethodsMAvgLayout = new javax.swing.GroupLayout(panelSettingsMethodsMAvg);
        panelSettingsMethodsMAvg.setLayout(panelSettingsMethodsMAvgLayout);
        panelSettingsMethodsMAvgLayout.setHorizontalGroup(
            panelSettingsMethodsMAvgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsMAvgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMethodsMAvgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSettingsMethodsMAvgLayout.createSequentialGroup()
                        .addComponent(buttonSettingsAddToBatch_MAvg)
                        .addGap(0, 1156, Short.MAX_VALUE))
                    .addComponent(panelMAvgMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelSettingsMethodsMAvgLayout.setVerticalGroup(
            panelSettingsMethodsMAvgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsMAvgLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonSettingsAddToBatch_MAvg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelMAvgMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        paneSettingsMethods.addTab("MA", panelSettingsMethodsMAvg);

        buttonSettingsAddToBatch_Holt.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_Holt.setEnabled(false);
        buttonSettingsAddToBatch_Holt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_HoltActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSettingsMethodsHoltLayout = new javax.swing.GroupLayout(panelSettingsMethodsHolt);
        panelSettingsMethodsHolt.setLayout(panelSettingsMethodsHoltLayout);
        panelSettingsMethodsHoltLayout.setHorizontalGroup(
            panelSettingsMethodsHoltLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsHoltLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMethodsHoltLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelHoltInside, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelSettingsMethodsHoltLayout.createSequentialGroup()
                        .addComponent(buttonSettingsAddToBatch_Holt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelHoltPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 1150, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelSettingsMethodsHoltLayout.setVerticalGroup(
            panelSettingsMethodsHoltLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsHoltLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMethodsHoltLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelHoltPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSettingsAddToBatch_Holt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelHoltInside, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                .addContainerGap())
        );

        paneSettingsMethods.addTab("Holt", panelSettingsMethodsHolt);

        jLabel50.setText("Constructs an interval out of two separate forecasts for Center and Radius.");

        jLabel152.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel152.setText("Center:");

        jLabel153.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel153.setText("Radius:");

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        buttonSettingsAddToBatch_Holtint.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_Holtint.setEnabled(false);
        buttonSettingsAddToBatch_Holtint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_HoltintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSettingsMethodsHoltIntLayout = new javax.swing.GroupLayout(panelSettingsMethodsHoltInt);
        panelSettingsMethodsHoltInt.setLayout(panelSettingsMethodsHoltIntLayout);
        panelSettingsMethodsHoltIntLayout.setHorizontalGroup(
            panelSettingsMethodsHoltIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsHoltIntLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMethodsHoltIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSettingsMethodsHoltIntLayout.createSequentialGroup()
                        .addGroup(panelSettingsMethodsHoltIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelSettingsMethodsHoltIntLayout.createSequentialGroup()
                                .addComponent(jLabel152)
                                .addGap(0, 603, Short.MAX_VALUE))
                            .addComponent(panelHoltInt_center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelSettingsMethodsHoltIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel153)
                            .addComponent(panelHoltInt_radius, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelSettingsMethodsHoltIntLayout.createSequentialGroup()
                        .addGroup(panelSettingsMethodsHoltIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel50)
                            .addGroup(panelSettingsMethodsHoltIntLayout.createSequentialGroup()
                                .addComponent(buttonSettingsAddToBatch_Holtint)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(panelHoltIntPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(10, 10, 10)
                        .addComponent(panelHoltIntDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelSettingsMethodsHoltIntLayout.setVerticalGroup(
            panelSettingsMethodsHoltIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsHoltIntLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel50)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSettingsMethodsHoltIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSettingsMethodsHoltIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(panelHoltIntDistance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelHoltIntPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                    .addComponent(buttonSettingsAddToBatch_Holtint))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSettingsMethodsHoltIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSettingsMethodsHoltIntLayout.createSequentialGroup()
                        .addGroup(panelSettingsMethodsHoltIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel152)
                            .addComponent(jLabel153))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelSettingsMethodsHoltIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelHoltInt_center, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
                            .addComponent(panelHoltInt_radius, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jSeparator6))
                .addContainerGap())
        );

        paneSettingsMethods.addTab("Holt(i)", panelSettingsMethodsHoltInt);

        jLabel10.setText("Holt's method for interval data as in Maia and De Carvalho (2011).");

        jLabel11.setForeground(new java.awt.Color(204, 0, 51));
        jLabel11.setText("Experimental. Produces only point forecasts, which are probably wrong anyway.");

        buttonSettingsAddToBatch_IntervalHolt.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_IntervalHolt.setEnabled(false);
        buttonSettingsAddToBatch_IntervalHolt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_IntervalHoltActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSettingsMethodsIntervalHoltLayout = new javax.swing.GroupLayout(panelSettingsMethodsIntervalHolt);
        panelSettingsMethodsIntervalHolt.setLayout(panelSettingsMethodsIntervalHoltLayout);
        panelSettingsMethodsIntervalHoltLayout.setHorizontalGroup(
            panelSettingsMethodsIntervalHoltLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsIntervalHoltLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMethodsIntervalHoltLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelIntervalHoltMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelSettingsMethodsIntervalHoltLayout.createSequentialGroup()
                        .addGroup(panelSettingsMethodsIntervalHoltLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelSettingsMethodsIntervalHoltLayout.createSequentialGroup()
                                .addComponent(buttonSettingsAddToBatch_IntervalHolt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelIntervalHoltPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE))
                            .addGroup(panelSettingsMethodsIntervalHoltLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel11)))
                        .addGap(10, 10, 10)
                        .addComponent(panelIntervalHoltDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelSettingsMethodsIntervalHoltLayout.setVerticalGroup(
            panelSettingsMethodsIntervalHoltLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsIntervalHoltLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMethodsIntervalHoltLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSettingsMethodsIntervalHoltLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSettingsMethodsIntervalHoltLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(panelIntervalHoltDistance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelIntervalHoltPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                    .addComponent(buttonSettingsAddToBatch_IntervalHolt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelIntervalHoltMain, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
                .addContainerGap())
        );

        paneSettingsMethods.addTab("iHolt", panelSettingsMethodsIntervalHolt);

        panelSettingsMethodsHoltWinters.setPreferredSize(new java.awt.Dimension(20, 610));

        buttonSettingsAddToBatch_HoltWinters.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_HoltWinters.setEnabled(false);
        buttonSettingsAddToBatch_HoltWinters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_HoltWintersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSettingsMethodsHoltWintersLayout = new javax.swing.GroupLayout(panelSettingsMethodsHoltWinters);
        panelSettingsMethodsHoltWinters.setLayout(panelSettingsMethodsHoltWintersLayout);
        panelSettingsMethodsHoltWintersLayout.setHorizontalGroup(
            panelSettingsMethodsHoltWintersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsHoltWintersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMethodsHoltWintersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelHoltWintersInside, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelSettingsMethodsHoltWintersLayout.createSequentialGroup()
                        .addComponent(buttonSettingsAddToBatch_HoltWinters)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelHoltWintersPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 1150, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelSettingsMethodsHoltWintersLayout.setVerticalGroup(
            panelSettingsMethodsHoltWintersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsHoltWintersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMethodsHoltWintersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelHoltWintersPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSettingsAddToBatch_HoltWinters))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelHoltWintersInside, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                .addContainerGap())
        );

        paneSettingsMethods.addTab("Holt-Winters", panelSettingsMethodsHoltWinters);

        jLabel52.setText("Constructs an interval out of two separate forecasts for Center and Radius.");

        jLabel158.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel158.setText("Center:");

        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel159.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel159.setText("Radius:");

        buttonSettingsAddToBatch_HoltWintersInt.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_HoltWintersInt.setEnabled(false);
        buttonSettingsAddToBatch_HoltWintersInt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_HoltWintersIntActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSettingsMethodsHoltWintersIntLayout = new javax.swing.GroupLayout(panelSettingsMethodsHoltWintersInt);
        panelSettingsMethodsHoltWintersInt.setLayout(panelSettingsMethodsHoltWintersIntLayout);
        panelSettingsMethodsHoltWintersIntLayout.setHorizontalGroup(
            panelSettingsMethodsHoltWintersIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsHoltWintersIntLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsMethodsHoltWintersIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSettingsMethodsHoltWintersIntLayout.createSequentialGroup()
                        .addGroup(panelSettingsMethodsHoltWintersIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelSettingsMethodsHoltWintersIntLayout.createSequentialGroup()
                                .addComponent(jLabel158)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(panelHoltWintersInt_center, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelSettingsMethodsHoltWintersIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelSettingsMethodsHoltWintersIntLayout.createSequentialGroup()
                                .addComponent(jLabel159)
                                .addGap(594, 594, 594))
                            .addComponent(panelHoltWintersInt_radius, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panelSettingsMethodsHoltWintersIntLayout.createSequentialGroup()
                        .addGroup(panelSettingsMethodsHoltWintersIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel52)
                            .addGroup(panelSettingsMethodsHoltWintersIntLayout.createSequentialGroup()
                                .addComponent(buttonSettingsAddToBatch_HoltWintersInt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(panelHoltWintersIntPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelHoltWintersIntDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelSettingsMethodsHoltWintersIntLayout.setVerticalGroup(
            panelSettingsMethodsHoltWintersIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsMethodsHoltWintersIntLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel52)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSettingsMethodsHoltWintersIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSettingsMethodsHoltWintersIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(panelHoltWintersIntDistance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelHoltWintersIntPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                    .addComponent(buttonSettingsAddToBatch_HoltWintersInt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSettingsMethodsHoltWintersIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSettingsMethodsHoltWintersIntLayout.createSequentialGroup()
                        .addGroup(panelSettingsMethodsHoltWintersIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel158)
                            .addComponent(jLabel159))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelSettingsMethodsHoltWintersIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelHoltWintersInt_center, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
                            .addComponent(panelHoltWintersInt_radius, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jSeparator8))
                .addContainerGap())
        );

        paneSettingsMethods.addTab("Holt-Winters(i)", panelSettingsMethodsHoltWintersInt);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Center:");

        comboBoxSettingsHybridMethod_center.setModel(new javax.swing.DefaultComboBoxModel(new String[] { Const.NNET, Const.NNETAR, Const.RBF, Const.ARIMA, Const.KNN_FNN, Const.KNN_KKNN, Const.SES, Const.HOLT, Const.BNN }));
        comboBoxSettingsHybridMethod_center.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxSettingsHybridMethod_centerActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Radius:");

        comboBoxSettingsHybridMethod_radius.setModel(new javax.swing.DefaultComboBoxModel(new String[] { Const.NNET, Const.NNETAR, Const.RBF, Const.ARIMA, Const.KNN_FNN, Const.KNN_KKNN, Const.SES, Const.HOLT, Const.BNN }));
        comboBoxSettingsHybridMethod_radius.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxSettingsHybridMethod_radiusActionPerformed(evt);
            }
        });

        panelSettingsHybrid_centerMain.setLayout(new java.awt.CardLayout());

        scrollPane_panelSettingsHybrid_centerMain_MLPnnet.setViewportView(panelSettingsHybrid_centerMain_MLPnnet);

        panelSettingsHybrid_centerMain.add(scrollPane_panelSettingsHybrid_centerMain_MLPnnet, "panelSettingsHybrid_centerMain_MLPnnet");
        panelSettingsHybrid_centerMain.add(panelSettingsHybrid_centerMain_MLPnnetar, "panelSettingsHybrid_centerMain_MLPnnetar");
        panelSettingsHybrid_centerMain.add(panelSettingsHybrid_centerMain_RBF, "panelSettingsHybrid_centerMain_RBF");
        panelSettingsHybrid_centerMain.add(panelSettingsHybrid_centerMain_ARIMA, "panelSettingsHybrid_centerMain_ARIMA");
        panelSettingsHybrid_centerMain.add(panelSettingsHybrid_centerMain_KNNFNN, "panelSettingsHybrid_centerMain_KNNFNN");
        panelSettingsHybrid_centerMain.add(panelSettingsHybrid_centerMain_KNNkknn, "panelSettingsHybrid_centerMain_KNNkknn");
        panelSettingsHybrid_centerMain.add(panelSettingsHybrid_centerMain_SES, "panelSettingsHybrid_centerMain_SES");
        panelSettingsHybrid_centerMain.add(panelSettingsHybrid_centerMain_Holt, "panelSettingsHybrid_centerMain_Holt");
        panelSettingsHybrid_centerMain.add(panelSettingsHybrid_centerMain_BNN, "panelSettingsHybrid_centerMain_BNN");

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        panelSettingsHybrid_radiusMain.setLayout(new java.awt.CardLayout());

        scrollPane_panelSettingsHybrid_radiusMain_MLPnnet.setViewportView(panelSettingsHybrid_radiusMain_MLPnnet);

        panelSettingsHybrid_radiusMain.add(scrollPane_panelSettingsHybrid_radiusMain_MLPnnet, "panelSettingsHybrid_radiusMain_MLPnnet");
        panelSettingsHybrid_radiusMain.add(panelSettingsHybrid_radiusMain_MLPnnetar, "panelSettingsHybrid_radiusMain_MLPnnetar");
        panelSettingsHybrid_radiusMain.add(panelSettingsHybrid_radiusMain_RBF, "panelSettingsHybrid_radiusMain_RBF");
        panelSettingsHybrid_radiusMain.add(panelSettingsHybrid_radiusMain_ARIMA, "panelSettingsHybrid_radiusMain_ARIMA");
        panelSettingsHybrid_radiusMain.add(panelSettingsHybrid_radiusMain_KNNFNN, "panelSettingsHybrid_radiusMain_KNNFNN");
        panelSettingsHybrid_radiusMain.add(panelSettingsHybrid_radiusMain_KNNkknn, "panelSettingsHybrid_radiusMain_KNNkknn");
        panelSettingsHybrid_radiusMain.add(panelSettingsHybrid_radiusMain_SES, "panelSettingsHybrid_radiusMain_SES");
        panelSettingsHybrid_radiusMain.add(panelSettingsHybrid_radiusMain_Holt, "panelSettingsHybrid_radiusMain_Holt");
        panelSettingsHybrid_radiusMain.add(panelSettingsHybrid_radiusMain_BNN, "panelSettingsHybrid_radiusMain_BNN");

        buttonSettingsAddToBatch_Hybrid.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_Hybrid.setEnabled(false);
        buttonSettingsAddToBatch_Hybrid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_HybridActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneSettingsMethodsHybridLayout = new javax.swing.GroupLayout(paneSettingsMethodsHybrid);
        paneSettingsMethodsHybrid.setLayout(paneSettingsMethodsHybridLayout);
        paneSettingsMethodsHybridLayout.setHorizontalGroup(
            paneSettingsMethodsHybridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsHybridLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsHybridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneSettingsMethodsHybridLayout.createSequentialGroup()
                        .addGroup(paneSettingsMethodsHybridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneSettingsMethodsHybridLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboBoxSettingsHybridMethod_center, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(paneSettingsMethodsHybridLayout.createSequentialGroup()
                                .addComponent(buttonSettingsAddToBatch_Hybrid)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelSettingsHybridPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelSettingsHybridDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paneSettingsMethodsHybridLayout.createSequentialGroup()
                        .addComponent(panelSettingsHybrid_centerMain, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paneSettingsMethodsHybridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneSettingsMethodsHybridLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboBoxSettingsHybridMethod_radius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 620, Short.MAX_VALUE))
                            .addComponent(panelSettingsHybrid_radiusMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        paneSettingsMethodsHybridLayout.setVerticalGroup(
            paneSettingsMethodsHybridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsHybridLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsHybridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneSettingsMethodsHybridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(panelSettingsHybridDistance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelSettingsHybridPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
                    .addComponent(buttonSettingsAddToBatch_Hybrid))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(paneSettingsMethodsHybridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboBoxSettingsHybridMethod_center, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(comboBoxSettingsHybridMethod_radius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(paneSettingsMethodsHybridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelSettingsHybrid_radiusMain, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
                    .addComponent(panelSettingsHybrid_centerMain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        paneSettingsMethods.addTab("[Hybrid]", paneSettingsMethodsHybrid);

        buttonSettingsAddToBatch_BNN.setText("Add to Analysis batch");
        buttonSettingsAddToBatch_BNN.setEnabled(false);
        buttonSettingsAddToBatch_BNN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_BNNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneSettingsMethodsBNNLayout = new javax.swing.GroupLayout(paneSettingsMethodsBNN);
        paneSettingsMethodsBNN.setLayout(paneSettingsMethodsBNNLayout);
        paneSettingsMethodsBNNLayout.setHorizontalGroup(
            paneSettingsMethodsBNNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsBNNLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsBNNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSettingsBNNinside, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(paneSettingsMethodsBNNLayout.createSequentialGroup()
                        .addComponent(buttonSettingsAddToBatch_BNN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelBNNPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        paneSettingsMethodsBNNLayout.setVerticalGroup(
            paneSettingsMethodsBNNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsBNNLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsBNNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonSettingsAddToBatch_BNN)
                    .addComponent(panelBNNPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSettingsBNNinside, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        paneSettingsMethods.addTab("Bayesian NN", paneSettingsMethodsBNN);

        buttonSettingsAddToBatch_BNNint.setText("Add to Analysis Batch");
        buttonSettingsAddToBatch_BNNint.setEnabled(false);
        buttonSettingsAddToBatch_BNNint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingsAddToBatch_BNNintActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel30.setText("Center:");

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel33.setText("Radius:");

        jLabel160.setText("Distance to use for computing the error measures:");

        jLabel139.setText("Num of networks to train:");

        textFieldNumNetworksToTrainBNNint.setText("1");

        jSeparator13.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout paneSettingsMethodsBNNintLayout = new javax.swing.GroupLayout(paneSettingsMethodsBNNint);
        paneSettingsMethodsBNNint.setLayout(paneSettingsMethodsBNNintLayout);
        paneSettingsMethodsBNNintLayout.setHorizontalGroup(
            paneSettingsMethodsBNNintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsBNNintLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsBNNintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneSettingsMethodsBNNintLayout.createSequentialGroup()
                        .addComponent(buttonSettingsAddToBatch_BNNint)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelBNNintPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneSettingsMethodsBNNintLayout.createSequentialGroup()
                        .addGroup(paneSettingsMethodsBNNintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30)
                            .addComponent(panelSettingsBNNint_center, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paneSettingsMethodsBNNintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(paneSettingsMethodsBNNintLayout.createSequentialGroup()
                                .addComponent(jLabel33)
                                .addGap(626, 626, 626))
                            .addComponent(panelSettingsBNNint_radius, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(paneSettingsMethodsBNNintLayout.createSequentialGroup()
                        .addGroup(paneSettingsMethodsBNNintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel160)
                            .addComponent(panelBNNintSettingsDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(paneSettingsMethodsBNNintLayout.createSequentialGroup()
                        .addComponent(jLabel139)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldNumNetworksToTrainBNNint, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelBestModelCriterionBNNint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        paneSettingsMethodsBNNintLayout.setVerticalGroup(
            paneSettingsMethodsBNNintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSettingsMethodsBNNintLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneSettingsMethodsBNNintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelBNNintPercentTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSettingsAddToBatch_BNNint))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(paneSettingsMethodsBNNintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneSettingsMethodsBNNintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panelSettingsBNNint_center, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addComponent(jSeparator13, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSettingsBNNint_radius, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel160)
                .addGap(4, 4, 4)
                .addComponent(panelBNNintSettingsDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneSettingsMethodsBNNintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneSettingsMethodsBNNintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel139)
                        .addComponent(textFieldNumNetworksToTrainBNNint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelBestModelCriterionBNNint, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        paneSettingsMethods.addTab("Bayesian NN(i)", paneSettingsMethodsBNNint);

        javax.swing.GroupLayout panelAnalysisSettingsLayout = new javax.swing.GroupLayout(panelAnalysisSettings);
        panelAnalysisSettings.setLayout(panelAnalysisSettingsLayout);
        panelAnalysisSettingsLayout.setHorizontalGroup(
            panelAnalysisSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paneSettingsMethods, javax.swing.GroupLayout.DEFAULT_SIZE, 1404, Short.MAX_VALUE)
        );
        panelAnalysisSettingsLayout.setVerticalGroup(
            panelAnalysisSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paneSettingsMethods, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelEverything.addTab("Analysis settings", panelAnalysisSettings);

        panelRunOutside.setPreferredSize(new java.awt.Dimension(1361, 615));

        comboBoxColnamesRun.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));

        checkBoxRunMLPnnetar.setSelected(true);
        checkBoxRunMLPnnetar.setText("MLP (nnetar)");

        checkBoxRunARIMA.setText("ARIMA");

        checkBoxRunMLPnnet.setText("MLP (nnet)");

        checkBoxRunMLPneuralnet.setText("MLP (neuralnet)");
        checkBoxRunMLPneuralnet.setEnabled(false);

        checkBoxRunIntervalMLPCcode.setText("iMLP (C code)");

        checkBoxRunIntervalMLPneuralnet.setText("iMLP (neuralnet)");
        checkBoxRunIntervalMLPneuralnet.setEnabled(false);

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel41.setText("CTS:");

        jLabel42.setText("Data:");

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel43.setText("ITS:");

        jSeparator1.setForeground(new java.awt.Color(200, 200, 200));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        buttonTrainAndTest.setText("Run");
        buttonTrainAndTest.setEnabled(false);
        buttonTrainAndTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTrainAndTestActionPerformed(evt);
            }
        });

        checkBoxRunKNNfnn.setText("kNN (FNN)");

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel49.setText("(i)TS:");

        labelRunFakeIntLower.setText("Lower bound");
        labelRunFakeIntLower.setEnabled(false);

        comboBoxRunFakeIntCenter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));

        comboBoxRunFakeIntRadius.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));

        labelRunFakeIntUpper.setText("Upper bound");
        labelRunFakeIntUpper.setEnabled(false);

        checkBoxRunMLPintNnetar.setText("MLP(i) (nnetar)");

        checkBoxRunKNNinterval.setText("kNN");
        checkBoxRunKNNinterval.setEnabled(false);

        checkBoxRunKNNcustom.setText("kNN (custom)");
        checkBoxRunKNNcustom.setEnabled(false);

        jLabel71.setText("Number of forecasts to produce:");

        textFieldRunNumForecasts.setText("0");

        jLabel72.setForeground(new java.awt.Color(255, 102, 51));
        jLabel72.setText("TODO check errors for FNN");

        checkBoxRunKNNkknn.setText("kNN (kknn)");

        jLabel8.setForeground(new java.awt.Color(255, 102, 0));
        jLabel8.setText("Please note: not all models support forecasting at the moment.");

        jLabel9.setText("with data at positions");

        textFieldRunDataRangeFrom.setText("1");

        jLabel44.setText("to");

        labelRunFakeIntCenter.setText("Center");

        labelRunFakeIntRadius.setText("Radius");

        comboBoxRunFakeIntLower.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
        comboBoxRunFakeIntLower.setEnabled(false);

        comboBoxRunFakeIntUpper.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
        comboBoxRunFakeIntUpper.setEnabled(false);

        radioButtonRunFakeIntLowerUpper.setEnabled(false);
        radioButtonRunFakeIntLowerUpper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonRunFakeIntLowerUpperActionPerformed(evt);
            }
        });

        radioButtonRunFakeIntCenterRadius.setSelected(true);
        radioButtonRunFakeIntCenterRadius.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonRunFakeIntCenterRadiusActionPerformed(evt);
            }
        });

        checkBoxRunMLPintNnet.setText("MLP(i) (nnet)");

        buttonRunRestoreRangeAll.setText("<-restore all data");
        buttonRunRestoreRangeAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRunRestoreRangeAllActionPerformed(evt);
            }
        });

        checkBoxAvgSimpleCTSperM.setText("simple avg per method");
        checkBoxAvgSimpleCTSperM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgSimpleCTSperMActionPerformed(evt);
            }
        });

        checkBoxAvgSimpleIntTSperM.setText("simple avg per method");
        checkBoxAvgSimpleIntTSperM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgSimpleIntTSperMActionPerformed(evt);
            }
        });

        checkBoxAvgONLY.setText("do not show all plots, just the average");
        checkBoxAvgONLY.setEnabled(false);

        checkBoxAvgSimpleCTS.setText("simple avg");
        checkBoxAvgSimpleCTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgSimpleCTSActionPerformed(evt);
            }
        });

        checkBoxAvgSimpleIntTS.setText("simple avg");
        checkBoxAvgSimpleIntTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgSimpleIntTSActionPerformed(evt);
            }
        });

        checkBoxRunIntervalRandomWalk.setText("random walk for ITS");

        jLabel133.setForeground(new java.awt.Color(255, 0, 0));
        jLabel133.setText("(takes values from (i)TS lower upper! and distance from MLP(i)nnet)");

        checkBoxRunVAR.setText("VAR");
        checkBoxRunVAR.setEnabled(false);

        checkBoxRunRBF.setText("RBF");

        checkBoxRunRBFint.setText("RBF(i)");

        checkBoxRunHybrid.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        checkBoxRunHybrid.setText("Hybrid(i)");

        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("TODO allow to specify Lower and Upper bound (but still compute with Center and Radius)");

        checkBoxRunVARint.setText("VAR(i)");

        checkBoxRunHolt.setText("Holt");

        checkBoxRunHoltInt.setText("Holt(i)");

        checkBoxRunIntervalHolt.setText("iHolt");

        checkBoxRunIncludeRMSSE.setText("include RMSSE with length of seasonality:");
        checkBoxRunIncludeRMSSE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxRunIncludeRMSSEActionPerformed(evt);
            }
        });

        textFieldRunRMSSESeasonality.setText("0");
        textFieldRunRMSSESeasonality.setEnabled(false);

        checkBoxRunSES.setText("SES");

        checkBoxRunSESint.setText("SES(i)");

        checkBoxRunHoltWinters.setForeground(new java.awt.Color(0, 204, 51));
        checkBoxRunHoltWinters.setText("Holt-Winters (under construction)");

        checkBoxRunHoltWintersInt.setText("Holt-Winters(i)");

        checkBoxRunRandomWalkCTS.setText("random walk for CTS");

        checkBoxAvgMDeCTSperM.setText("WIP MDE avg per method");
        checkBoxAvgMDeCTSperM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgMDeCTSperMActionPerformed(evt);
            }
        });

        checkBoxAvgMDeCTS.setText("WIP MDE avg");
        checkBoxAvgMDeCTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgMDeCTSActionPerformed(evt);
            }
        });

        checkBoxAvgMDeIntTSperM.setText("WIP MDE avg per method");
        checkBoxAvgMDeIntTSperM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgMDeIntTSperMActionPerformed(evt);
            }
        });

        checkBoxAvgMDeIntTS.setText("WIP MDE avg");
        checkBoxAvgMDeIntTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgMDeIntTSActionPerformed(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel45.setText("Forecasts combination:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("CTS:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setText("ITS:");

        checkBoxAvgTheilsuCTSperM.setText("WIP TheilsU avg per method");
        checkBoxAvgTheilsuCTSperM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgTheilsuCTSperMActionPerformed(evt);
            }
        });

        checkBoxAvgTheilsuCTS.setText("WIP TheilsU avg");
        checkBoxAvgTheilsuCTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgTheilsuCTSActionPerformed(evt);
            }
        });

        checkBoxAvgTheilsuIntTSperM.setText("WIP TheilsU avg per method");
        checkBoxAvgTheilsuIntTSperM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgTheilsuIntTSperMActionPerformed(evt);
            }
        });

        checkBoxAvgTheilsuIntTS.setText("WIP TheilsU avg");
        checkBoxAvgTheilsuIntTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgTheilsuIntTSActionPerformed(evt);
            }
        });

        checkBoxAvgCvgEffIntTSperM.setText("WDP cvg+eff per method");
        checkBoxAvgCvgEffIntTSperM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgCvgEffIntTSperMActionPerformed(evt);
            }
        });

        checkBoxAvgCvgEffIntTS.setText("WDP cvg+eff");
        checkBoxAvgCvgEffIntTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgCvgEffIntTSActionPerformed(evt);
            }
        });

        checkBoxAvgCenterLogRadiusIntTSperM.setText("avg center logradius per method");
        checkBoxAvgCenterLogRadiusIntTSperM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgCenterLogRadiusIntTSperMActionPerformed(evt);
            }
        });

        checkBoxAvgCenterLogRadiusIntTS.setText("avg center logradius");
        checkBoxAvgCenterLogRadiusIntTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgCenterLogRadiusIntTSActionPerformed(evt);
            }
        });

        checkBoxAvgMedianCTSperM.setText("median per method");
        checkBoxAvgMedianCTSperM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgMedianCTSperMActionPerformed(evt);
            }
        });

        checkBoxAvgMedianCTS.setText("median");
        checkBoxAvgMedianCTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgMedianCTSActionPerformed(evt);
            }
        });

        checkBoxAvgMedianIntTSperM.setText("median per method");
        checkBoxAvgMedianIntTSperM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgMedianIntTSperMActionPerformed(evt);
            }
        });

        checkBoxAvgMedianIntTS.setText("median");
        checkBoxAvgMedianIntTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAvgMedianIntTSActionPerformed(evt);
            }
        });

        checkBoxRunBNN.setText("Bayesian NN");

        checkBoxRunBNNInt.setText("BNN(i)");

        checkBoxRunKNNmyown.setText("kNN (my own)");
        checkBoxRunKNNmyown.setEnabled(false);

        checkBoxRunMAvg.setText("MA");

        javax.swing.GroupLayout panelRunOutsideLayout = new javax.swing.GroupLayout(panelRunOutside);
        panelRunOutside.setLayout(panelRunOutsideLayout);
        panelRunOutsideLayout.setHorizontalGroup(
            panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                        .addComponent(jLabel41)
                                        .addGap(42, 42, 42))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRunOutsideLayout.createSequentialGroup()
                                        .addComponent(jLabel42)
                                        .addGap(26, 26, 26)))
                                .addComponent(comboBoxColnamesRun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel49)
                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addComponent(radioButtonRunFakeIntLowerUpper))
                                    .addComponent(radioButtonRunFakeIntCenterRadius, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(labelRunFakeIntLower)
                                            .addComponent(labelRunFakeIntUpper))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(comboBoxRunFakeIntLower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(comboBoxRunFakeIntUpper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                                .addComponent(labelRunFakeIntRadius)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comboBoxRunFakeIntRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                                .addComponent(labelRunFakeIntCenter)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comboBoxRunFakeIntCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addComponent(jLabel43)
                            .addComponent(jLabel45)
                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                .addComponent(jLabel71)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textFieldRunNumForecasts, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRunOutsideLayout.createSequentialGroup()
                                .addComponent(jLabelTrainingInfo)
                                .addGap(10, 10, 10))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBoxRunHybrid)
                    .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                .addComponent(checkBoxRunMLPnnetar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBoxRunMLPneuralnet)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBoxRunMLPnnet)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBoxRunRBF))
                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                .addComponent(checkBoxRunRandomWalkCTS)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkBoxRunBNN)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBoxRunMAvg)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                .addGap(129, 129, 129)
                                .addComponent(jLabel72))
                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                .addComponent(checkBoxRunARIMA)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkBoxRunKNNfnn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBoxRunKNNcustom)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkBoxRunKNNkknn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkBoxRunKNNmyown)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkBoxRunVAR)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBoxRunSES)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBoxRunHolt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBoxRunHoltWinters))))
                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                        .addComponent(checkBoxRunIntervalMLPCcode)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBoxRunIntervalMLPneuralnet)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBoxRunKNNinterval)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBoxRunIntervalHolt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBoxRunIntervalRandomWalk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel133))
                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                        .addGap(622, 622, 622)
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkBoxAvgTheilsuCTSperM)
                            .addComponent(checkBoxAvgMedianCTSperM))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkBoxAvgMedianCTS)
                            .addComponent(checkBoxAvgTheilsuCTS)))
                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(checkBoxAvgSimpleCTSperM)
                        .addGap(18, 18, 18)
                        .addComponent(checkBoxAvgSimpleCTS)
                        .addGap(32, 32, 32)
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkBoxAvgMDeCTSperM)
                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                .addGap(181, 181, 181)
                                .addComponent(checkBoxAvgMDeCTS))))
                    .addComponent(checkBoxAvgONLY)
                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(18, 18, 18)
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                .addComponent(checkBoxAvgSimpleIntTSperM)
                                .addGap(18, 18, 18)
                                .addComponent(checkBoxAvgSimpleIntTS)
                                .addGap(32, 32, 32)
                                .addComponent(checkBoxAvgMDeIntTSperM)
                                .addGap(32, 32, 32)
                                .addComponent(checkBoxAvgMDeIntTS)
                                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                        .addGap(211, 211, 211)
                                        .addComponent(checkBoxAvgMedianIntTS))
                                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addComponent(checkBoxAvgTheilsuIntTSperM)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(checkBoxAvgTheilsuIntTS))))
                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                .addComponent(checkBoxAvgCvgEffIntTSperM)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBoxAvgCvgEffIntTS)
                                .addGap(16, 16, 16)
                                .addComponent(checkBoxAvgCenterLogRadiusIntTSperM)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkBoxAvgCenterLogRadiusIntTS)
                                .addGap(18, 18, 18)
                                .addComponent(checkBoxAvgMedianIntTSperM))))
                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                        .addComponent(checkBoxRunMLPintNnetar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBoxRunMLPintNnet)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBoxRunRBFint)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBoxRunVARint)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBoxRunSESint)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBoxRunHoltInt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBoxRunHoltWintersInt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBoxRunBNNInt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3))
                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                        .addComponent(buttonTrainAndTest)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldRunDataRangeFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textFieldRunDataRangeTo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonRunRestoreRangeAll)
                        .addGap(81, 81, 81)
                        .addComponent(checkBoxRunIncludeRMSSE, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldRunRMSSESeasonality, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(45, 46, Short.MAX_VALUE))
        );
        panelRunOutsideLayout.setVerticalGroup(
            panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                .addComponent(jLabelTrainingInfo)
                                .addGap(29, 29, 29))
                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(textFieldRunNumForecasts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel71))
                                .addGap(1, 1, 1)
                                .addComponent(jLabel8))))
                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonTrainAndTest)
                                .addComponent(jLabel9)
                                .addComponent(textFieldRunDataRangeFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel44)
                                .addComponent(textFieldRunDataRangeTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonRunRestoreRangeAll))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(checkBoxRunIncludeRMSSE)
                                .addComponent(textFieldRunRMSSESeasonality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRunOutsideLayout.createSequentialGroup()
                                .addComponent(jLabel41)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel42)
                                    .addComponent(comboBoxColnamesRun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, 14)
                                .addComponent(jLabel49)
                                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(labelRunFakeIntLower)
                                            .addComponent(comboBoxRunFakeIntLower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(labelRunFakeIntUpper)
                                            .addComponent(comboBoxRunFakeIntUpper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(radioButtonRunFakeIntLowerUpper)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(labelRunFakeIntCenter)
                                            .addComponent(comboBoxRunFakeIntCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(labelRunFakeIntRadius)
                                            .addComponent(comboBoxRunFakeIntRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRunOutsideLayout.createSequentialGroup()
                                        .addComponent(radioButtonRunFakeIntCenterRadius)
                                        .addGap(20, 20, 20))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRunOutsideLayout.createSequentialGroup()
                                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(checkBoxRunMLPnnetar)
                                    .addComponent(checkBoxRunMLPneuralnet)
                                    .addComponent(checkBoxRunMLPnnet)
                                    .addComponent(checkBoxRunARIMA)
                                    .addComponent(checkBoxRunKNNfnn)
                                    .addComponent(checkBoxRunKNNcustom)
                                    .addComponent(checkBoxRunKNNkknn)
                                    .addComponent(checkBoxRunVAR)
                                    .addComponent(checkBoxRunRBF)
                                    .addComponent(checkBoxRunHolt)
                                    .addComponent(checkBoxRunSES)
                                    .addComponent(checkBoxRunHoltWinters)
                                    .addComponent(checkBoxRunKNNmyown))
                                .addGap(1, 1, 1)
                                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel72)
                                    .addComponent(checkBoxRunRandomWalkCTS)
                                    .addComponent(checkBoxRunBNN)
                                    .addComponent(checkBoxRunMAvg))
                                .addGap(20, 20, 20)
                                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(checkBoxRunHybrid)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(checkBoxRunMLPintNnetar)
                                    .addComponent(checkBoxRunMLPintNnet)
                                    .addComponent(checkBoxRunRBFint)
                                    .addComponent(jLabel3)
                                    .addComponent(checkBoxRunVARint)
                                    .addComponent(checkBoxRunHoltInt)
                                    .addComponent(checkBoxRunSESint)
                                    .addComponent(checkBoxRunHoltWintersInt)
                                    .addComponent(checkBoxRunBNNInt))
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel43)
                            .addComponent(checkBoxRunKNNinterval)
                            .addComponent(checkBoxRunIntervalMLPneuralnet)
                            .addComponent(checkBoxRunIntervalMLPCcode)
                            .addComponent(checkBoxRunIntervalRandomWalk)
                            .addComponent(jLabel133)
                            .addComponent(checkBoxRunIntervalHolt))
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel15))
                                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(checkBoxAvgMDeCTSperM)
                                                .addComponent(checkBoxAvgMDeCTS)
                                                .addComponent(checkBoxAvgTheilsuCTSperM)
                                                .addComponent(checkBoxAvgTheilsuCTS))
                                            .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(checkBoxAvgSimpleCTSperM)
                                                .addComponent(checkBoxAvgSimpleCTS))))))
                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(jLabel45)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(checkBoxAvgMedianCTSperM)
                            .addComponent(checkBoxAvgMedianCTS))
                        .addGap(18, 18, 18)
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel16))
                            .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(checkBoxAvgSimpleIntTSperM)
                                .addComponent(checkBoxAvgSimpleIntTS))
                            .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(checkBoxAvgMDeIntTS)
                                .addComponent(checkBoxAvgTheilsuIntTSperM)
                                .addComponent(checkBoxAvgTheilsuIntTS))
                            .addComponent(checkBoxAvgMDeIntTSperM))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkBoxAvgMedianIntTS)
                            .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(checkBoxAvgCvgEffIntTSperM)
                                .addComponent(checkBoxAvgCvgEffIntTS)
                                .addComponent(checkBoxAvgCenterLogRadiusIntTSperM)
                                .addComponent(checkBoxAvgCenterLogRadiusIntTS)
                                .addComponent(checkBoxAvgMedianIntTSperM)))
                        .addGap(21, 21, 21)
                        .addComponent(checkBoxAvgONLY))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRunOutsideLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(75, 75, 75))
        );

        panelEverything.addTab("Run", panelRunOutside);

        panelErrorMeasuresAll.setPreferredSize(new java.awt.Dimension(1361, 615));

        javax.swing.GroupLayout panelErrorMeasuresLayout = new javax.swing.GroupLayout(panelErrorMeasures);
        panelErrorMeasures.setLayout(panelErrorMeasuresLayout);
        panelErrorMeasuresLayout.setHorizontalGroup(
            panelErrorMeasuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelErrorMeasuresLayout.setVerticalGroup(
            panelErrorMeasuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 608, Short.MAX_VALUE)
        );

        buttonRunShowHiddenErrorMeasures.setText("Show hidden rows");
        buttonRunShowHiddenErrorMeasures.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRunShowHiddenErrorMeasuresActionPerformed(evt);
            }
        });

        buttonHideAllErrorsExceptAvg.setText("Hide all rows except for avg");
        buttonHideAllErrorsExceptAvg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonHideAllErrorsExceptAvgActionPerformed(evt);
            }
        });

        jLabel13.setText("(double-click on a row to hide it.)");

        buttonRunExportErrorMeasures.setText("Export these error measures");
        buttonRunExportErrorMeasures.setEnabled(false);
        buttonRunExportErrorMeasures.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRunExportErrorMeasuresActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelErrorMeasuresAllLayout = new javax.swing.GroupLayout(panelErrorMeasuresAll);
        panelErrorMeasuresAll.setLayout(panelErrorMeasuresAllLayout);
        panelErrorMeasuresAllLayout.setHorizontalGroup(
            panelErrorMeasuresAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelErrorMeasuresAllLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelErrorMeasuresAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelErrorMeasures, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelErrorMeasuresAllLayout.createSequentialGroup()
                        .addComponent(buttonRunShowHiddenErrorMeasures)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonHideAllErrorsExceptAvg)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(buttonRunExportErrorMeasures)
                        .addGap(0, 742, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelErrorMeasuresAllLayout.setVerticalGroup(
            panelErrorMeasuresAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelErrorMeasuresAllLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelErrorMeasuresAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonRunShowHiddenErrorMeasures)
                    .addComponent(buttonHideAllErrorsExceptAvg)
                    .addComponent(jLabel13)
                    .addComponent(buttonRunExportErrorMeasures))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelErrorMeasures, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelEverything.addTab("Error measures", panelErrorMeasuresAll);

        gdBufferedPanelPlotResiduals = new JGDBufferedPanel(panelResidualsPlot.getWidth(), panelResidualsPlot.getHeight());
        panelResidualsPlot.add(gdBufferedPanelPlotResiduals, BorderLayout.CENTER);

        javax.swing.GroupLayout panelResidualsPlotLayout = new javax.swing.GroupLayout(panelResidualsPlot);
        panelResidualsPlot.setLayout(panelResidualsPlotLayout);
        panelResidualsPlotLayout.setHorizontalGroup(
            panelResidualsPlotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelResidualsPlotLayout.setVerticalGroup(
            panelResidualsPlotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 468, Short.MAX_VALUE)
        );

        textAreaResidualsBasicStats.setColumns(20);
        textAreaResidualsBasicStats.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        textAreaResidualsBasicStats.setRows(5);
        jScrollPane5.setViewportView(textAreaResidualsBasicStats);

        buttonExportResiduals.setText("Export");
        buttonExportResiduals.setEnabled(false);
        buttonExportResiduals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExportResidualsActionPerformed(evt);
            }
        });

        scrollPaneResiduals.setPreferredSize(new java.awt.Dimension(100, 600));

        javax.swing.GroupLayout panelResidualsLayout = new javax.swing.GroupLayout(panelResiduals);
        panelResiduals.setLayout(panelResidualsLayout);
        panelResidualsLayout.setHorizontalGroup(
            panelResidualsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneResiduals, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
        );
        panelResidualsLayout.setVerticalGroup(
            panelResidualsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneResiduals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelResidualsLeftLayout = new javax.swing.GroupLayout(panelResidualsLeft);
        panelResidualsLeft.setLayout(panelResidualsLeftLayout);
        panelResidualsLeftLayout.setHorizontalGroup(
            panelResidualsLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelResidualsLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelResidualsLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelResiduals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelResidualsLeftLayout.createSequentialGroup()
                        .addComponent(buttonExportResiduals)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        panelResidualsLeftLayout.setVerticalGroup(
            panelResidualsLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelResidualsLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonExportResiduals)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelResiduals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        buttonPlotResiduals.setText("Plot");
        buttonPlotResiduals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotResidualsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelResidualsRightButtonsLayout = new javax.swing.GroupLayout(panelResidualsRightButtons);
        panelResidualsRightButtons.setLayout(panelResidualsRightButtonsLayout);
        panelResidualsRightButtonsLayout.setHorizontalGroup(
            panelResidualsRightButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelResidualsRightButtonsLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(buttonPlotResiduals)
                .addContainerGap(87, Short.MAX_VALUE))
        );
        panelResidualsRightButtonsLayout.setVerticalGroup(
            panelResidualsRightButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelResidualsRightButtonsLayout.createSequentialGroup()
                .addComponent(buttonPlotResiduals)
                .addGap(0, 613, Short.MAX_VALUE))
        );

        listPlotResidualsLegend.setModel(new DefaultListModel());
        jScrollPane6.setViewportView(listPlotResidualsLegend);

        javax.swing.GroupLayout panelResidualsAllLayout = new javax.swing.GroupLayout(panelResidualsAll);
        panelResidualsAll.setLayout(panelResidualsAllLayout);
        panelResidualsAllLayout.setHorizontalGroup(
            panelResidualsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelResidualsAllLayout.createSequentialGroup()
                .addComponent(panelResidualsLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelResidualsRightButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelResidualsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelResidualsPlot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelResidualsAllLayout.setVerticalGroup(
            panelResidualsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelResidualsAllLayout.createSequentialGroup()
                .addGroup(panelResidualsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelResidualsLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelResidualsAllLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelResidualsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelResidualsAllLayout.createSequentialGroup()
                                .addComponent(panelResidualsRightButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panelResidualsAllLayout.createSequentialGroup()
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelResidualsPlot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)))))
                .addContainerGap())
        );

        panelEverything.addTab("Residuals", panelResidualsAll);

        panelForecastValsAll.setPreferredSize(new java.awt.Dimension(1361, 615));

        buttonExportForecastValues.setText("Export these values");
        buttonExportForecastValues.setEnabled(false);
        buttonExportForecastValues.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExportForecastValuesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelForecastValsLayout = new javax.swing.GroupLayout(panelForecastVals);
        panelForecastVals.setLayout(panelForecastValsLayout);
        panelForecastValsLayout.setHorizontalGroup(
            panelForecastValsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneForecastVals)
        );
        panelForecastValsLayout.setVerticalGroup(
            panelForecastValsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneForecastVals, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
        );

        buttonForecastValsShowHidden.setText("Show hidden columns");
        buttonForecastValsShowHidden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonForecastValsShowHiddenActionPerformed(evt);
            }
        });

        buttonForecastValsHideNoForecasts.setText("Hide models without forecasts");
        buttonForecastValsHideNoForecasts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonForecastValsHideNoForecastsActionPerformed(evt);
            }
        });

        jLabel14.setText("(double-click inside a column to hide it.)");

        buttonForecastValsHideAllButAvg.setText("Hide all except for average");
        buttonForecastValsHideAllButAvg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonForecastValsHideAllButAvgActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelForecastValsAllLayout = new javax.swing.GroupLayout(panelForecastValsAll);
        panelForecastValsAll.setLayout(panelForecastValsAllLayout);
        panelForecastValsAllLayout.setHorizontalGroup(
            panelForecastValsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelForecastValsAllLayout.createSequentialGroup()
                .addComponent(buttonExportForecastValues)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonForecastValsShowHidden)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonForecastValsHideNoForecasts)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonForecastValsHideAllButAvg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addGap(0, 591, Short.MAX_VALUE))
            .addComponent(panelForecastVals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelForecastValsAllLayout.setVerticalGroup(
            panelForecastValsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelForecastValsAllLayout.createSequentialGroup()
                .addGroup(panelForecastValsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonExportForecastValues)
                    .addComponent(buttonForecastValsShowHidden)
                    .addComponent(buttonForecastValsHideNoForecasts)
                    .addComponent(jLabel14)
                    .addComponent(buttonForecastValsHideAllButAvg))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelForecastVals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelEverything.addTab("Forecast values", panelForecastValsAll);

        panelDiagramsNNs.setPreferredSize(new java.awt.Dimension(1361, 615));

        javax.swing.GroupLayout panelDiagramsNNsInsideLayout = new javax.swing.GroupLayout(panelDiagramsNNsInside);
        panelDiagramsNNsInside.setLayout(panelDiagramsNNsInsideLayout);
        panelDiagramsNNsInsideLayout.setHorizontalGroup(
            panelDiagramsNNsInsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelDiagramsNNsInsideLayout.setVerticalGroup(
            panelDiagramsNNsInsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 586, Short.MAX_VALUE)
        );

        buttonExportDiagramsNN.setText("Export these diagrams");
        buttonExportDiagramsNN.setEnabled(false);

        javax.swing.GroupLayout panelDiagramsNNsLayout = new javax.swing.GroupLayout(panelDiagramsNNs);
        panelDiagramsNNs.setLayout(panelDiagramsNNsLayout);
        panelDiagramsNNsLayout.setHorizontalGroup(
            panelDiagramsNNsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDiagramsNNsLayout.createSequentialGroup()
                .addComponent(buttonExportDiagramsNN)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDiagramsNNsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPaneDiagramsNNs, javax.swing.GroupLayout.PREFERRED_SIZE, 1335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelDiagramsNNsInside, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelDiagramsNNsLayout.setVerticalGroup(
            panelDiagramsNNsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDiagramsNNsLayout.createSequentialGroup()
                .addComponent(buttonExportDiagramsNN)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDiagramsNNsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelDiagramsNNsInside, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tabbedPaneDiagramsNNs)))
        );

        panelEverything.addTab("Diagrams of NNs", panelDiagramsNNs);

        panelPredictionIntervalsAll.setPreferredSize(new java.awt.Dimension(1361, 615));

        buttonExportPredictionIntervals.setText("Export");
        buttonExportPredictionIntervals.setEnabled(false);

        javax.swing.GroupLayout panelPredictionIntervalsLayout = new javax.swing.GroupLayout(panelPredictionIntervals);
        panelPredictionIntervals.setLayout(panelPredictionIntervalsLayout);
        panelPredictionIntervalsLayout.setHorizontalGroup(
            panelPredictionIntervalsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPanePredictionIntervals)
        );
        panelPredictionIntervalsLayout.setVerticalGroup(
            panelPredictionIntervalsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPredictionIntervalsLayout.createSequentialGroup()
                .addComponent(scrollPanePredictionIntervals, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelPredictionIntervalsAllLayout = new javax.swing.GroupLayout(panelPredictionIntervalsAll);
        panelPredictionIntervalsAll.setLayout(panelPredictionIntervalsAllLayout);
        panelPredictionIntervalsAllLayout.setHorizontalGroup(
            panelPredictionIntervalsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPredictionIntervalsAllLayout.createSequentialGroup()
                .addComponent(buttonExportPredictionIntervals)
                .addGap(0, 1339, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPredictionIntervalsAllLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelPredictionIntervals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelPredictionIntervalsAllLayout.setVerticalGroup(
            panelPredictionIntervalsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPredictionIntervalsAllLayout.createSequentialGroup()
                .addComponent(buttonExportPredictionIntervals)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelPredictionIntervals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelEverything.addTab("Prediction intervals", panelPredictionIntervalsAll);
        panelEverything.removeTabAt(panelEverything.getTabCount() - 1);

        panelAnalysisBatch.setPreferredSize(new java.awt.Dimension(1361, 615));

        jLabel1.setText("TODO: add checkboxes");

        buttonAnalysisBatchRemoveSelectedRows.setText("Remove selected");
        buttonAnalysisBatchRemoveSelectedRows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAnalysisBatchRemoveSelectedRowsActionPerformed(evt);
            }
        });

        tableAnalysisBatch.setModel(AnalysisBatchTableModel.getInstance());
        TableColumn firstColumn = tableAnalysisBatch.getColumnModel().getColumn(0);
        firstColumn.setMinWidth(10);
        firstColumn.setMaxWidth(50);
        TableColumn secondColumn = tableAnalysisBatch.getColumnModel().getColumn(1);
        secondColumn.setMinWidth(50);
        secondColumn.setMaxWidth(110);
        scrollPaneAnalysisBatchInside.setViewportView(tableAnalysisBatch);

        buttonRunAnalysisBatch.setText("Run analysis batch");
        buttonRunAnalysisBatch.setEnabled(false);
        buttonRunAnalysisBatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRunAnalysisBatchActionPerformed(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(204, 0, 0));
        jLabel7.setText("TODO (later) dialog with data selection. for now takes what is selected here. and TODO: the plot! not everything into 1.");

        javax.swing.GroupLayout panelAnalysisBatchLayout = new javax.swing.GroupLayout(panelAnalysisBatch);
        panelAnalysisBatch.setLayout(panelAnalysisBatchLayout);
        panelAnalysisBatchLayout.setHorizontalGroup(
            panelAnalysisBatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAnalysisBatchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAnalysisBatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPaneAnalysisBatchInside, javax.swing.GroupLayout.DEFAULT_SIZE, 1384, Short.MAX_VALUE)
                    .addGroup(panelAnalysisBatchLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(buttonAnalysisBatchRemoveSelectedRows)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonRunAnalysisBatch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)))
                .addContainerGap())
        );
        panelAnalysisBatchLayout.setVerticalGroup(
            panelAnalysisBatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAnalysisBatchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAnalysisBatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAnalysisBatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(buttonRunAnalysisBatch)
                        .addComponent(jLabel7))
                    .addGroup(panelAnalysisBatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(buttonAnalysisBatchRemoveSelectedRows)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneAnalysisBatchInside, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelEverything.addTab("[Analysis batch]", panelAnalysisBatch);

        jButton2.setText("Export");
        jButton2.setEnabled(false);

        javax.swing.GroupLayout panelCombinationWeightsLayout = new javax.swing.GroupLayout(panelCombinationWeights);
        panelCombinationWeights.setLayout(panelCombinationWeightsLayout);
        panelCombinationWeightsLayout.setHorizontalGroup(
            panelCombinationWeightsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1384, Short.MAX_VALUE)
        );
        panelCombinationWeightsLayout.setVerticalGroup(
            panelCombinationWeightsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 619, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelCombinationWeightsAllLayout = new javax.swing.GroupLayout(panelCombinationWeightsAll);
        panelCombinationWeightsAll.setLayout(panelCombinationWeightsAllLayout);
        panelCombinationWeightsAllLayout.setHorizontalGroup(
            panelCombinationWeightsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCombinationWeightsAllLayout.createSequentialGroup()
                .addComponent(jButton2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelCombinationWeightsAllLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCombinationWeights, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelCombinationWeightsAllLayout.setVerticalGroup(
            panelCombinationWeightsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCombinationWeightsAllLayout.createSequentialGroup()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelCombinationWeights, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelEverything.addTab("Combination weights", panelCombinationWeightsAll);
        panelEverything.removeTabAt(panelEverything.getTabCount() - 1);

        jLabel23.setText("Models and methods used in the last analysis:");

        textAreaModelsInfo.setEditable(false);
        textAreaModelsInfo.setColumns(20);
        textAreaModelsInfo.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        textAreaModelsInfo.setLineWrap(true);
        textAreaModelsInfo.setRows(5);
        jScrollPane8.setViewportView(textAreaModelsInfo);

        javax.swing.GroupLayout panelModelDescriptionsAllLayout = new javax.swing.GroupLayout(panelModelDescriptionsAll);
        panelModelDescriptionsAll.setLayout(panelModelDescriptionsAllLayout);
        panelModelDescriptionsAllLayout.setHorizontalGroup(
            panelModelDescriptionsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModelDescriptionsAllLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelModelDescriptionsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelModelDescriptionsAllLayout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(0, 1165, Short.MAX_VALUE))
                    .addComponent(jScrollPane8))
                .addContainerGap())
        );
        panelModelDescriptionsAllLayout.setVerticalGroup(
            panelModelDescriptionsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModelDescriptionsAllLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelEverything.addTab("Model descriptions", panelModelDescriptionsAll);

        panelBayesianSettings.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        panelBinomPropPlot.setLayout(new java.awt.BorderLayout());
        panelBinomPropPlot.add(tabbedPaneBinomPropPlot, java.awt.BorderLayout.CENTER);

        textAreaBinomPropInfo.setColumns(20);
        textAreaBinomPropInfo.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        textAreaBinomPropInfo.setRows(5);
        textAreaBinomPropInfo.setFocusable(false);
        textAreaBinomPropInfo.setOpaque(false);
        panelBinomPropInfo.setViewportView(textAreaBinomPropInfo);

        buttonBinomPropComputePosterior.setText("Compute posterior");
        buttonBinomPropComputePosterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBinomPropComputePosteriorActionPerformed(evt);
            }
        });

        buttonBinomPropSimulate.setText("Simulate from posterior distr.");
        buttonBinomPropSimulate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBinomPropSimulateActionPerformed(evt);
            }
        });

        jLabel26.setText("and construct the");

        textFieldBinomPropPercProbInterval.setText("90");

        jLabel27.setText("% probability interval");

        buttonBinomPropPredict.setText("Predict");
        buttonBinomPropPredict.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBinomPropPredictActionPerformed(evt);
            }
        });

        jLabel28.setText("the distr. of successes in the next");

        textFieldBinomPropNumFutureObs.setText("20");

        jLabel29.setText("observations");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(panelBinomPropInfo, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(panelBinomPropSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(buttonBinomPropComputePosterior)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(buttonBinomPropPredict)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textFieldBinomPropNumFutureObs))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(buttonBinomPropSimulate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textFieldBinomPropPercProbInterval, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(jLabel29))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBinomPropPlot, javax.swing.GroupLayout.PREFERRED_SIZE, 708, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelBinomPropPlot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonBinomPropComputePosterior)
                            .addComponent(panelBinomPropSettings, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonBinomPropSimulate)
                            .addComponent(jLabel26)
                            .addComponent(textFieldBinomPropPercProbInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonBinomPropPredict)
                            .addComponent(jLabel28)
                            .addComponent(textFieldBinomPropNumFutureObs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelBinomPropInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        panelBayesianSettings.addTab("Binomial Proportion", jPanel5);

        javax.swing.GroupLayout panelBayesianAllLayout = new javax.swing.GroupLayout(panelBayesianAll);
        panelBayesianAll.setLayout(panelBayesianAllLayout);
        panelBayesianAllLayout.setHorizontalGroup(
            panelBayesianAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBayesianSettings, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        panelBayesianAllLayout.setVerticalGroup(
            panelBayesianAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBayesianSettings)
        );

        panelEverything.addTab("Bayesian", panelBayesianAll);

        getContentPane().add(panelEverything, java.awt.BorderLayout.CENTER);

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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuFileLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileLoadActionPerformed
        JFileChooser fileChooser = new JFileChooser((File)null);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setAcceptAllFileFilterUsed(false); //do not allow "All files"
        fileChooser.setFileFilter(new FileFilterXlsXlsxCsv());
        LoadDataCustomizerPanel customizer = new LoadDataCustomizerPanel();
        fileChooser.setAccessory(customizer);
        if (evt.getSource() == menuFileLoad) {
            switch (fileChooser.showOpenDialog(this)) {
                case JFileChooser.APPROVE_OPTION:
                    this.loadedFile = fileChooser.getSelectedFile();
                    DataTableModel.getInstance().openFile(loadedFile, customizer);
                    DataTableModel.getInstance().fireTableStructureChanged();
                    fillGUIelementsWithNewData();
                    break;
                case JFileChooser.CANCEL_OPTION:
                default:
                    this.loadedFile = null;
            }
        }
    }//GEN-LAST:event_menuFileLoadActionPerformed

    private void menuFileExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileExitActionPerformed
        MyRengine.stopRengine();
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_menuFileExitActionPerformed

    private void buttonPlotColnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotColnameActionPerformed
        drawPlotGeneral(true, "plot.ts", "");
        setPlotRanges(1, 0);
        
        panelEverything.setSelectedComponent(panelPlotImage);
    }//GEN-LAST:event_buttonPlotColnameActionPerformed

    private void comboBoxRPackageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxRPackageActionPerformed
        CardLayout card = (CardLayout)panelSettingsMLPPackage.getLayout();
        switch (comboBoxRPackage.getSelectedItem().toString()) {
            case Const.NNET:
                card.show(panelSettingsMLPPackage, "panelSettingsMLPPackage_nnet");
                break;
            case Const.NNETAR:
                card.show(panelSettingsMLPPackage, "panelSettingsMLPPackage_nnetar");
                break;
            case Const.NEURALNET:
                card.show(panelSettingsMLPPackage, "panelSettingsMLPPackage_neuralnet");
                break;
        }
        panelSettingsMLPPackage.repaint();
    }//GEN-LAST:event_comboBoxRPackageActionPerformed

    private void buttonACFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonACFActionPerformed
        PlotDrawer.drawSimpleFctionToGrid("acf", listColnames.getSelectedValuesList(), DataTableModel.getInstance(), tabbedPaneAnalysisPlotsCTS);
        setPlotRanges(0, 0);
        buttonExportAnalysisPlotsCTS.setEnabled(true);
    }//GEN-LAST:event_buttonACFActionPerformed

    private void buttonPACFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPACFActionPerformed
        PlotDrawer.drawSimpleFctionToGrid("pacf", listColnames.getSelectedValuesList(), DataTableModel.getInstance(), tabbedPaneAnalysisPlotsCTS);
        setPlotRanges(0, 0);
        buttonExportAnalysisPlotsCTS.setEnabled(true);
    }//GEN-LAST:event_buttonPACFActionPerformed

    private void buttonPlotSelectedITSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotSelectedITSActionPerformed
        List<IntervalNamesCentreRadius> listITSPlotCR = new ArrayList<>();
        List<IntervalNamesLowerUpper> listITSPlotLU = new ArrayList<>();
        
        List<Object> values = listPlotITSspecs.getSelectedValuesList();
        for (Object val : values) {
            if (val instanceof IntervalNamesCentreRadius) {
                listITSPlotCR.add((IntervalNamesCentreRadius) val);
            } else if (val instanceof IntervalNamesLowerUpper) {
                listITSPlotLU.add((IntervalNamesLowerUpper) val);
            }
        }
        
        PlotDrawer.drawPlotsITS(true, new CallParamsDrawPlotsITS(listPlotLegend, gdBufferedPanelPlot, panelPlot.getWidth(), 
                panelPlot.getHeight(), DataTableModel.getInstance(),
                listITSPlotCR, listITSPlotLU, false));
        buttonPlotExportPlot.setEnabled(true);
        setPlotRanges(0, 1);
        
        panelEverything.setSelectedComponent(panelPlotImage);
    }//GEN-LAST:event_buttonPlotSelectedITSActionPerformed

    private void comboBoxIntervalMLPModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxIntervalMLPModeActionPerformed
        CardLayout card = (CardLayout)panelSettingsIntervalMLPMode.getLayout();
        switch (comboBoxIntervalMLPMode.getSelectedItem().toString()) {
            case Const.INTERVAL_MLP_C_CODE:
                card.show(panelSettingsIntervalMLPMode, "panelSettingsIntervalMLPModeCcode");
                break;
            case Const.NEURALNET:
                card.show(panelSettingsIntervalMLPMode, "panelSettingsIntervalMLPModeNeuralnet");
                break;
        }
        
        panelSettingsMLPPackage.repaint();
    }//GEN-LAST:event_comboBoxIntervalMLPModeActionPerformed

    private void comboBoxKNNoptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxKNNoptionsActionPerformed
        CardLayout card = (CardLayout)panelSettingsKNNoptions.getLayout();
        switch (comboBoxKNNoptions.getSelectedItem().toString()) {
            case Const.KNN_FNN:
                card.show(panelSettingsKNNoptions, "panelSettingsKNNoptions_FNN");
                break;
            case Const.KNN_CUSTOM:
                card.show(panelSettingsKNNoptions, "panelSettingsKNNoptions_custom");
                break;
            case Const.KNN_KKNN:
                card.show(panelSettingsKNNoptions, "panelSettingsKNNoptions_kknn");
                break;
        }
        
        panelSettingsKNNoptions.repaint();
    }//GEN-LAST:event_comboBoxKNNoptionsActionPerformed

    private void buttonPlotAddITSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotAddITSActionPerformed
        dialogLBUBCenterRadius = DialogLbUbCenterRadius.getInstance(this, true);
        dialogLBUBCenterRadius.setColnames(DataTableModel.getInstance().getColnames());
        dialogLBUBCenterRadius.setVisible(true);
    }//GEN-LAST:event_buttonPlotAddITSActionPerformed

    private void buttonPlotRemoveITSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotRemoveITSActionPerformed
        List<Object> values = listPlotITSspecs.getSelectedValuesList();
        for (Object val : values) {
            if (val instanceof IntervalNamesCentreRadius) {
                listITSPlotCentreRadius.remove((IntervalNamesCentreRadius) val);
            } else if (val instanceof IntervalNamesLowerUpper) {
                listITSPlotLowerUpper.remove((IntervalNamesLowerUpper) val);
            }
            ((DefaultListModel)(listPlotITSspecs.getModel())).removeElement(val);
        }
    }//GEN-LAST:event_buttonPlotRemoveITSActionPerformed

    private void radioButtonRunFakeIntCenterRadiusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonRunFakeIntCenterRadiusActionPerformed
        if (radioButtonRunFakeIntCenterRadius.isSelected()) {
            groupRunControlsCenterRadius.enableAll();
            groupRunControlsLowerUpper.disableAll();
        }
    }//GEN-LAST:event_radioButtonRunFakeIntCenterRadiusActionPerformed

    private void radioButtonRunFakeIntLowerUpperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonRunFakeIntLowerUpperActionPerformed
        if (radioButtonRunFakeIntLowerUpper.isSelected()) {
            groupRunControlsCenterRadius.disableAll();
            groupRunControlsLowerUpper.enableAll();
        }
    }//GEN-LAST:event_radioButtonRunFakeIntLowerUpperActionPerformed

    private void buttonRunExportErrorMeasuresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRunExportErrorMeasuresActionPerformed
        //TODO export with formatting - the highest, lowest vals highlighted etc.
        JFileChooser fileChooser = new OverwriteFileChooser();
        fileChooser.setSelectedFile(new File("error_measures.xls"));
        if (evt.getSource() == buttonRunExportErrorMeasures) {
            switch (fileChooser.showSaveDialog(this)) {
                case JFileChooser.APPROVE_OPTION:
                    File errorMeasuresFile = fileChooser.getSelectedFile();
                    ExcelWriter.errorJTablesToExcel((ErrorMeasuresTableModel_CTS)(errorMeasuresLatest_CTS.getModel()),
                        (ErrorMeasuresTableModel_ITS)(errorMeasuresLatest_IntTS.getModel()), errorMeasuresFile);
                    break;
                case JFileChooser.CANCEL_OPTION:
                default:
                //nothing
            }
        }

        //a na zaver to disablovat, aby sa na to netukalo furt
        buttonRunExportErrorMeasures.setEnabled(false);
    }//GEN-LAST:event_buttonRunExportErrorMeasuresActionPerformed

    private void buttonTrainAndTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTrainAndTestActionPerformed
        //zazalohovat si aktualny batchTableModel
        AnalysisBatchTableModel lastKnownBatchModel = AnalysisBatchTableModel.getInstance();
        lastKnownBatchModel.setAllLines(AnalysisBatchTableModel.getInstance().getAllLines());
        AnalysisBatchTableModel.getInstance().clear();
        
        //TODO mozno si nahadzat tie checkboxy do ComponentGroup a prechadzat ich len v cykle a nie takto natvrdo?

        if (checkBoxRunMLPnnetar.isSelected()) {
            comboBoxRPackage.setSelectedItem(Const.NNETAR);
            buttonSettingsAddToBatch_MLPActionPerformed(null);
        }

        if (checkBoxRunMLPneuralnet.isSelected()) {
            comboBoxRPackage.setSelectedItem(Const.NEURALNET);
            buttonSettingsAddToBatch_MLPActionPerformed(null);
        }

        if (checkBoxRunMLPnnet.isSelected()) {
            comboBoxRPackage.setSelectedItem(Const.NNET);
            buttonSettingsAddToBatch_MLPActionPerformed(null);
        }

        if (checkBoxRunIntervalMLPCcode.isSelected()) {
            comboBoxIntervalMLPMode.setSelectedItem(Const.INTERVAL_MLP_C_CODE);
            buttonSettingsAddToBatch_intMLPActionPerformed(null);
        }

        if (checkBoxRunMLPintNnetar.isSelected()) {
            comboBoxRPackageMLPint.setSelectedItem(Const.NNETAR);
            buttonSettingsAddToBatch_MLPintActionPerformed(null);
        }
        
        if (checkBoxRunMLPintNnet.isSelected()) {
            comboBoxRPackageMLPint.setSelectedItem(Const.NNET);
            buttonSettingsAddToBatch_MLPintActionPerformed(null);
        }

        if (checkBoxRunARIMA.isSelected()) {
            buttonSettingsAddToBatch_ARIMAActionPerformed(null);
        }

        if (checkBoxRunKNNfnn.isSelected()) {
            comboBoxKNNoptions.setSelectedItem(Const.KNN_FNN);
            buttonSettingsAddToBatch_KNNActionPerformed(null);
        }

//        if (checkBoxRunKNNcustom.isSelected()) {
//            List<KNNcustomParams> params = getParamsKNNcustom();
//            
//            showDialogTooManyModelsInCase(params.size(), Const.KNN_CUSTOM);
//            if (continueWithTooManyModels) {
//                Forecastable kNNcustom = new KNNcustom();
//                for (KNNcustomParams p : params) {
//                    TrainAndTestReportCrisp report = (TrainAndTestReportCrisp) (kNNcustom.forecast(dataTableModel, p));
//                    report.setID(Utils.getModelID());
//                    reportsCTS.add(report);
//                }
//            }
//        }

        if (checkBoxRunKNNkknn.isSelected()) {
            comboBoxKNNoptions.setSelectedItem(Const.KNN_KKNN);
            buttonSettingsAddToBatch_KNNActionPerformed(null);
        }
        
        if (checkBoxRunKNNmyown.isSelected()) {
            comboBoxKNNoptions.setSelectedItem(Const.KNN_MYOWN);
            buttonSettingsAddToBatch_KNNActionPerformed(null);
        }
        
//        if (checkBoxRunVAR.isSelected()) {
//            try {
//                List<VARParams> params = getParamsVAR(comboBoxColnamesRun, panelSettingsVARMainInsideBecauseX);
//
//                showDialogTooManyModelsInCase(params.size(), "VAR");
//                if (continueWithTooManyModels) {
//                    VAR var = new VAR();
//                    for (VARParams p : params) {
//                        List<TrainAndTestReportCrisp> reports = var.forecast(p);
//                        report.setID(Utils.getModelID());
//                        reportsCTS.addAll(reports);
//                    }
//                }
//            } catch (IllegalArgumentException e) {
//                //TODO log alebo nieco
//            }
//        }
        
        if (checkBoxRunRBF.isSelected()) {
            buttonSettingsAddToBatch_RBFActionPerformed(null);
        }
        
        if (checkBoxRunRBFint.isSelected()) {
            buttonSettingsAddToBatch_RBFintActionPerformed(null);
        }
        
        if (checkBoxRunVARint.isSelected()) {
            buttonSettingsAddToBatch_VARintActionPerformed(null);
        }
        
        if (checkBoxRunHybrid.isSelected()) {
            buttonSettingsAddToBatch_HybridActionPerformed(null);
        }
        
        if (checkBoxRunHolt.isSelected()) {
            buttonSettingsAddToBatch_HoltActionPerformed(null);
        }
        
        if (checkBoxRunHoltInt.isSelected()) {
            buttonSettingsAddToBatch_HoltintActionPerformed(null);
        }
        
        if (checkBoxRunIntervalHolt.isSelected()) {
            buttonSettingsAddToBatch_IntervalHoltActionPerformed(null);
        }
        
        if (checkBoxRunSES.isSelected()) {
            buttonSettingsAddToBatch_SESActionPerformed(null);
        }
        
        if (checkBoxRunSESint.isSelected()) {
            buttonSettingsAddToBatch_SESintActionPerformed(null);
        }
        
        if (checkBoxRunMAvg.isSelected()) {
            buttonSettingsAddToBatch_MAvgActionPerformed(null);
        }
        
        if (checkBoxRunHoltWinters.isSelected()) {
            buttonSettingsAddToBatch_HoltWintersActionPerformed(null);
        }
        
        if (checkBoxRunHoltWintersInt.isSelected()) {
            buttonSettingsAddToBatch_HoltWintersIntActionPerformed(null);
        }
        
        if (checkBoxRunBNN.isSelected()) {
            buttonSettingsAddToBatch_BNNActionPerformed(null);
        }
        
        if (checkBoxRunBNNInt.isSelected()) {
            buttonSettingsAddToBatch_BNNintActionPerformed(null);
        }
        
        runModels(false);
        
        //a vratit tam stary batchTableModel
        AnalysisBatchTableModel.getInstance().setAllLines(lastKnownBatchModel.getAllLines());
    }//GEN-LAST:event_buttonTrainAndTestActionPerformed

    private void comboBoxRPackageMLPintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxRPackageMLPintActionPerformed
        CardLayout card = (CardLayout) panelSettingsMLPintPackage.getLayout();
        switch (comboBoxRPackageMLPint.getSelectedItem().toString()) {
            case Const.NNET:
                card.show(panelSettingsMLPintPackage, "panelSettingsMLPintPackage_nnet");
                break;
            case Const.NNETAR:
                card.show(panelSettingsMLPintPackage, "panelSettingsMLPintPackage_nnetar");
        }
        
        panelSettingsMLPintPackage.repaint();
    }//GEN-LAST:event_comboBoxRPackageMLPintActionPerformed

    private void buttonPlotExportPlotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotExportPlotActionPerformed
        JFileChooser fileChooser = new PlotExtensionFileChooser();
        fileChooser.setSelectedFile(new File("plotExport.eps"));
        
        fileChooser.setAcceptAllFileFilterUsed(false); //do not allow "All files"
        fileChooser.addChoosableFileFilter(new FileFilterEps());
        fileChooser.addChoosableFileFilter(new FileFilterPs());
        fileChooser.addChoosableFileFilter(new FileFilterPng());
        fileChooser.addChoosableFileFilter(new FileFilterPdf());
        
        if (evt.getSource() == buttonPlotExportPlot) {
            switch (fileChooser.showSaveDialog(this)) {
                case JFileChooser.APPROVE_OPTION:
                    File plotFile = fileChooser.getSelectedFile();
                    MyRengine rengine = MyRengine.getRengine();

                    String device = "";
                    String ext = "";
                    if (fileChooser.getFileFilter() instanceof RFileFilter) {
                        device = ((RFileFilter)fileChooser.getFileFilter()).getDevice();
                        ext = ((RFileFilter)fileChooser.getFileFilter()).getExtension();
                    }
                    
                    String fileName = plotFile.getPath().replace("\\", "\\\\");
                    if (fileName.contains(".") && (fileName.lastIndexOf('.') < (fileName.length()-1))) {
                        //tipnem si, ze je tam pripona
                        String extCurr = fileName.substring((fileName.lastIndexOf('.')+1), fileName.length()); //vezmem si priponu
                        if (extCurr.equals("eps") || extCurr.equals("ps") || extCurr.equals("png") || extCurr.equals("pdf")) {
                            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
                        } //else to bola nejaka ina cast mena za bodkou
                    }
                    
                    rengine.eval("dev.print(" + device + ", file=\"" + fileName + "." + ext + "\", width=" + panelPlot.getWidth() + ", height=" + panelPlot.getHeight() + ")");
//                    rengine.eval("dev.off()"); //z nejakeho dovodu to "nerefreshuje" nasledujuce ploty, ked to vypnem.
                    //a na zaver to disablovat, aby sa na to netukalo furt
                    buttonPlotExportPlot.setEnabled(false);
                    
                    
                    
                    //a exportuj aj legendu (zatial do samostatneho obrazku):
                    BufferedImage im = new BufferedImage(listPlotLegend.getWidth(), listPlotLegend.getHeight(),
                            BufferedImage.TYPE_INT_ARGB);
                    listPlotLegend.paint(im.getGraphics());
                    try {
                        //for some reason only works with "PNG", so leave it be for now //TODO fix
                        ImageIO.write(im, "PNG", new File(fileName + "-legend.png"));
                    } catch (IOException ex) {
                        //TODO log
                        System.out.println(ex.toString());
                    }
                    break;
                case JFileChooser.CANCEL_OPTION:
                default:
                //nothing
            }
        }
    }//GEN-LAST:event_buttonPlotExportPlotActionPerformed

    private void buttonRunRestoreRangeAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRunRestoreRangeAllActionPerformed
        textFieldRunDataRangeFrom.setText("1");
        textFieldRunDataRangeTo.setText("" + DataTableModel.getInstance().getRowCount());
    }//GEN-LAST:event_buttonRunRestoreRangeAllActionPerformed

    private void buttonPlotRestoreCTSRangeXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotRestoreCTSRangeXActionPerformed
        textFieldPlotRangeCTSXfrom.setText("" + textFieldRunDataRangeFrom.getText());
        int upperBound = Integer.parseInt(textFieldRunDataRangeTo.getText()) + 
                Integer.parseInt(textFieldRunNumForecasts.getText());
        textFieldPlotRangeCTSXto.setText("" + upperBound);
    }//GEN-LAST:event_buttonPlotRestoreCTSRangeXActionPerformed

    private void buttonPlotRestoreCTSRangeYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotRestoreCTSRangeYActionPerformed
        textFieldPlotRangeCTSYfrom.setText("" + PlotStateKeeper.getCrispYmin());
        textFieldPlotRangeCTSYto.setText("" + PlotStateKeeper.getCrispYmax());
    }//GEN-LAST:event_buttonPlotRestoreCTSRangeYActionPerformed

    private void buttonPlotRestoreIntTSRangeXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotRestoreIntTSRangeXActionPerformed
        if ((PlotStateKeeper.getLastCallParams() instanceof CallParamsDrawPlotsITS) &&
                (((CallParamsDrawPlotsITS)PlotStateKeeper.getLastCallParams()).isScatterplot())) {
            //TODO
            textAreaPlotBasicStats.setText("The scatterplot does not support restoring the original range yet.");
        } else {
            textFieldPlotRangeIntTSXfrom.setText("" + textFieldRunDataRangeFrom.getText());
            int upperBound = Integer.parseInt(textFieldRunDataRangeTo.getText()) + 
                    Integer.parseInt(textFieldRunNumForecasts.getText());
            textFieldPlotRangeIntTSXto.setText("" + upperBound);
        }
    }//GEN-LAST:event_buttonPlotRestoreIntTSRangeXActionPerformed

    private void buttonPlotRestoreIntTSRangeYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotRestoreIntTSRangeYActionPerformed
        if ((PlotStateKeeper.getLastCallParams() instanceof CallParamsDrawPlotsITS) &&
                (((CallParamsDrawPlotsITS)PlotStateKeeper.getLastCallParams()).isScatterplot())) {
            //TODO
            textAreaPlotBasicStats.setText("The scatterplot does not support restoring the original range yet.");
        } else {
            textFieldPlotRangeIntTSYfrom.setText("" + PlotStateKeeper.getIntYmin());
            textFieldPlotRangeIntTSYto.setText("" + PlotStateKeeper.getIntYmax());
        }
    }//GEN-LAST:event_buttonPlotRestoreIntTSRangeYActionPerformed

    private void buttonPlotZoomCTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotZoomCTSActionPerformed
        if (PlotStateKeeper.getLastCallParams() instanceof CallParamsDrawPlots) {
            String rangeXCrisp = "range(c(" + textFieldPlotRangeCTSXfrom.getText() + "," + textFieldPlotRangeCTSXto.getText() + "))";
            String rangeYCrisp = "range(c(" + textFieldPlotRangeCTSYfrom.getText() + "," + textFieldPlotRangeCTSYto.getText() + "))";
            String rangeXInt = "range(c(" + PlotStateKeeper.getLastDrawnIntXmin() + "," + PlotStateKeeper.getLastDrawnIntXmax() + "))";
            String rangeYInt = "range(c(" + PlotStateKeeper.getLastDrawnIntYmin() + "," + PlotStateKeeper.getLastDrawnIntYmax() + "))";
                    
            PlotDrawer.drawPlots(Const.MODE_DRAW_ZOOM_ONLY, Const.MODE_REFRESH_NO, 
                    (CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams()), rangeXCrisp , rangeYCrisp, rangeXInt, rangeYInt);
            setPlotRanges(((CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams())).getReportsCTS().size(),
                          ((CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams())).getReportsITS().size());
        } else if (PlotStateKeeper.getLastCallParams() instanceof CallParamsDrawPlotGeneral) {
            String rangeXCrisp = "range(c(" + textFieldPlotRangeCTSXfrom.getText() + "," + textFieldPlotRangeCTSXto.getText() + "))";
            String rangeYCrisp = "range(c(" + textFieldPlotRangeCTSYfrom.getText() + "," + textFieldPlotRangeCTSYto.getText() + "))";
            
            PlotDrawer.drawPlotGeneral(false, (CallParamsDrawPlotGeneral)(PlotStateKeeper.getLastCallParams()), rangeXCrisp, rangeYCrisp);
            setPlotRanges(1, 0); //hack - cokolvek ine ako nula na prvom mieste
        }
    }//GEN-LAST:event_buttonPlotZoomCTSActionPerformed

    private void buttonPlotZoomIntTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotZoomIntTSActionPerformed
        if (PlotStateKeeper.getLastCallParams() instanceof CallParamsDrawPlots) {
            String rangeXCrisp = "range(c(" + PlotStateKeeper.getLastDrawnCrispXmin() + "," + PlotStateKeeper.getLastDrawnCrispXmax() + "))";
            String rangeYCrisp = "range(c(" + PlotStateKeeper.getLastDrawnCrispYmin() + "," + PlotStateKeeper.getLastDrawnCrispYmax() + "))";
            String rangeXInt = "range(c(" + textFieldPlotRangeIntTSXfrom.getText() + "," + textFieldPlotRangeIntTSXto.getText() + "))";
            String rangeYInt = "range(c(" + textFieldPlotRangeIntTSYfrom.getText() + "," + textFieldPlotRangeIntTSYto.getText() + "))";
                    
            PlotDrawer.drawPlots(Const.MODE_DRAW_ZOOM_ONLY, Const.MODE_REFRESH_NO,
                    (CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams()), rangeXCrisp , rangeYCrisp, rangeXInt, rangeYInt);
            setPlotRanges(((CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams())).getReportsCTS().size(),
                          ((CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams())).getReportsITS().size());
        } else if (PlotStateKeeper.getLastCallParams() instanceof CallParamsDrawPlotsITS) {
            String rangeXInt = "range(c(" + textFieldPlotRangeIntTSXfrom.getText() + "," + textFieldPlotRangeIntTSXto.getText() + "))";
            String rangeYInt = "range(c(" + textFieldPlotRangeIntTSYfrom.getText() + "," + textFieldPlotRangeIntTSYto.getText() + "))";
            
            if (((CallParamsDrawPlotsITS)(PlotStateKeeper.getLastCallParams())).isScatterplot()) {
                PlotDrawer.drawScatterPlotsITS(false, (CallParamsDrawPlotsITS)(PlotStateKeeper.getLastCallParams()), rangeXInt, rangeYInt);
            } else {
                PlotDrawer.drawPlotsITS(false, (CallParamsDrawPlotsITS)(PlotStateKeeper.getLastCallParams()), rangeXInt, rangeYInt);
            }
            setPlotRanges(0, 1); //hack, cokolvek ine ako 0 znamena enable
        }
    }//GEN-LAST:event_buttonPlotZoomIntTSActionPerformed

    private void buttonExportForecastValuesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExportForecastValuesActionPerformed
        JFileChooser fileChooser = new OverwriteFileChooser();
        fileChooser.setSelectedFile(new File("forecast_values.xls"));
        if (evt.getSource() == buttonExportForecastValues) {
            switch (fileChooser.showSaveDialog(this)) {
                case JFileChooser.APPROVE_OPTION:
                    File forecastValuesFile = fileChooser.getSelectedFile();
                    ExcelWriter.forecastJTableToExcel((ForecastValsTableModel)(forecastValuesLatest.getModel()), forecastValuesFile);
                    break;
                case JFileChooser.CANCEL_OPTION:
                default:
                //nothing
            }
        }

        //a na zaver to disablovat, aby sa na to netukalo furt
        buttonExportForecastValues.setEnabled(false);
    }//GEN-LAST:event_buttonExportForecastValuesActionPerformed

    private void checkBoxAvgSimpleCTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgSimpleCTSperMActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgSimpleCTSperM);
    }//GEN-LAST:event_checkBoxAvgSimpleCTSperMActionPerformed

    private void checkBoxAvgSimpleCTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgSimpleCTSActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgSimpleCTS);
    }//GEN-LAST:event_checkBoxAvgSimpleCTSActionPerformed

    private void checkBoxAvgSimpleIntTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgSimpleIntTSperMActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgSimpleIntTSperM);
    }//GEN-LAST:event_checkBoxAvgSimpleIntTSperMActionPerformed

    private void checkBoxAvgSimpleIntTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgSimpleIntTSActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgSimpleIntTS);
    }//GEN-LAST:event_checkBoxAvgSimpleIntTSActionPerformed

    private void comboBoxSettingsHybridMethod_centerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxSettingsHybridMethod_centerActionPerformed
        switchHybridToCard("center", comboBoxSettingsHybridMethod_center, panelSettingsHybrid_centerMain);
    }//GEN-LAST:event_comboBoxSettingsHybridMethod_centerActionPerformed

    private void comboBoxSettingsHybridMethod_radiusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxSettingsHybridMethod_radiusActionPerformed
        switchHybridToCard("radius", comboBoxSettingsHybridMethod_radius, panelSettingsHybrid_radiusMain);
    }//GEN-LAST:event_comboBoxSettingsHybridMethod_radiusActionPerformed

    private void switchHybridToCard(String centerOrRadius, JComboBox comboBox, JPanel panel) {
        CardLayout card = (CardLayout)panel.getLayout();
        //TODO neslo by tie panely nazvat tak, aby sa odstranil ten switch? ze uz priamo by tam mali tu konstantu v mene
        switch (comboBox.getSelectedItem().toString()) {
            case Const.NNETAR:
                card.show(panel, "panelSettingsHybrid_" + centerOrRadius + "Main_MLPnnetar");
                break;
            case Const.NNET:
                card.show(panel, "panelSettingsHybrid_" + centerOrRadius + "Main_MLPnnet");
                break;
            case Const.RBF:
                card.show(panel, "panelSettingsHybrid_" + centerOrRadius + "Main_RBF");
                break;
            case Const.ARIMA:
                card.show(panel, "panelSettingsHybrid_" + centerOrRadius + "Main_ARIMA");
                break;
            case Const.KNN_FNN:
                card.show(panel, "panelSettingsHybrid_" + centerOrRadius + "Main_KNNFNN");
                break;
            case Const.KNN_KKNN:
                card.show(panel, "panelSettingsHybrid_" + centerOrRadius + "Main_KNNkknn");
                break;
            case Const.SES:
                card.show(panel, "panelSettingsHybrid_" + centerOrRadius + "Main_SES");
                break;
            case Const.HOLT:
                card.show(panel, "panelSettingsHybrid_" + centerOrRadius + "Main_Holt");
                break;
            case Const.BNN:
                card.show(panel, "panelSettingsHybrid_" + centerOrRadius + "Main_BNN");
                break;
        }
        panel.repaint();
    }
    
    private void checkBoxRunIncludeRMSSEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxRunIncludeRMSSEActionPerformed
        textFieldRunRMSSESeasonality.setEnabled(checkBoxRunIncludeRMSSE.isSelected());
    }//GEN-LAST:event_checkBoxRunIncludeRMSSEActionPerformed

    private void buttonRunAnalysisBatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRunAnalysisBatchActionPerformed
        runModels(true);
    }//GEN-LAST:event_buttonRunAnalysisBatchActionPerformed

    private void buttonSettingsAddToBatch_MLPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_MLPActionPerformed
        //zistit, ktora karta je hore
        switch (comboBoxRPackage.getSelectedItem().toString()) {
            case Const.NNET:
                try {
                    List<NnetParams> paramsNnet = NnetParams.getParamsNnet(panelMLPPercentTrain, comboBoxColnamesRun, panelSettingsMLPPackage_nnet);
                    AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.NNET, paramsNnet, paramsNnet.size()));
                } catch (IllegalArgumentException e) {
                    //TODO
                }
                break;
            case Const.NNETAR:
                try {
                    List<NnetarParams> paramsNnetar = NnetarParams.getParamsNnetar(panelMLPPercentTrain, comboBoxColnamesRun, panelSettingsMLPPackage_nnetar);
                    AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.NNETAR, paramsNnetar, paramsNnetar.size()));
                } catch (IllegalArgumentException e) {
                    //TODO
                }
                break;
            case Const.NEURALNET:
                break;
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_MLPActionPerformed

    private void buttonSettingsAddToBatch_MLPintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_MLPintActionPerformed
        switch (comboBoxRPackageMLPint.getSelectedItem().toString()) {
            case Const.NNET:
                try {
                    List<MLPintNnetParams> paramsNnet = MLPintNnetParams.getParamsMLPintNnet(panelMLPintPercentTrain, 
                            comboBoxRunFakeIntCenter, 
                        panelSettingsMLPintPackage_nnet_center, panelMLPintPercentTrain, comboBoxRunFakeIntRadius, 
                        panelSettingsMLPintPackage_nnet_radius, panelMLPintSettingsDistance, textFieldNumNetsToTrainMLPint,
                        panelBestModelCriterionMLPint);
                    AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.MLP_INT_NNET, paramsNnet, 
                            paramsNnet.size()*(paramsNnet.get(0).getNumNetsToTrain()))); //TODO mozno brat to numNetsToTrain nejak bezpecnejsie?
                } catch (IllegalArgumentException e) {
                    //TODO
                }
                break;
            case Const.NNETAR:
                try {
                    List<MLPintNnetarParams> paramsNnetar = MLPintNnetarParams.getParamsMLPintNnetar(panelMLPintPercentTrain, 
                            comboBoxRunFakeIntCenter, 
                        panelSettingsMLPintPackage_nnetar_center, panelMLPintPercentTrain, comboBoxRunFakeIntRadius, 
                        panelSettingsMLPintPackage_nnetar_radius, panelMLPintSettingsDistance, textFieldNumNetsToTrainMLPint,
                        panelBestModelCriterionMLPint);
                    AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.MLP_INT_NNETAR, paramsNnetar,
                            paramsNnetar.size()*(paramsNnetar.get(0).getNumNetsToTrain())));
                } catch (IllegalArgumentException e) {
                    //TODO
                }
                break;
            case Const.NEURALNET:
                break;
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_MLPintActionPerformed

    private void buttonSettingsAddToBatch_intMLPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_intMLPActionPerformed
        switch (comboBoxIntervalMLPMode.getSelectedItem().toString()) {
            case Const.INTERVAL_MLP_C_CODE:
                try {
                    List<IntervalMLPCcodeParams> paramsIMLP = IntervalMLPCcodeParams.getParamsIntervalMLPCcode(panelIntMLPPercentTrain, panelSettingsIntervalMLPModeCcode);
                    AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.INTERVAL_MLP_C_CODE, paramsIMLP,
                            paramsIMLP.size()*(paramsIMLP.get(0).getNumNetworks())));
                } catch (IllegalArgumentException e) {
                    //TODO
                }
                break;
            case Const.NEURALNET:
                break;
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_intMLPActionPerformed

    private void buttonSettingsAddToBatch_RBFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_RBFActionPerformed
        try {
            List<RBFParams> paramsRBF = RBFParams.getParamsRBF(panelRBFPercentTrain, comboBoxColnamesRun, panelSettingsRBFMain);
            AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.RBF, paramsRBF, paramsRBF.size()));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_RBFActionPerformed

    private void buttonSettingsAddToBatch_RBFintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_RBFintActionPerformed
        try {
            List<RBFintParams> paramsRBFint = RBFintParams.getParamsRBFint(panelRBFintPercentTrain, comboBoxRunFakeIntCenter, 
                panelSettingsRBFint_center, panelRBFintPercentTrain, comboBoxRunFakeIntRadius, 
                panelSettingsRBFint_radius, panelRBFintSettingsDistance, textFieldNumNetworksToTrainRBFint,
                panelBestModelCriterionRBFint);
            AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.RBF_INT, paramsRBFint, paramsRBFint.size()*(paramsRBFint.get(0).getNumNetsToTrain())));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_RBFintActionPerformed

    private void buttonSettingsAddToBatch_ARIMAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_ARIMAActionPerformed
        try {
            List<ArimaParams> paramsArima = ArimaParams.getParamsArima(panelARIMAPercTrain, comboBoxColnamesRun, panelSettingsARIMAMain);
            AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.ARIMA, paramsArima, paramsArima.size()));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_ARIMAActionPerformed

    private void buttonSettingsAddToBatch_KNNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_KNNActionPerformed
        switch (comboBoxKNNoptions.getSelectedItem().toString()) {
            case Const.KNN_FNN:
                try {
                    List<KNNfnnParams> paramsFNN = KNNfnnParams.getParamsKNNfnn(panelKNNPercTrain, comboBoxColnamesRun, panelSettingsKNNoptions_FNN);
                    AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.KNN_FNN, paramsFNN, paramsFNN.size()));
                } catch (IllegalArgumentException e) {
                    //TODO
                }
                break;
            case Const.KNN_KKNN:
                try {
                    List<KNNkknnParams> paramsKKNN = KNNkknnParams.getParamsKNNkknn(panelKNNPercTrain, comboBoxColnamesRun, panelSettingsKNNoptions_kknn);
                    AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.KNN_KKNN, paramsKKNN, paramsKKNN.size()));
                } catch (IllegalArgumentException e) {
                    //TODO
                }
                break;
            case Const.KNN_CUSTOM:
                break;
            case Const.KNN_MYOWN:
                try {
                    List<KNNmyownParams> paramsMyOwn = KNNmyownParams.getParamsKNNmyown(panelKNNPercTrain, comboBoxColnamesRun, panelSettingsKNNoptions_myown);
                    AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.KNN_MYOWN, paramsMyOwn, paramsMyOwn.size()));
                } catch (IllegalArgumentException e) {
                    //TODO
                }
                break;
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_KNNActionPerformed

    private void buttonSettingsAddToBatch_VARintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_VARintActionPerformed
        try {
            List<VARintParams> paramsVARint = VARintParams.getParamsVARint(panelVARintPercentTrain, panelVARintDistance, panelVARintInsideBecause);
            AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.VAR_INT, paramsVARint, paramsVARint.size()));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_VARintActionPerformed

    private void buttonSettingsAddToBatch_SESActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_SESActionPerformed
        try {
            List<SESParams> paramsSES = SESParams.getParamsSES(panelSESpercentTrain, comboBoxColnamesRun, panelSESmain);
            AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.SES, paramsSES, paramsSES.size()));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_SESActionPerformed

    private void buttonSettingsAddToBatch_SESintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_SESintActionPerformed
        try {
            List<SESintParams> paramsSESint = SESintParams.getParamsSESint(panelSESintPercentTrain, panelSESint_center, 
                        comboBoxRunFakeIntCenter, panelSESintPercentTrain, panelSESint_radius, comboBoxRunFakeIntRadius,
                        panelSESintDistance);
            AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.SES_INT, paramsSESint, paramsSESint.size()));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_SESintActionPerformed

    private void buttonSettingsAddToBatch_HoltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_HoltActionPerformed
        try {
            List<HoltParams> paramsHolt = HoltParams.getParamsHolt(panelHoltPercentTrain, panelHoltInside, comboBoxColnamesRun);
            AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.HOLT, paramsHolt, paramsHolt.size()));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_HoltActionPerformed

    private void buttonSettingsAddToBatch_HoltintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_HoltintActionPerformed
        try {
            List<HoltIntParams> paramsHoltInt = HoltIntParams.getParamsHoltInt(panelHoltIntPercentTrain, panelHoltInt_center, 
                        comboBoxRunFakeIntCenter, panelHoltIntPercentTrain, panelHoltInt_radius, comboBoxRunFakeIntRadius,
                        panelHoltIntDistance);
            AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.HOLT_INT, paramsHoltInt, paramsHoltInt.size()));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_HoltintActionPerformed

    private void buttonSettingsAddToBatch_IntervalHoltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_IntervalHoltActionPerformed
        try {
            List<IntervalHoltParams> paramsIntervalHolt = IntervalHoltParams.getParamsIntervalHolt(panelIntervalHoltPercentTrain, comboBoxRunFakeIntCenter,
                        comboBoxRunFakeIntRadius, panelIntervalHoltDistance, panelIntervalHoltMain);
            AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.INTERVAL_HOLT, paramsIntervalHolt, paramsIntervalHolt.size()));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_IntervalHoltActionPerformed

    private void buttonSettingsAddToBatch_HoltWintersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_HoltWintersActionPerformed
        try {
            List<HoltWintersParams> paramsHoltWinters = HoltWintersParams.getParamsHoltWinters(panelHoltWintersPercentTrain, 
                        panelHoltWintersInside, comboBoxColnamesRun);
            AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.HOLT_WINTERS, paramsHoltWinters, paramsHoltWinters.size()));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_HoltWintersActionPerformed

    private void buttonSettingsAddToBatch_HoltWintersIntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_HoltWintersIntActionPerformed
        try {
            List<HoltWintersIntParams> paramsHoltWintersInt = HoltWintersIntParams.getParamsHoltWintersInt(panelHoltWintersIntPercentTrain, 
                        panelHoltWintersInt_center, comboBoxRunFakeIntCenter, panelHoltWintersIntPercentTrain, 
                        panelHoltWintersInt_radius, comboBoxRunFakeIntRadius, panelHoltWintersIntDistance);
            AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.HOLT_WINTERS_INT, paramsHoltWintersInt, paramsHoltWintersInt.size()));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_HoltWintersIntActionPerformed

    private void buttonSettingsAddToBatch_HybridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_HybridActionPerformed
        try {
            List<HybridParams> paramsHybrid = getParamsHybrid();
            AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.HYBRID, paramsHybrid, paramsHybrid.size()));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_HybridActionPerformed

    private void buttonAnalysisBatchRemoveSelectedRowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAnalysisBatchRemoveSelectedRowsActionPerformed
        ((AnalysisBatchTableModel)(tableAnalysisBatch.getModel())).removeRows(tableAnalysisBatch.getSelectedRows());
    }//GEN-LAST:event_buttonAnalysisBatchRemoveSelectedRowsActionPerformed

    private void buttonLegendSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLegendSelectAllActionPerformed
        selectAllOrNone(Const.ALL);
    }//GEN-LAST:event_buttonLegendSelectAllActionPerformed

    private void buttonLegendSelectNoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLegendSelectNoneActionPerformed
        selectAllOrNone(Const.NONE);
    }//GEN-LAST:event_buttonLegendSelectNoneActionPerformed

    private void selectAllOrNone(String selectWhat) {
        if (listPlotLegend.getCellRenderer() instanceof PlotLegendTurnOFFableListCellRenderer) {
            //fuuuj, to je hnusny sposob zistovania, ci to je ta legenda :/ TODO prerobit
            DefaultListModel model = (DefaultListModel)listPlotLegend.getModel();
            
            switch (selectWhat) {
                case Const.NONE:
                    for (int i = 0; i < model.size(); i++) {
                        Plottable p = ((PlotLegendTurnOFFableListElement)model.getElementAt(i)).getReport();
                        if ((p instanceof TrainAndTestReport) && (! ((TrainAndTestReport)p).isAverage())) {
                            p.setVisible(false);
                        }
                    }   break;
                case Const.ALL:
                    for (int i = 0; i < model.size(); i++) {
                        ((PlotLegendTurnOFFableListElement)model.getElementAt(i)).getReport().setVisible(true);
                }   break;
            }
            
            //to iste ako v buttonLegenSelectAll a v drawLegend - mouseListener. TODO refactor
            listPlotLegend.repaint();
            //and then redraw the plots:
            String rangeXCrisp = "range(c(" + PlotStateKeeper.getLastDrawnCrispXmin() + "," + PlotStateKeeper.getLastDrawnCrispXmax() + "))";
            String rangeYCrisp = "range(c(" + PlotStateKeeper.getLastDrawnCrispYmin() + "," + PlotStateKeeper.getLastDrawnCrispYmax() + "))";
            String rangeXInt = "range(c(" + PlotStateKeeper.getLastDrawnIntXmin() + "," + PlotStateKeeper.getLastDrawnIntXmax() + "))";
            String rangeYInt = "range(c(" + PlotStateKeeper.getLastDrawnIntYmin() + "," + PlotStateKeeper.getLastDrawnIntYmax() + "))";
            PlotDrawer.drawPlots(Const.MODE_DRAW_NEW, Const.MODE_REFRESH_ONLY, (CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams()), 
                    rangeXCrisp , rangeYCrisp, rangeXInt, rangeYInt);
        } //else nereaguj
    }
    
    private void buttonRunShowHiddenErrorMeasuresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRunShowHiddenErrorMeasuresActionPerformed
        ((ErrorMeasuresTableModel_CTS)errorMeasuresLatest_CTS.getModel()).showAllHiddenRows();
        ((ErrorMeasuresTableModel_ITS)errorMeasuresLatest_IntTS.getModel()).showAllHiddenRows();
    }//GEN-LAST:event_buttonRunShowHiddenErrorMeasuresActionPerformed

    private void buttonForecastValsShowHiddenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonForecastValsShowHiddenActionPerformed
        ((ForecastValsTableModel)forecastValuesLatest.getModel()).showAllHiddenColumns();
    }//GEN-LAST:event_buttonForecastValsShowHiddenActionPerformed

    private void buttonForecastValsHideNoForecastsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonForecastValsHideNoForecastsActionPerformed
        ((ForecastValsTableModel)forecastValuesLatest.getModel()).hideNoForecasts();
    }//GEN-LAST:event_buttonForecastValsHideNoForecastsActionPerformed

    private void buttonForecastValsHideAllButAvgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonForecastValsHideAllButAvgActionPerformed
        ((ForecastValsTableModel)forecastValuesLatest.getModel()).hideAllButAvg();
    }//GEN-LAST:event_buttonForecastValsHideAllButAvgActionPerformed

    private void buttonPlotAllITSScatterplotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotAllITSScatterplotActionPerformed
        //tu uz len vezmi nasyslene v tych listoch
        PlotDrawer.drawScatterPlotsITS(true, new CallParamsDrawPlotsITS(listPlotLegend, gdBufferedPanelPlot, panelPlot.getWidth(), 
                panelPlot.getHeight(), DataTableModel.getInstance(), listITSPlotCentreRadius, listITSPlotLowerUpper, true));
        buttonPlotExportPlot.setEnabled(true);
        setPlotRanges(0, 1);
        
        panelEverything.setSelectedComponent(panelPlotImage);
    }//GEN-LAST:event_buttonPlotAllITSScatterplotActionPerformed

    private void buttonHideAllErrorsExceptAvgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonHideAllErrorsExceptAvgActionPerformed
        ((ErrorMeasuresTableModel_CTS)errorMeasuresLatest_CTS.getModel()).hideAllButAvg();
        ((ErrorMeasuresTableModel_ITS)errorMeasuresLatest_IntTS.getModel()).hideAllButAvg();
    }//GEN-LAST:event_buttonHideAllErrorsExceptAvgActionPerformed

    private void checkBoxAvgMDeCTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMDeCTSperMActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgMDeCTSperM);
    }//GEN-LAST:event_checkBoxAvgMDeCTSperMActionPerformed
    
    private void checkBoxAvgMDeIntTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMDeIntTSperMActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgMDeIntTSperM);
    }//GEN-LAST:event_checkBoxAvgMDeIntTSperMActionPerformed

    private void checkBoxAvgMDeCTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMDeCTSActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgMDeCTS);
    }//GEN-LAST:event_checkBoxAvgMDeCTSActionPerformed

    private void checkBoxAvgMDeIntTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMDeIntTSActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgMDeIntTS);
    }//GEN-LAST:event_checkBoxAvgMDeIntTSActionPerformed

    private void checkBoxAvgTheilsuCTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgTheilsuCTSperMActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgTheilsuCTSperM);
    }//GEN-LAST:event_checkBoxAvgTheilsuCTSperMActionPerformed

    private void checkBoxAvgTheilsuIntTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgTheilsuIntTSperMActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgTheilsuIntTSperM);
    }//GEN-LAST:event_checkBoxAvgTheilsuIntTSperMActionPerformed

    private void checkBoxAvgTheilsuCTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgTheilsuCTSActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgTheilsuCTS);
    }//GEN-LAST:event_checkBoxAvgTheilsuCTSActionPerformed

    private void checkBoxAvgTheilsuIntTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgTheilsuIntTSActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgTheilsuIntTS);
    }//GEN-LAST:event_checkBoxAvgTheilsuIntTSActionPerformed

    private void checkBoxAvgCvgEffIntTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgCvgEffIntTSperMActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgCvgEffIntTSperM);
    }//GEN-LAST:event_checkBoxAvgCvgEffIntTSperMActionPerformed
    
    private void checkBoxAvgCvgEffIntTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgCvgEffIntTSActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgCvgEffIntTS);
    }//GEN-LAST:event_checkBoxAvgCvgEffIntTSActionPerformed

    private void checkBoxAvgCenterLogRadiusIntTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgCenterLogRadiusIntTSperMActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgCenterLogRadiusIntTSperM);
    }//GEN-LAST:event_checkBoxAvgCenterLogRadiusIntTSperMActionPerformed

    private void checkBoxAvgCenterLogRadiusIntTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgCenterLogRadiusIntTSActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgCenterLogRadiusIntTS);
    }//GEN-LAST:event_checkBoxAvgCenterLogRadiusIntTSActionPerformed

    private void checkBoxAvgGotSelected(JCheckBox checkBox) {
        if (checkBox.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }
    
    private void buttonPlotAllITSScatterplotMatrixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotAllITSScatterplotMatrixActionPerformed
        buttonPlotAllITSScatterplot.doClick(); //hack. for some reason does not draw the matrix without setting up the
                                               //   plot with drawing sth else first.
        ((DefaultListModel)listPlotLegend.getModel()).clear(); //a second hack to clear the legend after the scatterplot
        
        //tu uz len vezmi nasyslene v tych listoch
        PlotDrawer.drawScatterPlotMatrixITS(true, new CallParamsDrawPlotsITS(listPlotLegend, gdBufferedPanelPlot, panelPlot.getWidth(), 
                panelPlot.getHeight(), DataTableModel.getInstance(), listITSPlotCentreRadius, listITSPlotLowerUpper, true));
        buttonPlotExportPlot.setEnabled(true);
        setPlotRanges(0, 0);
        
        panelEverything.setSelectedComponent(panelPlotImage);
    }//GEN-LAST:event_buttonPlotAllITSScatterplotMatrixActionPerformed

    private void checkBoxAvgMedianCTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMedianCTSperMActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgMedianCTSperM);
    }//GEN-LAST:event_checkBoxAvgMedianCTSperMActionPerformed

    private void checkBoxAvgMedianCTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMedianCTSActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgMedianCTS);
    }//GEN-LAST:event_checkBoxAvgMedianCTSActionPerformed

    private void checkBoxAvgMedianIntTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMedianIntTSperMActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgMedianIntTSperM);
    }//GEN-LAST:event_checkBoxAvgMedianIntTSperMActionPerformed

    private void checkBoxAvgMedianIntTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMedianIntTSActionPerformed
        checkBoxAvgGotSelected(checkBoxAvgMedianIntTS);
    }//GEN-LAST:event_checkBoxAvgMedianIntTSActionPerformed

    private void buttonBoxplotsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBoxplotsActionPerformed
        PlotDrawer.drawSimpleFctionToGrid("boxplot", listColnames.getSelectedValuesList(), DataTableModel.getInstance(), tabbedPaneAnalysisPlotsCTS);
        setPlotRanges(0, 0);
        buttonExportAnalysisPlotsCTS.setEnabled(true);
    }//GEN-LAST:event_buttonBoxplotsActionPerformed

    private void buttonHistogramsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonHistogramsActionPerformed
        PlotDrawer.drawSimpleFctionToGrid("hist", listColnames.getSelectedValuesList(), DataTableModel.getInstance(), tabbedPaneAnalysisPlotsCTS);
        setPlotRanges(0, 0);
        buttonExportAnalysisPlotsCTS.setEnabled(true);
    }//GEN-LAST:event_buttonHistogramsActionPerformed

    private void buttonPlotResidualsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotResidualsActionPerformed
        //TODO mozno refaktor a vyhodit do PlotDrawera - aby tam bolo vsetko kreslenie grafov
        //TODO refaktor, ptz je to to iste ako drawPlotGeneral/3, len s inymi objektami trosku
        
        int[] selectedCols = residualsTableLatest.getSelectedColumns();
        
        List<BasicStats> basicStats = PlotDrawer.drawPlotsResiduals(
                ((ResidualsTableModel)residualsTableLatest.getModel()).getDataForSelectedCols(selectedCols), 
                listPlotResidualsLegend, gdBufferedPanelPlotResiduals, 
                panelResidualsPlot.getWidth(), panelResidualsPlot.getHeight(), "plot.ts");
//        buttonPlotExportPlotResiduals.setEnabled(true);
        
        //mean, standard deviation, median
        StringBuilder basicStatsString = new StringBuilder();
        for (BasicStats stat : basicStats) {
            basicStatsString.append(stat.toString());
            basicStatsString.append(System.lineSeparator());
        }
        textAreaResidualsBasicStats.setText(basicStatsString.toString());
    }//GEN-LAST:event_buttonPlotResidualsActionPerformed

    private void buttonNormProbPlotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNormProbPlotActionPerformed
        PlotDrawer.drawSimpleFctionToGrid("qqnorm", listColnames.getSelectedValuesList(), DataTableModel.getInstance(), tabbedPaneAnalysisPlotsCTS);
        setPlotRanges(0, 0);
        buttonExportAnalysisPlotsCTS.setEnabled(true);
    }//GEN-LAST:event_buttonNormProbPlotActionPerformed

    private void buttonNormalityTestsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNormalityTestsActionPerformed
        textAreaTests.setText(StatisticalTests.normalProbabilityTests(listColnamesTests.getSelectedValuesList()));

        //and show the normal probability plots:
        PlotDrawer.drawSimpleFctionToGrid("qqnorm", listColnamesTests.getSelectedValuesList(), DataTableModel.getInstance(), tabbedPaneAnalysisPlotsTests);
        setPlotRanges(0, 0);
    }//GEN-LAST:event_buttonNormalityTestsActionPerformed

    private void buttonStationarityTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStationarityTestActionPerformed
        textAreaTests.setText(StatisticalTests.stationarityTests(listColnamesTests.getSelectedValuesList()));
    }//GEN-LAST:event_buttonStationarityTestActionPerformed

    private void buttonDiffSeriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDiffSeriesActionPerformed
        Transformations.difference(listColnamesTransform.getSelectedValuesList());
        fillGUIelementsWithNewData();
    }//GEN-LAST:event_buttonDiffSeriesActionPerformed

    private void buttonLogTransformSeriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLogTransformSeriesActionPerformed
        Transformations.logTransform(listColnamesTransform.getSelectedValuesList());
        fillGUIelementsWithNewData();
    }//GEN-LAST:event_buttonLogTransformSeriesActionPerformed

    private void buttonRemoveTrendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveTrendActionPerformed
        Transformations.removeTrend(listColnamesTransform.getSelectedValuesList());
        fillGUIelementsWithNewData();
    }//GEN-LAST:event_buttonRemoveTrendActionPerformed

    private void buttonExportResidualsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExportResidualsActionPerformed
        //TODO refactor - je to to iste ako export forecasts len s premenovanymi premennymi
        JFileChooser fileChooser = new OverwriteFileChooser();
        fileChooser.setSelectedFile(new File("residuals.xls"));
        if (evt.getSource() == buttonExportResiduals) {
            switch (fileChooser.showSaveDialog(this)) {
                case JFileChooser.APPROVE_OPTION:
                    File residualsFile = fileChooser.getSelectedFile();
                    ExcelWriter.residualsJTableToExcel((ResidualsTableModel)(residualsTableLatest.getModel()), residualsFile);
                    break;
                case JFileChooser.CANCEL_OPTION:
                default:
                //nothing
            }
        }

        //a na zaver to disablovat, aby sa na to netukalo furt
        buttonExportResiduals.setEnabled(false);
    }//GEN-LAST:event_buttonExportResidualsActionPerformed

    private void buttonStructBreaksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStructBreaksActionPerformed
        int breaks = 5;
        try {
            breaks = Integer.parseInt(textFieldMaxStructBreaks.getText());
        } catch (NumberFormatException e) {
            //TODO log
        }
        
        List<String> plots = StatisticalTests.stucturalBreaksTests(listColnamesTests.getSelectedValuesList(), 
                listPlotITSspecs.getSelectedValuesList(), breaks); //bacha, posledny prvok je to info
        //TODO toto potom vyriesit lepsie (uz na urovni tej metody, ktora to zle vracia
        String structBreaksInfo = plots.get(plots.size() - 1);
        plots.remove(plots.size() - 1);
        
        //TODO refactor odtialto------------------
        //potom ich nechaj vyplut do mriezky
        List<JGDBufferedPanel> panels = PlotDrawer.drawToGrid(tabbedPaneAnalysisPlotsTests.getWidth(), 
                tabbedPaneAnalysisPlotsTests.getHeight(), plots, 1, 1);
        
        //a tu mriezku nakresli
        tabbedPaneAnalysisPlotsTests.removeAll();
        int i = 0;
        for (JGDBufferedPanel p : panels) {
            tabbedPaneAnalysisPlotsTests.addTab("Page "+(++i), p);
        }

        tabbedPaneAnalysisPlotsTests.repaint();
        //---------------potialto je to rovnake aj inde. vybrat do samostatnej metody
        
        //a vypis info
        textAreaTests.setText(structBreaksInfo);

        setPlotRanges(0, 0);
        buttonExportTestsPlots.setEnabled(true);
    }//GEN-LAST:event_buttonStructBreaksActionPerformed

    private void buttonExportAnalysisPlotsCTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExportAnalysisPlotsCTSActionPerformed
        //idealne by mohlo v buducnosti exportovat vsetky zobrazene taby, ale tam je problem s existujucimi subormi
        //TODO vymysliet
        JFileChooser fileChooser = new PlotExtensionFileChooser();
        fileChooser.setSelectedFile(new File("plotExport.eps"));
        
        fileChooser.setAcceptAllFileFilterUsed(false); //do not allow "All files"
        fileChooser.addChoosableFileFilter(new FileFilterEps());
        fileChooser.addChoosableFileFilter(new FileFilterPs());
        fileChooser.addChoosableFileFilter(new FileFilterPng());
        fileChooser.addChoosableFileFilter(new FileFilterPdf());
        
        //TODO aj toto sa opakuje, refactor out
        if (evt.getSource() == buttonExportAnalysisPlotsCTS) {
            switch (fileChooser.showSaveDialog(this)) {
                case JFileChooser.APPROVE_OPTION:
                    File plotFile = fileChooser.getSelectedFile();
                    MyRengine rengine = MyRengine.getRengine();

                    String device = "";
                    String ext = "";
                    if (fileChooser.getFileFilter() instanceof RFileFilter) {
                        device = ((RFileFilter)fileChooser.getFileFilter()).getDevice();
                        ext = ((RFileFilter)fileChooser.getFileFilter()).getExtension();
                    }
                    
                    String fileName = plotFile.getPath().replace("\\", "\\\\");
                    if (fileName.contains(".") && (fileName.lastIndexOf('.') < (fileName.length()-1))) {
                        //tipnem si, ze je tam pripona
                        String extCurr = fileName.substring((fileName.lastIndexOf('.')+1), fileName.length()); //vezmem si priponu
                        if (extCurr.equals("eps") || extCurr.equals("ps") || extCurr.equals("png") || extCurr.equals("pdf")) {
                            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
                        } //else to bola nejaka ina cast mena za bodkou
                    }
                    
                    rengine.eval("dev.print(" + device + ", file=\"" + fileName + "." + ext + "\", width=" + panelPlot.getWidth() + ", height=" + panelPlot.getHeight() + ")");
//                    rengine.eval("dev.off()"); //z nejakeho dovodu to "nerefreshuje" nasledujuce ploty, ked to vypnem.
                    break;
                case JFileChooser.CANCEL_OPTION:
                default:
                //nothing
            }
        }
    }//GEN-LAST:event_buttonExportAnalysisPlotsCTSActionPerformed

    private void buttonExportAnalysisTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExportAnalysisTextActionPerformed
        exportTextArea(evt, buttonExportAnalysisText, textAreaPlotBasicStats);
    }//GEN-LAST:event_buttonExportAnalysisTextActionPerformed

    private void buttonAggregateToITSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAggregateToITSActionPerformed
        final int length = Integer.parseInt(textFieldAggregateToITSevery.getText());
        Transformations.aggregateToITS(listColnamesTransform.getSelectedValuesList(), length);
        fillGUIelementsWithNewData();
    }//GEN-LAST:event_buttonAggregateToITSActionPerformed

    private void buttonBinomPropComputePosteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBinomPropComputePosteriorActionPerformed
        List<BinomPropParams> params;
        try {
            params = BinomPropParams.getParamsBinomProp(panelBinomPropSettings);
        } catch (IllegalArgumentException e) {
            return;
        }
        
        List<String> plots = BinomProp.binomPropComputePosterior(params);
        //TODO odstranit tento hnusny hack
        String info = plots.get(plots.size() - 1);
        plots.remove(plots.size() - 1);
        
        //draw plots into the right panel
        PlotDrawer.drawBayesToGrid(plots, tabbedPaneBinomPropPlot);
        
        textAreaBinomPropInfo.setText(info);
    }//GEN-LAST:event_buttonBinomPropComputePosteriorActionPerformed

    private void buttonBinomPropSimulateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBinomPropSimulateActionPerformed
        List<BinomPropParams> params;
        try {
            params = BinomPropParams.getParamsBinomProp(panelBinomPropSettings);
            if (textFieldBinomPropPercProbInterval.getText().isEmpty()) {
                textAreaBinomPropInfo.setText("<no prediction interval specified>");
                return;
            }
        } catch (IllegalArgumentException e) {
            return;
        }
        
        List<Double> alphasssRaw = FieldsParser.parseDoubles(textFieldBinomPropPercProbInterval);
        
        textAreaBinomPropInfo.setText(BinomProp.binomPropSimulate(params, alphasssRaw));
    }//GEN-LAST:event_buttonBinomPropSimulateActionPerformed

    private void buttonBinomPropPredictActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBinomPropPredictActionPerformed
        List<BinomPropParams> params;
        try {
            params = BinomPropParams.getParamsBinomProp(panelBinomPropSettings);
            //TODO zlepsit?
            if (textFieldBinomPropNumFutureObs.getText().isEmpty()) {
                textAreaBinomPropInfo.setText("<no number of future observations specified>");
                return;
            }
        } catch (IllegalArgumentException e) {
            return;
        }
        
        List<Integer> numFutureObsss = FieldsParser.parseIntegers(textFieldBinomPropNumFutureObs);
        
        List<String> plots = BinomProp.binomPropPredict(params, numFutureObsss);
        
        PlotDrawer.drawBayesToGrid(plots, tabbedPaneBinomPropPlot);
    }//GEN-LAST:event_buttonBinomPropPredictActionPerformed

    private void buttonSettingsAddToBatch_BNNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_BNNActionPerformed
        try {
            List<BNNParams> paramsBNN = BNNParams.getParamsBNN(panelBNNPercentTrain, comboBoxColnamesRun, panelSettingsBNNinside);
            AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.BNN, paramsBNN, paramsBNN.size()));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_BNNActionPerformed

    private void buttonSettingsAddToBatch_BNNintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_BNNintActionPerformed
        try {
            List<BNNintParams> paramsBNNint = BNNintParams.getParamsBNNint(panelBNNintPercentTrain, comboBoxRunFakeIntCenter,
                panelSettingsBNNint_center, comboBoxRunFakeIntRadius, panelSettingsBNNint_radius,
                panelBNNintSettingsDistance, textFieldNumNetworksToTrainBNNint,
                panelBestModelCriterionBNNint);
            AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.BNN_INT, paramsBNNint, paramsBNNint.size()*(paramsBNNint.get(0).getNumNetsToTrain())));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_BNNintActionPerformed

    private void buttonConvertITSLBUBCRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonConvertITSLBUBCRActionPerformed
        DialogConvertLbUbCenterRadius dialogConvertLBUBCenterRadius = DialogConvertLbUbCenterRadius.getInstance(this, true);
        dialogConvertLBUBCenterRadius.setColnames(DataTableModel.getInstance().getColnames());
        dialogConvertLBUBCenterRadius.setVisible(true);
        
        fillGUIelementsWithNewData();
    }//GEN-LAST:event_buttonConvertITSLBUBCRActionPerformed

    private void buttonSettingsAddToBatch_MAvgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_MAvgActionPerformed
        try {
            List<MAvgParams> paramsMAvg = MAvgParams.getParamsMAvg(comboBoxColnamesRun, panelMAvgMain);
            AnalysisBatchTableModel.getInstance().addLine(new AnalysisBatchLine(Const.MAvg, paramsMAvg, paramsMAvg.size()));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_MAvgActionPerformed

    private void buttonExportTextAreaTestsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExportTextAreaTestsActionPerformed
        exportTextArea(evt, buttonExportTextAreaTests, textAreaTests);
    }//GEN-LAST:event_buttonExportTextAreaTestsActionPerformed

    private void buttonExportTestsPlotsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExportTestsPlotsActionPerformed
        //idealne by mohlo v buducnosti exportovat vsetky zobrazene taby, ale tam je problem s existujucimi subormi
        //TODO vymysliet
        
        JFileChooser fileChooser = new PlotExtensionFileChooser();
        fileChooser.setSelectedFile(new File("plotExport.eps"));
        
        fileChooser.setAcceptAllFileFilterUsed(false); //do not allow "All files"
        fileChooser.addChoosableFileFilter(new FileFilterEps());
        fileChooser.addChoosableFileFilter(new FileFilterPs());
        fileChooser.addChoosableFileFilter(new FileFilterPng());
        fileChooser.addChoosableFileFilter(new FileFilterPdf());
        
        if (evt.getSource() == buttonExportTestsPlots) {
            switch (fileChooser.showSaveDialog(this)) {
                case JFileChooser.APPROVE_OPTION:
                    File plotFile = fileChooser.getSelectedFile();
                    MyRengine rengine = MyRengine.getRengine();

                    String device = "";
                    String ext = "";
                    if (fileChooser.getFileFilter() instanceof RFileFilter) {
                        device = ((RFileFilter)fileChooser.getFileFilter()).getDevice();
                        ext = ((RFileFilter)fileChooser.getFileFilter()).getExtension();
                    }
                    
                    String fileName = plotFile.getPath().replace("\\", "\\\\");
                    if (fileName.contains(".") && (fileName.lastIndexOf('.') < (fileName.length()-1))) {
                        //tipnem si, ze je tam pripona
                        String extCurr = fileName.substring((fileName.lastIndexOf('.')+1), fileName.length()); //vezmem si priponu
                        if (extCurr.equals("eps") || extCurr.equals("ps") || extCurr.equals("png") || extCurr.equals("pdf")) {
                            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
                        } //else to bola nejaka ina cast mena za bodkou
                    }
                    
                    rengine.eval("dev.print(" + device + ", file=\"" + fileName + "." + ext + "\", width=" + panelPlot.getWidth() + ", height=" + panelPlot.getHeight() + ")");
//                    rengine.eval("dev.off()"); //z nejakeho dovodu to "nerefreshuje" nasledujuce ploty, ked to vypnem.
                    break;
                case JFileChooser.CANCEL_OPTION:
                default:
                //nothing
            }
        }
    }//GEN-LAST:event_buttonExportTestsPlotsActionPerformed

    private void buttonBasicStatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBasicStatsActionPerformed
        textAreaPlotBasicStats.setText(AnalysisUtils.getBasicStats(listColnames.getSelectedValuesList()));
    }//GEN-LAST:event_buttonBasicStatsActionPerformed

    private void buttonNormalizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNormalizeActionPerformed
        Transformations.normalize(listColnamesTransform.getSelectedValuesList());
        fillGUIelementsWithNewData();
    }//GEN-LAST:event_buttonNormalizeActionPerformed

    private void buttonCTSclearSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCTSclearSelectionActionPerformed
        listColnames.setSelectedIndices(new int[]{});
    }//GEN-LAST:event_buttonCTSclearSelectionActionPerformed

    private void buttonITSclearSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonITSclearSelectionActionPerformed
        listPlotITSspecs.setSelectedIndices(new int[]{});
    }//GEN-LAST:event_buttonITSclearSelectionActionPerformed
    
    private void maybeTurnOffPlotAvgONLY() {
        if (groupCheckBoxesAvg_withoutAvgONLY.areEnabled() == ComponentGroup.ENABLED.NONE) {
            checkBoxAvgONLY.setSelected(false);
            checkBoxAvgONLY.setEnabled(false);
        }
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonACF;
    private javax.swing.JButton buttonAggregateToITS;
    private javax.swing.JButton buttonAnalysisBatchRemoveSelectedRows;
    private javax.swing.JButton buttonBasicStats;
    private javax.swing.JButton buttonBinomPropComputePosterior;
    private javax.swing.JButton buttonBinomPropPredict;
    private javax.swing.JButton buttonBinomPropSimulate;
    private javax.swing.JButton buttonBoxplots;
    private javax.swing.JButton buttonCTSclearSelection;
    private javax.swing.JButton buttonConvertITSLBUBCR;
    private javax.swing.JButton buttonConvertITSLBUBCR1;
    private javax.swing.JButton buttonDiffSeries;
    private javax.swing.JButton buttonExportAnalysisPlotsCTS;
    private javax.swing.JButton buttonExportAnalysisText;
    private javax.swing.JButton buttonExportDiagramsNN;
    private javax.swing.JButton buttonExportForecastValues;
    private javax.swing.JButton buttonExportPredictionIntervals;
    private javax.swing.JButton buttonExportResiduals;
    private javax.swing.JButton buttonExportTestsPlots;
    private javax.swing.JButton buttonExportTextAreaTests;
    private javax.swing.JButton buttonForecastValsHideAllButAvg;
    private javax.swing.JButton buttonForecastValsHideNoForecasts;
    private javax.swing.JButton buttonForecastValsShowHidden;
    private javax.swing.ButtonGroup buttonGroup_runFakeIntCRLBUB;
    private javax.swing.JButton buttonHideAllErrorsExceptAvg;
    private javax.swing.JButton buttonHistograms;
    private javax.swing.JButton buttonITSclearSelection;
    private javax.swing.JButton buttonLegendSelectAll;
    private javax.swing.JButton buttonLegendSelectNone;
    private javax.swing.JButton buttonLogTransformSeries;
    private javax.swing.JButton buttonNormProbPlot;
    private javax.swing.JButton buttonNormalityTests;
    private javax.swing.JButton buttonNormalize;
    private javax.swing.JButton buttonPACF;
    private javax.swing.JButton buttonPlotAddITS;
    private javax.swing.JButton buttonPlotAllITSScatterplot;
    private javax.swing.JButton buttonPlotAllITSScatterplotMatrix;
    private javax.swing.JButton buttonPlotColname;
    private javax.swing.JButton buttonPlotExportPlot;
    private javax.swing.JButton buttonPlotRemoveITS;
    private javax.swing.JButton buttonPlotResiduals;
    private javax.swing.JButton buttonPlotRestoreCTSRangeX;
    private javax.swing.JButton buttonPlotRestoreCTSRangeY;
    private javax.swing.JButton buttonPlotRestoreIntTSRangeX;
    private javax.swing.JButton buttonPlotRestoreIntTSRangeY;
    private javax.swing.JButton buttonPlotSelectedITS;
    private javax.swing.JButton buttonPlotZoomCTS;
    private javax.swing.JButton buttonPlotZoomIntTS;
    private javax.swing.JButton buttonRemoveTrend;
    private javax.swing.JButton buttonRunAnalysisBatch;
    private javax.swing.JButton buttonRunExportErrorMeasures;
    private javax.swing.JButton buttonRunRestoreRangeAll;
    private javax.swing.JButton buttonRunShowHiddenErrorMeasures;
    private javax.swing.JButton buttonSettingsAddToBatch_ARIMA;
    private javax.swing.JButton buttonSettingsAddToBatch_BNN;
    private javax.swing.JButton buttonSettingsAddToBatch_BNNint;
    private javax.swing.JButton buttonSettingsAddToBatch_Holt;
    private javax.swing.JButton buttonSettingsAddToBatch_HoltWinters;
    private javax.swing.JButton buttonSettingsAddToBatch_HoltWintersInt;
    private javax.swing.JButton buttonSettingsAddToBatch_Holtint;
    private javax.swing.JButton buttonSettingsAddToBatch_Hybrid;
    private javax.swing.JButton buttonSettingsAddToBatch_IntervalHolt;
    private javax.swing.JButton buttonSettingsAddToBatch_KNN;
    private javax.swing.JButton buttonSettingsAddToBatch_MAvg;
    private javax.swing.JButton buttonSettingsAddToBatch_MLP;
    private javax.swing.JButton buttonSettingsAddToBatch_MLPint;
    private javax.swing.JButton buttonSettingsAddToBatch_RBF;
    private javax.swing.JButton buttonSettingsAddToBatch_RBFint;
    private javax.swing.JButton buttonSettingsAddToBatch_SES;
    private javax.swing.JButton buttonSettingsAddToBatch_SESint;
    private javax.swing.JButton buttonSettingsAddToBatch_VARint;
    private javax.swing.JButton buttonSettingsAddToBatch_intMLP;
    private javax.swing.JButton buttonStationarityTest;
    private javax.swing.JButton buttonStructBreaks;
    private javax.swing.JButton buttonTrainAndTest;
    private javax.swing.JCheckBox checkBoxAvgCenterLogRadiusIntTS;
    private javax.swing.JCheckBox checkBoxAvgCenterLogRadiusIntTSperM;
    private javax.swing.JCheckBox checkBoxAvgCvgEffIntTS;
    private javax.swing.JCheckBox checkBoxAvgCvgEffIntTSperM;
    private javax.swing.JCheckBox checkBoxAvgMDeCTS;
    private javax.swing.JCheckBox checkBoxAvgMDeCTSperM;
    private javax.swing.JCheckBox checkBoxAvgMDeIntTS;
    private javax.swing.JCheckBox checkBoxAvgMDeIntTSperM;
    private javax.swing.JCheckBox checkBoxAvgMedianCTS;
    private javax.swing.JCheckBox checkBoxAvgMedianCTSperM;
    private javax.swing.JCheckBox checkBoxAvgMedianIntTS;
    private javax.swing.JCheckBox checkBoxAvgMedianIntTSperM;
    private javax.swing.JCheckBox checkBoxAvgONLY;
    private javax.swing.JCheckBox checkBoxAvgSimpleCTS;
    private javax.swing.JCheckBox checkBoxAvgSimpleCTSperM;
    private javax.swing.JCheckBox checkBoxAvgSimpleIntTS;
    private javax.swing.JCheckBox checkBoxAvgSimpleIntTSperM;
    private javax.swing.JCheckBox checkBoxAvgTheilsuCTS;
    private javax.swing.JCheckBox checkBoxAvgTheilsuCTSperM;
    private javax.swing.JCheckBox checkBoxAvgTheilsuIntTS;
    private javax.swing.JCheckBox checkBoxAvgTheilsuIntTSperM;
    private javax.swing.JCheckBox checkBoxRunARIMA;
    private javax.swing.JCheckBox checkBoxRunBNN;
    private javax.swing.JCheckBox checkBoxRunBNNInt;
    private javax.swing.JCheckBox checkBoxRunHolt;
    private javax.swing.JCheckBox checkBoxRunHoltInt;
    private javax.swing.JCheckBox checkBoxRunHoltWinters;
    private javax.swing.JCheckBox checkBoxRunHoltWintersInt;
    private javax.swing.JCheckBox checkBoxRunHybrid;
    private javax.swing.JCheckBox checkBoxRunIncludeRMSSE;
    private javax.swing.JCheckBox checkBoxRunIntervalHolt;
    private javax.swing.JCheckBox checkBoxRunIntervalMLPCcode;
    private javax.swing.JCheckBox checkBoxRunIntervalMLPneuralnet;
    private javax.swing.JCheckBox checkBoxRunIntervalRandomWalk;
    private javax.swing.JCheckBox checkBoxRunKNNcustom;
    private javax.swing.JCheckBox checkBoxRunKNNfnn;
    private javax.swing.JCheckBox checkBoxRunKNNinterval;
    private javax.swing.JCheckBox checkBoxRunKNNkknn;
    private javax.swing.JCheckBox checkBoxRunKNNmyown;
    private javax.swing.JCheckBox checkBoxRunMAvg;
    private javax.swing.JCheckBox checkBoxRunMLPintNnet;
    private javax.swing.JCheckBox checkBoxRunMLPintNnetar;
    private javax.swing.JCheckBox checkBoxRunMLPneuralnet;
    private javax.swing.JCheckBox checkBoxRunMLPnnet;
    private javax.swing.JCheckBox checkBoxRunMLPnnetar;
    private javax.swing.JCheckBox checkBoxRunRBF;
    private javax.swing.JCheckBox checkBoxRunRBFint;
    private javax.swing.JCheckBox checkBoxRunRandomWalkCTS;
    private javax.swing.JCheckBox checkBoxRunSES;
    private javax.swing.JCheckBox checkBoxRunSESint;
    private javax.swing.JCheckBox checkBoxRunVAR;
    private javax.swing.JCheckBox checkBoxRunVARint;
    private javax.swing.JComboBox comboBoxColnamesRun;
    private javax.swing.JComboBox comboBoxIntervalMLPMode;
    private javax.swing.JComboBox comboBoxKNNoptions;
    private javax.swing.JComboBox comboBoxRPackage;
    private javax.swing.JComboBox comboBoxRPackageMLPint;
    private javax.swing.JComboBox comboBoxRunFakeIntCenter;
    private javax.swing.JComboBox comboBoxRunFakeIntLower;
    private javax.swing.JComboBox comboBoxRunFakeIntRadius;
    private javax.swing.JComboBox comboBoxRunFakeIntUpper;
    private javax.swing.JComboBox comboBoxSettingsHybridMethod_center;
    private javax.swing.JComboBox comboBoxSettingsHybridMethod_radius;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel153;
    private javax.swing.JLabel jLabel154;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel160;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelRPkg;
    private javax.swing.JLabel jLabelRPkg1;
    private javax.swing.JLabel jLabelTrainingInfo;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTable jTableData;
    private javax.swing.JLabel labelRunFakeIntCenter;
    private javax.swing.JLabel labelRunFakeIntLower;
    private javax.swing.JLabel labelRunFakeIntRadius;
    private javax.swing.JLabel labelRunFakeIntUpper;
    private javax.swing.JList listColnames;
    private javax.swing.JList listColnamesTests;
    private javax.swing.JList listColnamesTransform;
    private javax.swing.JList listPlotITSspecs;
    private javax.swing.JList listPlotLegend;
    private javax.swing.JList listPlotResidualsLegend;
    private javax.swing.JMenuBar menuBarMain;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem menuFileExit;
    private javax.swing.JMenuItem menuFileLoad;
    private javax.swing.JTabbedPane paneSettingsMethods;
    private javax.swing.JPanel paneSettingsMethodsARIMA;
    private javax.swing.JPanel paneSettingsMethodsBNN;
    private javax.swing.JPanel paneSettingsMethodsBNNint;
    private javax.swing.JPanel paneSettingsMethodsHybrid;
    private javax.swing.JPanel paneSettingsMethodsIntervalMLP;
    private javax.swing.JPanel paneSettingsMethodsKNN;
    private javax.swing.JPanel paneSettingsMethodsMLP;
    private javax.swing.JPanel paneSettingsMethodsMLPint;
    private javax.swing.JPanel paneSettingsMethodsRBF;
    private javax.swing.JPanel paneSettingsMethodsRBFint;
    private javax.swing.JPanel panelARIMAPercTrain;
    private javax.swing.JPanel panelAnalysisBatch;
    private javax.swing.JPanel panelAnalysisSettings;
    private javax.swing.JPanel panelBNNPercentTrain;
    private javax.swing.JPanel panelBNNintPercentTrain;
    private javax.swing.JPanel panelBNNintSettingsDistance;
    private javax.swing.JPanel panelBayesianAll;
    private javax.swing.JTabbedPane panelBayesianSettings;
    private javax.swing.JPanel panelBestModelCriterionBNNint;
    private javax.swing.JPanel panelBestModelCriterionMLPint;
    private javax.swing.JPanel panelBestModelCriterionRBFint;
    private javax.swing.JScrollPane panelBinomPropInfo;
    private javax.swing.JPanel panelBinomPropPlot;
    private javax.swing.JPanel panelBinomPropSettings;
    private javax.swing.JPanel panelCTS;
    private javax.swing.JPanel panelCombinationWeights;
    private javax.swing.JPanel panelCombinationWeightsAll;
    private javax.swing.JPanel panelData;
    private javax.swing.JPanel panelDiagramsNNs;
    private javax.swing.JPanel panelDiagramsNNsInside;
    private javax.swing.JPanel panelErrorMeasures;
    private javax.swing.JPanel panelErrorMeasuresAll;
    private javax.swing.JTabbedPane panelEverything;
    private javax.swing.JPanel panelForecastVals;
    private javax.swing.JPanel panelForecastValsAll;
    private javax.swing.JPanel panelHoltInside;
    private javax.swing.JPanel panelHoltIntDistance;
    private javax.swing.JPanel panelHoltIntPercentTrain;
    private javax.swing.JPanel panelHoltInt_center;
    private javax.swing.JPanel panelHoltInt_radius;
    private javax.swing.JPanel panelHoltPercentTrain;
    private javax.swing.JPanel panelHoltWintersInside;
    private javax.swing.JPanel panelHoltWintersIntDistance;
    private javax.swing.JPanel panelHoltWintersIntPercentTrain;
    private javax.swing.JPanel panelHoltWintersInt_center;
    private javax.swing.JPanel panelHoltWintersInt_radius;
    private javax.swing.JPanel panelHoltWintersPercentTrain;
    private javax.swing.JPanel panelIntMLPPercentTrain;
    private javax.swing.JPanel panelIntTS;
    private javax.swing.JPanel panelIntervalHoltDistance;
    private javax.swing.JPanel panelIntervalHoltMain;
    private javax.swing.JPanel panelIntervalHoltPercentTrain;
    private javax.swing.JPanel panelKNNPercTrain;
    private javax.swing.JPanel panelMAvgMain;
    private javax.swing.JPanel panelMLPPercentTrain;
    private javax.swing.JPanel panelMLPintPercentTrain;
    private javax.swing.JPanel panelMLPintSettingsDistance;
    private javax.swing.JPanel panelModelDescriptionsAll;
    private javax.swing.JPanel panelPlot;
    private javax.swing.JPanel panelPlotImage;
    private javax.swing.JPanel panelPredictionIntervals;
    private javax.swing.JPanel panelPredictionIntervalsAll;
    private javax.swing.JPanel panelRBFPercentTrain;
    private javax.swing.JPanel panelRBFintPercentTrain;
    private javax.swing.JPanel panelRBFintSettingsDistance;
    private javax.swing.JPanel panelResiduals;
    private javax.swing.JPanel panelResidualsAll;
    private javax.swing.JPanel panelResidualsLeft;
    private javax.swing.JPanel panelResidualsPlot;
    private javax.swing.JPanel panelResidualsRightButtons;
    private javax.swing.JPanel panelRunOutside;
    private javax.swing.JPanel panelSESintDistance;
    private javax.swing.JPanel panelSESintPercentTrain;
    private javax.swing.JPanel panelSESint_center;
    private javax.swing.JPanel panelSESint_radius;
    private javax.swing.JPanel panelSESmain;
    private javax.swing.JPanel panelSESpercentTrain;
    private javax.swing.JPanel panelSettingsARIMAMain;
    private javax.swing.JPanel panelSettingsBNNinside;
    private javax.swing.JPanel panelSettingsBNNint_center;
    private javax.swing.JPanel panelSettingsBNNint_radius;
    private javax.swing.JPanel panelSettingsHybridDistance;
    private javax.swing.JPanel panelSettingsHybridPercentTrain;
    private javax.swing.JPanel panelSettingsHybrid_centerMain;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_ARIMA;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_BNN;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_Holt;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_KNNFNN;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_KNNkknn;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_MLPnnet;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_MLPnnetar;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_RBF;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_SES;
    private javax.swing.JPanel panelSettingsHybrid_radiusMain;
    private javax.swing.JPanel panelSettingsHybrid_radiusMain_ARIMA;
    private javax.swing.JPanel panelSettingsHybrid_radiusMain_BNN;
    private javax.swing.JPanel panelSettingsHybrid_radiusMain_Holt;
    private javax.swing.JPanel panelSettingsHybrid_radiusMain_KNNFNN;
    private javax.swing.JPanel panelSettingsHybrid_radiusMain_KNNkknn;
    private javax.swing.JPanel panelSettingsHybrid_radiusMain_MLPnnet;
    private javax.swing.JPanel panelSettingsHybrid_radiusMain_MLPnnetar;
    private javax.swing.JPanel panelSettingsHybrid_radiusMain_RBF;
    private javax.swing.JPanel panelSettingsHybrid_radiusMain_SES;
    private javax.swing.JPanel panelSettingsIntervalMLPMode;
    private javax.swing.JPanel panelSettingsIntervalMLPModeCcode;
    private javax.swing.JPanel panelSettingsIntervalMLPModeNeuralnet;
    private javax.swing.JPanel panelSettingsKNNoptions;
    private javax.swing.JPanel panelSettingsKNNoptions_FNN;
    private javax.swing.JPanel panelSettingsKNNoptions_custom;
    private javax.swing.JPanel panelSettingsKNNoptions_kknn;
    private javax.swing.JPanel panelSettingsKNNoptions_myown;
    private javax.swing.JPanel panelSettingsMLPPackage;
    private javax.swing.JPanel panelSettingsMLPPackage_neuralnet;
    private javax.swing.JPanel panelSettingsMLPPackage_nnet;
    private javax.swing.JPanel panelSettingsMLPPackage_nnetar;
    private javax.swing.JPanel panelSettingsMLPintPackage;
    private javax.swing.JPanel panelSettingsMLPintPackage_nnet;
    private javax.swing.JPanel panelSettingsMLPintPackage_nnet_center;
    private javax.swing.JPanel panelSettingsMLPintPackage_nnet_radius;
    private javax.swing.JPanel panelSettingsMLPintPackage_nnetar;
    private javax.swing.JPanel panelSettingsMLPintPackage_nnetar_center;
    private javax.swing.JPanel panelSettingsMLPintPackage_nnetar_radius;
    private javax.swing.JPanel panelSettingsMethodsHolt;
    private javax.swing.JPanel panelSettingsMethodsHoltInt;
    private javax.swing.JPanel panelSettingsMethodsHoltWinters;
    private javax.swing.JPanel panelSettingsMethodsHoltWintersInt;
    private javax.swing.JPanel panelSettingsMethodsIntervalHolt;
    private javax.swing.JPanel panelSettingsMethodsMAvg;
    private javax.swing.JPanel panelSettingsMethodsSES;
    private javax.swing.JPanel panelSettingsMethodsSESint;
    private javax.swing.JPanel panelSettingsMethodsVARint;
    private javax.swing.JPanel panelSettingsRBFMain;
    private javax.swing.JPanel panelSettingsRBFint_center;
    private javax.swing.JPanel panelSettingsRBFint_radius;
    private javax.swing.JPanel panelTestsOutside;
    private javax.swing.JPanel panelTransform;
    private javax.swing.JPanel panelVARintDistance;
    private javax.swing.JPanel panelVARintInside;
    private javax.swing.JPanel panelVARintInsideBecause;
    private javax.swing.JPanel panelVARintPercentTrain;
    private javax.swing.JRadioButton radioButtonRunFakeIntCenterRadius;
    private javax.swing.JRadioButton radioButtonRunFakeIntLowerUpper;
    private javax.swing.JScrollPane scrollPaneAnalysisBatchInside;
    private javax.swing.JScrollPane scrollPaneData;
    private javax.swing.JScrollPane scrollPaneForecastVals;
    private javax.swing.JScrollPane scrollPaneListPlotLegend;
    private javax.swing.JScrollPane scrollPanePredictionIntervals;
    private javax.swing.JScrollPane scrollPaneResiduals;
    private javax.swing.JScrollPane scrollPane_panelSettingsHybrid_centerMain_MLPnnet;
    private javax.swing.JScrollPane scrollPane_panelSettingsHybrid_radiusMain_MLPnnet;
    private javax.swing.JScrollPane scrollPane_panelSettingsMLPPackage_nnet;
    private javax.swing.JScrollPane scrollPane_panelSettingsMLPintPackage_nnet_center;
    private javax.swing.JScrollPane scrollPane_panelSettingsMLPintPackage_nnet_radius;
    private javax.swing.JTabbedPane tabbedPaneAnalysisPlotsCTS;
    private javax.swing.JTabbedPane tabbedPaneAnalysisPlotsTests;
    private javax.swing.JTabbedPane tabbedPaneBinomPropPlot;
    private javax.swing.JTabbedPane tabbedPaneDiagramsNNs;
    private javax.swing.JTable tableAnalysisBatch;
    private javax.swing.JTextArea textAreaBinomPropInfo;
    private javax.swing.JTextArea textAreaModelsInfo;
    private javax.swing.JTextArea textAreaPlotBasicStats;
    private javax.swing.JTextArea textAreaResidualsBasicStats;
    private javax.swing.JTextArea textAreaTests;
    private javax.swing.JTextField textFieldAggregateToITSevery;
    private javax.swing.JTextField textFieldBinomPropNumFutureObs;
    private javax.swing.JTextField textFieldBinomPropPercProbInterval;
    private javax.swing.JTextField textFieldMaxStructBreaks;
    private javax.swing.JTextField textFieldNumNetsToTrainMLPint;
    private javax.swing.JTextField textFieldNumNetworksToTrainBNNint;
    private javax.swing.JTextField textFieldNumNetworksToTrainRBFint;
    private javax.swing.JTextField textFieldPlotRangeCTSXfrom;
    private javax.swing.JTextField textFieldPlotRangeCTSXto;
    private javax.swing.JTextField textFieldPlotRangeCTSYfrom;
    private javax.swing.JTextField textFieldPlotRangeCTSYto;
    private javax.swing.JTextField textFieldPlotRangeIntTSXfrom;
    private javax.swing.JTextField textFieldPlotRangeIntTSXto;
    private javax.swing.JTextField textFieldPlotRangeIntTSYfrom;
    private javax.swing.JTextField textFieldPlotRangeIntTSYto;
    private javax.swing.JTextField textFieldRunDataRangeFrom;
    private javax.swing.JTextField textFieldRunDataRangeTo;
    private javax.swing.JTextField textFieldRunNumForecasts;
    private javax.swing.JTextField textFieldRunRMSSESeasonality;
    // End of variables declaration//GEN-END:variables

    private File loadedFile;
    public static JGDBufferedPanel drawNowToThisGDBufferedPanel;
    private static JGDBufferedPanel gdBufferedPanelPlot;
    private static JGDBufferedPanel gdBufferedPanelPlotResiduals;
    private DialogLbUbCenterRadius dialogLBUBCenterRadius;
    private JTable errorMeasuresLatest_CTS = new JTable(new ErrorMeasuresTableModel_CTS(new ArrayList<>()));
    private JTable errorMeasuresLatest_IntTS = new JTable(new ErrorMeasuresTableModel_ITS((new ArrayList<>())));
    private JTable forecastValuesLatest;
    private JTable residualsTableLatest;
    private List<IntervalNamesCentreRadius> listITSPlotCentreRadius = new ArrayList<>();
    private List<IntervalNamesLowerUpper> listITSPlotLowerUpper = new ArrayList<>();
    private boolean continueWithTooManyModels = true;
    
    public void setContinueWithTooManyModels(boolean continueWithTooManyModels) {
        this.continueWithTooManyModels = continueWithTooManyModels; //TODO toto tiez zavana skaredym dizajnom, prerobit
    }

    public void drawPlotGeneral(boolean drawNew, String plotFunction, String additionalArgs) {
        //TODO mozno refaktor a vyhodit do PlotDrawera - aby tam bolo vsetko kreslenie grafov
        //String colname = comboBoxColnames.getSelectedItem().toString();
        List<String> colnames = listColnames.getSelectedValuesList();
        
        List<DefaultPlottable> plottables = new ArrayList<>();
        for (String col : colnames) {
            DefaultPlottable p = new DefaultPlottable(col, null, col);
            plottables.add(p);
        }
        
        drawPlotGeneral(drawNew, plotFunction, additionalArgs, plottables);
        
        //mean, standard deviation, median
        textAreaPlotBasicStats.setText(AnalysisUtils.getBasicStats(colnames));
    }
    
    public void drawPlotGeneral(boolean drawNew, String plotFunction, String additionalArgs, List<DefaultPlottable> plottables) {
        //TODO mozno refaktor a vyhodit do PlotDrawera - aby tam bolo vsetko kreslenie grafov
        //String colname = comboBoxColnames.getSelectedItem().toString();
        
        //TODO refactor? - tie basicStats by sa nemuseli ani prepocitavat, ak sa len prefarbuje
        PlotDrawer.drawPlotGeneral(drawNew, new CallParamsDrawPlotGeneral(listPlotLegend, 
                gdBufferedPanelPlot, panelPlot.getWidth(), panelPlot.getHeight(), plottables, plotFunction, additionalArgs));
        buttonPlotExportPlot.setEnabled(true);
    }
    
    public <T extends Params> void setParamsGeneral(Class<T> classss, List<T> resultList) {
        SettingsPanel.setSomethingList(classss, resultList, "setNumForecasts", 
                Integer.class, FieldsParser.parseIntegers(textFieldRunNumForecasts).subList(0, 1)); //multiple vals not supported; will work with the first
        SettingsPanel.setSomethingList(classss, resultList, "setDataRangeFrom",
                Integer.class, FieldsParser.parseIntegers(textFieldRunDataRangeFrom).subList(0, 1)); //multiple vals not supported; will work with the first
        SettingsPanel.setSomethingList(classss, resultList, "setDataRangeTo",
                Integer.class, FieldsParser.parseIntegers(textFieldRunDataRangeTo).subList(0, 1)); //multiple vals not supported; will work with the first
        Integer seasonality = 0;
        if (checkBoxRunIncludeRMSSE.isSelected()) {
            seasonality = Integer.parseInt(textFieldRunRMSSESeasonality.getText());
        }
        SettingsPanel.setSomethingOneValue(classss, resultList, "setSeasonality",
                Integer.class, seasonality);
    }
    
    private List<HybridParams> getParamsHybrid() throws IllegalArgumentException {
        HybridParams par = new HybridParams();
        List<HybridParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        switch (comboBoxSettingsHybridMethod_center.getSelectedItem().toString()) {
            case Const.NNETAR:
                List<NnetarParams> resultListCenterNnetar = NnetarParams.getParamsNnetar(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntCenter, panelSettingsHybrid_centerMain_MLPnnetar);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        NnetarParams.class, resultListCenterNnetar);
                break;
            case Const.NNET:
                List<NnetParams> resultListCenterNnet = NnetParams.getParamsNnet(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntCenter, panelSettingsHybrid_centerMain_MLPnnet);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        NnetParams.class, resultListCenterNnet);
                break;
            case Const.RBF:
                List<RBFParams> resultListCenterRBF = RBFParams.getParamsRBF(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntCenter, panelSettingsHybrid_centerMain_RBF);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        RBFParams.class, resultListCenterRBF);
                break;
            case Const.ARIMA:
                List<ArimaParams> resultListCenterARIMA = ArimaParams.getParamsArima(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntCenter, panelSettingsHybrid_centerMain_ARIMA);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        ArimaParams.class, resultListCenterARIMA);
                break;
            case Const.KNN_FNN:
                List<KNNfnnParams> resultListCenterKNNFNN = KNNfnnParams.getParamsKNNfnn(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntCenter, panelSettingsHybrid_centerMain_KNNFNN);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        KNNfnnParams.class, resultListCenterKNNFNN);
                break;
            case Const.KNN_KKNN:
                List<KNNkknnParams> resultListCenterKNNkknn = KNNkknnParams.getParamsKNNkknn(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntCenter, panelSettingsHybrid_centerMain_KNNkknn);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        KNNkknnParams.class, resultListCenterKNNkknn);
                break;
            case Const.SES:
                List<SESParams> resultListCenterSES = SESParams.getParamsSES(panelSettingsHybridPercentTrain, 
                        comboBoxRunFakeIntCenter, panelSettingsHybrid_centerMain_SES);
                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        SESParams.class, resultListCenterSES);
                break;
            case Const.HOLT:
                List<HoltParams> resultListCenterHolt = HoltParams.getParamsHolt(panelSettingsHybridPercentTrain, 
                        panelSettingsHybrid_centerMain_Holt, comboBoxRunFakeIntCenter);
                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        HoltParams.class, resultListCenterHolt);
                break;
            case Const.BNN:
                List<BNNParams> resultListCenterBNN = BNNParams.getParamsBNN(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntCenter, panelSettingsHybrid_centerMain_BNN);
                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        BNNParams.class, resultListCenterBNN);
                break;
            default:
                //do not do anything, this should never happen. maybe throw an exception? 
                break;
        }
        
        
        
        switch (comboBoxSettingsHybridMethod_radius.getSelectedItem().toString()) {
            case Const.NNETAR:
                List<NnetarParams> resultListRadiusNnetar = NnetarParams.getParamsNnetar(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntRadius, panelSettingsHybrid_radiusMain_MLPnnetar);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        NnetarParams.class, resultListRadiusNnetar);
                break;
            case Const.NNET:
                List<NnetParams> resultListRadiusNnet = NnetParams.getParamsNnet(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntRadius, panelSettingsHybrid_radiusMain_MLPnnet);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        NnetParams.class, resultListRadiusNnet);
                break;
            case Const.RBF:
                List<RBFParams> resultListRadiusRBF = RBFParams.getParamsRBF(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntRadius, panelSettingsHybrid_radiusMain_RBF);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        RBFParams.class, resultListRadiusRBF);
                break;
            case Const.ARIMA:
                List<ArimaParams> resultListRadiusARIMA = ArimaParams.getParamsArima(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntRadius, panelSettingsHybrid_radiusMain_ARIMA);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        ArimaParams.class, resultListRadiusARIMA);
                break;
            case Const.KNN_FNN:
                List<KNNfnnParams> resultListRadiusKNNFNN = KNNfnnParams.getParamsKNNfnn(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntRadius, panelSettingsHybrid_radiusMain_KNNFNN);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        KNNfnnParams.class, resultListRadiusKNNFNN);
                break;
            case Const.KNN_KKNN:
                List<KNNkknnParams> resultListRadiusKNNkknn = KNNkknnParams.getParamsKNNkknn(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntRadius, panelSettingsHybrid_radiusMain_KNNkknn);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        KNNkknnParams.class, resultListRadiusKNNkknn);
                break;
            case Const.SES:
                List<SESParams> resultListRadiusSES = SESParams.getParamsSES(panelSettingsHybridPercentTrain, 
                        comboBoxRunFakeIntRadius, panelSettingsHybrid_radiusMain_SES);
                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        SESParams.class, resultListRadiusSES);
                break;
            case Const.HOLT:
                List<HoltParams> resultListRadiusHolt = HoltParams.getParamsHolt(panelSettingsHybridPercentTrain, 
                        panelSettingsHybrid_radiusMain_Holt, comboBoxRunFakeIntRadius);
                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        HoltParams.class, resultListRadiusHolt);
                break;
            case Const.BNN:
                List<BNNParams> resultListRadiusBNN = BNNParams.getParamsBNN(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntRadius, panelSettingsHybrid_radiusMain_BNN);
                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        BNNParams.class, resultListRadiusBNN);
                break;
            default:
                //do not do anything, this should never happen. maybe throw an exception? 
                break;
        }
        
        ((DistanceSettingsPanel)panelSettingsHybridDistance).setSpecificParams(HybridParams.class, resultList);
        SettingsPanel.setSomethingList(HybridParams.class, resultList, "setNumNetsToTrain",
                Integer.class, FieldsParser.parseIntegers(textFieldNumNetworksToTrainRBFint));
        
        return resultList;
    }
    
    public void addPlotITS_CentreRadius(IntervalNamesCentreRadius interval) {
        listITSPlotCentreRadius.add(interval);
        ((DefaultListModel)(listPlotITSspecs.getModel())).addElement(interval);
    }
    
    public void addPlotITS_LowerUpper(IntervalNamesLowerUpper interval) {
        listITSPlotLowerUpper.add(interval);
        ((DefaultListModel)(listPlotITSspecs.getModel())).addElement(interval);
    }
    
    private void drawTablesErrorMeasures(List<TrainAndTestReportCrisp> rCTS,
                                         List<TrainAndTestReportInterval> rIntTS,
                                         List<TrainAndTestReport> addedReports) {
        panelErrorMeasures.removeAll();
        JTabbedPane tabbedPaneTablesErrors = new JTabbedPane(JTabbedPane.TOP);
        tabbedPaneTablesErrors.setSize(panelErrorMeasures.getWidth(), panelErrorMeasures.getHeight());
        
        List<TrainAndTestReport> reportsCTS = new ArrayList<>();
        reportsCTS.addAll(rCTS);
        List<TrainAndTestReport> reportsIntTS = new ArrayList<>();
        reportsIntTS.addAll(rIntTS);
        for (TrainAndTestReport r : addedReports) {
            if (r instanceof TrainAndTestReportCrisp) {
                reportsCTS.add((TrainAndTestReportCrisp)r);
            } else if (r instanceof TrainAndTestReportInterval) {
                reportsIntTS.add((TrainAndTestReportInterval)r);
            }
        }
        
        if ((! reportsCTS.isEmpty()) && (! reportsIntTS.isEmpty())) { //kresli obe
            final JTable errorMeasuresTable_CTS = new JTable();
            errorMeasuresTable_CTS.setModel(new ErrorMeasuresTableModel_CTS(reportsCTS));
            errorMeasuresTable_CTS.setDefaultRenderer(Object.class, new ErrorTableCellRenderer());
            errorMeasuresTable_CTS.setTableHeader(null);
            errorMeasuresTable_CTS.setVisible(true);

            JScrollPane scrollPaneErrorMeasuresCTS = new JScrollPane(errorMeasuresTable_CTS);
            tabbedPaneTablesErrors.addTab("CTS", scrollPaneErrorMeasuresCTS);
            errorMeasuresTable_CTS.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        //pozor, -1! pretoze 
                        int selectedRow = errorMeasuresTable_CTS.getSelectedRow();
                        ((ErrorMeasuresTableModel_CTS)errorMeasuresTable_CTS.getModel()).hideRow(selectedRow);
                    }
                }
                
                @Override
                public void mousePressed(MouseEvent e) { }
                @Override
                public void mouseReleased(MouseEvent e) { }
                @Override
                public void mouseEntered(MouseEvent e) { }
                @Override
                public void mouseExited(MouseEvent e) { }
            });
            errorMeasuresLatest_CTS = errorMeasuresTable_CTS; //and save it for possible future export


            final JTable errorMeasuresTable_ITS = new JTable();
            errorMeasuresTable_ITS.setModel(new ErrorMeasuresTableModel_ITS(reportsIntTS));
            errorMeasuresTable_ITS.setDefaultRenderer(Object.class, new ErrorTableCellRenderer());
            errorMeasuresTable_ITS.setTableHeader(null);
            errorMeasuresTable_ITS.setVisible(true);

            JScrollPane scrollPaneErrorMeasuresITS = new JScrollPane(errorMeasuresTable_ITS);
            tabbedPaneTablesErrors.addTab("ITS", scrollPaneErrorMeasuresITS);
            errorMeasuresTable_ITS.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        //pozor, -1! pretoze 
                        int selectedRow = errorMeasuresTable_ITS.getSelectedRow();
                        ((ErrorMeasuresTableModel_ITS)errorMeasuresTable_ITS.getModel()).hideRow(selectedRow);
                    }
                }
                
                @Override
                public void mousePressed(MouseEvent e) { }
                @Override
                public void mouseReleased(MouseEvent e) { }
                @Override
                public void mouseEntered(MouseEvent e) { }
                @Override
                public void mouseExited(MouseEvent e) { }
            });
            errorMeasuresLatest_IntTS = errorMeasuresTable_ITS; //and save it for possible future export
        } else {
            if (! reportsCTS.isEmpty()) { //takze IntTS je empty, CTS moze zaplnit cele miesto
                final JTable errorMeasuresTable_CTS = new JTable();
                errorMeasuresTable_CTS.setModel(new ErrorMeasuresTableModel_CTS(reportsCTS));
                errorMeasuresTable_CTS.setDefaultRenderer(Object.class, new ErrorTableCellRenderer());
                errorMeasuresTable_CTS.setTableHeader(null);
                errorMeasuresTable_CTS.setVisible(true);

                JScrollPane scrollPaneErrorMeasuresCTS = new JScrollPane(errorMeasuresTable_CTS);
                tabbedPaneTablesErrors.addTab("CTS", scrollPaneErrorMeasuresCTS);
                errorMeasuresTable_CTS.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            //pozor, -1! pretoze 
                            int selectedRow = errorMeasuresTable_CTS.getSelectedRow();
                            ((ErrorMeasuresTableModel_CTS)errorMeasuresTable_CTS.getModel()).hideRow(selectedRow);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) { }
                    @Override
                    public void mouseReleased(MouseEvent e) { }
                    @Override
                    public void mouseEntered(MouseEvent e) { }
                    @Override
                    public void mouseExited(MouseEvent e) { }
                });
                errorMeasuresLatest_CTS = errorMeasuresTable_CTS; //and save it for possible future export
            } else {
                if (! reportsIntTS.isEmpty()) { //CTS je empty, ITS moze zaplnit cele miesto
                    final JTable errorMeasuresTable_ITS = new JTable();
                    errorMeasuresTable_ITS.setModel(new ErrorMeasuresTableModel_ITS(reportsIntTS));
                    errorMeasuresTable_ITS.setDefaultRenderer(Object.class, new ErrorTableCellRenderer());
                    errorMeasuresTable_ITS.setTableHeader(null);
                    errorMeasuresTable_ITS.setVisible(true);

                    JScrollPane scrollPaneErrorMeasuresITS = new JScrollPane(errorMeasuresTable_ITS);
                    tabbedPaneTablesErrors.addTab("ITS", scrollPaneErrorMeasuresITS);
                    errorMeasuresTable_ITS.addMouseListener(new MouseListener() {

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (e.getClickCount() == 2) {
                                //pozor, -1! pretoze 
                                int selectedRow = errorMeasuresTable_ITS.getSelectedRow();
                                ((ErrorMeasuresTableModel_ITS)errorMeasuresTable_ITS.getModel()).hideRow(selectedRow);
                            }
                        }

                        @Override
                        public void mousePressed(MouseEvent e) { }
                        @Override
                        public void mouseReleased(MouseEvent e) { }
                        @Override
                        public void mouseEntered(MouseEvent e) { }
                        @Override
                        public void mouseExited(MouseEvent e) { }
                    });
                    errorMeasuresLatest_IntTS = errorMeasuresTable_ITS; //and save it for possible future export
                } //else do not draw anything
            }
        }
        
        tabbedPaneTablesErrors.setVisible(true);
        panelErrorMeasures.add(tabbedPaneTablesErrors);
        
        panelErrorMeasures.repaint();
   }

    private void showDialogTooManyModelsInCase(int paramsSize, String modelName) {
        if (paramsSize > Utils.REASONABLY_MANY_MODELS) {
            DialogTooManyModels dialogTooManyModels = new DialogTooManyModels(this, true, paramsSize, modelName);
            dialogTooManyModels.setVisible(true);
        }
    }

    public void setPlotRanges(int sizeCTS, int sizeIntTS) {
        if (sizeCTS == 0) {
            groupZoomControlsCTS.disableAll();
        } else {
            groupZoomControlsCTS.enableAll();
            textFieldPlotRangeCTSXfrom.setText("" + PlotStateKeeper.getLastDrawnCrispXmin());
            textFieldPlotRangeCTSXto.setText("" + PlotStateKeeper.getLastDrawnCrispXmax());
            textFieldPlotRangeCTSYfrom.setText("" + PlotStateKeeper.getLastDrawnCrispYmin());
            textFieldPlotRangeCTSYto.setText("" + PlotStateKeeper.getLastDrawnCrispYmax());
        }
        
        if (sizeIntTS == 0) {
            groupZoomControlsIntTS.disableAll();
        } else {
            groupZoomControlsIntTS.enableAll();
            textFieldPlotRangeIntTSXfrom.setText("" + PlotStateKeeper.getLastDrawnIntXmin());
            textFieldPlotRangeIntTSXto.setText("" + PlotStateKeeper.getLastDrawnIntXmax());
            textFieldPlotRangeIntTSYfrom.setText("" + PlotStateKeeper.getLastDrawnIntYmin());
            textFieldPlotRangeIntTSYto.setText("" + PlotStateKeeper.getLastDrawnIntYmax());
        }
    }

    private void outputPredictionIntervals(List<TrainAndTestReportCrisp> reportsCTS) {
        List<TrainAndTestReportCrisp> reportsWithPredInts = new ArrayList<>();
        
        for (TrainAndTestReportCrisp r : reportsCTS) {
            if (r.getPredictionIntervalsLowers().length > 0) {
                reportsWithPredInts.add(r);
            }
        }
        
        if (! reportsWithPredInts.isEmpty()) {
            JTable tablePredictionInts = new JTable(new PredictionIntsTableModel(reportsWithPredInts));
            tablePredictionInts.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            TableColumn firstColumn = tablePredictionInts.getColumnModel().getColumn(0);
            firstColumn.setMinWidth(10);
            firstColumn.setMaxWidth(50);
            tablePredictionInts.setVisible(true);
            
            panelPredictionIntervals.removeAll();
            scrollPanePredictionIntervals.setViewportView(tablePredictionInts);
            panelPredictionIntervals.add(scrollPanePredictionIntervals);
            panelPredictionIntervals.repaint();
            
            panelPredictionIntervalsAll.setVisible(true);
            panelEverything.addTab("Prediction intervals", panelPredictionIntervalsAll);
        } else {
            panelEverything.remove(panelPredictionIntervalsAll);
        }
    }

    private List<Average> getAllAvgs(List<TrainAndTestReportCrisp> reportsCTS, List<TrainAndTestReportInterval> reportsIntTS) {
        List<Average> avgList = new ArrayList<>();
        avgList.add(new AverageSimple(checkBoxAvgSimpleCTSperM.isSelected(), 
                checkBoxAvgSimpleCTS.isSelected(), checkBoxAvgSimpleIntTSperM.isSelected(),
                checkBoxAvgSimpleIntTS.isSelected()));
        avgList.add(new AverageMDE(checkBoxAvgMDeCTSperM.isSelected(),
                checkBoxAvgMDeCTS.isSelected(), checkBoxAvgMDeIntTSperM.isSelected(),
                checkBoxAvgMDeIntTS.isSelected(), reportsCTS, reportsIntTS));
        avgList.add(new AverageTheilsU(checkBoxAvgTheilsuCTSperM.isSelected(), 
                checkBoxAvgTheilsuCTS.isSelected(), checkBoxAvgTheilsuIntTSperM.isSelected(), 
                checkBoxAvgTheilsuIntTS.isSelected(), reportsCTS, reportsIntTS));
        avgList.add(new AverageCoverageEfficiency(checkBoxAvgCvgEffIntTSperM.isSelected(), 
                checkBoxAvgCvgEffIntTS.isSelected(), reportsIntTS));
        avgList.add(new AverageEqCenterEqLogRadius(checkBoxAvgCenterLogRadiusIntTSperM.isSelected(), 
                checkBoxAvgCenterLogRadiusIntTS.isSelected()));
        avgList.add(new Median(checkBoxAvgMedianCTSperM.isSelected(), checkBoxAvgMedianCTS.isSelected(),
                checkBoxAvgMedianIntTSperM.isSelected(), checkBoxAvgMedianIntTS.isSelected()));
        
        return avgList;
    }

    private void outputComputedWeights() {
        if (checkBoxAvgONLY.isEnabled()) { //hack; tj aspon jeden avg bol pocitany
            panelCombinationWeights.removeAll();
            
            JTabbedPane tabbedPaneComputedWeights = new JTabbedPane(JTabbedPane.TOP);
            tabbedPaneComputedWeights.setSize(panelCombinationWeights.getWidth(), panelCombinationWeights.getHeight());
            
            for (Average avg : ((CallParamsDrawPlots)PlotStateKeeper.getLastCallParams()).getAvgConfig().getAvgs()) {
                if (! avg.getAllWeightsCrisp().isEmpty()) {
                    JTable tableComputedWeightsCTS = new JTable(new CombinationWeightsTableModel(avg.getAllWeightsCrisp()));
                    tableComputedWeightsCTS.setVisible(true);
                    JScrollPane scrollPaneComputedWeightsCTS = new JScrollPane();
                    scrollPaneComputedWeightsCTS.setViewportView(tableComputedWeightsCTS);
                    tabbedPaneComputedWeights.addTab(avg.getName(), scrollPaneComputedWeightsCTS);
                }
                
                if (! avg.getAllWeightsInterval().isEmpty()) {
                    JTable tableComputedWeightsIntTS = new JTable(new CombinationWeightsTableModel(avg.getAllWeightsInterval()));
                    tableComputedWeightsIntTS.setVisible(true);
                    JScrollPane scrollPaneComputedWeightsIntTS = new JScrollPane();
                    scrollPaneComputedWeightsIntTS.setViewportView(tableComputedWeightsIntTS);
                    tabbedPaneComputedWeights.addTab(avg.getName(), scrollPaneComputedWeightsIntTS);
                }
            }
            
            panelCombinationWeights.add(tabbedPaneComputedWeights);
            panelCombinationWeights.setVisible(true);
            panelCombinationWeights.repaint();
            
            panelCombinationWeightsAll.setVisible(true);
            panelEverything.addTab("Combination weights", panelCombinationWeightsAll);
            panelCombinationWeightsAll.repaint();
        } else {
            panelEverything.remove(panelCombinationWeightsAll);
        }
    }

    private void fillGUIelementsWithNewData() { //TODO pridat observer na polia v dataTableModele?
        cleanGUIelements();
        
        textFieldRunDataRangeTo.setText("" + DataTableModel.getInstance().getRowCount());
        for (String colname : DataTableModel.getInstance().getColnames()) {
            ((DefaultListModel)(listColnames.getModel())).addElement(colname);
            ((DefaultListModel)(listColnamesTransform.getModel())).addElement(colname);
            ((DefaultListModel)(listColnamesTests.getModel())).addElement(colname);
            comboBoxColnamesRun.addItem(colname);
            comboBoxRunFakeIntCenter.addItem(colname);
            comboBoxRunFakeIntRadius.addItem(colname);
            comboBoxRunFakeIntLower.addItem(colname);
            comboBoxRunFakeIntUpper.addItem(colname);
        }
        DialogAddIntervalExplanatoryVar.setColNames(DataTableModel.getInstance().getColnames());
        DialogAddIntervalOutputVar.setColNames(DataTableModel.getInstance().getColnames());
        DialogAddCrispExplanatoryVar.setColNames(DataTableModel.getInstance().getColnames());
        VARSettingsPanel.setColNames(DataTableModel.getInstance().getColnames());
//        panelSettingsVARMainInsideBecauseX = new VARSettingsPanel(); //musim ho znovu vytvorit, inak je uz vytvoreny a nema
//                                                       //tam tie colnames.
//        paneSettingsMethodsVAR.removeAll();
//        panelSettingsVARMain.removeAll();
//        panelSettingsVARMain.add(panelSettingsVARMainInsideBecauseX);
//        paneSettingsMethodsVAR.add(panelSettingsVARMain);
//        paneSettingsMethodsVAR.repaint();

        VARintSettingsPanel.setColNames(DataTableModel.getInstance().getColnames());
        panelVARintInside.removeAll();
        panelVARintInsideBecause = new VARintSettingsPanel();
        panelVARintInside.add(panelVARintInsideBecause);
        panelVARintInside.repaint();

        if (! DataTableModel.getInstance().getColnames().isEmpty()) {
            enableAllButtons();
        }
    }

    private void cleanGUIelements() {
        ((DefaultListModel)(listColnames.getModel())).removeAllElements();
        ((DefaultListModel)(listColnamesTransform.getModel())).removeAllElements();
        ((DefaultListModel)(listColnamesTests.getModel())).removeAllElements();
        ((DefaultListModel)(listPlotITSspecs.getModel())).removeAllElements();
        comboBoxColnamesRun.removeAllItems();
        comboBoxRunFakeIntCenter.removeAllItems();
        comboBoxRunFakeIntRadius.removeAllItems();
        comboBoxRunFakeIntLower.removeAllItems();
        comboBoxRunFakeIntUpper.removeAllItems();
        
        if (! DataTableModel.getInstance().getColnames().isEmpty()) {
            disableAllButtons();
        }
    }

    private void enableAllButtons() {
        groupButtons.enableAll();
        
        ((IntMLPCcodeSettingsPanel)panelSettingsIntervalMLPModeCcode).enableAllButtons();
        ((RBFSettingsPanel)panelSettingsRBFMain).enableAllButtons();
        ((RBFSettingsPanel)panelSettingsRBFint_center).enableAllButtons();
        ((RBFSettingsPanel)panelSettingsRBFint_radius).enableAllButtons();
        ((RBFSettingsPanel)panelSettingsHybrid_centerMain_RBF).enableAllButtons();
        ((RBFSettingsPanel)panelSettingsHybrid_radiusMain_RBF).enableAllButtons();
        ((MLPNnetSettingsPanel)panelSettingsMLPPackage_nnet).enableAllButtons();
        ((MLPNnetSettingsPanel)panelSettingsMLPintPackage_nnet_center).enableAllButtons();
        ((MLPNnetSettingsPanel)panelSettingsMLPintPackage_nnet_radius).enableAllButtons();
        ((MLPNnetSettingsPanel)panelSettingsHybrid_centerMain_MLPnnet).enableAllButtons();
        ((MLPNnetSettingsPanel)panelSettingsHybrid_radiusMain_MLPnnet).enableAllButtons();
        ((BNNSettingsPanel)panelSettingsBNNinside).enableAllButtons();
        ((BNNSettingsPanel)panelSettingsBNNint_center).enableAllButtons();
        ((BNNSettingsPanel)panelSettingsBNNint_radius).enableAllButtons();
        ((BNNSettingsPanel)panelSettingsHybrid_centerMain_BNN).enableAllButtons();
        ((BNNSettingsPanel)panelSettingsHybrid_radiusMain_BNN).enableAllButtons();
    }
    
    private void disableAllButtons() {
        groupButtons.disableAll();
        
        ((IntMLPCcodeSettingsPanel)panelSettingsIntervalMLPModeCcode).disableAllButtons();
        ((RBFSettingsPanel)panelSettingsRBFMain).disableAllButtons();
        ((RBFSettingsPanel)panelSettingsRBFint_center).disableAllButtons();
        ((RBFSettingsPanel)panelSettingsRBFint_radius).disableAllButtons();
        ((RBFSettingsPanel)panelSettingsHybrid_centerMain_RBF).disableAllButtons();
        ((RBFSettingsPanel)panelSettingsHybrid_radiusMain_RBF).disableAllButtons();
        ((MLPNnetSettingsPanel)panelSettingsMLPPackage_nnet).disableAllButtons();
        ((MLPNnetSettingsPanel)panelSettingsMLPintPackage_nnet_center).disableAllButtons();
        ((MLPNnetSettingsPanel)panelSettingsMLPintPackage_nnet_radius).disableAllButtons();
        ((MLPNnetSettingsPanel)panelSettingsHybrid_centerMain_MLPnnet).disableAllButtons();
        ((MLPNnetSettingsPanel)panelSettingsHybrid_radiusMain_MLPnnet).disableAllButtons();
        ((BNNSettingsPanel)panelSettingsBNNinside).disableAllButtons();
        ((BNNSettingsPanel)panelSettingsBNNint_center).disableAllButtons();
        ((BNNSettingsPanel)panelSettingsBNNint_radius).disableAllButtons();
        ((BNNSettingsPanel)panelSettingsHybrid_centerMain_BNN).disableAllButtons();
        ((BNNSettingsPanel)panelSettingsHybrid_radiusMain_BNN).disableAllButtons();
    }
    
    private void writeAllModelDetails(List<TrainAndTestReport> allReports) {
        textAreaModelsInfo.setText(AnalysisUtils.getModelDetails(allReports));
    }
    
    private void runModels(boolean isBatch) {
        Utils.resetModelID();

        groupExportButtons.enableAll();
        
        ///hack: turn off all AVG checkboxes in case the params tried to take values from them.
        //  it is not safe to support average in these settings; there may be too many differences, we cannot check everything
        boolean originalStateCheckboxAvgCTSPerMethod = checkBoxAvgSimpleCTSperM.isSelected();
        boolean originalStateCheckboxAvgCTS = checkBoxAvgSimpleCTS.isSelected();
        boolean originalStateCheckboxAvgIntTSPerMethod = checkBoxAvgSimpleIntTSperM.isSelected();
        boolean originalStateCheckboxAvgIntTS = checkBoxAvgSimpleIntTS.isSelected();
        boolean originalStateCheckboxAvgONLY = checkBoxAvgONLY.isSelected();
        if (isBatch) {
            checkBoxAvgSimpleCTSperM.setSelected(false);
            checkBoxAvgSimpleCTS.setSelected(false);
            checkBoxAvgSimpleIntTSperM.setSelected(false);
            checkBoxAvgSimpleIntTS.setSelected(false);
            checkBoxAvgONLY.setSelected(false);
            //------------end hack, part 1/2
        }
        
        
        //vsetky pridaju do zoznamu trainingreports svoje errormeasures a plotcode
        List<TrainAndTestReportCrisp> reportsCTS = new ArrayList<>();
        List<TrainAndTestReportInterval> reportsIntTS = new ArrayList<>();

        for (AnalysisBatchLine l : AnalysisBatchTableModel.getInstance().getAllLines()) {
            List<? extends Params> params = new ArrayList<>();
            int numModels = 0;
            try {
                params = l.getModelParams();
                numModels = l.getNumModels();
            } catch (IllegalArgumentException e) {
                //TODO log?
            }
            showDialogTooManyModelsInCase(numModels, l.getModel());
            
            if (continueWithTooManyModels) {
                Forecastable forecastable = null;
                
                switch (l.getModel()) { //crisp models
                    case Const.ARIMA:
                        forecastable = new Arima();
                        break;
                    case Const.HOLT:
                        forecastable = new Holt();
                        break;
                    case Const.HOLT_WINTERS:
                        forecastable = new HoltWinters();
                        break;
                    case Const.KNN_CUSTOM:
                        break;
                    case Const.KNN_FNN:
                        forecastable = new KNNfnn();
                        break;
                    case Const.KNN_KKNN:
                        forecastable = new KNNkknn();
                        break;
                    case Const.KNN_MYOWN:
                        forecastable = new KNNmyown();
                        break;
                    case Const.NEURALNET:
                        forecastable = new Neuralnet();
                        break;
                    case Const.NNET:
                        forecastable = new Nnet();
                        break;
                    case Const.NNETAR:
                        forecastable = new Nnetar();
                        break;
                    case Const.RBF:
                        forecastable = new RBF();
                        break;
                    case Const.SES:
                        forecastable = new SES();
                        break;
                    case Const.MAvg:
                        forecastable = new MAvg();
                    case Const.VAR:
                        break;
                        
                    case Const.BNN:
                        forecastable = new BNN();
                        break;

                }
                
                if (forecastable != null) {
                    for (Params p : params) {
                        TrainAndTestReportCrisp report = (TrainAndTestReportCrisp) (forecastable.forecast(DataTableModel.getInstance(), p));
                        if (report != null) {
                            report.setID(Utils.getModelID());
                            reportsCTS.add(report);
                        }
                    }
                } else { //inak je to intervalovy model, pokracovat
                    switch (l.getModel()) {
                        case Const.HOLT_INT:
                            forecastable = new HoltInt();
                            break;
                        case Const.HOLT_WINTERS_INT:
                            forecastable = new HoltWintersInt();
                            break;
                        case Const.HYBRID:
                            forecastable = new Hybrid();
                            break;
                        case Const.INTERVAL_HOLT:
                            forecastable = new IntervalHolt();
                            break;
                        case Const.INTERVAL_MLP_C_CODE:
                            forecastable = new IntervalMLPCcode();
                            break;
                        case Const.MLP_INT_NNET:
                            forecastable = new MLPintNnet();
                            break;
                        case Const.MLP_INT_NNETAR:
                            forecastable = new MLPintNnetar();
                            break;
                        case Const.RBF_INT:
                            forecastable = new RBFint();
                            break;
                        case Const.SES_INT:
                            forecastable = new SESint();
                            break;
                        case Const.VAR_INT:
                            forecastable = new VARint();
                            break;
                        case Const.BNN_INT:
                            forecastable = new BNNint();
                            break;
                    }

                    if (forecastable != null) {
                        for (Params p : params) {
                            TrainAndTestReportInterval report = (TrainAndTestReportInterval) (forecastable.forecast(DataTableModel.getInstance(), p));
                            report.setID(Utils.getModelID());
                            reportsIntTS.add(report);
                        }
                    } else { //still null?
                        throw new IllegalArgumentException("should have found something by now..?");
                    }
                }
            }
        }
        
        if (checkBoxRunIntervalRandomWalk.isSelected()) {
            String colnameCenter = comboBoxRunFakeIntCenter.getSelectedItem().toString();
            String colnameRadius = comboBoxRunFakeIntRadius.getSelectedItem().toString();
            List<Interval> dataRandomWalk = Utils.zipCentersRadiiToIntervals(DataTableModel.getInstance().getDataForColname(colnameCenter),
                    DataTableModel.getInstance().getDataForColname(colnameRadius));

            Distance distance = ((DistanceSettingsPanel)panelMLPintSettingsDistance).getSelectedDistance();
            RandomWalkIntervalParams params = new RandomWalkIntervalParams(); //TODO add support for rangeRun
            params.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)panelMLPintPercentTrain).getPercentTrain())); //TODO prerobit? zatial berie tento
            params.setDistance(distance);
            params.setDataRangeFrom(Integer.parseInt(textFieldRunDataRangeFrom.getText()));
            params.setDataRangeTo(Integer.parseInt(textFieldRunDataRangeTo.getText()));
            params.setNumForecasts(Integer.parseInt(textFieldRunNumForecasts.getText()));
            if (checkBoxRunIncludeRMSSE.isSelected()) {
                params.setSeasonality(Integer.parseInt(textFieldRunRMSSESeasonality.getText()));
            }
            
            RandomWalkInterval randomWalkInterval = new RandomWalkInterval();
            TrainAndTestReportInterval report = (TrainAndTestReportInterval) (randomWalkInterval.forecast(dataRandomWalk, params));
            report.setID(Utils.getModelID());
            reportsIntTS.add(report);
        }
        
        if (checkBoxRunRandomWalkCTS.isSelected()) {
            String colname = comboBoxColnamesRun.getSelectedItem().toString();
            List<Double> dataRandomWalk = DataTableModel.getInstance().getDataForColname(colname);

            RandomWalkParams params = new RandomWalkParams(); //TODO add support for rangeRun
            params.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)panelMLPPercentTrain).getPercentTrain())); //TODO prerobit? zatial berie tento
            params.setDataRangeFrom(Integer.parseInt(textFieldRunDataRangeFrom.getText()));
            params.setDataRangeTo(Integer.parseInt(textFieldRunDataRangeTo.getText()));
            params.setNumForecasts(Integer.parseInt(textFieldRunNumForecasts.getText()));
            if (checkBoxRunIncludeRMSSE.isSelected()) {
                params.setSeasonality(Integer.parseInt(textFieldRunRMSSESeasonality.getText()));
            }
            
            RandomWalk randomWalk = new RandomWalk();
            TrainAndTestReportCrisp report = (TrainAndTestReportCrisp) (randomWalk.forecast(dataRandomWalk, params));
            report.setID(Utils.getModelID());
            reportsCTS.add(report);
        }
        
        
        //first draw diagrams of NNs, if applicable. the plots need to be drawn second because of the problems
        //  with determining the canvas to export. this way the last canvas can be exported, for it is the plot
        List<TrainAndTestReport> allReports = new ArrayList<>();
        allReports.addAll(reportsCTS);
        allReports.addAll(reportsIntTS);
        
        writeAllModelDetails(allReports);
        
        List<JGDBufferedPanel> diagramPanels = PlotDrawer.drawDiagrams(tabbedPaneDiagramsNNs.getWidth(), tabbedPaneDiagramsNNs.getHeight(), allReports);
        
        tabbedPaneDiagramsNNs.removeAll();
        int i = 0;
        for (JGDBufferedPanel p : diagramPanels) {
            tabbedPaneDiagramsNNs.addTab("Page "+(++i), p);
        }
        panelDiagramsNNsInside.repaint();
        
        //show Forecast plot
        int numForecasts = FieldsParser.parseIntegers(textFieldRunNumForecasts).get(0);
        int from = Integer.parseInt(textFieldRunDataRangeFrom.getText()) - 1;
        int to = Integer.parseInt(textFieldRunDataRangeTo.getText());
        String colname_CTS = comboBoxColnamesRun.getSelectedItem().toString();
        List<TrainAndTestReport> addedReports = PlotDrawer.drawPlots(Const.MODE_DRAW_NEW, Const.MODE_REFRESH_NO, 
                new CallParamsDrawPlots(listPlotLegend, gdBufferedPanelPlot, panelPlot.getWidth(), 
                panelPlot.getHeight(),
                DataTableModel.getInstance().getDataForColname(colname_CTS), DataTableModel.getInstance().getRowCount(), numForecasts, reportsCTS,
                reportsIntTS, from, to, colname_CTS, 
                new AveragesConfig(getAllAvgs(reportsCTS, reportsIntTS), checkBoxAvgONLY.isSelected())));
        setPlotRanges(reportsCTS.size(), reportsIntTS.size());
        buttonPlotExportPlot.setEnabled(true);
        
        allReports = new ArrayList<>(); //we need to refresh allReports, 'cause sth might've been hack-added in drawPlots
        allReports.addAll(reportsCTS);
        allReports.addAll(reportsIntTS);
        allReports.addAll(addedReports);
        
        //show errors
        drawTablesErrorMeasures(reportsCTS, reportsIntTS, addedReports);
        
        //show prediction intervals, if any
        outputPredictionIntervals(reportsCTS);
        
        //show computed weights for combined models, if any
        outputComputedWeights();
        
        
        
        ///hack: turn on all AVG checkboxes the way they were before so nobody notices
        //  (and so that... happy debugging, suckers :D)
        if (isBatch) {
            checkBoxAvgSimpleCTSperM.setSelected(originalStateCheckboxAvgCTSPerMethod);
            checkBoxAvgSimpleCTS.setSelected(originalStateCheckboxAvgCTS);
            checkBoxAvgSimpleIntTSperM.setSelected(originalStateCheckboxAvgIntTSPerMethod);
            checkBoxAvgSimpleIntTS.setSelected(originalStateCheckboxAvgIntTS);
            checkBoxAvgONLY.setSelected(originalStateCheckboxAvgONLY);
        }
        //------------end hack, part 2/2
        

        //and show forecast values in the other pane
        final JTable forecastValuesTable = new JTable(new ForecastValsTableModel(numForecasts, allReports));
        
        forecastValuesTable.setColumnSelectionAllowed(true);
        forecastValuesTable.setRowSelectionAllowed(false);
        
        forecastValuesTable.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    //pozor, -1! pretoze 
                    int selectedCol = forecastValuesTable.getSelectedColumn();
                    ((ForecastValsTableModel)forecastValuesTable.getModel()).hideColumn(selectedCol);
//                    forecastValuesTable.repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });
        
        forecastValuesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn firstColumn = forecastValuesTable.getColumnModel().getColumn(0);
        firstColumn.setMinWidth(10);
        firstColumn.setMaxWidth(50);
        forecastValuesTable.setVisible(true);
        forecastValuesLatest = forecastValuesTable;
        panelForecastVals.removeAll();
        scrollPaneForecastVals.setViewportView(forecastValuesLatest);
        panelForecastVals.add(scrollPaneForecastVals);
        panelForecastVals.repaint();
        
        //and show residuals
        if (! allReports.isEmpty()) {
            final JTable residualsTable = new JTable(new ResidualsTableModel(allReports));
            residualsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
            residualsTable.setColumnSelectionAllowed(true);
            residualsTable.setRowSelectionAllowed(false);
            
            TableColumn firstCol = residualsTable.getColumnModel().getColumn(0);
            firstCol.setMinWidth(10);
            firstCol.setMaxWidth(50);
            residualsTable.setVisible(true);
            residualsTableLatest = residualsTable;
            panelResiduals.removeAll();
            scrollPaneResiduals.setViewportView(residualsTableLatest);
            panelResiduals.add(scrollPaneResiduals);
            panelResiduals.repaint();
        }
    }
    
    public void addReportToData(TrainAndTestReportCrisp r) {
        AnalysisUtils.addReportToData(r);
        fillGUIelementsWithNewData();
    }
    
    public void addReportToData(TrainAndTestReportInterval r) {
        AnalysisUtils.addReportToData(r);
        fillGUIelementsWithNewData();
    }

    public void convertITStoLBUB(IntervalNamesCentreRadius namesCR) {
        Transformations.convertITStoLBUB(namesCR);
        fillGUIelementsWithNewData();
    }

    public void convertITStoCR(IntervalNamesLowerUpper namesLBUB) {
        Transformations.convertITStoCR(namesLBUB);
        fillGUIelementsWithNewData();
    }

    private void exportTextArea(ActionEvent evt, JButton whichButton, JTextArea whichTextArea) {
        //TODO refactor this out to my own class; it is repeated quite a few times throughout the code
        JFileChooser fileChooser = new OverwriteFileChooser();
        fileChooser.setSelectedFile(new File("analysis.txt"));
        if (evt.getSource() == whichButton) {
            switch (fileChooser.showSaveDialog(this)) {
                case JFileChooser.APPROVE_OPTION:
                    File file = fileChooser.getSelectedFile();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        String text = whichTextArea.getText();
                        text = text.replace("\n", System.lineSeparator());
                        writer.append(text);
                        writer.flush();
                    } catch (IOException e) {
                        //TODO log
                    }
                    break;
                case JFileChooser.CANCEL_OPTION:
                default:
                //nothing
            }
        }
    }
    
    //groups
    private final ComponentGroup groupButtons = new ComponentGroup();
    private final ComponentGroup groupExportButtons = new ComponentGroup();
    private final ComponentGroup groupRunControlsCenterRadius = new ComponentGroup();
    private final ComponentGroup groupRunControlsLowerUpper = new ComponentGroup();
    private final ComponentGroup groupZoomControlsCTS = new ComponentGroup();
    private final ComponentGroup groupZoomControlsIntTS = new ComponentGroup();
    
    private final ComponentGroup groupCheckBoxesAvg_withoutAvgONLY = new ComponentGroup();
    
    private void addComponentsToGroups() {
        groupRunControlsCenterRadius.addAll(labelRunFakeIntCenter, labelRunFakeIntRadius, comboBoxRunFakeIntCenter,
                comboBoxRunFakeIntRadius);
        
        groupRunControlsLowerUpper.addAll(labelRunFakeIntLower, labelRunFakeIntUpper, comboBoxRunFakeIntLower,
                comboBoxRunFakeIntUpper);
        
        groupCheckBoxesAvg_withoutAvgONLY.addAll(
                checkBoxAvgSimpleCTS, checkBoxAvgSimpleCTSperM, checkBoxAvgSimpleIntTS, checkBoxAvgSimpleIntTSperM, 
                checkBoxAvgMDeCTS, checkBoxAvgMDeCTSperM, checkBoxAvgMDeIntTS, checkBoxAvgMDeIntTSperM,
                checkBoxAvgTheilsuCTS, checkBoxAvgTheilsuCTSperM, checkBoxAvgTheilsuIntTS, checkBoxAvgTheilsuIntTSperM,
                checkBoxAvgCvgEffIntTS, checkBoxAvgCvgEffIntTSperM,
                checkBoxAvgCenterLogRadiusIntTS, checkBoxAvgCenterLogRadiusIntTSperM,
                checkBoxAvgMedianCTS, checkBoxAvgMedianCTSperM, checkBoxAvgMedianIntTS, checkBoxAvgMedianIntTSperM);
        
        groupZoomControlsCTS.addAll(textFieldPlotRangeCTSXfrom, textFieldPlotRangeCTSXto, textFieldPlotRangeCTSYfrom,
                textFieldPlotRangeCTSYto, buttonPlotRestoreCTSRangeX, buttonPlotRestoreCTSRangeY, buttonPlotZoomCTS);
        
        groupZoomControlsIntTS.addAll(textFieldPlotRangeIntTSXfrom, textFieldPlotRangeIntTSXto, textFieldPlotRangeIntTSYfrom,
                textFieldPlotRangeIntTSYto, buttonPlotRestoreIntTSRangeX, buttonPlotRestoreIntTSRangeY, buttonPlotZoomIntTS);
        
        //TODO pridat annotation processor alebo daco, co prejde vsetky JButtony deklarovane v MainFrame a napridava ich do
        //   tejto grupy automaticky
        groupButtons.addAll(buttonPlotColname, buttonTrainAndTest, buttonRunAnalysisBatch, buttonSettingsAddToBatch_MLP, 
                buttonSettingsAddToBatch_MLPint, buttonSettingsAddToBatch_intMLP, buttonSettingsAddToBatch_RBF, 
                buttonSettingsAddToBatch_RBFint, buttonSettingsAddToBatch_ARIMA, buttonSettingsAddToBatch_Holt, 
                buttonSettingsAddToBatch_HoltWinters, buttonSettingsAddToBatch_HoltWintersInt, buttonSettingsAddToBatch_Holtint, 
                buttonSettingsAddToBatch_Hybrid, buttonSettingsAddToBatch_IntervalHolt, buttonSettingsAddToBatch_KNN, 
                buttonSettingsAddToBatch_SES, buttonSettingsAddToBatch_SESint, buttonSettingsAddToBatch_VARint, 
                buttonSettingsAddToBatch_BNN, buttonSettingsAddToBatch_BNNint, buttonACF, buttonPACF, buttonBasicStats, 
                buttonBoxplots, buttonHistograms, buttonNormProbPlot, buttonNormalityTests, buttonStationarityTest,
                buttonStructBreaks, buttonExportAnalysisText, buttonExportTestsPlots, buttonExportTextAreaTests, 
                buttonDiffSeries, buttonLogTransformSeries, buttonNormalize, buttonRemoveTrend, buttonAggregateToITS, 
                buttonConvertITSLBUBCR, buttonPlotSelectedITS, buttonPlotAllITSScatterplot, buttonPlotAllITSScatterplotMatrix, 
                buttonPlotAddITS, buttonPlotRemoveITS, buttonCTSclearSelection, buttonITSclearSelection);
        
        groupExportButtons.addAll(buttonRunExportErrorMeasures, buttonExportForecastValues, buttonExportResiduals);
    }
}
