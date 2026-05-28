package Controller.PersoonFactory;

import Model.Layout.Locatie;
import Model.Personen.PersoonModel;
import Model.Personen.SchoonmakerModel;
import Model.Personen.TypePersoon;

// maakt schoonmaker aan gebaseerd op de meegekregen data
public class SchoonmakerCreator extends PersoonFactory {
    @Override
    public PersoonModel createPersoon(int gastId, Locatie locatie, Locatie targetLocatie, int wensen, Locatie stationLocatie, TypePersoon typePersoon) {
        return new SchoonmakerModel(gastId, locatie, targetLocatie, typePersoon, stationLocatie);
    }
}
