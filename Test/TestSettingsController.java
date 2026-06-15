
import Controller.Systeem.DarkModeController;
import Controller.Systeem.Interfaces.settingsListener;
import Controller.Systeem.SettingsController;
import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.SettingsView;
import View.Systeem.TijdsDuur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;

public class TestSettingsController {

    private HotelSimulatieView fakeHotelView;
    private FakeSettingsView fakeSettingsView;
    private FakeDarkModeController fakeDarkModeController;
    private SettingsController settingsController;
    private MockSettingsListener mockListener;

    static class FakeDarkModeController extends DarkModeController {
        boolean toggleDarkModeAangeroepen = false;

        public FakeDarkModeController() {
            super(null, null, new DarkModeModel());
        }

        @Override
        public void toggleDarkMode() {
            this.toggleDarkModeAangeroepen = true;
        }
    }

    static class FakeSettingsView extends SettingsView {
        private final JButton darkModeButton = new JButton();
        private final JComboBox<String> schoonmaakTijd = new JComboBox<>(new String[]{"Normaal", "Lang", "Kort", "Onbekend"});
        private final JComboBox<String> filmDuur = new JComboBox<>(new String[]{"Normaal", "Lang", "Kort", "Onbekend"});
        private final JSlider aantalSchoonmakers = new JSlider();
        private final JSlider restaurantCapaciteit = new JSlider();
        private final JSlider trapLoopDuur = new JSlider();
        private final JSlider gastMaxWachttijd = new JSlider();

        public FakeSettingsView(HotelSimulatieView view) {
            super(view);
        }

        @Override public JButton getDarkModeButton() { return darkModeButton; }
        @Override public JComboBox<String> getSchoonmaakTijd() { return schoonmaakTijd; }
        @Override public JComboBox<String> getFilmDuur() { return filmDuur; }
        @Override public JSlider getAantalSchoonmakers() { return aantalSchoonmakers; }
        @Override public JSlider getRestaurantCapaciteit() { return restaurantCapaciteit; }
        @Override public JSlider getTrapLoopDuur() { return trapLoopDuur; }
        @Override public JSlider getGastMaxWachttijd() { return gastMaxWachttijd; }
    }

    static class MockSettingsListener implements settingsListener {
        TijdsDuur lastSchoonmaakTijd;
        TijdsDuur lastFilmDuur;
        int lastAantalSchoonmakers = -1;
        int lastRestaurantCapaciteit = -1;
        int lastTrapLoopDuur = -1;
        int lastGastMaxWachttijd = -1;

        @Override public void schoonmaakTijdVeranderd(TijdsDuur duur) { this.lastSchoonmaakTijd = duur; }
        @Override public void filmDuurVeranderd(TijdsDuur duur) { this.lastFilmDuur = duur; }
        @Override public void aantalSchoonmakersVeranderd(int waarde) { this.lastAantalSchoonmakers = waarde; }
        @Override public void restaurantCapaciteitVeranderd(int waarde) { this.lastRestaurantCapaciteit = waarde; }
        @Override public void trapLoopDuurVeranderd(int waarde) { this.lastTrapLoopDuur = waarde; }
        @Override public void gastMaxWachttijdVeranderd(int waarde) { this.lastGastMaxWachttijd = waarde; }
    }

    @BeforeEach
    void setUp() throws Exception {
        fakeHotelView = new HotelSimulatieView(new DarkModeModel());
        fakeSettingsView = new FakeSettingsView(fakeHotelView);
        fakeDarkModeController = new FakeDarkModeController();

        settingsController = new SettingsController(fakeHotelView, null, fakeDarkModeController) {
            @Override
            public SettingsView getSettingsFrame() {
                return fakeSettingsView;
            }
        };

        java.lang.reflect.Field field = SettingsController.class.getDeclaredField("settingsFrame");
        field.setAccessible(true);
        field.set(settingsController, fakeSettingsView);

        mockListener = new MockSettingsListener();
        settingsController.addListener(mockListener);
    }


    @Test
    void testActionPerformedDarkModeButton() {
        ActionEvent event = new ActionEvent(fakeSettingsView.getDarkModeButton(), ActionEvent.ACTION_PERFORMED, "");
        settingsController.actionPerformed(event);
        assertTrue(fakeDarkModeController.toggleDarkModeAangeroepen);
    }

