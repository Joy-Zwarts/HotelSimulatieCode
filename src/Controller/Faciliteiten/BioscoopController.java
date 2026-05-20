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

    // voeg gast toe aan lijst van gasten in de bios
    @Override
    public void goToCinemaEvent(HotelEvent hotelEvent) {
        gastenInBios.add(hotelEvent.getGuestId());
    }

    // kies een random tijd voor de film en reset de timer
    @Override
    public void startCinemaEvent(HotelEvent hotelEvent) {
        filmTimer = 1;
        filmEindTijd = rand.nextInt(15, 30);
        System.out.println("De film is gestart! Duur: " + filmEindTijd + " ticks.");
    }

    // per tick de timer ophogen en checken of de timer de eindtijd heeft bereikt
    @Override
    public void noneEvent(HotelEvent event) {
        if (filmTimer != 0) {
            filmTimer++;

            // controleer of de timer de eindtijd heeft bereikt als er is opgehoogd
            if (filmTimer >= filmEindTijd) {
                System.out.println("De film is afgelopen. Gasten verlaten de bioscoop.");
                for (bioscoopOver listener : listeners) {
                    listener.gaWegUitBioscoop(gastenInBios);
                }
                gastenInBios.clear();
                filmTimer = 0;
            }
        }
    }

    public void addlisteners(bioscoopOver listener) {
        listeners.add(listener);
    }
}