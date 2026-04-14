package Controller.PersoonFactory;

import Model.Personen.PersoonModel;
import Model.Personen.SchoonmakerModel;
import Model.Ruimtes.KamerType;

public class SchoonmakerCreator extends PersoonFactory {
    @Override
    public PersoonModel createPersoon(int gastId, KamerType targetlocatie, KamerType locatie, int wensen) {
        return new SchoonmakerModel(null, null, null);
    }
}
