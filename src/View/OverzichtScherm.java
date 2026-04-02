package View;

import Model.Gast;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OverzichtScherm {
    private ArrayList<Gast> gasten;
    private int tijd;

    public static JFrame pauseFrame = new JFrame();

    public OverzichtScherm() {
        pauseFrame.setSize(400, 200);

        JPanel panel = new JPanel(new BorderLayout());
        pauseFrame.add(panel);

        JLabel pause = new JLabel("Gepauseerd");
        pause.setHorizontalAlignment(JLabel.CENTER);
        pause.setVerticalAlignment(JLabel.CENTER);

        panel.add(pause, BorderLayout.CENTER);
    }

    public OverzichtScherm(ArrayList<Gast> gasten, int tijd) {
        this.gasten = gasten;
        this.tijd = tijd;
    }

    public ArrayList<Gast> getGasten() {
        return this.gasten;
    }

    public void addGasten(ArrayList<Gast> gasten) {
        this.gasten = gasten;
    }

    public int getTijd() {
        return this.tijd;
    }

    public void setTijd(int tijd) {
        this.tijd = tijd;
    }

    public static void setVisibility(boolean visible) {
        pauseFrame.setVisible(visible);
    }
}
