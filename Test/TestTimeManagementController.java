import Controller.SimulatieController;
import Controller.Systeem.Interfaces.onTimeChange;
import Controller.Systeem.TimeManagementController;
import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.TimeManagementPanel;
import hotelevents.HotelEventManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestTimeManagementController {

    private HotelEventManager manager;
    private SimulatieController simulatieController;
    private HotelSimulatieView view;
    private TimeManagementPanel panel;
    private TimeManagementController controller;

    // Dummy listener om te controleren of de juiste snelheid wordt doorgegeven.
    private static class TestListener implements onTimeChange {

        int snelheid = -1;

        @Override
        public void timeChange(int tijd) {
            snelheid = tijd;
        }
    }

    @BeforeEach
    void setUp() {

        DarkModeModel model = new DarkModeModel();

        manager = new HotelEventManager(false);

        simulatieController = new SimulatieController();

        view = new HotelSimulatieView(model);

        panel = new TimeManagementPanel(view.getTopBar(), model);

        controller = new TimeManagementController(
                manager,
                simulatieController,
                view,
                panel
        );
    }

    // Controleert dat een listener toegevoegd kan worden.
    @Test
    void testListenerToevoegen() {

        TestListener listener = new TestListener();

        controller.setListener(listener);

        assertNotNull(listener);
    }

    // Controleert de normale snelheid.
    @Test
    void testNormaleSnelheid() {

        simulatieController.setStarted(true);

        TestListener listener = new TestListener();

        controller.setListener(listener);

        panel.getNormaleTijd().doClick();

        assertEquals(1000, listener.snelheid);
    }

    // Controleert fast forward.
    @Test
    void testFastForward() {

        simulatieController.setStarted(true);

        TestListener listener = new TestListener();

        controller.setListener(listener);

        panel.getFastForwardTijd().doClick();

        assertEquals(600, listener.snelheid);
    }

    // Controleert double fast forward.
    @Test
    void testDoubleFastForward() {

        simulatieController.setStarted(true);

        TestListener listener = new TestListener();

        controller.setListener(listener);

        panel.getDoubleFastForwardTijd().doClick();

        assertEquals(100, listener.snelheid);
    }

    // Controleert dat de normale knop niets doet als de simulatie niet gestart is.
    @Test
    void testNormaleTijdNietGestart() {

        simulatieController.setStarted(false);

        TestListener listener = new TestListener();

        controller.setListener(listener);

        panel.getNormaleTijd().doClick();

        assertEquals(-1, listener.snelheid);
    }

    // Controleert dat fast forward niets doet als de simulatie niet gestart is.
    @Test
    void testFastForwardNietGestart() {

        simulatieController.setStarted(false);

        TestListener listener = new TestListener();

        controller.setListener(listener);

        panel.getFastForwardTijd().doClick();

        assertEquals(-1, listener.snelheid);
    }

    // Controleert dat double fast forward niets doet als de simulatie niet gestart is.
    @Test
    void testDoubleFastForwardNietGestart() {

        simulatieController.setStarted(false);

        TestListener listener = new TestListener();

        controller.setListener(listener);

        panel.getDoubleFastForwardTijd().doClick();

        assertEquals(-1, listener.snelheid);
    }
}