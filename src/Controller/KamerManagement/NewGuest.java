package Controller.KamerManagement;

import Model.Personen.GastModel;
import hotelevents.HotelEvent;

public interface NewGuest {
    void onGastAangemaakt(GastModel gast);
    void onGastVertrokken(int gastID);
}