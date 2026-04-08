package Controller;

import Model.GastModel;
import Model.KamerModel;

import java.util.ArrayList;

public class ReceptieController {
    private ArrayList<KamerModel> kamers;
    private final ArrayList<KamerModel> legeKamers;
    private ArrayList<GastModel> gasten;

    public ReceptieController(ArrayList<KamerModel> kamers, ArrayList<KamerModel> legeKamers, ArrayList<GastModel> gasten) {
        this.kamers = kamers;
        this.legeKamers = legeKamers;
        this.gasten = gasten;
    }

    public void gastCheckIn() {
    }

    public void gastCheckOut() {
    }

    public ArrayList<KamerModel> getLegeKamers() {
        return this.legeKamers;
    }

    public void setKamerLeeg(KamerModel kamer) {
        this.legeKamers.add(kamer);
    }

    public void setKamerVol(KamerModel kamer) {
        this.legeKamers.remove(kamer);
    }

    public ArrayList<KamerModel> getKamers() {
        return this.kamers;
    }

    public void addKamers(ArrayList<KamerModel> kamers) {
        this.kamers = kamers;
    }

    public ArrayList<GastModel> getGasten() {
        return this.gasten;
    }

    public void addGasten(ArrayList<GastModel> gasten) {
        this.gasten = gasten;
    }
}
