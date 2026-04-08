package Controller.RuimteFactory;

import Model.FitnessModel;
import Model.RuimteModel;

public class FitnessCreator extends RuimteFactory {

    @Override
    public RuimteModel createRuimte(String areaType, String position, String dimension, long capacity, String classification) {
        return new FitnessModel(areaType, position, dimension);
    }
}