    @Test
    void testActionPerformedSchoonmaakTijd() {
        // case: normaal
        fakeSettingsView.getSchoonmaakTijd().setSelectedItem("Normaal");
        settingsController.actionPerformed(new ActionEvent(fakeSettingsView.getSchoonmaakTijd(), ActionEvent.ACTION_PERFORMED, ""));
        assertEquals(TijdsDuur.NORMAAL, mockListener.lastSchoonmaakTijd);

        // case: lang
        fakeSettingsView.getSchoonmaakTijd().setSelectedItem("Lang");
        settingsController.actionPerformed(new ActionEvent(fakeSettingsView.getSchoonmaakTijd(), ActionEvent.ACTION_PERFORMED, ""));
        assertEquals(TijdsDuur.LANG, mockListener.lastSchoonmaakTijd);

        // case: kort
        fakeSettingsView.getSchoonmaakTijd().setSelectedItem("Kort");
        settingsController.actionPerformed(new ActionEvent(fakeSettingsView.getSchoonmaakTijd(), ActionEvent.ACTION_PERFORMED, ""));
        assertEquals(TijdsDuur.KORT, mockListener.lastSchoonmaakTijd);

        // case: default/null
        fakeSettingsView.getSchoonmaakTijd().setSelectedItem("Onbekend");
        settingsController.actionPerformed(new ActionEvent(fakeSettingsView.getSchoonmaakTijd(), ActionEvent.ACTION_PERFORMED, ""));
        assertEquals(TijdsDuur.NORMAAL, mockListener.lastSchoonmaakTijd);
    }

    @Test
    void testActionPerformedFilmDuur() {
        // case: normaal
        fakeSettingsView.getFilmDuur().setSelectedItem("Normaal");
        settingsController.actionPerformed(new ActionEvent(fakeSettingsView.getFilmDuur(), ActionEvent.ACTION_PERFORMED, ""));
        assertEquals(TijdsDuur.NORMAAL, mockListener.lastFilmDuur);

        // case: lang
        fakeSettingsView.getFilmDuur().setSelectedItem("Lang");
        settingsController.actionPerformed(new ActionEvent(fakeSettingsView.getFilmDuur(), ActionEvent.ACTION_PERFORMED, ""));
        assertEquals(TijdsDuur.LANG, mockListener.lastFilmDuur);

        // case: kort
        fakeSettingsView.getFilmDuur().setSelectedItem("Kort");
        settingsController.actionPerformed(new ActionEvent(fakeSettingsView.getFilmDuur(), ActionEvent.ACTION_PERFORMED, ""));
        assertEquals(TijdsDuur.KORT, mockListener.lastFilmDuur);

        // case: default/null
        fakeSettingsView.getFilmDuur().setSelectedItem("Onbekend");
        settingsController.actionPerformed(new ActionEvent(fakeSettingsView.getFilmDuur(), ActionEvent.ACTION_PERFORMED, ""));
        assertEquals(TijdsDuur.NORMAAL, mockListener.lastFilmDuur);
    }


    @Test
    void testStateChangedSlider() {
        JSlider slider = fakeSettingsView.getAantalSchoonmakers();
        slider.setValue(45);
        slider.setValueIsAdjusting(true); // gebruiker houdt de slider nog vast

        settingsController.stateChanged(new ChangeEvent(slider));

        assertEquals(-1, mockListener.lastAantalSchoonmakers);
    }

    @Test
    void testStateChangedAantalSchoonmakers() {
        JSlider slider = fakeSettingsView.getAantalSchoonmakers();
        slider.setValue(12);
        slider.setValueIsAdjusting(false);

        settingsController.stateChanged(new ChangeEvent(slider));
        assertEquals(12, mockListener.lastAantalSchoonmakers);
    }

    @Test
    void testStateChangedRestaurantCapaciteit() {
        JSlider slider = fakeSettingsView.getRestaurantCapaciteit();
        slider.setValue(50);
        slider.setValueIsAdjusting(false);

        settingsController.stateChanged(new ChangeEvent(slider));
        assertEquals(50, mockListener.lastRestaurantCapaciteit);
    }

    @Test
    void testStateChangedTrapLoopDuur() {
        JSlider slider = fakeSettingsView.getTrapLoopDuur();
        slider.setValue(8);
        slider.setValueIsAdjusting(false);

        settingsController.stateChanged(new ChangeEvent(slider));
        assertEquals(8, mockListener.lastTrapLoopDuur);
    }

    @Test
    void testStateChangedGastMaxWachttijd() {
        JSlider slider = fakeSettingsView.getGastMaxWachttijd();
        slider.setValue(30);
        slider.setValueIsAdjusting(false);

        settingsController.stateChanged(new ChangeEvent(slider));
        assertEquals(30, mockListener.lastGastMaxWachttijd);
    }

    @Test
    void testStateChangedOnbekend() {
        JSlider onbekendeSlider = new JSlider();
        onbekendeSlider.setValue(99);
        onbekendeSlider.setValueIsAdjusting(false);

        assertDoesNotThrow(() -> settingsController.stateChanged(new ChangeEvent(onbekendeSlider)));
        assertEquals(-1, mockListener.lastAantalSchoonmakers);
    }

    @Test
    void testGetSettingsFrame() {
        assertNotNull(settingsController.getSettingsFrame());
    }
}