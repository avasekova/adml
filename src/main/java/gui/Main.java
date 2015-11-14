package gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String args[]) {
        logger.info("Starting the application");
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame.getInstance().setExtendedState(JFrame.MAXIMIZED_BOTH); //maximize the window
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException ex) {
                    logger.error("Exception", ex);
                }
                
                MainFrame.getInstance().setVisible(true);
            }
        });
    }
    
}
