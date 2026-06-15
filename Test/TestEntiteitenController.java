
import Controller.Layout.LayoutController;
import Controller.PersoonManagement.BeweegHelper;
import Controller.PersoonManagement.EntiteitenController;
import Model.Layout.LayoutModel;
import Model.Layout.Locatie;
import Model.Entiteiten.EntiteitenModel;
import View.Layout.LayoutView;
import View.Systeem.HotelSimulatieView;
import View.Systeem.OverzichtView;
import View.Systeem.TijdsDuur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class TestEntiteitenController {

    private ConcreteEntiteitenController controller;
    private LayoutController fakeLayoutController;

    static class ConcreteEntiteitenController extends EntiteitenController {
        public ConcreteEntiteitenController() {
            super();
        }

        public void voegHandmatigEntiteitToe(int id, EntiteitenModel model) {
            this.actieveEntiteiten.put(id, model);
        }

        public int getActieveEntiteitenAantal() {
            return this.actieveEntiteiten.size();
        }

        public void forceerBeweegHelperNull() {
            try {
                Field field = EntiteitenController.class.getDeclaredField("beweegHelper");
                field.setAccessible(true);

                try {
                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
                } catch (Exception ignored) {}

                field.set(this, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStepTaken(EntiteitenModel entiteit, Locatie oudeLocatie) {
            assertNotNull(entiteit);
        }

        @Override
        public void onDestinationReached(EntiteitenModel entiteit) {
            assertNotNull(entiteit);
        }

        @Override public void schoonmaakTijdVeranderd(TijdsDuur tijdsDuur) {}
        @Override public void filmDuurVeranderd(TijdsDuur tijdsDuur) {}
        @Override public void aantalSchoonmakersVeranderd(int aantalSchoonmakers) {}
        @Override public void restaurantCapaciteitVeranderd(int restaurantCapaciteit) {}
        @Override public void gastMaxWachttijdVeranderd(int gastMaxWachttijd) {}
    }

    static class FakeOverzichtView extends OverzichtView {
        public FakeOverzichtView() { super(null, null, null); }
    }

    static class FakeEntiteit extends EntiteitenModel {
        public FakeEntiteit(int id) {
            super(id, new Locatie(0,0), new Locatie(0,0));
        }
    }

    @BeforeEach
    void setUp() {
        controller = new ConcreteEntiteitenController();

        LayoutModel model = new LayoutModel();
        LayoutView view = new LayoutView(null, null);
        HotelSimulatieView hoofdView = new HotelSimulatieView(null);
        fakeLayoutController = new LayoutController(model, view, hoofdView);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller.actieveEntiteiten);
        assertNotNull(controller.beweegHelper);

        controller.onLayoutGeladen(fakeLayoutController);

        try {
            Field f = EntiteitenController.class.getDeclaredField("layoutController");
            f.setAccessible(true);
            LayoutController opgeslagenController = (LayoutController) f.get(controller);
            assertEquals(fakeLayoutController, opgeslagenController);
        } catch (Exception e) {
            fail("Kon layoutController veld niet controleren");
        }
    }

    @Test
    void injecteerOverzichtViewTest() {
        FakeOverzichtView fakeOverzicht = new FakeOverzichtView();

        controller.injecteerOverzichtView(fakeOverzicht);

        try {
            Field f = BeweegHelper.class.getDeclaredField("overzichtView");
            f.setAccessible(true);
            OverzichtView geinjecteerdeView = (OverzichtView) f.get(controller.beweegHelper);
            assertEquals(fakeOverzicht, geinjecteerdeView);
        } catch (Exception e) {
            fail("Kon overzichtView niet controleren");
        }

        controller.forceerBeweegHelperNull();
        assertDoesNotThrow(() -> controller.injecteerOverzichtView(fakeOverzicht));
    }

    @Test
    void resetControllerTest() {
        controller.voegHandmatigEntiteitToe(1, new FakeEntiteit(1));
        assertEquals(1, controller.getActieveEntiteitenAantal());

        controller.resetController();
        assertEquals(0, controller.getActieveEntiteitenAantal());

        controller.voegHandmatigEntiteitToe(2, new FakeEntiteit(2));
        controller.forceerBeweegHelperNull();

        assertDoesNotThrow(() -> controller.resetController());
        assertEquals(0, controller.getActieveEntiteitenAantal());
    }

    @Test
    void testTrapLoopDuurVeranderd() {
        controller.trapLoopDuurVeranderd(5);

        try {
            Field f = BeweegHelper.class.getDeclaredField("trapVertragingTicks");
            f.setAccessible(true);
            int ticks = f.getInt(null);
            assertEquals(5, ticks);
        } catch (Exception e) {
            fail("Kon static trapVertragingTicks niet controleren");
        }
    }

    @Test
    void testListenerAanroepen() {
        FakeEntiteit testEntiteit = new FakeEntiteit(10);
        Locatie testLocatie = new Locatie(1, 1);

        assertDoesNotThrow(() -> controller.onStepTaken(testEntiteit, testLocatie));
        assertDoesNotThrow(() -> controller.onDestinationReached(testEntiteit));

        assertDoesNotThrow(() -> controller.schoonmaakTijdVeranderd(null));
        assertDoesNotThrow(() -> controller.filmDuurVeranderd(null));
        assertDoesNotThrow(() -> controller.aantalSchoonmakersVeranderd(10));
        assertDoesNotThrow(() -> controller.restaurantCapaciteitVeranderd(50));
        assertDoesNotThrow(() -> controller.gastMaxWachttijdVeranderd(100));
    }
}