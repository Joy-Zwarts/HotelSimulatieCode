package Model.Personen;

import Model.Ruimtes.KamerType;

public class PersoonModel {
    private String targetLocatie;
    private String locatie;

    public PersoonModel(String targetLocatie, String locatie) {
        this.targetLocatie = targetLocatie;
        this.locatie = locatie;
    }

    public void bewegen() {
    }

    public String getTargetLocatie() {
        return targetLocatie;
    }
    public void setTargetLocatie(String targetLocatie) {
        this.targetLocatie = targetLocatie;
    }
    public String getLocatie() {
        return locatie;
    }
    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }
}
