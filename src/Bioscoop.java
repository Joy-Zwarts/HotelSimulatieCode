public class Bioscoop extends Ruimte {

    private int stoelen;

    public Bioscoop(String areaType, String position, String dimension, int stoelen) {
        super(areaType, position, dimension);
        this.stoelen = stoelen;
    }
}
