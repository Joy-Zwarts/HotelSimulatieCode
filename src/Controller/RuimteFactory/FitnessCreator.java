package Controller.RuimteFactory;

import Model.Layout.Locatie;
import Model.Ruimtes.FitnessModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;

public class FitnessCreator extends RuimteFactory {

    // maakt een nieuwe fitness room aan gebaseerd op de meegekregen data
    @Override
    public RuimteModel createRuimte(Locatie position, String dimension, long capacity, String classification) {
        return new FitnessModel(KamerType.FITNESS, position, dimension, "F");
    }
}