import Model.Systeem.DarkModeModel;
import View.Systeem.TimeManagementPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class TestTimeManagementPanel {

    private JPanel panelRechts;
    private TimeManagementPanel panel;

    // nep darkmodemodel zodat die gebruikt kan worden omdat darkmode in de panels staan
    static class FakeDarkModeModel extends DarkModeModel {
        private final boolean darkMode;

        public FakeDarkModeModel(boolean darkMode) {
            this.darkMode = darkMode;
        }

        @Override
        public boolean isDarkMode() {
            return darkMode;
        }
    }

    @BeforeEach
    void setUp() {
        panelRechts = new JPanel();
        panel = new TimeManagementPanel(panelRechts, new FakeDarkModeModel(false));
    }

    @Test
    void testPanelAanmaken() {
        assertNotNull(panel.getTimeManagementPanel());
    }

    @Test
    void BestaandeKnoppen() {
        assertNotNull(panel.getNormaleTijd());
        assertNotNull(panel.getFastForwardTijd());
        assertNotNull(panel.getDoubleFastForwardTijd());
    }

    @Test
    void KnoppenToegevoegd() {
        assertEquals(3, panel.getTimeManagementPanel().getComponentCount());
    }

    @Test
    void ButtonsLightMode() {
        panel = new TimeManagementPanel(panelRechts, new FakeDarkModeModel(false));

        panel.setTimeButtons();

        assertEquals(3, panel.getTimeManagementPanel().getComponentCount());
        assertNotNull(panel.getNormaleTijd().getIcon());
    }

    @Test
    void ButtonsDarkMode() {
        panel = new TimeManagementPanel(panelRechts, new FakeDarkModeModel(true));

        panel.setTimeButtons();

        assertEquals(3, panel.getTimeManagementPanel().getComponentCount());
        assertNotNull(panel.getFastForwardTijd().getIcon());
    }
}