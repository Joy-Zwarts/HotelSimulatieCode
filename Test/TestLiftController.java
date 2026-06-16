import Controller.Layout.LayoutController;
import Controller.PersoonManagement.Interfaces.NewLift;
import Controller.PersoonManagement.LiftController;
import Model.Entiteiten.EntiteitenModel;
import Model.Entiteiten.LiftModel;
import Model.Layout.LayoutModel;
import Model.Layout.Locatie;
import View.Layout.LayoutView;
import View.Systeem.HotelSimulatieView;
import hotelevents.HotelEvent;
import hotelevents.HotelEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class TestLiftController {

    private LiftController liftController;
    private LayoutController fakeLayoutController;
    private MockNewLiftListener mockListener;

    static class MockNewLiftListener implements NewLift {
        boolean aangemaaktCalled = false;
        boolean verplaatstCalled = false;
        LiftModel lastLift;
        Locatie lastOudeLocatie;

        @Override
        public void onLiftAangemaakt(LiftModel lift) {
            aangemaaktCalled = true;
            lastLift = lift;
        }

        @Override
        public void onLiftVerplaatst(LiftModel lift, Locatie oudeLocatie) {
            verplaatstCalled = true;
            lastLift = lift;
            lastOudeLocatie = oudeLocatie;
        }
    }

    static class FakeLayoutView extends LayoutView {
        private final int gridLengte;
        public FakeLayoutView(int gridLengte) {
            super(null, null);
            this.gridLengte = gridLengte;
        }
        @Override public int getGridLengte() { return this.gridLengte; }
    }

    static class DummyEntiteit extends EntiteitenModel {
        public DummyEntiteit() {
            super(111, new Locatie(0,0), new Locatie(0,0));
        }
    }

    @BeforeEach
    void setUp() {
        liftController = new LiftController();
        mockListener = new MockNewLiftListener();
        liftController.setNewLiftListener(mockListener);

        // Maak een fake layout met een gridLengte van 5 (Y loopt van 0 tot 4)
        LayoutModel model = new LayoutModel();
        FakeLayoutView view = new FakeLayoutView(5);
        HotelSimulatieView hoofdView = new HotelSimulatieView(null);
        fakeLayoutController = new LayoutController(model, view, hoofdView);
    }

    // Helper om asynchrone Swing-events synchroon af te handelen in tests
    private void flushSwingQueue() {
        try {
            java.awt.EventQueue.invokeAndWait(() -> {});
        } catch (Exception e) {
            fail("Swing queue flush mislukt: " + e.getMessage());
        }
    }

    @Test
    void onLayoutGeladenMaaktLiftAanEnTriggertListener() {
        liftController.onLayoutGeladen(fakeLayoutController);
        flushSwingQueue();

        assertTrue(mockListener.aangemaaktCalled);
        assertNotNull(mockListener.lastLift);
        // Gridlengte is 5, dus bodemY = 5 - 1 = 4
        assertEquals(4, mockListener.lastLift.getLocatie().getY());
        assertEquals(0, mockListener.lastLift.getLocatie().getX());
    }

    @Test
    void LiftCalledDektLegeMethodeAf() {
        assertDoesNotThrow(() -> liftController.liftCalled());
    }

    @Test
    void ResetSimulatieZetRichtingOmhoog() throws Exception {
        Field field = LiftController.class.getDeclaredField("gaatOmhoog");
        field.setAccessible(true);

        field.set(liftController, false); // handmatig omlaag zetten
        liftController.resetSimulatie();

        assertTrue(field.getBoolean(liftController));
    }

    @Test
    void liftOmhoogEnLiftOmlaagWanneerLiftModelNullIsDoetNiets() {
        assertDoesNotThrow(() -> liftController.liftOmhoog());
        assertDoesNotThrow(() -> liftController.liftOmlaag());
        assertFalse(mockListener.verplaatstCalled);
    }

    @Test
    void liftOmhoogNormaalEnRandvoorwaardeTop() {
        liftController.onLayoutGeladen(fakeLayoutController); // staat nu op Y=4
        flushSwingQueue();

        // 1. Normale stap omhoog (van Y=4 naar Y=3)
        mockListener.verplaatstCalled = false;
        liftController.liftOmhoog();
        flushSwingQueue();

        assertTrue(mockListener.verplaatstCalled);
        assertEquals(3, mockListener.lastLift.getLocatie().getY());
        assertEquals(4, mockListener.lastOudeLocatie.getY());

        // 2. Forceer lift naar de absolute top (Y=0) om grens-branch te testen
        mockListener.lastLift.getLocatie().setY(0);
        mockListener.verplaatstCalled = false;

        liftController.liftOmhoog(); // Mag niet verder omhoog vliegen wegens: if (nieuweY >= 0)
        flushSwingQueue();

        assertFalse(mockListener.verplaatstCalled);
        assertEquals(0, mockListener.lastLift.getLocatie().getY());
    }

    @Test
    void liftOmlaagNormaalEnRandvoorwaardeBodem() {
        liftController.onLayoutGeladen(fakeLayoutController);
        flushSwingQueue();

        // Zet lift handmatig op Y=2 (maxBodemY is 4)
        mockListener.lastLift.getLocatie().setY(2);

        // 1. Normale stap omlaag (van Y=2 naar Y=3)
        liftController.liftOmlaag();
        flushSwingQueue();

        assertTrue(mockListener.verplaatstCalled);
        assertEquals(3, mockListener.lastLift.getLocatie().getY());

        // 2. Zet lift op de bodem (Y=4) om grens-branch te testen
        mockListener.lastLift.getLocatie().setY(4);
        mockListener.verplaatstCalled = false;

        liftController.liftOmlaag(); // Mag niet door de bodem zakken wegens: if (nieuweY <= maxBodemY)
        flushSwingQueue();

        assertFalse(mockListener.verplaatstCalled);
        assertEquals(4, mockListener.lastLift.getLocatie().getY());
    }

    @Test
    void onStepTakenEnonDestinationReachedMetFoutieveInstantieDoetNiets() {
        DummyEntiteit dummy = new DummyEntiteit();

        assertDoesNotThrow(() -> liftController.onStepTaken(dummy, new Locatie(0,0)));
        assertDoesNotThrow(() -> liftController.onDestinationReached(dummy));

        flushSwingQueue();
        assertFalse(mockListener.verplaatstCalled);
    }

    @Test
    void onDestinationReachedMetCorrecteLiftTriggertConsoleLog() {
        LiftModel fakeLift = new LiftModel(999, new Locatie(0,2), new Locatie(0,2), 0, true);
        assertDoesNotThrow(() -> liftController.onDestinationReached(fakeLift));
    }

    @Test
    void HTETickWanneerLayoutOfLiftNullIsRetouneertVroegtijdig() {
        assertDoesNotThrow(() -> liftController.HTETick(null));
    }

    @Test
    void LegeInterfaceStubs() {
        assertDoesNotThrow(() -> liftController.schoonmaakTijdVeranderd(null));
        assertDoesNotThrow(() -> liftController.filmDuurVeranderd(null));
        assertDoesNotThrow(() -> liftController.restaurantCapaciteitVeranderd(10));
        assertDoesNotThrow(() -> liftController.gastMaxWachttijdVeranderd(20));
    }
}