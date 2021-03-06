package gui;

import com.klinec.admwl.remoteLogic.AdmwlProviderImpl;
import com.klinec.admwl.remoteLogic.AdmwlRegistry;
import com.klinec.admwl.remoteLogic.AdmwlWorkerImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import models.TrainAndTestReport;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String SVCNAME = "ADMWL";

    // receives other command line parameters than options
    @Argument
    private List<String> arguments = new ArrayList<>(8);

    @Option(name="--rmi", aliases={"-r"}, usage = "If set, RMI is started in the server mode")
    private boolean rmi = false;

    @Option(name="--worker-spawn", aliases={"-n"}, usage = "Number of workers to be spawned with the manager process. " +
            "Use only to start additional workers automatically with manager. Ignored in worker case.")
    private int workerSpawn = 0;

    @Option(name="--worker", aliases={"-w"}, usage = "Start application in the RMI worker mode, no GUI.")
    private boolean workerMode = false;

    @Option(name="--rmiregistry-connect", aliases={"-c"}, usage = "RMI registry to connect to. If specified, for server " +
            "registry is not created, this one is used")
    private String rmiRegistryConnect = null;

    @Option(name="--rmiregistry-only", aliases={"-o"}, usage = "Used only to start RMI registry and loop forever. Starts registry " +
            "on localhost.")
    private boolean rmiRegistryOnly = false;

    @Option(name="--rmiregistry-port", aliases={"-p"}, usage = "RMI registry port to use. If negative, the default one is used")
    private int rmiRegistryPort = -1;

    private AdmwlRegistry registry;
    private AdmwlProviderImpl<TrainAndTestReport> server;
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

            // Worker
            if (workerMode){
                workerLogic();
                return;
            }

            // Server + main application
            gui =  MainFrame.getInstance();
            if (rmi || rmiRegistryConnect != null) {
                registry = new AdmwlRegistry();

                // If registry hostname to connect is not null, do not create own but use existing
                if (rmiRegistryConnect != null && !rmiRegistryConnect.isEmpty()){
                    logger.info("Connecting to the RMI registry at {}, port: {}",
                            rmiRegistryConnect, rmiRegistryPort <= 0 ? "Default" : rmiRegistryPort);

                    registry.lookupRegistry(rmiRegistryConnect, rmiRegistryPort);

                } else {
                    // Create a new local host registry.
                    logger.info("Creating new local RMI registry, port: {}", rmiRegistryPort <= 0 ? "Default" : rmiRegistryPort);
                    registry.createRegistry(rmiRegistryPort);

                }

                logger.info("Binding RMI server");
                server = new AdmwlProviderImpl<>(SVCNAME);
                server.setRegistry(registry.getRegistry());
                server.setJobFinishedListener(gui);
                server.initServer();

                // Spawn some worker processes?
                spawnWorkers();
            }

            gui.setServer(server);

            // Not a worker mode -> start GUI
            startApplicationGUI();

        } catch (CmdLineException e) {
            logger.error("Exception during parsing command line arguments");
            cmdLineParser.printUsage(System.err);

        } catch (AccessException | AccessControlException e){
            logger.error("Access exception", e);
            logger.error("Security or Access exception indicates you may have forgotten to" +
                    " you use -Djava.security.policy=java.policy for starting the program. Please check the policy");

        } catch (RemoteException e) {
            logger.error("RMI exception", e);

        } catch (Exception e){
            logger.error("General exception", e);
        }
    }

    /**
     * Acting as a worker.
     *
     * @throws RemoteException
     * @throws NotBoundException
     * @throws MalformedURLException
     */
    protected void workerLogic() throws RemoteException, NotBoundException, MalformedURLException {
        rmi = true;
        logger.info("Starting in the worker mode");
        if (rmiRegistryConnect == null){
            rmiRegistryConnect = "localhost";
        }

        AdmwlWorkerImpl<TrainAndTestReport> worker = new AdmwlWorkerImpl<>(rmiRegistryConnect, rmiRegistryPort, SVCNAME);

        // Starts worker loops, blocks until worker is running.
        worker.work();
        System.exit(0);
    }

    /**
     * Spawn additional workers if enabled in options.
     */
    private void spawnWorkers(){
        if (workerSpawn <= 0){
            return;
        }

        final File f = new File(System.getProperty("java.class.path"));
        final File dir = f.getAbsoluteFile().getParentFile();
        final String path = dir.toString();
        logger.info("Class path {}", path);

        for (int i = 0; i < workerSpawn; i++) {
            try {
                logger.info("Starting worker process {}/{}", i+1, workerSpawn);

                ProcessBuilder pb = new ProcessBuilder(
                        "java",
                        "-Djava.security.policy="+path+"/../java.policy",
                        "-jar",
                        f.getAbsolutePath(),
                        "--worker");

                Process p = pb.start();

            } catch (Exception e) {
                logger.error("Exception in starting a worker", e);
            }
        }
    }

    /**
     * Starts localhost registry.
     */
    protected boolean startLocalRegistry(){
        registry = new AdmwlRegistry();
        try {
            logger.info("Starting local RMI registry, port: {}", rmiRegistryPort <= 0 ? "Default" : rmiRegistryPort);
            registry.createRegistry(rmiRegistryPort);
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
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(gui);
            gui.pack();

            Platform.setImplicitExit(false);//if true, the JavaFX runtime will implicitly shutdown on last window closed
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


    @Override
    public void start(Stage primaryStage) throws Exception { //just so JavaFX environment initializes
    }
}
