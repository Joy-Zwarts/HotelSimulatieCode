package Model;

import hotelevents.HotelEventManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GridVakje {

    private int x;
    private int y;
    private JPanel Vakjepanel;
    private ImageIcon icon;
    private Ruimte ruimte;
    private HotelEventManager manager;

    // constructor
    public GridVakje(int x, int y, int breedte, int hoogte) {
        this.x = x;
        this.y = y;
        this.manager = manager;

        Vakjepanel = new JPanel();
        Vakjepanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        Vakjepanel.setBackground(Color.WHITE);
        Vakjepanel.setBounds(x * breedte, y * hoogte, breedte, hoogte);
        Vakjepanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(ruimte.getAreaType().equals("Lobby")){
                    manager.pauze();
                }
                System.out.println("Geklikt op vakje: " + ruimte);
            }
        });
    }

    // zet wat er in het vakje zit
    public void zetInhoud(Ruimte ruimte) {
        this.ruimte = ruimte;

        if (ruimte.getAreaType().equals("Room")) {
            this.icon = bepaalSterrenIcon(((Kamer) ruimte).getClassification());
        } else {
            this.icon = bepaalIcon(ruimte.getAreaType());
        }

        Vakjepanel.removeAll();

        if (icon != null) {
            Vakjepanel.add(new JLabel(icon));
        }

        Vakjepanel.revalidate();
        Vakjepanel.repaint();
        setColor(ruimte);
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

    public void setColor (Ruimte ruimte) {
        switch (ruimte.getAreaType()) {
            case "Room": getVakjepanel().setBackground(new Color(0xD4C2A8)); break;
            case "Lift": getVakjepanel().setBackground(new Color(0x8EA570)); break;
            case "Lobby": getVakjepanel().setBackground(new Color(0xCDA12D)); break;
            case "Restaurant": getVakjepanel().setBackground(new Color(0xC1864F)); break;
            case "Fitness": getVakjepanel().setBackground(new Color(0xC1864F)); break;
            case "Cinema": getVakjepanel().setBackground(new Color(0xC1864F)); break;
            case "Schacht": getVakjepanel().setBackground(Color.DARK_GRAY); break;
            case "Trappen": getVakjepanel().setBackground(new Color(0x759DB6)); break;
            default: getVakjepanel().setBackground(Color.WHITE); break;
        }
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

    public void setRuimte(Ruimte ruimte) {
        this.ruimte = ruimte;
    }
}