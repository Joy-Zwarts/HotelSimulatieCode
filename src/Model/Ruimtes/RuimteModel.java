package Model.Ruimtes;

import Model.Personen.GastModel;

import java.util.ArrayList;

public class RuimteModel {

    // attributen
    private KamerType areaType;
    private String position;
    private String dimension;
    private ArrayList<GastModel> gasten;

    // constructor
    public RuimteModel(KamerType areaType, String position, String dimension) {
        this.areaType = areaType;
        this.position = position;
        this.dimension = dimension;
        this.gasten = new ArrayList<>();
    }

    // getters en setters

    public KamerType getAreaType() {
        return areaType;
    }

    public int getPositionX() {
        String[] pos = position.split(",");
        int x = Integer.parseInt(pos[0].trim());
        return x;
    }
    public int getPositionY() {
        String[] pos = position.split(",");
        int y = Integer.parseInt(pos[1].trim());
        return y;
    }
    public void setPositionY(int y) {
        String newpositionY = getPositionX() + "," + y;
        this.position = newpositionY;
    }
    public int getDimensionW() {
        String[] dim = dimension.split(",");
        int w = Integer.parseInt(dim[0].trim());
        return w;
    }
    public int getDimensionH() {
        String[] dim = dimension.split(",");
        int h = Integer.parseInt(dim[1].trim());
        return h;
    }
    public String getDimension() {
        return dimension;
    }
    public String getPosition() {
        return position;
    }
}