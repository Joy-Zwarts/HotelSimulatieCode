package Model.Entiteiten;

import Model.Layout.Locatie;
import javax.swing.*;
import java.awt.*;

public class PersoonModel extends EntiteitenModel {

    // VERWIJDERD: ID, locatie, targetLocatie en vorigeLocatie (staan al in EntiteitenModel)
    private final Color kleur;
    private final JLabel persoonLabel;
    private final TypePersoon typePersoon;

    public PersoonModel(int ID, Locatie locatie, Locatie targetLocatie, Color bolletjeKleur, TypePersoon typePersoon) {
        // Dit stuurt de data netjes door naar EntiteitenModel
        super(ID, locatie, targetLocatie);
        this.kleur = bolletjeKleur;
        // Let op: createLabel() wordt nu aangeroepen ná super(), zodat getID() de juiste waarde heeft
        this.persoonLabel = createLabel();
        this.typePersoon = typePersoon;
    }

    private JLabel createLabel() {
        // Gebruik getID() in plaats van de losse variabele ID,
        // zodat je de ID uit de superclass ophaalt.
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

    // VERWIJDERD: getVorigeLocatie() en setVorigeLocatie()
    // Deze stonden al in EntiteitenModel en wermsten hier de werking tegen.

    public TypePersoon getTypePersoon() {
        return typePersoon;
    }
}