package Model;

public class GridVakjeModel {

    // attributen

    private final int x;
    private final int y;
    private RuimteModel ruimte;
    private boolean paused;
    private final int breedte;
    private final int hoogte;
    private boolean linksboven = false;

    // constructor
    public GridVakjeModel(int x, int y, int breedte, int hoogte) {
        this.x = x;
        this.y = y;
        this.breedte = breedte;
        this.hoogte = hoogte;
        this.paused = false;
    }

    // getters & setters

    public void setRuimte(RuimteModel ruimte) {
        this.ruimte = ruimte;
    }

    public RuimteModel getRuimte() {
        return ruimte;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getBreedte() {
        return breedte;
    }

    public int getHoogte() {
        return hoogte;
    }

    public boolean islinksboven() {
        return linksboven;
    }

    public void setlinksboven(boolean trueOrFalse) {
        this.linksboven = trueOrFalse;
    }
}