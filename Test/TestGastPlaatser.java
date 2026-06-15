import Controller.PersoonManagement.PlaatsHelper;
import Controller.Systeem.PauseController;
import Model.Entiteiten.GastModel;
import Model.Entiteiten.TypePersoon;
import Model.Layout.Locatie;
import Model.Ruimtes.KamerClassificatie;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;
import View.Layout.GridVakjeView;
import Controller.Layout.GridVakjeController;
import Model.Layout.GridVakjeModel;
import View.Systeem.OverzichtView;
import hotelevents.HotelEventManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestGastPlaatser {

    private PlaatsHelper gastPlaatser;
    private HashMap<Locatie, GridVakjeController> testGrid;
    private Locatie testLocatie;
    private GastModel testGast;
    private GridVakjeController testVakController;
    private RuimteModel testRuimte;
    private PauseController pauseController;
    private HotelEventManager manager;
    private OverzichtView view2;


    @BeforeEach
    void setUp() {
        testLocatie = new Locatie(1, 1);
        pauseController = new PauseController(manager, view2);
        KamerClassificatie sterren = KamerClassificatie.eenSter;
        KamerModel testKamer = new KamerModel(
                KamerType.ROOM,
                testLocatie,
                "1,1",
                sterren,
                false
        );

        this.testRuimte = testKamer;

        GridVakjeModel vakModel = new GridVakjeModel(testLocatie.getX(), testLocatie.getY());
        vakModel.setRuimte(testKamer);

        GridVakjeView vakView = new GridVakjeView(testLocatie.getX(), testLocatie.getY(), 50, 50);
        testVakController = new GridVakjeController(vakModel, vakView, pauseController);

        testGrid = new HashMap<>();
        testGrid.put(testLocatie, testVakController);

        gastPlaatser = new PlaatsHelper(null, null);
        injectGridTest(testGrid);

        testGast = new GastModel(
                1,
                testLocatie,
                new Locatie(1, 1),
                TypePersoon.GAST,
                sterren,
                testKamer);
    }

    private void injectGridTest(HashMap<Locatie, GridVakjeController> grid) {
        try {
            java.lang.reflect.Field field = PlaatsHelper.class.getDeclaredField("grid");
            field.setAccessible(true);
            field.set(this.gastPlaatser, grid);
        } catch (Exception e) {
            fail("Reflectie mislukt: " + e.getMessage());
        }
    }

    @Test
    public void testOnGastAangemaakt() {
        JPanel panel = this.testVakController.getGridView().getGuestPanel();
        int voor = panel.getComponentCount();

        this.gastPlaatser.onGastAangemaakt(this.testGast);

        assertEquals(voor + 1, panel.getComponentCount());
    }

    @Test
    public void testOnGastAangekomenInKamer() {
        JPanel panel = this.testVakController.getGridView().getGuestPanel();
        panel.add(this.testGast.getPersoonLabel());
        this.testRuimte.setAantalGasten(0);

        this.gastPlaatser.onGastAangekomenInKamer(this.testGast, this.testLocatie);

        assertEquals(1, this.testRuimte.getAantalGasten());
        assertEquals(0, panel.getComponentCount());
    }

    @Test
    public void testOnGastVerplaatst() {
        Locatie nieuweLocatie = new Locatie(2, 2);
        GridVakjeModel nieuwVakModel = new GridVakjeModel(2, 2);
        GridVakjeView nieuwVakView = new GridVakjeView(2, 2, 50, 50);
        GridVakjeController nieuwVakController = new GridVakjeController(nieuwVakModel, nieuwVakView, pauseController);

        testGrid.put(nieuweLocatie, nieuwVakController);

        JPanel oudPanel = testVakController.getGridView().getGuestPanel();
        oudPanel.add(testGast.getPersoonLabel());

        Locatie oudeLocatie = testLocatie;
        testGast.setLocatie(nieuweLocatie);
        gastPlaatser.onGastVerplaatst(testGast, oudeLocatie);

        assertEquals(0, oudPanel.getComponentCount(), "Gast moet verwijderd zijn uit het oude vakje.");
        assertEquals(1, nieuwVakController.getGridView().getGuestPanel().getComponentCount(), "Gast moet toegevoegd zijn aan het nieuwe vakje.");
    }


    @Test
    public void testOnGastVerplaatstZelfdeLocatie() {
        JPanel panel = testVakController.getGridView().getGuestPanel();
        panel.add(testGast.getPersoonLabel());
        gastPlaatser.onGastVerplaatst(testGast, testLocatie);
        assertEquals(1, panel.getComponentCount(), "Gast moet gewoon blijven staan als de locatie hetzelfde is.");
    }



    @Test
    public void testOudeLocatieNull() {

        JPanel nieuwPanel = testVakController.getGridView().getGuestPanel();

        gastPlaatser.onGastVerplaatst(testGast, null);

        assertEquals(1, nieuwPanel.getComponentCount(), "Gast moet alsnog geplaatst worden, ook als de oude locatie onbekend was.");
    }

    @Test
    void testOnGastVertrokken() {
        JPanel guestPanel = testVakController.getGridView().getGuestPanel();
        guestPanel.add(testGast.getPersoonLabel());
        int initialCount = guestPanel.getComponentCount();

        gastPlaatser.onGastVertrokken(testGast);

        assertEquals(initialCount - 1, guestPanel.getComponentCount(),
                "Het gast-label moet verwijderd zijn uit het panel.");
    }


    @Test
    void testOnGastVertrokkenMetNullLocatie() {
        GastModel spookGast = new GastModel(99, null, null, TypePersoon.GAST, KamerClassificatie.eenSter, (KamerModel) testRuimte);

        assertDoesNotThrow(() -> gastPlaatser.onGastVertrokken(spookGast),
                "De methode mag niet crashen als de locatie van de gast null is.");
    }
}