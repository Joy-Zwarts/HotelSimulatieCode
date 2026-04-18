package Controller.GastManagement;

import Controller.Layout.GridVakjeController;
import Controller.Layout.LayoutController;
import Controller.Layout.LayoutGeladen;
import Controller.Layout.Locatie;
import Controller.PersoonFactory.GastCreator;
import Model.Personen.GastModel;
import Model.Ruimtes.KamerModel;
import View.Systeem.OverzichtView;
import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;
import hotelevents.HotelEventManager;
import hotelevents.HotelEventType;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersoonController implements HotelEventListener, LayoutGeladen {

    // Attributen
    private final OverzichtView view;
    private final GastCreator factory;
    private final ArrayList<NewGuest> listeners;
    private final ReceptieController receptie;
    private Locatie startLocatie;
    private LayoutController layoutController;

    // Bewegingsbeheer
    private final Map<Integer, PathFinder> actieveRoutes;
    private final Map<Integer, GastModel> actieveGasten;
    private Timer bewegingsTimer;

    public PersoonController(HotelEventManager hotelEventManager, OverzichtView overzichtView, ReceptieController receptieController) {
        this.view = overzichtView;
        this.receptie = receptieController;
        this.listeners = new ArrayList<>();
        this.actieveRoutes = new HashMap<>();
        this.actieveGasten = new HashMap<>();

        this.factory = new GastCreator();
        hotelEventManager.register(this);

        initBewegingsTimer();
    }

    private void initBewegingsTimer() {
        // Elke 500ms zetten alle actieve gasten één stap
        bewegingsTimer = new Timer(500, e -> moveGasten());
        bewegingsTimer.start();
    }

    private void moveGasten() {
        // Kopie van de keys om ConcurrentModificationException te voorkomen tijdens het lopen
        for (Integer gastId : new ArrayList<>(actieveRoutes.keySet())) {
            GastModel gast = actieveGasten.get(gastId);
            PathFinder pf = actieveRoutes.get(gastId);

            if (gast != null && pf != null) {
                if (!pf.isBestemmingBereikt()) {
                    Locatie oudeLocatie = gast.getLocatie();
                    Locatie volgendeStap = pf.getNextStep();

                    // Update model
                    gast.getLocatie().setX(volgendeStap.getX());
                    gast.getLocatie().setY(volgendeStap.getY());

                    // Update View via listeners (GastPlaatser)
                    for (NewGuest listener : listeners) {
                        listener.onGastVerplaatst(gast, oudeLocatie);
                    }
                } else {
                    // Gast is op bestemming aangekomen
                    actieveRoutes.remove(gastId);
                    afhandelenAankomst(gast);
                }
            }
        }
    }

    private void afhandelenAankomst(GastModel gast) {
        // Check of de gast bij de uitgang is (na een checkout)
        if (gast.getLocatie().equals(startLocatie)) {
            for (NewGuest listener : listeners) {
                listener.onGastVertrokken(gast);
            }
            actieveGasten.remove(gast.getGastID());
        } else {
            System.out.println("Gast " + gast.getGastID() + " is aangekomen in kamer.");
        }
    }

    @Override
    public void notify(HotelEvent hotelEvent) {
        if (hotelEvent.getEventType() == HotelEventType.CHECK_IN) {
            handleCheckIn(hotelEvent);
        } else if (hotelEvent.getEventType() == HotelEventType.CHECK_OUT) {
            handleCheckOut(hotelEvent);
        }
    }

    private void handleCheckIn(HotelEvent hotelEvent) {
        // 1. Maak de gast aan op startlocatie
        GastModel gast = (GastModel) factory.createPersoon(
                hotelEvent.getGuestId(),
                new Locatie(startLocatie.getX(), startLocatie.getY()),
                new Locatie(0,0), // Tijdelijk leeg target
                hotelEvent.getData()
        );

        actieveGasten.put(gast.getGastID(), gast);

        // 2. TRIGGER LISTENERS (Nu krijgt de gast zijn kamer via RoomAssign!)
        for (NewGuest listener : listeners) {
            listener.onGastAangemaakt(gast);
        }

        // 3. NU PAS de route berekenen met het definitieve target van de gast
        if (gast.getTargetLocatie() != null) {
            PathFinder pf = new PathFinder(gast.getLocatie(), gast.getTargetLocatie(), layoutController);
            actieveRoutes.put(gast.getGastID(), pf);
        }
    }

    private void handleCheckOut(HotelEvent hotelEvent) {
        GastModel gast = actieveGasten.get(hotelEvent.getGuestId());

        if (gast != null) {
            // Zet target naar de uitgang (startLocatie)
            gast.getTargetLocatie().setX(startLocatie.getX());
            gast.getTargetLocatie().setY(startLocatie.getY());

            // Maak nieuwe route naar de uitgang
            PathFinder pf = new PathFinder(gast.getLocatie(), startLocatie, layoutController);
            actieveRoutes.put(gast.getGastID(), pf);
        }
    }

    public void setNewGuestListener(NewGuest listener) {
        listeners.add(listener);
    }

    @Override
    public void onLayoutGeladen(LayoutController layoutController) {
        this.layoutController = layoutController;
        // De ingang/lobby bevindt zich onderaan in het midden
        int x = layoutController.getView().getGridBreedte() / 2;
        int y = layoutController.getView().getGridLengte() - 1;
        startLocatie = new Locatie(x, y);
    }

    // Handig voor pauze-knop functionaliteit
    public void pauzeerSimulatie(boolean pauze) {
        if (pauze) bewegingsTimer.stop();
        else bewegingsTimer.start();
    }
}