package rmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Simple implementation by
 * Created by dusanklinec on 15.11.15.
 */
public class AdmlWorkerImpl implements AdmlWorker {
    private static final Logger logger = LoggerFactory.getLogger(AdmlWorkerImpl.class);
    private static final long serialVersionUID = 1L;

    /**
     * Adml provider from RMI.
     */
    private AdmlProvider provider;

    /**
     * Our worker ID.
     */
    private final String workerId = UUID.randomUUID().toString();

    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    public AdmlWorkerImpl(String host){
        try {
            // Install security manager.  This is only necessary
            // if the remote object's client stub does not reside
            // on the client machine (it resides on the server).
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }

            // Export ourselves so provider can invoke methods on us.
            final AdmlWorker workerStub = (AdmlWorker)UnicastRemoteObject.exportObject(this, 0);

            //Get a reference to the remote server on this machine
            provider = (AdmlProvider) Naming.lookup("rmi://"+host+"/ADMLP");
            logger.info("Provider looked up successfully");

            // Register to the manager.
            provider.registerWorker(workerId, workerStub);

            // Worker loop.
            while(isRunning.get()){
                Thread.sleep(100);
                if (provider.shouldTerminate(workerId)){
                    break;
                }

                final Task<Object> job = provider.<Object>getNewJob(workerId, 1000);
                if (job == null){
                    continue;
                }

                Object result = null;
                try {
                    logger.info("<job name={}>", job.getTaskId());
                    result = job.execute();
                    logger.info("</job name={}>", job.getTaskId());

                }catch (Exception ex){
                    logger.error("Exception while evaluation a job", ex);
                }

                provider.<Object>jobFinished(workerId, job, result);
            }

            logger.info("Terminating worker {}", workerId);
            provider.unregisterWorker(workerId);

        } catch(Exception e){
            logger.error("Error connecting to the server: " + host, e);
        }
    }

    @Override
    public String getWorkerId() throws RemoteException {
        return workerId;
    }

    @Override
    public <T> T executeTask(Task<T> t) throws RemoteException {
        // TODO: implement
        return null;
    }

    @Override
    public String ping(long pingCtr) throws RemoteException {
        // TODO: implement.
        return workerId;
    }

    @Override
    public String cancelTask() throws RemoteException {
        // TODO: implement
        return null;
    }

    @Override
    public void shutdown() throws RemoteException {
        logger.info("Shutting down worker {}", workerId);
        isRunning.set(false);
    }
}
