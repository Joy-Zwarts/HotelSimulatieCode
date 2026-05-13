package Controller.PersoonManagement;

import Controller.Events.cleaningEmergencyEvent;
import Controller.Layout.LayoutController;
import Controller.Layout.LayoutGeladen;
import Controller.PersoonFactory.GastCreator;
import Controller.PersoonFactory.SchoonmakerCreator;
import Model.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Personen.SchoonmakerModel;
import hotelevents.HotelEvent;

import java.util.ArrayList;

public class SchoonmakerController implements LayoutGeladen, cleaningEmergencyEvent, NewGast, BeweegHelper.MovementListener {

    private final SchoonmakerCreator factory;
    private final BeweegHelper movementEngine;
    private final ArrayList<NewSchoonmaker> listeners;

    public SchoonmakerController() {
        this.listeners = new ArrayList<>();
        this.movementEngine = new BeweegHelper(1000, this);
        this.movementEngine.start();
        this.factory = new SchoonmakerCreator();
    }

    // zet een nieuwe listener als er een gast wordt aangemaakt
    public void setNewSchoonmakerListener(NewSchoonmaker listener) {
        listeners.add(listener);
    }

    @Override
    public void onLayoutGeladen(LayoutController layoutController) {
        int breedte = layoutController.getView().getGridBreedte();
        int lengte = layoutController.getView().getGridLengte();

        Locatie locatie1 = new Locatie(breedte - 1, 0);
        Locatie locatie2 = new Locatie(breedte - 1, lengte / 2);

        SchoonmakerModel schoonmaker1 = (SchoonmakerModel) factory.createPersoon(1, locatie1, null, 0);

        for (NewSchoonmaker listener : listeners) {
            listener.onSchoonmakerAangemaakt(schoonmaker1);
        }

        SchoonmakerModel schoonmaker2 = (SchoonmakerModel) factory.createPersoon(2, locatie2, null, 0);

        for (NewSchoonmaker listener : listeners) {
            listener.onSchoonmakerAangemaakt(schoonmaker2);
        }
    }

    @Override
    public void cleaningEmergencyEvent(HotelEvent hotelEvent) {

    }

    @Override
    public void onGastAangemaakt(GastModel gast) {

    }

    @Override
    public void onGastVertrokken(GastModel gast) {

    }

    @Override
    public void onGastVerplaatst(GastModel gast, Locatie oudeLocatie) {

    }

    @Override
    public void onGastAangekomenInKamer(GastModel gast, Locatie behaaldeLocatie) {

    }

    @Override
    public void onGastGaatWegUitKamer(GastModel gast, Locatie oudeLocatie) {

    }

    @Override
    public void onStepTaken(GastModel gast, Locatie oudeLocatie) {

    }

    @Override
    public void onDestinationReached(GastModel gast) {

    }
}
