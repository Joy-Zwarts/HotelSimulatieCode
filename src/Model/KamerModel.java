package Model;

public class KamerModel extends RuimteModel {
    private RoomClassificatie classification;

    public KamerModel(String areaType, String position, String dimension, RoomClassificatie classification) {
        super(areaType, position, dimension);
        this.classification = classification;
    }

    public RoomClassificatie getClassification() {
        return this.classification;
    }
}
