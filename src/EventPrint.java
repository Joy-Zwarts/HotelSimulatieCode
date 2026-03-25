import javax.swing.*;
import java.awt.*;

public class EventPrint implements HotelEventListener {

    private JFrame frame; // frame voor events printen
    private JPanel panel; // panel om de jlabels met de events aan toe te voegen
    private JScrollPane scrollbar; // scrollbar toevoegen

    public EventPrint(HotelEventManager manager) {

        frame = new JFrame("Events");
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        scrollbar = new JScrollPane(panel); // nieuwe scrollbar
        frame.add(scrollbar); // voeg toe aan frame

        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        manager.registerListener(this);
    }

    @Override
    public void notify(HotelEvent hotelEvent) {
        printEvent(hotelEvent);
    }

    public void printEvent(HotelEvent hotelEvent) {

        JLabel label = new JLabel(
                "Time: " + hotelEvent.getTime() +
                        " | Type: " + hotelEvent.getHotelEventType() +
                        " | GuestID: " + hotelEvent.getGuestID() +
                        " | Data: " + hotelEvent.getData()
        );

        panel.add(label);
        panel.revalidate();
        panel.repaint();

        // auto-scroll naar beneden
        JScrollBar vertical = scrollbar.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }
}