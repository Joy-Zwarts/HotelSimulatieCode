package View.Layout;

import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RoomClassificatie;
import Model.Ruimtes.RuimteModel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GridVakjeView {

    // attributen
    private final JPanel vakjePanel;

    // constructor
    public GridVakjeView(int x, int y, int breedte, int hoogte) {
        vakjePanel = new JPanel();
        vakjePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vakjePanel.setBackground(Color.WHITE);
        vakjePanel.setBounds(x * breedte, y * hoogte, breedte, hoogte);
    }

    // zet icoontjes links boven
    public void zetInhoud(RuimteModel ruimte, boolean isLinksboven) {

        setColor(ruimte);

        boolean toonIcon = KamerType.TRAPPEN.equals(ruimte.getAreaType()) || isLinksboven;

        if (toonIcon) {
            ImageIcon icon;

            if (KamerType.ROOM.equals(ruimte.getAreaType())) {
                icon = bepaalSterrenIcon(((KamerModel) ruimte).getClassification());
            } else {
                icon = bepaalIcon(ruimte.getAreaType());
            }

            vakjePanel.removeAll(); // oude iconen verwijderen

            if (icon != null) {
                JLabel iconLabel = new JLabel(icon);
                vakjePanel.setLayout(new BorderLayout());
                vakjePanel.add(iconLabel, BorderLayout.NORTH);
            }

            vakjePanel.revalidate();
            vakjePanel.repaint();
        }
    }

    public void zetKamerNummer(KamerModel kamer, boolean isLinksboven) {
        if (isLinksboven) {
            if (kamer.getDimension().equals("2, 2")) {
                JLabel kamerNummer = new JLabel(String.valueOf(kamer.getRoomNumber()));

                vakjePanel.setLayout(null);

                kamerNummer.setBounds(2, vakjePanel.getHeight() - 15, 30, 15);

                vakjePanel.add(kamerNummer);
                vakjePanel.repaint();
            } else {
                JLabel kamerNummer = new JLabel(String.valueOf(kamer.getRoomNumber()));
                vakjePanel.add(kamerNummer, BorderLayout.WEST);
                vakjePanel.repaint();
            }
        }
    }

    public void clearInhoud() {
        vakjePanel.removeAll();
        vakjePanel.repaint();
    }

    // zet de border om de kamers heen
    public void setBorder(boolean top, boolean left, boolean bottom, boolean right) {
        int topDikte = top ? 1 : 0;
        int leftDikte = left ? 1 : 0;
        int bottomDikte = bottom ? 1 : 0;
        int rightDikte = right ? 1 : 0;

        Border vakjeRand = BorderFactory.createMatteBorder(
                topDikte, leftDikte, bottomDikte, rightDikte, Color.BLACK);

        vakjePanel.setBorder(vakjeRand);
    }

    // kleur het vakje in gebaseerd op de inhoud
    private void setColor(RuimteModel ruimte) {
        switch (ruimte.getAreaType()) {
            case ROOM: vakjePanel.setBackground(new Color(0xD4C2A8)); break;
            case LIFT: vakjePanel.setBackground(new Color(0x8EA570)); break;
            case LOBBY: vakjePanel.setBackground(new Color(0xCDA12D)); break;
            case RESTAURANT, FITNESS, CINEMA: vakjePanel.setBackground(new Color(0xC1864F)); break;
            case SCHACHT: vakjePanel.setBackground(Color.DARK_GRAY); break;
            case TRAPPEN: vakjePanel.setBackground(new Color(0x759DB6)); break;
            default: vakjePanel.setBackground(Color.WHITE); break;
        }
    }

    private ImageIcon laadIcon(String bestand) {
        java.net.URL url = getClass().getResource("/Res/" + bestand);

        if (url == null) {
            System.out.println("Niet gevonden: " + bestand);
            return null;
        }

        return new ImageIcon(url);
    }

    // bepaal welk icoon er geplaatst moet worden in het vakje
    private ImageIcon bepaalIcon(KamerType areaType) {
        switch (areaType) {
            case CINEMA: return laadIcon("Cinema.png");
            case FITNESS: return laadIcon("Fitness.png");
            case LIFT: return laadIcon("Lift.png");
            case TRAPPEN: return laadIcon("Trap.png");
            case RESTAURANT: return laadIcon("Restaurant.png");
            case LOBBY: return laadIcon("Lobby.png");
            default: return null;
        }
    }

    // als het een kamer is moet het een icoon kiezen gebaseerd op het aantal sterren van een kamer
    private ImageIcon bepaalSterrenIcon(RoomClassificatie classificatie) {
        if (classificatie == null) return null;

        switch (classificatie) {
            case eenSter: return laadIcon("eenSter.png");
            case tweeSterren: return laadIcon("tweeSter.png");
            case drieSterren: return laadIcon("drieSter.png");
            case vierSterren: return laadIcon("vierSter.png");
            case vijfSterren: return laadIcon("vijfSter.png");
            default: return null;
        }
    }

    // getters & setters

    public JPanel getVakjePanel() {
        return vakjePanel;
    }
}