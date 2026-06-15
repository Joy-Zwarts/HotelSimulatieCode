package Model.Entiteiten;

import Model.Layout.Locatie;
import javax.swing.*;
import java.awt.*;

public class PersoonModel extends EntiteitenModel {

    // attributen

    private final Color kleur;
    private final JLabel persoonLabel;
    private final TypePersoon typePersoon;

    // constructor
    public PersoonModel(int ID, Locatie locatie, Locatie targetLocatie, Color bolletjeKleur, TypePersoon typePersoon) {
        super(ID, locatie, targetLocatie);
        this.kleur = bolletjeKleur;
        this.persoonLabel = createLabel();
        this.typePersoon = typePersoon;
    }

    // maakt het bolletje voor het lopen door het hotel (moet later in een view klasse)
    private JLabel createLabel() {
        JLabel label = new JLabel(String.valueOf(getID()), SwingConstants.CENTER) {

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

    public JLabel getPersoonLabel() {
        return persoonLabel;
    }

    public TypePersoon getTypePersoon() {
        return typePersoon;
    }
}