package Controller.PersoonFactory;


import Model.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Personen.PersoonModel;
import Model.Personen.TypePersoon;
import Model.Ruimtes.KamerClassificatie;

public class GastCreator extends PersoonFactory {

    // maakt gast aan gebaseerd op de meegekregen data
    @Override
    public PersoonModel createPersoon(int gastId, Locatie locatie, Locatie targetLocatie, int wensen, Locatie stationLocatie, TypePersoon typePersoon) {

        KamerClassificatie wensEnum = switch (wensen) {
            case 1 -> KamerClassificatie.eenSter;
            case 2 -> KamerClassificatie.tweeSterren;
            case 3 -> KamerClassificatie.drieSterren;
            case 4 -> KamerClassificatie.vierSterren;
            case 5 -> KamerClassificatie.vijfSterren;
            default -> throw new IllegalStateException("Unexpected value: " + wensen);
        };

        return new GastModel(gastId, locatie, targetLocatie, typePersoon, wensEnum, null);
    }
}
