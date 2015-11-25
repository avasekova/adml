package rmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Simple implementation by
 * Created by dusanklinec on 15.11.15.
 */
public class AdmlWorkerImpl<T> implements AdmlWorker<T> {
    private static final Logger logger = LoggerFactory.getLogger(AdmlWorkerImpl.class);
    private static final long serialVersionUID = 1L;

    /**
     * Adml provider from RMI.
     */
    private AdmlProvider<T> provider;

    /**
     * Our worker ID.
     */
    private final String workerId = UUID.randomUUID().toString();

    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    public AdmlWorkerImpl(String host, String svc) throws RemoteException, MalformedURLException, NotBoundException {
        startWorker(host, -1, svc);
    }

    public AdmlWorkerImpl(String host, int port, String svc) throws RemoteException, MalformedURLException, NotBoundException {
        startWorker(host, port, svc);
    }

    protected void startWorker(String host, int port, String svc) throws RemoteException, MalformedURLException, NotBoundException {
        // Install security manager.  This is only necessary
        // if the remote object's client stub does not reside
        // on the client machine (it resides on the server).
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        // Export ourselves so provider can invoke methods on us.
        final AdmlWorker workerStub = (AdmlWorker)UnicastRemoteObject.exportObject(this, 0);
        String connectURL = "rmi://"+host+"/" + svc;
        if (port > 0){
            connectURL = "rmi://"+host+":"+port+"/" + svc;
        }

        //Get a reference to the remote server on this machine
        provider = (AdmlProvider<T>) Naming.lookup(connectURL);
        logger.info("Provider looked up successfully");

        // Register to the manager.
        provider.registerWorker(workerId, workerStub);
    }

    /**
     * Worker method.
     */
    public void work(){
        try {
            logger.info("Entering worker loop, waiting for jobs, my id is {}", workerId);
            while (isRunning.get()) {
                try {
                    Thread.sleep(100);
                } catch(InterruptedException ie){
                    logger.info("Worker interrupted", ie);
                    break;
                }

                if (!isRunning.get() || provider == null || provider.shouldTerminate(workerId)) {
                    break;
                }

                final Task<T> job = provider.getNewJob(workerId, 1000);
                if (job == null) {
                    continue;
                }

                T result = null;
                try {
                    logger.info("<job name={}>", job.getTaskId());
                    result = job.execute();
                    logger.info("</job name={}>", job.getTaskId());

                } catch (Exception ex) {
                    logger.error("Exception while evaluation a job", ex);
                }

                provider.jobFinished(workerId, job, result);
            }

            logger.info("Terminating worker {}", workerId);

            // Unregister from the provider, if possible.
            // If shutdown was triggered by the server during shutdown sequence,
            // it is probably dead by now.
            try {
                if (provider != null) {
                    provider.unregisterWorker(workerId);
                }
            }catch(Exception e){
                logger.error("Could not unregister from the provider, maybe it is dead", e);
            }

        } catch(Exception e){
            logger.error("Exception during worker run", e);
        }

        logger.info("Worker terminated {}", workerId);
    }

    @Override
    public String getWorkerId() throws RemoteException {
        return workerId;
    }

    @Override
    public T executeTask(Task<T> t) throws RemoteException {
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
        isRunning.set(false);
        logger.info("Shutting down worker {}", workerId);
    }
}
