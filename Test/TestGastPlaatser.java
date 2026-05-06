import Controller.GastManagement.GastPlaatser;
import Controller.Systeem.PauseController;
import Model.Layout.Locatie;
import Model.Personen.GastModel;
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

public class TestGastPlaatser { // Klasse even op public gezet

    private GastPlaatser gastPlaatser;
    private HashMap<Locatie, GridVakjeController> testGrid;

    // Test data velden
    private Locatie testLocatie;
    private GastModel testGast;
    private GridVakjeController testVakController;
    private RuimteModel testRuimte;
    private PauseController pauseController;
    private HotelEventManager manager;
    private OverzichtView view2;


    @BeforeEach
    void setUp() {
        // 1. Basis data initialiseren
        testLocatie = new Locatie(1, 1);
        pauseController = new PauseController(manager, view2);
        KamerClassificatie sterren = KamerClassificatie.eenSter;

        // 2. Maak het KamerModel aan met de 5 juiste parameters
        // (Type, Positie, Dimensie, Classificatie, IsBezet)
        KamerModel testKamer = new KamerModel(
                KamerType.ROOM,
                testLocatie,
                "1,1",
                sterren,
                false
        );

        this.testRuimte = testKamer; // RuimteModel veld vullen

        // 3. GridVakjeModel & View (4 ints: x, y, w, h)
        GridVakjeModel vakModel = new GridVakjeModel(testLocatie.getX(), testLocatie.getY(), 50, 50);
        vakModel.setRuimte(testKamer);

        GridVakjeView vakView = new GridVakjeView(testLocatie.getX(), testLocatie.getY(), 50, 50);
        testVakController = new GridVakjeController(vakModel, vakView, pauseController);

        testGrid = new HashMap<>();
        testGrid.put(testLocatie, testVakController);

        // 4. GastPlaatser initialiseren en grid injecteren
        gastPlaatser = new GastPlaatser(null);
        injectGridIntoPlaatser(testGrid);

        // 5. GastModel aanmaken
        // Gebruik de testKamer die we net hebben gemaakt
        testGast = new GastModel(
                1,
                testLocatie,
                new Locatie(1, 1),
                sterren,
                testKamer
        );
    }

    private void injectGridIntoPlaatser(HashMap<Locatie, GridVakjeController> grid) {
        try {
            java.lang.reflect.Field field = GastPlaatser.class.getDeclaredField("grid");
            field.setAccessible(true);
            field.set(this.gastPlaatser, grid);
        } catch (Exception e) {
            fail("Reflectie mislukt: " + e.getMessage());
        }
    }

    @Test
    public void testOnGastAangemaakt() {
        // We gebruiken this.testVakController om zeker te weten dat we het veld pakken
        JPanel panel = this.testVakController.getGridView().getGuestPanel();
        int voor = panel.getComponentCount();

        this.gastPlaatser.onGastAangemaakt(this.testGast);

        assertEquals(voor + 1, panel.getComponentCount());
    }

    @Test
    public void testOnGastAangekomenInKamer() {
        JPanel panel = this.testVakController.getGridView().getGuestPanel();
        panel.add(this.testGast.getGastLabel());
        this.testRuimte.setAantalGasten(0);

        // Gebruik expliciet het veld testLocatie
        this.gastPlaatser.onGastAangekomenInKamer(this.testGast, this.testLocatie);

        assertEquals(1, this.testRuimte.getAantalGasten());
        assertEquals(0, panel.getComponentCount());
    }

    @Test
    public void testOnGastVerplaatst_SuccesvolleVerplaatsing() {
        // Setup: Maak een tweede locatie en vakje aan
        Locatie nieuweLocatie = new Locatie(2, 2);
        GridVakjeModel nieuwVakModel = new GridVakjeModel(2, 2, 50, 50);
        GridVakjeView nieuwVakView = new GridVakjeView(2, 2, 50, 50);
        GridVakjeController nieuwVakController = new GridVakjeController(nieuwVakModel, nieuwVakView, pauseController);

        testGrid.put(nieuweLocatie, nieuwVakController);

        // Plaats de gast eerst op de oude locatie
        JPanel oudPanel = testVakController.getGridView().getGuestPanel();
        oudPanel.add(testGast.getGastLabel());

        // Actie: Verplaats de gast in het model en roep de methode aan
        Locatie oudeLocatie = testLocatie;
        testGast.setLocatie(nieuweLocatie); // Zorg dat je GastModel een setLocatie heeft
        gastPlaatser.onGastVerplaatst(testGast, oudeLocatie);

        // Assert: Check of hij weg is bij oud en erbij bij nieuw
        assertEquals(0, oudPanel.getComponentCount(), "Gast moet verwijderd zijn uit het oude vakje.");
        assertEquals(1, nieuwVakController.getGridView().getGuestPanel().getComponentCount(), "Gast moet toegevoegd zijn aan het nieuwe vakje.");
    }


    @Test
    public void testOnGastVerplaatst_ZelfdeLocatieDoetNiets() {
        JPanel panel = testVakController.getGridView().getGuestPanel();
        panel.add(testGast.getGastLabel());

        // We simuleren een verplaatsing naar de huidige locatie (1,1 naar 1,1)
        gastPlaatser.onGastVerplaatst(testGast, testLocatie);

        // De gast moet gewoon blijven staan en de methode moet vroegtijdig 'returnen'
        assertEquals(1, panel.getComponentCount(), "Gast moet gewoon blijven staan als de locatie hetzelfde is.");
    }



    @Test
    public void testOnGastVerplaatst_OudeLocatieNull_AlleenToevoegen() {
        // Setup: Zorg dat de gast op de nieuwe plek staat, maar de 'oudeLocatie' parameter is null
        JPanel nieuwPanel = testVakController.getGridView().getGuestPanel();

        // Actie: Verplaatsing aanroepen met null als oude locatie
        gastPlaatser.onGastVerplaatst(testGast, null);

        // Assert: De code moet niet crashen en de gast moet op de huidige locatie geplaatst worden
        assertEquals(1, nieuwPanel.getComponentCount(), "Gast moet alsnog geplaatst worden, ook als de oude locatie onbekend was.");
    }

    @Test
    void testOnGastVertrokken_VerwijdertLabel() {
        // Setup: Voeg de gast handmatig toe aan het panel
        JPanel guestPanel = testVakController.getGridView().getGuestPanel();
        guestPanel.add(testGast.getGastLabel());
        int initialCount = guestPanel.getComponentCount();

        // Actie
        gastPlaatser.onGastVertrokken(testGast);

        // Assert
        assertEquals(initialCount - 1, guestPanel.getComponentCount(),
                "Het gast-label moet verwijderd zijn uit het panel.");
    }


    @Test
    void testOnGastVertrokken_MetNullLocatie_CrashtNiet() {
        // Setup: Maak een gast zonder locatie
        GastModel spookGast = new GastModel(99, null, null, KamerClassificatie.eenSter, (KamerModel) testRuimte);

        // Actie & Assert
        assertDoesNotThrow(() -> gastPlaatser.onGastVertrokken(spookGast),
                "De methode mag niet crashen als de locatie van de gast null is.");
    }
}