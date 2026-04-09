package View;

import javax.swing.*;
import java.awt.*;

public class SettingsView {

    // attributen

    public HotelSimulatieView view;
    public JButton darkModeButton;

    public JFrame settingsFrame;

    // constructor
    public SettingsView(HotelSimulatieView view) {
        settingsFrame = new JFrame("Settings");
        settingsFrame.setSize(300, 200);
        settingsFrame.setLayout(new FlowLayout());

        darkModeButton = new JButton("Toggle Dark Mode");

        settingsFrame.add(darkModeButton);

        settingsFrame.setLocationRelativeTo(view);
    }

    // getters & setters

    public JButton getDarkModeButton() {
        return darkModeButton;
    }

    public JFrame getFrame() {
        return settingsFrame;
    }
}
