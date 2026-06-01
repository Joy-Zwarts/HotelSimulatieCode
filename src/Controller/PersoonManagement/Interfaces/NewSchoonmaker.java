package Controller.PersoonManagement.Interfaces;

import Model.Layout.Locatie;
import Model.Personen.SchoonmakerModel;

public interface NewSchoonmaker {

    void onSchoonmakerAangemaakt(SchoonmakerModel schoonmaker);
    void onSchoonmakerVerplaatst(SchoonmakerModel schoonmaker, Locatie oudeLocatie);
    void onSchoonmakerAangekomenInKamer(SchoonmakerModel schoonmaker);
    void onSchoonmakerVerlaatKamer(SchoonmakerModel schoonmaker,  Locatie oudeLocatie);
}