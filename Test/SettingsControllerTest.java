import Controller.Systeem.DarkModeController;
import Controller.Systeem.SettingsController;
import Controller.Systeem.Interfaces.settingsListener;
import View.Systeem.TijdsDuur;
import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.TimeManagementPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsControllerTest {

    private SettingsController controller;

    private static class TestListener implements settingsListener {

        TijdsDuur schoonmaakTijd;
        TijdsDuur filmDuur;

        int schoonmakers;
        int restaurant;
        int trapLoop;
        int wachttijd;

        @Override
        public void schoonmaakTijdVeranderd(TijdsDuur duur) {
            schoonmaakTijd = duur;
        }

        @Override
        public void filmDuurVeranderd(TijdsDuur duur) {
            filmDuur = duur;
        }

        @Override
        public void showFactuurBonnen(boolean bool) {}


        @Override
        public void restaurantCapaciteitVeranderd(int waarde) {
            restaurant = waarde;
        }

        @Override
        public void trapLoopDuurVeranderd(int waarde) {
            trapLoop = waarde;
        }

        @Override
        public void gastMaxWachttijdVeranderd(int waarde) {
            wachttijd = waarde;
        }
    }

    @BeforeEach
    void setUp() {

        DarkModeModel model = new DarkModeModel();

        HotelSimulatieView view = new HotelSimulatieView(model);

        TimeManagementPanel panel =
                new TimeManagementPanel(view.getTopBar(), model);

        DarkModeController darkController =
                new DarkModeController(null, null, model);

        controller = new SettingsController(view, panel, darkController);
    }

    @Test
    void testAddListener() {

        TestListener listener = new TestListener();

        controller.addListener(listener);

        assertNotNull(listener);
    }

    @Test
    void testSchoonmaakKort() {

        TestListener listener = new TestListener();

        controller.addListener(listener);

        controller.getSettingsFrame().getSchoonmaakTijd().setSelectedItem("Kort");

        assertEquals(TijdsDuur.KORT, listener.schoonmaakTijd);
    }

    @Test
    void testSchoonmaakLang() {

        TestListener listener = new TestListener();

        controller.addListener(listener);

        controller.getSettingsFrame().getSchoonmaakTijd().setSelectedItem("Lang");

        assertEquals(TijdsDuur.LANG, listener.schoonmaakTijd);
    }

    @Test
    void testFilmKort() {

        TestListener listener = new TestListener();

        controller.addListener(listener);

        controller.getSettingsFrame().getFilmDuur().setSelectedItem("Kort");

        assertEquals(TijdsDuur.KORT, listener.filmDuur);
    }

    @Test
    void testFilmLang() {

        TestListener listener = new TestListener();

        controller.addListener(listener);

        controller.getSettingsFrame().getFilmDuur().setSelectedItem("Lang");

        assertEquals(TijdsDuur.LANG, listener.filmDuur);
    }

    @Test
    void testGastWachttijdSlider() {

        TestListener listener = new TestListener();

        controller.addListener(listener);

        controller.getSettingsFrame().getGastMaxWachttijd().setValue(15);

        assertEquals(15, listener.wachttijd);
    }
}