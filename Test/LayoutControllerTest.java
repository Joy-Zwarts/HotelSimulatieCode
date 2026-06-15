import Controller.Layout.GridVakjeController;
import Controller.Layout.LayoutController;
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

    static class StubLayoutView extends LayoutView {
        int gridBreedte = 10;
        int gridLengte = 10;

        public StubLayoutView() {
            super(null, null);
        }

        @Override public int getGridBreedte() { return gridBreedte; }
        @Override public int getGridLengte() { return gridLengte; }
        @Override public void berekenGridGrootte(ArrayList<RuimteModel> r) {}

        @Override
        public void maakGrid(int b, int l, int vb, int vh, HashMap<Locatie, GridVakjeController> grid) {
            this.setGrid(grid != null ? grid : new HashMap<>());
        }

        @Override
        public void plaatsKamers(ArrayList<RuimteModel> ruimtes, ArrayList<RuimteModel> verplichteElementen) {
        }
    }

    static class StubHotelSimulatieView extends HotelSimulatieView {
        private final JScrollPane fakeScrollPane = new JScrollPane();
        private final JPanel fakeViewportView = new JPanel();

        public StubHotelSimulatieView() {
            super(null);
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


    @BeforeEach
    void setUp() {
        fakeModel = new LayoutModel();
        stubView = new StubLayoutView();
        stubHoofdView = new StubHotelSimulatieView();
    }

    @Test
    void berekenEnPasVakgrootteAanTest() {
        stubHoofdView.setMockViewportSize(800, 600);
        controller = new LayoutController(fakeModel, stubView, stubHoofdView);

        assertTrue(fakeModel.getVakBreedte() > 0, "Vakbreedte moet berekend zijn");
        assertTrue(fakeModel.getVakHoogte() > 0, "Vakhoogte moet berekend zijn");
    }

    @Test
    void SchermGrootteNullTest() {
        stubHoofdView.setMockViewportSize(0, 0);
        controller = new LayoutController(fakeModel, stubView, stubHoofdView);

        assertTrue(fakeModel.getVakBreedte() > 0, "Fallback voor breedte heeft niet gewerkt");
        assertTrue(fakeModel.getVakHoogte() > 0, "Fallback voor hoogte heeft niet gewerkt");
    }

    @Test
    void vindLocatieTest() {
        FakeRuimte lobby = new FakeRuimte(KamerType.LOBBY, 4, 6);
        fakeModel.getRuimtes().add(lobby);

        controller = new LayoutController(fakeModel, stubView, stubHoofdView);
        Locatie resultaat = controller.vindLocatie(KamerType.LOBBY);

        assertNotNull(resultaat);
        assertEquals(4, resultaat.getX());
        assertEquals(5, resultaat.getY());
    }

    @Test
    void KamerBestaatNietTest() {
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