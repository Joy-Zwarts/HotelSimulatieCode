package Model.Ruimtes;

public class BioscoopModel extends RuimteModel {

    private int stoelen;

    private String id;

    private static int counter = 0;

    public BioscoopModel(KamerType areaType, String position, String dimension, int stoelen, String ID) {
        super(areaType, position, dimension);

        counter++;

        this.stoelen = stoelen;
        this.id = ID+counter;
    }

    public String getId() {
        return id;
    }
}
