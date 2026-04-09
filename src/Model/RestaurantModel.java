package Model;

public class RestaurantModel extends RuimteModel {
    private long capacity;

    public RestaurantModel(String areaType, String position, String dimension, long capacity) {
        super(areaType, position, dimension);
        this.capacity = capacity;
    }

    public void plaatsGast() {
    }

    public void gastAfrekenen() {
    }

    public long getCapacity() {
        return this.capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }
}
