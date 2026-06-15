package Controller.Layout;

import Controller.Systeem.PauseController;
import Model.Layout.GridVakjeModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;
import Model.Layout.Locatie;
import View.Layout.GridVakjeView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static org.junit.jupiter.api.Assertions.*;

class GridVakjeControllerTest {

    private FakePauseController fakePauseController;
    private FakeGridVakjeView fakeView;
    private FakeGridVakjeModel fakeModel;
    private GridVakjeController controller;

    static class FakePauseController extends PauseController {
        int pauseCount = 0;

        public FakePauseController() {
            super(null, null);
        }

        @Override
        public void pause() {
            pauseCount++;
        }
    }

    static class FakeGridVakjeView extends GridVakjeView {
        boolean repainted = false;
        private final JPanel customPanel = new JPanel() {
            @Override
            public void repaint() {
                super.repaint();
                repainted = true;
            }
        };

        public FakeGridVakjeView() {
            super(0, 0, 0, 0);
        }

        @Override
        public JComponent getVakjePanel() {
            return customPanel;
        }
    }

    static class FakeGridVakjeModel extends GridVakjeModel {
        private RuimteModel testRuimte;

        public FakeGridVakjeModel() {
            super(0, 0);
        }

        public void setTestRuimte(RuimteModel ruimte) {
            this.testRuimte = ruimte;
        }

        @Override
        public RuimteModel getRuimte() {
            return testRuimte;
        }
    }

    static class FakeRuimte extends RuimteModel {
        private final KamerType vastType;

        public FakeRuimte(KamerType type) {
            super(type, new Locatie(0, 0), "1x1");
            this.vastType = type;
        }

        @Override
        public KamerType getAreaType() {
            return this.vastType;
        }
    }


    @BeforeEach
    void setup() {
        fakePauseController = new FakePauseController();
        fakeView = new FakeGridVakjeView();
        fakeModel = new FakeGridVakjeModel();

        controller = new GridVakjeController(fakeModel, fakeView, fakePauseController);
    }

    private void triggerMouseClick() {
        JPanel panel = (JPanel) fakeView.getVakjePanel();
        MouseListener[] listeners = panel.getMouseListeners();
        assertTrue(listeners.length > 0, "Er is geen MouseListener geregistreerd op het panel!");

        MouseEvent clickEvent = new MouseEvent(
                panel, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(),
                0, 0, 0, 1, false
        );

        for (MouseListener listener : listeners) {
            listener.mouseClicked(clickEvent);
        }
    }

    @Test
    void wanneerRuimteNullIsTest() {
        fakeModel.setTestRuimte(null);

        assertDoesNotThrow(this::triggerMouseClick);
        assertEquals(0, fakePauseController.pauseCount);
    }

    @Test
    void RuimteGeenLobbyTest() {
        FakeRuimte restaurant = new FakeRuimte(KamerType.RESTAURANT);
        fakeModel.setTestRuimte(restaurant);

        triggerMouseClick();

        assertEquals(0, fakePauseController.pauseCount);
    }

    @Test
    void pauzeerSimulatieTest() throws Exception {
        FakeRuimte lobby = new FakeRuimte(KamerType.LOBBY);
        fakeModel.setTestRuimte(lobby);

        triggerMouseClick();

        SwingUtilities.invokeAndWait(() -> {});

        assertEquals(1, fakePauseController.pauseCount, "De simulatie had gepauzeerd moeten worden!");
    }

    @Test
    void testGettersEnUpdateView() {
        assertEquals(fakeModel, controller.getModel());
        assertEquals(fakeView, controller.getGridView());

        fakeView.repainted = false;

        assertFalse(fakeView.repainted, "Vlag moet false zijn voor de updateView aanroep.");

        controller.updateView();

        assertTrue(fakeView.repainted, "Repaint is niet correct aangeroepen op de view.");
    }
}