import Controller.PersoonManagement.GastController;
import Model.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Personen.TypePersoon;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
}
