package Model;

public class SchoonmakerModel extends PersoonModel {
    public SchoonmakerModel(String locatie, String targetLocatie, RoomClassificatie wensen, KamerModel kamer) {
        super(locatie, targetLocatie);
    }

    public void schoonmaken() {
    }
}
