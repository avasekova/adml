package rmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Simple worker provider implementation
 * Created by dusanklinec on 15.11.15.
 */
public class AdmlProviderImpl<T> implements AdmlProvider<T> {
    private static final Logger logger = LoggerFactory.getLogger(AdmlProviderImpl.class);
    private static final long serialVersionUID = 1L;
    public static final String NAME = "ADMLP";

    /**
     * Map of registered workers.
     */
    private final Map<String, AdmlWorker> workers = new ConcurrentHashMap<>();

    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    private final Queue<Task<T>> jobQueue = new ConcurrentLinkedQueue<>();

    private OnJobFinishedListener<T> jobFinishedListener;

    /**
     * Starts main ADMLP server.
     */
    public void initServer(){
        try {
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }

            AdmlProvider<T> stub = (AdmlProvider<T>) UnicastRemoteObject.exportObject(this, 0);

            // Starting our own registry so it has class definitions of our classes.
            // Starting a new registry may need to allow it on the local firewall
            // or to execute manager with administrator rights.
            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            registry.rebind(NAME, stub);

            // Old way - using default registry
            // Naming.bind("rmi://localhost/" + NAME, stub);
            logger.info("ADML Provider ready");
        } catch (Exception e){
            logger.error("Failed to start RMI ADML Provider", e);
        }
    }

    /**
     * Adds computation job to the queue
     */
    public void enqueueJob(Task<T> job){
        if (!isRunning.get()){
            logger.info("System could not accept a new job as it is terminated");
            return;
        }

        jobQueue.add(job);
    }

    /**
     * Returns false is server is shutted down - accepting no more jobs.
     * @return
     */
    public boolean isServerRunning(){
        return isRunning.get();
    }

    /**
     * Initiates shutting down procedure.
     * Terminates all connected workers.
     */
    public void shutdown(){
        for (Map.Entry<String, AdmlWorker> workerEntry : workers.entrySet()) {
            try {
                logger.info("Shutting down worker {}", workerEntry.getKey());
                workerEntry.getValue().shutdown();

            } catch (RemoteException e) {
                logger.error("Exception when shutting down worker " + workerEntry.getKey(), e);
            }
        }

        isRunning.set(false);
    }

    @Override
    public Task<T> getNewJob(String workerId, long timeout) throws RemoteException {
        if (!isRunning.get()){
            return null;
        }

        return jobQueue.poll();
    }

    @Override
    public void jobFinished(String workerId, Task<T> task, T jobResult) throws RemoteException {
        logger.info("Job has finished");
        if (jobFinishedListener == null){
            logger.error("Job finished listener is null");
            return;
        }

        jobFinishedListener.onJobFinished(task, jobResult);
    }

    @Override
    public boolean jobProgress(String workerId, String taskId, double progress) throws RemoteException {
        if (!isRunning.get()){
            return false;
        }

        // TODO: implement
        return true;
    }

    @Override
    public boolean shouldCancel(String workerId, String taskId) throws RemoteException {
        if (!isRunning.get()){
            return true;
        }

        // TODO: implement
        return false;
    }

    @Override
    public boolean shouldTerminate(String workerId) throws RemoteException {
        if (!isRunning.get()){
            return true;
        }

        return false;
    }

    @Override
    public void registerWorker(String workerId, AdmlWorker workerCallback) throws RemoteException {
        logger.info("Registering worker {}", workerId);
        workers.put(workerId, workerCallback);
    }

    @Override
    public void unregisterWorker(String workerId) throws RemoteException {
        logger.info("Unregistering worker {}", workerId);
        workers.remove(workerId);
    }

    @Override
    public String keepAlivePing(String workerId, long pingCtr) throws RemoteException {
        if (!isRunning.get()){
            return null;
        }

        return workerId;
    }

    public OnJobFinishedListener<T> getJobFinishedListener() {
        return jobFinishedListener;
    }

    public void setJobFinishedListener(OnJobFinishedListener<T> jobFinishedListener) {
        this.jobFinishedListener = jobFinishedListener;
    }
}
