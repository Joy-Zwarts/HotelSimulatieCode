public class Lift extends Ruimte {
    private int verdieping;
    private boolean beschikbaar;

    public Lift(String areaType, String position, String dimension, int verdieping, boolean beschikbaar) {
        super(areaType, position, dimension);
        this.verdieping = verdieping;
        this.beschikbaar = beschikbaar;
    }

    public void liftOmhoog() {
        --this.verdieping;
        this.setPositionY(this.verdieping);
    }

    public void liftOmlaag() {
        ++this.verdieping;
        this.setPositionY(this.verdieping);
    }

    public void liftCalled() {
    }

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
