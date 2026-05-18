package Controller.Faciliteiten;

import Controller.Events.cinemaEvent;
import Controller.Events.noneEvent;
import Controller.PersoonManagement.GastController;
import Model.Personen.GastModel;
import hotelevents.HotelEvent;

import java.util.ArrayList;
import java.util.Random;

public class BioscoopController implements cinemaEvent, noneEvent {

    private int filmTimer = 0;
    private int filmEindTijd = 0;
    private final ArrayList<bioscoopOver> listeners;
    private final Random rand = new Random();
    private final ArrayList<Integer> gastenInBios;

    public BioscoopController() {
        this.listeners = new ArrayList<>();
        this.gastenInBios = new ArrayList<>();
    }

    @Override
    public void goToCinemaEvent(HotelEvent hotelEvent) {
        gastenInBios.add(hotelEvent.getGuestId());
    }

    @Override
    public void startCinemaEvent(HotelEvent hotelEvent) {
        filmTimer = 1;
        filmEindTijd = rand.nextInt(15, 30);
        System.out.println("De film is gestart! Duur: " + filmEindTijd + " ticks.");
    }

    @Override
    public void noneEvent(HotelEvent event) { // Throws InterruptedException weggehaald (niet nodig)
        if (filmTimer != 0) {
            filmTimer++;

            // FIX: Controleer óf de timer de eindtijd heeft bereikt tijdens het ophogen
            if (filmTimer >= filmEindTijd) {
                System.out.println("De film is afgelopen. Gasten verlaten de bioscoop.");
                for (bioscoopOver listener : listeners) {
                    listener.gaWegUitBioscoop(gastenInBios);
                }
                gastenInBios.clear();
                filmTimer = 0; // Zet timer terug op inactief
            }
        }
    }

    public void addlisteners(bioscoopOver listener) {
        listeners.add(listener);
    }
}