import Controller.SimulatieController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestSimulatieController {

    // Controleert de beginwaarde van started.
    @Test
    void StartedDefault() {

        SimulatieController controller = new SimulatieController();

        assertFalse(controller.getStarted());
    }

    // Controleert setStarted().
    @Test
    void SetStarted() {

        SimulatieController controller = new SimulatieController();

        controller.setStarted(true);

        assertTrue(controller.getStarted());
    }

    // Controleert de standaardwaarde van scenario.
    @Test
    void ScenarioDefault() {

        SimulatieController controller = new SimulatieController();

        assertEquals(1, controller.getScenario());
    }

    // Controleert setScenario().
    @Test
    void SetScenario() {

        SimulatieController controller = new SimulatieController();

        controller.setScenario(3);

        assertEquals(3, controller.getScenario());
    }

    // Controleert resetSimulatie().
    @Test
    void ResetSimulatie() {

        SimulatieController controller = new SimulatieController();

        controller.setStarted(true);
        controller.setScenario(3);

        controller.resetSimulatie();

        assertFalse(controller.getStarted());
        assertEquals(1, controller.getScenario());
    }

    // Controleert scenario 1.
    @Test
    void MaxHteScenario1() {

        SimulatieController controller = new SimulatieController();

        controller.setScenario(1);

        assertEquals(500, controller.getMaxHteVoorScenario());
    }

    // Controleert scenario 2.
    @Test
    void MaxHteScenario2() {

        SimulatieController controller = new SimulatieController();

        controller.setScenario(2);

        assertEquals(1000, controller.getMaxHteVoorScenario());
    }

    // Controleert scenario 3.
    @Test
    void MaxHteScenario3() {

        SimulatieController controller = new SimulatieController();

        controller.setScenario(3);

        assertEquals(2000, controller.getMaxHteVoorScenario());
    }

    // Controleert de fallback.
    @Test
    void MaxHteOnbekendScenario() {

        SimulatieController controller = new SimulatieController();

        controller.setScenario(99);

        assertEquals(2000, controller.getMaxHteVoorScenario());
    }
}