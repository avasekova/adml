package gui;

import gui.dialogs.DialogAddCrispExplanatoryVar;
import gui.dialogs.DialogAddIntervalExplanatoryVar;
import gui.dialogs.DialogAddIntervalOutputVar;
import gui.dialogs.DialogLbUbCenterRadius;
import gui.dialogs.DialogTooManyModels;
import gui.filefilters.FileFilterEps;
import gui.filefilters.FileFilterPdf;
import gui.filefilters.FileFilterPng;
import gui.filefilters.FileFilterPs;
import gui.filefilters.FileFilterXlsXlsx;
import gui.filefilters.RFileFilter;
import gui.renderers.PlotLegendListCellRenderer;
import gui.renderers.PlotLegendTurnOFFableListCellRenderer;
import gui.renderers.PlotLegendTurnOFFableListElement;
import gui.renderers.TableBothHeadersCellColorRenderer;
import gui.settingspanels.ARIMASettingsPanel;
import gui.settingspanels.BestModelCriterionIntervalSettingsPanel;
import gui.settingspanels.DistanceSettingsPanel;
import gui.settingspanels.HoltSettingsPanel;
import gui.settingspanels.HoltWintersSettingsPanel;
import gui.settingspanels.IntHoltSettingsPanel;
import gui.settingspanels.IntMLPCcodeSettingsPanel;
import gui.settingspanels.KNNCustomSettingsPanel;
import gui.settingspanels.KNNFNNSettingsPanel;
import gui.settingspanels.KNNkknnSettingsPanel;
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
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableColumn;
import models.Arima;
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
import org.rosuda.JRI.Rengine;
import org.rosuda.javaGD.JGDBufferedPanel;
import params.AnalysisBatchLine;
import params.ArimaParams;
import params.BasicStats;
import params.HoltIntParams;
import params.HoltParams;
import params.HoltWintersIntParams;
import params.HoltWintersParams;
import params.HybridParams;
import params.IntervalHoltParams;
import params.IntervalMLPCcodeParams;
import params.KNNfnnParams;
import params.KNNkknnParams;
import params.MLPintNnetParams;
import params.MLPintNnetarParams;
import params.NeuralnetParams;
import params.NnetParams;
import params.NnetarParams;
import params.Params;
import params.RBFParams;
import params.RBFintParams;
import params.RandomWalkIntervalParams;
import params.RandomWalkParams;
import params.SESParams;
import params.SESintParams;
import params.VARParams;
import params.VARintParams;
import utils.Const;
import utils.CrispOutputVariable;
import utils.ExcelWriter;
import utils.FieldsParser;
import utils.Improvable;
import utils.MyRengine;
import utils.Utils;
import utils.imlp.Interval;
import utils.imlp.IntervalNamesCentreRadius;
import utils.imlp.IntervalNamesLowerUpper;
import utils.imlp.dist.BertoluzzaDistance;
import utils.imlp.dist.DeCarvalhoDistance;
import utils.imlp.dist.Distance;
import utils.imlp.dist.HausdorffDistance;
import utils.imlp.dist.IchinoYaguchiDistance;
import utils.imlp.dist.WeightedEuclideanDistance;
import utils.ugliez.CallParamsDrawPlotGeneral;
import utils.ugliez.CallParamsDrawPlots;
import utils.ugliez.CallParamsDrawPlotsITS;
import utils.ugliez.PlotStateKeeper;

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

        buttonGroup_runFakeIntCRLBUB = new javax.swing.ButtonGroup();
        panelEverything = new javax.swing.JTabbedPane();
        panelChart = new javax.swing.JPanel();
        buttonPlotColname = new javax.swing.JButton();
        panelPlot = new javax.swing.JPanel();
        buttonACF = new javax.swing.JButton();
        buttonPACF = new javax.swing.JButton();
        buttonPlotAllITS = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listColnames = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        textAreaPlotBasicStats = new javax.swing.JTextArea();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane4 = new javax.swing.JScrollPane();
        listPlotITSspecs = new javax.swing.JList();
        buttonPlotRemoveITS = new javax.swing.JButton();
        buttonPlotAddITS = new javax.swing.JButton();
        buttonPlotExportPlot = new javax.swing.JButton();
        jLabel89 = new javax.swing.JLabel();
        textFieldPlotRangeCTSXfrom = new javax.swing.JTextField();
        jLabel126 = new javax.swing.JLabel();
        textFieldPlotRangeCTSXto = new javax.swing.JTextField();
        jLabel127 = new javax.swing.JLabel();
        textFieldPlotRangeCTSYfrom = new javax.swing.JTextField();
        jLabel128 = new javax.swing.JLabel();
        textFieldPlotRangeCTSYto = new javax.swing.JTextField();
        buttonPlotRestoreCTSRangeX = new javax.swing.JButton();
        buttonPlotRestoreCTSRangeY = new javax.swing.JButton();
        jLabel129 = new javax.swing.JLabel();
        jLabel130 = new javax.swing.JLabel();
        textFieldPlotRangeIntTSYfrom = new javax.swing.JTextField();
        textFieldPlotRangeIntTSXfrom = new javax.swing.JTextField();
        jLabel131 = new javax.swing.JLabel();
        jLabel132 = new javax.swing.JLabel();
        textFieldPlotRangeIntTSYto = new javax.swing.JTextField();
        textFieldPlotRangeIntTSXto = new javax.swing.JTextField();
        buttonPlotRestoreIntTSRangeX = new javax.swing.JButton();
        buttonPlotRestoreIntTSRangeY = new javax.swing.JButton();
        buttonPlotZoomCTS = new javax.swing.JButton();
        buttonPlotZoomIntTS = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        listPlotLegend = new javax.swing.JList();
        buttonLegendSelectAll = new javax.swing.JButton();
        buttonLegendSelectNone = new javax.swing.JButton();
        buttonPlotAllITSScatterplot = new javax.swing.JButton();
        buttonPlotAllITSScatterplotMatrix = new javax.swing.JButton();
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
        panelSettingsHybridPercentTrain = new PercentTrainSettingsPanel();
        panelSettingsHybridDistance = new DistanceSettingsPanel();
        buttonSettingsAddToBatch_Hybrid = new javax.swing.JButton();
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
        panelErrorMeasuresAll = new javax.swing.JPanel();
        panelErrorMeasures = new javax.swing.JPanel();
        buttonRunShowHiddenErrorMeasures = new javax.swing.JButton();
        buttonHideAllErrorsExceptAvg = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        buttonRunExportErrorMeasures = new javax.swing.JButton();
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
        jButton1 = new javax.swing.JButton();
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
        menuBarMain = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuFileLoad = new javax.swing.JMenuItem();
        menuFileExit = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();

        buttonGroup_runFakeIntCRLBUB.add(radioButtonRunFakeIntLowerUpper);
        buttonGroup_runFakeIntCRLBUB.add(radioButtonRunFakeIntCenterRadius);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelEverything.setPreferredSize(new java.awt.Dimension(1361, 687));

        panelChart.setPreferredSize(new java.awt.Dimension(1361, 614));

        buttonPlotColname.setText("Plot data");
        buttonPlotColname.setEnabled(false);
        buttonPlotColname.setEnabled(false);
        buttonPlotColname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotColnameActionPerformed(evt);
            }
        });

        gdBufferedPanelPlot = new JGDBufferedPanel(panelPlot.getWidth(), panelPlot.getHeight());
        panelPlot.add(gdBufferedPanelPlot, BorderLayout.CENTER);
        panelPlot.setLayout(new java.awt.BorderLayout());

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

        buttonPlotAllITS.setText("Plot all specified ITS");
        buttonPlotAllITS.setEnabled(false);
        buttonPlotAllITS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotAllITSActionPerformed(evt);
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

        jScrollPane4.setPreferredSize(new java.awt.Dimension(247, 130));

        listPlotITSspecs.setModel(new DefaultListModel());
        listPlotITSspecs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(listPlotITSspecs);

        buttonPlotRemoveITS.setText("Remove selected ITS");
        buttonPlotRemoveITS.setEnabled(false);
        buttonPlotRemoveITS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotRemoveITSActionPerformed(evt);
            }
        });

        buttonPlotAddITS.setText("Specify new ITS");
        buttonPlotAddITS.setEnabled(false);
        buttonPlotAddITS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotAddITSActionPerformed(evt);
            }
        });

        buttonPlotExportPlot.setText("Save currently shown plot");
        buttonPlotExportPlot.setEnabled(false);
        buttonPlotExportPlot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotExportPlotActionPerformed(evt);
            }
        });

        jLabel89.setText("Zoom CTS axis x: from");

        textFieldPlotRangeCTSXfrom.setText("0");
        textFieldPlotRangeCTSXfrom.setEnabled(false);

        jLabel126.setText("to");

        textFieldPlotRangeCTSXto.setText("0");
        textFieldPlotRangeCTSXto.setEnabled(false);

        jLabel127.setText("Zoom CTS axis y: from");

        textFieldPlotRangeCTSYfrom.setText("0");
        textFieldPlotRangeCTSYfrom.setEnabled(false);

        jLabel128.setText("to");

        textFieldPlotRangeCTSYto.setText("0");
        textFieldPlotRangeCTSYto.setEnabled(false);

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

        jLabel129.setText("Zoom ITS axis x: from");

        jLabel130.setText("Zoom ITS axis y: from");

        textFieldPlotRangeIntTSYfrom.setText("0");
        textFieldPlotRangeIntTSYfrom.setEnabled(false);

        textFieldPlotRangeIntTSXfrom.setText("0");
        textFieldPlotRangeIntTSXfrom.setEnabled(false);

        jLabel131.setText("to");

        jLabel132.setText("to");

        textFieldPlotRangeIntTSYto.setText("0");
        textFieldPlotRangeIntTSYto.setEnabled(false);

        textFieldPlotRangeIntTSXto.setText("0");
        textFieldPlotRangeIntTSXto.setEnabled(false);

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

        buttonPlotZoomCTS.setText("Zoom CTS");
        buttonPlotZoomCTS.setEnabled(false);
        buttonPlotZoomCTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotZoomCTSActionPerformed(evt);
            }
        });

        buttonPlotZoomIntTS.setText("Zoom ITS");
        buttonPlotZoomIntTS.setEnabled(false);
        buttonPlotZoomIntTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotZoomIntTSActionPerformed(evt);
            }
        });

        jScrollPane3.setPreferredSize(new java.awt.Dimension(300, 130));

        listPlotLegend.setModel(new DefaultListModel());
        listPlotLegend.setCellRenderer(new PlotLegendListCellRenderer());
        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((PlotLegendTurnOFFableListElement)(((DefaultListModel)listPlotLegend.getModel())
                    .getElementAt(listPlotLegend.getSelectedIndex()))).dispatchEvent(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) { }
        @Override
        public void mouseEntered(MouseEvent e) { }
        @Override
        public void mouseExited(MouseEvent e) { }
        @Override
        public void mouseClicked(MouseEvent e) { }
    };
    listPlotLegend.addMouseListener(mouseListener);
    jScrollPane3.setViewportView(listPlotLegend);

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

    buttonPlotAllITSScatterplot.setText("Scatterplot");
    buttonPlotAllITSScatterplot.setEnabled(false);
    buttonPlotAllITSScatterplot.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            buttonPlotAllITSScatterplotActionPerformed(evt);
        }
    });

    buttonPlotAllITSScatterplotMatrix.setText("Sc.plt matrix");
    buttonPlotAllITSScatterplotMatrix.setEnabled(false);
    buttonPlotAllITSScatterplotMatrix.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            buttonPlotAllITSScatterplotMatrixActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout panelChartLayout = new javax.swing.GroupLayout(panelChart);
    panelChart.setLayout(panelChartLayout);
    panelChartLayout.setHorizontalGroup(
        panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(panelChartLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelPlot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelChartLayout.createSequentialGroup()
                    .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(panelChartLayout.createSequentialGroup()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(buttonPlotColname)
                                .addComponent(buttonACF)
                                .addComponent(buttonPACF))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(panelChartLayout.createSequentialGroup()
                            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel127)
                                .addGroup(panelChartLayout.createSequentialGroup()
                                    .addComponent(buttonPlotExportPlot)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel89)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(textFieldPlotRangeCTSXfrom, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                                .addComponent(textFieldPlotRangeCTSYfrom))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel126)
                                .addComponent(jLabel128))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(textFieldPlotRangeCTSYto, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                                .addComponent(textFieldPlotRangeCTSXto))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(buttonPlotRestoreCTSRangeX)
                                .addComponent(buttonPlotRestoreCTSRangeY))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(buttonPlotZoomCTS)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelChartLayout.createSequentialGroup()
                            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel129)
                                .addComponent(jLabel130))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(textFieldPlotRangeIntTSXfrom)
                                .addComponent(textFieldPlotRangeIntTSYfrom, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel131)
                                .addComponent(jLabel132))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(textFieldPlotRangeIntTSYto)
                                .addComponent(textFieldPlotRangeIntTSXto, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(panelChartLayout.createSequentialGroup()
                            .addComponent(buttonPlotAllITS)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(buttonPlotAllITSScatterplotMatrix))
                        .addGroup(panelChartLayout.createSequentialGroup()
                            .addComponent(buttonPlotRemoveITS)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(buttonPlotAllITSScatterplot))
                        .addComponent(buttonPlotAddITS))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelChartLayout.createSequentialGroup()
                            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(buttonPlotRestoreIntTSRangeX)
                                .addComponent(buttonPlotRestoreIntTSRangeY))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(buttonPlotZoomIntTS)
                            .addGap(0, 59, Short.MAX_VALUE))
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelChartLayout.createSequentialGroup()
                            .addComponent(buttonLegendSelectAll)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(buttonLegendSelectNone)))))
            .addContainerGap())
    );
    panelChartLayout.setVerticalGroup(
        panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(panelChartLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelChartLayout.createSequentialGroup()
                    .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelChartLayout.createSequentialGroup()
                                .addComponent(buttonPlotColname)
                                .addGap(3, 3, 3)
                                .addComponent(buttonACF)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonPACF))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelChartLayout.createSequentialGroup()
                                .addComponent(buttonPlotAddITS)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(buttonPlotRemoveITS)
                                    .addComponent(buttonPlotAllITSScatterplot))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(buttonPlotAllITS)
                                    .addComponent(buttonPlotAllITSScatterplotMatrix))))
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelChartLayout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(buttonPlotZoomCTS)
                                .addGap(15, 15, 15))
                            .addGroup(panelChartLayout.createSequentialGroup()
                                .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(buttonPlotRestoreCTSRangeX)
                                    .addComponent(textFieldPlotRangeCTSXto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonPlotRestoreCTSRangeY)))
                        .addGroup(panelChartLayout.createSequentialGroup()
                            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(textFieldPlotRangeCTSXfrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel126)
                                .addComponent(buttonPlotExportPlot))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel127)
                                .addComponent(textFieldPlotRangeCTSYfrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel128)
                                .addComponent(textFieldPlotRangeCTSYto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(panelChartLayout.createSequentialGroup()
                            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonPlotRestoreIntTSRangeX)
                                .addComponent(textFieldPlotRangeIntTSXto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(textFieldPlotRangeIntTSXfrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel129, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel131))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonPlotRestoreIntTSRangeY)
                                .addComponent(textFieldPlotRangeIntTSYto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(textFieldPlotRangeIntTSYfrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel130)
                                .addComponent(jLabel132)))
                        .addGroup(panelChartLayout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addComponent(buttonPlotZoomIntTS))))
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelChartLayout.createSequentialGroup()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(buttonLegendSelectAll)
                        .addComponent(buttonLegendSelectNone))))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(panelPlot, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
            .addContainerGap())
    );

    panelEverything.addTab("Plot", panelChart);

    jTableData.setModel(dataTableModel);
    scrollPaneData.setViewportView(jTableData);

    javax.swing.GroupLayout panelDataLayout = new javax.swing.GroupLayout(panelData);
    panelData.setLayout(panelDataLayout);
    panelDataLayout.setHorizontalGroup(
        panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(scrollPaneData, javax.swing.GroupLayout.DEFAULT_SIZE, 1401, Short.MAX_VALUE)
    );
    panelDataLayout.setVerticalGroup(
        panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(scrollPaneData, javax.swing.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE)
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
                    .addGap(0, 1153, Short.MAX_VALUE)))
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
            .addComponent(panelSettingsMLPPackage, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
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
                    .addGap(547, 647, Short.MAX_VALUE))
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
                        .addComponent(scrollPane_panelSettingsMLPintPackage_nnet_center, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
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
                    .addGap(0, 662, Short.MAX_VALUE))
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
                .addComponent(panelSettingsMLPintPackage_nnetar_center, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
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
            .addGap(0, 1310, Short.MAX_VALUE))
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
            .addGap(0, 637, Short.MAX_VALUE))
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
                    .addGap(0, 1153, Short.MAX_VALUE)))
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
            .addContainerGap(56, Short.MAX_VALUE))
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
                    .addGap(0, 1153, Short.MAX_VALUE))
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
            .addComponent(panelSettingsRBFMain, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
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
                            .addGap(0, 720, Short.MAX_VALUE))
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
                        .addComponent(panelSettingsRBFint_center, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE))))
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
                    .addComponent(panelARIMAPercTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 1143, Short.MAX_VALUE)))
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
            .addComponent(panelSettingsARIMAMain, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
            .addContainerGap())
    );

    paneSettingsMethods.addTab("ARIMA", paneSettingsMethodsARIMA);

    jLabel64.setText("Select method:");

    panelSettingsKNNoptions.setLayout(new java.awt.CardLayout());
    panelSettingsKNNoptions.add(panelSettingsKNNoptions_FNN, "panelSettingsKNNoptions_FNN");
    panelSettingsKNNoptions.add(panelSettingsKNNoptions_custom, "panelSettingsKNNoptions_custom");
    panelSettingsKNNoptions.add(panelSettingsKNNoptions_kknn, "panelSettingsKNNoptions_kknn");

    comboBoxKNNoptions.setModel(new javax.swing.DefaultComboBoxModel(new String[] { Const.KNN_FNN, Const.KNN_KKNN/*, Const.KNN_CUSTOM*/ }));
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
                    .addGap(0, 1180, Short.MAX_VALUE))
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
            .addComponent(panelSettingsKNNoptions, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE))
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
                            .addComponent(panelVARintPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)))
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
            .addComponent(panelVARintInside, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
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
                    .addComponent(panelSESpercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 1147, Short.MAX_VALUE)))
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
            .addComponent(panelSESmain, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
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
                            .addGap(0, 600, Short.MAX_VALUE))
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
                        .addComponent(panelSESint_center, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
                        .addComponent(panelSESint_radius, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jSeparator7))
            .addContainerGap())
    );

    paneSettingsMethods.addTab("SES(i)", panelSettingsMethodsSESint);

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
                    .addComponent(panelHoltPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 1147, Short.MAX_VALUE)))
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
            .addComponent(panelHoltInside, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
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
                            .addGap(0, 600, Short.MAX_VALUE))
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
                        .addComponent(panelHoltInt_center, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
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
                            .addComponent(panelIntervalHoltPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE))
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
            .addComponent(panelIntervalHoltMain, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
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
                    .addComponent(panelHoltWintersPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 1147, Short.MAX_VALUE)))
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
            .addComponent(panelHoltWintersInside, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
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
                            .addComponent(panelHoltWintersIntPercentTrain, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)))
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
                        .addComponent(panelHoltWintersInt_center, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
                        .addComponent(panelHoltWintersInt_radius, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jSeparator8))
            .addContainerGap())
    );

    paneSettingsMethods.addTab("Holt-Winters(i)", panelSettingsMethodsHoltWintersInt);

    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel2.setText("Center:");

    comboBoxSettingsHybridMethod_center.setModel(new javax.swing.DefaultComboBoxModel(new String[] { Const.NNET, Const.NNETAR, Const.RBF, Const.ARIMA, Const.KNN_FNN, Const.KNN_KKNN, Const.SES, Const.HOLT }));
    comboBoxSettingsHybridMethod_center.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            comboBoxSettingsHybridMethod_centerActionPerformed(evt);
        }
    });

    jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel4.setText("Radius:");

    comboBoxSettingsHybridMethod_radius.setModel(new javax.swing.DefaultComboBoxModel(new String[] { Const.NNET, Const.NNETAR, Const.RBF, Const.ARIMA, Const.KNN_FNN, Const.KNN_KKNN, Const.SES, Const.HOLT }));
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
                            .addGap(0, 617, Short.MAX_VALUE))
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
                .addComponent(panelSettingsHybrid_radiusMain, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                .addComponent(panelSettingsHybrid_centerMain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );

    paneSettingsMethods.addTab("[Hybrid]", paneSettingsMethodsHybrid);

    javax.swing.GroupLayout panelAnalysisSettingsLayout = new javax.swing.GroupLayout(panelAnalysisSettings);
    panelAnalysisSettings.setLayout(panelAnalysisSettingsLayout);
    panelAnalysisSettingsLayout.setHorizontalGroup(
        panelAnalysisSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(paneSettingsMethods, javax.swing.GroupLayout.DEFAULT_SIZE, 1401, Short.MAX_VALUE)
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

    checkBoxRunHoltWinters.setText("Holt-Winters");
    checkBoxRunHoltWinters.setEnabled(false);

    checkBoxRunHoltWintersInt.setText("Holt-Winters(i)");
    checkBoxRunHoltWintersInt.setEnabled(false);

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
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRunOutsideLayout.createSequentialGroup()
                            .addComponent(jLabelTrainingInfo)
                            .addGap(10, 10, 10))
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelRunOutsideLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkBoxRunHybrid)
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3))
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
                                    .addComponent(checkBoxRunRandomWalkCTS))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                        .addComponent(checkBoxRunARIMA)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(checkBoxRunKNNfnn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(checkBoxRunKNNcustom)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(checkBoxRunKNNkknn))
                                    .addGroup(panelRunOutsideLayout.createSequentialGroup()
                                        .addGap(129, 129, 129)
                                        .addComponent(jLabel72)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkBoxRunVAR)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBoxRunSES)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBoxRunHolt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBoxRunHoltWinters))
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
                                .addComponent(jLabel133)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelRunOutsideLayout.createSequentialGroup()
                            .addGap(622, 622, 622)
                            .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(checkBoxAvgTheilsuCTSperM)
                                .addComponent(checkBoxAvgMedianCTSperM))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(checkBoxAvgMedianCTS)
                                .addComponent(checkBoxAvgTheilsuCTS)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelRunOutsideLayout.createSequentialGroup()
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
                        .addComponent(checkBoxAvgONLY, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelRunOutsideLayout.createSequentialGroup()
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
                                    .addComponent(checkBoxAvgMedianIntTSperM)))))
                    .addGap(140, 140, 140))
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
                    .addComponent(textFieldRunRMSSESeasonality, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                                .addComponent(checkBoxRunHoltWinters))
                            .addGap(1, 1, 1)
                            .addGroup(panelRunOutsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel72)
                                .addComponent(checkBoxRunRandomWalkCTS))
                            .addGap(18, 18, 18)
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
                                .addComponent(checkBoxRunHoltWintersInt))
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
        .addGap(0, 591, Short.MAX_VALUE)
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
                    .addGap(0, 739, Short.MAX_VALUE)))
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
        .addComponent(scrollPaneForecastVals, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
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
            .addGap(0, 588, Short.MAX_VALUE))
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

    jButton1.setText("Export these diagrams");
    jButton1.setEnabled(false);

    javax.swing.GroupLayout panelDiagramsNNsLayout = new javax.swing.GroupLayout(panelDiagramsNNs);
    panelDiagramsNNs.setLayout(panelDiagramsNNsLayout);
    panelDiagramsNNsLayout.setHorizontalGroup(
        panelDiagramsNNsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(panelDiagramsNNsLayout.createSequentialGroup()
            .addComponent(jButton1)
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
            .addComponent(jButton1)
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
            .addComponent(scrollPanePredictionIntervals, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
            .addContainerGap())
    );

    javax.swing.GroupLayout panelPredictionIntervalsAllLayout = new javax.swing.GroupLayout(panelPredictionIntervalsAll);
    panelPredictionIntervalsAll.setLayout(panelPredictionIntervalsAllLayout);
    panelPredictionIntervalsAllLayout.setHorizontalGroup(
        panelPredictionIntervalsAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(panelPredictionIntervalsAllLayout.createSequentialGroup()
            .addComponent(buttonExportPredictionIntervals)
            .addGap(0, 1336, Short.MAX_VALUE))
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

    tableAnalysisBatch.setModel(batchTableModel);
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
                .addComponent(scrollPaneAnalysisBatchInside, javax.swing.GroupLayout.DEFAULT_SIZE, 1381, Short.MAX_VALUE)
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
            .addComponent(scrollPaneAnalysisBatchInside, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
            .addContainerGap())
    );

    panelEverything.addTab("[Analysis batch]", panelAnalysisBatch);

    jButton2.setText("Export");
    jButton2.setEnabled(false);

    javax.swing.GroupLayout panelCombinationWeightsLayout = new javax.swing.GroupLayout(panelCombinationWeights);
    panelCombinationWeights.setLayout(panelCombinationWeightsLayout);
    panelCombinationWeightsLayout.setHorizontalGroup(
        panelCombinationWeightsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 1381, Short.MAX_VALUE)
    );
    panelCombinationWeightsLayout.setVerticalGroup(
        panelCombinationWeightsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 602, Short.MAX_VALUE)
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

        //TODO odkomentovat------------------------------------------------------
