package gui.subpanels;

import gui.MainFrame;
import gui.PlotContainer;
import gui.PlotDrawer;
import gui.Plottable;
import gui.filefilters.FileFilterEps;
import gui.filefilters.FileFilterPdf;
import gui.filefilters.FileFilterPng;
import gui.filefilters.FileFilterPs;
import gui.filefilters.RFileFilter;
import gui.files.PlotExtensionFileChooser;
import gui.renderers.PlotLegendSimpleListElement;
import gui.renderers.PlotLegendTurnOFFableListCellRenderer;
import gui.renderers.PlotLegendTurnOFFableListElement;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import models.Model;
import models.TrainAndTestReport;
import org.rosuda.javaGD.JGDBufferedPanel;
import utils.Const;
import utils.MyRengine;
import utils.ugliez.CallParamsDrawPlotGeneral;
import utils.ugliez.CallParamsDrawPlots;
import utils.ugliez.CallParamsDrawPlotsITS;
import utils.ugliez.PlotStateKeeper;

public class PlotSubPanel extends javax.swing.JPanel implements PlotContainer {

    private JGDBufferedPanel gdBufferedPanelPlot;


    public PlotSubPanel() {
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

        buttonPlotExportPlot.setText("Save currently shown plot");
        buttonPlotExportPlot.setEnabled(false);
        buttonPlotExportPlot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlotExportPlotActionPerformed(evt);
            }
        });

        gdBufferedPanelPlot = new JGDBufferedPanel(panelPlot.getWidth(), panelPlot.getHeight());
        panelPlot.add(gdBufferedPanelPlot);

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
                if (listPlotLegend.getModel()
                    .getElementAt(listPlotLegend.getSelectedIndex()) instanceof PlotLegendTurnOFFableListElement) {
                    ((PlotLegendTurnOFFableListElement) listPlotLegend.getModel()
                        .getElementAt(listPlotLegend.getSelectedIndex())).dispatchEvent(e);
                } else {
                    if (listPlotLegend.getModel()
                        .getElementAt(listPlotLegend.getSelectedIndex()) instanceof PlotLegendSimpleListElement) {
                        ((PlotLegendSimpleListElement) listPlotLegend.getModel()
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
                            .addComponent(buttonPlotExportPlot)
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel127)
                                .addComponent(jLabel89))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(textFieldPlotRangeCTSXfrom)
                                .addComponent(textFieldPlotRangeCTSYfrom, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel126)
                                .addComponent(jLabel128))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(textFieldPlotRangeCTSYto)
                                .addComponent(textFieldPlotRangeCTSXto, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(buttonPlotRestoreCTSRangeX)
                                .addComponent(buttonPlotRestoreCTSRangeY))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(buttonPlotZoomCTS)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel130)
                                .addComponent(jLabel129))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(textFieldPlotRangeIntTSXfrom)
                                .addComponent(textFieldPlotRangeIntTSYfrom, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel131)
                                .addComponent(jLabel132))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(textFieldPlotRangeIntTSYto)
                                .addComponent(textFieldPlotRangeIntTSXto, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(buttonPlotRestoreIntTSRangeX)
                                .addComponent(buttonPlotRestoreIntTSRangeY))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(buttonPlotZoomIntTS)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(buttonLegendSelectNone, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(scrollPaneListPlotLegend, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(buttonLegendSelectAll)
                                    .addGap(12, 12, 12))))
                        .addComponent(panelPlot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(buttonLegendSelectAll)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(buttonLegendSelectNone))
                        .addComponent(buttonPlotExportPlot)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(buttonPlotRestoreCTSRangeX)
                                    .addComponent(textFieldPlotRangeCTSXto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(buttonPlotRestoreCTSRangeY)
                                    .addComponent(jLabel127)
                                    .addComponent(textFieldPlotRangeCTSYfrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel128)
                                    .addComponent(textFieldPlotRangeCTSYto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(buttonPlotZoomCTS)
                                .addGap(15, 15, 15)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldPlotRangeCTSXfrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel126))
                        .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonPlotRestoreIntTSRangeX)
                                .addComponent(textFieldPlotRangeIntTSXto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(textFieldPlotRangeIntTSXfrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel131)
                                .addComponent(jLabel129, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonPlotRestoreIntTSRangeY)
                                .addComponent(textFieldPlotRangeIntTSYto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(textFieldPlotRangeIntTSYfrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel132)
                                .addComponent(jLabel130)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addComponent(buttonPlotZoomIntTS))
                        .addComponent(scrollPaneListPlotLegend, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(panelPlot, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

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
                        //logger.error("Exception", ex);
                    }
                    break;
                    case JFileChooser.CANCEL_OPTION:
                    default:
                    //nothing
                }
            }
    }//GEN-LAST:event_buttonPlotExportPlotActionPerformed

    private void buttonPlotRestoreCTSRangeXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotRestoreCTSRangeXActionPerformed
        textFieldPlotRangeCTSXfrom.setText("" + MainFrame.getInstance().getTextFieldRunDataRangeFrom().getText());
        int upperBound = Integer.parseInt(MainFrame.getInstance().getTextFieldRunDataRangeTo().getText()) +
        Integer.parseInt(MainFrame.getInstance().getTextFieldRunNumForecasts().getText());
        textFieldPlotRangeCTSXto.setText("" + upperBound);
    }//GEN-LAST:event_buttonPlotRestoreCTSRangeXActionPerformed

    private void buttonPlotRestoreCTSRangeYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotRestoreCTSRangeYActionPerformed
        textFieldPlotRangeCTSYfrom.setText("" + PlotStateKeeper.getCrispYmin());
        textFieldPlotRangeCTSYto.setText("" + PlotStateKeeper.getCrispYmax());
    }//GEN-LAST:event_buttonPlotRestoreCTSRangeYActionPerformed

    private void buttonPlotZoomCTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotZoomCTSActionPerformed
        if (PlotStateKeeper.getLastCallParams() instanceof CallParamsDrawPlots) {
            String rangeXCrisp = "range(c(" + textFieldPlotRangeCTSXfrom.getText() + "," + textFieldPlotRangeCTSXto.getText() + "))";
            String rangeYCrisp = "range(c(" + textFieldPlotRangeCTSYfrom.getText() + "," + textFieldPlotRangeCTSYto.getText() + "))";
            String rangeXInt = "range(c(" + PlotStateKeeper.getLastDrawnIntXmin() + "," + PlotStateKeeper.getLastDrawnIntXmax() + "))";
            String rangeYInt = "range(c(" + PlotStateKeeper.getLastDrawnIntYmin() + "," + PlotStateKeeper.getLastDrawnIntYmax() + "))";

            PlotDrawer.drawPlots(Const.MODE_DRAW_ZOOM_ONLY, Const.MODE_REFRESH_NO,
                (CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams()), rangeXCrisp , rangeYCrisp, rangeXInt, rangeYInt);
            MainFrame.getInstance().setPlotRanges(((CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams())).getReportsCTS().size(),
                ((CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams())).getReportsITS().size());
        } else if (PlotStateKeeper.getLastCallParams() instanceof CallParamsDrawPlotGeneral) {
            String rangeXCrisp = "range(c(" + textFieldPlotRangeCTSXfrom.getText() + "," + textFieldPlotRangeCTSXto.getText() + "))";
            String rangeYCrisp = "range(c(" + textFieldPlotRangeCTSYfrom.getText() + "," + textFieldPlotRangeCTSYto.getText() + "))";

            PlotDrawer.drawPlotGeneral(false, (CallParamsDrawPlotGeneral)(PlotStateKeeper.getLastCallParams()), rangeXCrisp, rangeYCrisp);
            MainFrame.getInstance().setPlotRanges(1, 0); //hack - cokolvek ine ako nula na prvom mieste
        }
    }//GEN-LAST:event_buttonPlotZoomCTSActionPerformed

    private void buttonPlotRestoreIntTSRangeXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotRestoreIntTSRangeXActionPerformed
        if ((PlotStateKeeper.getLastCallParams() instanceof CallParamsDrawPlotsITS) &&
            (((CallParamsDrawPlotsITS)PlotStateKeeper.getLastCallParams()).isScatterplot())) {
            //TODO
            MainFrame.getInstance().getTextAreaPlotBasicStats().setText("The scatterplot does not support restoring the original range yet.");
        } else {
            textFieldPlotRangeIntTSXfrom.setText("" + MainFrame.getInstance().getTextFieldRunDataRangeFrom().getText());
            int upperBound = Integer.parseInt(MainFrame.getInstance().getTextFieldRunDataRangeTo().getText()) +
            Integer.parseInt(MainFrame.getInstance().getTextFieldRunNumForecasts().getText());
            textFieldPlotRangeIntTSXto.setText("" + upperBound);
        }
    }//GEN-LAST:event_buttonPlotRestoreIntTSRangeXActionPerformed

    private void buttonPlotRestoreIntTSRangeYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotRestoreIntTSRangeYActionPerformed
        if ((PlotStateKeeper.getLastCallParams() instanceof CallParamsDrawPlotsITS) &&
            (((CallParamsDrawPlotsITS)PlotStateKeeper.getLastCallParams()).isScatterplot())) {
            //TODO
            MainFrame.getInstance().getTextAreaPlotBasicStats().setText("The scatterplot does not support restoring the original range yet.");
        } else {
            textFieldPlotRangeIntTSYfrom.setText("" + PlotStateKeeper.getIntYmin());
            textFieldPlotRangeIntTSYto.setText("" + PlotStateKeeper.getIntYmax());
        }
    }//GEN-LAST:event_buttonPlotRestoreIntTSRangeYActionPerformed

    private void buttonPlotZoomIntTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlotZoomIntTSActionPerformed
        if (PlotStateKeeper.getLastCallParams() instanceof CallParamsDrawPlots) {
            String rangeXCrisp = "range(c(" + PlotStateKeeper.getLastDrawnCrispXmin() + "," + PlotStateKeeper.getLastDrawnCrispXmax() + "))";
            String rangeYCrisp = "range(c(" + PlotStateKeeper.getLastDrawnCrispYmin() + "," + PlotStateKeeper.getLastDrawnCrispYmax() + "))";
            String rangeXInt = "range(c(" + textFieldPlotRangeIntTSXfrom.getText() + "," + textFieldPlotRangeIntTSXto.getText() + "))";
            String rangeYInt = "range(c(" + textFieldPlotRangeIntTSYfrom.getText() + "," + textFieldPlotRangeIntTSYto.getText() + "))";

            PlotDrawer.drawPlots(Const.MODE_DRAW_ZOOM_ONLY, Const.MODE_REFRESH_NO,
                (CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams()), rangeXCrisp , rangeYCrisp, rangeXInt, rangeYInt);
            MainFrame.getInstance().setPlotRanges(((CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams())).getReportsCTS().size(),
                ((CallParamsDrawPlots)(PlotStateKeeper.getLastCallParams())).getReportsITS().size());
        } else if (PlotStateKeeper.getLastCallParams() instanceof CallParamsDrawPlotsITS) {
            String rangeXInt = "range(c(" + textFieldPlotRangeIntTSXfrom.getText() + "," + textFieldPlotRangeIntTSXto.getText() + "))";
            String rangeYInt = "range(c(" + textFieldPlotRangeIntTSYfrom.getText() + "," + textFieldPlotRangeIntTSYto.getText() + "))";

            if (((CallParamsDrawPlotsITS)(PlotStateKeeper.getLastCallParams())).isScatterplot()) {
                PlotDrawer.drawScatterPlotsITS(false, (CallParamsDrawPlotsITS)(PlotStateKeeper.getLastCallParams()), rangeXInt, rangeYInt);
            } else {
                PlotDrawer.drawPlotsITS(false, (CallParamsDrawPlotsITS)(PlotStateKeeper.getLastCallParams()), rangeXInt, rangeYInt);
            }
            MainFrame.getInstance().setPlotRanges(0, 1); //hack, cokolvek ine ako 0 znamena enable
        }
    }//GEN-LAST:event_buttonPlotZoomIntTSActionPerformed

    private void buttonLegendSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLegendSelectAllActionPerformed
        selectAllOrNone(Model.ALL);
    }//GEN-LAST:event_buttonLegendSelectAllActionPerformed

    private void buttonLegendSelectNoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLegendSelectNoneActionPerformed
        selectAllOrNone(Model.NONE);
    }//GEN-LAST:event_buttonLegendSelectNoneActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonLegendSelectAll;
    private javax.swing.JButton buttonLegendSelectNone;
    private javax.swing.JButton buttonPlotExportPlot;
    private javax.swing.JButton buttonPlotRestoreCTSRangeX;
    private javax.swing.JButton buttonPlotRestoreCTSRangeY;
    private javax.swing.JButton buttonPlotRestoreIntTSRangeX;
    private javax.swing.JButton buttonPlotRestoreIntTSRangeY;
    private javax.swing.JButton buttonPlotZoomCTS;
    private javax.swing.JButton buttonPlotZoomIntTS;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JList listPlotLegend;
    private javax.swing.JPanel panelPlot;
    private javax.swing.JScrollPane scrollPaneListPlotLegend;
    private javax.swing.JTextField textFieldPlotRangeCTSXfrom;
    private javax.swing.JTextField textFieldPlotRangeCTSXto;
    private javax.swing.JTextField textFieldPlotRangeCTSYfrom;
    private javax.swing.JTextField textFieldPlotRangeCTSYto;
    private javax.swing.JTextField textFieldPlotRangeIntTSXfrom;
    private javax.swing.JTextField textFieldPlotRangeIntTSXto;
    private javax.swing.JTextField textFieldPlotRangeIntTSYfrom;
    private javax.swing.JTextField textFieldPlotRangeIntTSYto;
    // End of variables declaration//GEN-END:variables


    private void selectAllOrNone(Model selectWhat) {
        if (listPlotLegend.getCellRenderer() instanceof PlotLegendTurnOFFableListCellRenderer) {
            //fuuuj, to je hnusny sposob zistovania, ci to je ta legenda :/ TODO prerobit
            DefaultListModel model = (DefaultListModel)listPlotLegend.getModel();
            
            switch (selectWhat) {
                case NONE:
                    for (int i = 0; i < model.size(); i++) {
                        Plottable p = ((PlotLegendTurnOFFableListElement)model.getElementAt(i)).getReport();
                        if ((p instanceof TrainAndTestReport) && (! ((TrainAndTestReport)p).isAverage())) {
                            p.setVisible(false);
                        }
                    }   break;
                case ALL:
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

    public JList getListPlotLegend() {
        return listPlotLegend;
    }

    public JPanel getPanelPlot() {
        return panelPlot;
    }

    public JTextField getTextFieldPlotRangeCTSXfrom() {
        return textFieldPlotRangeCTSXfrom;
    }

    public JTextField getTextFieldPlotRangeCTSXto() {
        return textFieldPlotRangeCTSXto;
    }

    public JTextField getTextFieldPlotRangeCTSYfrom() {
        return textFieldPlotRangeCTSYfrom;
    }

    public JTextField getTextFieldPlotRangeCTSYto() {
        return textFieldPlotRangeCTSYto;
    }

    public JTextField getTextFieldPlotRangeIntTSXfrom() {
        return textFieldPlotRangeIntTSXfrom;
    }

    public JTextField getTextFieldPlotRangeIntTSXto() {
        return textFieldPlotRangeIntTSXto;
    }

    public JTextField getTextFieldPlotRangeIntTSYfrom() {
        return textFieldPlotRangeIntTSYfrom;
    }

    public JTextField getTextFieldPlotRangeIntTSYto() {
        return textFieldPlotRangeIntTSYto;
    }

    public JButton getButtonLegendSelectAll() {
        return buttonLegendSelectAll;
    }

    public JButton getButtonLegendSelectNone() {
        return buttonLegendSelectNone;
    }

    public JButton getButtonPlotExportPlot() {
        return buttonPlotExportPlot;
    }

    public JButton getButtonPlotRestoreCTSRangeX() {
        return buttonPlotRestoreCTSRangeX;
    }

    public JButton getButtonPlotRestoreCTSRangeY() {
        return buttonPlotRestoreCTSRangeY;
    }

    public JButton getButtonPlotRestoreIntTSRangeX() {
        return buttonPlotRestoreIntTSRangeX;
    }

    public JButton getButtonPlotRestoreIntTSRangeY() {
        return buttonPlotRestoreIntTSRangeY;
    }

    public JButton getButtonPlotZoomCTS() {
        return buttonPlotZoomCTS;
    }

    public JButton getButtonPlotZoomIntTS() {
        return buttonPlotZoomIntTS;
    }


    @Override
    public void setPlots(List<JGDBufferedPanel> plots) {
        //TODO ak ich bude nahodou viac? zahodit, ci..?
        //TODO plus ak tam nebude nic
        panelPlot.removeAll();
        panelPlot.add(plots.get(0));  //pri troche stastia ani netreba atribut na GDPlot, TODO potom
        panelPlot.repaint();
    }
}
