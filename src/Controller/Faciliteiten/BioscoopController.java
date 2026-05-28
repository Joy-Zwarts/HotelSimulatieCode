package Controller.Faciliteiten;

import Controller.Events.cinemaEvent;
import Controller.PersoonManagement.GastController;
import Controller.PersoonManagement.NewGast;
import Controller.Timer.WachtTimer;
import Model.Layout.Locatie;
import Model.Personen.GastModel;
import hotelevents.HotelEvent;

import java.util.ArrayList;
import java.util.Random;

public class BioscoopController implements cinemaEvent {
    private final ArrayList<bioscoopOver> listeners;
    private final Random rand = new Random();
    private final ArrayList<Integer> gastenInBios;
    private final WachtTimer  wachtTimer;

    public BioscoopController(WachtTimer timer) {
        this.listeners = new ArrayList<>();
        this.gastenInBios = new ArrayList<>();
        this.wachtTimer = timer;
    }

    // voeg gast toe aan lijst van gasten in de gym
    @Override
    public void goToCinemaEvent(HotelEvent hotelEvent) {
        gastenInBios.add(hotelEvent.getGuestId());
    }

    // kies een random tijd voor de film en start de timer
    @Override
    public void startCinemaEvent(HotelEvent hotelEvent) {
        int verblijfTijd = rand.nextInt(15, 30);

        // De bioscoop heeft één centrale timer voor de film
        wachtTimer.startTimer("CINEMA-FILM", () -> stuurGastenWeg(), verblijfTijd);

        System.out.println("De film is gestart! Duur: " + verblijfTijd + " ticks.");
    }

    // als de timer is afgelopen stuur iedere gast in de bioscoop weg
    public void stuurGastenWeg() {
        ArrayList<Integer> vertrekkendeGasten = new ArrayList<>(gastenInBios);

        gastenInBios.clear();

        for (bioscoopOver listener : listeners) {
            listener.gaWegUitBioscoop(vertrekkendeGasten);
        }
    }

    public void addlisteners(bioscoopOver listener) {
        listeners.add(listener);
    }
}