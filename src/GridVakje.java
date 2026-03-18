import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class GridVakje {
    private int x;
    private int y;
    private JPanel Vakjepanel;
    private String inhoud;

    public GridVakje(int x, int y, int breedte, int hoogte) {
        this.x = x;
        this.y = y;
        this.Vakjepanel = new JPanel();
        this.Vakjepanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.Vakjepanel.setBackground(Color.WHITE);
        this.Vakjepanel.setBounds(x * breedte, y * hoogte, breedte, hoogte);
    }

    public void zetInhoud(Ruimte ruimte) {
        String label;
        if (ruimte.getAreaType().equals("Room")) {
            String var10000 = ruimte.getAreaType();
            label = var10000 + " " + String.valueOf(((Kamer)ruimte).getClassification());
        } else {
            label = ruimte.getAreaType();
        }

        this.inhoud = label;
        this.Vakjepanel.add(new JLabel(this.inhoud));
    }

    public void setBorder(boolean top, boolean left, boolean bottom, boolean right) {
        int topDikte;
        if (top) {
            topDikte = 1;
        } else {
            topDikte = 0;
        }

        int leftDikte;
        if (left) {
            leftDikte = 1;
        } else {
            leftDikte = 0;
        }

        int bottomDikte;
        if (bottom) {
            bottomDikte = 1;
        } else {
            bottomDikte = 0;
        }

        int rightDikte;
        if (right) {
            rightDikte = 1;
        } else {
            rightDikte = 0;
        }

        Border vakjeRand = BorderFactory.createMatteBorder(topDikte, leftDikte, bottomDikte, rightDikte, Color.BLACK);
        this.Vakjepanel.setBorder(vakjeRand);
    }

    public JPanel getVakjepanel() {
        return this.Vakjepanel;
    }

    public String getKey() {
        return this.x + "," + this.y;
    }
}
