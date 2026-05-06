package Model.Ruimtes;

import Model.Layout.Locatie;
import Model.Personen.GastModel;

public class KamerModel extends RuimteModel {
    // attributen
    private final KamerClassificatie classification;
    private int roomNumber;
    private boolean bezet;
    private GastModel verblijvende;
    private static int counter = 0;

    // constructor
    public KamerModel(KamerType areaType, Locatie position, String dimension, KamerClassificatie classification, boolean Bezet) {
        super(areaType, position, dimension);
        this.classification = classification;
        this.roomNumber = ++counter;
        this.bezet = Bezet;
        this.verblijvende = null;
    }

    // getters en setters

    public KamerClassificatie getClassification() {
        return this.classification;
    }
    public int getRoomNumber() {
        return this.roomNumber;
    }
    public void setBezet(boolean bezet) {
        this.bezet = bezet;
    }
    public boolean isBezet() {
        return this.bezet;
    }
    public GastModel getVerblijvende() {
        return this.verblijvende;
    }
    public void setVerblijvende(GastModel verblijvende) {
        this.verblijvende = verblijvende;
    }

    public void setRoomNumber(int i) {
        roomNumber = i;
    }
}
