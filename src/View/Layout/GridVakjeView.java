package View.Layout;

import Model.Ruimtes.*;

import javax.swing.*;
import java.awt.*;

public class GridVakjeView {

    // attributen
    private final JLayeredPane layeredPane;
    private final JPanel backgroundPanel; // back layer (layout)
    private final JPanel liftContainer;    // lift layer (laag 1)
    private final JPanel gastenContainer;  // gasten layer (laag 2)

    // constructor
    public GridVakjeView(int x, int y, int breedte, int hoogte) {
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(x * breedte, y * hoogte, breedte, hoogte);
        layeredPane.setPreferredSize(new Dimension(breedte, hoogte));

        // layout laag
        backgroundPanel = new JPanel();
        backgroundPanel.setBackground(Color.WHITE);
        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0, 0, breedte, hoogte);
        backgroundPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        backgroundPanel.putClientProperty("noTheme", true);

        // laag 1
        liftContainer = new JPanel(new BorderLayout());
        liftContainer.setOpaque(false);
        liftContainer.setBounds(0, 0, breedte, hoogte);

        // laag 2
        JPanel guestPanel = new JPanel();
        guestPanel.setOpaque(false);
        guestPanel.setBounds(0, 0, breedte, hoogte);
        guestPanel.setLayout(new BorderLayout());

