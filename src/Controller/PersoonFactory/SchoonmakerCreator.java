package Controller.PersoonFactory;

import Model.Layout.Locatie;
import Model.Entiteiten.EntiteitenModel;
import Model.Entiteiten.SchoonmakerModel;
import Model.Entiteiten.TypePersoon;

// maakt schoonmaker aan gebaseerd op de meegekregen data
public class SchoonmakerCreator extends EntiteitenFactory {
    @Override
    public EntiteitenModel createEntiteit(int gastId, Locatie locatie, Locatie targetLocatie, int wensen, Locatie stationLocatie, TypePersoon typePersoon) {
        return new SchoonmakerModel(gastId, locatie, targetLocatie, typePersoon, stationLocatie);
    }
}
