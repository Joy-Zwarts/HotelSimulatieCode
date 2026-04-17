package Controller.RuimteFactory;

import Controller.Layout.Locatie;
import Model.Ruimtes.BioscoopModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;

public class CinemaCreator extends RuimteFactory {

    // maakt een nieuwe cinema aan gebaseerd op de meegekregen data
    @Override
    public RuimteModel createRuimte(Locatie position, String dimension, long capacity, String classification) {
        return new BioscoopModel(KamerType.CINEMA, position, dimension, 20, "C");
    }
}