package Controller.PersoonFactory;


import Model.Personen.GastModel;
import Model.Personen.PersoonModel;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RoomClassificatie;

public class GastCreator extends PersoonFactory {

    private RoomClassificatie wensen;

    @Override
    public PersoonModel createPersoon(int gastId, String targetlocatie, String locatie, int Wensen) {
        switch (Wensen) {
            case 1:
                wensen = RoomClassificatie.eenSter;
                break;
            case 2:
                wensen = RoomClassificatie.tweeSterren;
                break;
            case 3:
                wensen = RoomClassificatie.drieSterren;
                break;
            case 4:
                wensen = RoomClassificatie.vierSterren;
                break;
            case 5:
                wensen = RoomClassificatie.vijfSterren;
                break;
        }

        return new GastModel(gastId, null,null, wensen, null);
    }
}
