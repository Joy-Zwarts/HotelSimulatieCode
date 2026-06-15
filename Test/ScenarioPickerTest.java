import Controller.SimulatieController;
import Controller.Systeem.ScenarioPicker;
import View.Systeem.ScenarioPickerView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;

public class ScenarioPickerTest {

    private ScenarioPicker scenarioPicker;
    private FakeSimulatieController fakeSimulatieController;
    private FakeScenarioPickerView fakeScenarioPickerView;

    static class FakeSimulatieController extends SimulatieController {
        int gekozenScenario = -1;

        public FakeSimulatieController() {
            super();
        }

        @Override
        public void setScenario(int scenario) {
            this.gekozenScenario = scenario;
        }
    }

    static class FakeScenarioPickerView extends ScenarioPickerView {
        private final JComboBox<String> dummyComboBox;
        private final JButton dummyButton;
        boolean isGesloten = false;

        public FakeScenarioPickerView() {
            super(null);
            this.dummyComboBox = new JComboBox<>(new String[]{"Scenario 1", "Scenario 2", "Scenario 3", "Scenario 4", "Onbekend Scenario"});
            this.dummyButton = new JButton();
        }

        @Override public JComboBox<String> getComboBox() { return this.dummyComboBox; }
        @Override public JButton getConfirmButton() { return this.dummyButton; }
        @Override public void close() { this.isGesloten = true; }
    }

    @BeforeEach
    void setUp() throws Exception {
        fakeSimulatieController = new FakeSimulatieController();
        fakeScenarioPickerView = new FakeScenarioPickerView();

        scenarioPicker = new ScenarioPicker(null, fakeSimulatieController);

        java.lang.reflect.Field viewField = ScenarioPicker.class.getDeclaredField("scenarioPickerView");
        viewField.setAccessible(true);
        viewField.set(scenarioPicker, fakeScenarioPickerView);
    }

    @Test
    void testPickScenario1() {
        fakeScenarioPickerView.getComboBox().setSelectedItem("Scenario 1");

        ActionEvent event = new ActionEvent(fakeScenarioPickerView.getConfirmButton(), ActionEvent.ACTION_PERFORMED, "click");
        scenarioPicker.actionPerformed(event);

        assertEquals(1, fakeSimulatieController.gekozenScenario);
        assertTrue(fakeScenarioPickerView.isGesloten);
    }

    @Test
    void testPickScenario2() {
        fakeScenarioPickerView.getComboBox().setSelectedItem("Scenario 2");

        ActionEvent event = new ActionEvent(fakeScenarioPickerView.getConfirmButton(), ActionEvent.ACTION_PERFORMED, "click");
        scenarioPicker.actionPerformed(event);

        assertEquals(2, fakeSimulatieController.gekozenScenario);
        assertTrue(fakeScenarioPickerView.isGesloten);
    }

    @Test
    void testPickScenario3() {
        fakeScenarioPickerView.getComboBox().setSelectedItem("Scenario 3");

        ActionEvent event = new ActionEvent(fakeScenarioPickerView.getConfirmButton(), ActionEvent.ACTION_PERFORMED, "click");
        scenarioPicker.actionPerformed(event);

        assertEquals(3, fakeSimulatieController.gekozenScenario);
        assertTrue(fakeScenarioPickerView.isGesloten);
    }

    @Test
    void testPickScenario4() {
        fakeScenarioPickerView.getComboBox().setSelectedItem("Scenario 4");

        ActionEvent event = new ActionEvent(fakeScenarioPickerView.getConfirmButton(), ActionEvent.ACTION_PERFORMED, "click");
        scenarioPicker.actionPerformed(event);

        assertEquals(4, fakeSimulatieController.gekozenScenario);
        assertTrue(fakeScenarioPickerView.isGesloten);
    }

    @Test
    void testDefaultBranch() {
        fakeScenarioPickerView.getComboBox().setSelectedItem("Onbekend Scenario");

        ActionEvent event = new ActionEvent(fakeScenarioPickerView.getConfirmButton(), ActionEvent.ACTION_PERFORMED, "click");
        scenarioPicker.actionPerformed(event);

        assertEquals(1, fakeSimulatieController.gekozenScenario);
        assertTrue(fakeScenarioPickerView.isGesloten);
    }
}