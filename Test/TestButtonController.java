import Controller.Systeem.ButtonController;
import Controller.Systeem.SettingsController;
import Controller.Layout.LayoutLoader;
import Controller.SimulatieController;
import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.TimeManagementPanel;
import hotelevents.HotelEventManager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestButtonController {

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
}