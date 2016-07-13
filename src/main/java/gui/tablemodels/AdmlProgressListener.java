package gui.tablemodels;

import com.klinec.admwl.AdmwlOnJobProgressListener;
import gui.AdmlProgressMonitor;
import models.TrainAndTestReport;


public class AdmlProgressListener implements AdmwlOnJobProgressListener<TrainAndTestReport> {

    private AdmlProgressMonitor progressMonitor;

    public AdmlProgressListener(AdmlProgressMonitor progressMonitor) {
        this.progressMonitor = progressMonitor;
    }

    @Override
    public boolean onAdmwlJobProgressed(String workerId, String taskId, double progress) {
        progressMonitor.progressOneStep();
        return true;
    }
}
