package Controller.GastManagement;

import Model.Personen.GastModel;

import java.awt.event.ActionListener;

public class GastPlaatser implements NewGuest {
    @Override
    public void onGastAangemaakt(GastModel gast) {
        gast.getKamer().getRoomNumber();
    }

    @Override
    public void onGastVertrokken(int gastID) {

    }
}
