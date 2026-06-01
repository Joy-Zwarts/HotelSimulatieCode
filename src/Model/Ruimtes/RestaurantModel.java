package Model.Ruimtes;

import Model.Layout.Locatie;

import java.util.ArrayList;

public class RestaurantModel extends RuimteModel {
    // attributen
    private long capacity;
    private final String id;
    private static int counter = 0;
    private final ArrayList<Integer> huidigeGasten = new ArrayList<>();

    // constructor
    public RestaurantModel(KamerType areaType, Locatie position, String dimension, long capacity, String ID) {
        super(areaType, position, dimension);
        counter++;
        this.capacity = capacity;
        this.id = ID+counter;
    }

    // getters en setters

    public String getID() {
        return id;
    }

    public void setCapacity(long cap) {
        this.capacity = cap;
    }

    public long getCapacity() {
        return capacity;
    }

    // controleer of er nog plek is
    public boolean isVol() {
        return huidigeGasten.size() >= capacity;
    }

    // gast toevoegen
    public boolean voegGastToe(int gastId) {
        if (!isVol()) {
            huidigeGasten.add(gastId);
            return true;
        }
        return false; // restaurant is vol
    }

    // gast verwijderen
    public void verwijderGast(int gastId) {
        huidigeGasten.remove(Integer.valueOf(gastId));
    }

    public boolean bevatGast(int gastId) {
        return huidigeGasten.contains(gastId);
    }
}
