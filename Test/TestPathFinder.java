import Controller.Layout.LayoutController;
import Controller.PersoonManagement.PathFinder;
import Model.Layout.LayoutModel;
import Model.Layout.Locatie;
import View.Layout.LayoutView;
import View.Systeem.HotelSimulatieView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPathFinder {

    private LayoutController fakeLayoutController;
    private final int GRID_BREEDTE = 5;

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
        FakeLayoutView view = new FakeLayoutView(GRID_BREEDTE);
        HotelSimulatieView hoofdView = new HotelSimulatieView(null);
        fakeLayoutController = new LayoutController(model, view, hoofdView);
    }

    @Test
    void berekenRouteZelfdeVerdiepingNaarRechts() {
        Locatie start = new Locatie(1, 2);
        Locatie target = new Locatie(3, 2);

        PathFinder pf = new PathFinder(start, target, fakeLayoutController);
        assertFalse(pf.isBestemmingBereikt());

        Locatie stap1 = pf.getNextStep();
        assertEquals(2, stap1.getX());
        assertEquals(2, stap1.getY());

        Locatie peek = pf.peekNextStep();
        assertNotNull(peek);
        assertEquals(3, peek.getX());

        Locatie stap2 = pf.getNextStep();
        assertEquals(3, stap2.getX());
        assertEquals(2, stap2.getY());

        assertTrue(pf.isBestemmingBereikt());
    }

    @Test
    void berekenRouteZelfdeVerdiepingNaarLinks() {
        Locatie start = new Locatie(4, 1);
        Locatie target = new Locatie(2, 1);

        PathFinder pf = new PathFinder(start, target, fakeLayoutController);

        assertEquals(3, pf.getNextStep().getX());
        assertEquals(2, pf.getNextStep().getX());
        assertTrue(pf.isBestemmingBereikt());
    }

    @Test
    void berekenRouteMeteenOpBestemming() {
        Locatie start = new Locatie(2, 2);
        Locatie target = new Locatie(2, 2);

        PathFinder pf = new PathFinder(start, target, fakeLayoutController);

        assertTrue(pf.isBestemmingBereikt());
        assertNull(pf.getNextStep());
    }

    @Test
    void berekenRouteAndereVerdiepingOmhoogEnNaarTrap() {
        Locatie start = new Locatie(2, 3);
        Locatie target = new Locatie(1, 1);

        PathFinder pf = new PathFinder(start, target, fakeLayoutController);

        // Verzamel de hele route in een lijst om deze onafhankelijk van Random te valideren
        List<Locatie> route = haalVolledigeRouteOp(pf);
        assertFalse(route.isEmpty(), "Route mag niet leeg zijn");

        // De allerlaatste stap van de route moet ALTIJD de target locatie zijn
        Locatie eindBestemming = route.get(route.size() - 1);
        assertEquals(target.getX(), eindBestemming.getX(), "Eindbestemming X klopt niet");
        assertEquals(target.getY(), eindBestemming.getY(), "Eindbestemming Y klopt niet");

        // Controleer welk transportmiddel gekozen is op basis van de eerste stap richting X=0 of X=4
        int gekozenTransportX = route.get(0).getX() > start.getX() ? (GRID_BREEDTE - 1) : 0;

        // Valideer of de route de logische knooppunten bevat
        boolean heeftTransportHubBereikt = false;
        boolean heeftVerdiepingGewisseld = false;

        for (Locatie stap : route) {
            // Heeft de hub (X=0 of X=4) bereikt op de startverdieping?
            if (stap.getX() == gekozenTransportX && stap.getY() == start.getY()) {
                heeftTransportHubBereikt = true;
            }
            // Is de hub bereikt op de doelverdieping?
            if (stap.getX() == gekozenTransportX && stap.getY() == target.getY()) {
                heeftVerdiepingGewisseld = true;
            }
        }

        assertTrue(heeftTransportHubBereikt, "De route is niet naar de lift/trap gelopen op de huidige verdieping.");
        assertTrue(heeftVerdiepingGewisseld, "De route is niet verticaal naar de juiste verdieping gegaan.");
    }

    @Test
    void berekenRouteAndereVerdiepingOmlaag() {
        // Start staat op de X-as van de trap (4), target ligt lager (Y stijgt)
        Locatie start = new Locatie(4, 0);
        Locatie target = new Locatie(4, 2);

        PathFinder pf = new PathFinder(start, target, fakeLayoutController);
        List<Locatie> route = haalVolledigeRouteOp(pf);

        Locatie eindBestemming = route.get(route.size() - 1);
        assertEquals(target.getX(), eindBestemming.getX());
        assertEquals(target.getY(), eindBestemming.getY());
    }

    @Test
    void GetNextStepWanneerWachtrijLeegIsReturnsNull() {
        Locatie start = new Locatie(0, 0);
        Locatie target = new Locatie(0, 0);
        PathFinder pf = new PathFinder(start, target, fakeLayoutController);

        assertNull(pf.getNextStep());
    }

    @Test
    void PeekNextStepWanneerWachtrijLeegIsReturnsNull() {
        Locatie start = new Locatie(0, 0);
        Locatie target = new Locatie(0, 0);
        PathFinder pf = new PathFinder(start, target, fakeLayoutController);

        assertNull(pf.peekNextStep());
    }

    // Helper methode om de stappen veilig uit de pathfinder te consumeren
    private List<Locatie> haalVolledigeRouteOp(PathFinder pf) {
        List<Locatie> route = new ArrayList<>();
        while (!pf.isBestemmingBereikt()) {
            route.add(pf.getNextStep());
        }
        return route;
    }
}