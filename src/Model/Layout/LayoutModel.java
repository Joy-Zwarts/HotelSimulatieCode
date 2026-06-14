package Model.Layout;

import Controller.Layout.GridVakjeController;
import Controller.PersoonFactory.LiftCreator;
import Controller.RuimteFactory.LobbyCreator;
import Controller.RuimteFactory.SchachtCreator;
import Controller.RuimteFactory.TrappenhuisCreator;
import Model.Ruimtes.*;

import java.util.ArrayList;
import java.util.HashMap;

public class LayoutModel {

    // attributen

    private final ArrayList<RuimteModel> ruimtes;
    private final ArrayList<RuimteModel> verplichteElementen;
    private final HashMap<Locatie, GridVakjeController> grid;
    private int vakBreedte = 118;
    private int vakHoogte = 59;

    private final LiftCreator liftCreator;
    private final SchachtCreator schachtCreator;
    private final TrappenhuisCreator trappenhuisCreator;
    private final LobbyCreator lobbyCreator;

    //constructor
    public LayoutModel() {
        ruimtes = new ArrayList<>();
        verplichteElementen = new ArrayList<>();
        grid = new HashMap<>();

        this.liftCreator = new LiftCreator();
        this.schachtCreator = new SchachtCreator();
        this.trappenhuisCreator = new TrappenhuisCreator();
        this.lobbyCreator = new LobbyCreator();
    }

    // voeg kamer toe aan lijst van kamers
    public void addRuimte(RuimteModel ruimte) {
        ruimtes.add(ruimte);
    }

    // add verplichte elementen (schacht, lift, trappen & lobby)
    public void addVerplichteElementen(int gridLengte, int gridBreedte) {

        // lift en schachten
        int liftY = gridLengte / 2;
        if (liftY < 1) liftY = 1;
        if (liftY > 1) {
            addKamerBuitenJson(KamerType.SCHACHT, new Locatie(1, 1), "1," + gridLengte, gridLengte);
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
            case KamerType.SCHACHT:
                // gebruik de schachtCreator
                RuimteModel schacht = schachtCreator.createRuimte(position, dimension, 0, null);
                verplichteElementen.add(schacht);
                break;

            case KamerType.TRAPPEN:
                // gebruik de trappenhuisCreator
                RuimteModel trap = trappenhuisCreator.createRuimte(position, dimension, 0, null);
                verplichteElementen.add(trap);
                break;

            case KamerType.LOBBY:
                // gebruik de lobbyCreator
                RuimteModel lobby = lobbyCreator.createRuimte(position, dimension, 0, null);
                verplichteElementen.add(lobby);
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

    public RuimteModel getRuimteBijLocatie(Locatie loc) {
        Locatie locatie = new Locatie(loc.getX(), loc.getY()+1);
        for (RuimteModel ruimte : ruimtes) {
            if (ruimte.getPosition().equals(locatie)) {
                return ruimte;
            }
        }

        for (RuimteModel ruimte : verplichteElementen) {
            if (ruimte.getPosition().equals(locatie)) {
                return ruimte;
            }
        }

        return null;
    }

    public void setVakBreedte(int i) {
        this.vakBreedte = i;
    }

    public void setVakHoogte(int i) {
        this.vakHoogte = i;
    }
}
