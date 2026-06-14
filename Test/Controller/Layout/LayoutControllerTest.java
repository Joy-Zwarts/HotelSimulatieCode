package Controller.Layout;

import Model.Layout.LayoutModel;
import Model.Layout.Locatie;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;
import View.Layout.LayoutView;
import View.Systeem.HotelSimulatieView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class LayoutControllerTest {

    private LayoutModel fakeModel;
    private StubLayoutView stubView;
    private StubHotelSimulatieView stubHoofdView;
    private LayoutController controller;

    // =========================================================================
    // Lightweight Stubs / Fakes om Swing UI dependencies te isoleren
    // =========================================================================

    static class StubLayoutView extends LayoutView {
        int gridBreedte = 10;
        int gridLengte = 10;

        public StubLayoutView() {
            super(null, null); // Voorkom UI-bindingen in de echte LayoutView
        }

        @Override public int getGridBreedte() { return gridBreedte; }
        @Override public int getGridLengte() { return gridLengte; }
        @Override public void berekenGridGrootte(ArrayList<RuimteModel> r) {}

        // CRUCIAL: Overschrijf deze methodes om de echte, zware UI-logica
        // (die de ClassCastException veroorzaakte) volledig te passeren!
        @Override
        public void maakGrid(int b, int l, int vb, int vh, HashMap<Locatie, GridVakjeController> grid) {
            // We simuleren het gedrag van de echte maakGrid door een lege grid te instantiëren
            // zodat de controller geen NullPointer krijgt bij eventuele vervolgacties.
            this.setGrid(grid != null ? grid : new HashMap<>());
        }

        @Override
        public void plaatsKamers(ArrayList<RuimteModel> ruimtes, ArrayList<RuimteModel> verplichteElementen) {
            // Leeglaten: we hoeven de kamers visueel niet te plaatsen tijdens een controller branch-test
        }
    }

    static class StubHotelSimulatieView extends HotelSimulatieView {
        private final JScrollPane fakeScrollPane = new JScrollPane();
        private final JPanel fakeViewportView = new JPanel();

        public StubHotelSimulatieView() {
            super(null); // Omzeil de constructor die een JFrame bouwt
            fakeScrollPane.setViewportView(fakeViewportView);
        }

        @Override
        public JScrollPane getLayoutScrollPane() {
            return fakeScrollPane;
        }

        public void setMockViewportSize(int width, int height) {
            fakeViewportView.setSize(width, height);
            fakeScrollPane.getViewport().setViewSize(new Dimension(width, height));
        }
    }

    static class FakeRuimte extends RuimteModel {
        private final KamerType type;

        public FakeRuimte(KamerType type, int x, int y) {
            super(type, new Locatie(x, y), "1,1");
            this.type = type;
        }

        @Override
        public KamerType getAreaType() {
            return type;
        }
    }

    // =========================================================================
    // Test Setup
    // =========================================================================

    @BeforeEach
    void setUp() {
        fakeModel = new LayoutModel();
        stubView = new StubLayoutView();
        stubHoofdView = new StubHotelSimulatieView();
    }

    // =========================================================================
    // Branch & Functionaliteit Tests
    // =========================================================================

    @Test
    void berekenEnPasVakgrootteAan_metNormaleSchermGrootte_berekentDimensies() {
        stubHoofdView.setMockViewportSize(800, 600);
        controller = new LayoutController(fakeModel, stubView, stubHoofdView);

        assertTrue(fakeModel.getVakBreedte() > 0, "Vakbreedte moet berekend zijn");
        assertTrue(fakeModel.getVakHoogte() > 0, "Vakhoogte moet berekend zijn");
    }

    @Test
    void berekenEnPasVakgrootteAan_wanneerSchermGrootteNulIs_valtTerugOpFallbacks() {
        stubHoofdView.setMockViewportSize(0, 0);
        controller = new LayoutController(fakeModel, stubView, stubHoofdView);

        assertTrue(fakeModel.getVakBreedte() > 0, "Fallback voor breedte heeft niet gewerkt");
        assertTrue(fakeModel.getVakHoogte() > 0, "Fallback voor hoogte heeft niet gewerkt");
    }

    @Test
    void vindLocatie_wanneerKamerBestaat_geeftGecorrigeerdeLocatieTerug() {
        FakeRuimte lobby = new FakeRuimte(KamerType.LOBBY, 4, 6);
        fakeModel.getRuimtes().add(lobby);

        controller = new LayoutController(fakeModel, stubView, stubHoofdView);
        Locatie resultaat = controller.vindLocatie(KamerType.LOBBY);

        assertNotNull(resultaat);
        assertEquals(4, resultaat.getX());
        assertEquals(5, resultaat.getY());
    }

    @Test
    void vindLocatie_wanneerKamerNietBestaat_geeftNullTerug() {
        FakeRuimte kamer = new FakeRuimte(KamerType.ROOM, 1, 1);
        fakeModel.getRuimtes().add(kamer);

        controller = new LayoutController(fakeModel, stubView, stubHoofdView);
        Locatie resultaat = controller.vindLocatie(KamerType.RESTAURANT);

        assertNull(resultaat);
    }

    @Test
    void testGetters() {
        controller = new LayoutController(fakeModel, stubView, stubHoofdView);

        assertEquals(fakeModel, controller.getModel());
        assertEquals(stubView, controller.getView());
    }
}