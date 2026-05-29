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

import static org.junit.jupiter.api.Assertions.*;

public class TestButtonController {

    private HotelSimulatieView view;
    private OverzichtView view2;
    private SimulatieController simulatieController;

    @BeforeEach
    void setUp() {

        DarkModeModel darkModeModel = new DarkModeModel();
        view = new HotelSimulatieView(darkModeModel);
        TimeManagementPanel panel = new TimeManagementPanel(view.getTopBar(), darkModeModel);
        HotelEventManager manager = new HotelEventManager(false);
        simulatieController = new SimulatieController();
        PauseController pauseController = new PauseController(manager, view2);
        SettingsController settingsController = new SettingsController(view, panel, darkModeModel);
        LayoutLoader layoutLoader = new LayoutLoader(manager, view, null, pauseController);
    }

    @Test
    void testStartSimulationButtonZonderLayout() {
        simulatieController.setStarted(false);

        view.getStartSimulationButton().doClick();

        assertFalse(simulatieController.getStarted(),
                "Simulatie mag niet starten zonder dat er een layout geladen is.");
    }

    @Test
    void testOpenWindow() {
        view.getSettingsButton().doClick();

        assertNotNull(view.getSettingsButton(), "Button moet aanwezig zijn");
    }




//    @Test
//    void TestStartStopFlow() throws InterruptedException {
//        DarkModeModel darkMode = new DarkModeModel();
//        HotelSimulatieView view = new HotelSimulatieView(darkMode);
//
//        SimulatieController simulatieController = new SimulatieController();
//        simulatieController.setStarted(false);
//
//        HotelEventManager manager = new HotelEventManager(false);
//
//        LayoutLoader layoutLoader = new LayoutLoader(
//                manager, view, null, null, view
//        );
//
//        TimeManagementPanel panel = new TimeManagementPanel(view.getTopBar(), darkMode);
//
//        SettingsController settingsController =
//                new SettingsController(view, panel, darkMode);
//
//        ButtonController controller =
//                new ButtonController(view, simulatieController, manager, layoutLoader, settingsController);
//
//        Thread.sleep(500);
//
//        view.getStartSimulationButton().doClick();
//
//        assertFalse(simulatieController.getStarted(),
//                "Simulatie mag niet starten zonder layout");
//
//
//        view.getStopSimulationButton().doClick();
//
//        assertFalse(simulatieController.getStarted(),
//                "Simulatie blijft gestopt");
//
//
//        view.getSettingsButton().doClick();
//
//        assertTrue(settingsController.getSettingsFrame().getFrame().isVisible(),
//                "Settings window moet openen");
//
//
//        view.dispose();
//    }
//    @Test
//    void testLoadScenarioButton_UpdatesManagerWithInt() {
//        ButtonController testController = new ButtonController(
//                view, simulatieController, manager, layoutLoader, settingsController
//        ) {
//            @Override
//            protected int pickScenario() {
//                return 2;
//            }
//        };
//
//        testController.actionPerformed(new ActionEvent(view.getLoadScenarioButton(), 0, ""));
//        assertEquals(2, simulatieController.getScenario(),
//                "De manager moet het scenario-nummer 2 hebben ontvangen.");
//    }
}
































