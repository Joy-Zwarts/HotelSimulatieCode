package Model.Ruimtes;

import Controller.Layout.Locatie;

public class LobbyModel extends RuimteModel {

    // attributes
    private Locatie middelLocatie;

    // constructor
    public LobbyModel(KamerType areaType, Locatie position, String dimension) {
        super(areaType, position, dimension);
        int width = getDimensionW();
        setMiddelLocatie(position.getX() + width/2, position.getY());
    }

    // getters en setters

    public void setMiddelLocatie(int X, int Y){
        middelLocatie = new Locatie(X,Y);
    }

    public Locatie getMiddelLocatie(){
        return middelLocatie;
    }
}
