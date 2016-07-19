package gui;

import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ComponentGroup {
    
    private final Set<Component> components = new HashSet<>();
    
    public enum ENABLED { ALL, NONE, SOME }
    
    public void add(Component c) {
        components.add(c);
    }
    
    public void addAll(ComponentGroup group) {
        group.getAllComponents().forEach(this::add);
    }
    
    public void addAll(Component... comps) {
        for (Component c : comps) {
            add(c);
        }
    }
    
    public void remove(Component c) {
        components.remove(c);
    }
    
    public void removeAll(ComponentGroup group) {
        group.getAllComponents().forEach(this::remove);
    }
    
    public void removeAll(Component... comps) {
        for (Component c : comps) {
            remove(c);
        }
    }
    
    public Set<Component> getAllComponents() {
        return Collections.unmodifiableSet(components);
    }
    
    public void enableAll() {
        for (Component c : components) {
            c.setEnabled(true);
        }
    }
    
    public void disableAll() {
        for (Component c : components) {
            c.setEnabled(false);
        }
    }
    
    public ENABLED areEnabled() {
        boolean enabledAtLeastOne = false;
        boolean disabledAtLeastOne = false;
        
        for (Component c : components) {
            if (c.isEnabled()) {
                if (disabledAtLeastOne) {
                    return ENABLED.SOME;
                }
                
                enabledAtLeastOne = true;
            } else {
                if (enabledAtLeastOne) {
                    return ENABLED.SOME;
                }
                
                disabledAtLeastOne = true;
            }
        }
        
        if (enabledAtLeastOne && !disabledAtLeastOne) {
            return ENABLED.ALL;
        } else if (enabledAtLeastOne && disabledAtLeastOne) {
            return ENABLED.SOME;
        } else { //(!enabled && disabled) || (!enabled && !disabled)
            return ENABLED.NONE; //may also indicate that the group is empty
        }
    }
}
