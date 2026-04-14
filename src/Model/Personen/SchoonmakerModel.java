package Model.Personen;

import Model.Ruimtes.KamerType;

public class SchoonmakerModel extends PersoonModel {
    public SchoonmakerModel(KamerType locatie, KamerType targetLocatie, String schoonmaakSpot) {
        super(locatie, targetLocatie);
    }

    public void schoonmaken() {
    }
}
