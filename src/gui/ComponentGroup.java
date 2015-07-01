package gui;

import java.awt.Component;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ComponentGroup {
    
    private final Set<Component> components = new HashSet<>();
    
    public void add(Component c) {
        components.add(c);
    }
    
    public void addAll(ComponentGroup group) {
        for (Component c : group.getAllComponents()) {
            add(c);
        }
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
        for (Component c : group.getAllComponents()) {
            remove(c);
        }
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
}
