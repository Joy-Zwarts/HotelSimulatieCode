package Model.Ruimtes;

import Controller.Layout.Locatie;
import Model.Personen.GastModel;

public class KamerModel extends RuimteModel {
    // attributen
    private final RoomClassificatie classification;
    private final int roomNumber;
    private boolean bezet;
    private GastModel verblijvende;
    private static int counter = 0;

    // constructor
    public KamerModel(KamerType areaType, Locatie position, String dimension, RoomClassificatie classification, boolean Bezet) {
        super(areaType, position, dimension);
        this.classification = classification;
        this.roomNumber = ++counter;
        this.bezet = Bezet;
        this.verblijvende = null;
    }

    // getters en setters

    public RoomClassificatie getClassification() {
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
}
