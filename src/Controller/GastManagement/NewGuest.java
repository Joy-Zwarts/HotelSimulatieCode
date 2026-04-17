package Controller.GastManagement;

import Model.Personen.GastModel;

// notified listener als er een gast is aangemaakt
public interface NewGuest {
    void onGastAangemaakt(GastModel gast);
    void onGastVertrokken(GastModel gast);
}