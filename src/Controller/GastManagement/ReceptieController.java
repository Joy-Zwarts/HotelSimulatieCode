package Controller.GastManagement;

import Model.Personen.GastModel;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.RuimteModel;
import View.Systeem.OverzichtView;

import java.util.HashMap;

public class ReceptieController implements NewRoom, NewGuest {
    private final HashMap<Integer, KamerModel> kamers;
    private final HashMap<Integer, KamerModel> legeKamers;
    private final HashMap<Integer, KamerModel> volleKamers;

    private final HashMap<Integer, GastModel> gasten;

    private final OverzichtView view;

    public ReceptieController(OverzichtView view) {
        this.kamers = new HashMap<>();
        this.legeKamers = new HashMap<>();
        this.volleKamers = new HashMap<>();
        this.gasten = new HashMap<>();
        this.view = view;
    }

    public void setKamerLeeg(KamerModel kamer) {
        if (kamer == null) return;

        kamer.setBezet(false);
        kamer.setVerblijvende(null);

        legeKamers.put(kamer.getRoomNumber(), kamer);
        volleKamers.remove(kamer.getRoomNumber());
    }

    public void setKamerVol(KamerModel kamer) {
        if (kamer == null) return;

        kamer.setBezet(true);

        volleKamers.put(kamer.getRoomNumber(), kamer);
        legeKamers.remove(kamer.getRoomNumber());
    }

    public HashMap<Integer, KamerModel> getLegeKamers() {
        return legeKamers;
    }

    public HashMap<Integer, KamerModel> getVolleKamers() {
        return volleKamers;
    }

    public HashMap<Integer, KamerModel> getKamers() {
        return kamers;
    }

    public HashMap<Integer, GastModel> getGasten() {
        return gasten;
    }

    public GastModel getGast(int gastID) {
        return gasten.get(gastID);
    }

    public void addGast(GastModel gast) {
        gasten.put(gast.getGastID(), gast);
        refreshView();
    }

    public void removeGast(int gastId) {
        gasten.remove(gastId);
        refreshView();
    }

    @Override
    public void onNewRoom(RuimteModel kamer) {
        KamerModel k = (KamerModel) kamer;

        kamers.put(k.getRoomNumber(), k);

        setKamerLeeg(k); // initieel leeg

        System.out.println("New room has been created");

        refreshView();
    }

    @Override
    public void onGastAangemaakt(GastModel gast) {
        addGast(gast);

        System.out.println("New Guest has been created");
    }

    @Override
    public void onGastVertrokken(int gastID) {

        GastModel gast = gasten.get(gastID);

        if (gast != null && gast.getKamer() != null) {
            KamerModel kamer = gast.getKamer();

            kamer.setBezet(false);
            kamer.setVerblijvende(null);

            setKamerLeeg(kamer);

            gast.setKamer(null);
        }

        removeGast(gastID);

        System.out.println("Guest has left");
    }

    public void refreshView() {
        view.tekenGastLijst(gasten);

        view.tekenKamerLijst(kamers);
    }
}