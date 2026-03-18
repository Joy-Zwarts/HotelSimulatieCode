public class Gast extends Persoon {
    private RoomClassificatie wensen;
    private Kamer kamer;

    public Gast(String locatie, String targetLocatie, RoomClassificatie wensen, Kamer kamer) {
        super(locatie, targetLocatie);
        this.wensen = wensen;
        this.kamer = kamer;
    }

    public void inchecken(Kamer kamer) {
    }

    public void uitchecken(Kamer kamer) {
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

    public Kamer getKamer() {
        return this.kamer;
    }

    public void setKamer(Kamer kamer) {
        this.kamer = kamer;
    }

    public RoomClassificatie getWensen() {
        return this.wensen;
    }
}
