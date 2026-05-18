package View.Systeem;

import javax.swing.*;
import java.awt.*;

public class EindeSimulatieView extends JPanel {

    public EindeSimulatieView() {
        setOpaque(false);
        addMouseListener(new java.awt.event.MouseAdapter() {});
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int balkHoogte = 120;
        int balkY = (getHeight() - balkHoogte) / 2;
        g2d.setColor(new Color(20, 20, 20, 220));
        g2d.fillRect(0, balkY, getWidth(), balkHoogte);

        String tekst = "Simulatie Beëindigt";
        g2d.setFont(new Font("PricedownBl", Font.ITALIC | Font.BOLD, 70));

        FontMetrics fm = g2d.getFontMetrics();
        int tekstX = (getWidth() - fm.stringWidth(tekst)) / 2;
        int tekstY = balkY + ((balkHoogte - fm.getHeight()) / 2) + fm.getAscent();

        g2d.setColor(Color.BLACK);
        g2d.drawString(tekst, tekstX + 3, tekstY + 3);

        // Teken de rode GTA-kleur letters
        g2d.setColor(new Color(180, 0, 0));
        g2d.drawString(tekst, tekstX, tekstY);

        g2d.dispose();
    }
}