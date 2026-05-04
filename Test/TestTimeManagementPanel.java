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
    class FakeDarkModeModel extends DarkModeModel {
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
    void testPanelWordtAangemaakt() {
        assertNotNull(panel.getTimeManagementPanel());
    }

    @Test
    void testKnoppenBestaan() {
        assertNotNull(panel.getNormaleTijd());
        assertNotNull(panel.getFastForwardTijd());
        assertNotNull(panel.getDoubleFastForwardTijd());
    }

    @Test
    void testKnoppenToegevoegdAanPanel() {
        assertEquals(3, panel.getTimeManagementPanel().getComponentCount());
    }

    @Test
    void testSetTimeButtonsLightMode() {
        panel = new TimeManagementPanel(panelRechts, new FakeDarkModeModel(false));

        panel.setTimeButtons();

        assertEquals(3, panel.getTimeManagementPanel().getComponentCount());
        assertNotNull(panel.getNormaleTijd().getIcon());
    }

    @Test
    void testSetTimeButtonsDarkMode() {
        panel = new TimeManagementPanel(panelRechts, new FakeDarkModeModel(true));

        panel.setTimeButtons();

        assertEquals(3, panel.getTimeManagementPanel().getComponentCount());
        assertNotNull(panel.getFastForwardTijd().getIcon());
    }
}