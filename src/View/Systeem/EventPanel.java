package View.Systeem;

import javax.swing.*;
import java.awt.*;

import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;
import hotelevents.HotelEventManager;
import hotelevents.HotelEventType;

public class EventPanel implements HotelEventListener {

    private final JPanel container;
    private final JPanel content;
    private final JScrollPane scrollbar;

    public EventPanel(HotelEventManager manager) {

        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // scrollbar
        scrollbar = new JScrollPane(content);
        scrollbar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        container = new JPanel(new BorderLayout());
        container.setPreferredSize(new Dimension(300, 600));
        container.add(scrollbar, BorderLayout.CENTER);

        manager.register(this);
    }

    public JPanel getPanelRechts() {
        return container;
    }

    @Override
    public void notify(HotelEvent hotelEvent) {
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

            content.add(label);

            content.revalidate();
            content.repaint();

            JScrollBar vertical = scrollbar.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        }
    }
}