package Model.Personen;

import Model.Layout.Locatie;

import java.awt.*;

public class SchoonmakerModel extends PersoonModel {

    // constructor
    public SchoonmakerModel(int ID, Locatie locatie, Locatie targetLocatie) {
        super(ID, locatie, targetLocatie,   new Color(150, 60, 200));
    }

    public void schoonmaken() {
    }
}
