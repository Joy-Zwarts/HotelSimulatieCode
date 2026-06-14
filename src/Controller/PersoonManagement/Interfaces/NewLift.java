package Controller.PersoonManagement.Interfaces;

import Model.Layout.Locatie;
import Model.Entiteiten.LiftModel;

public interface NewLift {
    void onLiftAangemaakt(LiftModel lift);
    void onLiftVerplaatst(LiftModel lift, Locatie oudeLocatie);
}