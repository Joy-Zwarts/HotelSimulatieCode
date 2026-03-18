public class Restaurant extends Ruimte {
    private long capacity;

    public Restaurant(String areaType, String position, String dimension, long capacity) {
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
