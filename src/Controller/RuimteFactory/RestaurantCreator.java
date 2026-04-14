package Controller.RuimteFactory;

import Model.Ruimtes.KamerType;
import Model.Ruimtes.RestaurantModel;
import Model.Ruimtes.RuimteModel;

public class RestaurantCreator extends RuimteFactory {

    @Override
    public RuimteModel createRuimte(String position, String dimension, long capacity, String classification) {
        return new RestaurantModel(KamerType.RESTAURANT, position, dimension, capacity);
    }
}