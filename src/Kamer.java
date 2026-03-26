import Model.Ruimte;

public class Kamer extends Ruimte {
    private RoomClassificatie classification;

    public Kamer(String areaType, String position, String dimension, RoomClassificatie classification) {
        super(areaType, position, dimension);
        this.classification = classification;
    }

    public RoomClassificatie getClassification() {
        return this.classification;
    }
}
