package Model.Personen;

import Model.Layout.Locatie;
import Model.Ruimtes.KamerType;
import Model.Personen.PersoonModel;

import java.awt.*;

public class LiftModel extends PersoonModel {
    // attributen
    private int verdieping;
    private boolean beschikbaar;

    // constructors
    public LiftModel(int id, Locatie position, Locatie targetLocatie, int verdieping, boolean beschikbaar) {
        super(id, position, targetLocatie, Color.GRAY, TypePersoon.LIFT);
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
