package View;

import Model.Gast;

import java.util.ArrayList;

public class OverzichtScherm {
    private ArrayList<Gast> gasten;
    private int tijd;

    public OverzichtScherm(ArrayList<Gast> gasten, int tijd) {
        this.gasten = gasten;
        this.tijd = tijd;
    }

    public ArrayList<Gast> getGasten() {
        return this.gasten;
    }

    public void addGasten(ArrayList<Gast> gasten) {
        this.gasten = gasten;
    }

    public int getTijd() {
        return this.tijd;
    }

    public void setTijd(int tijd) {
        this.tijd = tijd;
    }
}
