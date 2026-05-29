package Controller.PersoonManagement;

import Controller.Events.Interfaces.*;
import Controller.Faciliteiten.Interfaces.bioscoopOver;
import Controller.Faciliteiten.Interfaces.fitnessOver;
import Controller.Faciliteiten.Interfaces.restaurantOver;
import Controller.Layout.LayoutController;
import Controller.PersoonManagement.Interfaces.NewGast;
import Controller.Systeem.Intefaces.onTimeChange;
import Controller.Systeem.Intefaces.reset;
import Model.Layout.Locatie;
import Controller.PersoonFactory.GastCreator;
import Model.Personen.Activiteit;
import Model.Personen.GastModel;
import Model.Personen.PersoonModel;
import Model.Personen.TypePersoon;
import Model.Ruimtes.KamerType;
import hotelevents.HotelEvent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GastController extends PersoonController implements checkInEvent, checkOutEvent, onTimeChange, needFoodEvent, fitnessEvent, cinemaEvent, BeweegHelper.MovementListener, bioscoopOver, restaurantOver, fitnessOver, reset {

    // attributen
    private final GastCreator factory;
    private final ArrayList<NewGast> listeners;
    private Locatie startLocatie;
    private final Map<Integer, GastModel> actieveGasten;

    // constructor
    public GastController() {
        super();
        this.listeners = new ArrayList<>();
        this.actieveGasten = new HashMap<>();
        this.factory = new GastCreator();
    }

    // check na het lopen of de gast naar de uitgang is gelopen of niet
    // check na het lopen of de gast naar de uitgang is gelopen of niet
    private void afhandelenAankomst(GastModel gast) {
        // gast is bij de uitgang aangekomen en verlaat het hotel
        if (gast.getLocatie().equals(startLocatie)) {
            for (NewGast listener : listeners) {
                listener.onGastVertrokken(gast);
            }
            actieveGasten.remove(gast.getID());
        }
        // gast is aangekomen bij een faciliteit (restaurant, fitness, cinema)
        else if (gast.getActivity() == Activiteit.ETEN || gast.getActivity() == Activiteit.SPORTEN || gast.getActivity() == Activiteit.FILM) {
            for (NewGast listener : listeners) {
                listener.onGastAangekomenInKamer(gast, gast.getLocatie());
            }
        }
        // er blijft nog over dat de gast in hun kamer zit
        else {
            gast.setActivity(Activiteit.IN_KAMER); // ze de status naar IN_KAMER
            gast.setVorigeLocatie(new Locatie(gast.getLocatie().getX(), gast.getLocatie().getY()));

            for (NewGast listener : listeners) {
                listener.onGastAangekomenInKamer(gast, gast.getLocatie());
            }
            System.out.println("Gast " + gast.getID() + " is aangekomen in hun hotelkamer.");
        }
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
        if (actieveGasten.containsKey(hotelEvent.getGuestId())) {
            return;
        }

        // maak de gast aan op startlocatie
        GastModel gast = (GastModel) factory.createPersoon(hotelEvent.getGuestId(), new Locatie(startLocatie.getX(), startLocatie.getY()), new Locatie(0, 0), hotelEvent.getData(), null, TypePersoon.GAST);

        actieveGasten.put(gast.getID(), gast);

        // notify de listener dat er een nieuwe gast is gemaakt
        for (NewGast listener : listeners) {
            listener.onGastAangemaakt(gast);
        }

        // bereken de route naar hun kamer
        if (gast.getTargetLocatie() != null) {
            PathFinder pf = new PathFinder(gast.getLocatie(), gast.getTargetLocatie(), layoutController);
            beweegHelper.voegRouteToe(gast, pf);
            gast.setActivity(Activiteit.ONDERWEG);
        }
    }

    // maakt een nieuwe route naar de uitgang
    @Override
    public void checkOut(HotelEvent hotelEvent) {
        GastModel gast = actieveGasten.get(hotelEvent.getGuestId());

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
        GastModel gast = actieveGasten.get(hotelEvent.getGuestId());

        if (gast != null) {
            // zoek een restaurant locatie
            Locatie restaurantLocatie = layoutController.vindLocatie(KamerType.RESTAURANT);

            if (restaurantLocatie != null) {
                // maak een nieuwe Pathfinder naar het restaurant
                PathFinder pf = new PathFinder(gast.getLocatie(), restaurantLocatie, layoutController);

                // geef de route aan de engine (deze overschrijft de oude route)
                beweegHelper.voegRouteToe(gast, pf);

                System.out.println("Gast " + gast.getID() + " heeft honger en loopt naar het restaurant.");
            } else {
                System.out.println("Geen restaurant gevonden in het hotel!");
            }
        }
    }

    // bij elke stap laat de abonnees weten dat de gast is verplaatst
    @Override
    public void onStepTaken(PersoonModel persoon, Locatie oudeLocatie) {
        GastModel gast = (GastModel) persoon;

        gast.setActivity(Activiteit.ONDERWEG);

        SwingUtilities.invokeLater(() -> {
            for (NewGast listener : listeners) {
                listener.onGastVerplaatst(gast, oudeLocatie);
            }
        });

        // als de gast is verplaatst vanuit hun vorige target locatie laat dit ook weten (bijv. voor de drukte teller in kamers)
        if (gast.getVorigeLocatie() != null && gast.getVorigeLocatie().equals(oudeLocatie)) {
            for (NewGast listener : listeners) {
                listener.onGastGaatWegUitKamer(gast, oudeLocatie);
            }
        }
    }

    @Override
    public void onDestinationReached(PersoonModel persoon) {
        afhandelenAankomst((GastModel) persoon);
    }

    @Override
    public void goToFitnessEvent(HotelEvent hotelEvent) {
        GastModel gast = actieveGasten.get(hotelEvent.getGuestId());

        if (gast != null) {
            // zoek een restaurant locatie
            Locatie fitnessLocatie = layoutController.vindLocatie(KamerType.FITNESS);

            if (fitnessLocatie != null) {
                // maak een nieuwe Pathfinder naar het restaurant
                PathFinder pf = new PathFinder(gast.getLocatie(), fitnessLocatie, layoutController);

                // geef de route aan de engine (deze overschrijft de oude route)
                beweegHelper.voegRouteToe(gast, pf);
                gast.setActivity(Activiteit.ONDERWEG);

                System.out.println("Gast " + gast.getID() + " wilt sporten en loopt naar de gym.");
            } else {
                System.out.println("Geen gym gevonden in het hotel!");
            }
        }
    }

    @Override
    public void goToCinemaEvent(HotelEvent hotelEvent) {
        GastModel gast = actieveGasten.get(hotelEvent.getGuestId());

        if (gast != null) {
            // zoek een restaurant locatie
            Locatie bioscoopLocatie = layoutController.vindLocatie(KamerType.CINEMA);

            if (bioscoopLocatie != null) {
                // maak een nieuwe Pathfinder naar het restaurant
                PathFinder pf = new PathFinder(gast.getLocatie(), bioscoopLocatie, layoutController);

                // geef de route aan de engine (deze overschrijft de oude route)
                beweegHelper.voegRouteToe(gast, pf);
                gast.setActivity(Activiteit.ONDERWEG);

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
            GastModel gast = actieveGasten.get(gastID);
            if (gast.getTargetLocatie() != null) {
                PathFinder pf = new PathFinder(gast.getLocatie(), gast.getTargetLocatie(), layoutController);
                beweegHelper.voegRouteToe(gast, pf);
                gast.setActivity(Activiteit.ONDERWEG);
            }
        }
    }

    @Override
    public void gaWegUitRestaurant(int gastID) {
        GastModel gast = actieveGasten.get(gastID);
        if (gast.getTargetLocatie() != null) {
            PathFinder pf = new PathFinder(gast.getLocatie(), gast.getTargetLocatie(), layoutController);
            beweegHelper.voegRouteToe(gast, pf);
            gast.setActivity(Activiteit.ONDERWEG);
        }
    }

    @Override
    public void gaWegUitGym(int gastID) {
        GastModel gast = actieveGasten.get(gastID);
        if (gast.getTargetLocatie() != null) {
            PathFinder pf = new PathFinder(gast.getLocatie(), gast.getTargetLocatie(), layoutController);
            beweegHelper.voegRouteToe(gast, pf);
            gast.setActivity(Activiteit.ONDERWEG);
        }
    }

    @Override
    public void resetSimulatie() {
        super.resetController();
        this.actieveGasten.clear();
    }
}