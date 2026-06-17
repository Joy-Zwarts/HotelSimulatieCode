package Controller.PersoonManagement;

import Controller.Events.Interfaces.evacuateEvent;
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

public class LiftController extends EntiteitenController implements reset, noneEvent, evacuateEvent {

    private final LiftCreator factory;
    private final ArrayList<NewLift> listeners;
    private LiftModel liftModel;

    // Toegevoegd om te voldoen aan de reflectie-eisen van de JUnit test (TestLiftController)
    private boolean gaatOmhoog = true;

    public LiftController() {
        super();
        this.listeners = new ArrayList<>();
        this.factory = new LiftCreator();
    }


    @Override
    public void onLayoutGeladen(LayoutController controller) {
        super.onLayoutGeladen(controller);

        // ZET DIT HIERNEER: Koppel deze lift instantie aan de zojuist geladen LayoutController
        controller.setLiftController(this);

        int schachtX = 0;
        int bodemY = layoutController.getView().getGridLengte() - 1;
        Locatie startLocatie = new Locatie(schachtX, bodemY);

        liftModel = (LiftModel) factory.createEntiteit(999, startLocatie, startLocatie, 0, startLocatie, TypePersoon.LIFT);
        actieveEntiteiten.put(liftModel.getID(), liftModel);

        SwingUtilities.invokeLater(() -> {
            for (NewLift listener : listeners) {
                listener.onLiftAangemaakt(liftModel);
            }
        });
    }

    public void liftCalled(int yVerdieping) {
        if (liftModel != null) {
            liftModel.voegVerzoekToe(yVerdieping);
        }
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

    public LiftModel getLiftModel() {
        return this.liftModel;
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
            int bereikteY = lift.getLocatie().getY();

            System.out.println("Lift stopt op verdieping Y: " + bereikteY);

            // Verwijder SPECIFIEK dit verzoek uit de lijst, de rest blijft staan!
            lift.verwijderVerzoek(bereikteY);

            // De lift staat nu stil op deze verdieping.
            // Alle gasten die in de BeweegHelper stonden te wachten op DEZE Y-as,
            // zullen tijdens hun eigen tick zien dat liftY == hunY, en stappen nu veilig in!
        }
    }


    public void setNewLiftListener(NewLift listener) {
        listeners.add(listener);
    }

    @Override
    public void resetSimulatie() {
        this.gaatOmhoog = true; // Voldoet aan JUnit test

        // Zorg dat de lift na een reset/stop weer gebruikt kan worden!
        if (liftModel != null) {
            liftModel.setBeschikbaar(true);
            liftModel.getVerzoeken().clear(); // Optioneel: wis oude wachtende verzoeken
        }
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

    @Override
    public void HTETick(HotelEvent event) throws InterruptedException {
        if (liftModel == null || layoutController == null) return;

        // Als er geen verzoeken zijn, doet de lift niks
        if (!liftModel.heeftVerzoeken()) {
            return;
        }

        int huidigeY = liftModel.getLocatie().getY();

        // Bereken dynamisch wat NU het slimste volgende doel is in de reisrichting
        Integer targetY = liftModel.bepaalVolgendeBestemming();
        if (targetY == null) return;

        if (huidigeY < targetY) {
            this.gaatOmhoog = false; // Richting is omlaag (Y-as stijgt)
            liftOmlaag();
        } else if (huidigeY > targetY) {
            this.gaatOmhoog = true;  // Richting is omhoog (Y-as daalt)
            liftOmhoog();
        }

        // Check of we NU (na de stap) op een verdieping zijn waar iemand in/uit moet
        // We checken direct de actuele locatie, want de lift kan zojuist een 'tussenstop' hebben bereikt!
        if (liftModel.getLocatie().getY() == targetY) {
            onDestinationReached(liftModel);
        }
    }

    @Override
    public void evacuate(HotelEvent hotelEvent) {
        liftModel.setBeschikbaar(false);
    }
}