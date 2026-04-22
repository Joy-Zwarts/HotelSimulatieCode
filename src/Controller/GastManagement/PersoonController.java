package Controller.GastManagement;

import Controller.Events.needFoodEvent;
import Controller.Layout.LayoutController;
import Controller.Layout.LayoutGeladen;
import Controller.Events.checkInEvent;
import Controller.Events.checkOutEvent;
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

public class PersoonController implements LayoutGeladen, checkInEvent, checkOutEvent, onTimeChange, needFoodEvent {

    // attributen
    private final GastCreator factory;
    private final ArrayList<NewGuest> listeners;
    private Locatie startLocatie;
    private LayoutController layoutController;
    private final Map<Integer, PathFinder> actieveRoutes;
    private final Map<Integer, GastModel> actieveGasten;
    private Timer bewegingsTimer;
    private int hteSnelheid;
    private ArrayList<RuimteModel> ruimtes;
    private ReceptieController receptieController;

    // constructor
    public PersoonController(HotelEventManager hotelEventManager, OverzichtView overzichtView, ReceptieController ReceptieController) {
        this.listeners = new ArrayList<>();
        this.actieveRoutes = new HashMap<>();
        this.actieveGasten = new HashMap<>();
        this.hteSnelheid = 1000;
        this.receptieController = ReceptieController;

        this.factory = new GastCreator();

        initBewegingsTimer();
    }

    private void initBewegingsTimer() {
        bewegingsTimer = new Timer(hteSnelheid, e -> moveGasten());
        bewegingsTimer.start();
    }

    // beweeg de gast met hulp van de pathfinder
    private void moveGasten() {
        for (Integer gastId : new ArrayList<>(actieveRoutes.keySet())) {
            GastModel gast = actieveGasten.get(gastId);
            PathFinder pf = actieveRoutes.get(gastId);

            if (gast != null && pf != null) {
                if (!pf.isBestemmingBereikt()) {
                    Locatie oudeLocatie = new Locatie(gast.getLocatie().getX(), gast.getLocatie().getY());
                    Locatie volgendeStap = pf.getNextStep();

                    gast.getLocatie().setX(volgendeStap.getX());
                    gast.getLocatie().setY(volgendeStap.getY());

                    //  icoontje verplaatsen
                    SwingUtilities.invokeLater(() -> {
                        for (NewGuest listener : listeners) {
                            listener.onGastVerplaatst(gast, oudeLocatie);
                        }
                    });
                } else {
                    actieveRoutes.remove(gastId);
                    afhandelenAankomst(gast);
                }
            }
        }
    }

    private void afhandelenAankomst(GastModel gast) {
        if (gast.getLocatie().equals(startLocatie)) {
            // gast verlaat het hotel
            for (NewGuest listener : listeners) {
                listener.onGastVertrokken(gast);
            }
            actieveGasten.remove(gast.getGastID());
        } else {
            // gast is in zijn target kamer aangekomen
            for (NewGuest listener : listeners) {
                listener.onGastAangekomenInKamer(gast, gast.getLocatie());
            }
            System.out.println("Gast " + gast.getGastID() + " is op bestemming. Teller +1.");
        }
    }

    // zet een nieuwe listener als er een gast wordt aangemaakt
    public void setNewGuestListener(NewGuest listener) {
        listeners.add(listener);
    }


    @Override
    public void onLayoutGeladen(LayoutController layoutController) {
        this.layoutController = layoutController;
        // onder midden van de grid (waar de lobby is)
        int x = layoutController.getView().getGridBreedte() / 2;
        int y = layoutController.getView().getGridLengte() - 1;
        startLocatie = new Locatie(x, y);
        ruimtes = layoutController.getModel().getRuimtes();
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
        for (NewGuest listener : listeners) {
            listener.onGastAangemaakt(gast);
        }

        // bereken de route naar hun kamer
        if (gast.getTargetLocatie() != null) {
            PathFinder pf = new PathFinder(gast.getLocatie(), gast.getTargetLocatie(), layoutController);
            actieveRoutes.put(gast.getGastID(), pf);
        }
    }

    // maakt een nieuwe route naar de uitgang
    @Override
    public void checkOutEvent(HotelEvent hotelEvent) {
        GastModel gast = actieveGasten.get(hotelEvent.getGuestId());

        if (gast != null) {
            // zet target naar de uitgang (startLocatie)
            gast.getTargetLocatie().setX(startLocatie.getX());
            gast.getTargetLocatie().setY(startLocatie.getY());

            PathFinder pf = new PathFinder(gast.getLocatie(), startLocatie, layoutController);
            actieveRoutes.put(gast.getGastID(), pf);
        }
    }

    // als de snelheid veranderd, zet dit als de hte waarop de gasten bewegen
    @Override
    public void timeChange(int HTE) {
        this.hteSnelheid = HTE;

        if (bewegingsTimer != null) {
            bewegingsTimer.setDelay(hteSnelheid);
        }
    }

    @Override
    public void needFoodEvent(HotelEvent hotelEvent) {

    }
}