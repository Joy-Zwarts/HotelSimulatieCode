package Model.Entiteiten;

import Model.Layout.Locatie;

public class EntiteitenModel {

    private final int ID;

    private Locatie locatie;
    private final Locatie targetLocatie;
    private Locatie vorigeLocatie;

    public EntiteitenModel(int ID, Locatie locatie, Locatie targetLocatie) {
        this.ID = ID;
        this.locatie = locatie;
        this.targetLocatie = targetLocatie;
    }

    public int getID() {
        return ID;
    }

    public Locatie getLocatie() {
        return locatie;
    }

    public void setLocatie(Locatie locatie) {
        this.locatie = locatie;
    }

    public Locatie getTargetLocatie() {
        return targetLocatie;
    }

    public Locatie getVorigeLocatie() {
        return vorigeLocatie;
    }

    public void setVorigeLocatie(Locatie vorigeLocatie) {
        this.vorigeLocatie = vorigeLocatie;
    }


    public void setTargetLocatie(Locatie locatie) {
        this.locatie = targetLocatie;
    }
}