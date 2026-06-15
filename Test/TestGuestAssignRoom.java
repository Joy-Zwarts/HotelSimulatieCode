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
        ReceptieController controller = new ReceptieController(null) {
            HashMap<Integer, KamerModel> kamers = new HashMap<>();

            {
                kamers.put(1, new KamerModel(KamerType.ROOM, new Locatie(5,5), "1,1", KamerClassificatie.vierSterren, false));
            }

            @Override
            public HashMap<Integer, KamerModel> getKamers() {
                return this.kamers;
            }

            @Override
            public void refreshView() {
            }

            @Override
            public void setKamerVol(KamerModel kamer) {
            }
        };

        KamerAssign roomAssign = new KamerAssign(controller);

        // gast die een 4 sterren kamer wil
        GastModel gast = new GastModel(
                1,
                new Locatie(0,0),
                new Locatie(0,0),
                TypePersoon.GAST,
                vierSterren,
                null
        );

        if (gast.getTargetLocatie() == null) {
            gast.setTargetLocatie(new Locatie(0,0));
        }

        roomAssign.assignKamer(gast);

        assertNotNull(gast.getKamer(), "De gast had een kamer moeten krijgen");
        assertEquals(vierSterren, gast.getKamer().getClassification());
    }
}