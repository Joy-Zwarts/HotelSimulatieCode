package Controller.GastManagement;

import Controller.Layout.LayoutController;
import Controller.Layout.LayoutGeladen;
import Controller.Layout.Locatie;
import Controller.PersoonFactory.GastCreator;
import Model.Personen.GastModel;
import View.Systeem.OverzichtView;
import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;
import hotelevents.HotelEventManager;
import hotelevents.HotelEventType;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersoonController implements HotelEventListener, LayoutGeladen {

    // attributen
    private final GastCreator factory;
    private final ArrayList<NewGuest> listeners;
    private Locatie startLocatie;
    private LayoutController layoutController;
    private final Map<Integer, PathFinder> actieveRoutes;
    private final Map<Integer, GastModel> actieveGasten;
    private Timer bewegingsTimer;
    private int hteSnelheid = 1000;

    // constructor
    public PersoonController(HotelEventManager hotelEventManager, OverzichtView overzichtView, ReceptieController receptieController) {
        this.listeners = new ArrayList<>();
        this.actieveRoutes = new HashMap<>();
        this.actieveGasten = new HashMap<>();

        this.factory = new GastCreator();
        hotelEventManager.register(this);

        initBewegingsTimer();
    }

    private void initBewegingsTimer() {
        bewegingsTimer = new Timer(hteSnelheid, e -> moveGasten());
        bewegingsTimer.start();
    }

    // beweeg de gast met hulp van de pathfinder
    private void moveGasten() {
        int vertraging = 0;
        for (Integer gastId : new ArrayList<>(actieveRoutes.keySet())) {
            GastModel gast = actieveGasten.get(gastId);
            PathFinder pf = actieveRoutes.get(gastId);

            if (gast != null && pf != null) {
                // gebruik een kleine vertraging per gast
                final int finalDelay = vertraging;
                Timer delayTimer = new Timer(finalDelay, event -> {
                    if (!pf.isBestemmingBereikt()) {
                        Locatie oudeLocatie = new Locatie(gast.getLocatie().getX(), gast.getLocatie().getY());
                        Locatie volgendeStap = pf.getNextStep();

                        gast.getLocatie().setX(volgendeStap.getX());
                        gast.getLocatie().setY(volgendeStap.getY());

                        // render elke gast apart anders blijven gasten hangen op vakjes(is nog een beetje lelijk)
                        SwingUtilities.invokeLater(() -> {
                            for (NewGuest listener : listeners) {
                                listener.onGastVerplaatst(gast, oudeLocatie); // notify listeners
                            }
                        });
                    } else {
                        actieveRoutes.remove(gastId);
                        afhandelenAankomst(gast);
                    }
                });
                delayTimer.setRepeats(false);
                delayTimer.start();

                vertraging += 10; // elke volgende gast beweegt 10ms later zodat ze niet clashen
            }
        }
    }

    private void afhandelenAankomst(GastModel gast) {
        // check of de gast bij de ingang/uitgang is
        if (gast.getLocatie().equals(startLocatie)) {
            for (NewGuest listener : listeners) {
                listener.onGastVertrokken(gast);
            }
            actieveGasten.remove(gast.getGastID());
        } else {
            System.out.println("Gast " + gast.getGastID() + " is aangekomen in kamer.");
        }
    }

    // per tick bij handle check in bij een check in event en vice versa met een check out event
    @Override
    public void notify(HotelEvent hotelEvent) {
        if (hotelEvent.getEventType() == HotelEventType.CHECK_IN) {
            handleCheckIn(hotelEvent);
        } else if (hotelEvent.getEventType() == HotelEventType.CHECK_OUT) {
            handleCheckOut(hotelEvent);
        }
    }

    private void handleCheckIn(HotelEvent hotelEvent) {
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
    private void handleCheckOut(HotelEvent hotelEvent) {
        GastModel gast = actieveGasten.get(hotelEvent.getGuestId());

        if (gast != null) {
            // zet target naar de uitgang (startLocatie)
            gast.getTargetLocatie().setX(startLocatie.getX());
            gast.getTargetLocatie().setY(startLocatie.getY());

            PathFinder pf = new PathFinder(gast.getLocatie(), startLocatie, layoutController);
            actieveRoutes.put(gast.getGastID(), pf);
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
    }
}