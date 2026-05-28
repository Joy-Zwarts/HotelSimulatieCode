package Model.Personen;

import Model.Layout.Locatie;
import Model.Ruimtes.*;

import java.awt.*;

public class GastModel extends PersoonModel {

    private KamerClassificatie wensen;
    private KamerModel kamer;
    private String activity;

    public GastModel(int ID, Locatie locatie, Locatie targetLocatie, TypePersoon type, KamerClassificatie wensen, KamerModel kamer) {
        super(ID, locatie, targetLocatie, new Color(36, 104, 181), type);
        this.wensen = wensen;
        this.kamer = kamer;
    }

    public KamerModel getKamer() {
        return kamer;
    }

    public KamerClassificatie getWensen() {
        return wensen;
    }

    public void setKamer(KamerModel k) {
        this.kamer = k;
    }

    public void setWensen(KamerClassificatie kamerClassificatie) {
        this.wensen = kamerClassificatie;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}