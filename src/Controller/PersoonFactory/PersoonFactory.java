package Controller.PersoonFactory;

import Model.PersoonModel;

public abstract class PersoonFactory {

    public abstract PersoonModel createPersoon(int gastId, String targetlocatie, String locatie);
}
