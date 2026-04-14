package Controller.KamerManagement;

import Model.Personen.GastModel;

public interface NewGuest {
    void onGastAangemaakt(GastModel gast);
    void onGastVertrokken(int gastID);
}