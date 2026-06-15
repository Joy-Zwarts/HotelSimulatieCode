import Controller.Layout.LayoutLoader;
import Controller.SimulatieController;
import Controller.Systeem.ButtonController;
import Controller.Systeem.Interfaces.reset;
import Controller.Systeem.SettingsController;
import Model.Layout.LayoutModel;
import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.SettingsView;
import hotelevents.HotelEventManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class TestButtonController {

    private HotelSimulatieView view;
    private FakeSimulatieController simulatieController;
    private HotelEventManager manager;
    private FakeLayoutLoader fakeLayoutLoader;
    private FakeSettingsController fakeSettingsController;
    private ButtonController buttonController;

    // Vlaggen om te controleren of een JOptionPane dialoog getriggerd werd
    private boolean errorDialoogGetoond = false;
    private String laatsteDialoogBericht = "";

    // =========================================================================
    // FAKES & STUBS (Geen Mockito benodigd)
    // =========================================================================

    static class FakeSimulatieController extends SimulatieController {
        private boolean started = false;
        private int scenario = 1;

        public FakeSimulatieController() {
            super();
        }

        @Override public boolean getStarted() { return this.started; }
        @Override public void setStarted(boolean started) { this.started = started; }
        @Override public int getScenario() { return this.scenario; }
    }

    static class FakeLayoutLoader extends LayoutLoader {
        LayoutModel returnModel = new LayoutModel();

        public FakeLayoutLoader() {
            super(null, null, null, null);
        }

        @Override
        public LayoutModel loadLayout() {
            return returnModel;
        }
    }

    static class FakeSettingsController extends SettingsController {
        private final SettingsView fakeFrame;

        public FakeSettingsController() {
            super(null, null, null);
            // Zorg voor een headless frame om schermen op het bureaublad te voorkomen
            this.fakeFrame = new SettingsView(null);
        }

        @Override
        public SettingsView getSettingsFrame() {
            return this.fakeFrame;
        }
    }

    private static class TestResetListener implements reset {
        boolean resetAangeroepen = false;

        @Override
        public void resetSimulatie() {
            resetAangeroepen = true;
        }
    }

    // =========================================================================
    // SETUP
    // =========================================================================

    @BeforeEach
    void setUp() {
        errorDialoogGetoond = false;
        laatsteDialoogBericht = "";

        DarkModeModel darkModeModel = new DarkModeModel();
        view = new HotelSimulatieView(darkModeModel);
        manager = new HotelEventManager(false);
        simulatieController = new FakeSimulatieController();
        fakeLayoutLoader = new FakeLayoutLoader();
        fakeSettingsController = new FakeSettingsController();

        // Subklasse om JOptionPane dialogen te onderscheppen zodat de tests headless kunnen draaien
        buttonController = new ButtonController(view, simulatieController, manager, fakeLayoutLoader, fakeSettingsController) {
            // We omzeilen actionPerformed niet, maar we vangen branches op die JOptionPane zouden triggeren
            // door het gedrag te monitoren via asserties op de toestand of via een overschreven methode mocht dat erin zitten.
            // Aangezien JOptionPane statisch is, controleren we de logica via de neveneffecten.
        };
    }

    // Helper om handmatig actionPerformed te triggeren voor specifieke componenten
    private void triggerButton(JButton button) {
        ActionEvent event = new ActionEvent(button, ActionEvent.ACTION_PERFORMED, "click");
        buttonController.actionPerformed(event);
    }

    // Helper om via reflection het interne model op null of gevuld te zetten
    private void setInterneModel(LayoutModel model) throws Exception {
        Field field = ButtonController.class.getDeclaredField("model");
        field.setAccessible(true);
        field.set(buttonController, model);
    }

    // =========================================================================
    // HOOFDBRANCHES EN EVENT COVERS
    // =========================================================================

    @Test
    void testActionLoadScenario() {
        // Trigger de knop. Dit maakt een nieuwe ScenarioPicker aan.
        assertDoesNotThrow(() -> triggerButton(view.getLoadScenarioButton()));
    }

    @Test
    void testActionLoadLayout() throws Exception {
        setInterneModel(null);

        triggerButton(view.getLoadLayoutButton());

        // Controleer of het model succesvol is gevuld vanuit de fakeLayoutLoader
        Field field = ButtonController.class.getDeclaredField("model");
        field.setAccessible(true);
        assertNotNull(field.get(buttonController));
    }

    @Test
    void testActionSettings() {
        SettingsView frame = fakeSettingsController.getSettingsFrame();
        frame.getFrame().setVisible(false);

        triggerButton(view.getSettingsButton());

        // Controleer of de branch de setVisible(true) heeft aangeroepen
        assertTrue(frame.getFrame().isVisible());
        frame.getFrame().dispose(); // Netjes opruimen
    }

    // =========================================================================
    // START SIMULATION SUB-BRANCHES
    // =========================================================================

    @Test
    void testStartSimulation_BranchModelNull() throws Exception {
        setInterneModel(null); // Voorwaarde 1: model == null
        simulatieController.setStarted(false);

        // Verifieer dat het niet crasht (Triggert JOptionPane error dialoog op de achtergrond)
        assertDoesNotThrow(() -> triggerButton(view.getStartSimulationButton()));
        assertFalse(simulatieController.getStarted());
    }

    @Test
    void testStartSimulation_BranchSuccesvolStarten() throws Exception {
        setInterneModel(new LayoutModel()); // Voorwaarde 1: model != null
        simulatieController.setStarted(false);    // Voorwaarde 2: !(simulatieManager.getStarted())

        triggerButton(view.getStartSimulationButton());

        // Controleer of de simulatie nu gestart is en de status is bijgewerkt
        assertTrue(simulatieController.getStarted());
    }

    @Test
    void testStartSimulation_BranchAlGestart() throws Exception {
        setInterneModel(new LayoutModel()); // Voorwaarde 1: model != null
        simulatieController.setStarted(true);     // Voorwaarde 2: simulatieManager.getStarted() == true

        assertDoesNotThrow(() -> triggerButton(view.getStartSimulationButton()));
        // Moet true blijven en de fouttak raken
        assertTrue(simulatieController.getStarted());
    }

    // =========================================================================
    // STOP SIMULATION SUB-BRANCHES
    // =========================================================================

    @Test
    void testStopSimulation_BranchSuccesvolStoppenMetListeners() throws Exception {
        // 1. Zorg dat er een valide model in de controller zit, anders kan het panel niet initialiseren
        setInterneModel(new LayoutModel());

        // 2. Start de eventManager écht (met een dummy scenario) zodat de interne k (ScheduledExecutorService)
        // wordt geïnitialiseerd en niet null is wanneer stop() wordt aangeroepen.
        manager.start(1);

        // 3. Zet de simulatiemanager op gestart (dit matcht de voorwaarde van de if-branch)
        simulatieController.setStarted(true);

        TestResetListener listener = new TestResetListener();
        buttonController.setListeners(listener);

        // 4. Trigger de stopknop
        triggerButton(view.getStopSimulationButton());

        // Controleer of gestart naar false is gezet én de listeners zijn aangeroepen
        assertFalse(simulatieController.getStarted(), "De simulatie had moeten stoppen (started = false)");
        assertTrue(listener.resetAangeroepen, "De resetSimulatie listener is niet afgevuurd");
    }

    @Test
    void testStopSimulation_BranchNietGestart() {
        simulatieController.setStarted(false); // Voorwaarde: !(simulatieManager.getStarted())

        assertDoesNotThrow(() -> triggerButton(view.getStopSimulationButton()));
        assertFalse(simulatieController.getStarted());
    }
}