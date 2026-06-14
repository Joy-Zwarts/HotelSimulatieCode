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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GridVakjeControllerTest {

    private FakePauseController fakePauseController;
    private FakeGridVakjeView fakeView;
    private FakeGridVakjeModel fakeModel;
    private GridVakjeController controller;

    // =========================================================================
    // Veilige, Crash-bestendige Fakes
    // =========================================================================

    static class FakePauseController extends PauseController {
        int pauseCount = 0;

        public FakePauseController() {
            // We geven null mee als de superklasse dat toestaat,
            // of een dummy-object mocht de superklasse direct crashen op null.
            super(null, null);
        }

        @Override
        public void pause() {
            pauseCount++;
        }
    }

    static class FakeGridVakjeView extends GridVakjeView {
        boolean repainted = false;

        // We maken een anonieme JPanel subclass waarin we repaint opvangen
        private final JPanel customPanel = new JPanel() {
            @Override
            public void repaint() {
                super.repaint();
                repainted = true; // Dit zet de vlag om als de controller repaint() aanroept!
            }
        };

        public FakeGridVakjeView() {
            super(0, 0, 0, 0); // Veilige aanroep super-constructor
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
            // We geven een veilige dummy Locatie mee in plaats van null
            // om NPE's in de super-constructor te voorkomen.
            super(type, new Locatie(0, 0), "1x1");
            this.vastType = type;
        }

        @Override
        public KamerType getAreaType() {
            return this.vastType;
        }
    }

    // =========================================================================
    // Test Setup
    // =========================================================================

    @BeforeEach
    void setup() {
        fakePauseController = new FakePauseController();
        fakeView = new FakeGridVakjeView();
        fakeModel = new FakeGridVakjeModel();

        controller = new GridVakjeController(fakeModel, fakeView, fakePauseController);
    }

    /**
     * Hulpmethode om een handmatige muisklik op het JPanel te simuleren.
     */
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

    // =========================================================================
    // Branch Tests (100% Coverage op handleClick)
    // =========================================================================

    @Test
    void handleClick_wanneerRuimteNullIs_doetNiets() {
        fakeModel.setTestRuimte(null); // Vertakking 1: ruimte == null

        assertDoesNotThrow(this::triggerMouseClick);
        assertEquals(0, fakePauseController.pauseCount);
    }

    @Test
    void handleClick_wanneerRuimteBestaatMaarGeenLobbyIs_doetNiets() {
        FakeRuimte restaurant = new FakeRuimte(KamerType.RESTAURANT); // Vertakking 2: ruimte != null, type != LOBBY
        fakeModel.setTestRuimte(restaurant);

        triggerMouseClick();

        assertEquals(0, fakePauseController.pauseCount);
    }

    @Test
    void handleClick_wanneerRuimteLobbyIs_pauzeertSimulatie() throws Exception {
        FakeRuimte lobby = new FakeRuimte(KamerType.LOBBY); // Vertakking 3: ruimte != null, type == LOBBY
        fakeModel.setTestRuimte(lobby);

        triggerMouseClick();

        // Omdat de controller SwingUtilities.invokeLater() gebruikt, wachten we tot
        // de AWT Event Queue leeg is alvorens we de controle uitvoeren.
        SwingUtilities.invokeAndWait(() -> {});

        assertEquals(1, fakePauseController.pauseCount, "De simulatie had gepauzeerd moeten worden!");
    }

    @Test
    void testGettersEnUpdateView() {
        assertEquals(fakeModel, controller.getModel());
        assertEquals(fakeView, controller.getGridView());

        // We resetten de vlag expliciet naar false om eventuele repaints
        // vanuit de Swing/initialisatiefase te negeren.
        fakeView.repainted = false;

        assertFalse(fakeView.repainted, "Vlag moet false zijn voor de updateView aanroep.");

        controller.updateView();

        assertTrue(fakeView.repainted, "Repaint is niet correct aangeroepen op de view.");
    }
}