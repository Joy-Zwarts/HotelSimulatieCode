package Model.Personen;

import Model.Layout.Locatie;

public class PersoonModel {
    // attributen
    private Locatie targetLocatie;
    private Locatie locatie;
    private Locatie vorigeLocatie;

    // constructor
    public PersoonModel(Locatie targetLocatie, Locatie locatie) {
        this.targetLocatie = targetLocatie;
        this.vorigeLocatie = null;
        this.locatie = locatie;
    }

    // getters en setters

    public Locatie getTargetLocatie() {
        return targetLocatie;
    }

    public Locatie getLocatie() {
        return locatie;
    }
    public Locatie getVorigeLocatie() {
        return vorigeLocatie;
    }

    public void setVorigeLocatie(Locatie locatie) {
        this.vorigeLocatie = locatie;
    }

    public void setLocatie(Locatie loc) {
        this.locatie = loc;
    }
}
