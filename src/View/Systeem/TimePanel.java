package View.Systeem;

import Controller.Events.EventHandler;
import Controller.Events.noneEvent;
import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.awt.*;

public class TimePanel implements noneEvent{

    // attributen

    private final JLabel timeLabel;

    // constructor
    public TimePanel(HotelEventManager manager, JPanel panelRechts) {

        // panel for tijd
        JPanel panelTime = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeLabel = new JLabel("00:00");
        timeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 35));
        timeLabel.setVerticalAlignment(SwingConstants.CENTER);
        timeLabel.setHorizontalAlignment(SwingConstants.LEFT);

        panelTime.add(timeLabel);

        panelRechts.add(panelTime);
    }

    // getters & setters

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    // per tick bereken de tijd opnieuw en pas het label aan
    @Override
    public void noneEvent(HotelEvent event) {
        long totalSeconds = event.getTime();
        int minutes = (int) (totalSeconds / 60);
        int seconds = (int) (totalSeconds % 60);

        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }
}

