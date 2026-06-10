package Controller.PersoonManagement;

import Controller.Events.Interfaces.*;
import Controller.Faciliteiten.Interfaces.bioscoopOver;
import Controller.Faciliteiten.Interfaces.fitnessOver;
import Controller.Faciliteiten.Interfaces.restaurantOver;
import Controller.Layout.LayoutController;
import Controller.PersoonManagement.Interfaces.NewGast;
import Controller.Systeem.Interfaces.onTimeChange;
import Controller.Systeem.Interfaces.reset;
import Model.Layout.Locatie;
import Controller.PersoonFactory.GastCreator;
import Model.Personen.*;
import Model.Ruimtes.KamerType;
import View.Systeem.TijdsDuur;
import hotelevents.HotelEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GastController extends EntiteitenController implements checkInEvent, checkOutEvent, onTimeChange, needFoodEvent, fitnessEvent, cinemaEvent, BeweegHelper.MovementListener, bioscoopOver, restaurantOver, fitnessOver, reset {

    // attributen
    private final GastCreator factory;
    private final ArrayList<NewGast> listeners;
    private Locatie startLocatie;
    private final Map<Integer, GastModel> gasten;

    // constructor
    public GastController() {
        super();
        this.listeners = new ArrayList<>();
        this.gasten = new HashMap<>();
        this.factory = new GastCreator();
    }

    private void afhandelenAankomst(GastModel gast) {
        // als de gast bij de uitgang is
        if (gast.getLocatie().equals(startLocatie)) {
            for (NewGast listener : listeners) {
                listener.onGastVertrokken(gast);
            }
            gasten.remove(gast.getID());
            return;
        }

        if ((layoutController.getModel().getRuimteBijLocatie(gast.getLocatie()).getAreaType().equals(KamerType.RESTAURANT)) ||
                (layoutController.getModel().getRuimteBijLocatie(gast.getLocatie()).getAreaType().equals(KamerType.FITNESS)) ||
                (layoutController.getModel().getRuimteBijLocatie(gast.getLocatie()).getAreaType().equals(KamerType.CINEMA)) ||
                (layoutController.getModel().getRuimteBijLocatie(gast.getLocatie()).getAreaType().equals(KamerType.LIFT)) ||
                (layoutController.getModel().getRuimteBijLocatie(gast.getLocatie()).getAreaType().equals(KamerType.SCHACHT)) ||
                (layoutController.getModel().getRuimteBijLocatie(gast.getLocatie()).getAreaType().equals(KamerType.LOBBY)) ||
                (layoutController.getModel().getRuimteBijLocatie(gast.getLocatie()).getAreaType().equals(KamerType.TRAPPEN))
        ) {
            for (NewGast listener : listeners) {
                listener.onGastAangekomenInKamer(gast, gast.getLocatie());
            }
            return;
        }

        gast.setActiviteit(Activiteit.IN_KAMER);
        gast.setVorigeLocatie(new Locatie(gast.getLocatie().getX(), gast.getLocatie().getY()));

        for (NewGast listener : listeners) {
            listener.onGastAangekomenInKamer(gast, gast.getLocatie());
        }
        System.out.println("Gast " + gast.getID() + " is aangekomen in hun hotelkamer.");
    }

    // zet een nieuwe listener als er een gast wordt aangemaakt
    public void setNewGuestListener(NewGast listener) {
        listeners.add(listener);
    }


    // zet de startlocatie nadat de layout is geladen
    @Override
    public void onLayoutGeladen(LayoutController layoutController) {
        this.layoutController = layoutController;
        // onder midden van de grid (waar de lobby is)
        int x = layoutController.getView().getGridBreedte() / 2;
        int y = layoutController.getView().getGridLengte() - 1;
        startLocatie = new Locatie(x, y);
    }

    // bij een check in event, maak een nieuwe gast, laat de abonnees het weten en stuur hun naar hun kamer
    @Override
    public void checkIn(HotelEvent hotelEvent) {

        // failsafe als de gast al bestaat
        if (gasten.containsKey(hotelEvent.getGuestId())) {
            return;
        }

        // maak de gast aan op startlocatie
        GastModel gast = (GastModel) factory.createPersoon(hotelEvent.getGuestId(), new Locatie(startLocatie.getX(), startLocatie.getY()), new Locatie(0, 0), hotelEvent.getData(), null, TypePersoon.GAST);

        gasten.put(gast.getID(), gast);

        // notify de listener dat er een nieuwe gast is gemaakt
        for (NewGast listener : listeners) {
            listener.onGastAangemaakt(gast);
        }

        // bereken de route naar hun kamer
        if (gast.getTargetLocatie() != null) {
            PathFinder pf = new PathFinder(gast.getLocatie(), gast.getTargetLocatie(), layoutController);
            beweegHelper.voegRouteToe(gast, pf);
            gast.setActiviteit(Activiteit.ONDERWEG);
        }
    }

    // maakt een nieuwe route naar de uitgang
    @Override
    public void checkOut(HotelEvent hotelEvent) {
        GastModel gast = gasten.get(hotelEvent.getGuestId());

        if (gast != null) {
            PathFinder pf = new PathFinder(gast.getLocatie(), startLocatie, layoutController);
            beweegHelper.voegRouteToe(gast, pf);
        }
    }

    // als de snelheid veranderd, zet dit als de hte waarop de gasten bewegen
    @Override
    public void timeChange(int HTE) {
        // vertel de engine dat de snelheid is veranderd
        if (beweegHelper != null) {
            beweegHelper.setSpeed(HTE);
        }
    }

    @Override
    public void needFood(HotelEvent hotelEvent) {
        GastModel gast = gasten.get(hotelEvent.getGuestId());
        if (gast != null) {
            Locatie restaurantLocatie = layoutController.vindLocatie(KamerType.RESTAURANT);
            if (restaurantLocatie != null) {
                PathFinder pf = new PathFinder(gast.getLocatie(), restaurantLocatie, layoutController);
                beweegHelper.voegRouteToe(gast, pf);


                gast.setActiviteit(Activiteit.ONDERWEG);

                System.out.println("Gast " + gast.getID() + " heeft honger en loopt naar het restaurant.");
            }
        }
    }

    // bij elke stap laat de abonnees weten dat de gast is verplaatst
    @Override
    public void onStepTaken(EntiteitenModel Entiteit, Locatie oudeLocatie) {
        GastModel gast = (GastModel) Entiteit;

        for (NewGast listener : listeners) {
            listener.onGastVerplaatst(gast, oudeLocatie);
        }

        // als de gast is verplaatst vanuit hun vorige target locatie laat dit ook weten
        if (gast.getVorigeLocatie() != null && gast.getVorigeLocatie().equals(oudeLocatie)) {
            for (NewGast listener : listeners) {
                listener.onGastGaatWegUitKamer(gast, oudeLocatie);
            }
        }
    }

    @Override
    public void onDestinationReached(EntiteitenModel Entiteit) {
        afhandelenAankomst((GastModel) Entiteit);
    }

    @Override
    public void goToFitnessEvent(HotelEvent hotelEvent) {
        GastModel gast = gasten.get(hotelEvent.getGuestId());

        if (gast != null) {
            // zoek een restaurant locatie
            Locatie fitnessLocatie = layoutController.vindLocatie(KamerType.FITNESS);

            if (fitnessLocatie != null) {
                // maak een nieuwe Pathfinder naar het restaurant
                PathFinder pf = new PathFinder(gast.getLocatie(), fitnessLocatie, layoutController);

                // geef de route aan de engine (deze overschrijft de oude route)
                beweegHelper.voegRouteToe(gast, pf);
                gast.setActiviteit(Activiteit.ONDERWEG);

                System.out.println("Gast " + gast.getID() + " wilt sporten en loopt naar de gym.");
            } else {
                System.out.println("Geen gym gevonden in het hotel!");
            }
        }
    }

    @Override
    public void goToCinemaEvent(HotelEvent hotelEvent) {
        GastModel gast = gasten.get(hotelEvent.getGuestId());

        if (gast != null) {
            // zoek een restaurant locatie
            Locatie bioscoopLocatie = layoutController.vindLocatie(KamerType.CINEMA);

            if (bioscoopLocatie != null) {
                // maak een nieuwe Pathfinder naar het restaurant
                PathFinder pf = new PathFinder(gast.getLocatie(), bioscoopLocatie, layoutController);

                // geef de route aan de engine (deze overschrijft de oude route)
                beweegHelper.voegRouteToe(gast, pf);
                gast.setActiviteit(Activiteit.ONDERWEG);

                System.out.println("Gast " + gast.getID() + " wilt film kijken en loopt naar de bioscoop.");
            } else {
                System.out.println("Geen bioscoop gevonden in het hotel!");
            }
        }
    }

    @Override
    public void startCinemaEvent(HotelEvent hotelEvent) {

    }

    @Override
    public void gaWegUitBioscoop(ArrayList<Integer> gastenInBios) {
        for (int gastID : gastenInBios) {
            GastModel gast = gasten.get(gastID);
            if (gast.getTargetLocatie() != null) {
                PathFinder pf = new PathFinder(gast.getLocatie(), gast.getTargetLocatie(), layoutController);
                beweegHelper.voegRouteToe(gast, pf);
                gast.setActiviteit(Activiteit.ONDERWEG);
            }
            for (NewGast listener : listeners) {
                listener.onGastGaatWegUitKamer(gast, gast.getLocatie());
            }
        }
    }

    @Override
    public void gaWegUitRestaurant(int gastID) {
        GastModel gast = gasten.get(gastID);
        if (gast != null && gast.getTargetLocatie() != null) {

            for (NewGast listener : listeners) {
                listener.onGastGaatWegUitKamer(gast, gast.getLocatie());
            }

            PathFinder pf = new PathFinder(gast.getLocatie(), gast.getTargetLocatie(), layoutController);
            beweegHelper.voegRouteToe(gast, pf);
            gast.setActiviteit(Activiteit.ONDERWEG);
        }
    }

    @Override
    public void gastGeweigerd(int gastID) {
        GastModel gast = gasten.get(gastID);
        if (gast != null && gast.getTargetLocatie() != null) {

            // sla de restaurant-locatie op als vorige locatie
            gast.setVorigeLocatie(new Locatie(gast.getLocatie().getX(), gast.getLocatie().getY()));

            // bereken de route terug naar hun hotelkamer
            PathFinder pf = new PathFinder(gast.getLocatie(), gast.getTargetLocatie(), layoutController);
            beweegHelper.voegRouteToe(gast, pf);

            gast.setActiviteit(Activiteit.ONDERWEG);
        }
    }

    @Override
    public void gaWegUitGym(int gastID) {
        GastModel gast = gasten.get(gastID);
        if (gast.getTargetLocatie() != null) {
            PathFinder pf = new PathFinder(gast.getLocatie(), gast.getTargetLocatie(), layoutController);
            beweegHelper.voegRouteToe(gast, pf);
            gast.setActiviteit(Activiteit.ONDERWEG);
        }
        for (NewGast listener : listeners) {
            listener.onGastGaatWegUitKamer(gast, gast.getLocatie());
        }
    }

    @Override
    public void resetSimulatie() {
        super.resetController();
        this.gasten.clear();
    }

    @Override
    public void schoonmaakTijdVeranderd(TijdsDuur tijdsDuur) {

    }

    @Override
    public void filmDuurVeranderd(TijdsDuur tijdsDuur) {

    }

    @Override
    public void aantalSchoonmakersVeranderd(int aantalSchoonmakers) {

    }

    @Override
    public void restaurantCapaciteitVeranderd(int restaurantCapaciteit) {

    }

    @Override
    public void trapLoopDuurVeranderd(int trapLoopDuur) {
        beweegHelper.setTrapVertragingTicks(trapLoopDuur);
    }

    @Override
    public void gastMaxWachttijdVeranderd(int gastMaxWachttijd) {

    }
}