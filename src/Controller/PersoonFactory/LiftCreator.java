package Controller.PersoonFactory;

import Model.Layout.Locatie;
import Model.Entiteiten.EntiteitenModel;
import Model.Entiteiten.TypePersoon;
import Model.Entiteiten.LiftModel;

public class LiftCreator extends EntiteitenFactory {
    // In LiftCreator.java
    @Override
    public EntiteitenModel createEntiteit(int gastId, Locatie locatie, Locatie targetLocatie, int wensen, Locatie stationLocatie, TypePersoon typePersoon) {
        // ID, position, targetLocatie, verdieping, beschikbaar
        return new LiftModel(999, locatie, targetLocatie, 0, true);
    }
}
