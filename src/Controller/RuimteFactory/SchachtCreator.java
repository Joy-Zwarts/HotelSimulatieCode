package Controller.RuimteFactory;

import Model.Layout.Locatie;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;
import Model.Ruimtes.SchachtModel;

public class SchachtCreator extends RuimteFactory{
    @Override
    public RuimteModel createRuimte(Locatie position, String dimension, long capacity, String classification) {
        return new SchachtModel(KamerType.SCHACHT, position, dimension);
    }
}
