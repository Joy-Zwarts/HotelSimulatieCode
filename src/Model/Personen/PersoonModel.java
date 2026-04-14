package Model.Personen;

import Model.Ruimtes.KamerType;

public class PersoonModel {
    private KamerType targetLocatie;
    private KamerType locatie;

    public PersoonModel(KamerType targetLocatie, KamerType locatie) {
        this.targetLocatie = targetLocatie;
        this.locatie = locatie;
    }

    public void bewegen() {
    }

    public KamerType getTargetLocatie() {
        return targetLocatie;
    }
    public void setTargetLocatie(KamerType targetLocatie) {
        this.targetLocatie = targetLocatie;
    }
    public KamerType getLocatie() {
        return locatie;
    }
    public void setLocatie(KamerType locatie) {
        this.locatie = locatie;
    }
}
