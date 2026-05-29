package Controller.Systeem;

import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.TimeManagementPanel;
import View.Systeem.SettingsView;    // NIEUW: Importeer de andere views
import View.Systeem.OverzichtView;

import javax.swing.*;
import java.awt.*;

public class DarkModeController {

    // attributen
    private final HotelSimulatieView view;
    private final DarkModeModel model;
    private final TimeManagementPanel timeManagementPanel;

    // NIEUW: Bewaar referenties naar de andere views
    private SettingsView settingsView;
    private OverzichtView overzichtView;

    // constructor
    public DarkModeController(HotelSimulatieView view, TimeManagementPanel panel, DarkModeModel darkModeModel) {
        this.model = darkModeModel;
        this.view = view;
        this.timeManagementPanel = panel;
    }

    // NIEUW: Zorg dat we de schermen later kunnen koppelen vanuit je hoofdprogramma/controller
    public void verbindExtraSchermen(SettingsView settingsView, OverzichtView overzichtView) {
        this.settingsView = settingsView;
        this.overzichtView = overzichtView;
    }

    // centrale functie om dark mode aan of uit te toggelen
    public void toggleDarkMode() {
        model.setDarkMode(!model.isDarkMode());
        applyTheme();
    }

    // pas het thema toe aan alle componenten
    public void applyTheme() {
        // Stel de standaard Java Swing kleuren in
        UIManager.put("Panel.background", model.getBackgroundColor());
        UIManager.put("Label.foreground", model.getForegroundColor());
        UIManager.put("Button.background", model.getButtonBackgroundColor());
        UIManager.put("Button.foreground", model.getButtonForegroundColor());

        // 1. Update de Hoofdview
        SwingUtilities.updateComponentTreeUI(view);
        updateComponentColors(view, model.getBackgroundColor(), model.getForegroundColor());

        // 2. NIEUW: Update de SettingsView (als deze is gekoppeld)
        if (settingsView != null && settingsView.getFrame() != null) {
            SwingUtilities.updateComponentTreeUI(settingsView.getFrame());
            updateComponentColors(settingsView.getFrame(), model.getBackgroundColor(), model.getForegroundColor());
        }

        // 3. NIEUW: Update de OverzichtView (als deze is gekoppeld)
        if (overzichtView != null && overzichtView.getFrame() != null) {
            SwingUtilities.updateComponentTreeUI(overzichtView.getFrame());
            updateComponentColors(overzichtView.getFrame(), model.getBackgroundColor(), model.getForegroundColor());
        }

        timeManagementPanel.setTimeButtons(); // switch icons voor de time manager panel
        view.setLegendaView(); // update de legenda

        // hertekenen van de hoofdview
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