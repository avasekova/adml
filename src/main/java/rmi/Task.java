package rmi;

import java.io.Serializable;

/**
 * Basic computation task interface for RMI.
 *
 * Created by dusanklinec on 15.11.15.
 */
public interface Task<T> extends Serializable {
    /**
     * Returns unique task identifier UUID.
     * @return task UUID.
     */
    String getTaskId();

    /**
     * Main work method;
     * @return task computation result.
     */
    T execute();
}