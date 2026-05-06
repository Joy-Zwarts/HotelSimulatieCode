package Controller.RuimteFactory;

import Model.Layout.Locatie;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.KamerClassificatie;
import Model.Ruimtes.RuimteModel;

public class KamerCreator extends RuimteFactory {

    // maakt een nieuwe kamer aan gebaseerd op de meegekregen data
    @Override
    public RuimteModel createRuimte(Locatie position, String dimension, long capacity, String classification) {
        switch (classification) {
            case "1 Star":
                return new KamerModel(KamerType.ROOM, position, dimension, KamerClassificatie.eenSter, false);
            case "2 Star":
                return new KamerModel(KamerType.ROOM, position, dimension, KamerClassificatie.tweeSterren, false);
            case "3 Star":
                return new KamerModel(KamerType.ROOM, position, dimension, KamerClassificatie.drieSterren, false);
            case "4 Star":
                return new KamerModel(KamerType.ROOM, position, dimension, KamerClassificatie.vierSterren, false);
            case "5 Star":
                return new KamerModel(KamerType.ROOM, position, dimension, KamerClassificatie.vijfSterren, false);
            default:
                throw new IllegalArgumentException("Unknown classification");
        }
    }
}