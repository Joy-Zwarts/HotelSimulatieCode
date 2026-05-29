package View.Systeem;

import Model.Systeem.DarkModeModel;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class TimeManagementPanel {

    // attributen

    private final JPanel panelTimeManagement;
    private final JButton normaleTijd;
    private final JButton fastForwardTijd;
    private final JButton doubleFastForwardTijd;
    private final DarkModeModel darkMode;

    // constructor
    public TimeManagementPanel(JPanel panelRechts, DarkModeModel darkMode) {
        this.darkMode = darkMode;

        // panel instellen
        panelTimeManagement = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelTimeManagement.setBackground(UIManager.getColor("Panel.background"));
        panelTimeManagement.setPreferredSize(new Dimension(525, 50));

        // buttons aanmaken
        normaleTijd = new JButton(laadIcon("play.png", 45));
        normaleTijd.setOpaque(false);
        normaleTijd.setContentAreaFilled(false);
        normaleTijd.setBorderPainted(false);

        fastForwardTijd = new JButton(laadIcon("fastForward.png", 68));
        fastForwardTijd.setOpaque(false);
        fastForwardTijd.setContentAreaFilled(false);
        fastForwardTijd.setBorderPainted(false);

        doubleFastForwardTijd = new JButton(laadIcon("doubleFastForward.png", 83));
        doubleFastForwardTijd.setOpaque(false);
        doubleFastForwardTijd.setContentAreaFilled(false);
        doubleFastForwardTijd.setBorderPainted(false);

        // buttons toevoegen aan panel
        panelTimeManagement.add(normaleTijd);
        panelTimeManagement.add(fastForwardTijd);
        panelTimeManagement.add(doubleFastForwardTijd);

        panelRechts.add(panelTimeManagement);
    }

    // icon laden
    private ImageIcon laadIcon(String bestand, int width) {
        java.net.URL url = getClass().getResource("/Res/" + bestand);
        if (url == null) {
            System.out.println("Niet gevonden: " + bestand);
            return null;
        }
        ImageIcon icon = new ImageIcon(url);
        Image scaled = icon.getImage().getScaledInstance(width, 45, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // switch op dark / light mode
    public void setTimeButtons() {
        this.panelTimeManagement.removeAll(); // oude inhoud verwijderen

        ImageIcon normaleTijdIcon;
        ImageIcon fastForwardTijdIcon;
        ImageIcon doubleFastForwardTijdIcon;
        if (darkMode.isDarkMode()) {
            normaleTijdIcon = new ImageIcon(Objects.requireNonNull(laadIcon("playDarkMode.png", 45)).getImage());
            fastForwardTijdIcon = new ImageIcon(Objects.requireNonNull(laadIcon("fastForwardDarkMode.png", 55)).getImage());
            doubleFastForwardTijdIcon = new ImageIcon(Objects.requireNonNull(laadIcon("doubleFastForwardDarkMode.png", 65)).getImage());
        } else {
            normaleTijdIcon = new ImageIcon(Objects.requireNonNull(laadIcon("play.png", 45)).getImage());
            fastForwardTijdIcon = new ImageIcon(Objects.requireNonNull(laadIcon("fastForward.png", 55)).getImage());
            doubleFastForwardTijdIcon = new ImageIcon(Objects.requireNonNull(laadIcon("doubleFastForward.png", 65)).getImage());
        }

        normaleTijd.setIcon(normaleTijdIcon);
        fastForwardTijd.setIcon(fastForwardTijdIcon);
        doubleFastForwardTijd.setIcon(doubleFastForwardTijdIcon);
        this.panelTimeManagement.add(normaleTijd);
        this.panelTimeManagement.add(fastForwardTijd);
        this.panelTimeManagement.add(doubleFastForwardTijd);

        this.panelTimeManagement.revalidate();
        this.panelTimeManagement.repaint();
    }

    // getters & setters

    public JPanel getTimeManagementPanel() {
        return panelTimeManagement;
    }
    public JButton getNormaleTijd() {
        return normaleTijd;
    }
    public JButton getFastForwardTijd() {
        return fastForwardTijd;
    }
    public JButton getDoubleFastForwardTijd() {
        return doubleFastForwardTijd;
    }
}