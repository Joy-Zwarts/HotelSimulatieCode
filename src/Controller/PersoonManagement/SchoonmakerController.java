package Controller.PersoonManagement;

import Controller.Events.cleaningEmergencyEvent;
import Controller.Layout.LayoutController;
import Model.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Personen.PersoonModel;
import Model.Personen.SchoonmakerModel;
import Controller.PersoonFactory.SchoonmakerCreator;
import hotelevents.HotelEvent;

import java.util.ArrayList;

public class SchoonmakerController extends PersoonController implements cleaningEmergencyEvent {

    // attributen

    private final SchoonmakerCreator factory;
    private final ArrayList<NewSchoonmaker> listeners;
    private SchoonmakerModel schoonmaker1;
    private SchoonmakerModel schoonmaker2;
    private final ReceptieController receptieController;

    // constructor
    public SchoonmakerController(ReceptieController rec) {
        super();
        this.receptieController = rec;
        this.listeners = new ArrayList<>();
        this.factory = new SchoonmakerCreator();
    }

    // om een klasse zich te laten abonneren op deze klasse voor als er een nieuwe schoonmaker wordt aangemaakt
    public void setNewSchoonmakerListener(NewSchoonmaker listener) {
        listeners.add(listener);
    }

    // als de layout is geladen worden de 2 schoonmakers per hotel aangemaakt en op de juiste plek geplaatst (1 voor boven 1 voor onder)
    @Override
    public void onLayoutGeladen(LayoutController controller) {

        super.onLayoutGeladen(controller);

        int breedte = layoutController.getView().getGridBreedte();

        int lengte = layoutController.getView().getGridLengte();

        schoonmaker1 = (SchoonmakerModel) factory.createPersoon(1, new Locatie(breedte / 2, 0), null, 0);

        schoonmaker2 = (SchoonmakerModel) factory.createPersoon(2, new Locatie(breedte / 2, lengte / 2), null, 0);

        actievePersonen.put(schoonmaker1.getID(), schoonmaker1);
        actievePersonen.put(schoonmaker2.getID(), schoonmaker2);

        // stuur een ping naar de listeners
        for (NewSchoonmaker listener : listeners) {
            listener.onSchoonmakerAangemaakt(schoonmaker1);
            listener.onSchoonmakerAangemaakt(schoonmaker2);
        }
    }

    // stuur de juiste schoonmaker naar de cleaning emergency
    @Override
    public void cleaningEmergencyEvent(HotelEvent hotelEvent) {

        // zoek de locatie op voor de gast waarbij de cleaning emergency is gemeld
        Locatie target = new Locatie(receptieController.getGast(hotelEvent.getGuestId()).getKamer().getPosition().getX(), receptieController.getGast(hotelEvent.getGuestId()).getKamer().getPosition().getY()-1);

        SchoonmakerModel gekozen;

        if (target.getX() < layoutController.getView().getGridBreedte() / 2) {
            gekozen = schoonmaker2;
        } else {
            gekozen = schoonmaker1;
        }

        PathFinder pf = new PathFinder(gekozen.getLocatie(), target, layoutController);

        movementEngine.voegRouteToe(gekozen, pf);

        System.out.println("Schoonmaker " + gekozen.getID() + " onderweg.");
    }

    // laat geabonneerde weten dat de schoonmaker is verplaatst
    @Override
    public void onStepTaken(PersoonModel persoon, Locatie oudeLocatie) {
        SchoonmakerModel sm = (SchoonmakerModel) persoon;

        for (NewSchoonmaker listener : listeners) {
            listener.onSchoonmakerVerplaatst(sm, oudeLocatie);
        }
    }

    // zorg dat de schoonmaker gaat schoonmaken bij de kamer waar hij is aangekomen
    @Override
    public void onDestinationReached(PersoonModel persoon) {

        SchoonmakerModel sm = (SchoonmakerModel) persoon;

        System.out.println("Schoonmaker " + sm.getID() + " aangekomen.");

        sm.setCleaning(true);

        for (NewSchoonmaker listener : listeners) {
            listener.onSchoonmakerAangekomenInKamer(sm);
        }
    }
}