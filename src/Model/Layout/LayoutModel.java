package Model.Layout;

import Controller.Layout.GridVakjeController;
import Model.Ruimtes.*;

import java.util.ArrayList;
import java.util.HashMap;

public class LayoutModel {

    // attributen

    private final ArrayList<RuimteModel> ruimtes;
    private final ArrayList<RuimteModel> verplichteElementen;
    private final HashMap<String, GridVakjeController> grid;

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
    public void addverplichteElementen(int gridLengte, int gridBreedte) {
        String schachtDimension = "1," + gridLengte/2;
        addKamerBuitenJson(KamerType.SCHACHT, "1,1", schachtDimension, gridLengte);
        String verdiepingLift = "1," + (gridLengte+1)/2;
        addKamerBuitenJson(KamerType.LIFT, verdiepingLift, "1,1", gridLengte);
        String schachtStart = "1," + (gridLengte/2 + 2);
        addKamerBuitenJson(KamerType.SCHACHT, schachtStart, schachtDimension, gridLengte);
        String trappenPosition = gridBreedte + ",1";
        String trappenDimension = "1," + gridLengte;
        addKamerBuitenJson(KamerType.TRAPPEN, trappenPosition, trappenDimension, gridLengte);
        String lobbyPosition = "2," + gridLengte;
        String lobbyDimension = gridLengte-3 + ",1";
        addKamerBuitenJson(KamerType.LOBBY, lobbyPosition, lobbyDimension, gridLengte);
    }

    // voeg toe aan de lijst
    public void addKamerBuitenJson(KamerType kamerType, String position, String dimension, int gridLengte) {
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

    public HashMap<String, GridVakjeController> getGrid() {
        return grid;
    }

    public ArrayList<RuimteModel> getRuimtes() {
        return ruimtes;
    }

    public ArrayList<RuimteModel> getVerplichteElementen() {
        return verplichteElementen;
    }
}
