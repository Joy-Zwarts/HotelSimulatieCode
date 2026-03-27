package View;

import javax.swing.*;
import java.awt.*;

public class LegendaView {
    JPanel legendaPanel;
    public LegendaView() {
        legendaPanel = new JPanel();
        JLabel legendaLabel = new JLabel("Legenda");
        legendaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        legendaLabel.setVerticalAlignment(SwingConstants.CENTER);
        legendaPanel.add(legendaLabel);
    }

    public JPanel getLegendaPanel() {
        return legendaPanel;
    }
}
