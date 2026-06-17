package View.Layout;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LiftView extends JLabel {

    public LiftView(int vakBreedte, int vakHoogte) {
        // basis instellingen van het label zelf
        this.setText("");
        this.setOpaque(true);
        this.setBackground(new java.awt.Color(0x8EA570));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // --- VOEG DIT TOE: Bescherm de lift tegen de DarkModeController ---
        this.putClientProperty("noTheme", true);
        // ------------------------------------------------------------------

        // afmetingen instellen
        Dimension liftGrootte = new Dimension(vakBreedte, vakHoogte);
        this.setPreferredSize(liftGrootte);
        this.setSize(liftGrootte);

        // afbeelding laden en schalen
        URL imageURL = getClass().getResource("/res/lift.png");
        if (imageURL != null) {
            ImageIcon origineelIcoon = new ImageIcon(imageURL);
            Image geschaaldeAfbeelding = origineelIcoon.getImage().getScaledInstance(
                    vakBreedte - 2, vakHoogte - 2, Image.SCALE_SMOOTH
            );
            this.setIcon(new ImageIcon(geschaaldeAfbeelding));
        }
    }
}