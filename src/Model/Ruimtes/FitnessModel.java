package Model.Ruimtes;

import Model.Layout.Locatie;

public class FitnessModel extends RuimteModel {
    // attributen
    private static int counter = 0;
    private final String id;

    // constructor
    public FitnessModel(KamerType areaType, Locatie position, String dimension, String ID) {
        super(areaType, position, dimension);
        counter++;
        this.id = ID+counter;
    }

    // getters en setters
    public String getId() {
        return id;
    }
}
