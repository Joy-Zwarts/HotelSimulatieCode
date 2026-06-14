package Controller.PersoonManagement;

import Controller.PersoonManagement.Interfaces.NewGast;
import Controller.PersoonManagement.Interfaces.NewKamer;
import Controller.Systeem.Interfaces.reset;
import Model.Layout.Locatie;
import Model.Entiteiten.GastModel;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.RuimteModel;
import View.Systeem.OverzichtView;

import java.util.HashMap;

public class ReceptieController implements NewKamer, NewGast, reset {

    // attributen
    private final HashMap<Integer, KamerModel> kamers;

    private final HashMap<Integer, GastModel> gasten;

    private final OverzichtView view;

    // constructor

    public ReceptieController(OverzichtView view) {
        this.kamers = new HashMap<>();
        this.gasten = new HashMap<>();
        this.view = view;
    }

    // zet een kamer op leeg en haalt deze uit de volle kamer lijst en stopt deze in de lege kamer lijst
    public void setKamerLeeg(KamerModel kamer) {
        if (kamer == null) return;

        kamer.setBezet(false);
        kamer.setVerblijvende(null);
    }

    // zet een kamer op vol en haalt deze uit de lege kamer lijst en stopt deze in de volle kamer lijst
    public void setKamerVol(KamerModel kamer) {
        if (kamer == null) return;

        kamer.setBezet(true);
    }

    // getters en setters

    public HashMap<Integer, KamerModel> getKamers() {
        return kamers;
    }

    public GastModel getGast(int gastID) {
        return gasten.get(gastID);
    }

    public void addGast(GastModel gast) {
        gasten.put(gast.getID(), gast);
        refreshView();
    }

    public void removeGast(int gastId) {
        gasten.remove(gastId);
        refreshView();
    }

    // reacties op events

    // zet de nieuwe kamer in de lijst met kamers en print een confirmation message
    @Override
    public void onNewKamer(RuimteModel kamer) {
        KamerModel k = (KamerModel) kamer;

        kamers.put(k.getRoomNumber(), k);

        setKamerLeeg(k); // initieel leeg

        System.out.println("New room has been created");

        refreshView();
    }

    // zet de nieuwe gast in de lijst met gasten en print een confirmation message
    @Override
    public void onGastAangemaakt(GastModel gast) {
        addGast(gast);

        System.out.println("New Guest has been created");
    }

    // zet de kamer weer op leeg en haal de gast uit de lijst met gasten en print een confirmation message
    @Override
    public void onGastVertrokken(GastModel gast) {

        if (gast != null && gast.getKamer() != null) {
            KamerModel kamer = gast.getKamer();

            kamer.setBezet(false);
            kamer.setVerblijvende(null);

            setKamerLeeg(kamer);

            gast.setKamer(null);
        }

        if (gast != null) {
            removeGast(gast.getID());
        }

        System.out.println("Guest has left");
    }

    @Override
    public void onGastVerplaatst(GastModel gast, Locatie oudeLocatie) {

    }

    @Override
    public void onGastAangekomenInKamer(GastModel gast, Locatie behaaldeLocatie) {

    }

    @Override
    public void onGastGaatWegUitKamer(GastModel gast, Locatie oudeLocatie) {

    }

    public void refreshView() {
        view.tekenGastLijst(gasten);
        view.tekenKamerLijst(kamers);
    }

    @Override
    public void resetSimulatie() {
        this.gasten.clear();

        // maak alle kamers in het hotel weer leeg en beschikbaar
        for (KamerModel kamer : kamers.values()) {
            if (kamer != null) {
                setKamerLeeg(kamer);
            }
        }
        refreshView();
    }

    public HashMap<Integer, GastModel> getGasten() {
        return gasten;
    }

}