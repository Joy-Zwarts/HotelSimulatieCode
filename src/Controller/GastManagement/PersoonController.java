package Controller.GastManagement;

import Controller.Events.*;
import Controller.Layout.LayoutController;
import Controller.Layout.LayoutGeladen;
import Controller.Systeem.onTimeChange;
import Model.Layout.Locatie;
import Controller.PersoonFactory.GastCreator;
import Model.Personen.GastModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;
import View.Systeem.OverzichtView;
import hotelevents.HotelEvent;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersoonController implements LayoutGeladen, checkInEvent, checkOutEvent, onTimeChange, needFoodEvent, fitnessEvent, cinemaEvent, GastBeweeg.MovementListener {

    // attributen
    private final GastCreator factory;
    private final ArrayList<NewGast> listeners;
    private Locatie startLocatie;
    private LayoutController layoutController;
    private final Map<Integer, GastModel> actieveGasten;
    private GastBeweeg movementEngine;

    // constructor
    public PersoonController(HotelEventManager hotelEventManager, OverzichtView overzichtView, ReceptieController ReceptieController) {
        this.listeners = new ArrayList<>();
        this.actieveGasten = new HashMap<>();
        this.movementEngine = new GastBeweeg(1000, this);
        this.movementEngine.start();

        this.factory = new GastCreator();
    }

    private void afhandelenAankomst(GastModel gast) {
        if (gast.getLocatie().equals(startLocatie)) {
            // gast verlaat het hotel
            for (NewGast listener : listeners) {
                listener.onGastVertrokken(gast);
            }
            actieveGasten.remove(gast.getGastID());
        } else {
            gast.setVorigeLocatie(new Locatie(gast.getLocatie().getX(), gast.getLocatie().getY()));
            // gast is in zijn target kamer aangekomen
            for (NewGast listener : listeners) {
                listener.onGastAangekomenInKamer(gast, gast.getLocatie());
            }
            System.out.println("Gast " + gast.getGastID() + " is op bestemming. Teller +1.");
        }
    }

    // zet een nieuwe listener als er een gast wordt aangemaakt
    public void setNewGuestListener(NewGast listener) {
        listeners.add(listener);
    }


    @Override
    public void onLayoutGeladen(LayoutController layoutController) {
        this.layoutController = layoutController;
        // onder midden van de grid (waar de lobby is)
        int x = layoutController.getView().getGridBreedte() / 2;
        int y = layoutController.getView().getGridLengte() - 1;
        startLocatie = new Locatie(x, y);
    }

    @Override
    public void checkInEvent(HotelEvent hotelEvent) {
        // maak de gast aan op startlocatie
        GastModel gast = (GastModel) factory.createPersoon(
                hotelEvent.getGuestId(),
                new Locatie(startLocatie.getX(), startLocatie.getY()),
                new Locatie(0,0),
                hotelEvent.getData()
        );

        actieveGasten.put(gast.getGastID(), gast);

        // notify de listener dat er een nieuwe gast is gemaakt
        for (NewGast listener : listeners) {
            listener.onGastAangemaakt(gast);
        }

        // bereken de route naar hun kamer
        if (gast.getTargetLocatie() != null) {
            PathFinder pf = new PathFinder(gast.getLocatie(), gast.getTargetLocatie(), layoutController);
            movementEngine.voegRouteToe(gast, pf);
        }
    }

    // maakt een nieuwe route naar de uitgang
    @Override
    public void checkOutEvent(HotelEvent hotelEvent) {
        GastModel gast = actieveGasten.get(hotelEvent.getGuestId());

        if (gast != null) {
            PathFinder pf = new PathFinder(gast.getLocatie(), startLocatie, layoutController);
            // Gebruik de engine in plaats van de lokale actieveRoutes map!
            movementEngine.voegRouteToe(gast, pf);
        }
    }

    // als de snelheid veranderd, zet dit als de hte waarop de gasten bewegen
    @Override
    public void timeChange(int HTE) {
        // vertel de engine dat de snelheid is veranderd
        if (movementEngine != null) {
            movementEngine.setSpeed(HTE);
        }
    }

    @Override
    public void needFoodEvent(HotelEvent hotelEvent) {
        GastModel gast = actieveGasten.get(hotelEvent.getGuestId());

        if (gast != null) {
            // zoek een restaurant locatie
            Locatie restaurantLocatie = layoutController.vindLocatie(KamerType.RESTAURANT);

            if (restaurantLocatie != null) {
                // maak een nieuwe Pathfinder naar het restaurant
                PathFinder pf = new PathFinder(gast.getLocatie(), restaurantLocatie, layoutController);

                // geef de route aan de engine (deze overschrijft de oude route)
                movementEngine.voegRouteToe(gast, pf);

                System.out.println("Gast " + gast.getGastID() + " heeft honger en loopt naar het restaurant.");
            } else {
                System.out.println("Geen restaurant gevonden in het hotel!");
            }
        }
    }

    @Override
    public void onStepTaken(GastModel gast, Locatie oudeLocatie) {
        SwingUtilities.invokeLater(() -> {
            for (NewGast listener : listeners) {
                listener.onGastVerplaatst(gast, oudeLocatie);
            }
        });
        if (gast.getVorigeLocatie() != null) {
            if (gast.getVorigeLocatie().equals(oudeLocatie)) {
                for (NewGast listener : listeners) {
                    listener.onGastGaatWegUitKamer(gast, oudeLocatie);
                }
            }
        }
    }

    @Override
    public void onDestinationReached(GastModel gast) {
        afhandelenAankomst(gast);
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
                movementEngine.voegRouteToe(gast, pf);

                System.out.println("Gast " + gast.getGastID() + " wilt sporten en loopt naar de gym.");
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
                movementEngine.voegRouteToe(gast, pf);

                System.out.println("Gast " + gast.getGastID() + " wilt film kijken en loopt naar de bioscoop.");
            } else {
                System.out.println("Geen bioscoop gevonden in het hotel!");
            }
        }
    }

    @Override
    public void startCinemaEvent(HotelEvent hotelEvent) {

    }
}