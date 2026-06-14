package Model.Entiteiten;

import Model.Layout.Locatie;
import java.awt.*;

public class SchoonmakerModel extends PersoonModel {

    private Boolean cleaning;
    private final Locatie station;
    private int schoonmaakTijd;
    private final int huidigeSchoonmaakTijd;

    // constructor
    public SchoonmakerModel(int ID, Locatie locatie, Locatie targetLocatie, TypePersoon type, Locatie stationLocatie) {
        super(ID, locatie, targetLocatie, new Color(150, 60, 200), type);

        // sla de startpositie op als het vast basisstation
        this.station = stationLocatie;
        this.cleaning = false;
        this.schoonmaakTijd = 0;
        this.huidigeSchoonmaakTijd = 0;
    }

    // getters & setters

    public void setCleaning(boolean b) {
        this.cleaning = b;
    }
    public boolean isCleaning() {
        return this.cleaning != null && this.cleaning;
    }
    public Locatie getStation() {
        return this.station;
    }
    public int getSchoonmaakTijd() {
        return schoonmaakTijd;
    }
    public void setSchoonmaakTijd(int schoonmaakTijd) {
        this.schoonmaakTijd = schoonmaakTijd;
    }
    public int getHuidigeSchoonmaakTijd() {
        return huidigeSchoonmaakTijd;
    }
}