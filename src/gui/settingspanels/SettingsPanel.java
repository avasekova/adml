package gui.settingspanels;

import gui.MainFrame;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import params.Params;

public abstract class SettingsPanel extends JPanel {
    
    public abstract List<Params> addSpecificParams(List<Params> resultList);
    
    //does not support multiple values yet
    public static <T extends Params, Q> List<T> setSomethingOneValue(Class<T> classs,
            List<T> workingList, List<T> resultList, String methodName, Class<Q> classsQ, Q valueQ) {
        workingList.clear();
        workingList.addAll(resultList);
        resultList.clear();
        
        for (T p : workingList) {
            T plone = (T) p.getClone();
            try {
                Method method = classs.getMethod(methodName, classsQ);
                method.invoke(plone, valueQ);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            resultList.add(plone);
        }
        
        return resultList;
    }
    
    public static <T extends Params, Q> List<T> setSomethingList(Class<T> classs,
            List<T> workingList, List<T> resultList, String methodName, Class<Q> classsQ, List<Q> valuesQ) {
        workingList.clear();
        workingList.addAll(resultList);
        resultList.clear();
        for (Q i : valuesQ) {
            for (T p : workingList) {
                T plone = (T) p.getClone();
                try {
                    Method method = classs.getMethod(methodName, classsQ);
                    method.invoke(plone, i);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                resultList.add(plone);
            }
        }
        
        return resultList;
    }
    
    public static <T extends Params, Q extends Params> List<T> setSomethingListForHybrid(Class<T> classs,
            List<T> workingList, List<T> resultList, String methodName, Class<Q> classsQ, List<Q> valuesQ) {
        workingList.clear();
        workingList.addAll(resultList);
        resultList.clear();
        for (Q i : valuesQ) {
            for (T p : workingList) {
                T plone = (T) p.getClone();
                try {
                    Method method = classs.getMethod(methodName, Params.class);
                    method.invoke(plone, i);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                resultList.add(plone);
            }
        }
        
        return resultList;
    }
    
}
