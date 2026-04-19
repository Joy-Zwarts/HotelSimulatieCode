package Model.Ruimtes;

import Controller.Layout.Locatie;

public class BioscoopModel extends RuimteModel {

    // attributen

    private final int stoelen;

    private final String id;

    private static int counter = 0;

    // constructor
    public BioscoopModel(KamerType areaType, Locatie position, String dimension, int stoelen, String ID) {
        super(areaType, position, dimension);

        counter++;

        this.stoelen = stoelen;
        this.id = ID+counter;
    }

    // getters en setters
    public String getId() {
        return id;
    }
}