        // container voor de gasten zelf
        gastenContainer = new JPanel();
        gastenContainer.setOpaque(false);
        gastenContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 0));
        guestPanel.add(gastenContainer, BorderLayout.SOUTH);

        // voeg de panelen in de juiste volgorde toe aan de layeredPane
        layeredPane.add(backgroundPanel, Integer.valueOf(0));
        layeredPane.add(liftContainer, Integer.valueOf(1));
        layeredPane.add(guestPanel, Integer.valueOf(2));
    }

    // voegt de lift toe aan dit specifieke vakje
    public void voegLiftToe(LiftView lift) {
        liftContainer.removeAll();
        liftContainer.add(lift, BorderLayout.CENTER);
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    // verwijdert de lift uit dit vakje (als de lift gaat bewegen)
    public void verwijderLift() {
        liftContainer.removeAll();
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    // zet de icoontjes van de kamer, de kleurtjes en het kamer nummer
    public void zetInhoud(RuimteModel ruimte, boolean isLinksboven, boolean isLinksOnder) {
        backgroundPanel.removeAll();
        setColor(ruimte);

        boolean toonIcon = KamerType.TRAPPEN.equals(ruimte.getAreaType()) || isLinksboven;

        if (toonIcon) {
            ImageIcon icon = KamerType.ROOM.equals(ruimte.getAreaType())
                    ? bepaalSterrenIcon(((KamerModel) ruimte).getClassification())
                    : bepaalIcon(ruimte.getAreaType());

            if (icon != null) {
                JLabel iconLabel = new JLabel(icon);
                int xPos = (backgroundPanel.getWidth() - icon.getIconWidth()) / 2;
                iconLabel.setBounds(xPos, 2, icon.getIconWidth(), icon.getIconHeight());
                backgroundPanel.add(iconLabel);
            }
        }

        if (isLinksOnder) {
            String nummer = bepaalRuimteNummer(ruimte);
            if (nummer != null) {
                JLabel kamerNummer = new JLabel(nummer);

                kamerNummer.putClientProperty("noTheme", true);
                kamerNummer.setForeground(Color.BLACK);

                kamerNummer.setBounds(2, backgroundPanel.getHeight() - 15, 40, 15);
                backgroundPanel.add(kamerNummer);
            }
        }

        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

    // maakt een label met het gast icoontje ernaast die laat zien hoeveel mensen er in die kamer zitten
    public void zetPersonenAantal(RuimteModel ruimte, boolean isRechtsboven, boolean isLinksboven) {

        if (isNietTelbaar(ruimte)) {
            return;
        }

        if (!isLinksboven && !isRechtsboven) {
            return;
        }

        Component[] componenten = backgroundPanel.getComponents();
        for (int i = componenten.length - 1; i >= 0; i--) {
            Component c = componenten[i];
            if (c instanceof JLabel) {
                String name = c.getName();
                if (isRechtsboven && ("GAST_LABEL".equals(name) || "GAST_ICON".equals(name))) {
                    backgroundPanel.remove(c);
                }
                if (isLinksboven && ("SCHOONMAKER_LABEL".equals(name) || "SCHOONMAKER_ICON".equals(name))) {
                    backgroundPanel.remove(c);
                }
            }
        }

        int iconSize = 12;
        int spacing = 2;

        // schoonmakers
        if (isLinksboven && ruimte.getAantalSchoonmakers() > 0) {
            ImageIcon schoonmakerIcon = laadIcon("Schoonmaker.png");
            JLabel schoonmakerLabel = new JLabel(String.valueOf(ruimte.getAantalSchoonmakers()));
            schoonmakerLabel.putClientProperty("noTheme", true);
            schoonmakerLabel.setForeground(Color.BLACK);
            schoonmakerLabel.setFont(new Font("Arial", Font.BOLD, 12));
            schoonmakerLabel.setName("SCHOONMAKER_LABEL");
            schoonmakerLabel.setSize(schoonmakerLabel.getPreferredSize());

            int schoonmakerX = 2;

            if (schoonmakerIcon != null) {
                Image scaled = schoonmakerIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                JLabel iconLabel = new JLabel(new ImageIcon(scaled));
                iconLabel.setName("SCHOONMAKER_ICON");
                iconLabel.setBounds(schoonmakerX, 2, iconSize, iconSize);
                backgroundPanel.add(iconLabel);
            }

            schoonmakerLabel.setLocation(schoonmakerX + iconSize + spacing, 2);
            backgroundPanel.add(schoonmakerLabel);
        }

        // gasten
        if (isRechtsboven && ruimte.getAantalGasten() > 0) {
            ImageIcon gastIcon = laadIcon("gast.png");
            JLabel gastenLabel = new JLabel(String.valueOf(ruimte.getAantalGasten()));
            gastenLabel.putClientProperty("noTheme", true);
            gastenLabel.setForeground(Color.BLACK);
            gastenLabel.setFont(new Font("Arial", Font.BOLD, 12));
            gastenLabel.setName("GAST_LABEL");
            gastenLabel.setSize(gastenLabel.getPreferredSize());

            int gastenBreedte = gastenLabel.getWidth() + iconSize + spacing;
            int gastenX = backgroundPanel.getWidth() - gastenBreedte - 2;

            if (gastIcon != null) {
                Image scaled = gastIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                JLabel iconLabel = new JLabel(new ImageIcon(scaled));
                iconLabel.setName("GAST_ICON");
                iconLabel.setBounds(gastenX, 2, iconSize, iconSize);
                backgroundPanel.add(iconLabel);
            }

            gastenLabel.setLocation(gastenX + iconSize + spacing, 2);
            backgroundPanel.add(gastenLabel);
        }

        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

    // krijg de kamernummers per kamer
    private String bepaalRuimteNummer(RuimteModel ruimte) {
        if (ruimte instanceof KamerModel) return String.valueOf(((KamerModel) ruimte).getRoomNumber());
        if (ruimte instanceof BioscoopModel) return String.valueOf(((BioscoopModel) ruimte).getId());
        if (ruimte instanceof RestaurantModel) return String.valueOf(((RestaurantModel) ruimte).getID());
        if (ruimte instanceof FitnessModel) return String.valueOf(((FitnessModel) ruimte).getId());
        return null;
    }

    // kamers die geen kamernummers moeten hebben
    private boolean isNietTelbaar(RuimteModel ruimte) {
        KamerType type = ruimte.getAreaType();
        return type == KamerType.SCHACHT || type == KamerType.LOBBY || type == KamerType.TRAPPEN;
    }

    // zet de border om een vakje heen met de gegeven bepaalde waarden
    public void setBorder(boolean top, boolean left, boolean bottom, boolean right) {
        int t = top ? 1 : 0;
        int l = left ? 1 : 0;
        int b = bottom ? 1 : 0;
        int r = right ? 1 : 0;
        backgroundPanel.setBorder(BorderFactory.createMatteBorder(t, l, b, r, Color.BLACK));
    }

    // bepaal de kleur en zet de background deze kleur
    private void setColor(RuimteModel ruimte) {
        Color c;
        switch (ruimte.getAreaType()) {
            case ROOM: c = new Color(0xD4C2A8); break;
            case LIFT: c = new Color(0x8EA570); break;
            case LOBBY: c = new Color(0xCDA12D); break;
            case RESTAURANT, FITNESS, CINEMA: c = new Color(0xC1864F); break;
            case SCHACHT: c = Color.DARK_GRAY; break;
            case TRAPPEN: c = new Color(0x759DB6); break;
            default: c = Color.WHITE; break;
        }
        backgroundPanel.setBackground(c);
    }

    // laad het icoon gebaseerd op de naam van het bestand
    private ImageIcon laadIcon(String bestand) {
        // Let op: controleer of je map "Res" of "res" heet om nullpointers te voorkomen!
        java.net.URL url = getClass().getResource("/Res/" + bestand);
        return (url == null) ? null : new ImageIcon(url);
    }

    // bepaal welk icoon je moet gebruiken gebaseerd op het type kamer
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

    // bepaal welk icoon je moet gebruiken voor kamers gebaseerd op het aantal sterren van de kamer
    private ImageIcon bepaalSterrenIcon(KamerClassificatie classificatie) {
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

    // getters en setters

    public JPanel getGuestPanel() {
        return gastenContainer;
    }

    public JPanel getBackgroundPanel() {
        return backgroundPanel;
    }

    public JPanel getLiftContainer() {
        return liftContainer;
    }

    public JComponent getVakjePanel() {
        return layeredPane;
    }
}