package View.Layout;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LiftView {

    public void styleLiftLabel(JLabel liftLabel, int vakBreedte, int vakHoogte) {
        liftLabel.setText("");
        URL imageURL = getClass().getResource("/res/lift.png");

        if (imageURL != null) {
            ImageIcon origineelIcoon = new ImageIcon(imageURL);

            Image geschaaldeAfbeelding = origineelIcoon.getImage().getScaledInstance(
                    vakBreedte - 2, vakHoogte - 2, Image.SCALE_SMOOTH
            );
            liftLabel.setIcon(new ImageIcon(geschaaldeAfbeelding));
        }

        liftLabel.setOpaque(true);
        liftLabel.setBackground(new java.awt.Color(0x8EA570));
        liftLabel.setHorizontalAlignment(SwingConstants.CENTER);
        liftLabel.setVerticalAlignment(SwingConstants.CENTER);
        liftLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        Dimension liftGrootte = new Dimension(vakBreedte, vakHoogte);
        liftLabel.setPreferredSize(liftGrootte);
        liftLabel.setSize(liftGrootte);
    }
}