package Controller.PersoonFactory;


import Model.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Personen.PersoonModel;
import Model.Ruimtes.KamerClassificatie;

public class GastCreator extends PersoonFactory {
    private KamerClassificatie wensen;

    // maakt gast aan gebaseerd op de meegekregen data
    @Override
    public PersoonModel createPersoon(int gastId, Locatie targetlocatie, Locatie locatie, int Wensen) {
        switch (Wensen) {
            case 1:
                wensen = KamerClassificatie.eenSter;
                break;
            case 2:
                wensen = KamerClassificatie.tweeSterren;
                break;
            case 3:
                wensen = KamerClassificatie.drieSterren;
                break;
            case 4:
                wensen = KamerClassificatie.vierSterren;
                break;
            case 5:
                wensen = KamerClassificatie.vijfSterren;
                break;
        }

        // return de nieuwe gast
        return new GastModel(gastId, locatie, targetlocatie, wensen, null);
    }
}
