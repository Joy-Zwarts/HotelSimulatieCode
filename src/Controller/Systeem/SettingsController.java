package Controller.Systeem;

import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.SettingsView;
import View.Systeem.TimeManagementPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsController implements ActionListener {

    // attributen

    private final SettingsView settingsFrame;
    private final DarkModeController darkModeController;

    // constructor
    public SettingsController(HotelSimulatieView view, TimeManagementPanel panel, DarkModeModel DarkModeModel) {
        settingsFrame = new SettingsView(view);
        this.darkModeController = new DarkModeController(view, panel, DarkModeModel);

        darkModeController.applyTheme();

        init();
    }

    // registreert zichzelf als listener van de dark mode toggle button
    public void init() {
        settingsFrame.getDarkModeButton().addActionListener(this);
    }

    // op klik toggle dark mode
    @Override
    public void actionPerformed(ActionEvent e) {
        darkModeController.toggleDarkMode();
    }

    // getters & setters
    public SettingsView getSettingsFrame() {
        return settingsFrame;
    }
}