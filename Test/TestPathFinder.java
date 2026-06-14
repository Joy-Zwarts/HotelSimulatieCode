import Controller.Layout.LayoutController;
import Controller.PersoonManagement.PathFinder;
import Model.Layout.LayoutModel;
import Model.Layout.Locatie;
import View.Layout.LayoutView;
import View.Systeem.HotelSimulatieView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPathFinder {

    private LayoutController fakeLayoutController;

    // =========================================================================
    // Lightweight Fake voor LayoutView om de gridbreedte te controleren
    // =========================================================================
    static class FakeLayoutView extends LayoutView {
        private final int gridBreedte;

        public FakeLayoutView(int gridBreedte) {
            super(null, null);
            this.gridBreedte = gridBreedte;
        }

        @Override
        public int getGridBreedte() {
            return this.gridBreedte;
        }
    }

    @BeforeEach
    void setUp() {
        // Setup een fake layoutomgeving met een gridbreedte van 5 (X-as loopt van 0 t/m 4)
        LayoutModel model = new LayoutModel();
        FakeLayoutView view = new FakeLayoutView(5);
        HotelSimulatieView hoofdView = new HotelSimulatieView(null);
        fakeLayoutController = new LayoutController(model, view, hoofdView);
    }

    // =========================================================================
    // Tests voor dezelfde verdieping (Horizontale beweging)
    // =========================================================================

    @Test
    void berekenRoute_ZelfdeVerdieping_NaarRechts() {
        Locatie start = new Locatie(1, 2);
        Locatie target = new Locatie(3, 2); // Y is gelijk, X stijgt (naar rechts)

        PathFinder pf = new PathFinder(start, target, fakeLayoutController);

        assertFalse(pf.isBestemmingBereikt());

        // Verwachte stappen: (2, 2) -> (3, 2)
        Locatie stap1 = pf.getNextStep();
        assertEquals(2, stap1.getX());
        assertEquals(2, stap1.getY());

        // Test peekNextStep functionaliteit tussendoor
        Locatie peek = pf.peekNextStep();
        assertNotNull(peek);
        assertEquals(3, peek.getX());

        Locatie stap2 = pf.getNextStep();
        assertEquals(3, stap2.getX());
        assertEquals(2, stap2.getY());

        assertTrue(pf.isBestemmingBereikt());
    }

    @Test
    void berekenRoute_ZelfdeVerdieping_NaarLinks() {
        Locatie start = new Locatie(4, 1);
        Locatie target = new Locatie(2, 1); // Y is gelijk, X daalt (naar links)

        PathFinder pf = new PathFinder(start, target, fakeLayoutController);

        // Verwachte stappen: (3, 1) -> (2, 1)
        assertEquals(3, pf.getNextStep().getX());
        assertEquals(2, pf.getNextStep().getX());
        assertTrue(pf.isBestemmingBereikt());
    }

    @Test
    void berekenRoute_MeteenOpBestemming() {
        Locatie start = new Locatie(2, 2);
        Locatie target = new Locatie(2, 2); // Start en target zijn exact gelijk

        PathFinder pf = new PathFinder(start, target, fakeLayoutController);

        // De while-loops mogen niet worden uitgevoerd (0 iteraties branch)
        assertTrue(pf.isBestemmingBereikt());
        assertNull(pf.getNextStep());
    }

    // =========================================================================
    // Tests voor andere verdieping (Horizontaal + Verticaal via de trap)
    // =========================================================================

    @Test
    void berekenRoute_AndereVerdieping_OmhoogEnNaarTrap() {
        // Gridbreedte = 5, dus trapX = 5 - 1 = 4
        Locatie start = new Locatie(2, 3);
        Locatie target = new Locatie(1, 1); // Andere verdieping: Y daalt (omhoog), targetX is links van de trap

        PathFinder pf = new PathFinder(start, target, fakeLayoutController);

        // Verwacht padverloop:
        // 1. Horizontaal naar trapX (4) op huidige verdieping (3): (3,3) -> (4,3)
        // 2. Verticaal via trap naar target verdieping (1): (4,2) -> (4,1)
        // 3. Horizontaal vanaf de trap (4) naar targetX (1) op verdieping (1): (3,1) -> (2,1) -> (1,1)

        // Deel 1: Naar de trap (X stijgt naar 4)
        assertEquals(new Locatie(3, 3), pf.getNextStep());
        assertEquals(new Locatie(4, 3), pf.getNextStep());

        // Deel 2: Met de trap omhoog (Y daalt naar 1)
        assertEquals(new Locatie(4, 2), pf.getNextStep());
        assertEquals(new Locatie(4, 1), pf.getNextStep());

        // Deel 3: Vanaf de trap naar de kamer (X daalt naar 1)
        assertEquals(new Locatie(3, 1), pf.getNextStep());
        assertEquals(new Locatie(2, 1), pf.getNextStep());
        assertEquals(new Locatie(1, 1), pf.getNextStep());

        assertTrue(pf.isBestemmingBereikt());
    }

    @Test
    void berekenRoute_AndereVerdieping_Omlaag() {
        // Start staat toevallig al op de X-as van de trap (4), target ligt lager (Y stijgt)
        Locatie start = new Locatie(4, 0);
        Locatie target = new Locatie(4, 2);

        PathFinder pf = new PathFinder(start, target, fakeLayoutController);

        // 1. Horizontaal naar trap op huidige verdieping -> Al op X=4 (0 iteraties)
        // 2. Verticaal omlaag (Y stijgt naar 2): (4,1) -> (4,2)
        // 3. Horizontaal vanaf trap naar target -> Al op X=4 (0 iteraties)

        assertEquals(new Locatie(4, 1), pf.getNextStep());
        assertEquals(new Locatie(4, 2), pf.getNextStep());

        assertTrue(pf.isBestemmingBereikt());
    }

    // =========================================================================
    // Tests voor Randvoorwaarden & Getters
    // =========================================================================

    @Test
    void testGetNextStep_WanneerWachtrijLeegIs_ReturnsNull() {
        Locatie start = new Locatie(0, 0);
        Locatie target = new Locatie(0, 0);
        PathFinder pf = new PathFinder(start, target, fakeLayoutController);

        // Branch check: walkQueue.isEmpty() == true
        assertNull(pf.getNextStep());
    }

    @Test
    void testPeekNextStep_WanneerWachtrijLeegIs_ReturnsNull() {
        Locatie start = new Locatie(0, 0);
        Locatie target = new Locatie(0, 0);
        PathFinder pf = new PathFinder(start, target, fakeLayoutController);

        assertNull(pf.peekNextStep());
    }
}