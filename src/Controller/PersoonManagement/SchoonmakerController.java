package Controller.PersoonManagement;

import Controller.Events.cleaningEmergencyEvent;
import Controller.Layout.LayoutController;
import Controller.Layout.LayoutGeladen;
import Controller.PersoonFactory.GastCreator;
import Controller.PersoonFactory.SchoonmakerCreator;
import Model.Layout.Locatie;
import Model.Personen.GastModel;
import hotelevents.HotelEvent;

public class SchoonmakerController implements LayoutGeladen, cleaningEmergencyEvent, NewGast, BeweegHelper.MovementListener {

    private final SchoonmakerCreator factory;
    private final BeweegHelper movementEngine;

    public SchoonmakerController() {
        this.movementEngine = new BeweegHelper(1000, this);
        this.movementEngine.start();
        this.factory = new SchoonmakerCreator();
    }

    @Override
    public void onLayoutGeladen(LayoutController layoutController) {
        int breedte = layoutController.getView().getGridBreedte();
        int lengte = layoutController.getView().getGridLengte();

        Locatie locatie1 = new Locatie(breedte, 0);

        Locatie locatie2 = new Locatie(breedte, lengte/2);

        factory.createPersoon(1, locatie1, null, 0);
        factory.createPersoon(1, locatie2, null, 0);


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
