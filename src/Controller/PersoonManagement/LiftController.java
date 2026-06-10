package Controller.PersoonManagement;

import Controller.Layout.LayoutController;
import Controller.PersoonFactory.LiftCreator;
import Controller.PersoonManagement.Interfaces.NewLift;
import Controller.Systeem.Interfaces.onTimeChange;
import Controller.Systeem.Interfaces.reset;
import Model.Layout.Locatie;
import Model.Personen.LiftModel;
import Model.Personen.PersoonModel;
import Model.Personen.TypePersoon;
import View.Systeem.TijdsDuur;
import hotelevents.HotelEvent;

import javax.swing.SwingUtilities;
import java.util.ArrayList;

public class LiftController extends PersoonController implements onTimeChange, reset { // Voeg hier straks je liftEvent interface aan toe!

    private final LiftCreator factory;
    private final ArrayList<NewLift> listeners;
    private LiftModel deLift;

    public LiftController() {
        super();
        this.listeners = new ArrayList<>();
        this.factory = new LiftCreator();
    }

    // Plaats de lift zodra de layout geladen is
    @Override
    public void onLayoutGeladen(LayoutController controller) {
        super.onLayoutGeladen(controller);

      //waar staat de lift
        int schachtX = 0;
        int bodemY = layoutController.getView().getGridLengte() - 1;
        Locatie startLocatie = new Locatie(schachtX, bodemY);

        // Maak de lift aan via de factory
        deLift = (LiftModel) factory.createPersoon(999, startLocatie, startLocatie, 0, startLocatie, TypePersoon.LIFT);

        actievePersonen.put(deLift.getID(), deLift);

        // Laat de listeners weten dat de lift getekend moet worden
        SwingUtilities.invokeLater(() -> {
            for (NewLift listener : listeners) {
                listener.onLiftAangemaakt(deLift);
            }
        });
    }

    // Per stap die de lift zet, de PlaatsHelper aansturen
    @Override
    public void onStepTaken(PersoonModel persoon, Locatie oudeLocatie) {
        if (persoon instanceof LiftModel) {
            LiftModel lift = (LiftModel) persoon;
            SwingUtilities.invokeLater(() -> {
                for (NewLift listener : listeners) {
                    listener.onLiftVerplaatst(lift, oudeLocatie);
                }
            });
        }
    }

    // Als de lift arriveert op de verdieping waar hij heen moest
    @Override
    public void onDestinationReached(PersoonModel persoon) {
        if (persoon instanceof LiftModel) {
            LiftModel lift = (LiftModel) persoon;
            System.out.println("Lift is aangekomen op bestemming: " + lift.getLocatie());
            // hier moet er nog komen te staan wat de gasten moeten doen: in en uitstappen
        }
    }


    public void setNewLiftListener(NewLift listener) {
        listeners.add(listener);
    }

    @Override
    public void timeChange(int HTE) {

    }

    @Override
    public void resetSimulatie() {

    }

    @Override
    public void schoonmaakTijdVeranderd(TijdsDuur tijdsDuur) {

    }

    @Override
    public void filmDuurVeranderd(TijdsDuur tijdsDuur) {

    }

    @Override
    public void aantalSchoonmakersVeranderd(int aantalSchoonmakers) {

    }

    @Override
    public void restaurantCapaciteitVeranderd(int restaurantCapaciteit) {

    }

    @Override
    public void gastMaxWachttijdVeranderd(int gastMaxWachttijd) {

    }
}
