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

    private int gridBreedte;
    private int gridLengte;

    //constructor
    public LayoutModel() {
        ruimtes = new ArrayList<>();
        verplichteElementen = new ArrayList<>();
        grid = new HashMap<>();
    }

    public void addKamer(Ruimte ruimte) {
        ruimtes.add(ruimte);
    }

    public void berekenGridGrootte() {

        for (Ruimte ruimte : ruimtes) { // voor aantal ruimtes in de lijst

            int right = ruimte.getPositionX() + ruimte.getDimensionW() - 1; // positie x plus hoe wijd de kamer is (-1 voor de x positie die al is meegerekend)
            int bottom = ruimte.getPositionY() + ruimte.getDimensionH() - 1; // positie y plus hoe hoog de kamer is (-1 voor de y positie die al is meegerekend)

            if (right > gridBreedte) { //zoek de meest rechtse positie+wijdte
                gridBreedte = right; // dat is nu de breedte
            }
            if (bottom > gridLengte) { //zoek de meest onderste positie+hoogte
                gridLengte = bottom; // dat is nu de lengte
            }
        }
        gridBreedte = gridBreedte +2; // 2 extra kolommen voor de trap en liftschacht
        gridLengte = gridLengte +1; // 1 extra rij voor de lobby
    }

    public void addverplichteElementen() {
        String schachtDimension = "1," + gridLengte/2;
        addKamerBuitenJson("Model.Schacht", "1,1", schachtDimension);
        String verdiepingLift = "1," + (gridLengte+1)/2;
        addKamerBuitenJson("Model.Lift", verdiepingLift, "1,1");
        String schachtStart = "1," + (gridLengte/2 + 2);
        addKamerBuitenJson("Model.Schacht", schachtStart, schachtDimension);
        String trappenPosition = gridBreedte + ",1";
        String trappenDimension = "1," + gridLengte;
        addKamerBuitenJson("Trappen", trappenPosition, trappenDimension);
        String lobbyPosition = "2," + gridLengte;
        String lobbyDimension = gridLengte-3 + ",1";
        addKamerBuitenJson("Model.Lobby", lobbyPosition, lobbyDimension);
    }

    public void addKamerBuitenJson(String AreaType, String position, String dimension){
        switch (AreaType) {
            case "Model.Lift":
                verplichteElementen.add(new Lift(AreaType, position, dimension, (gridLengte + 1) / 2, true));
                break;
            case "Model.Schacht":
                verplichteElementen.add(new Schacht(AreaType, position, dimension));
                break;
            case "Trappen":
                verplichteElementen.add(new Trappenhuis(AreaType, position, dimension));
                break;
            case "Model.Lobby":
                verplichteElementen.add(new Lobby(AreaType, position, dimension));
                break;
        }
    }

    public GridVakje getGridVakje(int x, int y) {
        String coordinaten = x+","+y;
        return grid.get(coordinaten);
    }

    public int getGridBreedte() {
        return gridBreedte;
    }

    public int getGridLengte() {
        return gridLengte;
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
