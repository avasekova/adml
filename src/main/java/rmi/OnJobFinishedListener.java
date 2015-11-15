package rmi;

/**
 * Created by dusanklinec on 15.11.15.
 */
public interface OnJobFinishedListener<T> {
    void onJobFinished(Task<T> task, T jobResult);
}
