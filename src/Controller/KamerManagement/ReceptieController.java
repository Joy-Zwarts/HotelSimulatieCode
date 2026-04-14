package Controller.KamerManagement;

import Model.Personen.GastModel;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.RuimteModel;
import View.Systeem.OverzichtView;

import java.awt.event.ActionListener;
import java.util.HashMap;

public class ReceptieController implements NewRoom, NewGuest {
    private final HashMap<Integer, KamerModel> kamers;
    private final HashMap<Integer, KamerModel> legeKamers;
    private final HashMap<Integer, GastModel> gasten;
    private final OverzichtView view;

    public ReceptieController(OverzichtView View) {
        this.kamers = new HashMap<>();
        this.legeKamers = new HashMap<>();
        this.gasten = new HashMap<>();
        this.view = View;
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

    private void refreshView() {
        view.tekenGastLijst(gasten);
    }

    @Override
    public void onNewRoom(RuimteModel kamer) {
        addKamer((KamerModel) kamer);
        System.out.println("New room has been created");
        view.tekenKamerLijst(kamers);
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
