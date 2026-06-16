package Controller.PersoonManagement;

import Controller.Events.Interfaces.noneEvent;
import Controller.Layout.LayoutController;
import Controller.PersoonFactory.LiftCreator;
import Controller.PersoonManagement.Interfaces.NewLift;
import Controller.Systeem.Interfaces.onTimeChange;
import Controller.Systeem.Interfaces.reset;
import Model.Layout.Locatie;
import Model.Entiteiten.EntiteitenModel;
import Model.Entiteiten.LiftModel;
import Model.Entiteiten.TypePersoon;
import View.Systeem.TijdsDuur;
import hotelevents.HotelEvent;

import javax.swing.SwingUtilities;
import java.util.ArrayList;

public class LiftController extends EntiteitenController implements reset, noneEvent {

    private final LiftCreator factory;
    private final ArrayList<NewLift> listeners;
    private LiftModel liftModel;
    private boolean gaatOmhoog = true;

    public LiftController() {
        super();
        this.listeners = new ArrayList<>();
        this.factory = new LiftCreator();
    }


    // plaats de lift zodra de layout geladen is
    @Override
    public void onLayoutGeladen(LayoutController controller) {
        super.onLayoutGeladen(controller);

        // waar staat de lift
        int schachtX = 0;
        int bodemY = layoutController.getView().getGridLengte() - 1;
        Locatie startLocatie = new Locatie(schachtX, bodemY);

        // maak de lift aan via de factory
        liftModel = (LiftModel) factory.createEntiteit(999, startLocatie, startLocatie, 0, startLocatie, TypePersoon.LIFT);

        actieveEntiteiten.put(liftModel.getID(), liftModel);

        // laat de listeners weten dat de lift getekend moet worden
        SwingUtilities.invokeLater(() -> {
            for (NewLift listener : listeners) {
                listener.onLiftAangemaakt(liftModel);
            }
        });
    }

    public void liftOmhoog() {
        if (liftModel != null) {
            // sla de huidige (oude) locatie op voor de listeners
            Locatie oudeLocatie = new Locatie(liftModel.getLocatie().getX(), liftModel.getLocatie().getY());

            // bereken de nieuwe Y-waarde (omhoog = y wordt kleiner)
            int nieuweY = liftModel.getLocatie().getY() - 1;

            // zorg dat het niet boven het grid uitkomt
            if (nieuweY >= 0) {
                liftModel.getLocatie().setY(nieuweY);
                onStepTaken(liftModel, oudeLocatie);
            }
        }
    }

    public void liftOmlaag() {
        if (liftModel != null && layoutController != null) {
            // sla de huidige (oude) locatie op
            Locatie oudeLocatie = new Locatie(liftModel.getLocatie().getX(), liftModel.getLocatie().getY());

            // bereken de nieuwe Y-waarde (omlaag = y wordt groter)
            int nieuweY = liftModel.getLocatie().getY() + 1;
            int maxBodemY = layoutController.getView().getGridLengte() - 1;

            // zorg dat de lift niet lager kan dan de begane grond
            if (nieuweY <= maxBodemY) {
                liftModel.getLocatie().setY(nieuweY);
                onStepTaken(liftModel, oudeLocatie);
            }
        }
    }

    public void liftCalled() {
    }

    // per stap die de lift zet, laat de PlaatsHelper hem tekenen
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

    // als de lift arriveert op de verdieping waar hij heen moest
    @Override
    public void onDestinationReached(EntiteitenModel Entiteit) {
        if (Entiteit instanceof LiftModel) {
            LiftModel lift = (LiftModel) Entiteit;
            System.out.println("Lift is aangekomen op bestemming: " + lift.getLocatie());
            // hier moet er nog komen te staan wat de gasten moeten doen
        }
    }


    public void setNewLiftListener(NewLift listener) {
        listeners.add(listener);
    }

    @Override
    public void resetSimulatie() {
        gaatOmhoog = true;
    }

    @Override
    public void schoonmaakTijdVeranderd(TijdsDuur tijdsDuur) {

    }

    @Override
    public void filmDuurVeranderd(TijdsDuur tijdsDuur) {

    }

    @Override
    public void showFactuurBonnen(boolean bool) {

    }

    @Override
    public void restaurantCapaciteitVeranderd(int restaurantCapaciteit) {

    }

    @Override
    public void gastMaxWachttijdVeranderd(int gastMaxWachttijd) {

    }

    // elke tick is een stap omhoog of omlaag zodat de lift de hele tijd omhoog en omlaag gaat
    @Override
    public void HTETick(HotelEvent event) throws InterruptedException {
        if (liftModel == null || layoutController == null) return;

        int maxBodemY = layoutController.getView().getGridLengte() - 1;
        int huidigeY = liftModel.getLocatie().getY();

        // elke tick is 1 stap omhoog of omlaag
        if (gaatOmhoog) {
            if (huidigeY > 0) {
                liftOmhoog();
            } else {
                // top bereikt (y=0), draai om en zet de eerste stap omlaag
                gaatOmhoog = false;
                liftOmlaag();
            }
        } else {
            if (huidigeY < maxBodemY) {
                liftOmlaag();
            } else {
                // bodem bereikt, draai om en zet de eerste stap omhoog
                gaatOmhoog = true;
                liftOmhoog();
            }
        }
    }
}