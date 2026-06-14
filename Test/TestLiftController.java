import Controller.Layout.LayoutController;
import Controller.PersoonManagement.Interfaces.NewLift;
import Controller.PersoonManagement.LiftController;
import Model.Entiteiten.EntiteitenModel;
import Model.Entiteiten.LiftModel;
import Model.Layout.LayoutModel;
import Model.Layout.Locatie;
import View.Layout.LayoutView;
import View.Systeem.HotelSimulatieView;
import View.Systeem.TijdsDuur;
import hotelevents.HotelEvent;
import hotelevents.HotelEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestLiftController {

    private LiftController liftController;
    private LayoutController fakeLayoutController;
    private MockNewLiftListener mockListener;

    // =========================================================================
    // Lightweight Mocks & Stubs
    // =========================================================================

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

    // =========================================================================
    // Setup
    // =========================================================================

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

    private void injecteerLiftModel(LiftModel lift) throws Exception {
        Field field = LiftController.class.getDeclaredField("liftModel");
        field.setAccessible(true);
        field.set(liftController, lift);
        liftController.actieveEntiteiten.put(lift.getID(), lift);
    }

    // =========================================================================
    // Tests voor Basis en Initialisatie Branches
    // =========================================================================

    @Test
    void onLayoutGeladen_MaaktLiftAanEnTriggertListener() {
        liftController.onLayoutGeladen(fakeLayoutController);

        flushSwingQueue();

        assertTrue(mockListener.aangemaaktCalled);
        assertNotNull(mockListener.lastLift);
        // Gridlengte is 5, dus bodemY = 5 - 1 = 4
        assertEquals(4, mockListener.lastLift.getLocatie().getY());
        assertEquals(0, mockListener.lastLift.getLocatie().getX());
    }

    @Test
    void testLiftCalled_DektLegeMethodeAf() {
        assertDoesNotThrow(() -> liftController.liftCalled());
    }

    @Test
    void testResetSimulatie_ZetRichtingOmhoog() throws Exception {
        Field field = LiftController.class.getDeclaredField("gaatOmhoog");
        field.setAccessible(true);

        field.set(liftController, false); // handmatig omlaag zetten
        liftController.resetSimulatie();

        assertTrue(field.getBoolean(liftController));
    }

    // =========================================================================
    // Tests voor LiftOmhoog en LiftOmlaag Randvoorwaarden
    // =========================================================================

    @Test
    void liftOmhoog_En_LiftOmlaag_WanneerLiftModelNullIs_DoetNiets() {
        // liftModel is default null bij constructie
        assertDoesNotThrow(() -> liftController.liftOmhoog());
        assertDoesNotThrow(() -> liftController.liftOmlaag());
        assertFalse(mockListener.verplaatstCalled);
    }

    @Test
    void liftOmhoog_NormaalEnRandvoorwaardeTop() throws Exception {
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
    void liftOmlaag_NormaalEnRandvoorwaardeBodem() throws Exception {
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

    // =========================================================================
    // Tests voor Event en Type Branches (instanceof)
    // =========================================================================

    @Test
    void onStepTaken_En_onDestinationReached_MetFoutieveInstantie_DoetNiets() {
        DummyEntiteit dummy = new DummyEntiteit();

        // Test de 'false' branch van de 'if (Entiteit instanceof LiftModel)' check
        assertDoesNotThrow(() -> liftController.onStepTaken(dummy, new Locatie(0,0)));
        assertDoesNotThrow(() -> liftController.onDestinationReached(dummy));

        flushSwingQueue();
        assertFalse(mockListener.verplaatstCalled);
    }

    @Test
    void onDestinationReached_MetCorrecteLift_TriggertConsoleLog() throws Exception {
        LiftModel fakeLift = new LiftModel(999, new Locatie(0,2), new Locatie(0,2), 0, true);
        assertDoesNotThrow(() -> liftController.onDestinationReached(fakeLift));
    }

    // =========================================================================
    // Tests voor Pendel Logica Branches (timeChange & HTETick)
    // =========================================================================

    @Test
    void timeChange_En_HTETick_WanneerLayoutOfLiftNullIs_RetouneertVroegtijdig() {
        // Geen layout of lift geladen -> raakt de vroege 'if (liftModel == null || layoutController == null) return;' branch
        assertDoesNotThrow(() -> liftController.timeChange(1000));
        assertDoesNotThrow(() -> liftController.HTETick(null));
    }

    @Test
    void testPendelLogica_AlleRichtingWissels_In_TimeChange() throws Exception {
        liftController.onLayoutGeladen(fakeLayoutController); // Lift staat op Y=4, gaatOmhoog = true
        flushSwingQueue();

        Field richtingField = LiftController.class.getDeclaredField("gaatOmhoog");
        richtingField.setAccessible(true);

        // Branch A: Gaat omhoog, huidigeY (4) > 0 -> roept liftOmhoog() aan
        liftController.timeChange(1000);
        flushSwingQueue();
        assertEquals(3, mockListener.lastLift.getLocatie().getY());
        assertTrue(richtingField.getBoolean(liftController));

        // Branch B: Gaat omhoog, maar we bereiken de top (Y=0) -> moet omdraaien en omlaag gaan
        mockListener.lastLift.getLocatie().setY(0);
        liftController.timeChange(1000);
        flushSwingQueue();
        assertFalse(richtingField.getBoolean(liftController)); // gaatOmhoog is nu false
        assertEquals(1, mockListener.lastLift.getLocatie().getY()); // liftOmlaag() uitgevoerd

        // Branch C: Gaat omlaag, huidigeY (1) < maxBodemY (4) -> roept liftOmlaag() aan
        liftController.timeChange(1000);
        flushSwingQueue();
        assertEquals(2, mockListener.lastLift.getLocatie().getY());
        assertFalse(richtingField.getBoolean(liftController));

        // Branch D: Gaat omlaag, maar bereikt de bodem (Y=4) -> moet omdraaien en omhoog gaan
        mockListener.lastLift.getLocatie().setY(4);
        liftController.timeChange(1000);
        flushSwingQueue();
        assertTrue(richtingField.getBoolean(liftController)); // gaatOmhoog is weer true
        assertEquals(3, mockListener.lastLift.getLocatie().getY()); // liftOmhoog() uitgevoerd
    }

    @Test
    void testPendelLogica_In_HTETick() throws Exception {
        liftController.onLayoutGeladen(fakeLayoutController); // Bodem Y=4, gaatOmhoog = true
        flushSwingQueue();
        HotelEvent dummyEvent = new HotelEvent(1, HotelEventType.NONE, 0, 0);

        // 1. Gaat omhoog loop
        liftController.HTETick(dummyEvent);
        flushSwingQueue();
        assertEquals(3, mockListener.lastLift.getLocatie().getY());

        // 2. Top bereikt wissel (Y=0)
        mockListener.lastLift.getLocatie().setY(0);
        liftController.HTETick(dummyEvent);
        flushSwingQueue();
        assertEquals(1, mockListener.lastLift.getLocatie().getY()); // omgedraaid naar beneden

        // 3. Gaat omlaag loop
        liftController.HTETick(dummyEvent);
        flushSwingQueue();
        assertEquals(2, mockListener.lastLift.getLocatie().getY());

        // 4. Bodem bereikt wissel (Y=4)
        mockListener.lastLift.getLocatie().setY(4);
        liftController.HTETick(dummyEvent);
        flushSwingQueue();
        assertEquals(3, mockListener.lastLift.getLocatie().getY()); // omgedraaid naar boven
    }

    @Test
    void testLegeInterfaceStubs() {
        assertDoesNotThrow(() -> liftController.schoonmaakTijdVeranderd(null));
        assertDoesNotThrow(() -> liftController.filmDuurVeranderd(null));
        assertDoesNotThrow(() -> liftController.aantalSchoonmakersVeranderd(5));
        assertDoesNotThrow(() -> liftController.restaurantCapaciteitVeranderd(10));
        assertDoesNotThrow(() -> liftController.gastMaxWachttijdVeranderd(20));
    }
}