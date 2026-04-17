package Controller.RuimteFactory;

import Controller.Layout.Locatie;
import Model.Ruimtes.RuimteModel;

public abstract class RuimteFactory {

    // factory voor alle ruimtes
    public abstract RuimteModel createRuimte(Locatie position, String dimension, long capacity, String classification);
}