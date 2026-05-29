package Model.Ruimtes;

import Model.Layout.Locatie;

public class BioscoopModel extends RuimteModel {

    // attributen

    private final String id;

    private static int counter = 0;

    // constructor
    public BioscoopModel(KamerType areaType, Locatie position, String dimension, String ID) {
        super(areaType, position, dimension);

        counter++;

        this.id = ID+counter;
    }

    // getters en setters
    public String getId() {
        return id;
    }
}
