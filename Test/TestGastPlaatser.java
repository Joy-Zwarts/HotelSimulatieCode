
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
}