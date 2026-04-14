package Controller.RuimteFactory;

import Model.Ruimtes.RuimteModel;

public abstract class RuimteFactory {

    public abstract RuimteModel createRuimte(String position, String dimension, long capacity, String classification);
}