import Controller.Layout.LayoutLoader;
import Controller.SimulatieController;
import Controller.Systeem.ButtonController;
import Controller.Systeem.DarkModeController;
import Controller.Systeem.Interfaces.reset;
import Controller.Systeem.PauseController;
import Controller.Systeem.SettingsController;
import Model.Layout.LayoutModel;
import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.OverzichtView;
import View.Systeem.TimeManagementPanel;
import hotelevents.HotelEventManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class TestButtonController {

    private HotelSimulatieView view;
    private OverzichtView view2;
    private SimulatieController simulatieController;
    private HotelEventManager manager;
    private ButtonController buttonController;

    @BeforeEach
    void setUp() {

        DarkModeModel darkModeModel = new DarkModeModel();

        view = new HotelSimulatieView(darkModeModel);

        TimeManagementPanel panel =
                new TimeManagementPanel(view.getTopBar(), darkModeModel);

        manager = new HotelEventManager(false);

        simulatieController = new SimulatieController();

        PauseController pauseController =
                new PauseController(manager, view2);

        SettingsController settingsController =
                new SettingsController(
                        view,
                        panel,
                        new DarkModeController(null, null, darkModeModel));

        LayoutLoader layoutLoader =
                new LayoutLoader(manager, view, null, pauseController);

        // Maak de controller aan zodat alle buttons een ActionListener krijgen.
        buttonController = new ButtonController(
                view,
                simulatieController,
                manager,
                layoutLoader,
                settingsController);
    }



    // Dummy reset-listener.
    private static class TestReset implements reset {

        boolean resetAangeroepen = false;

        @Override
        public void resetSimulatie() {
            resetAangeroepen = true;
        }
    }

    // Controleert dat de simulatie niet start zonder layout.
    @Test
    void testStartSimulationButtonZonderLayout() {

        simulatieController.setStarted(false);

        view.getStartSimulationButton().doClick();

        assertFalse(simulatieController.getStarted());
    }

    // Controleert dat de settingsknop bestaat.
    @Test
    void testSettingsButtonBestaat() {

        assertNotNull(view.getSettingsButton());
    }

    // Controleert dat op de settingsknop geklikt kan worden.
    @Test
    void testOpenWindow() {

        view.getSettingsButton().doClick();

        assertNotNull(view.getSettingsButton());
    }

    // Controleert dat stoppen niets doet als de simulatie niet gestart is.
    @Test
    void testStopSimulationNietGestart() {

        simulatieController.setStarted(false);

        view.getStopSimulationButton().doClick();

        assertFalse(simulatieController.getStarted());
    }

    // Controleert dat de simulatie stopt.


    // Controleert dat de simulatie niet opnieuw gestart wordt.
    @Test
    void testStartSimulationWanneerAlGestart() {

        simulatieController.setStarted(true);

        view.getStartSimulationButton().doClick();

        assertTrue(simulatieController.getStarted());
    }

    @Test
    void testStartSimulationMetLayout() throws Exception {

        LayoutModel model = new LayoutModel();

        Field field = ButtonController.class.getDeclaredField("model");
        field.setAccessible(true);
        field.set(buttonController, model);

        simulatieController.setStarted(false);

        view.getStartSimulationButton().doClick();

        assertTrue(simulatieController.getStarted());
    }


    @Test
    void testLoadScenarioButton() {

        view.getLoadScenarioButton().doClick();

        assertNotNull(view.getLoadScenarioButton());
    }

    @Test
    void testSetListeners() {

        TestReset listener = new TestReset();

        buttonController.setListeners(listener);

        assertNotNull(listener);
    }

}



































