import Controller.PersoonManagement.GastController;
import Controller.PersoonManagement.NewGast;
import Controller.Layout.LayoutController;
import Model.Layout.LayoutModel;
import Model.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Personen.PersoonModel;
import View.Layout.LayoutView;
import View.Systeem.OverzichtView;
import hotelevents.HotelEvent;
import hotelevents.HotelEventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestPersoonController {

    private GastController gastController;
    private LayoutController layoutController;
    private Locatie startLoc;

    @BeforeEach
    public void setUp() {
        gastController = new GastController();

        LayoutView view = new LayoutView(null, null);
        view.setGridBreedte(10);
        view.setGridLengte(10);
        layoutController = new LayoutController(new LayoutModel(), view);

        gastController.onLayoutGeladen(layoutController);
        startLoc = new Locatie(5, 9);
    }

    @Test
    public void testCheckIn() {
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        gastController.setNewGuestListener(new NewGast() {
            @Override public void onGastAangemaakt(GastModel gast) { listenerCalled.set(true); }
            @Override public void onGastVertrokken(GastModel gast) {}
            @Override public void onGastVerplaatst(GastModel gast, Locatie oude) {}
            @Override public void onGastAangekomenInKamer(GastModel gast, Locatie loc) {}
            @Override public void onGastGaatWegUitKamer(GastModel gast, Locatie oude) {}
        });

        HotelEvent event = new HotelEvent(1, HotelEventType.CHECK_IN, 1, 2); // Gast 1, 2 sterren
        gastController.checkInEvent(event);

        Assertions.assertTrue(listenerCalled.get(), "Listener moet aangeroepen worden bij check-in");
    }

    @Test
    public void testOnStepTaken() {
        GastModel gast = new GastModel(1, startLoc, new Locatie(0,0), null, null);
        Locatie oudeLoc = new Locatie(5, 8);

        gast.setVorigeLocatie(oudeLoc);

        gastController.onStepTaken(gast, oudeLoc);
    }

    @Test
    public void testDestinationReached() {
        GastModel gast = new GastModel(1, startLoc, startLoc, null, null);

        gastController.onDestinationReached(gast);

        Locatie kamerLoc = new Locatie(2, 2);
        GastModel gastInKamer = new GastModel(2, kamerLoc, kamerLoc, null, null);
        gastController.onDestinationReached(gastInKamer);

        Assertions.assertEquals(kamerLoc.getX(), gastInKamer.getVorigeLocatie().getX());
    }

    @Test
    public void testEventRoutes() {
        gastController.checkInEvent(new HotelEvent(1, HotelEventType.CHECK_IN, 1, 1));
        gastController.needFoodEvent(new HotelEvent(1, HotelEventType.NEED_FOOD, 1, 0));
        gastController.goToFitnessEvent(new HotelEvent(1, HotelEventType.GOTO_FITNESS, 1, 0));
        gastController.goToCinemaEvent(new HotelEvent(1, HotelEventType.GOTO_CINEMA, 1, 0));
        gastController.checkOutEvent(new HotelEvent(1, HotelEventType.CHECK_OUT, 1, 0));
    }

    @Test
    public void testTimeChange() {
        gastController.timeChange(2000);
    }

    @Test
    public void testStartCinemaEmptyMethod() {
        gastController.startCinemaEvent(null);
    }
}