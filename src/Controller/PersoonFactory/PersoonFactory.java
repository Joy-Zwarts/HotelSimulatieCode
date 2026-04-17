package Controller.PersoonFactory;

import Controller.Layout.Locatie;
import Model.Personen.PersoonModel;

public abstract class PersoonFactory {

    // factory voor alle personen
    public abstract PersoonModel createPersoon(int gastId, Locatie locatie,Locatie targetLocatie, int wensen);
}
