package View;

import javax.swing.*;
import java.awt.*;

import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;
import hotelevents.HotelEventManager;
import hotelevents.HotelEventType;

public class EventPrint implements HotelEventListener {

    private JPanel panel;          // panel met event labels
    private JPanel panelRechts;    // rechter panel
    private JScrollPane scrollbar;
    private JLabel timeLabel;
    private HotelEventManager manager;
    private int time;

    public EventPrint(HotelEventManager manager) {
        this.manager = manager;
        time = 0;

        panelRechts = new JPanel();

        // panel voor events
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // scroll bar voor events
        scrollbar = new JScrollPane(panel);
        scrollbar.setPreferredSize(new Dimension(525, 500));
        scrollbar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        TimePanel timePanel = new TimePanel(manager, panelRechts);

        panelRechts.setLayout(new BorderLayout());
        panelRechts.add(timePanel.getTimePanel(),  BorderLayout.NORTH);
        panelRechts.add(scrollbar, BorderLayout.CENTER);

        manager.register(this);
    }

    public JPanel getPanelRechts() {
        return panelRechts;
    }

    @Override
    public void notify(HotelEvent hotelEvent) {
        time = hotelEvent.getTime() + 1000;
        printEvent(hotelEvent);
    }

    public void printEvent(HotelEvent hotelEvent) {
        if (hotelEvent.getEventType() != HotelEventType.NONE) {
            JLabel label = new JLabel(
                    "Time: " + hotelEvent.getTime() +
                            " | Type: " + hotelEvent.getEventType() +
                            " | GuestID: " + hotelEvent.getGuestId() +
                            " | Data: " + hotelEvent.getData()
            );
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(label);

            panel.revalidate();
            panel.repaint();

            JScrollBar vertical = scrollbar.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        }
    }
}