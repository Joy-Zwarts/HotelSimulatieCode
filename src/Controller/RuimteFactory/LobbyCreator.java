package Controller.RuimteFactory;

import Model.Layout.Locatie;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.LobbyModel;
import Model.Ruimtes.RuimteModel;

public class LobbyCreator extends  RuimteFactory{
    @Override
    public RuimteModel createRuimte(Locatie position, String dimension, long capacity, String classification) {
        return new LobbyModel(KamerType.LOBBY, position, dimension);
    }
}
