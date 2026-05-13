package Model.Personen;

import Model.Layout.Locatie;
import Model.Ruimtes.*;

public class GastModel extends PersoonModel {

    private KamerClassificatie wensen;
    private KamerModel kamer;

    public GastModel(int ID,
                     Locatie locatie,
                     Locatie targetLocatie,
                     KamerClassificatie wensen,
                     KamerModel kamer) {

        super(ID, locatie, targetLocatie);

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
}