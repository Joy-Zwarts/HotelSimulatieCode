package Model.Ruimtes;

import Controller.Layout.Locatie;

public class RestaurantModel extends RuimteModel {
    // attributen
    private final long capacity;
    private final String id;
    private static int counter = 0;

    // constructor
    public RestaurantModel(KamerType areaType, Locatie position, String dimension, long capacity, String ID) {
        super(areaType, position, dimension);

        counter++;

        this.capacity = capacity;
        this.id = ID+counter;
    }

    // getters en setters
    public String getID() {
        return id;
    }
}
