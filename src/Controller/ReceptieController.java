package Controller;

import Model.GastModel;
import Model.KamerModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ReceptieController {
    private final HashMap<Integer, KamerModel> kamers;
    private final HashMap<Integer, KamerModel> legeKamers;
    private final HashMap<Integer, GastModel> gasten;

    public ReceptieController() {
        this.kamers = new HashMap<>();
        this.legeKamers = new HashMap<>();
        this.gasten = new HashMap<>();
    }

    public void gastCheckIn() {
    }

    public void gastCheckOut() {
    }

    public HashMap<Integer, KamerModel> getLegeKamers() {
        return this.legeKamers;
    }

    public void setKamerLeeg(KamerModel kamer) {
        kamer.setBezet(false);
        legeKamers.put(kamer.getRoomNumber(), kamer);
        kamers.remove(kamer.getRoomNumber());
    }

    public void setKamerVol(KamerModel kamer) {
        kamer.setBezet(true);
        kamers.put(kamer.getRoomNumber(), kamer);
        legeKamers.remove(kamer.getRoomNumber());
    }

    public HashMap<Integer, KamerModel> getKamers() {
        return this.kamers;
    }

    public void addKamer(KamerModel kamer) {
        kamers.put(kamer.getRoomNumber(), kamer);
    }

    public HashMap<Integer, GastModel> getGasten() {
        return this.gasten;
    }

    public void addGasten(GastModel gast) {
        this.gasten.put(gast.getGastID(), gast);
    }
}
