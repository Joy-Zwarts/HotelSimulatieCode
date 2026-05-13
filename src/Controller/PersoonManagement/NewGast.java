package Controller.PersoonManagement;

import Model.Layout.Locatie;
import Model.Personen.GastModel;

// notified listener als er een gast is aangemaakt
public interface NewGast {
    void onGastAangemaakt(GastModel gast);
    void onGastVertrokken(GastModel gast);
    void onGastVerplaatst(GastModel gast, Locatie oudeLocatie);
    void onGastAangekomenInKamer(GastModel gast, Locatie behaaldeLocatie);
    void onGastGaatWegUitKamer(GastModel gast, Locatie oudeLocatie);
}