import Controller.Layout.LayoutController;
import Controller.PersoonManagement.EntiteitenController;
import Controller.PersoonManagement.GastController;
import Controller.PersoonManagement.Interfaces.NewGast;
import Model.Entiteiten.GastModel;
import Model.Entiteiten.TypePersoon;
import Model.Layout.LayoutModel;
import Model.Layout.Locatie;

import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.*;

class TestGastController {

    @Test
    void GastVerwijderen() throws Exception {

        GastController controller = new GastController();

        //startlocatie aanmaken
        Locatie startLocatie = new Locatie(0, 0);

        // startlocatie instellen
        Field startField = GastController.class.getDeclaredField("startLocatie");
        startField.setAccessible(true);
        startField.set(controller, startLocatie);

        // gast maken
        GastModel gast = new GastModel(
                1,
                startLocatie,
                null,
                TypePersoon.GAST,
                null,
                null
        );

        // gastmap
        Field gastenField = GastController.class.getDeclaredField("gasten");
        gastenField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<Integer, GastModel> gasten =
                (Map<Integer, GastModel>) gastenField.get(controller);

        // gast toevoegen
        gasten.put(gast.getID(), gast);

        // controle vooraf
        assertTrue(gasten.containsKey(gast.getID()));

        // branch uitvoeren
        controller.onDestinationReached(gast);

        // gast moet verwijderd zijn
        assertFalse(gasten.containsKey(gast.getID()));
    }


    private static class TestLayoutController extends LayoutController {
        public TestLayoutController(LayoutModel model) {
            super(model, null, null);
        }

        @Override
        protected void init() {
            // doet niets zodat het niet echt helemaal moet worden opgebouwd
        }
    }

    private static class TestGastListener implements NewGast {
        boolean aangekomenAangeroepen = false;
        Locatie ontvangenLocatie;

        @Override
        public void onGastAangekomenInKamer(GastModel gast, Locatie behaaldeLocatie) {
            aangekomenAangeroepen = true;
            ontvangenLocatie = behaaldeLocatie;
        }

        @Override public void onGastAangemaakt(GastModel gast) {}
        @Override public void onGastVertrokken(GastModel gast) {}
        @Override public void onGastVerplaatst(GastModel gast, Locatie oudeLocatie) {}
        @Override public void onGastGaatWegUitKamer(GastModel gast, Locatie oudeLocatie) {}
    }

    @Test
    void gastJuistelocatie () throws Exception {
        GastController controller = new GastController();

        LayoutModel layoutModel = new LayoutModel();

        layoutModel.addRuimte(new RuimteModel(
                KamerType.RESTAURANT,
                new Locatie(1, 1),
                "1,1"
        ));

        LayoutController layoutController = new TestLayoutController(layoutModel);

        Field layoutField = EntiteitenController.class.getDeclaredField("layoutController");
        layoutField.setAccessible(true);
        layoutField.set(controller, layoutController);
        //geef gast een locatie
        Locatie gastLocatie = new Locatie(1, 0);

        GastModel gast = new GastModel(
                1,
                gastLocatie,
                null,
                TypePersoon.GAST,
                null,
                null
        );

        TestGastListener listener = new TestGastListener();
        controller.setNewGuestListener(listener);

        controller.onDestinationReached(gast);

        assertTrue(listener.aangekomenAangeroepen);
        assertEquals(gastLocatie, listener.ontvangenLocatie);
    }

}

