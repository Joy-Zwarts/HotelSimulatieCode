import Controller.PersoonManagement.KamerAssign;
import Controller.PersoonManagement.ReceptieController;
import Controller.SimulatieController;
import Model.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Personen.TypePersoon;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static Model.Ruimtes.KamerClassificatie.*;
import static org.junit.jupiter.api.Assertions.*;

class TestKamerAssign {

    private HashMap<Integer, KamerModel> kamerLijst;
    private KamerAssign kamerAssign;

    @BeforeEach
    void setUp() {
        kamerLijst = new HashMap<>();

        ReceptieController controller = new ReceptieController(null) {
            @Override
            public HashMap<Integer, KamerModel> getKamers() {
                return kamerLijst;
            }

            @Override
            public void setKamerVol(KamerModel k) {
                k.setBezet(true);
            }

            @Override
            public void setKamerLeeg(KamerModel k) {
                k.setBezet(false);
            }

            @Override
            public void refreshView() {
            }
        };

        kamerAssign = new KamerAssign(controller, new SimulatieController());
    }

    @Test
    void testAssignKamerDirectMatch() {
        KamerModel kamer = new KamerModel(KamerType.ROOM, new Locatie(5,5), "1,1", vierSterren, false);
        kamerLijst.put(1, kamer);

        GastModel gast = new GastModel(1, new Locatie(0,0), new Locatie(0,0), TypePersoon.GAST, vierSterren, null);

        kamerAssign.assignKamer(gast);

        assertNotNull(gast.getKamer());
        assertEquals(vierSterren, gast.getKamer().getClassification());
        assertTrue(kamer.isBezet());
        assertEquals(gast, kamer.getVerblijvende());
        assertEquals(4, gast.getTargetLocatie().getY());
    }

    @Test
    void testAssignKamerDowngradeTraject() {
        // alleen een 1-ster kamer beschikbaar
        KamerModel kamer = new KamerModel(KamerType.ROOM, new Locatie(10,10), "1,1", eenSter, false);
        kamerLijst.put(1, kamer);

        // gast wil 5 sterren
        GastModel gast = new GastModel(1, new Locatie(0,0), new Locatie(0,0),TypePersoon.GAST, vijfSterren, null);

        kamerAssign.assignKamer(gast);

        // de gast moet na 4 downgrades in de 1-ster kamer zitten
        assertNotNull(gast.getKamer());
        assertEquals(eenSter, gast.getKamer().getClassification());
    }

    @Test
    void testAssignKamerGeenBeschikbaarheid() {
        // geen kamers in de lijst
        GastModel gast = new GastModel(1, new Locatie(0,0), new Locatie(0,0),TypePersoon.GAST, eenSter, null);

        kamerAssign.assignKamer(gast);

        assertNull(gast.getKamer());
    }

    @Test
    void testAssignKamerBezetSlaatOver() {
        KamerModel bezetteKamer = new KamerModel(KamerType.ROOM, new Locatie(5,5), "1,1", tweeSterren, true);
        kamerLijst.put(1, bezetteKamer);

        GastModel gast = new GastModel(1, new Locatie(0,0), new Locatie(0,0),TypePersoon.GAST, tweeSterren, null);

        kamerAssign.assignKamer(gast);

        assertNull(gast.getKamer(), "Gast mag geen bezette kamer krijgen");
    }

    @Test
    void testNullGuards() {
        assertDoesNotThrow(() -> kamerAssign.assignKamer(null));

        // gast heeft geen wensen
        GastModel gastZonderWensen = new GastModel(1, new Locatie(0,0), new Locatie(0,0),TypePersoon.GAST, null, null);
        assertDoesNotThrow(() -> kamerAssign.assignKamer(gastZonderWensen));
        assertNull(gastZonderWensen.getKamer());
    }

    @Test
    void testOnGastAangemaakt() {
        KamerModel kamer = new KamerModel(KamerType.ROOM, new Locatie(5,5), "1,1", eenSter, false);
        kamerLijst.put(1, kamer);
        GastModel gast = new GastModel(1, new Locatie(0,0), new Locatie(0,0),TypePersoon.GAST, eenSter, null);

        kamerAssign.onGastAangemaakt(gast);

        assertNotNull(gast.getKamer());
    }

    @Test
    void testOnGastVertrokken() {
        KamerModel kamer = new KamerModel(KamerType.ROOM, new Locatie(5,5), "1,1", eenSter, true);
        GastModel gast = new GastModel(1, new Locatie(0,0), new Locatie(0,0),TypePersoon.GAST,eenSter, null);
        gast.setKamer(kamer);
        kamer.setVerblijvende(gast);

        kamerAssign.onGastVertrokken(gast);

        assertFalse(kamer.isBezet());
        assertNull(kamer.getVerblijvende());

        assertDoesNotThrow(() -> kamerAssign.onGastVertrokken(null));
        assertDoesNotThrow(() -> kamerAssign.onGastVertrokken(new GastModel(2, null, null,TypePersoon.GAST, null, null)));
    }

    @Test
    void testLegeMethodesVoorCoverage() {
        assertDoesNotThrow(() -> kamerAssign.onGastVerplaatst(null, null));
        assertDoesNotThrow(() -> kamerAssign.onGastAangekomenInKamer(null, null));
        assertDoesNotThrow(() -> kamerAssign.onGastGaatWegUitKamer(null, null));
    }
}