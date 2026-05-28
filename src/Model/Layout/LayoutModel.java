package Model.Layout;

import Controller.Layout.GridVakjeController;
import Model.Ruimtes.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LayoutModel {

    // attributen

    private final ArrayList<RuimteModel> ruimtes;
    private final ArrayList<RuimteModel> verplichteElementen;
    private final HashMap<Locatie, GridVakjeController> grid;
    private int vakBreedte = 118;
    private int vakHoogte = 59;

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

        // lift en schachten
        int liftY = gridLengte / 2;
        if (liftY < 1) liftY = 1;
        if (liftY > 1) {
            addKamerBuitenJson(KamerType.SCHACHT, new Locatie(1, 1), "1," + (liftY - 1), gridLengte);
        }

        addKamerBuitenJson(KamerType.LIFT, new Locatie(1, liftY), "1,1", gridLengte);

        int ondersteSchachtHoogte = gridLengte - liftY;
        if (ondersteSchachtHoogte > 0) {
            addKamerBuitenJson(KamerType.SCHACHT, new Locatie(1, liftY + 1), "1," + ondersteSchachtHoogte, gridLengte);
        }

        // trappenhuis
        addKamerBuitenJson(KamerType.TRAPPEN, new Locatie(gridBreedte, 1), "1," + gridLengte, gridLengte);


        // lobby
        int lobbyBreedte = gridBreedte - 2;

        if (lobbyBreedte > 0) {
            String lobbyDimension = lobbyBreedte + ",1";
            Locatie lobbyPos = new Locatie(2, gridLengte);
            addKamerBuitenJson(KamerType.LOBBY, lobbyPos, lobbyDimension, gridLengte);
        }
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
        return vakBreedte;
    }

    public int getVakHoogte() {
        return vakHoogte;
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

    public void setVakDimensies(int dynamischeBreedte, int dynamischeHoogte) {
        this.vakBreedte = dynamischeBreedte;
        this.vakHoogte = dynamischeHoogte;
    }
}
