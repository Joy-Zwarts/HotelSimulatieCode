package Controller.RuimteFactory;

import Model.Layout.Locatie;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RestaurantModel;
import Model.Ruimtes.RuimteModel;

public class RestaurantCreator extends RuimteFactory {

    // maakt een nieuw restaurant aan gebaseerd op de meegekregen data
    @Override
    public RuimteModel createRuimte(Locatie position, String dimension, long capacity, String classification) {
        return new RestaurantModel(KamerType.RESTAURANT, position, dimension, capacity, "R");
    }
}