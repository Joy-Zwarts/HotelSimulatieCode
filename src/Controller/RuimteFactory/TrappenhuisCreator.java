package Controller.RuimteFactory;

import Model.Layout.Locatie;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;
import Model.Ruimtes.TrappenhuisModel;

public class TrappenhuisCreator extends RuimteFactory{
    @Override
    public RuimteModel createRuimte(Locatie position, String dimension, long capacity, String classification) {
        return new TrappenhuisModel(KamerType.TRAPPEN, position, dimension);
    }
}
