package Controller.PersoonFactory;

import Model.Layout.Locatie;
import Model.Personen.PersoonModel;
import Model.Personen.SchoonmakerModel;

// maakt schoonmaker aan gebaseerd op de meegekregen data
public class SchoonmakerCreator extends PersoonFactory {
    @Override
    public PersoonModel createPersoon(int gastId, Locatie locatie, Locatie targetLocatie, int wensen) {
        return new SchoonmakerModel(locatie, targetLocatie);
    }
}
