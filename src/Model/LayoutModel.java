package Model;

import View.LayoutView;

import java.util.ArrayList;
import java.util.HashMap;

public class LayoutModel {

    private ArrayList<Ruimte> ruimtes;

    private  ArrayList<Ruimte> verplichteElementen;

    private HashMap<String, GridVakje> grid;

    private int vakHoogte = 60;
    private int vakBreedte = 120;

    //constructor
    public LayoutModel() {
        ruimtes = new ArrayList<>();
        verplichteElementen = new ArrayList<>();
        grid = new HashMap<>();
    }

    public void addKamer(Ruimte ruimte) {
        ruimtes.add(ruimte);
    }

    public void addverplichteElementen(int gridLengte, int gridBreedte) {
        String schachtDimension = "1," + gridLengte/2;
        addKamerBuitenJson("Schacht", "1,1", schachtDimension, gridLengte);
        String verdiepingLift = "1," + (gridLengte+1)/2;
        addKamerBuitenJson("Lift", verdiepingLift, "1,1", gridLengte);
        String schachtStart = "1," + (gridLengte/2 + 2);
        addKamerBuitenJson("Schacht", schachtStart, schachtDimension, gridLengte);
        String trappenPosition = gridBreedte + ",1";
        String trappenDimension = "1," + gridLengte;
        addKamerBuitenJson("Trappen", trappenPosition, trappenDimension, gridLengte);
        String lobbyPosition = "2," + gridLengte;
        String lobbyDimension = gridLengte-3 + ",1";
        addKamerBuitenJson("Lobby", lobbyPosition, lobbyDimension, gridLengte);
    }

    public void addKamerBuitenJson(String AreaType, String position, String dimension, int gridLengte) {
        switch (AreaType) {
            case "Lift":
                verplichteElementen.add(new Lift(AreaType, position, dimension, (gridLengte + 1) / 2, true));
                break;
            case "Schacht":
                verplichteElementen.add(new Schacht(AreaType, position, dimension));
                break;
            case "Trappen":
                verplichteElementen.add(new Trappenhuis(AreaType, position, dimension));
                break;
            case "Lobby":
                verplichteElementen.add(new Lobby(AreaType, position, dimension));
                break;
        }
    }

    public GridVakje getGridVakje(int x, int y) {
        String coordinaten = x+","+y;
        return grid.get(coordinaten);
    }

    public int getVakBreedte() {
        return vakBreedte;
    }

    public int getVakHoogte() {
        return vakHoogte;
    }

    public HashMap<String, GridVakje> getGrid() {
        return grid;
    }

    public ArrayList<Ruimte> getRuimtes() {
        return ruimtes;
    }

    public ArrayList<Ruimte> getVerplichteElementen() {
        return verplichteElementen;
    }
}
