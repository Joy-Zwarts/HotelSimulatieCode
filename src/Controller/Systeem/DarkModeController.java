package Controller.Systeem;

import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.TimeManagementPanel;

import javax.swing.*;
import java.awt.*;

public class DarkModeController {

    // attributen

    private final HotelSimulatieView view;
    private final DarkModeModel model;
    private final TimeManagementPanel timeManagementPanel;

    // constructor
    public DarkModeController(HotelSimulatieView view, TimeManagementPanel panel, DarkModeModel darkModeModel) {
        this.model = darkModeModel;
        this.view = view;
        this.timeManagementPanel = panel;
    }

    // centrale functie om dark mode aan of uit te toggelen
    public void toggleDarkMode() {
        model.setDarkMode(!model.isDarkMode());
        applyTheme();
    }

    // pas het thema toe aan alle componenten
    public void applyTheme() {
        UIManager.put("Panel.background", model.getBackgroundColor());
        UIManager.put("Label.foreground", model.getForegroundColor());
        UIManager.put("Button.background", model.getButtonBackgroundColor());
        UIManager.put("Button.foreground", model.getButtonFgColor());

        SwingUtilities.updateComponentTreeUI(view);
        updateComponentColors(view, model.getBackgroundColor(), model.getForegroundColor());

        timeManagementPanel.setTimeButtons(); // switch icons voor de time manager panel van light naar dark mode en vice versa

        view.setLegendaView(view.getLegendaPanel()); // hetzelfde voor de legenda

        // hertekenen
        view.revalidate();
        view.repaint();
    }

    private void updateComponentColors(Component comp, Color bg, Color fg) {


        // als een element de key noTheme heeft switch daar de kleuren niet
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

    // getters & setters

    public DarkModeModel getModel() {
        return model;
    }
}