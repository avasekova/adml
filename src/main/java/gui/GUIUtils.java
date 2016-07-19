package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class GUIUtils {

    public static void setComponentsEnabled(JFrame parent, Class<? extends Component> childType, boolean enabled) {
        setComponentsEnabled(parent.getRootPane(), childType, enabled);
    }

    public static void setComponentsEnabled(JDialog parent, Class<? extends Component> childType, boolean enabled) {
        setComponentsEnabled(parent.getRootPane(), childType, enabled);
    }

    public static void setComponentsEnabled(JComponent parent, Class<? extends Component> childType, boolean enabled) {
        setComponentsEnabled(parent, c -> childType.isInstance(c), enabled);
    }

    public static void setComponentsEnabled(JComponent parent, Predicate<JComponent> filter, boolean enabled) {
        setComponentsEnabled(parent, filter, c -> c.setEnabled(enabled));
    }

    public static void setComponentsEnabled(JComponent parent, Predicate<JComponent> filter, Consumer<JComponent> action) {
        getAllComponents(parent).stream().filter(filter).forEach(action);
    }

    public static List<JComponent> getAllComponents(JComponent component) {
        List<JComponent> components = new ArrayList<>();
        for (Component c : component.getComponents()) {
            if (c instanceof JComponent) {
                components.add((JComponent) c);
                components.addAll(getAllComponents((JComponent) c));
                //do not process JWindows or JDialogs
            }
        }
        return components;
    }

    public static void setButtonsEnabled(JComponent parent, boolean enabled) {
        setComponentsEnabled(parent, JButton.class, enabled);
    }

    public static void setButtonsEnabled(JFrame parent, boolean enabled) {
        setComponentsEnabled(parent, JButton.class, enabled);
    }
}