//        JFileChooser fileChooser = new JFileChooser((File)null);
//        fileChooser.setMultiSelectionEnabled(false);
//        fileChooser.setAcceptAllFileFilterUsed(false); //do not allow "All files"
//        fileChooser.setFileFilter(new FileFilterXlsXlsx());
        LoadDataCustomizerPanel customizer = new LoadDataCustomizerPanel();
//        fileChooser.setAccessory(customizer);
//        if (evt.getSource() == menuFileLoad) {
//            switch (fileChooser.showOpenDialog(this)) {
//                case JFileChooser.APPROVE_OPTION:
//                    this.loadedFile = fileChooser.getSelectedFile();
                                               this.loadedFile = new File("C:\\Users\\Andrejka\\Documents\\fi_muni\\phd\\3d_semester-madrid\\w02\\javier redondo\\brent_prices_its_2000_2014.xlsx");
                    dataTableModel.openFile(loadedFile, customizer);
                    dataTableModel.fireTableStructureChanged();
                    textFieldRunDataRangeTo.setText("" + dataTableModel.getRowCount());
                    for (String colname : dataTableModel.getColnames()) {
                        //TODO na toto si dat potom pozor! - obavam sa, ze ked naloadujem novy subor, ostanu tam aj stare prvky v comboBoxoch, len pribudnu nove.
                        ((DefaultListModel)(listColnames.getModel())).addElement(colname);
                        comboBoxColnamesRun.addItem(colname);
                        comboBoxRunFakeIntCenter.addItem(colname);
                        comboBoxRunFakeIntRadius.addItem(colname);
                        comboBoxRunFakeIntLower.addItem(colname);
                        comboBoxRunFakeIntUpper.addItem(colname);
                    }
                    DialogAddIntervalExplanatoryVar.setColNames(dataTableModel.getColnames());
                    DialogAddIntervalOutputVar.setColNames(dataTableModel.getColnames());
                    DialogAddCrispExplanatoryVar.setColNames(dataTableModel.getColnames());
                    VARSettingsPanel.setColNames(dataTableModel.getColnames());
//                    panelSettingsVARMainInsideBecauseX = new VARSettingsPanel(); //musim ho znovu vytvorit, inak je uz vytvoreny a nema
//                                                                   //tam tie colnames.
//                    paneSettingsMethodsVAR.removeAll();
//                    panelSettingsVARMain.removeAll();
//                    panelSettingsVARMain.add(panelSettingsVARMainInsideBecauseX);
//                    paneSettingsMethodsVAR.add(panelSettingsVARMain);
//                    paneSettingsMethodsVAR.repaint();
                    
                    VARintSettingsPanel.setColNames(dataTableModel.getColnames());
                    panelVARintInside.removeAll();
                    panelVARintInsideBecause = new VARintSettingsPanel();
                    panelVARintInside.add(panelVARintInsideBecause);
                    panelVARintInside.repaint();
                    
                    if (! dataTableModel.getColnames().isEmpty()) {
                        buttonPlotColname.setEnabled(true);
                        buttonTrainAndTest.setEnabled(true);
                        buttonRunAnalysisBatch.setEnabled(true);
                        
                        buttonSettingsAddToBatch_MLP.setEnabled(true);
                        buttonSettingsAddToBatch_MLPint.setEnabled(true);
                        buttonSettingsAddToBatch_intMLP.setEnabled(true);
                        buttonSettingsAddToBatch_RBF.setEnabled(true);
                        buttonSettingsAddToBatch_RBFint.setEnabled(true);
                        buttonSettingsAddToBatch_ARIMA.setEnabled(true);
                        buttonSettingsAddToBatch_Holt.setEnabled(true);
                        buttonSettingsAddToBatch_HoltWinters.setEnabled(true);
                        buttonSettingsAddToBatch_HoltWintersInt.setEnabled(true);
                        buttonSettingsAddToBatch_Holtint.setEnabled(true);
                        buttonSettingsAddToBatch_Hybrid.setEnabled(true);
                        buttonSettingsAddToBatch_IntervalHolt.setEnabled(true);
                        buttonSettingsAddToBatch_KNN.setEnabled(true);
                        buttonSettingsAddToBatch_SES.setEnabled(true);
                        buttonSettingsAddToBatch_SESint.setEnabled(true);
                        buttonSettingsAddToBatch_VARint.setEnabled(true);
                        
                        buttonACF.setEnabled(true);
                        buttonPACF.setEnabled(true);
                        buttonPlotAllITS.setEnabled(true);
                        buttonPlotAllITSScatterplot.setEnabled(true);
                        buttonPlotAllITSScatterplotMatrix.setEnabled(true);
                        buttonPlotAddITS.setEnabled(true);
                        buttonPlotRemoveITS.setEnabled(true);
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
                    }
