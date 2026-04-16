package Controller.PersoonFactory;

import Model.Personen.PersoonModel;
import Model.Ruimtes.KamerType;

public abstract class PersoonFactory {

    public abstract PersoonModel createPersoon(int gastId, String targetlocatie, String locatie, int wensen);
}
