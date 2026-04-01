package View;

import Controller.HotelEventManager;
import Model.HotelEvent;
import Model.HotelEventListener;
import Model.HotelEventType;

import javax.swing.*;
import java.awt.*;

public class EventPrint implements HotelEventListener {
    private JPanel panel;         // contains event labels
    private JPanel panelRechts;   // right-side panel
    private JScrollPane scrollbar;
    private JLabel timeLabel;
    private HotelEventManager manager;
    private int seconds;
    private int minutes;

    String timeLabelInhoud;

    public EventPrint(HotelEventManager manager) {
        this.manager = manager;

        // panel voor events
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // scroll bar voor events
        scrollbar = new JScrollPane(panel);
        scrollbar.setPreferredSize(new Dimension(525, 500)); // width + height
        scrollbar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    }

    public JPanel getPanelRechts() {
        return panelRechts;
    }

    @Override
    public void notify(HotelEvent hotelEvent) {
        printEvent(hotelEvent);
    }

    public void printEvent(HotelEvent hotelEvent) {
        if (hotelEvent.getHotelEventType() != HotelEventType.NONE) {
            JLabel label = new JLabel(
                    "Time: " + hotelEvent.getTime() +
                            " | Type: " + hotelEvent.getHotelEventType() +
                            " | GuestID: " + hotelEvent.getGuestID() +
                            " | Data: " + hotelEvent.getData()
            );
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(label);

            // update UI
            panel.revalidate();
            panel.repaint();

            // scroll to bottom
            JScrollBar vertical = scrollbar.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        }


    }
}