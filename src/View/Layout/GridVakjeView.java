package View.Layout;

import Model.Ruimtes.*;

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
        vakjePanel.setLayout(null);
    }

    // zet icoontjes links boven
    public void zetInhoud(RuimteModel ruimte, boolean isLinksboven, boolean isLinksOnder) {

        vakjePanel.removeAll();
        setColor(ruimte);

        boolean toonIcon = KamerType.TRAPPEN.equals(ruimte.getAreaType()) || isLinksboven;

        if (toonIcon) {

            ImageIcon icon;

            if (KamerType.ROOM.equals(ruimte.getAreaType())) {
                icon = bepaalSterrenIcon(((KamerModel) ruimte).getClassification());
            } else {
                icon = bepaalIcon(ruimte.getAreaType());
            }

            if (icon != null) {
                JLabel iconLabel = new JLabel(icon);

                int iconBreedte = icon.getIconWidth();
                int iconHoogte = icon.getIconHeight();

                int x = (vakjePanel.getWidth() - iconBreedte) / 2;
                int y = 2;

                iconLabel.setBounds(x, y, iconBreedte, iconHoogte);
                vakjePanel.add(iconLabel);
            }
        }

        if (isLinksOnder) {

            String nummer = null;

            if (ruimte.getAreaType().equals(KamerType.ROOM)) {
                nummer = String.valueOf(((KamerModel) ruimte).getRoomNumber());
            } else if (ruimte.getAreaType().equals(KamerType.CINEMA)) {
                nummer = String.valueOf(((BioscoopModel) ruimte).getId());
            } else if (ruimte.getAreaType().equals(KamerType.RESTAURANT)) {
                nummer = String.valueOf(((RestaurantModel) ruimte).getID());
            } else if (ruimte.getAreaType().equals(KamerType.FITNESS)) {
                nummer = String.valueOf(((FitnessModel) ruimte).getId());
            }

            if (nummer != null) {
                JLabel kamerNummer = new JLabel(nummer);
                int x = 2;
                int y = vakjePanel.getHeight() - 15;

                kamerNummer.setBounds(x, y, 40, 15);
                vakjePanel.add(kamerNummer);
            }
        }

        vakjePanel.revalidate();
        vakjePanel.repaint();
    }

    public void zetPersonenAantal(RuimteModel ruimte, boolean isRechtsBoven){

        if ((ruimte.getAreaType().equals(KamerType.SCHACHT)) || (ruimte.getAreaType().equals(KamerType.LOBBY))|| (ruimte.getAreaType().equals(KamerType.TRAPPEN))) {
            return;
        }

        if(isRechtsBoven){

            vakjePanel.setLayout(null);

            ImageIcon icon = laadIcon("gast.png");

            JLabel iconLabel = null;
            int iconSize = 12;

            if (icon != null) {
                Image img = icon.getImage();
                Image scaled = img.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaled);

                iconLabel = new JLabel(icon);
            }

            JLabel aantalMensen = new JLabel(String.valueOf(ruimte.getAantalGasten()));
            aantalMensen.setFont(new Font("Arial", Font.BOLD, 12));

            int x = vakjePanel.getWidth() - 2;
            int y = 2;

            int spacing = 2;

            aantalMensen.setSize(aantalMensen.getPreferredSize());

            int totalWidth = aantalMensen.getWidth() + iconSize + spacing;

            int startX = vakjePanel.getWidth() - totalWidth - 2;

            // 👤 icon positie
            if (iconLabel != null) {
                iconLabel.setBounds(startX, y, iconSize, iconSize);
                vakjePanel.add(iconLabel);
            }

            aantalMensen.setLocation(startX + iconSize + spacing, y);

            vakjePanel.add(aantalMensen);
            vakjePanel.repaint();
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