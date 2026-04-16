import Controller.GastManagement.RoomAssign;
import Model.Personen.GastModel;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.RoomClassificatie;
import Controller.GastManagement.ReceptieController;

import View.Systeem.OverzichtView;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TestAssignRoom {

    class DummyController extends ReceptieController {
        HashMap<Integer, KamerModel> kamers = new HashMap<>();
        HashMap<Integer, GastModel> gasten = new HashMap<>();

        public DummyController(OverzichtView view) {
            super(view);
        }

        @Override
        public HashMap<Integer, KamerModel> getKamers() {
            return kamers;
        }

        @Override
        public GastModel getGast(int id) {
            return gasten.get(id);
        }

        @Override
        public void setKamerVol(KamerModel kamer) {}

        @Override
        public void setKamerLeeg(KamerModel kamer) {}

        @Override
        public void refreshView() {}
    }

    @Test
    void testAssignRoom_Success() {
        DummyController controller = new DummyController(null);
        RoomAssign roomAssign = new RoomAssign(controller);

        GastModel gast = new GastModel(1, null, null, null, null);
        gast.setWensen(RoomClassificatie.eenSter);

        KamerModel kamer = new KamerModel(null,null, null, null, false);
        kamer.setClassification(RoomClassificatie.eenSter);
        kamer.setBezet(false);
        kamer.setPosition(0,0);
        kamer.setPositionX(1);
        kamer.setPositionY(2);
        kamer.setPositionX(1);
        kamer.setPositionY(2);

        controller.kamers.put(1, kamer);

        roomAssign.assignRoom(gast);

        assertEquals(kamer, gast.getKamer());
        assertTrue(kamer.isBezet());
        assertEquals(gast, kamer.getVerblijvende());
        assertEquals("1, 2", gast.getLocatie());
    }

    @Test
    void testAssignRoom_GeenKamerBeschikbaar() {
        DummyController controller = new DummyController(null);
        RoomAssign roomAssign = new RoomAssign(controller);

        GastModel gast = new GastModel(1, null, null, null, null);
        gast.setWensen(RoomClassificatie.vijfSterren);

        KamerModel kamer = new KamerModel(null,null, null, null, false);
        kamer.setClassification(RoomClassificatie.eenSter);
        kamer.setBezet(false);

        controller.kamers.put(1, kamer);

        roomAssign.assignRoom(gast);

        assertNull(gast.getKamer());
    }

    @Test
    void testAssignRoom_GastZonderWensen() {
        DummyController controller = new DummyController(null);
        RoomAssign roomAssign = new RoomAssign(controller);

        GastModel gast = new GastModel(1, null, null, null, null);
        gast.setWensen(null);

        roomAssign.assignRoom(gast);

        assertNull(gast.getKamer());
    }

    @Test
    void testOnGastVertrokken() {
        DummyController controller = new DummyController(null);
        RoomAssign roomAssign = new RoomAssign(controller);

        GastModel gast = new GastModel(1, null, null, null, null);
        gast.setGastID(1);

        KamerModel kamer = new KamerModel(null, null, null, null, false);
        kamer.setBezet(true);
        kamer.setVerblijvende(gast);

        gast.setKamer(kamer);
        controller.gasten.put(1, gast);

        roomAssign.onGastVertrokken(1);

        assertFalse(kamer.isBezet());
        assertNull(kamer.getVerblijvende());
    }

    @Test
    void testOnGastVertrokken_GeenGast() {
        DummyController controller = new DummyController(null);
        RoomAssign roomAssign = new RoomAssign(controller);

        roomAssign.onGastVertrokken(99);

        // geen crash = succes
        assertTrue(true);
    }
}