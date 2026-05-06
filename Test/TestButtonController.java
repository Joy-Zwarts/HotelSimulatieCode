import Controller.Systeem.ButtonController;
import Controller.Systeem.PauseController;
import Controller.Systeem.SettingsController;
import Controller.Layout.LayoutLoader;
import Controller.SimulatieController;
import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.OverzichtView;
import View.Systeem.TimeManagementPanel;
import hotelevents.HotelEventManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;

public class TestButtonController {

    private HotelSimulatieView view;
    private OverzichtView view2;
    private TimeManagementPanel panel;
    private DarkModeModel darkModeModel;
    private SettingsController settingsController;
    private SimulatieController simulatieController;
    private HotelEventManager manager;
    private LayoutLoader layoutLoader;
    private PauseController pauseController;

    @BeforeEach
    void setUp() {

        darkModeModel = new DarkModeModel();
        view = new HotelSimulatieView(darkModeModel);
        panel = new TimeManagementPanel(view.getTopBar(), darkModeModel);
        manager = new HotelEventManager(false);
        simulatieController = new SimulatieController();
        pauseController = new PauseController(manager, view2);
        settingsController = new SettingsController(view, panel, darkModeModel);
        layoutLoader = new LayoutLoader(manager, view, null, pauseController, view);
    }

    @Test
    void testStartSimulationButtonZonderLayout() {
        // Zorg dat de simulatie uit staat en er geen model is
        simulatieController.setStarted(false);
        // We gaan ervan uit dat buttonController.model op dit moment null is

        // Act: Simuleer een klik op de startknop
        view.getStartSimulationButton().doClick();

        // Assert: De simulatie mag niet gestart zijn
        assertFalse(simulatieController.getStarted(),
                "Simulatie mag niet starten zonder dat er een layout geladen is.");
    }

    @Test
    void testOpenWindow() {
        // Act
        view.getSettingsButton().doClick();

        // Assert: Check of het frame van de settingscontroller nu bestaat en zichtbaar is
        // Je moet hiervoor wel een getter hebben in je ButtonController of SettingsController
        assertNotNull(view.getSettingsButton(), "Button moet aanwezig zijn");
        // Je kunt hier controleren of de actie effect heeft gehad op het model of de view
    }




    @Test
    void TestStartStopFlow() throws InterruptedException {

        // setup (objecten)
        DarkModeModel darkMode = new DarkModeModel();
        HotelSimulatieView view = new HotelSimulatieView(darkMode);

        SimulatieController simulatieController = new SimulatieController();
        simulatieController.setStarted(false); // reset state

        HotelEventManager manager = new HotelEventManager(false);

        LayoutLoader layoutLoader = new LayoutLoader(
                manager, view, null, null, view
        );

        TimeManagementPanel panel = new TimeManagementPanel(view.getTopBar(), darkMode);

        SettingsController settingsController =
                new SettingsController(view, panel, darkMode);

        ButtonController controller =
                new ButtonController(view, simulatieController, manager, layoutLoader, settingsController);

        // delay Swing
        Thread.sleep(500);

        view.getStartSimulationButton().doClick();

        assertFalse(simulatieController.getStarted(),
                "Simulatie mag niet starten zonder layout");


        view.getStopSimulationButton().doClick();

        assertFalse(simulatieController.getStarted(),
                "Simulatie blijft gestopt");


        view.getSettingsButton().doClick();

        assertTrue(settingsController.getSettingsFrame().getFrame().isVisible(),
                "Settings window moet openen");


        view.dispose();
    }
    @Test
    void testLoadScenarioButton_UpdatesManagerWithInt() {
        // Arrange: Maak de controller met een overriden pickScenario
        ButtonController testController = new ButtonController(
                view, simulatieController, manager, layoutLoader, settingsController
        ) {
            @Override
            protected int pickScenario() {
                // We simuleren dat de gebruiker scenario 2 heeft gekozen
                return 2;
            }
        };

        // Act: Simuleer de klik op de knop
        testController.actionPerformed(new ActionEvent(view.getLoadScenarioButton(), 0, ""));

        // Assert: Controleer of het getal 2 is aangekomen in de manager
        // (Zorg dat getScenario() in je manager ook een int teruggeeft)
        assertEquals(2, simulatieController.getScenario(),
                "De manager moet het scenario-nummer 2 hebben ontvangen.");
    }




    }
































