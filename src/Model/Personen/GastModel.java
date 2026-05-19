package Model.Personen;

import Model.Layout.Locatie;
import Model.Ruimtes.*;

import java.awt.*;

public class GastModel extends PersoonModel {

    private KamerClassificatie wensen;
    private KamerModel kamer;
    private String activity;
    private int ticksLeft;

    public GastModel(int ID,
                     Locatie locatie,
                     Locatie targetLocatie,
                     KamerClassificatie wensen,
                     KamerModel kamer) {

        super(ID, locatie, targetLocatie, new Color(36, 104, 181));

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
    public int getTicksLeft() {
        return ticksLeft;
    }
    public void setActivity(String activity) {
        this.activity = activity;
    }
    public void setTicksLeft(int ticksLeft) {
        this.ticksLeft = ticksLeft;
    }
}