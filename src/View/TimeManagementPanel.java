package View;

import hotelevents.HotelEventManager;
import Model.DarkModeModel;

import javax.swing.*;
import java.awt.*;

public class TimeManagementPanel {
    private JPanel panelTimeManagement;
    private JButton normaleTijd;
    private JButton fastForwardTijd;
    private JButton doubleFastForwardTijd;
    private HotelEventManager manager;
    private DarkModeModel darkMode;
    private boolean started;
    private HotelSimulatieView view;

    public TimeManagementPanel(HotelEventManager manager, JPanel panelRechts, DarkModeModel darkMode, Boolean started) {
        this.manager = manager;
        this.darkMode = darkMode;
        this.started = started;

        // panel instellen
        panelTimeManagement = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelTimeManagement.setBackground(UIManager.getColor("Panel.background"));
        panelTimeManagement.setPreferredSize(new Dimension(525, 50));

        // buttons aanmaken

        normaleTijd = new JButton(laadIcon("play.png", 45, 45));
        normaleTijd.setOpaque(false);
        normaleTijd.setContentAreaFilled(false);
        normaleTijd.setBorderPainted(false);
        normaleTijd.addActionListener(e -> {
            if (started) {
                manager.setHte(1000);
            } else {
                JOptionPane.showMessageDialog(view,
                        "De simulatie is nog niet gestart!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        fastForwardTijd = new JButton(laadIcon("fastForward.png", 68, 45));
        fastForwardTijd.setOpaque(false);
        fastForwardTijd.setContentAreaFilled(false);
        fastForwardTijd.setBorderPainted(false);
        fastForwardTijd.addActionListener(e -> {
            if (started) {
                manager.setHte(1000);
            } else {
                JOptionPane.showMessageDialog(view,
                        "De simulatie is nog niet gestart!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        doubleFastForwardTijd = new JButton(laadIcon("doubleFastForward.png", 83, 45));
        doubleFastForwardTijd.setOpaque(false);
        doubleFastForwardTijd.setContentAreaFilled(false);
        doubleFastForwardTijd.setBorderPainted(false);
        doubleFastForwardTijd.addActionListener(e -> {
            if (started) {
                manager.setHte(1000);
            } else {
                JOptionPane.showMessageDialog(view,
                        "De simulatie is nog niet gestart!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Buttons toevoegen aan panel
        panelTimeManagement.add(normaleTijd);
        panelTimeManagement.add(fastForwardTijd);
        panelTimeManagement.add(doubleFastForwardTijd);

        // Panel toevoegen aan parent
        panelRechts.add(panelTimeManagement);
    }

    // Wissel icons voor dark/light mode
    public void changeIcons() {
        if (darkMode.isDarkMode()) {
            normaleTijd.setIcon(laadIcon("playDarkMode.png", 45, 45));
            fastForwardTijd.setIcon(laadIcon("fastForwardDarkMode.png", 68, 45));
            doubleFastForwardTijd.setIcon(laadIcon("doubleFastForwardDarkMode.png", 83, 45));
        } else {
            normaleTijd.setIcon(laadIcon("play.png", 45, 45));
            fastForwardTijd.setIcon(laadIcon("fastForward.png", 68, 45));
            doubleFastForwardTijd.setIcon(laadIcon("doubleFastForward.png", 83, 45));
        }
    }

    // Icon laden vanaf resources
    private ImageIcon laadIcon(String bestand, int width, int height) {
        java.net.URL url = getClass().getResource("/Res/" + bestand);
        if (url == null) {
            System.out.println("Niet gevonden: " + bestand);
            return null;
        }
        ImageIcon icon = new ImageIcon(url);
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // Panel getter
    public JPanel getTimeManagementPanel() {
        return panelTimeManagement;
    }
}