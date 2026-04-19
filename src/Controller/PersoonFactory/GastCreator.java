package Controller.PersoonFactory;


import Controller.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Personen.PersoonModel;
import Model.Ruimtes.RoomClassificatie;

public class GastCreator extends PersoonFactory {
    private RoomClassificatie wensen;

    // maakt gast aan gebaseerd op de meegekregen data
    @Override
    public PersoonModel createPersoon(int gastId, Locatie targetlocatie, Locatie locatie, int Wensen) {
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

        // return de nieuwe gast
        return new GastModel(gastId, locatie, targetlocatie, wensen, null);
    }
}
