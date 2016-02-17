package gui.settingspanels;

import gui.ComponentGroup;
import gui.MainFrame;
import models.params.Params;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SettingsPanel extends JPanel {
    
    private ComponentGroup groupButtons = new ComponentGroup();
    private boolean takenIntoAccount = true;
    
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
    
    public void enableAllButtons() {
        groupButtons.enableAll();
    }
    
    public void disableAllButtons() {
        groupButtons.disableAll();
    }
    
    public void setButtons(JButton... buttons) {
        groupButtons = new ComponentGroup();
        groupButtons.addAll(buttons);
    }
    
    public abstract void enableAllElements(boolean trueFalse);
    
    //this is here because of the radius panels (which won't be used in the params if this is turned off) and
    //   CRCombinationStrategy panels (which turn them off using this)
    public void setTakenIntoAccount(boolean takenIntoAccount) {
        this.takenIntoAccount = takenIntoAccount;
    }
    
    public boolean isTakenIntoAccount() {
        return takenIntoAccount;
    }
}
