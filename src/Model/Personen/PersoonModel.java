package Model.Personen;

import Model.Layout.Locatie;

import javax.swing.*;
import java.awt.*;

public class PersoonModel {

    private final int ID;

    private Locatie locatie;
    private final Locatie targetLocatie;
    private Locatie vorigeLocatie;
    private final Color kleur;
    private final JLabel persoonLabel;
        private final TypePersoon typePersoon;

    public PersoonModel(int ID, Locatie locatie, Locatie targetLocatie, Color bolletjeKleur, TypePersoon typePersoon) {
        this.ID = ID;
        this.locatie = locatie;
        this.targetLocatie = targetLocatie;
        this.kleur = bolletjeKleur;
        this.persoonLabel = createLabel();
        this.typePersoon = typePersoon;
    }

    private JLabel createLabel() {
        JLabel label = new JLabel(String.valueOf(ID), SwingConstants.CENTER) {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(kleur);
                g2.fillOval(0, 0, getWidth(), getHeight());

                g2.setColor(Color.WHITE);
                g2.drawOval(0, 0, getWidth(), getHeight());

                super.paintComponent(g);
            }
        };

        label.setPreferredSize(new Dimension(20, 20));
        label.setSize(new Dimension(20, 20));
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 11));

        return label;
    }

    public int getID() {
        return ID;
    }

    public JLabel getPersoonLabel() {
        return persoonLabel;
    }

    public Locatie getLocatie() {
        return locatie;
    }

    public void setLocatie(Locatie locatie) {
        this.locatie = locatie;
    }

    public Locatie getTargetLocatie() {
        return targetLocatie;
    }

    public Locatie getVorigeLocatie() {
        return vorigeLocatie;
    }

    public void setVorigeLocatie(Locatie vorigeLocatie) {
        this.vorigeLocatie = vorigeLocatie;
    }

    public TypePersoon getTypePersoon() {
        return typePersoon;
    }
}