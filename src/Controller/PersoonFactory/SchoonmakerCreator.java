package Controller.PersoonFactory;

import Model.PersoonModel;
import Model.SchoonmakerModel;

public class SchoonmakerCreator extends PersoonFactory {
    @Override
    public PersoonModel createPersoon(int gastId, String targetlocatie, String locatie) {
        return new SchoonmakerModel(null, null, null);
    }
}
