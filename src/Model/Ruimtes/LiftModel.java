package Model.Ruimtes;

import Controller.Layout.Locatie;

public class LiftModel extends RuimteModel {
    // attributen
    private int verdieping;
    private boolean beschikbaar;

    // constructors
    public LiftModel(KamerType areaType, Locatie position, String dimension, int verdieping, boolean beschikbaar) {
        super(areaType, position, dimension);
        this.verdieping = verdieping;
        this.beschikbaar = beschikbaar;
    }

    public void liftOmhoog() {
        --this.verdieping;
        this.getPosition().setY(this.verdieping);
    }

    public void liftOmlaag() {
        ++this.verdieping;
        this.getPosition().setY(this.verdieping);
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
