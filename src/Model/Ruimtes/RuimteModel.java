package Model.Ruimtes;

import Model.Layout.Locatie;

public class RuimteModel {

    // attributen
    private final KamerType areaType;
    private final Locatie position;
    private final String dimension;
    private int aantalGasten = 0;

    // constructor
    public RuimteModel(KamerType areaType, Locatie position, String dimension) {
        this.areaType = areaType;
        this.position = position;
        this.dimension = dimension;
    }

    // getters en setters

    public KamerType getAreaType() {
        return areaType;
    }

    public int getDimensionW() {
        String[] dim = dimension.split(",");
        return Integer.parseInt(dim[0].trim());
    }
    public int getDimensionH() {
        String[] dim = dimension.split(",");
        return Integer.parseInt(dim[1].trim());
    }
    public String getDimension() {
        return dimension;
    }
    public String getPositionString() {
        return position.toString();
    }

    public int getAantalGasten() {
        return aantalGasten;
    }

    public void setAantalGasten(int aantalGasten) {
        this.aantalGasten = aantalGasten;
    }

    public Locatie getPosition() {
        return position;
    }
}