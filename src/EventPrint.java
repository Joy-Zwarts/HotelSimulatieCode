import javax.swing.*;
import java.awt.*;

public class EventPrint implements HotelEventListener {

    private JFrame frame; // maak frame aan om te laten zien
    private JPanel panel; // maak panel aan voor de events

    public EventPrint(HotelEventManager manager) {

        frame = new JFrame("Events");
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // zet layout dat elk element onder elkaar wordt gezet

        frame.add(new JScrollPane(panel));
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        manager.registerListener(this); // register deze klasse als een listener
    }

    @Override
    public void notify(HotelEvent hotelEvent) { // functie die wordt aangeroepen als er iets gebeurt
        printEvent(hotelEvent); // print het hotelEvent
    }

    public void printEvent(HotelEvent hotelEvent) {

        // maak een nieuw textlabel voor om het meegegeven event te printen
        JLabel label = new JLabel("Time: " + hotelEvent.getTime() + " | Type: " + hotelEvent.getHotelEventType() + " | GuestID: " + hotelEvent.getGuestID() + " | Data: " + hotelEvent.getData());

        panel.add(label); // voeg deze toe aan de panel
        panel.revalidate(); // teken de panel opnieuw
    }
}