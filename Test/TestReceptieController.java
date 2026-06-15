import Controller.PersoonManagement.ReceptieController;
import Model.Entiteiten.GastModel;
import Model.Entiteiten.TypePersoon;
import Model.Layout.Locatie;
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
    private OverzichtView mockView;

    @BeforeEach
    public void setUp() {
        mockView = new OverzichtView(new HotelSimulatieView(null), null, new HotelEventManager());

        receptieController = new ReceptieController(mockView);
        testKamer = new KamerModel(KamerType.ROOM, new Locatie(1, 1), "1,1", KamerClassificatie.eenSter, true);
        testKamer.setRoomNumber(1);
    }

    @Test
    public void SetKamerLeegEnVol() {
        receptieController.setKamerLeeg(null);
        receptieController.setKamerVol(null);

        receptieController.setKamerVol(testKamer);
        Assertions.assertTrue(testKamer.isBezet());

        receptieController.setKamerLeeg(testKamer);
        Assertions.assertFalse(testKamer.isBezet());
        Assertions.assertNull(testKamer.getVerblijvende());
    }

    @Test
    public void OnNewRoom() {
        receptieController.onNewKamer(testKamer);

        Assertions.assertEquals(1, receptieController.getKamers().size());
        Assertions.assertTrue(receptieController.getKamers().containsKey(1));
        Assertions.assertFalse(testKamer.isBezet(), "Kamer moet initieel leeg zijn na onNewRoom");
    }

    @Test
    public void OnGastAangemaakt() {
        GastModel gast = new GastModel(1, null, null, TypePersoon.GAST, KamerClassificatie.eenSter, null);
        receptieController.onGastAangemaakt(gast);

        Assertions.assertEquals(gast, receptieController.getGast(1));
    }

    @Test
    public void OnGastVertrokken() {
        // Setup: Gast met kamer
        GastModel gast = new GastModel(1, null, null, TypePersoon.GAST, KamerClassificatie.eenSter, null);
        gast.setKamer(testKamer);
        testKamer.setBezet(true);
        receptieController.addGast(gast);

        receptieController.onGastVertrokken(gast);

        Assertions.assertNull(receptieController.getGast(1), "Gast moet verwijderd zijn uit de lijst");
        Assertions.assertFalse(testKamer.isBezet(), "Kamer moet vrijgegeven zijn");
        Assertions.assertNull(gast.getKamer(), "Gast mag geen referentie meer hebben naar kamer");
    }

    @Test
    public void OnGastVertrokkenNullChecks() {
        receptieController.onGastVertrokken(null);

        GastModel gastZonderKamer = new GastModel(2, null, null, TypePersoon.GAST, KamerClassificatie.tweeSterren, null);
        receptieController.addGast(gastZonderKamer);

        receptieController.onGastVertrokken(gastZonderKamer);

        Assertions.assertNull(receptieController.getGast(2));
    }

    @Test
    public void EmptyInterfaceMethods() {
        GastModel gast = new GastModel(1, null, null, TypePersoon.GAST, KamerClassificatie.eenSter, null);
        receptieController.onGastVerplaatst(gast, new Locatie(0,0));
        receptieController.onGastAangekomenInKamer(gast, new Locatie(0,0));
        receptieController.onGastGaatWegUitKamer(gast, new Locatie(0,0));
    }

    @Test
    public void GettersSettersAndRemove() {
        GastModel gast = new GastModel(5, null, null, TypePersoon.GAST, KamerClassificatie.vijfSterren, null);
        receptieController.addGast(gast);
        Assertions.assertEquals(gast, receptieController.getGast(5));

        receptieController.removeGast(5);
        Assertions.assertNull(receptieController.getGast(5));
        Assertions.assertNotNull(receptieController.getKamers());

        Assertions.assertNotNull(receptieController.getGasten());
    }


    @Test
    public void ResetSimulatieInclusiefNullKamer() {

        ReceptieController veiligeController = new ReceptieController(mockView) {
            @Override
            public void refreshView() {

            }
        };

        // 1. Voeg een normale kamer toe via de reguliere weg
        veiligeController.onNewKamer(testKamer);
        testKamer.setBezet(true);

        // 2. Voeg een gast toe die gereset moet worden
        GastModel gast = new GastModel(10, null, null, TypePersoon.GAST, KamerClassificatie.drieSterren, null);
        veiligeController.addGast(gast);

        // 3. Forceer handmatig de 'null' waarde in de kamers-map van deze controller.

        veiligeController.getKamers().put(999, null);

        // Voer de reset uit (roept de overschreven, veilige refreshView aan aan het eind)
        veiligeController.resetSimulatie();

        // Asserts om te controleren of de logica werkt
        Assertions.assertEquals(0, veiligeController.getGasten().size(), "Gastenlijst moet leeg zijn");
        Assertions.assertFalse(testKamer.isBezet(), "Bestaande kamers moeten leeggehaald zijn");
        Assertions.assertTrue(veiligeController.getKamers().containsKey(999), "De null-indringer moet er nog zijn");
    }
}