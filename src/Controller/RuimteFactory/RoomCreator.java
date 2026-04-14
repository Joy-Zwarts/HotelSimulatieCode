package Controller.RuimteFactory;

import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RoomClassificatie;
import Model.Ruimtes.RuimteModel;

public class RoomCreator extends RuimteFactory {

    private int roomNumber = 0;
    @Override
    public RuimteModel createRuimte(String position, String dimension, long capacity, String classification) {
        roomNumber++;

        switch (classification) {
            case "1 Star":
                return new KamerModel(KamerType.ROOM, position, dimension, RoomClassificatie.eenSter, roomNumber, false);
            case "2 Star":
                return new KamerModel(KamerType.ROOM, position, dimension, RoomClassificatie.tweeSterren, roomNumber, false);
            case "3 Star":
                return new KamerModel(KamerType.ROOM, position, dimension, RoomClassificatie.drieSterren, roomNumber, false);
            case "4 Star":
                return new KamerModel(KamerType.ROOM, position, dimension, RoomClassificatie.vierSterren, roomNumber, false);
            case "5 Star":
                return new KamerModel(KamerType.ROOM, position, dimension, RoomClassificatie.vijfSterren, roomNumber, false);
            default:
                throw new IllegalArgumentException("Unknown classification");
        }
    }
}