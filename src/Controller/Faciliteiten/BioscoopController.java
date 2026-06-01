package Controller.Faciliteiten;

import Controller.Events.Interfaces.cinemaEvent;
import Controller.Faciliteiten.Interfaces.bioscoopOver;
import Controller.PersoonManagement.Interfaces.NewGast;
import Controller.Timer.WachtTimer;
import Model.Layout.Locatie;
import Model.Personen.Activiteit;
import Model.Personen.GastModel;
import View.Systeem.TijdsDuur;
import Controller.Systeem.Interfaces.settingsListener;
import hotelevents.HotelEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BioscoopController implements cinemaEvent, NewGast, settingsListener {
    private final ArrayList<bioscoopOver> listeners;
    private final Random rand = new Random();
    private final ArrayList<Integer> gastenInBios;
    private final WachtTimer  wachtTimer;
    private final HashMap<TijdsDuur, Integer> maxTijd;
    private final HashMap<TijdsDuur, Integer> minTijd;
    private TijdsDuur filmDuur = TijdsDuur.NORMAAL;

    public BioscoopController(WachtTimer timer) {
        this.listeners = new ArrayList<>();
        this.gastenInBios = new ArrayList<>();
        this.wachtTimer = timer;
        this.maxTijd = new HashMap<>();
        this.minTijd = new HashMap<>();

        minTijd.put(TijdsDuur.LANG, 25);
        minTijd.put(TijdsDuur.NORMAAL, 15);
        minTijd.put(TijdsDuur.KORT, 8);

        maxTijd.put(TijdsDuur.LANG, 35);
        maxTijd.put(TijdsDuur.NORMAAL, 25);
        maxTijd.put(TijdsDuur.KORT, 17);
    }

    // voeg gast toe aan lijst van gasten in de gym
    @Override
    public void goToCinemaEvent(HotelEvent hotelEvent) {
        gastenInBios.add(hotelEvent.getGuestId());
    }

    // kies een random tijd voor de film en start de timer
    @Override
    public void startCinemaEvent(HotelEvent hotelEvent) {
        int verblijfTijd = rand.nextInt(minTijd.get(filmDuur), maxTijd.get(filmDuur));

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

    @Override
    public void onGastAangemaakt(GastModel gast) {

    }

    @Override
    public void onGastVertrokken(GastModel gast) {

    }

    @Override
    public void onGastVerplaatst(GastModel gast, Locatie oudeLocatie) {

    }

    @Override
    public void onGastAangekomenInKamer(GastModel gast, Locatie behaaldeLocatie) {
        int gastId = gast.getID();
        if (gastenInBios.contains(gast.getID())) {
            System.out.println("Gast " + gastId + " is aangekomen in de Bioscoop");

            gast.setActiviteit(Activiteit.FILM);
        }
    }

    @Override
    public void onGastGaatWegUitKamer(GastModel gast, Locatie oudeLocatie) {

    }

    @Override
    public void schoonmaakTijdVeranderd(TijdsDuur tijdsDuur) {

    }

    @Override
    public void filmDuurVeranderd(TijdsDuur tijdsDuur) {
        this.filmDuur = tijdsDuur;
    }

    @Override
    public void aantalSchoonmakersVeranderd(int aantalSchoonmakers) {

    }

    @Override
    public void restaurantCapaciteitVeranderd(int restaurantCapaciteit) {

    }

    @Override
    public void trapLoopDuurVeranderd(int trapLoopDuur) {

    }

    @Override
    public void gastMaxWachttijdVeranderd(int gastMaxWachttijd) {

    }
}