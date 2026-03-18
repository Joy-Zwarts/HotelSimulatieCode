import javax.swing.Timer;

public class Klok {
    private Event event;
    private int hotelTijdsEenheid;

    public Klok(Event event, int hotelTijdEenheid) {
        this.event = event;
    }

    public void startKlok() {
        Timer timer = new Timer(1000, this.event);
        timer.start();
    }

    public void stopKlok() {
    }

    public void pauseKlok() {
    }

    public int getTime() {
        return this.hotelTijdsEenheid;
    }
}
