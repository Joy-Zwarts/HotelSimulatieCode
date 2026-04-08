package Controller;

import Model.DarkModeModel;
import View.HotelSimulatieView;

import javax.swing.*;
import java.awt.*;

public class DarkModeController {

    private final HotelSimulatieView view;
    private final DarkModeModel model;

    public DarkModeController(HotelSimulatieView view, DarkModeModel model) {
        this.view = view;
        this.model = model;
    }

    public void toggleDarkMode() {
        model.setDarkMode(!model.isDarkMode());
        applyTheme();
    }

    public void applyTheme() {
        boolean darkMode = model.isDarkMode();

        Color bg = darkMode ? new Color(0x1e1f1f) : Color.WHITE;
        Color fg = darkMode ? Color.WHITE : new Color(0x1e1f1f);
        Color buttonBg = darkMode ? Color.WHITE : new Color(0x1e1f1f);
        Color buttonFg = darkMode ? new Color(0x1e1f1f) : Color.WHITE;

        UIManager.put("Panel.background", bg);
        UIManager.put("Viewport.background", bg);
        UIManager.put("ScrollPane.background", bg);
        UIManager.put("Label.foreground", fg);
        UIManager.put("Button.background", buttonBg);
        UIManager.put("Button.foreground", buttonFg);
        UIManager.put("control", bg);

        SwingUtilities.updateComponentTreeUI(view);
        updateComponentColors(view, bg, fg);

        view.setLegendaView(view.getLegendaPanel());

        view.revalidate();
        view.repaint();
    }

    private void updateComponentColors(Component comp, Color bg, Color fg) {

        if (comp instanceof JComponent jc) {
            if (Boolean.TRUE.equals(jc.getClientProperty("noTheme"))) {
                return;
            }
        }

        comp.setBackground(bg);
        comp.setForeground(fg);

        if (comp instanceof JButton) {
            comp.setBackground(bg.equals(Color.WHITE) ? new Color(0x1e1f1f) : Color.WHITE);
            comp.setForeground(fg.equals(Color.WHITE) ? new Color(0x1e1f1f) : Color.WHITE);
        }

        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                updateComponentColors(child, bg, fg);
            }
        }
    }

    public DarkModeModel getModel() {
        return model;
    }
}