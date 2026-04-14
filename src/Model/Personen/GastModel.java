package Model.Personen;

import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RoomClassificatie;

public class GastModel extends PersoonModel {
    private final RoomClassificatie wensen;
    private KamerModel kamer;
    private final int gastID;

    public GastModel(int gastId, KamerType locatie, KamerType targetLocatie, RoomClassificatie wensen, KamerModel kamer) {
        super(locatie, targetLocatie);
        this.gastID = gastId;
        this.wensen = wensen;
        this.kamer = kamer;
    }

    public void inchecken(KamerModel kamer) {
    }

    public void uitchecken(KamerModel kamer) {
    }

    public void gebruikFitness() {
    }

    public void gebruikKamer() {
    }

    public void gebruikBios() {
    }

    public void gebruikTrap() {
    }

    public void callLift() {
    }

    public void gebruikLift() {
    }

    public KamerModel getKamer() {
        return this.kamer;
    }

    public void setKamer(KamerModel kamer) {
        this.kamer = kamer;
    }

    public RoomClassificatie getWensen() {
        return this.wensen;
    }

    public int getGastID() {
        return this.gastID;
    }
}
