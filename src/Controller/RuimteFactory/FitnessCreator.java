package Controller.RuimteFactory;

import Model.Ruimtes.FitnessModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;

public class FitnessCreator extends RuimteFactory {

    @Override
    public RuimteModel createRuimte(String position, String dimension, long capacity, String classification) {
        return new FitnessModel(KamerType.FITNESS, position, dimension);
    }
}