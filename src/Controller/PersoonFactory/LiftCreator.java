package Controller.PersoonFactory;

import Model.Layout.Locatie;
import Model.Personen.TypePersoon;
import Model.Personen.LiftModel;
import Model.Personen.PersoonModel;

public class LiftCreator extends PersoonFactory {
    @Override
    public PersoonModel createPersoon(int gastId, Locatie locatie, Locatie targetLocatie, int wensen, Locatie stationLocatie, TypePersoon typePersoon) {
        return new LiftModel(999999, locatie, targetLocatie,0, true);
    }
}
