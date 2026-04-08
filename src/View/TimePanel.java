package View;

import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;
import hotelevents.HotelEventManager;
import hotelevents.HotelEventType;

import javax.swing.*;
import java.awt.*;

public class TimePanel implements HotelEventListener {

    // attributen

    private JPanel panelTime;
    private JLabel timeLabel;
    private HotelEventManager manager;

    // constructor
    public TimePanel(HotelEventManager manager, JPanel panelRechts) {
        this.manager = manager;

        // panel for tijd
        panelTime = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeLabel = new JLabel("00:00");
        timeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 35));
        timeLabel.setVerticalAlignment(SwingConstants.CENTER);
        timeLabel.setHorizontalAlignment(SwingConstants.LEFT);

        panelTime.add(timeLabel);

        panelRechts.add(panelTime);

        // register zichzelf als listener van ticks
        manager.register(this);
    }

    // per tick bereken de tijd opnieuw en pas het label aan
    @Override
    public void notify(HotelEvent event) {
        long totalSeconds = event.getTime();
        int minutes = (int) (totalSeconds / 60);
        int seconds = (int) (totalSeconds % 60);

        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    // getters & setters

    public JPanel getTimePanel() {
        return panelTime;
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }
}

