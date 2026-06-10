package Controller.PersoonManagement;

import Controller.Layout.LayoutController;
import Controller.PersoonFactory.LiftCreator;
import Controller.PersoonManagement.Interfaces.NewLift;
import Controller.Systeem.Interfaces.onTimeChange;
import Controller.Systeem.Interfaces.reset;
import Model.Layout.Locatie;
import Model.Personen.EntiteitenModel;
import Model.Personen.LiftModel;
import Model.Personen.TypePersoon;
import View.Systeem.TijdsDuur;

import javax.swing.SwingUtilities;
import java.util.ArrayList;

public class LiftController extends EntiteitenController implements onTimeChange, reset { // Voeg hier straks je liftEvent interface aan toe!

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
        deLift = (LiftModel) factory.createEntiteit(999, startLocatie, startLocatie, 0, startLocatie, TypePersoon.LIFT);

        actieveEntiteiten.put(deLift.getID(), deLift);

        // Laat de listeners weten dat de lift getekend moet worden
        SwingUtilities.invokeLater(() -> {
            for (NewLift listener : listeners) {
                listener.onLiftAangemaakt(deLift);
            }
        });
    }

    public void liftOmhoog() {
        if (deLift != null) {
            // 1. Sla de huidige (oude) locatie op voor de listeners
            Locatie oudeLocatie = new Locatie(deLift.getLocatie().getX(), deLift.getLocatie().getY());

            // 2. Bereken de nieuwe Y-waarde (omhoog = Y wordt kleiner)
            int nieuweY = deLift.getLocatie().getY() - 1;

            // 3. Zorg dat we niet boven het grid uitvliegen (veiligheidscheck)
            if (nieuweY >= 0) {
                deLift.getLocatie().setY(nieuweY);

                // 4. Trigger de methode die de View een seintje geeft
                onStepTaken(deLift, oudeLocatie);
            }
        }
    }

    public void liftOmlaag() {
        if (deLift != null && layoutController != null) {
            // 1. Sla de huidige (oude) locatie op
            Locatie oudeLocatie = new Locatie(deLift.getLocatie().getX(), deLift.getLocatie().getY());

            // 2. Bereken de nieuwe Y-waarde (omlaag = Y wordt groter)
            int nieuweY = deLift.getLocatie().getY() + 1;
            int maxBodemY = layoutController.getView().getGridLengte() - 1;

            // 3. Zorg dat we niet door de bodem zakken
            if (nieuweY <= maxBodemY) {
                deLift.getLocatie().setY(nieuweY);

                // 4. Trigger de methode die de View een seintje geeft
                onStepTaken(deLift, oudeLocatie);
            }
        }
    }

    public void liftCalled() {
    }

    // Per stap die de lift zet, de PlaatsHelper aansturen
    @Override
    public void onStepTaken(EntiteitenModel Entiteit, Locatie oudeLocatie) {
        if (Entiteit instanceof LiftModel) {
            LiftModel lift = (LiftModel) Entiteit;
            SwingUtilities.invokeLater(() -> {
                for (NewLift listener : listeners) {
                    listener.onLiftVerplaatst(lift, oudeLocatie);
                }
            });
        }
    }

    // Als de lift arriveert op de verdieping waar hij heen moest
    @Override
    public void onDestinationReached(EntiteitenModel Entiteit) {
        if (Entiteit instanceof LiftModel) {
            LiftModel lift = (LiftModel) Entiteit;
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
