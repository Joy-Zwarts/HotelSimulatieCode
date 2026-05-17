package Model.Personen;

import Model.Layout.Locatie;

import java.awt.*;

public class SchoonmakerModel extends PersoonModel {

    private Boolean cleaning;

    // constructor
    public SchoonmakerModel(int ID, Locatie locatie, Locatie targetLocatie) {
        super(ID, locatie, targetLocatie,   new Color(150, 60, 200));
    }

    public void setCleaning(boolean b) {
        this.cleaning = b;
    }

    public boolean getCleaning() {
        return this.cleaning;
    }
}
