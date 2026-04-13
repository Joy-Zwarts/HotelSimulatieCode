package Controller.PersoonFactory;


import Model.GastModel;
import Model.PersoonModel;
import Model.RoomClassificatie;

import java.util.Random;

public class GastCreator extends PersoonFactory {
    Random rand = new Random();
    RoomClassificatie wensen;

    @Override
    public PersoonModel createPersoon(int gastId, String targetlocatie, String locatie) {

        int randomNumber = rand.nextInt(5);

        switch (randomNumber) {
            case 0:
                wensen = RoomClassificatie.eenSter;
                break;
            case 1:
                wensen = RoomClassificatie.tweeSterren;
                break;
            case 2:
                wensen = RoomClassificatie.drieSterren;
                break;
            case 3:
                wensen = RoomClassificatie.vierSterren;
                break;
            case 4:
                wensen = RoomClassificatie.vijfSterren;
                break;
        }

        return new GastModel(gastId, null,null, wensen, null);
    }
}
