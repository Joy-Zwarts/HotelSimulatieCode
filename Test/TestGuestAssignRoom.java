import Controller.GastManagement.RoomAssign;
import Controller.GastManagement.ReceptieController;
import Controller.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.RoomClassificatie;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static Model.Ruimtes.RoomClassificatie.*;
import static org.junit.jupiter.api.Assertions.*;

class TestGuestAssignRoom {

    @Test
    void gastgetRoom() {
        // Fake controller met 1 kamer
        ReceptieController controller = new ReceptieController() {
            HashMap<Integer, KamerModel> kamers = new HashMap<>();

            {
                kamers.put(1, new KamerModel(RoomClassificatie,vierSterren, new Locatie(5,5)));
            }

            @Override
            public HashMap<Integer, KamerModel> getKamers() {
                return kamers;
            }

            @Override public void setKamerVol(KamerModel kamer) {}
            @Override public void setKamerLeeg(KamerModel kamer) {}
            @Override public void refreshView() {}
        };

        RoomAssign roomAssign = new RoomAssign(controller);

        // Gast die een 4 sterren kamer wil
        GastModel gast = new GastModel(
                1,
                new Locatie(0,0),
                new Locatie(0,0),
                vierSterren,
                null
        );

        // Act
        roomAssign.assignRoom(gast);

        // Assert
        assertNotNull(gast.getKamer());
        assertEquals(vierSterren, gast.getKamer().getClassification());
    }
}