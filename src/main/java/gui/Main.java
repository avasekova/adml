package gui;

import org.kohsuke.args4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // receives other command line parameters than options
    @Argument
    private List<String> arguments = new ArrayList<String>(8);

    @Option(name="--worker", aliases={"-w"}, usage = "Start application in the worker mode, no GUI.")
    private boolean workerMode = false;

    /**
     * Command line arguments parser.
     */
    private CmdLineParser cmdLineParser;

    public static void main(String args[]) {
        logger.info("Starting the application");

        final Main app = new Main();
        app.startApplication(args);
    }

    /**
     * Main entry point for starting the application.
     * @param args
     */
    protected void startApplication(String args[]){
        try {
            // Parse command line arguments.
            parseArgs(args);

            if (workerMode){
                logger.error("Worker mode not implemented yet");
                System.exit(2);

            } else {
                // Not a worker mode -> start GUI
                startApplicationGUI();
            }

        } catch (CmdLineException e) {
            logger.error("Exception during parsing command line arguments");

            // print option sample. This is useful some time
            System.err.println(cmdLineParser.printExample(OptionHandlerFilter.ALL, null));
        }
    }

    /**
     * Starts application GUI.
     */
    protected void startApplicationGUI(){
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

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame.getInstance().setExtendedState(JFrame.MAXIMIZED_BOTH); //maximize the window
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException ex) {
                    logger.error("Exception", ex);
                }

                MainFrame.getInstance().setVisible(true);
            }
        });
    }

    /**
     * Parse command line arguments, fills class attributes.
     * @param args argument list to parse
     */
    protected void parseArgs(String args[]) throws CmdLineException {
        // command line argument parser
        if (cmdLineParser == null) {
            cmdLineParser = new CmdLineParser(this);
        }

        cmdLineParser.parseArgument(args);
    }
    
}
