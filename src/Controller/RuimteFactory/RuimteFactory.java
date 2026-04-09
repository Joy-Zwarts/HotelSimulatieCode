package Controller.RuimteFactory;

import Model.RuimteModel;

public abstract class RuimteFactory {
    public abstract RuimteModel createRuimte(String areaType, String position, String dimension, long capacity, String classification);
}