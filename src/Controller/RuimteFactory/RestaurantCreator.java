package Controller.RuimteFactory;

import Model.RestaurantModel;
import Model.RuimteModel;

public class RestaurantCreator extends RuimteFactory {

    @Override
    public RuimteModel createRuimte(String areaType, String position, String dimension, long capacity, String classification) {
        return new RestaurantModel(areaType, position, dimension, capacity);
    }
}