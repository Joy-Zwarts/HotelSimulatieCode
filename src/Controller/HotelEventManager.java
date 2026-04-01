package Controller;

import Model.HotelEvent;
import Model.HotelEventListener;
import Model.HotelEventType;
import View.OverzichtScherm;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class HotelEventManager {

    private int hte;
    private static Timer timer;
    private ArrayList<HotelEventListener> listeners;
    private int time = 0;
    private static boolean paused = false;

    private Random rand = new Random();

    // constructor

    public HotelEventManager(int hte) {
        this.hte = hte;
        listeners = new ArrayList<>();
    }
    public void tick() { // tickevent
        generateEvent();
    }

    public void registerListener(HotelEventListener listener) { // voeg listener toe aan de lijst van listeners
        listeners.add(listener);
    }

    public void deregisterListener(HotelEventListener listener) { // remove listener uit de lijst van listeners
        listeners.remove(listener);
    }

    public void setHte(int nieuweHte) {
        this.hte = nieuweHte;
        if (timer != null) {
            timer.stop(); // stop de huidige timer
            timer = new Timer(hte, e -> generateEvent()); // maak nieuwe timer met nieuwe interval
            timer.start(); // start de nieuwe timer
            if (paused) {
                pause();
            }
        }
    }

    public void startScenario(){

    }

    public void startTimer() {
        timer = new Timer(hte, e -> generateEvent()); // maak de timer aan, tick per hte en per tick genereer een event
        timer.start(); // startTimer de klok
    }

    private void generateEvent() {
        time += 1000; // tijd aanpassen

        int kansOpRandomEvent = rand.nextInt(1,500); // kies een random getal tussen 1 en 500


        HotelEventType[] values = HotelEventType.values(); // krijg de values van de enum (dan krijg je bijv 0 = NONE)

        HotelEventType gekozenEvent = null;

        if (kansOpRandomEvent >= 400 && kansOpRandomEvent < 470){ // als het getal 4 is
            gekozenEvent = values[rand.nextInt(values.length-4)]; // kies een random guest-only event 14% van de tijd
            System.out.println("Random Gast event gekozen!");
        } else if (kansOpRandomEvent >= 470 && kansOpRandomEvent < 485){
            gekozenEvent = values[7]; // kies Start Cinema 3% van de tijd
            System.out.println("Lager probability event gekozen!");
        } else if (kansOpRandomEvent >= 485 && kansOpRandomEvent < 495){
            gekozenEvent = values[8]; // kies Evacuate 2% van de tijd
            System.out.println("Lager probability event gekozen!");
        } else if (kansOpRandomEvent >= 495 && kansOpRandomEvent < 500){
            gekozenEvent = values[9]; // kies Godzilla 1% van de tijd
            System.out.println("Lager probability event gekozen!");
        } else {
            gekozenEvent = values[0]; // kies NONE 80% van de tijd
            System.out.println("NONE event gekozen!");
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

    public static void pause() {
        if (paused) {
            paused = false;
            timer.start();
            OverzichtScherm.setInvisible();
        } else {
            paused = true;
            timer.stop();
            OverzichtScherm.setVisible();
        }
    }

    public int getTime() {
        return time;
    }

    public void stop() { // stop de timer
        timer.stop();
    }
}