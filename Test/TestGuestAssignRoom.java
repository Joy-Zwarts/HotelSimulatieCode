import Controller.PersoonManagement.KamerAssign;
import Controller.PersoonManagement.ReceptieController;
import Model.Entiteiten.GastModel;
import Model.Entiteiten.TypePersoon;
import Model.Layout.Locatie;
import Model.Ruimtes.KamerClassificatie;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerType;
import org.junit.jupiter.api.Test;
import java.util.HashMap;

import static Model.Ruimtes.KamerClassificatie.*;
import static org.junit.jupiter.api.Assertions.*;

class TestGuestAssignRoom {

    @Test
    void gastgetRoom() {
        // Fake controller met 1 kamer - Jouw exacte anonieme klasse opzet
        ReceptieController controller = new ReceptieController(null) {
            HashMap<Integer, KamerModel> kamers = new HashMap<>();

            {
                // De kamer staat op 'false' (dus NIET bezet, oftewel vrij)
                // Let op: controleer in je KamerModel constructor of de laatste parameter 'isBezet' betekent.
                // Als jouw constructor 'isBezet' verwacht, moet dit false zijn. Als het 'isAvailable' verwacht, true.
                // Gezien je code (if (kamer.isBezet()) continue;), moet de kamer hier op FALSE staan om bruikbaar te zijn!
                kamers.put(1, new KamerModel(KamerType.ROOM, new Locatie(5,5), "1,1", KamerClassificatie.vierSterren, false));
            }

            @Override
            public HashMap<Integer, KamerModel> getKamers() {
                return this.kamers;
            }

            // Voorkom NullPointerException als KamerAssign deze aanroept
            @Override
            public void refreshView() {
                // Dummy implementatie, doet niks tijdens de test
            }

            @Override
            public void setKamerVol(KamerModel kamer) {
                // Dummy implementatie, doet niks tijdens de test
            }
        };

        KamerAssign roomAssign = new KamerAssign(controller);

        // Gast die een 4 sterren kamer wil
        GastModel gast = new GastModel(
                1,
                new Locatie(0,0),
                new Locatie(0,0), // Dit is de targetLocatie. Zorg dat deze intern niet null is.
                TypePersoon.GAST,
                vierSterren,
                null
        );

        // Dubbele check: Mocht je constructor de targetLocatie niet opslaan, zet hem hier handmatig recht via reflectie of setter:
        if (gast.getTargetLocatie() == null) {
            gast.setTargetLocatie(new Locatie(0,0)); // Als je een setter hebt
        }

        // Act
        roomAssign.assignKamer(gast);

        // Assert
        assertNotNull(gast.getKamer(), "De gast had een kamer moeten krijgen");
        assertEquals(vierSterren, gast.getKamer().getClassification());
    }
}