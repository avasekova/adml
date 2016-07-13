package gui;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AdmlProgressMonitor extends ProgressMonitor {

    private AtomicInteger progress = new AtomicInteger(0);
    private final int MAX;

    public AdmlProgressMonitor(Component parentComponent, Object message, int max) {
        this(parentComponent, message, "Processing...", 0, max);
    }

    public AdmlProgressMonitor(Component parentComponent, Object message, String note, int max) {
        this(parentComponent, message, note, 0, max);
    }

    public AdmlProgressMonitor(Component parentComponent, Object message, String note, int min, int max) {
        super(parentComponent, message, note, min, max);
        MAX = max;
        setProgress(0);
    }

    public void progressOneStep() {
        this.setProgress(progress.incrementAndGet());
        this.setNote(progress.get() + "/" + MAX);
    }

    //TODO cancel

}
