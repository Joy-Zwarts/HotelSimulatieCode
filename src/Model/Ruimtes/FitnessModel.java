package Model.Ruimtes;

public class FitnessModel extends RuimteModel {

    private static int counter = 0;
    private String id;

    public FitnessModel(KamerType areaType, String position, String dimension, String ID) {
        super(areaType, position, dimension);
        counter++;
        this.id = ID+counter;
    }

    public String getId() {
        return id;
    }
}
