package Model.Ruimtes;

public class RestaurantModel extends RuimteModel {
    private long capacity;
    private String id;
    private static int counter = 0;

    public RestaurantModel(KamerType areaType, String position, String dimension, long capacity, String ID) {
        super(areaType, position, dimension);

        counter++;

        this.capacity = capacity;
        this.id = ID+counter;
    }

    public String getID() {
        return id;
    }
}
