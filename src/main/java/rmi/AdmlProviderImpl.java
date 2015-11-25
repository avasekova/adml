package rmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple worker provider implementation
 * Created by dusanklinec on 15.11.15.
 */
public class AdmlProviderImpl<T> implements AdmlProvider<T> {
    private static final Logger logger = LoggerFactory.getLogger(AdmlProviderImpl.class);
    private static final long serialVersionUID = 1L;
    public static final String NAME = "ADMLP";

    private static final AtomicLong pingCtr = new AtomicLong(0);

    /**
     * Map of registered workers.
     */
    private final Map<String, AdmlWorker<T>> workers = new ConcurrentHashMap<>();

    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    private final Queue<Task<T>> jobQueue = new ConcurrentLinkedQueue<>();

    private OnJobFinishedListener<T> jobFinishedListener;

    /**
     * Registry to be used for binding.
     */
    private Registry registry;

    /**
     * Starts main ADMLP server.
     */
    public void initServer() throws RemoteException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        AdmlProvider<T> stub = (AdmlProvider<T>) UnicastRemoteObject.exportObject(this, 0);

        // Starting our own registry so it has class definitions of our classes.
        // Starting a new registry may need to allow it on the local firewall
        // or to execute manager with administrator rights.
        if(registry == null) {
            logger.info("Registry was null, creating a new one on the localhost");
            registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }

        // Rebind the exported provider.
        registry.rebind(NAME, stub);

        // Old way - using default registry
        // Naming.bind("rmi://localhost/" + NAME, stub);
        logger.info("ADML Provider ready");
    }

    /**
     * Goes through all workers and remove those failing to respond on ping (connection broken)
     */
    public void checkAllWorkers(){
        if (workers.isEmpty()){
            return;
        }

        List<String> keysToRemove = new ArrayList<String>(workers.size());
        for (Map.Entry<String, AdmlWorker<T>> workerEntry : workers.entrySet()) {
            final String workerKey = workerEntry.getKey();
            final AdmlWorker<T> worker = workerEntry.getValue();

            boolean works = true;
            try {
                worker.ping(pingCtr.incrementAndGet());
                works = true;
            } catch(Exception e){
                works = false;
            }

            if (!works){
                logger.info("Worker is not working {}", workerKey);
                keysToRemove.add(workerKey);
            }
        }

        for(String wKey : keysToRemove){
            workers.remove(wKey);
        }

        if (!keysToRemove.isEmpty()){
            logger.info("Workers removed: {}, workers left: {}", keysToRemove.size(), workers.size());
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

        // Has to have at least one worker registered.
        if (workers.isEmpty()){
            logger.error("No workers registered, cannot compute a job");
            if (jobFinishedListener != null) {
                jobFinishedListener.onJobFinished(job, null);
            }
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
        for (Map.Entry<String, AdmlWorker<T>> workerEntry : workers.entrySet()) {
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
        return !isRunning.get();

    }

    @Override
    public void registerWorker(String workerId, AdmlWorker<T> workerCallback) throws RemoteException {
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

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }
}
