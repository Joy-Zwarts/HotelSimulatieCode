package View;

import Controller.HotelEventManager;
import Model.HotelEvent;
import Model.HotelEventListener;
import Model.HotelEventType;

import javax.swing.*;
import java.awt.*;

public class EventPrint implements HotelEventListener {
    private JPanel panel;
    private JPanel panelTime;
    private JScrollPane scrollbar;
    private JLabel timeLabel;
    private HotelEventManager manager;

    public EventPrint(HotelEventManager manager) {
        this.manager = manager;
        panel = new JPanel();
        panelTime = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JLabel timeLabel = new JLabel(String.valueOf(manager.getTime()));
        panelTime.add(timeLabel);
        panelTime.setPreferredSize(new Dimension(1500, 50));
        scrollbar = new JScrollPane(panel);
        scrollbar.setPreferredSize(new Dimension(525, 500)); // breedte + hoogte

        manager.registerListener(this);
    }

    public JScrollPane getEventPanel() {
        return scrollbar;
    }

    public JPanel getTimePanel() {
        return panelTime;
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
            panel.add(label);
            panel.revalidate();
            panel.repaint();

            JScrollBar vertical = scrollbar.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        }
    }
}