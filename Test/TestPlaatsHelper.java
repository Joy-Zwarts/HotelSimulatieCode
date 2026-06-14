import Controller.Layout.GridVakjeController;
import Controller.Layout.LayoutController;
import Controller.PersoonManagement.PlaatsHelper;
import Model.Entiteiten.GastModel;
import Model.Entiteiten.LiftModel;
import Model.Entiteiten.PersoonModel;
import Model.Entiteiten.SchoonmakerModel;
import Model.Entiteiten.TypePersoon;
import Model.Layout.GridVakjeModel;
import Model.Layout.LayoutModel;
import Model.Layout.Locatie;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;
import View.Layout.GridVakjeView;
import View.Layout.LayoutView;
import View.Layout.LiftView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlaatsHelper {

    private PlaatsHelper plaatsHelper;
    private FakeLayoutController fakeLayoutController;
    private LayoutModel fakeLayoutModel;
    private FakeLayoutView fakeLayoutView;
    private HashMap<Locatie, GridVakjeController> mockGrid;

    // =========================================================================
    // Lightweight Fakes & Stubs met instelbare booleans voor 100% coverage
    // =========================================================================

    static class FakeLayoutController extends LayoutController {
        private final LayoutModel model;
        private final LayoutView view;

        public FakeLayoutController(LayoutModel model, LayoutView view) {
            super(null, null, null);
            this.model = model;
            this.view = view;
        }

        @Override protected void init() {}
        @Override public LayoutModel getModel() { return this.model; }
        @Override public LayoutView getView() { return this.view; }
    }

    static class FakeLayoutView extends LayoutView {
        private HashMap<Locatie, GridVakjeController> grid;
        public FakeLayoutView() { super(null, null); }
        public void setGrid(HashMap<Locatie, GridVakjeController> grid) { this.grid = grid; }
        @Override public HashMap<Locatie, GridVakjeController> getGrid() { return this.grid; }
    }

    static class FakeGridVakjeView extends GridVakjeView {
        private final JPanel guestPanel = new JPanel();
        private final JPanel liftContainer = new JPanel();
        private LiftView gevoegdeLift = null;

        public FakeGridVakjeView() { super(0, 0, 0, 0); }
        @Override public JPanel getGuestPanel() { return this.guestPanel; }
        @Override public JPanel getLiftContainer() { return this.liftContainer; }
        @Override public void voegLiftToe(LiftView liftView) { this.gevoegdeLift = liftView; this.liftContainer.add(liftView); }
        @Override public void verwijderLift() { this.liftContainer.removeAll(); this.gevoegdeLift = null; }
        @Override public void zetInhoud(RuimteModel r, boolean lb, boolean lo) {}
        @Override public void zetPersonenAantal(RuimteModel r, boolean rb, boolean lb) {}
    }

    static class FakeGridVakjeModel extends GridVakjeModel {
        private RuimteModel ruimte;
        public FakeGridVakjeModel() { super(0, 0); }
        public void setRuimte(RuimteModel r) { this.ruimte = r; }
        @Override public RuimteModel getRuimte() { return this.ruimte; }

        @Override public boolean islinksboven() { return true; }
        @Override public boolean islinksOnder() { return true; }
        @Override public boolean isRechtsboven() { return true; }
    }

    static class FakeGridVakjeController extends GridVakjeController {
        public FakeGridVakjeController(GridVakjeModel model, GridVakjeView view) {
            super(model, view, null);
        }
        @Override public void updateView() {}
    }

    static class FakeAlgemeneRuimte extends RuimteModel {
        public FakeAlgemeneRuimte() { super(null, new Locatie(2,2), "1,1"); }
    }

    static class FakeGast extends GastModel {
        private final JLabel label = new JLabel("Gast");
        public FakeGast(int id, Locatie loc) { super(id, loc, loc, TypePersoon.GAST, null, null); }
        @Override public JLabel getPersoonLabel() { return this.label; }
    }

    static class FakeSchoonmaker extends SchoonmakerModel {
        private final JLabel label = new JLabel("Schoonmaker");
        public FakeSchoonmaker(int id, Locatie loc) { super(id, loc, loc, TypePersoon.SCHOONMAKER, null); }
        @Override public JLabel getPersoonLabel() { return this.label; }
    }

    // CRUCIAL: Dit type dekt de 'false' branches van 'instanceof GastModel' EN 'instanceof SchoonmakerModel' af!
    static class OnbekendPersoonType extends PersoonModel {
        private final JLabel label = new JLabel("Onbekend");
        public OnbekendPersoonType(Locatie loc) { super(999, loc, loc, Color.RED, TypePersoon.GAST); }
        @Override public JLabel getPersoonLabel() { return this.label; }
    }

    static class DummyPersoon extends PersoonModel {
        public DummyPersoon(Locatie loc) { super(99, loc, loc, Color.BLACK, TypePersoon.GAST); }
        @Override public JLabel getPersoonLabel() { return null; }
    }

    // =========================================================================
    // Setup
    // =========================================================================

    @BeforeEach
    void setUp() throws Exception {
        fakeLayoutModel = new LayoutModel();
        fakeLayoutView = new FakeLayoutView();
        fakeLayoutController = new FakeLayoutController(fakeLayoutModel, fakeLayoutView);
        mockGrid = new HashMap<>();
        fakeLayoutView.setGrid(mockGrid);
        plaatsHelper = new PlaatsHelper(fakeLayoutView, fakeLayoutController);
    }

    private GridVakjeController voegVakjeToe(Locatie loc, RuimteModel ruimte) {
        FakeGridVakjeModel vakModel = new FakeGridVakjeModel();
        vakModel.setRuimte(ruimte);
        FakeGridVakjeView vakView = new FakeGridVakjeView();
        FakeGridVakjeController vakController = new FakeGridVakjeController(vakModel, vakView);
        mockGrid.put(loc, vakController);
        return vakController;
    }

    private void injecteerGridDirect(HashMap<Locatie, GridVakjeController> grid) throws Exception {
        Field field = PlaatsHelper.class.getDeclaredField("grid");
        field.setAccessible(true);
        field.set(plaatsHelper, grid);
    }

    // =========================================================================
    // TEST CASES
    // =========================================================================

    @Test
    void testInitialisatieEnAanmaken() {
        plaatsHelper.onLayoutGeladen(fakeLayoutController);
        Locatie loc = new Locatie(1, 1);
        voegVakjeToe(loc, new KamerModel(KamerType.ROOM, null, "1,1", null, false));

        plaatsHelper.onGastAangemaakt(new FakeGast(1, loc));
        plaatsHelper.onSchoonmakerAangemaakt(new FakeSchoonmaker(2, loc));
    }

    @Test
    void testOnLiftAangemaakt_Variaties() throws Exception {
        Locatie loc = new Locatie(0, 4);
        LiftModel lift = new LiftModel(999, loc, loc, 0, true);

        voegVakjeToe(loc, null);
        injecteerGridDirect(null);
        fakeLayoutModel.setVakBreedte(0);
        fakeLayoutModel.setVakHoogte(50);
        plaatsHelper.onLiftAangemaakt(lift);

        injecteerGridDirect(null);
        fakeLayoutModel.setVakBreedte(100);
        fakeLayoutModel.setVakHoogte(0);
        plaatsHelper.onLiftAangemaakt(lift);

        injecteerGridDirect(null);
        Field controllerField = PlaatsHelper.class.getDeclaredField("layoutController");
        controllerField.setAccessible(true);
        controllerField.set(plaatsHelper, null);
        plaatsHelper.onLiftAangemaakt(lift);
    }

    @Test
    void testPlaatsPersoon_UnhappyPaths() throws Exception {
        plaatsHelper.plaatsPersoon(null);

        injecteerGridDirect(null);
        FakeLayoutController controllerZonderView = new FakeLayoutController(fakeLayoutModel, null);
        Field controllerField = PlaatsHelper.class.getDeclaredField("layoutController");
        controllerField.setAccessible(true);
        controllerField.set(plaatsHelper, controllerZonderView);
        plaatsHelper.plaatsPersoon(new FakeGast(1, new Locatie(1,1)));

        controllerField.set(plaatsHelper, fakeLayoutController);

        injecteerGridDirect(mockGrid);
        plaatsHelper.plaatsPersoon(new FakeGast(2, new Locatie(99, 99)));

        Locatie loc = new Locatie(1,1);
        voegVakjeToe(loc, null);
        plaatsHelper.plaatsPersoon(new DummyPersoon(loc));
    }

    @Test
    void testVerplaatsingen_NormaalEnVroegeReturns() throws Exception {
        injecteerGridDirect(mockGrid);
        Locatie oud = new Locatie(1, 1);
        Locatie nieuw = new Locatie(2, 1);

        voegVakjeToe(oud, null);
        voegVakjeToe(nieuw, null);

        FakeGast gast = new FakeGast(1, nieuw);
        FakeSchoonmaker sm = new FakeSchoonmaker(2, nieuw);

        plaatsHelper.onGastVerplaatst(gast, nieuw);
        plaatsHelper.onSchoonmakerVerplaatst(sm, nieuw);

        plaatsHelper.onGastVerplaatst(gast, oud);
        plaatsHelper.onSchoonmakerVerplaatst(sm, oud);
    }

    @Test
    void testOnLiftVerplaatst_AlleLoopBranches() throws Exception {
        injecteerGridDirect(mockGrid);
        Locatie oud = new Locatie(0, 2);
        Locatie nieuw = new Locatie(0, 1);
        LiftModel lift = new LiftModel(999, nieuw, nieuw, 0, true);

        GridVakjeController vakOud = voegVakjeToe(oud, null);
        voegVakjeToe(nieuw, null);

        plaatsHelper.onLiftVerplaatst(lift, nieuw);

        vakOud.getGridView().getLiftContainer().add(new JLabel("Ik ben een indringer"));
        plaatsHelper.onLiftVerplaatst(lift, oud);

        vakOud.getGridView().voegLiftToe(new LiftView(10, 10));
        plaatsHelper.onLiftVerplaatst(lift, oud);
    }

    @Test
    void testAankomstEnVertrek_KamerLogicaEnTellers() throws Exception {
        injecteerGridDirect(mockGrid);
        Locatie loc = new Locatie(1, 1);
        Locatie andereLoc = new Locatie(2, 2);

        KamerModel kamer = new KamerModel(KamerType.ROOM, null, "1,1", null, false);
        // Zet de initiële tellers hoger dan 0 om de aftreksom-branches te hitten (> 0 branch van Math.max)
        kamer.setAantalGasten(2);
        kamer.setAantalSchoonmakers(2);
        voegVakjeToe(loc, kamer);

        KamerModel andereKamer = new KamerModel(KamerType.ROOM, null, "2,2", null, false);
        voegVakjeToe(andereLoc, andereKamer);

        FakeGast gast = new FakeGast(1, loc);
        FakeSchoonmaker sm = new FakeSchoonmaker(2, loc);

        // 1. Aankomst & Vertrek Schoonmaker in een KamerModel
        // (Dekt het specifieke pad: persoon is Schoonmaker, maar ruimte is wél KamerModel)
        plaatsHelper.onSchoonmakerAangekomenInKamer(sm);
        assertEquals(3, kamer.getAantalSchoonmakers());
        plaatsHelper.onSchoonmakerVerlaatKamer(sm, loc);
        assertEquals(2, kamer.getAantalSchoonmakers());

        // 2. Aankomst Gast (verhoogt van 2 naar 3)
        plaatsHelper.onGastAangekomenInKamer(gast, loc);
        assertTrue(kamer.isBezet());

        // Vertrek Gast (verlaagt van 3 naar 2 -> aantalGasten == 0 branch evalueert naar false!)
        plaatsHelper.onGastGaatWegUitKamer(gast, loc);
        assertTrue(kamer.isBezet()); // Moet nog steeds true zijn want aantalGasten is 2

        // Forceer nu de ondergrens (0) branch
        kamer.setAantalGasten(1);
        plaatsHelper.onGastGaatWegUitKamer(gast, loc);
        assertFalse(kamer.isBezet()); // Nu moet hij false zijn (aantalGasten == 0 branch is true)
    }

    @Test
    void testAankomstEnVertrek_OnbekendPersoonType() throws Exception {
        injecteerGridDirect(mockGrid);
        Locatie loc = new Locatie(1, 1);
        KamerModel kamer = new KamerModel(KamerType.ROOM, null, "1,1", null, false);
        voegVakjeToe(loc, kamer);

        // Dit object dwingt de 'instanceof' checks voor zowel Gast als Schoonmaker naar FALSE!
        OnbekendPersoonType fakePersoon = new OnbekendPersoonType(loc);

        // Roept Aangekomen() en vertrokken() aan via de algemene helper
        plaatsHelper.Aangekomen(fakePersoon);
        plaatsHelper.vertrokken(fakePersoon, loc);

        // Tellers mogen niet veranderd zijn omdat het type is overgeslagen
        assertEquals(0, kamer.getAantalGasten());
        assertEquals(0, kamer.getAantalSchoonmakers());
    }

    @Test
    void testAankomstEnVertrek_AlgemeneRuimte() throws Exception {
        injecteerGridDirect(mockGrid);
        Locatie loc = new Locatie(5, 5);
        FakeAlgemeneRuimte algemeen = new FakeAlgemeneRuimte();
        voegVakjeToe(loc, algemeen);

        FakeGast gast = new FakeGast(1, loc);
        plaatsHelper.onGastAangekomenInKamer(gast, loc);
        plaatsHelper.onGastGaatWegUitKamer(gast, loc);
    }

    @Test
    void testOnGastVertrokken_En_RefreshBranches() throws Exception {
        injecteerGridDirect(mockGrid);
        Locatie loc = new Locatie(1, 1);
        voegVakjeToe(loc, null);

        FakeGast gast = new FakeGast(1, loc);
        plaatsHelper.onGastVertrokken(gast);

        injecteerGridDirect(null);
        plaatsHelper.onGastVertrokken(gast);
    }

    @Test
    void testLayoutGeladen_NullBranch() {
        plaatsHelper.onLayoutGeladen(null);
    }

    @Test
    void testResetSimulatie_Compleet() throws Exception {
        injecteerGridDirect(mockGrid);
        Locatie loc = new Locatie(1, 1);
        KamerModel kamer = new KamerModel(KamerType.ROOM, null, "1,1", null, false);
        GridVakjeController vak = voegVakjeToe(loc, kamer);

        vak.getGridView().getGuestPanel().add(new JLabel("Test"));
        vak.getGridView().voegLiftToe(new LiftView(10,10));

        mockGrid.put(new Locatie(4, 4), null);

        plaatsHelper.resetSimulatie();

        injecteerGridDirect(null);
        plaatsHelper.resetSimulatie();
    }
}