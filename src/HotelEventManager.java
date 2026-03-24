import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class HotelEventManager {

    private int hte;
    private Timer timer;
    private ArrayList<HotelEventListener> listeners;
    private int time = 0;

    private Random rand = new Random();


    // constructor

    public HotelEventManager(int hte) {
        this.hte = hte;
        listeners = new ArrayList<>();
    }

    public void registerListener(HotelEventListener listener) { // voeg listener toe aan de lijst van listeners
        listeners.add(listener);
    }

    public void deregisterListener(HotelEventListener listener) { // remove listener uit de lijst van listeners
        listeners.remove(listener);
    }

    public void setHte(int hte) { // pas de hte aan
        this.hte = (hte);
    }

    public void startScenario(){

    }

    public void startTimer() {
        timer = new Timer(hte, e -> generateEvent()); // maak de timer aan, tick per hte en per tick genereer een event
        timer.start(); // startTimer de klok
    }

    private void generateEvent() {
        time += hte; // tijd aanpassen

        int kansOpRandomEvent = rand.nextInt(1,5); // kies een random getal tussen 0 en 7

        System.out.println(kansOpRandomEvent);

        HotelEventType[] values = HotelEventType.values(); // krijg de values van de enum (dan krijg je bijv 0 = NONE)

        HotelEventType gekozenEvent = null;

        if (kansOpRandomEvent == 4){ // als het getal 6 is
            gekozenEvent = values[rand.nextInt(values.length)]; // kies een random event
        } else {
            gekozenEvent = values[0]; // kies NONE
        }


        int guestID = rand.nextInt(100, 999); // kies voor nu nog een random guestID
        int data = gekozenEvent.ordinal(); // geef door welk nummer het is in de lijst (idk wat je hiermee moet)

        HotelEvent event = new HotelEvent(time, gekozenEvent, guestID, data); // maak een nieuw event aan

        notifyListeners(event); // notify de listeners van het nieuwe event
    }

    private void notifyListeners(HotelEvent evt) {
        for (HotelEventListener listener : listeners) { // per listeners in de lijst
            listener.notify(evt); // notify ze en geef de event mee
        }
    }

    public void pause() {

    }

    public void stop() { // stop de timer
        timer.stop();
    }
}