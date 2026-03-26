package Model;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GridVakje {

    private int x;
    private int y;
    private JPanel Vakjepanel;
    private ImageIcon icon;

    // constructor
    public GridVakje(int x, int y, int breedte, int hoogte) {
        this.x = x;
        this.y = y;

        Vakjepanel = new JPanel();
        Vakjepanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        Vakjepanel.setBackground(Color.WHITE);
        Vakjepanel.setBounds(x * breedte, y * hoogte, breedte, hoogte);
    }

    // zet wat er in het vakje zit
    public void zetInhoud(Ruimte ruimte) {

        if (ruimte.getAreaType().equals("Room")) {
            this.icon = bepaalSterrenIcon(((Kamer) ruimte).getClassification());
        } else {
            this.icon = bepaalIcon(ruimte.getAreaType());
        }

        Vakjepanel.removeAll();

        if (icon != null) {
            Vakjepanel.add(new JLabel(icon));
        } else {
            System.out.println("Geen icoon voor: " + ruimte.getAreaType());
        }

        Vakjepanel.revalidate();
        Vakjepanel.repaint();
    }

    public void setBorder(boolean top, boolean left, boolean bottom, boolean right) {
        int topDikte = top ? 1 : 0;
        int leftDikte = left ? 1 : 0;
        int bottomDikte = bottom ? 1 : 0;
        int rightDikte = right ? 1 : 0;

        Border vakjeRand = BorderFactory.createMatteBorder(
                topDikte, leftDikte, bottomDikte, rightDikte, Color.BLACK);

        Vakjepanel.setBorder(vakjeRand);
    }

    public void clearInhoud() {
        Vakjepanel.removeAll();
        Vakjepanel.repaint();
    }

    private ImageIcon laadIcon(String bestand) {
        java.net.URL url = getClass().getResource("/Res/" + bestand);

        if (url == null) {
            System.out.println("Niet gevonden: " + bestand);
            return null;
        }

        return new ImageIcon(url);
    }

    private ImageIcon bepaalIcon(String areaType) {
        switch (areaType) {
            case "Cinema": return laadIcon("Cinema.png");
            case "Fitness": return laadIcon("Fitness.png");
            case "Lift": return laadIcon("Lift.png");
            case "Trappen": return laadIcon("Trap.png");
            case "Restaurant": return laadIcon("Restaurant.png");
            case "Lobby": return laadIcon("Lobby.png");
            default: return null;
        }
    }

    private ImageIcon bepaalSterrenIcon(RoomClassificatie classificatie) {
        if (classificatie == null) return null;

        switch (classificatie) {
            case RoomClassificatie.eenSter: return laadIcon("eenSter.png");
            case RoomClassificatie.tweeSterren: return laadIcon("tweeSter.png");
            case RoomClassificatie.drieSterren: return laadIcon("drieSter.png");
            case RoomClassificatie.vierSterren: return laadIcon("vierSter.png");
            case RoomClassificatie.vijfSterren: return laadIcon("vijfSter.png");
            default: return null;
        }
    }

    public JPanel getVakjepanel() {
        return Vakjepanel;
    }

    public String getKey() {
        return x + "," + y;
    }
}