package Controller.KamerManagement;

import Model.Personen.GastModel;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.RuimteModel;
import View.Systeem.OverzichtView;

import java.util.HashMap;

public class ReceptieController implements NewRoom, NewGuest {
    private final HashMap<Integer, KamerModel> legeKamers;
    private final HashMap<Integer, KamerModel> volleKamers;
    private final HashMap<Integer, GastModel> gasten;
    private final OverzichtView view;

    public ReceptieController(OverzichtView View) {
        this.legeKamers = new HashMap<>();
        this.volleKamers = new HashMap<>();
        this.gasten = new HashMap<>();
        this.view = View;
    }

    public void gastCheckIn() {
    }

    public void gastCheckOut() {
    }

    public HashMap<Integer, KamerModel> getVolleKamers() {
        return this.volleKamers;
    }

    public void setKamerLeeg(KamerModel kamer) {
        kamer.setBezet(false);
        volleKamers.put(kamer.getRoomNumber(), kamer);
        legeKamers.remove(kamer.getRoomNumber());
    }

    public void setKamerVol(KamerModel kamer) {
        kamer.setBezet(true);
        legeKamers.put(kamer.getRoomNumber(), kamer);
        volleKamers.remove(kamer.getRoomNumber());
    }

    public HashMap<Integer, KamerModel> getLegeKamers() {
        return this.legeKamers;
    }

    public void addKamer(KamerModel kamer) {
        legeKamers.put(kamer.getRoomNumber(), kamer);
    }

    public HashMap<Integer, GastModel> getGasten() {
        return this.gasten;
    }

    public GastModel getGast(int gastID) {
        return this.gasten.get(gastID);
    }

    public void addGast(GastModel gast) {
        gasten.put(gast.getGastID(), gast);
        refreshView();
    }

    public void removeGast(int gastId) {
        gasten.remove(gastId);
        refreshView();
    }

    public void refreshView() {
        view.tekenGastLijst(gasten);
        view.tekenKamerLijst(legeKamers);
    }

    @Override
    public void onNewRoom(RuimteModel kamer) {
        addKamer((KamerModel) kamer);
        System.out.println("New room has been created");
        view.tekenKamerLijst(legeKamers);
    }

    @Override
    public void onGastAangemaakt(GastModel gast) {
        addGast(gast);
        System.out.println("New Guest has been created");
        view.tekenGastLijst(gasten);
    }

    @Override
    public void onGastVertrokken(int gastID) {
        removeGast(gastID);
        System.out.println("Guest has left");
        view.tekenGastLijst(gasten);
    }
}
