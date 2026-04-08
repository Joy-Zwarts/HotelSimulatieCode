package Controller.RuimteFactory;

import Model.BioscoopModel;
import Model.RuimteModel;

public class CinemaCreator extends RuimteFactory {

    @Override
    public RuimteModel createRuimte(String areaType, String position, String dimension, long capacity, String classification) {
        return new BioscoopModel(areaType, position, dimension, 20);
    }
}