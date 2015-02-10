package gui.settingspanels;

import gui.MainFrame;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import models.params.Params;

public abstract class SettingsPanel extends JPanel {
    
    public abstract <T extends Params> void setSpecificParams(Class<T> classss, List<T> resultList);
    
    public static <T extends Params, Q> List<T> setSomethingOneValue(Class<T> classs,
            List<T> resultList, String methodName, Class<Q> classsQ, Q valueQ) {
        List<T> workingList = new ArrayList<>();
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
            List<T> resultList, String methodName, Class<Q> classsQ, List<Q> valuesQ) {
        List<T> workingList = new ArrayList<>();
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
            List<T> resultList, String methodName, Class<Q> classsQ, List<Q> valuesQ) {
        List<T> workingList = new ArrayList<>();
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
