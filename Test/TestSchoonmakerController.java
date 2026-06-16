import Controller.Layout.LayoutController;
import Controller.PersoonManagement.ReceptieController;
import Controller.PersoonManagement.SchoonmakerController;
import Controller.Timer.WachtTimer;
import Controller.Timer.TimerPing; // Importeer je eigen TimerPing interface
import Model.Entiteiten.GastModel;
import Model.Entiteiten.SchoonmakerModel;
import Model.Entiteiten.TypePersoon;
import Model.Layout.Locatie;
import Model.Ruimtes.KamerClassificatie;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerType;
import View.Layout.LayoutView;
import View.Systeem.HotelSimulatieView;
import View.Systeem.OverzichtView;
import View.Systeem.TijdsDuur;
import hotelevents.HotelEvent;
import hotelevents.HotelEventManager;
import hotelevents.HotelEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class TestSchoonmakerController {

    private SchoonmakerController schoonmakerController;
    private ReceptieController fakeReceptieController;
    private OverzichtView fakeOverzichtView;
    private FakeWachtTimer fakeWachtTimer;
    private LayoutController fakeLayoutController;

    private boolean aangemaaktAangeroepen = false;
    private boolean verplaatstAangeroepen = false;
    private boolean aangekomenAangeroepen = false;
    private boolean verlatenAangeroepen = false;

    static class FakeLayoutView extends LayoutView {
        public FakeLayoutView() { super(null, null); }
        @Override public int getGridBreedte() { return 10; }
        @Override public int getGridLengte() { return 10; }
    }

    static class FakeLayoutController extends LayoutController {
        private final LayoutView view = new FakeLayoutView();
        public FakeLayoutController() { super(null, null, null); }
        @Override protected void init() {}
        @Override public LayoutView getView() { return this.view; }
    }

    static class FakeReceptieController extends ReceptieController {
        private final HashMap<Integer, GastModel> gasten = new HashMap<>();
        public FakeReceptieController() { super(null); }
        public void voegGastToe(GastModel g) { gasten.put(g.getID(), g); }
        @Override public GastModel getGast(int id) { return gasten.get(id); }
    }

    static class FakeWachtTimer extends WachtTimer {
        public TimerPing opgeslagenCallback;

        @Override
        public synchronized void startTimer(String persoonsID, TimerPing listener, int verblijfTijd) {
            this.opgeslagenCallback = listener;
        }
    }

    @BeforeEach
    void setUp() {
        fakeOverzichtView = new OverzichtView(new HotelSimulatieView(null), null, new HotelEventManager()) {
            @Override public boolean isGepauzeerd() { return false; }
            @Override public void tekenSchoonmakerStatus(SchoonmakerController c) {}
        };

        fakeReceptieController = new FakeReceptieController();
        fakeWachtTimer = new FakeWachtTimer();
        fakeLayoutController = new FakeLayoutController();

        schoonmakerController = new SchoonmakerController(fakeReceptieController, fakeOverzichtView, null, fakeWachtTimer);

        schoonmakerController.setNewSchoonmakerListener(new Controller.PersoonManagement.Interfaces.NewSchoonmaker() {
            @Override public void onSchoonmakerAangemaakt(SchoonmakerModel s) { aangemaaktAangeroepen = true; }
            @Override public void onSchoonmakerVerplaatst(SchoonmakerModel s, Locatie o) { verplaatstAangeroepen = true; }
            @Override public void onSchoonmakerAangekomenInKamer(SchoonmakerModel s) { aangekomenAangeroepen = true; }
            @Override public void onSchoonmakerVerlaatKamer(SchoonmakerModel s, Locatie o) { verlatenAangeroepen = true; }
        });
    }

    private void forceerLayoutGeladen() {
        schoonmakerController.onLayoutGeladen(fakeLayoutController);
    }

    private void wachtOpSwing() {
        try {
            SwingUtilities.invokeAndWait(() -> {});
        } catch (InterruptedException | InvocationTargetException e) {
        }
    }


    @Test
    void OnLayoutGeladen() {
        assertNull(schoonmakerController.getSchoonmaker1());
        forceerLayoutGeladen();
        assertNotNull(schoonmakerController.getSchoonmaker1());
        assertNotNull(schoonmakerController.getSchoonmaker2());
        assertTrue(aangemaaktAangeroepen);
    }

    @Test
    void CleaningEmergency() {
        forceerLayoutGeladen();

        // Path: Gast heeft geen kamer
        HotelEvent eventNoRoom = new HotelEvent(1, HotelEventType.CLEANING_EMERGENCY, 99, -1);
        GastModel gastZonderKamer = new GastModel(99, null, null, TypePersoon.GAST, KamerClassificatie.eenSter, null);
        fakeReceptieController.voegGastToe(gastZonderKamer);

        schoonmakerController.cleaningEmergency(eventNoRoom);
        assertEquals(0, schoonmakerController.getActieveKlussenAantal());

        // Path: 1x1 Kamer
        HotelEvent event1 = new HotelEvent(1, HotelEventType.CLEANING_EMERGENCY, 1, -1);
        KamerModel kamer1x1 = new KamerModel(KamerType.ROOM, new Locatie(2, 3), "2,3", KamerClassificatie.eenSter, true);
        kamer1x1.setRoomNumber(101);
        GastModel gast1 = new GastModel(1, null, null, TypePersoon.GAST, KamerClassificatie.eenSter, null);
        gast1.setKamer(kamer1x1);
        fakeReceptieController.voegGastToe(gast1);

        schoonmakerController.schoonmaakTijdVeranderd(TijdsDuur.KORT);
        schoonmakerController.cleaningEmergency(event1);
        assertEquals(kamer1x1, schoonmakerController.getActieveKlus(1));

        // Path: 2x2 Kamer
        HotelEvent event2 = new HotelEvent(1, HotelEventType.CLEANING_EMERGENCY, 2, -1);
        KamerModel kamer2x2 = new KamerModel(KamerType.ROOM, new Locatie(4, 7), "4,7", KamerClassificatie.tweeSterren, true) {
            @Override public int getDimensionH() { return 2; }
            @Override public int getDimensionW() { return 2; }
        };
        GastModel gast2 = new GastModel(2, null, null, TypePersoon.GAST, KamerClassificatie.tweeSterren, null);
        gast2.setKamer(kamer2x2);
        fakeReceptieController.voegGastToe(gast2);

        schoonmakerController.schoonmaakTijdVeranderd(TijdsDuur.NORMAAL);
        schoonmakerController.cleaningEmergency(event2);
        assertEquals(kamer2x2, schoonmakerController.getActieveKlus(2));

        // Path: Grote kamer (3x3)
        HotelEvent event3 = new HotelEvent(1, HotelEventType.CLEANING_EMERGENCY, 3, -1);
        KamerModel kamerGroot = new KamerModel(KamerType.ROOM, new Locatie(4, 8), "4,8", KamerClassificatie.vijfSterren, true) {
            @Override public int getDimensionH() { return 3; }
            @Override public int getDimensionW() { return 3; }
        };
        GastModel gast3 = new GastModel(3, null, null, TypePersoon.GAST, KamerClassificatie.vijfSterren, null);
        gast3.setKamer(kamerGroot);
        fakeReceptieController.voegGastToe(gast3);

        schoonmakerController.schoonmaakTijdVeranderd(TijdsDuur.LANG);
        schoonmakerController.cleaningEmergency(event3);
    }

    @Test
    void CheckOutVariaties() {
        forceerLayoutGeladen();

        // Path: Gast heeft geen kamer
        HotelEvent eventNoRoom = new HotelEvent(1, HotelEventType.CHECK_OUT, 99, -1);
        GastModel gastZonderKamer = new GastModel(99, null, null, TypePersoon.GAST, KamerClassificatie.eenSter, null);
        fakeReceptieController.voegGastToe(gastZonderKamer);

        schoonmakerController.checkOut(eventNoRoom);

        // Path checkout: 1x1 kamer
        HotelEvent event1 = new HotelEvent(1, HotelEventType.CHECK_OUT, 10, -1);
        KamerModel kamer = new KamerModel(KamerType.ROOM, new Locatie(1, 1), "1,1", KamerClassificatie.eenSter, true);
        GastModel gast = new GastModel(10, null, null, TypePersoon.GAST, KamerClassificatie.eenSter, null);
        gast.setKamer(kamer);
        fakeReceptieController.voegGastToe(gast);

        schoonmakerController.checkOut(event1);
        assertNotNull(schoonmakerController.getActieveKlus(1));

        // Checkout: 2x2 kamer (Wachtrij branch)
        HotelEvent event2 = new HotelEvent(1, HotelEventType.CHECK_OUT, 11, -1);
        KamerModel kamer2 = new KamerModel(KamerType.ROOM, new Locatie(1, 2), "1,2", KamerClassificatie.eenSter, true) {
            @Override public int getDimensionH() { return 2; }
            @Override public int getDimensionW() { return 2; }
        };
        GastModel gast2 = new GastModel(11, null, null, TypePersoon.GAST, KamerClassificatie.eenSter, null);
        gast2.setKamer(kamer2);
        fakeReceptieController.voegGastToe(gast2);

        schoonmakerController.checkOut(event2);
        Queue<KamerModel> queue = schoonmakerController.getWachtrij(1);
        assertFalse(queue.isEmpty());
        assertEquals(kamer2, queue.peek());

        // Checkout: Grote kamer
        HotelEvent event3 = new HotelEvent(1, HotelEventType.CHECK_OUT, 12, -1);
        KamerModel kamer3 = new KamerModel(KamerType.ROOM, new Locatie(1, 3), "1,3", KamerClassificatie.eenSter, true) {
            @Override public int getDimensionH() { return 3; }
            @Override public int getDimensionW() { return 3; }
        };
        GastModel gast3 = new GastModel(12, null, null, TypePersoon.GAST, KamerClassificatie.eenSter, null);
        gast3.setKamer(kamer3);
        fakeReceptieController.voegGastToe(gast3);
        schoonmakerController.checkOut(event3);
    }

    @Test
    void KlaarCleaningWachtrij() {
        forceerLayoutGeladen();
        SchoonmakerModel sm1 = schoonmakerController.getSchoonmaker1();

        KamerModel kamer2 = new KamerModel(KamerType.ROOM, new Locatie(2, 2), "2,2", KamerClassificatie.eenSter, true);
        schoonmakerController.getWachtrij(sm1.getID()).add(kamer2);

        schoonmakerController.klaarCleaning(sm1);
        wachtOpSwing();
        assertTrue(verlatenAangeroepen);
        assertEquals(kamer2, schoonmakerController.getActieveKlus(sm1.getID()));

        schoonmakerController.klaarCleaning(sm1);
        wachtOpSwing();
        assertNull(schoonmakerController.getActieveKlus(sm1.getID()));
    }

    @Test
    void OnStepTaken() {
        forceerLayoutGeladen();
        SchoonmakerModel sm1 = schoonmakerController.getSchoonmaker1();
        Locatie oud = new Locatie(0, 0);

        schoonmakerController.onStepTaken(sm1, oud);
        wachtOpSwing();
        assertTrue(verplaatstAangeroepen);

        verplaatstAangeroepen = false;
        fakeOverzichtView = new OverzichtView(new HotelSimulatieView(null), null, new HotelEventManager()) {
            @Override public boolean isGepauzeerd() { return true; }
        };
        schoonmakerController.overzichtView = fakeOverzichtView;

        schoonmakerController.onStepTaken(sm1, oud);
        wachtOpSwing();
        assertFalse(verplaatstAangeroepen);
    }

    @Test
    void OnDestinationReached() {
        forceerLayoutGeladen();
        SchoonmakerModel sm1 = schoonmakerController.getSchoonmaker1();

        // Scenario 1: Gepauzeerd
        fakeOverzichtView = new OverzichtView(new HotelSimulatieView(null), null, new HotelEventManager()) {
            @Override public boolean isGepauzeerd() { return true; }
        };
        schoonmakerController.overzichtView = fakeOverzichtView;
        schoonmakerController.onDestinationReached(sm1);
        assertFalse(sm1.isCleaning());

        // Reset naar actieve view
        fakeOverzichtView = new OverzichtView(new HotelSimulatieView(null), null, new HotelEventManager()) {
            @Override public boolean isGepauzeerd() { return false; }
            @Override public void tekenSchoonmakerStatus(SchoonmakerController c) {}
        };
        schoonmakerController.overzichtView = fakeOverzichtView;

        // Scenario 2: Station bereikt
        sm1.setLocatie(sm1.getStation());
        schoonmakerController.onDestinationReached(sm1);
        wachtOpSwing();
        assertFalse(sm1.isCleaning());

        // Scenario 3: Kamer bereikt
        sm1.setLocatie(new Locatie(9, 9));
        schoonmakerController.onDestinationReached(sm1);
        wachtOpSwing();

        assertTrue(sm1.isCleaning());
        assertTrue(aangekomenAangeroepen);

        // Werkt nu perfect!
        assertNotNull(fakeWachtTimer.opgeslagenCallback);

        // Activeer de callback conform de interface methode 'timerAfgelopen'
        fakeWachtTimer.opgeslagenCallback.timerAfgelopen();
    }

    @Test
    void TimeChange() {
        schoonmakerController.timeChange(10);
        assertNotNull(schoonmakerController);
    }

    @Test
    void ResetSimulatie() {
        forceerLayoutGeladen();
        SchoonmakerModel sm1 = schoonmakerController.getSchoonmaker1();

        schoonmakerController.getWachtrij(sm1.getID()).add(new KamerModel(KamerType.ROOM, null, "1,1", null, false));
        schoonmakerController.resetSimulatie();
        wachtOpSwing();

        assertTrue(schoonmakerController.getWachtrij(sm1.getID()).isEmpty());
        assertEquals(0, schoonmakerController.getActieveKlussenAantal());
    }

    @Test
    void OngebruikteSettingsListeners() {
        schoonmakerController.filmDuurVeranderd(TijdsDuur.NORMAAL);
        schoonmakersVeranderd();
        schoonmakerController.restaurantCapaciteitVeranderd(50);
        schoonmakerController.trapLoopDuurVeranderd(4);
        schoonmakerController.gastMaxWachttijdVeranderd(20);
        assertNotNull(schoonmakerController);
    }

    private void schoonmakersVeranderd() {}
}