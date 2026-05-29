package View.Systeem;

public interface settingsListener {
    void schoonmaakTijdVeranderd(TijdsDuur tijdsDuur);
    void filmDuurVeranderd(TijdsDuur tijdsDuur);
    void aantalSchoonmakersVeranderd(int aantalSchoonmakers);
    void restaurantCapaciteitVeranderd(int restaurantCapaciteit);
    void trapLoopDuurVeranderd(int  trapLoopDuur);
    void gastMaxWachttijdVeranderd(int gastMaxWachttijd);
}
