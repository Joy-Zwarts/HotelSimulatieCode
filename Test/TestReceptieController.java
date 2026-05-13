import Controller.PersoonManagement.ReceptieController;
import Model.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Ruimtes.KamerClassificatie;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerType;
import View.Systeem.HotelSimulatieView;
import View.Systeem.OverzichtView;
import hotelevents.HotelEventManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestReceptieController {

    private ReceptieController receptieController;
    private KamerModel testKamer;

    @BeforeEach
    public void setUp() {
        OverzichtView mockView = new OverzichtView(new HotelSimulatieView(null), null, new HotelEventManager());

        receptieController = new ReceptieController(mockView);
        testKamer = new KamerModel(KamerType.ROOM, new Locatie(1, 1), "1,1", KamerClassificatie.eenSter, true);
        testKamer.setRoomNumber(1);
    }

    @Test
    public void testSetKamerLeegEnVol() {
        receptieController.setKamerLeeg(null);
        receptieController.setKamerVol(null);

        receptieController.setKamerVol(testKamer);
        Assertions.assertTrue(testKamer.isBezet());

        receptieController.setKamerLeeg(testKamer);
        Assertions.assertFalse(testKamer.isBezet());
        Assertions.assertNull(testKamer.getVerblijvende());
    }

    @Test
    public void testOnNewRoom() {
        receptieController.onNewKamer(testKamer);

        Assertions.assertEquals(1, receptieController.getKamers().size());
        Assertions.assertTrue(receptieController.getKamers().containsKey(1));
        Assertions.assertFalse(testKamer.isBezet(), "Kamer moet initieel leeg zijn na onNewRoom");
    }

    @Test
    public void testOnGastAangemaakt() {
        GastModel gast = new GastModel(1, null, null, , KamerClassificatie.eenSter, null);
        receptieController.onGastAangemaakt(gast);

        Assertions.assertEquals(gast, receptieController.getGast(1));
    }

    @Test
    public void testOnGastVertrokkenFullCoverage() {
        // Setup: Gast met kamer
        GastModel gast = new GastModel(1, null, null, , KamerClassificatie.eenSter, null);
        gast.setKamer(testKamer);
        testKamer.setBezet(true);
        receptieController.addGast(gast);

        receptieController.onGastVertrokken(gast);

        Assertions.assertNull(receptieController.getGast(1), "Gast moet verwijderd zijn uit de lijst");
        Assertions.assertFalse(testKamer.isBezet(), "Kamer moet vrijgegeven zijn");
        Assertions.assertNull(gast.getKamer(), "Gast mag geen referentie meer hebben naar kamer");
    }

    @Test
    public void testOnGastVertrokkenNullChecks() {
        receptieController.onGastVertrokken(null);

        GastModel gastZonderKamer = new GastModel(2, null, null, , KamerClassificatie.tweeSterren, null);
        receptieController.addGast(gastZonderKamer);
        receptieController.onGastVertrokken(gastZonderKamer);

        Assertions.assertNull(receptieController.getGast(2));
    }

    @Test
    public void testEmptyInterfaceMethods() {
        GastModel gast = new GastModel(1, null, null, , KamerClassificatie.eenSter, null);
        receptieController.onGastVerplaatst(gast, new Locatie(0,0));
        receptieController.onGastAangekomenInKamer(gast, new Locatie(0,0));
        receptieController.onGastGaatWegUitKamer(gast, new Locatie(0,0));
    }

    @Test
    public void testGettersSettersAndRemove() {
        GastModel gast = new GastModel(5, null, null, , KamerClassificatie.vijfSterren, null);
        receptieController.addGast(gast);
        Assertions.assertEquals(gast, receptieController.getGast(5));

        receptieController.removeGast(5);
        Assertions.assertNull(receptieController.getGast(5));
        Assertions.assertNotNull(receptieController.getKamers());
    }
}