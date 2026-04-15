package Model.Layout;

import Model.Ruimtes.RuimteModel;

public class GridVakjeModel {

    // attributen

    private final int x;
    private final int y;
    private RuimteModel ruimte;
    private final int breedte;
    private final int hoogte;
    private boolean linksboven = false;
    private boolean linksonder = false;
    private boolean rechtsboven = false;

    // constructor
    public GridVakjeModel(int x, int y, int breedte, int hoogte) {
        this.x = x;
        this.y = y;
        this.breedte = breedte;
        this.hoogte = hoogte;
    }

    // getters & setters

    public void setRuimte(RuimteModel ruimte) {
        this.ruimte = ruimte;
    }

    public RuimteModel getRuimte() {
        return ruimte;
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

    public boolean islinksOnder() {
        return linksonder;
    }

    public void setLinksonder(boolean trueOrFalse) {
        this.linksonder = trueOrFalse;
    }

    public boolean isRechtsboven() {
        return rechtsboven;
    }
    public void setRechtsboven(boolean trueOrFalse) {
        this.rechtsboven = trueOrFalse;
    }
}