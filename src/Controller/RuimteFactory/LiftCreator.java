package Controller.RuimteFactory;

import Model.Layout.Locatie;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.LiftModel;
import Model.Ruimtes.RuimteModel;
import Model.Ruimtes.SchachtModel;

public class LiftCreator extends RuimteFactory{
    @Override
    public RuimteModel createRuimte(Locatie position, String dimension, long capacity, String classification) {
        return new LiftModel(KamerType.LIFT, position, dimension, 0 , true);
    }
}
