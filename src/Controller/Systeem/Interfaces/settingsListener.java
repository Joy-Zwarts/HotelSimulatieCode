package Controller.Systeem.Interfaces;

import View.Systeem.TijdsDuur;

public interface settingsListener {
    void schoonmaakTijdVeranderd(TijdsDuur tijdsDuur);
    void filmDuurVeranderd(TijdsDuur tijdsDuur);
    void showFactuurBonnen(boolean bool);
    void restaurantCapaciteitVeranderd(int restaurantCapaciteit);
    void trapLoopDuurVeranderd(int  trapLoopDuur);
    void gastMaxWachttijdVeranderd(int gastMaxWachttijd);
}
