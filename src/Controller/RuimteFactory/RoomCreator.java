package Controller.RuimteFactory;

import Model.Layout.Locatie;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RoomClassificatie;
import Model.Ruimtes.RuimteModel;

public class RoomCreator extends RuimteFactory {

    // maakt een nieuwe kamer aan gebaseerd op de meegekregen data
    @Override
    public RuimteModel createRuimte(Locatie position, String dimension, long capacity, String classification) {
        switch (classification) {
            case "1 Star":
                return new KamerModel(KamerType.ROOM, position, dimension, RoomClassificatie.eenSter, false);
            case "2 Star":
                return new KamerModel(KamerType.ROOM, position, dimension, RoomClassificatie.tweeSterren, false);
            case "3 Star":
                return new KamerModel(KamerType.ROOM, position, dimension, RoomClassificatie.drieSterren, false);
            case "4 Star":
                return new KamerModel(KamerType.ROOM, position, dimension, RoomClassificatie.vierSterren, false);
            case "5 Star":
                return new KamerModel(KamerType.ROOM, position, dimension, RoomClassificatie.vijfSterren, false);
            default:
                throw new IllegalArgumentException("Unknown classification");
        }
    }
}