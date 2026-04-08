package Model;

public class GastModel extends PersoonModel {
    private RoomClassificatie wensen;
    private KamerModel kamer;

    public GastModel(String locatie, String targetLocatie, RoomClassificatie wensen, KamerModel kamer) {
        super(locatie, targetLocatie);
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
}
