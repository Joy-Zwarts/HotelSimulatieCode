package Model.Personen;

import Model.Layout.Locatie;

public class LiftModel extends EntiteitenModel {
    // attributen
    private int verdieping;
    private boolean beschikbaar;

    // constructors
    public LiftModel(int id, Locatie position, Locatie targetLocatie, int verdieping, boolean beschikbaar) {
        super(id, position, targetLocatie);
        this.verdieping = verdieping;
        this.beschikbaar = beschikbaar;
    }

    public void liftOmhoog() {
        --this.verdieping;
        this.getLocatie().setY(this.verdieping);
    }

    public void liftOmlaag() {
        ++this.verdieping;
        this.getLocatie().setY(this.verdieping);
    }

    public void liftCalled() {
    }

    // getters en setters
    public int getVerdieping() {
        return this.verdieping;
    }

    public void setVerdieping(int verdieping) {
        this.verdieping = verdieping;
    }

    public boolean isBeschikbaar() {
        return this.beschikbaar;
    }

    public void setBeschikbaar(boolean beschikbaar) {
        this.beschikbaar = beschikbaar;
    }

}
