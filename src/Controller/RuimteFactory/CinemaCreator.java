package Controller.RuimteFactory;

import Model.Ruimtes.BioscoopModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;

public class CinemaCreator extends RuimteFactory {

    @Override
    public RuimteModel createRuimte(String position, String dimension, long capacity, String classification) {
        return new BioscoopModel(KamerType.FITNESS, position, dimension, 20);
    }
}