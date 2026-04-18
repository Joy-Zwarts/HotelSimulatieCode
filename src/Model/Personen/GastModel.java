package Model.Personen;

import Controller.Layout.LayoutController;
import Controller.Layout.LayoutGeladen;
import Controller.Layout.Locatie;
import Model.Ruimtes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GastModel extends PersoonModel {
    // attributen

    private RoomClassificatie wensen;
    private KamerModel kamer;
    private final int gastID;
    private JLabel gastLabel;

    // constructor
    public GastModel(int gastId, Locatie locatie, Locatie targetLocatie, RoomClassificatie wensen, KamerModel kamer) {
        super(locatie, targetLocatie);
        this.gastID = gastId;
        this.wensen = wensen;
        this.kamer = kamer;
        initGastLabel();
    }

    // maak een rond gasten icoon label met de gastID in het midden
    private void initGastLabel() {
        this.gastLabel = new JLabel(String.valueOf(gastID), SwingConstants.CENTER) {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(36, 104, 181));
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);

                g2.setColor(Color.WHITE);
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);

                super.paintComponent(g);
            }
        };

        this.gastLabel.setPreferredSize(new Dimension(20, 20));
        this.gastLabel.setSize(new Dimension(20, 20));
        this.gastLabel.setForeground(Color.WHITE);
        this.gastLabel.setFont(new Font("Arial", Font.BOLD, 11));
    }

    // voor later

//    public void gebruikFitness() {
//    }
//
//    public void gebruikKamer() {
//    }
//
//    public void gebruikBios() {
//    }
//
//    public void gebruikTrap() {
//    }
//
//    public void callLift() {
//    }
//
//    public void gebruikLift() {
//    }

    // getters en setters
    public JLabel getGastLabel() {
        if (gastLabel == null) initGastLabel();
        return gastLabel;
    }

    public KamerModel getKamer() {
        return this.kamer;
    }

    public void setKamer(KamerModel kamer) {
        this.kamer = kamer;
    }

    public RoomClassificatie getWensen() {
        return this.wensen;
    }

    public void setWensen(RoomClassificatie wens) {
        this.wensen = wens;
    }

    public int getGastID() {
        return this.gastID;
    }
}
