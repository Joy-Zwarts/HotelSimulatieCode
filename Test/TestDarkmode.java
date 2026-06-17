import Controller.Systeem.DarkModeController;
import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.TimeManagementPanel;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class TestDarkMode {

    @Test
    void testToggleDarkModeChangesModelAndUI() throws Exception {

        SwingUtilities.invokeAndWait(() -> {

            // Begin set up
            DarkModeModel model = new DarkModeModel();
            JPanel dummyPanel = new JPanel();
            TimeManagementPanel timePanel = new TimeManagementPanel(dummyPanel, model);
            HotelSimulatieView view = new HotelSimulatieView(model);

            DarkModeController controller = new DarkModeController(view, timePanel, model);

            // begin state
            boolean initialState = model.isDarkMode();
            Color initialBg = UIManager.getColor("Panel.background");

            // actie
            controller.toggleDarkMode();

            // assert model veranderd
            assertNotEquals(initialState, model.isDarkMode(),
                    "Dark mode moet toggelen");

            // assert UIManager aangepast
            Color newBg = UIManager.getColor("Panel.background");
            assertNotEquals(initialBg, newBg,
                    "Background kleur moet veranderen bij dark mode");

            // check of buttons nog bestaan (indirect bewijs dat setTimeButtons() werken)
            assertNotNull(timePanel.getNormaleTijd().getIcon(),
                    "Normale tijd knop moet een icon hebben");

            assertNotNull(timePanel.getFastForwardTijd().getIcon(),
                    "Fast forward knop moet een icon hebben");

            assertNotNull(timePanel.getDoubleFastForwardTijd().getIcon(),
                    "Double fast forward knop moet een icon hebben");

            // cleanup (belangrijk bij Swing tests)
            view.dispose();
        });
    }
}