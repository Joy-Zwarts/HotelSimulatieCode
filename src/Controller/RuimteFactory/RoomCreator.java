package Controller.RuimteFactory;

import Model.KamerModel;
import Model.RoomClassificatie;
import Model.RuimteModel;

public class RoomCreator extends RuimteFactory {

    private int roomNumber = 0;

    @Override
    public RuimteModel createRuimte(String areaType, String position, String dimension, long capacity, String classification) {
        roomNumber++;

        switch (classification) {
            case "1 Star":
                return new KamerModel(areaType, position, dimension, RoomClassificatie.eenSter, roomNumber, false);
            case "2 Star":
                return new KamerModel(areaType, position, dimension, RoomClassificatie.tweeSterren, roomNumber, false);
            case "3 Star":
                return new KamerModel(areaType, position, dimension, RoomClassificatie.drieSterren, roomNumber, false);
            case "4 Star":
                return new KamerModel(areaType, position, dimension, RoomClassificatie.vierSterren, roomNumber, false);
            case "5 Star":
                return new KamerModel(areaType, position, dimension, RoomClassificatie.vijfSterren, roomNumber, false);
            default:
                throw new IllegalArgumentException("Unknown classification");
        }
    }
}