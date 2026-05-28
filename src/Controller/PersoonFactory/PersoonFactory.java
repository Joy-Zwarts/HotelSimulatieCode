package Controller.PersoonFactory;

import Model.Layout.Locatie;
import Model.Personen.PersoonModel;
import Model.Personen.TypePersoon;

public abstract class PersoonFactory {

    // factory voor alle personen
    public abstract PersoonModel createPersoon(int gastId, Locatie locatie,Locatie targetLocatie, int wensen, Locatie stationLocatie, TypePersoon typePersoon);
}
