package Controller.Systeem;

import Controller.Systeem.DarkModeController;
import Controller.Systeem.SettingsController;
import Controller.Systeem.Interfaces.settingsListener;
import View.Systeem.TijdsDuur;
import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.TijdsDuur;
import View.Systeem.TimeManagementPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsControllerTest {

    private SettingsController controller;

    // Dummy listener waarmee gecontroleerd wordt
    // of de juiste methodes worden aangeroepen.
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
        public void aantalSchoonmakersVeranderd(int waarde) {
            schoonmakers = waarde;
        }

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

    // Controleert dat de listener toegevoegd kan worden.
    @Test
    void testAddListener() {

        TestListener listener = new TestListener();

        controller.addListener(listener);

        assertNotNull(listener);
    }

    // Controleert of de schoonmaaktijd op Kort gezet kan worden.
    @Test
    void testSchoonmaakKort() {

        TestListener listener = new TestListener();

        controller.addListener(listener);

        controller.getSettingsFrame().getSchoonmaakTijd().setSelectedItem("Kort");

        assertEquals(TijdsDuur.KORT, listener.schoonmaakTijd);
    }

    // Controleert of de schoonmaaktijd op Lang gezet kan worden.
    @Test
    void testSchoonmaakLang() {

        TestListener listener = new TestListener();

        controller.addListener(listener);

        controller.getSettingsFrame().getSchoonmaakTijd().setSelectedItem("Lang");

        assertEquals(TijdsDuur.LANG, listener.schoonmaakTijd);
    }

    // Controleert of de filmduur op Kort gezet kan worden.
    @Test
    void testFilmKort() {

        TestListener listener = new TestListener();

        controller.addListener(listener);

        controller.getSettingsFrame().getFilmDuur().setSelectedItem("Kort");

        assertEquals(TijdsDuur.KORT, listener.filmDuur);
    }

    // Controleert of de filmduur op Lang gezet kan worden.
    @Test
    void testFilmLang() {

        TestListener listener = new TestListener();

        controller.addListener(listener);

        controller.getSettingsFrame().getFilmDuur().setSelectedItem("Lang");

        assertEquals(TijdsDuur.LANG, listener.filmDuur);
    }

    // Controleert of de slider voor schoonmakers werkt.
    @Test
    void testAantalSchoonmakersSlider() {

        TestListener listener = new TestListener();

        controller.addListener(listener);

        controller.getSettingsFrame().getAantalSchoonmakers().setValue(6);

        assertEquals(6, listener.schoonmakers);
    }

    // Controleert of de restaurantslider werkt.
    @Test
    void testRestaurantSlider() {

        TestListener listener = new TestListener();

        controller.addListener(listener);

        controller.getSettingsFrame().getRestaurantCapaciteit().setValue(20);

        assertEquals(20, listener.restaurant);
    }

    // Controleert of de traploopslider werkt.
    @Test
    void testTrapLoopSlider() {

        TestListener listener = new TestListener();

        controller.addListener(listener);

        controller.getSettingsFrame().getTrapLoopDuur().setValue(8);

        assertEquals(8, listener.trapLoop);
    }

    // Controleert of de wachttijdslider werkt.
    @Test
    void testGastWachttijdSlider() {

        TestListener listener = new TestListener();

        controller.addListener(listener);

        controller.getSettingsFrame().getGastMaxWachttijd().setValue(15);

        assertEquals(15, listener.wachttijd);
    }
}