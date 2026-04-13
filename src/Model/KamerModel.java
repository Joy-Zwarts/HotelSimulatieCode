package Model;

import Controller.ReceptieController;

public class KamerModel extends RuimteModel {
    private RoomClassificatie classification;
    private int roomNumber;
    private boolean bezet;
    private ReceptieController receptieController;

    public KamerModel(String areaType, String position, String dimension, RoomClassificatie classification, int RoomNumber, boolean Bezet) {
        super(areaType, position, dimension);
        this.classification = classification;
        this.roomNumber = RoomNumber;
        this.bezet = Bezet;
        this.receptieController = null;
    }

    public RoomClassificatie getClassification() {
        return this.classification;
    }
    public int getRoomNumber() {
        return this.roomNumber;
    }
    public void setClassification(RoomClassificatie classification) {
        this.classification = classification;
    }
    public void setBezet(boolean bezet) {
        this.bezet = bezet;
    }
    public boolean isBezet() {
        return this.bezet;
    }
    public void setReceptieController(ReceptieController receptieController) {
        this.receptieController = receptieController;
    }
}
