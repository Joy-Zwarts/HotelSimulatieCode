package Controller.PersoonFactory;

import Model.Personen.PersoonModel;
import Model.Ruimtes.KamerType;

public abstract class PersoonFactory {

    public abstract PersoonModel createPersoon(int gastId, KamerType targetlocatie, KamerType locatie, int wensen);
}