//                    break;
//                case JFileChooser.CANCEL_OPTION:
//                default:
//                    this.loadedFile = null;
//            }
//        }
        
    }//GEN-LAST:event_menuFileLoadActionPerformed

    private void menuFileExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileExitActionPerformed
        MyRengine.stopRengine();
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_menuFileExitActionPerformed

    private void buttonPlotColnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotColnameActionPerformed
        drawPlotGeneral(true, "plot.ts", "");
        setPlotRanges(1, 0);
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
        drawPlotGeneral(true, "acf", "");
        textAreaPlotBasicStats.setText("");
        setPlotRanges(0, 0);
    }//GEN-LAST:event_buttonACFActionPerformed

    private void buttonPACFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPACFActionPerformed
        drawPlotGeneral(true, "pacf", "");
        textAreaPlotBasicStats.setText("");
        setPlotRanges(0, 0);
    }//GEN-LAST:event_buttonPACFActionPerformed

    private void buttonPlotAllITSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotAllITSActionPerformed
        //tu uz len vezmi nasyslene v tych listoch
        PlotDrawer.drawPlotsITS(true, new CallParamsDrawPlotsITS(listPlotLegend, gdBufferedPanelPlot, panelPlot.getWidth(), 
                panelPlot.getHeight(), dataTableModel,
                listITSPlotCentreRadius, listITSPlotLowerUpper, false));
        textAreaPlotBasicStats.setText("");
        buttonPlotExportPlot.setEnabled(true);
        setPlotRanges(0, 1);
    }//GEN-LAST:event_buttonPlotAllITSActionPerformed

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
        dialogLBUBCenterRadius.setColnames(dataTableModel.getColnames());
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
            labelRunFakeIntCenter.setEnabled(true);
            labelRunFakeIntRadius.setEnabled(true);
            comboBoxRunFakeIntCenter.setEnabled(true);
            comboBoxRunFakeIntRadius.setEnabled(true);
            labelRunFakeIntLower.setEnabled(false);
            labelRunFakeIntUpper.setEnabled(false);
            comboBoxRunFakeIntLower.setEnabled(false);
            comboBoxRunFakeIntUpper.setEnabled(false);
        }
    }//GEN-LAST:event_radioButtonRunFakeIntCenterRadiusActionPerformed

    private void radioButtonRunFakeIntLowerUpperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonRunFakeIntLowerUpperActionPerformed
        if (radioButtonRunFakeIntLowerUpper.isSelected()) {
            labelRunFakeIntLower.setEnabled(true);
            labelRunFakeIntUpper.setEnabled(true);
            comboBoxRunFakeIntLower.setEnabled(true);
            comboBoxRunFakeIntUpper.setEnabled(true);
            labelRunFakeIntCenter.setEnabled(false);
            labelRunFakeIntRadius.setEnabled(false);
            comboBoxRunFakeIntCenter.setEnabled(false);
            comboBoxRunFakeIntRadius.setEnabled(false);
        }
    }//GEN-LAST:event_radioButtonRunFakeIntLowerUpperActionPerformed

    private void buttonRunExportErrorMeasuresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRunExportErrorMeasuresActionPerformed
        //TODO export with formatting - the highest, lowest vals highlighted etc.
        JFileChooser fileChooser = new JFileChooser() {
            @Override
            public void approveSelection() {
                File f = getSelectedFile();
                if (f.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            super.approveSelection();
                            return;
                        case JOptionPane.NO_OPTION:
                            return;
                        case JOptionPane.CLOSED_OPTION:
                            return;
                        case JOptionPane.CANCEL_OPTION:
                            cancelSelection();
                            return;
                    }
                }
                super.approveSelection();
            }
        };
        fileChooser.setCurrentDirectory(null);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setSelectedFile(new File("error_measures.xls"));
        if (evt.getSource() == buttonRunExportErrorMeasures) {
            switch (fileChooser.showSaveDialog(this)) {
                case JFileChooser.APPROVE_OPTION:
                    File errorMeasuresFile = fileChooser.getSelectedFile();
                    //TODO mozno sa tu spytat, ci chce prepisat existujuci subor
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
        buttonRunExportErrorMeasures.setEnabled(true); //enable error measures exporting after the first run
        buttonExportForecastValues.setEnabled(true);

        //ktorekolvek su zafajknute, pridaju do zoznamu trainingreports svoje errormeasures a plotcode
        List<TrainAndTestReportCrisp> reportsCTS = new ArrayList<>();
        List<TrainAndTestReportInterval> reportsIntTS = new ArrayList<>();

        if (checkBoxRunMLPnnetar.isSelected()) {
            List<NnetarParams> params = getParamsNnetar(panelMLPPercentTrain, comboBoxColnamesRun, panelSettingsMLPPackage_nnetar);
            
            showDialogTooManyModelsInCase(params.size(), Const.NNETAR);
            if (continueWithTooManyModels) {
                Forecastable nnetar = new Nnetar();
                for (NnetarParams p : params) {
                    TrainAndTestReportCrisp report = (TrainAndTestReportCrisp) (nnetar.forecast(dataTableModel, p));
                    reportsCTS.add(report);
                }
            }
        }

        if (checkBoxRunMLPneuralnet.isSelected()) {
            List<NeuralnetParams> params = getParamsNeuralnet();
            
            showDialogTooManyModelsInCase(params.size(), Const.NEURALNET);
            if (continueWithTooManyModels) {
                Forecastable neuralnet = new Neuralnet();
                for (NeuralnetParams p : params) {
                    TrainAndTestReportCrisp report = (TrainAndTestReportCrisp) (neuralnet.forecast(dataTableModel, p));
                    reportsCTS.add(report);
                }
            }
        }

        if (checkBoxRunMLPnnet.isSelected()) {
            try {
                List<NnetParams> params = getParamsNnet(panelMLPPercentTrain, comboBoxColnamesRun, panelSettingsMLPPackage_nnet);

                showDialogTooManyModelsInCase(params.size(), Const.NNET);
                if (continueWithTooManyModels) {
                    Forecastable nnet = new Nnet();
                    for (NnetParams p : params) {
                        TrainAndTestReportCrisp report = (TrainAndTestReportCrisp) (nnet.forecast(dataTableModel, p));
                        reportsCTS.add(report);
                    }
                }
            } catch (IllegalArgumentException e) {
                //TODO log alebo nieco
            }
        }

        if (checkBoxRunIntervalMLPCcode.isSelected()) {
            try {
                List<IntervalMLPCcodeParams> params = getParamsIntervalMLPCcode(panelIntMLPPercentTrain, panelSettingsIntervalMLPModeCcode);

                showDialogTooManyModelsInCase(params.size(), Const.INTERVAL_MLP_C_CODE);
                if (continueWithTooManyModels) {
                    Forecastable cCode = new IntervalMLPCcode();
                    for (IntervalMLPCcodeParams p : params) {
                        TrainAndTestReportInterval report = (TrainAndTestReportInterval) (cCode.forecast(dataTableModel, p));
                        reportsIntTS.add(report);
                    }
                }
            } catch (IllegalArgumentException e) {
                //TODO log alebo nieco
            }
        }

        if (checkBoxRunMLPintNnetar.isSelected()) {
            List<MLPintNnetarParams> params = getParamsMLPintNnetar(panelMLPintPercentTrain, comboBoxRunFakeIntCenter, 
                    panelSettingsMLPintPackage_nnetar_center, panelMLPintPercentTrain, comboBoxRunFakeIntRadius, 
                    panelSettingsMLPintPackage_nnetar_radius, panelMLPintSettingsDistance, textFieldNumNetsToTrainMLPint,
                        panelBestModelCriterionMLPint);
            
            showDialogTooManyModelsInCase(params.size(), Const.MLP_INT_NNETAR);
            if (continueWithTooManyModels) {
                //run two separate forecasts, one for Center and the other for Radius
                Forecastable mlpInt = new MLPintNnetar();

                for (MLPintNnetarParams p : params) {
                    TrainAndTestReportInterval report = (TrainAndTestReportInterval) (mlpInt.forecast(dataTableModel, p));
                    reportsIntTS.add(report);
                }
            }
        }
        
        if (checkBoxRunMLPintNnet.isSelected()) {
            try {
                List<MLPintNnetParams> params = getParamsMLPintNnet(panelMLPintPercentTrain, comboBoxRunFakeIntCenter, 
                        panelSettingsMLPintPackage_nnet_center, panelMLPintPercentTrain, comboBoxRunFakeIntRadius, 
                        panelSettingsMLPintPackage_nnet_radius, panelMLPintSettingsDistance, textFieldNumNetsToTrainMLPint,
                        panelBestModelCriterionMLPint);

                showDialogTooManyModelsInCase(params.size(), Const.MLP_INT_NNET);
                if (continueWithTooManyModels) {
                    //run two separate forecasts, one for Center and the other for Radius
                    Forecastable mlpInt = new MLPintNnet();

                    for (MLPintNnetParams p : params) {
                        TrainAndTestReportInterval report = (TrainAndTestReportInterval) (mlpInt.forecast(dataTableModel, p));
                        reportsIntTS.add(report);
                    }
                }
            } catch (IllegalArgumentException e) {
                //TODO log alebo nieco
            }
        }

        if (checkBoxRunARIMA.isSelected()) {
            List<ArimaParams> params = getParamsArima(panelARIMAPercTrain, comboBoxColnamesRun, panelSettingsARIMAMain);
            
            showDialogTooManyModelsInCase(params.size(), Const.ARIMA);
            if (continueWithTooManyModels) {
                Forecastable arima = new Arima();
                for (ArimaParams p : params) {
                    TrainAndTestReportCrisp report = (TrainAndTestReportCrisp) (arima.forecast(dataTableModel, p));
                    if (report != null) {
                        reportsCTS.add(report);
                    }
                }
            }
        }

        if (checkBoxRunKNNfnn.isSelected()) {
            List<KNNfnnParams> params = getParamsKNNfnn(panelKNNPercTrain, comboBoxColnamesRun, panelSettingsKNNoptions_FNN);
            
            showDialogTooManyModelsInCase(params.size(), Const.KNN_FNN);
            if (continueWithTooManyModels) {
                Forecastable kNN = new KNNfnn();
                for (KNNfnnParams p : params) {
                    TrainAndTestReportCrisp report = (TrainAndTestReportCrisp) (kNN.forecast(dataTableModel, p));
                    reportsCTS.add(report);
                }
            }
        }

//        if (checkBoxRunKNNcustom.isSelected()) {
//            List<KNNcustomParams> params = getParamsKNNcustom();
//            
//            showDialogTooManyModelsInCase(params.size(), Const.KNN_CUSTOM);
//            if (continueWithTooManyModels) {
//                Forecastable kNNcustom = new KNNcustom();
//                for (KNNcustomParams p : params) {
//                    TrainAndTestReportCrisp report = (TrainAndTestReportCrisp) (kNNcustom.forecast(dataTableModel, p));
//                    reportsCTS.add(report);
//                }
//            }
//        }

        if (checkBoxRunKNNkknn.isSelected()) {
            List<KNNkknnParams> params = getParamsKNNkknn(panelKNNPercTrain, comboBoxColnamesRun, panelSettingsKNNoptions_kknn);
            
            showDialogTooManyModelsInCase(params.size(), Const.KNN_KKNN);
            if (continueWithTooManyModels) {
                Forecastable kNNkknn = new KNNkknn();
                for (KNNkknnParams p : params) {
                    TrainAndTestReportCrisp report = (TrainAndTestReportCrisp) (kNNkknn.forecast(dataTableModel, p));
                    reportsCTS.add(report);
                }
            }
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
//                        reportsCTS.addAll(reports);
//                    }
//                }
//            } catch (IllegalArgumentException e) {
//                //TODO log alebo nieco
//            }
//        }
        
        if (checkBoxRunRBF.isSelected()) {
            try {
                List<RBFParams> params = getParamsRBF(panelRBFPercentTrain, comboBoxColnamesRun, panelSettingsRBFMain);

                showDialogTooManyModelsInCase(params.size(), Const.RBF);
                if (continueWithTooManyModels) {
                    Forecastable rbf = new RBF();
                    for (RBFParams p : params) {
                        TrainAndTestReportCrisp report = (TrainAndTestReportCrisp) (rbf.forecast(dataTableModel, p));
                        reportsCTS.add(report);
                    }
                }
            } catch (IllegalArgumentException e) {
                //TODO log alebo nieco
            }
            
        }
        
        if (checkBoxRunRBFint.isSelected()) {
            try {
                List<RBFintParams> params = getParamsRBFint(panelRBFintPercentTrain, comboBoxRunFakeIntCenter, 
                    panelSettingsRBFint_center, panelRBFintPercentTrain, comboBoxRunFakeIntRadius, 
                    panelSettingsRBFint_radius, panelRBFintSettingsDistance, textFieldNumNetworksToTrainRBFint,
                    panelBestModelCriterionRBFint);

                showDialogTooManyModelsInCase(params.size(), Const.RBF_INT);
                if (continueWithTooManyModels) {
                    //run two separate forecasts, one for Center and the other for Radius
                    Forecastable rbfInt = new RBFint();

                    for (RBFintParams p : params) {
                        TrainAndTestReportInterval report = (TrainAndTestReportInterval) (rbfInt.forecast(dataTableModel, p));
                        reportsIntTS.add(report);
                    }
                }
            } catch (IllegalArgumentException e) {
                //TODO log alebo nieco
            }
        }
        
        if (checkBoxRunVARint.isSelected()) {
            try {
                List<VARintParams> params = getParamsVARint(panelVARintPercentTrain, panelVARintDistance, panelVARintInsideBecause);

                showDialogTooManyModelsInCase(params.size(), Const.VAR_INT);
                if (continueWithTooManyModels) {
                    Forecastable varInt = new VARint();
                    for (VARintParams p : params) {
                        TrainAndTestReportInterval report = (TrainAndTestReportInterval) varInt.forecast(dataTableModel, p);
                        reportsIntTS.add(report);
                    }
                }
            } catch (IllegalArgumentException e) {
                //TODO log alebo nieco
            }
        }
        
        if (checkBoxRunIntervalRandomWalk.isSelected()) {
            String colnameCenter = comboBoxRunFakeIntCenter.getSelectedItem().toString();
            String colnameRadius = comboBoxRunFakeIntRadius.getSelectedItem().toString();
            List<Interval> dataRandomWalk = Utils.zipCentersRadiiToIntervals(dataTableModel.getDataForColname(colnameCenter),
                    dataTableModel.getDataForColname(colnameRadius));

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
            reportsIntTS.add(report);
        }
        
        if (checkBoxRunRandomWalkCTS.isSelected()) {
            String colname = comboBoxColnamesRun.getSelectedItem().toString();
            List<Double> dataRandomWalk = dataTableModel.getDataForColname(colname);

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
            reportsCTS.add(report);
        }
        
        if (checkBoxRunHybrid.isSelected()) {
            try {
                //TODO checks etc. - potom, ked sa presunut getParams metody do SettingsPanelov

                List<HybridParams> params = getParamsHybrid();

                showDialogTooManyModelsInCase(params.size(), Const.HYBRID);
                if (continueWithTooManyModels) {
                    //run two separate forecasts, one for Center and the other for Radius
                    Forecastable hybrid = new Hybrid();

                    for (HybridParams p : params) {
                        TrainAndTestReportInterval report = (TrainAndTestReportInterval) (hybrid.forecast(dataTableModel, p));
                        reportsIntTS.add(report);
                    }
                }
            } catch (IllegalArgumentException e) {
                //TODO log alebo nieco
            }
        }
        
        if (checkBoxRunHolt.isSelected()) {
            try {
                List<HoltParams> params = getParamsHolt(panelHoltPercentTrain, panelHoltInside, comboBoxColnamesRun);

                showDialogTooManyModelsInCase(params.size(), Const.HOLT);
                if (continueWithTooManyModels) {
                    Forecastable holt = new Holt();
                    for (HoltParams p : params) {
                        TrainAndTestReportCrisp report = (TrainAndTestReportCrisp) (holt.forecast(dataTableModel, p));
                        reportsCTS.add(report);
                    }
                }
            } catch (IllegalArgumentException e) {
                //TODO log alebo nieco
            }
        }
        
        if (checkBoxRunHoltInt.isSelected()) {
            try {
                List<HoltIntParams> params = getParamsHoltInt(panelHoltIntPercentTrain, panelHoltInt_center, 
                        comboBoxRunFakeIntCenter, panelHoltIntPercentTrain, panelHoltInt_radius, comboBoxRunFakeIntRadius,
                        panelHoltIntDistance);

                showDialogTooManyModelsInCase(params.size(), Const.HOLT_INT);
                if (continueWithTooManyModels) {
                    //run two separate forecasts, one for Center and the other for Radius
                    Forecastable holtInt = new HoltInt();

                    for (HoltIntParams p : params) {
                        TrainAndTestReportInterval report = (TrainAndTestReportInterval) (holtInt.forecast(dataTableModel, p));
                        reportsIntTS.add(report);
                    }
                }
            } catch (IllegalArgumentException e) {
                //TODO log alebo nieco
            }
        }
        
        if (checkBoxRunIntervalHolt.isSelected()) {
            try {
                List<IntervalHoltParams> params = getParamsIntervalHolt(panelIntervalHoltPercentTrain, comboBoxRunFakeIntCenter,
                        comboBoxRunFakeIntRadius, panelIntervalHoltDistance, panelIntervalHoltMain);

                showDialogTooManyModelsInCase(params.size(), Const.INTERVAL_HOLT);
                if (continueWithTooManyModels) {
                    Forecastable intHolt = new IntervalHolt();
                    for (IntervalHoltParams p : params) {
                        TrainAndTestReportInterval report = (TrainAndTestReportInterval) (intHolt.forecast(dataTableModel, p));
                        reportsIntTS.add(report);
                    }
                }
            } catch (IllegalArgumentException e) {
                //TODO log alebo nieco
            }
        }
        
        if (checkBoxRunSES.isSelected()) {
            List<SESParams> params = getParamsSES(panelSESpercentTrain, comboBoxColnamesRun, panelSESmain);
            
            showDialogTooManyModelsInCase(params.size(), Const.SES);
            if (continueWithTooManyModels) {
                Forecastable ses = new SES();
                for (SESParams p : params) {
                    TrainAndTestReportCrisp report = (TrainAndTestReportCrisp) (ses.forecast(dataTableModel, p));
                    reportsCTS.add(report);
                }
            }
        }
        
        if (checkBoxRunSESint.isSelected()) {
            try {
                List<SESintParams> params = getParamsSESint(panelSESintPercentTrain, panelSESint_center, 
                        comboBoxRunFakeIntCenter, panelSESintPercentTrain, panelSESint_radius, comboBoxRunFakeIntRadius,
                        panelSESintDistance);

                showDialogTooManyModelsInCase(params.size(), Const.SES_INT);
                if (continueWithTooManyModels) {
                    //run two separate forecasts, one for Center and the other for Radius
                    Forecastable sesInt = new SESint();

                    for (SESintParams p : params) {
                        TrainAndTestReportInterval report = (TrainAndTestReportInterval) (sesInt.forecast(dataTableModel, p));
                        reportsIntTS.add(report);
                    }
                }
            } catch (IllegalArgumentException e) {
                //TODO log alebo nieco
            }
        }
        
        if (checkBoxRunHoltWinters.isSelected()) {
            try {
                List<HoltWintersParams> params = getParamsHoltWinters(panelHoltWintersPercentTrain, 
                        panelHoltWintersInside, comboBoxColnamesRun);

                showDialogTooManyModelsInCase(params.size(), Const.HOLT_WINTERS);
                if (continueWithTooManyModels) {
                    Forecastable holtWinters = new HoltWinters();
                    for (HoltWintersParams p : params) {
                        TrainAndTestReportCrisp report = (TrainAndTestReportCrisp) (holtWinters.forecast(dataTableModel, p));
                        reportsCTS.add(report);
                    }
                }
            } catch (IllegalArgumentException e) {
                //TODO log or something
            }
        }
        
        if (checkBoxRunHoltWintersInt.isSelected()) {
            try {
                List<HoltWintersIntParams> params = getParamsHoltWintersInt(panelHoltWintersIntPercentTrain, 
                        panelHoltWintersInt_center, comboBoxRunFakeIntCenter, panelHoltWintersIntPercentTrain, 
                        panelHoltWintersInt_radius, comboBoxRunFakeIntRadius, panelHoltWintersIntDistance);

                showDialogTooManyModelsInCase(params.size(), Const.HOLT_WINTERS_INT);
                if (continueWithTooManyModels) {
                    //run two separate forecasts, one for Center and the other for Radius
                    Forecastable holtWintersInt = new HoltWintersInt();

                    for (HoltWintersIntParams p : params) {
                        TrainAndTestReportInterval report = (TrainAndTestReportInterval) (holtWintersInt.forecast(dataTableModel, p));
                        reportsIntTS.add(report);
                    }
                }
            } catch (IllegalArgumentException e) {
                //TODO log alebo nieco
            }
        }
        
        //add more methods/models here

        //first draw diagrams of NNs, if applicable. the plots need to be drawn second because of the problems
        //  with determining the canvas to export. this way the last canvas can be exported, for it is the plot
        List<TrainAndTestReport> allReports = new ArrayList<>();
        allReports.addAll(reportsCTS);
        allReports.addAll(reportsIntTS);
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
                dataTableModel.getDataForColname(colname_CTS), dataTableModel.getRowCount(), numForecasts, reportsCTS,
                reportsIntTS, from, to, colname_CTS, new AveragesConfig(getAllAvgs(reportsCTS, reportsIntTS), 
                checkBoxAvgONLY.isSelected())));
        setPlotRanges(reportsCTS.size(), reportsIntTS.size());
        textAreaPlotBasicStats.setText("");
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

        //and show forecast values in the other pane
        final JTable forecastValuesTable = new JTable(new ForecastValsTableModel(numForecasts, allReports));
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
        JFileChooser fileChooser = new JFileChooser() {
            @Override
            public void approveSelection() {
                File f = getSelectedFile();
                
                //teraz tomu prestavit priponu, ak ma priponu:
                String fileName = f.getPath().replace("\\", "\\\\");
                if (fileName.contains(".") && (fileName.lastIndexOf('.') < (fileName.length()-1))) {
                    //tipnem si, ze je tam pripona, a odrezem ju
                    String ext = fileName.substring((fileName.lastIndexOf('.')+1), fileName.length()); //vezmem si priponu
                    String fileNameOnly = fileName.substring(0, fileName.lastIndexOf('.'));
                    if (ext.equals("eps") || ext.equals("ps") || ext.equals("png") || ext.equals("pdf")) {
                        f = new File(fileNameOnly + "." + ((RFileFilter)getFileFilter()).getExtension());
                    }
                }
                
                if (f.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(this, "File " + f.toString() + " exists, overwrite?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            super.approveSelection();
                            return;
                        case JOptionPane.NO_OPTION:
                            return;
                        case JOptionPane.CLOSED_OPTION:
                            return;
                        case JOptionPane.CANCEL_OPTION:
                            cancelSelection();
                            return;
                    }
                }
                super.approveSelection();
            }
        };
        fileChooser.setCurrentDirectory(null);
        fileChooser.setMultiSelectionEnabled(false);
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
                    Rengine rengine = MyRengine.getRengine();

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
                    break;
                case JFileChooser.CANCEL_OPTION:
                default:
                //nothing
            }
        }
    }//GEN-LAST:event_buttonPlotExportPlotActionPerformed

    private void buttonRunRestoreRangeAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRunRestoreRangeAllActionPerformed
        textFieldRunDataRangeFrom.setText("1");
        textFieldRunDataRangeTo.setText("" + dataTableModel.getRowCount());
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
            
            dataTableModel.drawPlotGeneral(false, (CallParamsDrawPlotGeneral)(PlotStateKeeper.getLastCallParams()), rangeXCrisp, rangeYCrisp);
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
        JFileChooser fileChooser = new JFileChooser() {
            @Override
            public void approveSelection() {
                File f = getSelectedFile();
                if (f.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            super.approveSelection();
                            return;
                        case JOptionPane.NO_OPTION:
                            return;
                        case JOptionPane.CLOSED_OPTION:
                            return;
                        case JOptionPane.CANCEL_OPTION:
                            cancelSelection();
                            return;
                    }
                }
                super.approveSelection();
            }
        };
        fileChooser.setCurrentDirectory(null);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setSelectedFile(new File("forecast_values.xls"));
        if (evt.getSource() == buttonExportForecastValues) {
            switch (fileChooser.showSaveDialog(this)) {
                case JFileChooser.APPROVE_OPTION:
                    File forecastValuesFile = fileChooser.getSelectedFile();
                    //TODO mozno sa tu spytat, ci chce prepisat existujuci subor
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
        if (checkBoxAvgSimpleCTSperM.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgSimpleCTSperMActionPerformed

    private void checkBoxAvgSimpleCTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgSimpleCTSActionPerformed
        if (checkBoxAvgSimpleCTS.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgSimpleCTSActionPerformed

    private void checkBoxAvgSimpleIntTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgSimpleIntTSperMActionPerformed
        if (checkBoxAvgSimpleIntTSperM.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgSimpleIntTSperMActionPerformed

    private void checkBoxAvgSimpleIntTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgSimpleIntTSActionPerformed
        if (checkBoxAvgSimpleIntTS.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgSimpleIntTSActionPerformed

    private void comboBoxSettingsHybridMethod_centerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxSettingsHybridMethod_centerActionPerformed
        CardLayout card = (CardLayout)panelSettingsHybrid_centerMain.getLayout();
        switch (comboBoxSettingsHybridMethod_center.getSelectedItem().toString()) {
            case Const.NNETAR:
                card.show(panelSettingsHybrid_centerMain, "panelSettingsHybrid_centerMain_MLPnnetar");
                break;
            case Const.NNET:
                card.show(panelSettingsHybrid_centerMain, "panelSettingsHybrid_centerMain_MLPnnet");
                break;
            case Const.RBF:
                card.show(panelSettingsHybrid_centerMain, "panelSettingsHybrid_centerMain_RBF");
                break;
            case Const.ARIMA:
                card.show(panelSettingsHybrid_centerMain, "panelSettingsHybrid_centerMain_ARIMA");
                break;
            case Const.KNN_FNN:
                card.show(panelSettingsHybrid_centerMain, "panelSettingsHybrid_centerMain_KNNFNN");
                break;
            case Const.KNN_KKNN:
                card.show(panelSettingsHybrid_centerMain, "panelSettingsHybrid_centerMain_KNNkknn");
                break;
            case Const.SES:
                card.show(panelSettingsHybrid_centerMain, "panelSettingsHybrid_centerMain_SES");
                break;
            case Const.HOLT:
                card.show(panelSettingsHybrid_centerMain, "panelSettingsHybrid_centerMain_Holt");
                break;
        }
        panelSettingsHybrid_centerMain.repaint();
    }//GEN-LAST:event_comboBoxSettingsHybridMethod_centerActionPerformed

    private void comboBoxSettingsHybridMethod_radiusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxSettingsHybridMethod_radiusActionPerformed
        CardLayout card = (CardLayout)panelSettingsHybrid_radiusMain.getLayout();
        switch (comboBoxSettingsHybridMethod_radius.getSelectedItem().toString()) {
            case Const.NNETAR:
                card.show(panelSettingsHybrid_radiusMain, "panelSettingsHybrid_radiusMain_MLPnnetar");
                break;
            case Const.NNET:
                card.show(panelSettingsHybrid_radiusMain, "panelSettingsHybrid_radiusMain_MLPnnet");
                break;
            case Const.RBF:
                card.show(panelSettingsHybrid_radiusMain, "panelSettingsHybrid_radiusMain_RBF");
                break;
            case Const.ARIMA:
                card.show(panelSettingsHybrid_radiusMain, "panelSettingsHybrid_radiusMain_ARIMA");
                break;
            case Const.KNN_FNN:
                card.show(panelSettingsHybrid_radiusMain, "panelSettingsHybrid_radiusMain_KNNFNN");
                break;
            case Const.KNN_KKNN:
                card.show(panelSettingsHybrid_radiusMain, "panelSettingsHybrid_radiusMain_KNNkknn");
                break;
            case Const.SES:
                card.show(panelSettingsHybrid_radiusMain, "panelSettingsHybrid_radiusMain_SES");
                break;
            case Const.HOLT:
                card.show(panelSettingsHybrid_radiusMain, "panelSettingsHybrid_radiusMain_Holt");
                break;
        }
        panelSettingsHybrid_radiusMain.repaint();
    }//GEN-LAST:event_comboBoxSettingsHybridMethod_radiusActionPerformed

    private void checkBoxRunIncludeRMSSEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxRunIncludeRMSSEActionPerformed
        if (checkBoxRunIncludeRMSSE.isSelected()) {
            textFieldRunRMSSESeasonality.setEnabled(true);
        } else {
            textFieldRunRMSSESeasonality.setEnabled(false);
        }
    }//GEN-LAST:event_checkBoxRunIncludeRMSSEActionPerformed

    private void buttonRunAnalysisBatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRunAnalysisBatchActionPerformed
        //TODO neslo by toto cele nejak refaktorovat? je to prilis podobne buttonRun
        buttonRunExportErrorMeasures.setEnabled(true); //enable error measures exporting after the first run
        buttonExportForecastValues.setEnabled(true);
        
        
        ///hack: turn off all AVG checkboxes in case the params tried to take values from them.
        //  it is not safe to support average in these settings; there may be too many differences, we cannot check everything
        boolean originalStateCheckboxAvgCTSPerMethod = checkBoxAvgSimpleCTSperM.isSelected();
        boolean originalStateCheckboxAvgCTS = checkBoxAvgSimpleCTS.isSelected();
        boolean originalStateCheckboxAvgIntTSPerMethod = checkBoxAvgSimpleIntTSperM.isSelected();
        boolean originalStateCheckboxAvgIntTS = checkBoxAvgSimpleIntTS.isSelected();
        boolean originalStateCheckboxAvgONLY = checkBoxAvgONLY.isSelected();
        checkBoxAvgSimpleCTSperM.setSelected(false);
        checkBoxAvgSimpleCTS.setSelected(false);
        checkBoxAvgSimpleIntTSperM.setSelected(false);
        checkBoxAvgSimpleIntTS.setSelected(false);
        checkBoxAvgONLY.setSelected(false);
        //------------end hack, part 1/2
        
        
        

        //vsetky pridaju do zoznamu trainingreports svoje errormeasures a plotcode
        List<TrainAndTestReportCrisp> reportsCTS = new ArrayList<>();
        List<TrainAndTestReportInterval> reportsIntTS = new ArrayList<>();

        for (AnalysisBatchLine l : batchTableModel.getAllLines()) {//TODO refactor - toto je to iste, co v buttonRun
            List<? extends Params> params = new ArrayList<>();
            try {
                params = l.getModelParams();
            } catch (IllegalArgumentException e) {
                //TODO log?
            }
            showDialogTooManyModelsInCase(params.size(), l.getModel());
            
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
                    case Const.VAR:
                        break;

                }
                
                if (forecastable != null) {
                    for (Params p : params) {
                        TrainAndTestReportCrisp report = (TrainAndTestReportCrisp) (forecastable.forecast(dataTableModel, p));
                        if (report != null) {
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
                    }

                    if (forecastable != null) {
                        for (Params p : params) {
                            TrainAndTestReportInterval report = (TrainAndTestReportInterval) (forecastable.forecast(dataTableModel, p));
                            reportsIntTS.add(report);
                        }
                    } else { //still null?
                        switch (l.getModel()) {
                            case Const.RANDOM_WALK:
                                String colname = comboBoxColnamesRun.getSelectedItem().toString();
                                List<Double> dataRandomWalk = dataTableModel.getDataForColname(colname);

                                RandomWalkParams paramsRandomWalk = new RandomWalkParams(); //TODO add support for rangeRun
                                paramsRandomWalk.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)panelMLPPercentTrain).getPercentTrain())); //TODO prerobit? zatial berie tento
                                paramsRandomWalk.setDataRangeFrom(Integer.parseInt(textFieldRunDataRangeFrom.getText()));
                                paramsRandomWalk.setDataRangeTo(Integer.parseInt(textFieldRunDataRangeTo.getText()));
                                paramsRandomWalk.setNumForecasts(Integer.parseInt(textFieldRunNumForecasts.getText()));
                                if (checkBoxRunIncludeRMSSE.isSelected()) {
                                    paramsRandomWalk.setSeasonality(Integer.parseInt(textFieldRunRMSSESeasonality.getText()));
                                }

                                RandomWalk randomWalk = new RandomWalk();
                                TrainAndTestReportCrisp reportRandomWalk = (TrainAndTestReportCrisp) (randomWalk.forecast(dataRandomWalk, paramsRandomWalk));
                                reportsCTS.add(reportRandomWalk);
                                break;
                            case Const.RANDOM_WALK_INT:
                                String colnameCenter = comboBoxRunFakeIntCenter.getSelectedItem().toString();
                                String colnameRadius = comboBoxRunFakeIntRadius.getSelectedItem().toString();
                                List<Interval> dataRandomWalkInt = Utils.zipCentersRadiiToIntervals(dataTableModel.getDataForColname(colnameCenter),
                                        dataTableModel.getDataForColname(colnameRadius));

                                Distance distance = ((DistanceSettingsPanel)panelMLPintSettingsDistance).getSelectedDistance();
                                RandomWalkIntervalParams paramsRandomWalkInt = new RandomWalkIntervalParams(); //TODO add support for rangeRun
                                paramsRandomWalkInt.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)panelMLPintPercentTrain).getPercentTrain())); //TODO prerobit? zatial berie tento
                                paramsRandomWalkInt.setDistance(distance);
                                paramsRandomWalkInt.setDataRangeFrom(Integer.parseInt(textFieldRunDataRangeFrom.getText()));
                                paramsRandomWalkInt.setDataRangeTo(Integer.parseInt(textFieldRunDataRangeTo.getText()));
                                paramsRandomWalkInt.setNumForecasts(Integer.parseInt(textFieldRunNumForecasts.getText()));
                                if (checkBoxRunIncludeRMSSE.isSelected()) {
                                    paramsRandomWalkInt.setSeasonality(Integer.parseInt(textFieldRunRMSSESeasonality.getText()));
                                }

                                RandomWalkInterval randomWalkInterval = new RandomWalkInterval();
                                TrainAndTestReportInterval reportRandomWalkInt = (TrainAndTestReportInterval) (randomWalkInterval.forecast(dataRandomWalkInt, paramsRandomWalkInt));
                                reportsIntTS.add(reportRandomWalkInt);
                                break;
                        }
                    }
                }
            }
        }
        
        
        //first draw diagrams of NNs, if applicable. the plots need to be drawn second because of the problems
        //  with determining the canvas to export. this way the last canvas can be exported, for it is the plot
        List<TrainAndTestReport> allReports = new ArrayList<>();
        allReports.addAll(reportsCTS);
        allReports.addAll(reportsIntTS);
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
                dataTableModel.getDataForColname(colname_CTS), dataTableModel.getRowCount(), numForecasts, reportsCTS,
                reportsIntTS, from, to, colname_CTS, 
                new AveragesConfig(getAllAvgs(reportsCTS, reportsIntTS), checkBoxAvgONLY.isSelected())));
        setPlotRanges(reportsCTS.size(), reportsIntTS.size());
        textAreaPlotBasicStats.setText("");
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
        checkBoxAvgSimpleCTSperM.setSelected(originalStateCheckboxAvgCTSPerMethod);
        checkBoxAvgSimpleCTS.setSelected(originalStateCheckboxAvgCTS);
        checkBoxAvgSimpleIntTSperM.setSelected(originalStateCheckboxAvgIntTSPerMethod);
        checkBoxAvgSimpleIntTS.setSelected(originalStateCheckboxAvgIntTS);
        checkBoxAvgONLY.setSelected(originalStateCheckboxAvgONLY);
        //------------end hack, part 2/2
        
        
        
        

        //and show forecast values in the other pane
        forecastValuesLatest = new JTable(new ForecastValsTableModel(numForecasts, allReports));
        forecastValuesLatest.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn firstColumn = forecastValuesLatest.getColumnModel().getColumn(0);
        firstColumn.setMinWidth(10);
        firstColumn.setMaxWidth(50);
        forecastValuesLatest.setVisible(true);
        panelForecastVals.removeAll();
        scrollPaneForecastVals.setViewportView(forecastValuesLatest);
        panelForecastVals.add(scrollPaneForecastVals);
        panelForecastVals.repaint();
    }//GEN-LAST:event_buttonRunAnalysisBatchActionPerformed

    private void buttonSettingsAddToBatch_MLPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_MLPActionPerformed
        //zistit, ktora karta je hore
        switch (comboBoxRPackage.getSelectedItem().toString()) {
            case Const.NNET:
                try {
                    List<NnetParams> paramsNnet = getParamsNnet(panelMLPPercentTrain, comboBoxColnamesRun, panelSettingsMLPPackage_nnet);
                    batchTableModel.addLine(new AnalysisBatchLine(Const.NNET, paramsNnet));
                } catch (IllegalArgumentException e) {
                    //TODO
                }
                break;
            case Const.NNETAR:
                try {
                    List<NnetarParams> paramsNnetar = getParamsNnetar(panelMLPPercentTrain, comboBoxColnamesRun, panelSettingsMLPPackage_nnetar);
                    batchTableModel.addLine(new AnalysisBatchLine(Const.NNETAR, paramsNnetar));
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
                    List<MLPintNnetParams> paramsNnet = getParamsMLPintNnet(panelMLPintPercentTrain, comboBoxRunFakeIntCenter, 
                        panelSettingsMLPintPackage_nnet_center, panelMLPintPercentTrain, comboBoxRunFakeIntRadius, 
                        panelSettingsMLPintPackage_nnet_radius, panelMLPintSettingsDistance, textFieldNumNetsToTrainMLPint,
                        panelBestModelCriterionMLPint);
                    batchTableModel.addLine(new AnalysisBatchLine(Const.MLP_INT_NNET, paramsNnet));
                } catch (IllegalArgumentException e) {
                    //TODO
                }
                break;
            case Const.NNETAR:
                try {
                    List<MLPintNnetarParams> paramsNnetar = getParamsMLPintNnetar(panelMLPintPercentTrain, comboBoxRunFakeIntCenter, 
                        panelSettingsMLPintPackage_nnetar_center, panelMLPintPercentTrain, comboBoxRunFakeIntRadius, 
                        panelSettingsMLPintPackage_nnetar_radius, panelMLPintSettingsDistance, textFieldNumNetsToTrainMLPint,
                        panelBestModelCriterionMLPint);
                    batchTableModel.addLine(new AnalysisBatchLine(Const.MLP_INT_NNETAR, paramsNnetar));
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
                    List<IntervalMLPCcodeParams> paramsIMLP = getParamsIntervalMLPCcode(panelIntMLPPercentTrain, panelSettingsIntervalMLPModeCcode);
                    batchTableModel.addLine(new AnalysisBatchLine(Const.INTERVAL_MLP_C_CODE, paramsIMLP));
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
            List<RBFParams> paramsRBF = getParamsRBF(panelRBFPercentTrain, comboBoxColnamesRun, panelSettingsRBFMain);
            batchTableModel.addLine(new AnalysisBatchLine(Const.RBF, paramsRBF));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_RBFActionPerformed

    private void buttonSettingsAddToBatch_RBFintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_RBFintActionPerformed
        try {
            List<RBFintParams> paramsRBFint = getParamsRBFint(panelRBFintPercentTrain, comboBoxRunFakeIntCenter, 
                panelSettingsRBFint_center, panelRBFintPercentTrain, comboBoxRunFakeIntRadius, 
                panelSettingsRBFint_radius, panelRBFintSettingsDistance, textFieldNumNetworksToTrainRBFint,
                panelBestModelCriterionRBFint);
            batchTableModel.addLine(new AnalysisBatchLine(Const.RBF_INT, paramsRBFint));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_RBFintActionPerformed

    private void buttonSettingsAddToBatch_ARIMAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_ARIMAActionPerformed
        try {
            List<ArimaParams> paramsArima = getParamsArima(panelARIMAPercTrain, comboBoxColnamesRun, panelSettingsARIMAMain);
            batchTableModel.addLine(new AnalysisBatchLine(Const.ARIMA, paramsArima));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_ARIMAActionPerformed

    private void buttonSettingsAddToBatch_KNNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_KNNActionPerformed
        switch (comboBoxKNNoptions.getSelectedItem().toString()) {
            case Const.KNN_FNN:
                try {
                    List<KNNfnnParams> paramsFNN = getParamsKNNfnn(panelKNNPercTrain, comboBoxColnamesRun, panelSettingsKNNoptions_FNN);
                    batchTableModel.addLine(new AnalysisBatchLine(Const.KNN_FNN, paramsFNN));
                } catch (IllegalArgumentException e) {
                    //TODO
                }
                break;
            case Const.KNN_KKNN:
                try {
                    List<KNNkknnParams> paramsKKNN = getParamsKNNkknn(panelKNNPercTrain, comboBoxColnamesRun, panelSettingsKNNoptions_kknn);
                    batchTableModel.addLine(new AnalysisBatchLine(Const.KNN_KKNN, paramsKKNN));
                } catch (IllegalArgumentException e) {
                    //TODO
                }
                break;
            case Const.KNN_CUSTOM:
                break;
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_KNNActionPerformed

    private void buttonSettingsAddToBatch_VARintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_VARintActionPerformed
        try {
            List<VARintParams> paramsVARint = getParamsVARint(panelVARintPercentTrain, panelVARintDistance, panelVARintInsideBecause);
            batchTableModel.addLine(new AnalysisBatchLine(Const.VAR_INT, paramsVARint));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_VARintActionPerformed

    private void buttonSettingsAddToBatch_SESActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_SESActionPerformed
        try {
            List<SESParams> paramsSES = getParamsSES(panelSESpercentTrain, comboBoxColnamesRun, panelSESmain);
            batchTableModel.addLine(new AnalysisBatchLine(Const.SES, paramsSES));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_SESActionPerformed

    private void buttonSettingsAddToBatch_SESintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_SESintActionPerformed
        try {
            List<SESintParams> paramsSESint = getParamsSESint(panelSESintPercentTrain, panelSESint_center, 
                        comboBoxRunFakeIntCenter, panelSESintPercentTrain, panelSESint_radius, comboBoxRunFakeIntRadius,
                        panelSESintDistance);
            batchTableModel.addLine(new AnalysisBatchLine(Const.SES_INT, paramsSESint));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_SESintActionPerformed

    private void buttonSettingsAddToBatch_HoltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_HoltActionPerformed
        try {
            List<HoltParams> paramsHolt = getParamsHolt(panelHoltPercentTrain, panelHoltInside, comboBoxColnamesRun);
            batchTableModel.addLine(new AnalysisBatchLine(Const.HOLT, paramsHolt));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_HoltActionPerformed

    private void buttonSettingsAddToBatch_HoltintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_HoltintActionPerformed
        try {
            List<HoltIntParams> paramsHoltInt = getParamsHoltInt(panelHoltIntPercentTrain, panelHoltInt_center, 
                        comboBoxRunFakeIntCenter, panelHoltIntPercentTrain, panelHoltInt_radius, comboBoxRunFakeIntRadius,
                        panelHoltIntDistance);
            batchTableModel.addLine(new AnalysisBatchLine(Const.HOLT_INT, paramsHoltInt));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_HoltintActionPerformed

    private void buttonSettingsAddToBatch_IntervalHoltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_IntervalHoltActionPerformed
        try {
            List<IntervalHoltParams> paramsIntervalHolt = getParamsIntervalHolt(panelIntervalHoltPercentTrain, comboBoxRunFakeIntCenter,
                        comboBoxRunFakeIntRadius, panelIntervalHoltDistance, panelIntervalHoltMain);
            batchTableModel.addLine(new AnalysisBatchLine(Const.INTERVAL_HOLT, paramsIntervalHolt));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_IntervalHoltActionPerformed

    private void buttonSettingsAddToBatch_HoltWintersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_HoltWintersActionPerformed
        try {
            List<HoltWintersParams> paramsHoltWinters = getParamsHoltWinters(panelHoltWintersPercentTrain, 
                        panelHoltWintersInside, comboBoxColnamesRun);
            batchTableModel.addLine(new AnalysisBatchLine(Const.HOLT_WINTERS, paramsHoltWinters));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_HoltWintersActionPerformed

    private void buttonSettingsAddToBatch_HoltWintersIntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_HoltWintersIntActionPerformed
        try {
            List<HoltWintersIntParams> paramsHoltWintersInt = getParamsHoltWintersInt(panelHoltWintersIntPercentTrain, 
                        panelHoltWintersInt_center, comboBoxRunFakeIntCenter, panelHoltWintersIntPercentTrain, 
                        panelHoltWintersInt_radius, comboBoxRunFakeIntRadius, panelHoltWintersIntDistance);
            batchTableModel.addLine(new AnalysisBatchLine(Const.HOLT_WINTERS_INT, paramsHoltWintersInt));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_HoltWintersIntActionPerformed

    private void buttonSettingsAddToBatch_HybridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingsAddToBatch_HybridActionPerformed
        try {
            List<HybridParams> paramsHybrid = getParamsHybrid();
            batchTableModel.addLine(new AnalysisBatchLine(Const.HYBRID, paramsHybrid));
        } catch (IllegalArgumentException e) {
            //TODO
        }
    }//GEN-LAST:event_buttonSettingsAddToBatch_HybridActionPerformed

    private void buttonAnalysisBatchRemoveSelectedRowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAnalysisBatchRemoveSelectedRowsActionPerformed
        ((AnalysisBatchTableModel)(tableAnalysisBatch.getModel())).removeRows(tableAnalysisBatch.getSelectedRows());
    }//GEN-LAST:event_buttonAnalysisBatchRemoveSelectedRowsActionPerformed

    private void buttonLegendSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLegendSelectAllActionPerformed
        if (listPlotLegend.getCellRenderer() instanceof PlotLegendTurnOFFableListCellRenderer) {
            //fuuuj, to je hnusny sposob zistovania, ci to je ta legenda :/ TODO prerobit
            DefaultListModel model = (DefaultListModel)listPlotLegend.getModel();
            for (int i = 0; i < model.size(); i++) {
                ((PlotLegendTurnOFFableListElement)model.getElementAt(i)).getReport().setVisible(true);
            }
            
            //to iste ako v buttonLegenSelectNone a v drawLegend - mouseListener. TODO refactor
            listPlotLegend.repaint();
            //and then redraw the plots:
            String rangeXCrisp = "range(c(" + PlotStateKeeper.getLastDrawnCrispXmin() + "," + PlotStateKeeper.getLastDrawnCrispXmax() + "))";
            String rangeYCrisp = "range(c(" + PlotStateKeeper.getLastDrawnCrispYmin() + "," + PlotStateKeeper.getLastDrawnCrispYmax() + "))";
            String rangeXInt = "range(c(" + PlotStateKeeper.getLastDrawnIntXmin() + "," + PlotStateKeeper.getLastDrawnIntXmax() + "))";
            String rangeYInt = "range(c(" + PlotStateKeeper.getLastDrawnIntYmin() + "," + PlotStateKeeper.getLastDrawnIntYmax() + "))";
            PlotDrawer.drawPlots(Const.MODE_DRAW_NEW, Const.MODE_REFRESH_ONLY, (CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams()), 
                    rangeXCrisp , rangeYCrisp, rangeXInt, rangeYInt);
        } //else nereaguj
    }//GEN-LAST:event_buttonLegendSelectAllActionPerformed

    private void buttonLegendSelectNoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLegendSelectNoneActionPerformed
        if (listPlotLegend.getCellRenderer() instanceof PlotLegendTurnOFFableListCellRenderer) {
            //fuuuj, to je hnusny sposob zistovania, ci to je ta legenda :/ TODO prerobit
            DefaultListModel model = (DefaultListModel)listPlotLegend.getModel();
            for (int i = 0; i < model.size(); i++) {
                Plottable p = ((PlotLegendTurnOFFableListElement)model.getElementAt(i)).getReport();
                if ((p instanceof TrainAndTestReport) && (! ((TrainAndTestReport)p).isAverage())) {
                    p.setVisible(false);
                }
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
    }//GEN-LAST:event_buttonLegendSelectNoneActionPerformed

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
                panelPlot.getHeight(), dataTableModel, listITSPlotCentreRadius, listITSPlotLowerUpper, true));
        textAreaPlotBasicStats.setText("");
        buttonPlotExportPlot.setEnabled(true);
        setPlotRanges(0, 1);
    }//GEN-LAST:event_buttonPlotAllITSScatterplotActionPerformed

    private void buttonHideAllErrorsExceptAvgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonHideAllErrorsExceptAvgActionPerformed
        ((ErrorMeasuresTableModel_CTS)errorMeasuresLatest_CTS.getModel()).hideAllButAvg();
        ((ErrorMeasuresTableModel_ITS)errorMeasuresLatest_IntTS.getModel()).hideAllButAvg();
    }//GEN-LAST:event_buttonHideAllErrorsExceptAvgActionPerformed

    private void checkBoxAvgMDeCTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMDeCTSperMActionPerformed
        if (checkBoxAvgMDeCTSperM.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgMDeCTSperMActionPerformed

    private void checkBoxAvgMDeIntTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMDeIntTSperMActionPerformed
        if (checkBoxAvgMDeIntTSperM.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgMDeIntTSperMActionPerformed

    private void checkBoxAvgMDeCTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMDeCTSActionPerformed
        if (checkBoxAvgMDeCTS.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgMDeCTSActionPerformed

    private void checkBoxAvgMDeIntTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMDeIntTSActionPerformed
        if (checkBoxAvgMDeIntTS.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgMDeIntTSActionPerformed

    private void checkBoxAvgTheilsuCTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgTheilsuCTSperMActionPerformed
        if (checkBoxAvgTheilsuCTSperM.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgTheilsuCTSperMActionPerformed

    private void checkBoxAvgTheilsuIntTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgTheilsuIntTSperMActionPerformed
        if (checkBoxAvgTheilsuIntTSperM.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgTheilsuIntTSperMActionPerformed

    private void checkBoxAvgTheilsuCTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgTheilsuCTSActionPerformed
        if (checkBoxAvgTheilsuCTS.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgTheilsuCTSActionPerformed

    private void checkBoxAvgTheilsuIntTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgTheilsuIntTSActionPerformed
        if (checkBoxAvgTheilsuIntTS.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgTheilsuIntTSActionPerformed

    private void checkBoxAvgCvgEffIntTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgCvgEffIntTSperMActionPerformed
        if (checkBoxAvgCvgEffIntTSperM.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgCvgEffIntTSperMActionPerformed

    private void checkBoxAvgCvgEffIntTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgCvgEffIntTSActionPerformed
        if (checkBoxAvgCvgEffIntTS.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgCvgEffIntTSActionPerformed

    private void checkBoxAvgCenterLogRadiusIntTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgCenterLogRadiusIntTSperMActionPerformed
        if (checkBoxAvgCenterLogRadiusIntTSperM.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgCenterLogRadiusIntTSperMActionPerformed

    private void checkBoxAvgCenterLogRadiusIntTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgCenterLogRadiusIntTSActionPerformed
        if (checkBoxAvgCenterLogRadiusIntTS.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgCenterLogRadiusIntTSActionPerformed

    private void buttonPlotAllITSScatterplotMatrixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotAllITSScatterplotMatrixActionPerformed
        buttonPlotAllITSScatterplot.doClick(); //hack. for some reason does not draw the matrix without setting up the
                                               //   plot with drawing sth else first.
        ((DefaultListModel)listPlotLegend.getModel()).clear(); //a second hack to clear the legend after the scatterplot
        
        //tu uz len vezmi nasyslene v tych listoch
        PlotDrawer.drawScatterPlotMatrixITS(true, new CallParamsDrawPlotsITS(listPlotLegend, gdBufferedPanelPlot, panelPlot.getWidth(), 
                panelPlot.getHeight(), dataTableModel, listITSPlotCentreRadius, listITSPlotLowerUpper, true));
        textAreaPlotBasicStats.setText("");
        buttonPlotExportPlot.setEnabled(true);
        setPlotRanges(0, 0);
    }//GEN-LAST:event_buttonPlotAllITSScatterplotMatrixActionPerformed

    private void checkBoxAvgMedianCTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMedianCTSperMActionPerformed
        if (checkBoxAvgMedianCTSperM.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgMedianCTSperMActionPerformed

    private void checkBoxAvgMedianCTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMedianCTSActionPerformed
        if (checkBoxAvgMedianCTS.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgMedianCTSActionPerformed

    private void checkBoxAvgMedianIntTSperMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMedianIntTSperMActionPerformed
        if (checkBoxAvgMedianIntTSperM.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgMedianIntTSperMActionPerformed

    private void checkBoxAvgMedianIntTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAvgMedianIntTSActionPerformed
        if (checkBoxAvgMedianIntTS.isSelected()) { //ak sa to prave zafajklo
            checkBoxAvgONLY.setEnabled(true); //povol ONLY AVG
        } else { //prave sa to odfajklo
            maybeTurnOffPlotAvgONLY();
        }
    }//GEN-LAST:event_checkBoxAvgMedianIntTSActionPerformed
    
    private void maybeTurnOffPlotAvgONLY() {
        if ((! checkBoxAvgSimpleCTS.isSelected()) &&
            (! checkBoxAvgSimpleCTSperM.isSelected()) &&
            (! checkBoxAvgSimpleIntTS.isSelected()) &&
            (! checkBoxAvgSimpleIntTSperM.isSelected()) &&
                
            (! checkBoxAvgMDeCTS.isSelected()) &&
            (! checkBoxAvgMDeCTSperM.isSelected()) &&
            (! checkBoxAvgMDeIntTS.isSelected()) &&
            (! checkBoxAvgMDeIntTSperM.isSelected()) &&
                
            (! checkBoxAvgTheilsuCTS.isSelected()) &&
            (! checkBoxAvgTheilsuCTSperM.isSelected()) &&
            (! checkBoxAvgTheilsuIntTS.isSelected()) &&
            (! checkBoxAvgTheilsuIntTSperM.isSelected()) &&
                
            (! checkBoxAvgCvgEffIntTS.isSelected()) &&
            (! checkBoxAvgCvgEffIntTSperM.isSelected()) &&
                
            (! checkBoxAvgCenterLogRadiusIntTS.isSelected()) &&
            (! checkBoxAvgCenterLogRadiusIntTSperM.isSelected()) &&
                
            (! checkBoxAvgMedianCTS.isSelected()) &&
            (! checkBoxAvgMedianCTSperM.isSelected()) &&
            (! checkBoxAvgMedianIntTS.isSelected()) &&
            (! checkBoxAvgMedianIntTSperM.isSelected())) {
            checkBoxAvgONLY.setSelected(false);
            checkBoxAvgONLY.setEnabled(false);
        }
    }
    
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
                MainFrame mainFrame = new MainFrame();
                mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); //maximize the window
                mainFrame.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonACF;
    private javax.swing.JButton buttonAnalysisBatchRemoveSelectedRows;
    private javax.swing.JButton buttonExportForecastValues;
    private javax.swing.JButton buttonExportPredictionIntervals;
    private javax.swing.JButton buttonForecastValsHideAllButAvg;
    private javax.swing.JButton buttonForecastValsHideNoForecasts;
    private javax.swing.JButton buttonForecastValsShowHidden;
    private javax.swing.ButtonGroup buttonGroup_runFakeIntCRLBUB;
    private javax.swing.JButton buttonHideAllErrorsExceptAvg;
    private javax.swing.JButton buttonLegendSelectAll;
    private javax.swing.JButton buttonLegendSelectNone;
    private javax.swing.JButton buttonPACF;
    private javax.swing.JButton buttonPlotAddITS;
    private javax.swing.JButton buttonPlotAllITS;
    private javax.swing.JButton buttonPlotAllITSScatterplot;
    private javax.swing.JButton buttonPlotAllITSScatterplotMatrix;
    private javax.swing.JButton buttonPlotColname;
    private javax.swing.JButton buttonPlotExportPlot;
    private javax.swing.JButton buttonPlotRemoveITS;
    private javax.swing.JButton buttonPlotRestoreCTSRangeX;
    private javax.swing.JButton buttonPlotRestoreCTSRangeY;
    private javax.swing.JButton buttonPlotRestoreIntTSRangeX;
    private javax.swing.JButton buttonPlotRestoreIntTSRangeY;
    private javax.swing.JButton buttonPlotZoomCTS;
    private javax.swing.JButton buttonPlotZoomIntTS;
    private javax.swing.JButton buttonRunAnalysisBatch;
    private javax.swing.JButton buttonRunExportErrorMeasures;
    private javax.swing.JButton buttonRunRestoreRangeAll;
    private javax.swing.JButton buttonRunShowHiddenErrorMeasures;
    private javax.swing.JButton buttonSettingsAddToBatch_ARIMA;
    private javax.swing.JButton buttonSettingsAddToBatch_Holt;
    private javax.swing.JButton buttonSettingsAddToBatch_HoltWinters;
    private javax.swing.JButton buttonSettingsAddToBatch_HoltWintersInt;
    private javax.swing.JButton buttonSettingsAddToBatch_Holtint;
    private javax.swing.JButton buttonSettingsAddToBatch_Hybrid;
    private javax.swing.JButton buttonSettingsAddToBatch_IntervalHolt;
    private javax.swing.JButton buttonSettingsAddToBatch_KNN;
    private javax.swing.JButton buttonSettingsAddToBatch_MLP;
    private javax.swing.JButton buttonSettingsAddToBatch_MLPint;
    private javax.swing.JButton buttonSettingsAddToBatch_RBF;
    private javax.swing.JButton buttonSettingsAddToBatch_RBFint;
    private javax.swing.JButton buttonSettingsAddToBatch_SES;
    private javax.swing.JButton buttonSettingsAddToBatch_SESint;
    private javax.swing.JButton buttonSettingsAddToBatch_VARint;
    private javax.swing.JButton buttonSettingsAddToBatch_intMLP;
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
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTable jTableData;
    private javax.swing.JLabel labelRunFakeIntCenter;
    private javax.swing.JLabel labelRunFakeIntLower;
    private javax.swing.JLabel labelRunFakeIntRadius;
    private javax.swing.JLabel labelRunFakeIntUpper;
    private javax.swing.JList listColnames;
    private javax.swing.JList listPlotITSspecs;
    private javax.swing.JList listPlotLegend;
    private javax.swing.JMenuBar menuBarMain;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem menuFileExit;
    private javax.swing.JMenuItem menuFileLoad;
    private javax.swing.JTabbedPane paneSettingsMethods;
    private javax.swing.JPanel paneSettingsMethodsARIMA;
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
    private javax.swing.JPanel panelBestModelCriterionMLPint;
    private javax.swing.JPanel panelBestModelCriterionRBFint;
    private javax.swing.JPanel panelChart;
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
    private javax.swing.JPanel panelIntervalHoltDistance;
    private javax.swing.JPanel panelIntervalHoltMain;
    private javax.swing.JPanel panelIntervalHoltPercentTrain;
    private javax.swing.JPanel panelKNNPercTrain;
    private javax.swing.JPanel panelMLPPercentTrain;
    private javax.swing.JPanel panelMLPintPercentTrain;
    private javax.swing.JPanel panelMLPintSettingsDistance;
    private javax.swing.JPanel panelPlot;
    private javax.swing.JPanel panelPredictionIntervals;
    private javax.swing.JPanel panelPredictionIntervalsAll;
    private javax.swing.JPanel panelRBFPercentTrain;
    private javax.swing.JPanel panelRBFintPercentTrain;
    private javax.swing.JPanel panelRBFintSettingsDistance;
    private javax.swing.JPanel panelRunOutside;
    private javax.swing.JPanel panelSESintDistance;
    private javax.swing.JPanel panelSESintPercentTrain;
    private javax.swing.JPanel panelSESint_center;
    private javax.swing.JPanel panelSESint_radius;
    private javax.swing.JPanel panelSESmain;
    private javax.swing.JPanel panelSESpercentTrain;
    private javax.swing.JPanel panelSettingsARIMAMain;
    private javax.swing.JPanel panelSettingsHybridDistance;
    private javax.swing.JPanel panelSettingsHybridPercentTrain;
    private javax.swing.JPanel panelSettingsHybrid_centerMain;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_ARIMA;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_Holt;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_KNNFNN;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_KNNkknn;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_MLPnnet;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_MLPnnetar;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_RBF;
    private javax.swing.JPanel panelSettingsHybrid_centerMain_SES;
    private javax.swing.JPanel panelSettingsHybrid_radiusMain;
    private javax.swing.JPanel panelSettingsHybrid_radiusMain_ARIMA;
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
    private javax.swing.JPanel panelSettingsMethodsSES;
    private javax.swing.JPanel panelSettingsMethodsSESint;
    private javax.swing.JPanel panelSettingsMethodsVARint;
    private javax.swing.JPanel panelSettingsRBFMain;
    private javax.swing.JPanel panelSettingsRBFint_center;
    private javax.swing.JPanel panelSettingsRBFint_radius;
    private javax.swing.JPanel panelVARintDistance;
    private javax.swing.JPanel panelVARintInside;
    private javax.swing.JPanel panelVARintInsideBecause;
    private javax.swing.JPanel panelVARintPercentTrain;
    private javax.swing.JRadioButton radioButtonRunFakeIntCenterRadius;
    private javax.swing.JRadioButton radioButtonRunFakeIntLowerUpper;
    private javax.swing.JScrollPane scrollPaneAnalysisBatchInside;
    private javax.swing.JScrollPane scrollPaneData;
    private javax.swing.JScrollPane scrollPaneForecastVals;
    private javax.swing.JScrollPane scrollPanePredictionIntervals;
    private javax.swing.JScrollPane scrollPane_panelSettingsHybrid_centerMain_MLPnnet;
    private javax.swing.JScrollPane scrollPane_panelSettingsHybrid_radiusMain_MLPnnet;
    private javax.swing.JScrollPane scrollPane_panelSettingsMLPPackage_nnet;
    private javax.swing.JScrollPane scrollPane_panelSettingsMLPintPackage_nnet_center;
    private javax.swing.JScrollPane scrollPane_panelSettingsMLPintPackage_nnet_radius;
    private javax.swing.JTabbedPane tabbedPaneDiagramsNNs;
    private javax.swing.JTable tableAnalysisBatch;
    private javax.swing.JTextArea textAreaPlotBasicStats;
    private javax.swing.JTextField textFieldNumNetsToTrainMLPint;
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
    private static final DataTableModel dataTableModel = new DataTableModel();
    private static final AnalysisBatchTableModel batchTableModel = new AnalysisBatchTableModel();
    public static JGDBufferedPanel drawNowToThisGDBufferedPanel;
    private static JGDBufferedPanel gdBufferedPanelPlot;
    private DialogLbUbCenterRadius dialogLBUBCenterRadius;
    private JTable errorMeasuresLatest_CTS = new JTable(new ErrorMeasuresTableModel_CTS(new ArrayList<TrainAndTestReportCrisp>()));
    private JTable errorMeasuresLatest_IntTS = new JTable(new ErrorMeasuresTableModel_ITS((new ArrayList<TrainAndTestReportInterval>())));
    private JTable forecastValuesLatest;
    private List<IntervalNamesCentreRadius> listITSPlotCentreRadius = new ArrayList<>();
    private List<IntervalNamesLowerUpper> listITSPlotLowerUpper = new ArrayList<>();
    private boolean continueWithTooManyModels = true;
    
    public void setContinueWithTooManyModels(boolean continueWithTooManyModels) {
        this.continueWithTooManyModels = continueWithTooManyModels;
    }

    private void drawPlotGeneral(boolean drawNew, String plotFunction, String additionalArgs) {
        //TODO mozno refaktor a vyhodit do PlotDrawera - aby tam bolo vsetko kreslenie grafov
        //String colname = comboBoxColnames.getSelectedItem().toString();
        List<String> colnames = listColnames.getSelectedValuesList();
        
        List<BasicStats> basicStats = dataTableModel.drawPlotGeneral(drawNew, new CallParamsDrawPlotGeneral(listPlotLegend, 
                gdBufferedPanelPlot, panelPlot.getWidth(), panelPlot.getHeight(), colnames, plotFunction, additionalArgs));
        buttonPlotExportPlot.setEnabled(true);
        
        //mean, standard deviation, median
        StringBuilder basicStatsString = new StringBuilder();
        for (BasicStats stat : basicStats) {
            basicStatsString.append(stat.toString());
            basicStatsString.append(System.lineSeparator());
        }
        textAreaPlotBasicStats.setText(basicStatsString.toString());
    }
    
    private <T extends Params> void setParamsGeneral(Class<T> classss, List<T> resultList) {
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
    
    //TODO Java 8 a posielat metodu ako param, aby to nebolo tak ohavne?
    private List<NnetarParams> getParamsNnetar(javax.swing.JPanel percentTrainSettingsPanel,
            javax.swing.JComboBox comboBoxColName, javax.swing.JPanel panelSettingsNnetar) {
        NnetarParams par = new NnetarParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColName(comboBoxColName.getSelectedItem().toString()); //data
        
        List<NnetarParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        setParamsGeneral(NnetarParams.class, resultList);
        ((MLPNnetarSettingsPanel)panelSettingsNnetar).setSpecificParams(NnetarParams.class, resultList);
        
        return resultList;
    }
    
    private List<NeuralnetParams> getParamsNeuralnet() {
        NeuralnetParams params = new NeuralnetParams();
        //zohnat vsetky parametre pre dany model:
        //TODO: vymysliet nejak vseobecne! zatial je to natvrdo pre nnetar
//        params.put("percentTrain", sliderPercentTrain.getValue());
//        params.setNumForecasts(FieldsParser.parseIntegers(textFieldRunNumForecasts));
//        //TODO chytat vynimky, resp. validator na cisla
//        params.put("numNodesHidden", FieldsParser.parseIntegers(textFieldNumNodesInHiddenSingleLayer));
//        params.put("numSeasonalLags", FieldsParser.parseIntegers(textFieldNumSeasonalLags));
//        params.put("numNonSeasonalLags", FieldsParser.parseIntegers(textFieldNumNonSeasonalLags));
//        params.put("numReps", FieldsParser.parseIntegers(textFieldNumReps));
//        params.put("lambda", FieldsParser.parseIntegers(textFieldLambda));
//        params.put("numForecasts", FieldsParser.parseIntegers(textFieldNumForecasts)); //tieto sa pripocitaju k testovacim forecasts!
        
        List<NeuralnetParams> resultList = new ArrayList<>();
        resultList.add(params);
        return resultList;
    }
    
    private List<NnetParams> getParamsNnet(javax.swing.JPanel percentTrainSettingsPanel,
            javax.swing.JComboBox comboBoxColName, javax.swing.JPanel panelSettingsNnet) throws IllegalArgumentException {
        NnetParams par = new NnetParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColName(comboBoxColName.getSelectedItem().toString()); //data
        
        List<NnetParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        setParamsGeneral(NnetParams.class, resultList);
        ((MLPNnetSettingsPanel)panelSettingsNnet).setSpecificParams(NnetParams.class, resultList);
        
        return resultList;
    }
    
    private List<IntervalMLPCcodeParams> getParamsIntervalMLPCcode(javax.swing.JPanel percentTrainSettingsPanel,
            javax.swing.JPanel panelSettingsIMLPCcode) throws IllegalArgumentException {
        IntervalMLPCcodeParams par = new IntervalMLPCcodeParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        
        List<IntervalMLPCcodeParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        setParamsGeneral(IntervalMLPCcodeParams.class, resultList);
        ((IntMLPCcodeSettingsPanel)panelSettingsIMLPCcode).setSpecificParams(IntervalMLPCcodeParams.class, resultList);
        
        //TODO add the criterion here
        
        return resultList;
    }
    
    private List<MLPintNnetarParams> getParamsMLPintNnetar(javax.swing.JPanel percentTrainSettingsPanel_center,
            javax.swing.JComboBox comboBoxColName_center, javax.swing.JPanel panelSettingsNnetar_center,
            javax.swing.JPanel percentTrainSettingsPanel_radius, javax.swing.JComboBox comboBoxColName_radius, 
            javax.swing.JPanel panelSettingsNnetar_radius, javax.swing.JPanel panelSettingsDistance,
            JTextField numNetsToTrainField, JPanel panelBestModelCriterion) {
        List<NnetarParams> resultListCenter = getParamsNnetar(percentTrainSettingsPanel_center, comboBoxColName_center, 
                panelSettingsNnetar_center);
        List<NnetarParams> resultListRadius = getParamsNnetar(percentTrainSettingsPanel_radius, comboBoxColName_radius, 
                panelSettingsNnetar_radius);
        
        MLPintNnetarParams par = new MLPintNnetarParams();
        
        List<MLPintNnetarParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        ((DistanceSettingsPanel)panelSettingsDistance).setSpecificParams(MLPintNnetarParams.class, resultList);
        SettingsPanel.setSomethingList(MLPintNnetarParams.class, resultList, "setParamsCenter",
                NnetarParams.class, resultListCenter);
        SettingsPanel.setSomethingList(MLPintNnetarParams.class, resultList, "setParamsRadius",
                NnetarParams.class, resultListRadius);
        SettingsPanel.setSomethingList(MLPintNnetarParams.class, resultList, "setNumNetsToTrain",
                Integer.class, FieldsParser.parseIntegers(numNetsToTrainField).subList(0, 1));
        SettingsPanel.setSomethingOneValue(MLPintNnetarParams.class, resultList, "setCriterion",
                Improvable.class, ((BestModelCriterionIntervalSettingsPanel)panelBestModelCriterion).getBestModelCriterion());
        
        return resultList;
    }
    
    private List<MLPintNnetParams> getParamsMLPintNnet(javax.swing.JPanel percentTrainSettingsPanel_center,
            javax.swing.JComboBox comboBoxColName_center, javax.swing.JPanel panelSettingsNnet_center,
            javax.swing.JPanel percentTrainSettingsPanel_radius, javax.swing.JComboBox comboBoxColName_radius, 
            javax.swing.JPanel panelSettingsNnet_radius, javax.swing.JPanel panelSettingsDistance,
            javax.swing.JTextField numNetsToTrainField, 
            javax.swing.JPanel panelBestModelCriterion) throws IllegalArgumentException {
        List<NnetParams> resultListCenter = getParamsNnet(percentTrainSettingsPanel_center, comboBoxColName_center,
                panelSettingsNnet_center);
        List<NnetParams> resultListRadius = getParamsNnet(percentTrainSettingsPanel_radius, comboBoxColName_radius,
                panelSettingsNnet_radius);
        
        
        MLPintNnetParams par = new MLPintNnetParams();
        
        List<MLPintNnetParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        ((DistanceSettingsPanel)panelSettingsDistance).setSpecificParams(MLPintNnetParams.class, resultList);
        SettingsPanel.setSomethingList(MLPintNnetParams.class, resultList, "setParamsCenter",
                NnetParams.class, resultListCenter);
        SettingsPanel.setSomethingList(MLPintNnetParams.class, resultList, "setParamsRadius",
                NnetParams.class, resultListRadius);
        SettingsPanel.setSomethingList(MLPintNnetParams.class, resultList, "setNumNetsToTrain",
                Integer.class, FieldsParser.parseIntegers(numNetsToTrainField).subList(0, 1));
        SettingsPanel.setSomethingOneValue(MLPintNnetParams.class, resultList, "setCriterion",
                Improvable.class, ((BestModelCriterionIntervalSettingsPanel)panelBestModelCriterion).getBestModelCriterion());
        
        return resultList;
    }
    
    private List<ArimaParams> getParamsArima(javax.swing.JPanel percentTrainSettingsPanel,
            javax.swing.JComboBox comboBoxColName, javax.swing.JPanel panelSettingsArima) {
        ArimaParams par = new ArimaParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColName(comboBoxColName.getSelectedItem().toString()); //data
        
        List<ArimaParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        setParamsGeneral(ArimaParams.class, resultList);
        ((ARIMASettingsPanel)panelSettingsArima).setSpecificParams(ArimaParams.class, resultList);
        
        return resultList;
    }
    
//    private List<KNNcustomParams> getParamsKNNcustom() { //TODO multiple vals
//        KNNcustomParams params = new KNNcustomParams();
//        //zohnat vsetky parametre pre dany model:
//        params.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)panelKNNPercTrain).getPercentTrain()));
//        params.setColName(comboBoxColnamesRun.getSelectedItem().toString()); //data
//        params.setNumForecasts(FieldsParser.parseIntegers(textFieldRunNumForecasts).get(0));
//        params.setDataRangeFrom(Integer.parseInt(textFieldRunDataRangeFrom.getText()));
//        params.setDataRangeTo(Integer.parseInt(textFieldRunDataRangeTo.getText()));
//        //TODO setSpecific params
//        params.setNumNeighbours(Integer.parseInt(((KNNCustomSettingsPanel)panelSettingsKNNoptions_custom).getNumNeighbours()));
//        params.setLengthHistory(Integer.parseInt(((KNNCustomSettingsPanel)panelSettingsKNNoptions_custom).getLengthHistory()));
//        params.setLag(Integer.parseInt(((KNNCustomSettingsPanel)panelSettingsKNNoptions_custom).getLag()));
//        params.setDistanceMethodName(((KNNCustomSettingsPanel)panelSettingsKNNoptions_custom).getDistance());
//        params.setCombinationMethodName(((KNNCustomSettingsPanel)panelSettingsKNNoptions_custom).getCombination());
//        
//        List<KNNcustomParams> resultList = new ArrayList<>();
//        resultList.add(params);
//        return resultList;
//    }
    
    private List<KNNfnnParams> getParamsKNNfnn(javax.swing.JPanel percentTrainSettingsPanel,
            javax.swing.JComboBox comboBoxColName, javax.swing.JPanel panelSettingsKNNfnn) {
        KNNfnnParams par = new KNNfnnParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColName(comboBoxColName.getSelectedItem().toString()); //data
        
        List<KNNfnnParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        setParamsGeneral(KNNfnnParams.class, resultList);
        ((KNNFNNSettingsPanel)panelSettingsKNNfnn).setSpecificParams(KNNfnnParams.class, resultList);
        
        return resultList;
    }
    
    private List<KNNkknnParams> getParamsKNNkknn(javax.swing.JPanel percentTrainSettingsPanel,
            javax.swing.JComboBox comboBoxColName, javax.swing.JPanel panelSettingsKNNkknn) {
        KNNkknnParams par = new KNNkknnParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColName(comboBoxColName.getSelectedItem().toString()); //data
        
        List<KNNkknnParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        setParamsGeneral(KNNkknnParams.class, resultList);
        ((KNNkknnSettingsPanel)panelSettingsKNNkknn).setSpecificParams(KNNkknnParams.class, resultList);
        
        return resultList;
    }
    
    private List<VARParams> getParamsVAR(javax.swing.JComboBox comboBoxColName, javax.swing.JPanel panelSettingsVAR) throws IllegalArgumentException {
        VARParams par = new VARParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(100); //uses all data for training
        
        List<VARParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        setParamsGeneral(VARParams.class, resultList);
        ((VARSettingsPanel)panelSettingsVAR).setSpecificParams(VARParams.class, resultList);
        //TODO prehodit dnu?
        SettingsPanel.setSomethingOneValue(VARParams.class, resultList, "setOutputVarName",
                String.class, comboBoxColName.getSelectedItem().toString());
        SettingsPanel.setSomethingOneValue(VARParams.class, resultList, "setOutputVarVals",
                List.class, dataTableModel.getDataForColname(comboBoxColName.getSelectedItem().toString()));
        
        Map<String, List<Double>> data = new HashMap<>();
        for (Object var : ((VARSettingsPanel)panelSettingsVAR).getEndogenousVars()) {
            data.put(var.toString(), dataTableModel.getDataForColname(var.toString()));
        }
        SettingsPanel.setSomethingOneValue(VARParams.class, resultList, "setData", Map.class, data);
        
        return resultList;
    }
    
    private List<VARintParams> getParamsVARint(JPanel percentTrainSettingsPanel, JPanel distanceSettingsPanel, 
            JPanel panelSettingsVARint) throws IllegalArgumentException {
        VARintParams par = new VARintParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        
        List<VARintParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        setParamsGeneral(VARintParams.class, resultList);
        ((VARintSettingsPanel)panelSettingsVARint).setSpecificParams(VARintParams.class, resultList);
        ((DistanceSettingsPanel)distanceSettingsPanel).setSpecificParams(VARintParams.class, resultList);
        
        return resultList;
    }
    
    private List<RBFParams> getParamsRBF(javax.swing.JPanel percentTrainSettingsPanel,
            javax.swing.JComboBox comboBoxColName, javax.swing.JPanel panelSettingsRBF) throws IllegalArgumentException {
        RBFParams par = new RBFParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        
        List<RBFParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        setParamsGeneral(RBFParams.class, resultList);
        ((RBFSettingsPanel)panelSettingsRBF).setSpecificParams(RBFParams.class, resultList);
        //POZOR, OutVars sa nastavuju az tu vonku! TODO prerobit
        CrispOutputVariable outVar = new CrispOutputVariable(); //berie hodnoty z CTS Run
        outVar.setName(comboBoxColName.getSelectedItem().toString() + comboBoxColName.getSelectedIndex());
        outVar.setFieldName(comboBoxColName.getSelectedItem().toString());
        List<CrispOutputVariable> outVarList = new ArrayList<>();
        outVarList.add(outVar);
        SettingsPanel.setSomethingOneValue(RBFParams.class, resultList, "setOutVars", List.class, outVarList);
        
        return resultList;
    }
    
    private List<RBFintParams> getParamsRBFint(javax.swing.JPanel percentTrainSettingsPanel_center,
            javax.swing.JComboBox comboBoxColName_center, javax.swing.JPanel panelSettingsRBF_center,
            javax.swing.JPanel percentTrainSettingsPanel_radius, javax.swing.JComboBox comboBoxColName_radius, 
            javax.swing.JPanel panelSettingsRBF_radius, javax.swing.JPanel panelSettingsDistance,
            javax.swing.JTextField numNetsToTrainField,
            javax.swing.JPanel panelBestModelCriterion) throws IllegalArgumentException {
        List<RBFParams> resultListCenter = getParamsRBF(percentTrainSettingsPanel_center, comboBoxColName_center,
                panelSettingsRBF_center);
        List<RBFParams> resultListRadius = getParamsRBF(percentTrainSettingsPanel_radius, comboBoxColName_radius,
                panelSettingsRBF_radius);
        
        RBFintParams par = new RBFintParams();
        
        List<RBFintParams> resultList = new ArrayList<>();
        resultList.add(par);
        SettingsPanel.setSomethingList(RBFintParams.class, resultList, "setParamsCenter",
                RBFParams.class, resultListCenter);
        SettingsPanel.setSomethingList(RBFintParams.class, resultList, "setParamsRadius",
                RBFParams.class, resultListRadius);
        ((DistanceSettingsPanel)panelSettingsDistance).setSpecificParams(RBFintParams.class, resultList);
        SettingsPanel.setSomethingList(RBFintParams.class, resultList, "setNumNetsToTrain",
                Integer.class, FieldsParser.parseIntegers(numNetsToTrainField));
        SettingsPanel.setSomethingOneValue(RBFintParams.class, resultList, "setCriterion",
                Improvable.class, ((BestModelCriterionIntervalSettingsPanel)panelBestModelCriterion).getBestModelCriterion());
        
        return resultList;
    }
    
    private List<HybridParams> getParamsHybrid() throws IllegalArgumentException {
        HybridParams par = new HybridParams();
        List<HybridParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        switch (comboBoxSettingsHybridMethod_center.getSelectedItem().toString()) {
            case Const.NNETAR:
                List<NnetarParams> resultListCenterNnetar = getParamsNnetar(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntCenter, panelSettingsHybrid_centerMain_MLPnnetar);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        NnetarParams.class, resultListCenterNnetar);
                break;
            case Const.NNET:
                List<NnetParams> resultListCenterNnet = getParamsNnet(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntCenter, panelSettingsHybrid_centerMain_MLPnnet);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        NnetParams.class, resultListCenterNnet);
                break;
            case Const.RBF:
                List<RBFParams> resultListCenterRBF = getParamsRBF(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntCenter, panelSettingsHybrid_centerMain_RBF);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        RBFParams.class, resultListCenterRBF);
                break;
            case Const.ARIMA:
                List<ArimaParams> resultListCenterARIMA = getParamsArima(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntCenter, panelSettingsHybrid_centerMain_ARIMA);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        ArimaParams.class, resultListCenterARIMA);
                break;
            case Const.KNN_FNN:
                List<KNNfnnParams> resultListCenterKNNFNN = getParamsKNNfnn(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntCenter, panelSettingsHybrid_centerMain_KNNFNN);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        KNNfnnParams.class, resultListCenterKNNFNN);
                break;
            case Const.KNN_KKNN:
                List<KNNkknnParams> resultListCenterKNNkknn = getParamsKNNkknn(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntCenter, panelSettingsHybrid_centerMain_KNNkknn);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        KNNkknnParams.class, resultListCenterKNNkknn);
                break;
            case Const.SES:
                List<SESParams> resultListCenterSES = getParamsSES(panelSettingsHybridPercentTrain, 
                        comboBoxRunFakeIntCenter, panelSettingsHybrid_centerMain_SES);
                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        SESParams.class, resultListCenterSES);
                break;
            case Const.HOLT:
                List<HoltParams> resultListCenterHolt = getParamsHolt(panelSettingsHybridPercentTrain, 
                        panelSettingsHybrid_centerMain_Holt, comboBoxRunFakeIntCenter);
                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsCenter",
                        HoltParams.class, resultListCenterHolt);
                break;
            default:
                //do not do anything, this should never happen. maybe throw an exception? 
                break;
        }
        
        
        
        switch (comboBoxSettingsHybridMethod_radius.getSelectedItem().toString()) {
            case Const.NNETAR:
                List<NnetarParams> resultListRadiusNnetar = getParamsNnetar(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntRadius, panelSettingsHybrid_radiusMain_MLPnnetar);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        NnetarParams.class, resultListRadiusNnetar);
                break;
            case Const.NNET:
                List<NnetParams> resultListRadiusNnet = getParamsNnet(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntRadius, panelSettingsHybrid_radiusMain_MLPnnet);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        NnetParams.class, resultListRadiusNnet);
                break;
            case Const.RBF:
                List<RBFParams> resultListRadiusRBF = getParamsRBF(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntRadius, panelSettingsHybrid_radiusMain_RBF);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        RBFParams.class, resultListRadiusRBF);
                break;
            case Const.ARIMA:
                List<ArimaParams> resultListRadiusARIMA = getParamsArima(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntRadius, panelSettingsHybrid_radiusMain_ARIMA);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        ArimaParams.class, resultListRadiusARIMA);
                break;
            case Const.KNN_FNN:
                List<KNNfnnParams> resultListRadiusKNNFNN = getParamsKNNfnn(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntRadius, panelSettingsHybrid_radiusMain_KNNFNN);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        KNNfnnParams.class, resultListRadiusKNNFNN);
                break;
            case Const.KNN_KKNN:
                List<KNNkknnParams> resultListRadiusKNNkknn = getParamsKNNkknn(panelSettingsHybridPercentTrain,
                        comboBoxRunFakeIntRadius, panelSettingsHybrid_radiusMain_KNNkknn);
                                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        KNNkknnParams.class, resultListRadiusKNNkknn);
                break;
            case Const.SES:
                List<SESParams> resultListRadiusSES = getParamsSES(panelSettingsHybridPercentTrain, 
                        comboBoxRunFakeIntRadius, panelSettingsHybrid_radiusMain_SES);
                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        SESParams.class, resultListRadiusSES);
                break;
            case Const.HOLT:
                List<HoltParams> resultListRadiusHolt = getParamsHolt(panelSettingsHybridPercentTrain, 
                        panelSettingsHybrid_radiusMain_Holt, comboBoxRunFakeIntRadius);
                
                SettingsPanel.setSomethingListForHybrid(HybridParams.class, resultList, "setParamsRadius",
                        HoltParams.class, resultListRadiusHolt);
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
    
    private List<HoltParams> getParamsHolt(JPanel percentTrainSettingsPanel, JPanel panelSettingsHolt,
            JComboBox comboBoxColName) throws IllegalArgumentException {
        HoltParams par = new HoltParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColName(comboBoxColName.getSelectedItem().toString()); //data
        
        List<HoltParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        setParamsGeneral(HoltParams.class, resultList);
        ((HoltSettingsPanel)panelSettingsHolt).setSpecificParams(HoltParams.class, resultList);
        
        return resultList;
    }
    
    private List<HoltIntParams> getParamsHoltInt(JPanel percentTrainSettingsPanel_center, JPanel panelSettingsHolt_center,
                JComboBox comboBoxColName_center, JPanel percentTrainSettingsPanel_radius, JPanel panelSettingsHolt_radius,
                JComboBox comboBoxColName_radius, JPanel panelSettingsDistance) {
        List<HoltParams> resultListCenter = getParamsHolt(percentTrainSettingsPanel_center, panelSettingsHolt_center,
                comboBoxColName_center);
        List<HoltParams> resultListRadius = getParamsHolt(percentTrainSettingsPanel_radius, panelSettingsHolt_radius,
                comboBoxColName_radius);
        
        HoltIntParams par = new HoltIntParams();
        
        List<HoltIntParams> resultList = new ArrayList<>();
        resultList.add(par);
        SettingsPanel.setSomethingList(HoltIntParams.class, resultList, "setParamsCenter",
                HoltParams.class, resultListCenter);
        SettingsPanel.setSomethingList(HoltIntParams.class, resultList, "setParamsRadius",
                HoltParams.class, resultListRadius);
        ((DistanceSettingsPanel)panelSettingsDistance).setSpecificParams(HoltIntParams.class, resultList);
        
        return resultList;
    }
    
    private List<IntervalHoltParams> getParamsIntervalHolt(JPanel percentTrainSettingsPanel, JComboBox comboBoxCenter,
            JComboBox comboBoxRadius, JPanel distancePanel, JPanel panelSettingsHolt) {
        IntervalHoltParams par = new IntervalHoltParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColNameCenter(comboBoxCenter.getSelectedItem().toString()); //data
        par.setColNameRadius(comboBoxRadius.getSelectedItem().toString());
        
        List<IntervalHoltParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        setParamsGeneral(IntervalHoltParams.class, resultList);
        ((IntHoltSettingsPanel)panelSettingsHolt).setSpecificParams(IntervalHoltParams.class, resultList);
        ((DistanceSettingsPanel)distancePanel).setSpecificParams(IntervalHoltParams.class, resultList);
        
        return resultList;
    }
    
    private List<SESParams> getParamsSES(JPanel percentTrainSettingsPanel, JComboBox comboBoxColname, JPanel main) {
        SESParams par = new SESParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColName(comboBoxColname.getSelectedItem().toString()); //data
        
        List<SESParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        setParamsGeneral(SESParams.class, resultList);
        ((SESSettingsPanel)main).setSpecificParams(SESParams.class, resultList);
        
        return resultList;
    }
    
    private List<SESintParams> getParamsSESint(JPanel percentTrainSettingsPanel_center, JPanel panelSettingsSES_center,
                JComboBox comboBoxColName_center, JPanel percentTrainSettingsPanel_radius, JPanel panelSettingsSES_radius,
                JComboBox comboBoxColName_radius, JPanel panelSettingsDistance) {
        List<SESParams> resultListCenter = getParamsSES(percentTrainSettingsPanel_center, comboBoxColName_center,
                panelSettingsSES_center);
        List<SESParams> resultListRadius = getParamsSES(percentTrainSettingsPanel_radius, comboBoxColName_radius,
                panelSettingsSES_radius);
        
        SESintParams par = new SESintParams();
        
        List<SESintParams> resultList = new ArrayList<>();
        resultList.add(par);
        SettingsPanel.setSomethingList(SESintParams.class, resultList, "setParamsCenter",
                SESParams.class, resultListCenter);
        SettingsPanel.setSomethingList(SESintParams.class, resultList, "setParamsRadius",
                SESParams.class, resultListRadius);
        ((DistanceSettingsPanel)panelSettingsDistance).setSpecificParams(SESintParams.class, resultList);
        
        return resultList;
    }
    
    private List<HoltWintersParams> getParamsHoltWinters(JPanel percentTrainSettingsPanel, JPanel panelSettingsHoltWint,
            JComboBox comboBoxColName) throws IllegalArgumentException {
        HoltWintersParams par = new HoltWintersParams();
        //zohnat vsetky parametre pre dany model:
        par.setPercentTrain(Integer.parseInt(((PercentTrainSettingsPanel)percentTrainSettingsPanel).getPercentTrain()));
        par.setColName(comboBoxColName.getSelectedItem().toString()); //data
        
        List<HoltWintersParams> resultList = new ArrayList<>();
        resultList.add(par);
        
        setParamsGeneral(HoltWintersParams.class, resultList);
        ((HoltWintersSettingsPanel)panelSettingsHoltWint).setSpecificParams(HoltWintersParams.class, resultList);
        
        return resultList;
    }
    
    private List<HoltWintersIntParams> getParamsHoltWintersInt(JPanel percentTrainSettingsPanel_center, 
                JPanel panelSettingsHolt_center, JComboBox comboBoxColName_center, JPanel percentTrainSettingsPanel_radius, 
                JPanel panelSettingsHolt_radius, JComboBox comboBoxColName_radius, JPanel panelSettingsDistance) {
        List<HoltWintersParams> resultListCenter = getParamsHoltWinters(percentTrainSettingsPanel_center, 
                panelSettingsHolt_center, comboBoxColName_center);
        List<HoltWintersParams> resultListRadius = getParamsHoltWinters(percentTrainSettingsPanel_radius, 
                panelSettingsHolt_radius, comboBoxColName_radius);
        
        HoltWintersIntParams par = new HoltWintersIntParams();
        
        List<HoltWintersIntParams> resultList = new ArrayList<>();
        resultList.add(par);
        SettingsPanel.setSomethingList(HoltWintersIntParams.class, resultList, "setParamsCenter",
                HoltWintersParams.class, resultListCenter);
        SettingsPanel.setSomethingList(HoltWintersIntParams.class, resultList, "setParamsRadius",
                HoltWintersParams.class, resultListRadius);
        ((DistanceSettingsPanel)panelSettingsDistance).setSpecificParams(HoltWintersIntParams.class, resultList);
        
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
        
        List<TrainAndTestReportCrisp> reportsCTS = new ArrayList<>();
        reportsCTS.addAll(rCTS);
        List<TrainAndTestReportInterval> reportsIntTS = new ArrayList<>();
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
            errorMeasuresTable_CTS.setDefaultRenderer(Object.class, new TableBothHeadersCellColorRenderer());
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
            errorMeasuresTable_ITS.setDefaultRenderer(Object.class, new TableBothHeadersCellColorRenderer());
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
                errorMeasuresTable_CTS.setDefaultRenderer(Object.class, new TableBothHeadersCellColorRenderer());
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
                    errorMeasuresTable_ITS.setDefaultRenderer(Object.class, new TableBothHeadersCellColorRenderer());
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

    private void setPlotRanges(int sizeCTS, int sizeIntTS) {
        if (sizeCTS == 0) {
            enableZoomPlotCTS(false);
        } else {
            enableZoomPlotCTS(true);
            textFieldPlotRangeCTSXfrom.setText("" + PlotStateKeeper.getLastDrawnCrispXmin());
            textFieldPlotRangeCTSXto.setText("" + PlotStateKeeper.getLastDrawnCrispXmax());
            textFieldPlotRangeCTSYfrom.setText("" + PlotStateKeeper.getLastDrawnCrispYmin());
            textFieldPlotRangeCTSYto.setText("" + PlotStateKeeper.getLastDrawnCrispYmax());
        }
        
        if (sizeIntTS == 0) {
            enableZoomPlotIntTS(false);
        } else {
            enableZoomPlotIntTS(true);
            textFieldPlotRangeIntTSXfrom.setText("" + PlotStateKeeper.getLastDrawnIntXmin());
            textFieldPlotRangeIntTSXto.setText("" + PlotStateKeeper.getLastDrawnIntXmax());
            textFieldPlotRangeIntTSYfrom.setText("" + PlotStateKeeper.getLastDrawnIntYmin());
            textFieldPlotRangeIntTSYto.setText("" + PlotStateKeeper.getLastDrawnIntYmax());
        }
    }
    
    private void enableZoomPlotCTS(boolean trueFalse) {
        textFieldPlotRangeCTSXfrom.setEnabled(trueFalse);
        textFieldPlotRangeCTSXto.setEnabled(trueFalse);
        textFieldPlotRangeCTSYfrom.setEnabled(trueFalse);
        textFieldPlotRangeCTSYto.setEnabled(trueFalse);
        buttonPlotRestoreCTSRangeX.setEnabled(trueFalse);
        buttonPlotRestoreCTSRangeY.setEnabled(trueFalse);
        buttonPlotZoomCTS.setEnabled(trueFalse);
    }
    
    private void enableZoomPlotIntTS(boolean trueFalse) {
        textFieldPlotRangeIntTSXfrom.setEnabled(trueFalse);
        textFieldPlotRangeIntTSXto.setEnabled(trueFalse);
        textFieldPlotRangeIntTSYfrom.setEnabled(trueFalse);
        textFieldPlotRangeIntTSYto.setEnabled(trueFalse);
        buttonPlotRestoreIntTSRangeX.setEnabled(trueFalse);
        buttonPlotRestoreIntTSRangeY.setEnabled(trueFalse);
        buttonPlotZoomIntTS.setEnabled(trueFalse);
    }

    private Distance getSelectedDistance(JComboBox comboBoxDistance, JTextField euclid_beta, JTextField ichino_gamma, JTextField decarvalho_gamma, JTextField bertoluzza_beta) {
        switch (comboBoxDistance.getSelectedItem().toString()) {
            case "Euclidean distance":
                double beta = Double.parseDouble(euclid_beta.getText());
                return new WeightedEuclideanDistance(beta);
            case "Hausdorff distance":
                return new HausdorffDistance();
            case "Ichino-Yaguchi distance":
                double gamma = Double.parseDouble(ichino_gamma.getText());
                return new IchinoYaguchiDistance(gamma);
            case "De Carvalho distance":
                gamma = Double.parseDouble(decarvalho_gamma.getText());
                return new DeCarvalhoDistance(gamma);
            case "Bertoluzza distance":
                gamma = Double.parseDouble(bertoluzza_beta.getText());
                return new BertoluzzaDistance(gamma);
            default:
                return null;
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
}
