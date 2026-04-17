package Model.Layout;

import Controller.Layout.GridVakjeController;
import Controller.Layout.Locatie;
import Model.Ruimtes.*;

import java.util.ArrayList;
import java.util.HashMap;

public class LayoutModel {

    // attributen

    private final ArrayList<RuimteModel> ruimtes;
    private final ArrayList<RuimteModel> verplichteElementen;
    private final HashMap<Locatie, GridVakjeController> grid;

    //constructor
    public LayoutModel() {
        ruimtes = new ArrayList<>();
        verplichteElementen = new ArrayList<>();
        grid = new HashMap<>();
    }

    // voeg kamer toe aan lijst van kamers
    public void addKamer(RuimteModel ruimte) {
        ruimtes.add(ruimte);
    }

    // add verplichte elementen (schacht, lift, trappen & lobby)
    public void addVerplichteElementen(int gridLengte, int gridBreedte) {
        String schachtDimension = "1," + gridLengte/2;
        Locatie schacht1Pos = new Locatie(1,1);
        addKamerBuitenJson(KamerType.SCHACHT, schacht1Pos, schachtDimension, gridLengte);
        int verdiepingLiftY = (gridLengte+1)/2;
        Locatie liftPos = new Locatie(1,verdiepingLiftY);
        addKamerBuitenJson(KamerType.LIFT, liftPos, "1,1", gridLengte);
        int schachtStart = (gridLengte/2 + 2);
        Locatie schacht2Pos = new Locatie(1,schachtStart);
        addKamerBuitenJson(KamerType.SCHACHT, schacht2Pos, schachtDimension, gridLengte);
        String trappenDimension = "1," + gridLengte;
        Locatie trappenPos = new Locatie(gridBreedte,1);
        addKamerBuitenJson(KamerType.TRAPPEN, trappenPos, trappenDimension, gridLengte);
        String lobbyDimension = gridLengte-3 + ",1";
        Locatie lobbyPos = new Locatie(2,gridLengte);
        addKamerBuitenJson(KamerType.LOBBY, lobbyPos, lobbyDimension, gridLengte);
    }

    // voeg toe aan de lijst
    public void addKamerBuitenJson(KamerType kamerType, Locatie position, String dimension, int gridLengte) {
        switch (kamerType) {
            case KamerType.LIFT:
                verplichteElementen.add(new LiftModel(kamerType, position, dimension, (gridLengte + 1) / 2, true));
                break;
            case KamerType.SCHACHT:
                verplichteElementen.add(new SchachtModel(kamerType, position, dimension));
                break;
            case KamerType.TRAPPEN:
                verplichteElementen.add(new TrappenhuisModel(kamerType, position, dimension));
                break;
            case KamerType.LOBBY:
                verplichteElementen.add(new LobbyModel(kamerType, position, dimension));
                break;
        }
    }

    // getters & setters

    public int getVakBreedte() {
        return 120; // voor nu nog hardcoded
    }

    public int getVakHoogte() {
        return 60; // voor nu nog hardcoded
    }

    public HashMap<Locatie, GridVakjeController> getGrid() {
        return grid;
    }

    public ArrayList<RuimteModel> getRuimtes() {
        return ruimtes;
    }

    public ArrayList<RuimteModel> getVerplichteElementen() {
        return verplichteElementen;
    }
}
