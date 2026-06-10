package Controller.PersoonFactory;

import Model.Layout.Locatie;
import Model.Personen.EntiteitenModel;
import Model.Personen.PersoonModel;
import Model.Personen.TypePersoon;

public abstract class EntiteitenFactory {

    // factory voor alle personen
    public abstract EntiteitenModel createEntiteit(int gastId, Locatie locatie, Locatie targetLocatie, int wensen, Locatie stationLocatie, TypePersoon typePersoon);
}
