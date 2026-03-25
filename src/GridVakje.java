import javax.swing.*;
import java.awt.*;

public class GridVakje {

    private int x;
    private int y;
    private JPanel Vakjepanel;
    private String inhoud; // wat zit er in dit vakje (Room, Cinema, etc)


    // constructor
    public GridVakje(int x, int y, int breedte, int hoogte) {
        this.x = x; // zet x op de meegegeven x
        this.y = y; // zet y op de meegegeven y

        Vakjepanel = new JPanel(); // maak een nieuwe panel aan voor het vakje
        Vakjepanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // zet een border om het vakje heen
        Vakjepanel.setBackground(Color.WHITE); // zet de achtergrond kleur op wit

        Vakjepanel.setBounds(x * breedte, y * hoogte, breedte, hoogte); //zet het vak op de juiste plek en bepaalt de grootte
    }

    // zet wat er in het vakje zit
    public void zetInhoud(Ruimte ruimte) {
        String label;
        if (ruimte.getAreaType().equals("Room")) { // als het een kamer is
            label = ruimte.getAreaType() + " " + ((Kamer) ruimte).getClassification(); // zet er het aantal sterren bij
        } else label = ruimte.getAreaType(); // anders niet
        this.inhoud = label; // zet de inhoud
        Vakjepanel.add(new JLabel(inhoud)); // maak een Jlabel aan voor de inhoudstekst en plaats in het vakjespaneel
    }

    public void setBorder(boolean top, boolean left, boolean bottom, boolean right) {
        int topDikte;
        int leftDikte;
        int bottomDikte;
        int rightDikte;

        if (top) { // als er een top border moet komen
            topDikte = 1; // zet dikte op 1
        } else {
            topDikte = 0; // anders zet het op 0 (onzichtbaar dus)
        }

        // doe hetzelfde voor elke kant VV

        if (left) {
            leftDikte = 1;
        } else {
            leftDikte = 0;
        }

        if (bottom) {
            bottomDikte = 1;
        } else {
            bottomDikte = 0;
        }

        if (right) {
            rightDikte = 1;
        } else {
            rightDikte = 0;
        }

        // maak de border
        javax.swing.border.Border vakjeRand = javax.swing.BorderFactory.createMatteBorder(topDikte, leftDikte, bottomDikte, rightDikte, java.awt.Color.BLACK);

        // add border bij het vakjepanel
        Vakjepanel.setBorder(vakjeRand);
    }

    // In GridVakje.java
    public void clearInhoud() {
        // verwijder alle componenten in het vakje
        getVakjepanel().removeAll();
        // update het vakje visueel
        getVakjepanel().repaint();
    }

    // getters en setters

    public JPanel getVakjepanel() {
        return Vakjepanel;
    }

    public String getKey() {
        return x + "," + y;
    }
}