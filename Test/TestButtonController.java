import Controller.Systeem.ButtonController;
import Controller.SimulatieController;
import Controller.Layout.LayoutLoader;

import Model.Layout.LayoutModel;
import Model.Systeem.DarkModeModel;

import View.Systeem.HotelSimulatieView;

import hotelevents.HotelEventManager;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;

class TestButtonController {

    // 🔧 Minimal View
    class SimpleView extends HotelSimulatieView {
        JButton start = new JButton();
        JButton stop = new JButton();
        JButton loadLayout = new JButton();
        JButton settings = new JButton();
        JButton scenario = new JButton();

        public SimpleView() {
            super(new DarkModeModel());
        }

        @Override public JButton getStartSimulationButton() { return start; }
        @Override public JButton getStopSimulationButton() { return stop; }
        @Override public JButton getLoadLayoutButton() { return loadLayout; }
        @Override public JButton getSettingsButton() { return settings; }
        @Override public JButton getLoadScenarioButton() { return scenario; }

        @Override public void setRightView(JPanel panel) {}
    }

    // 🔧 Minimal SimulatieController
    class SimpleSimulatieController extends SimulatieController {
        boolean started = false;

        @Override public boolean getStarted() { return started; }
        @Override public void setStarted(boolean val) { started = val; }
    }

    // 🔧 Dummy LayoutLoader (BELANGRIJK FIX)
    class DummyLayoutLoader extends LayoutLoader {

        public DummyLayoutLoader() {
            super(null, null, null, null, null);
        }

        @Override
        public LayoutModel loadLayout() {
            // geen EventPanel, geen manager, geen crash
            return new LayoutModel();
        }
    }

    @Test
    void testStartKnopZonderLayout() {
        SimpleView view = new SimpleView();
        SimpleSimulatieController sim = new SimpleSimulatieController();

        ButtonController controller = new ButtonController(
                view,
                sim,
                new HotelEventManager(),
                new DummyLayoutLoader(),
                null,
                null
        );

        controller.actionPerformed(
                new ActionEvent(view.getStartSimulationButton(), 1, "")
        );

        assertFalse(sim.getStarted());
    }

    @Test
    void testLoadLayoutKnop() {
        SimpleView view = new SimpleView();
        SimpleSimulatieController sim = new SimpleSimulatieController();

        ButtonController controller = new ButtonController(
                view,
                sim,
                new HotelEventManager(),
                new DummyLayoutLoader(),
                null,
                null
        );

        controller.actionPerformed(
                new ActionEvent(view.getLoadLayoutButton(), 1, "")
        );

        // als hij hier komt zonder crash → OK
        assertTrue(true);
    }

    @Test
    void testStopKnopZonderStart() {
        SimpleView view = new SimpleView();
        SimpleSimulatieController sim = new SimpleSimulatieController();

        ButtonController controller = new ButtonController(
                view,
                sim,
                new HotelEventManager(),
                new DummyLayoutLoader(),
                null,
                null
        );

        controller.actionPerformed(
                new ActionEvent(view.getStopSimulationButton(), 1, "")
        );

        assertFalse(sim.getStarted());
    }
}