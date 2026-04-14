package Model.Ruimtes;

public class BioscoopModel extends RuimteModel {

    private int stoelen;

    public BioscoopModel(KamerType areaType, String position, String dimension, int stoelen) {
        super(areaType, position, dimension);
        this.stoelen = stoelen;
    }
}
