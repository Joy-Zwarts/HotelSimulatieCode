package Controller.GastManagement;

import Controller.Layout.Locatie;
import Model.Personen.GastModel;

// notified listener als er een gast is aangemaakt
public interface NewGuest {
    void onGastAangemaakt(GastModel gast);
    void onGastVertrokken(GastModel gast);
    void onGastVerplaatst(GastModel gast, Locatie oudeLocatie);
    void onGastAangekomenInKamer(GastModel gast, Locatie behaaldeLocatie);
    void onGastGaatWegUitKamer(GastModel gast, Locatie oudeLocatie);
}