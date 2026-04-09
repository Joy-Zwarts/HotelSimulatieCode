package View;

import Model.GastModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OverzichtView {

    // attributen

    private ArrayList<GastModel> gasten;
    private int tijd;
    public static JFrame pauseFrame = new JFrame();

    // constructor
    public OverzichtView() {
        pauseFrame.setSize(400, 200);

        JPanel panel = new JPanel(new BorderLayout());
        pauseFrame.add(panel);

        JLabel pause = new JLabel("Gepauseerd");
        pause.setHorizontalAlignment(JLabel.CENTER);
        pause.setVerticalAlignment(JLabel.CENTER);

        panel.add(pause, BorderLayout.CENTER);
    }

    // getters & setters

    public ArrayList<GastModel> getGasten() {
        return this.gasten;
    }

    public void addGasten(ArrayList<GastModel> gasten) {
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
