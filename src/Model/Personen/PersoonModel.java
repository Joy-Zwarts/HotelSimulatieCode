package Model.Personen;

import Model.Layout.Locatie;

public class PersoonModel {
    // attributen
    private final Locatie targetLocatie;
    private final Locatie locatie;

    // constructor
    public PersoonModel(Locatie targetLocatie, Locatie locatie) {
        this.targetLocatie = targetLocatie;
        this.locatie = locatie;
    }

    // getters en setters

    public Locatie getTargetLocatie() {
        return targetLocatie;
    }

    public Locatie getLocatie() {
        return locatie;
    }
}
