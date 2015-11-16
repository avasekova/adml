package gui;

import models.TrainAndTestReport;
import org.kohsuke.args4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rmi.AdmlProviderImpl;
import rmi.AdmlRegistry;
import rmi.AdmlWorkerImpl;

import javax.swing.JFrame;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // receives other command line parameters than options
    @Argument
    private List<String> arguments = new ArrayList<String>(8);

    @Option(name="--rmi", aliases={"-r"}, usage = "If set, RMI is started in the server mode")
    private boolean rmi = false;

    @Option(name="--worker", aliases={"-w"}, usage = "Start application in the RMI worker mode, no GUI.")
    private boolean workerMode = false;

    @Option(name="--rmiregistry-connect", aliases={"-c"}, usage = "RMI registry to connect to. If specified, for server " +
            "registry is not created, this one is used")
    private String rmiRegistryConnect = null;

    @Option(name="--rmiregistry-only", aliases={"-o"}, usage = "Used only to start RMI registry and loop forever. Starts registry " +
            "on localhost.")
    private boolean rmiRegistryOnly = false;

    private AdmlRegistry registry;
    private AdmlProviderImpl<TrainAndTestReport> server;
    private MainFrame gui;

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

            // Registry only?
            if (rmiRegistryOnly){
                rmi = true;
                if (!startRegistryAndWork()){
                    logger.error("Registry start failed");
                    return;
                }
                System.exit(0);
            }

            // Worker vs. master.
            if (workerMode){
                // Worker mode.
                rmi = true;
                logger.info("Starting in the worker mode");
                if (rmiRegistryConnect == null){
                    rmiRegistryConnect = "localhost";
                }

                AdmlWorkerImpl<TrainAndTestReport> worker = new AdmlWorkerImpl<>(rmiRegistryConnect, AdmlProviderImpl.NAME);
                worker.work();
                System.exit(0);

            } else {
                // Server + main application
                gui =  MainFrame.getInstance();
                if (rmi || rmiRegistryConnect != null) {
                    // If registry hostname to connect is not null, do not create own but use existing
                    registry = new AdmlRegistry();
                    if (rmiRegistryConnect != null && !rmiRegistryConnect.isEmpty()){
                        logger.info("Connecting to the RMI registry at {}", rmiRegistryConnect);
                        registry.lookupRegistry(rmiRegistryConnect);

                    } else {
                        logger.info("Creating new local RMI registry");
                        registry.createRegistry();

                    }

                    logger.info("Binding RMI server");
                    server = new AdmlProviderImpl<>();
                    server.setRegistry(registry.getRegistry());
                    server.setJobFinishedListener(gui);
                    server.initServer();
                }

                gui.setServer(server);

                // Not a worker mode -> start GUI
                startApplicationGUI();
            }

        } catch (CmdLineException e) {
            logger.error("Exception during parsing command line arguments");
            cmdLineParser.printUsage(System.err);

        } catch (RemoteException e){
            logger.error("RMI exception", e);

        } catch (Exception e){
            logger.error("General exception", e);
        }
    }

    /**
     * Starts localhost registry.
     */
    protected boolean startLocalRegistry(){
        registry = new AdmlRegistry();
        try {
            logger.info("Starting local RMI registry");
            registry.createRegistry();
            return true;

        } catch (RemoteException e) {
            logger.error("Registry starting error");
        }

        return false;
    }

    /**
     * Starts standalone registry and blocks forever.
     */
    protected boolean startRegistryAndWork(){
        final boolean success = startLocalRegistry();
        if (!success){
            return false;
        }

        logger.info("Registry started, going to loop forever");
        while(true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.info("Registry loop interrupted");
                return true;
            }
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
            logger.error("Exception", ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.setExtendedState(JFrame.MAXIMIZED_BOTH); //maximize the window
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException ex) {
                    logger.error("Exception", ex);
                }

                gui.setVisible(true);
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
