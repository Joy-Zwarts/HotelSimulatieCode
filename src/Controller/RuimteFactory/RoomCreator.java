package Controller.RuimteFactory;

import Model.KamerModel;
import Model.RoomClassificatie;
import Model.RuimteModel;

public class RoomCreator extends RuimteFactory {

    @Override
    public RuimteModel createRuimte(String areaType, String position, String dimension, long capacity, String classification) {

        switch (classification) {
            case "1 Star":
                return new KamerModel(areaType, position, dimension, RoomClassificatie.eenSter);
            case "2 Star":
                return new KamerModel(areaType, position, dimension, RoomClassificatie.tweeSterren);
            case "3 Star":
                return new KamerModel(areaType, position, dimension, RoomClassificatie.drieSterren);
            case "4 Star":
                return new KamerModel(areaType, position, dimension, RoomClassificatie.vierSterren);
            case "5 Star":
                return new KamerModel(areaType, position, dimension, RoomClassificatie.vijfSterren);
            default:
                throw new IllegalArgumentException("Unknown classification");
        }
    }
}