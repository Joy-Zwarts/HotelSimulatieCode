package Controller.Faciliteiten;

import Controller.Events.Interfaces.needFoodEvent;
import Controller.Faciliteiten.Interfaces.restaurantOver;
import Controller.PersoonManagement.Interfaces.NewGast;
import Controller.Timer.WachtTimer;
import Model.Layout.Locatie;
import Model.Personen.Activiteit;
import Model.Personen.GastModel;
import hotelevents.HotelEvent;

import java.util.ArrayList;
import java.util.Random;

public class RestaurantController implements needFoodEvent, NewGast {

    private final ArrayList<restaurantOver> listeners = new ArrayList<>();
    private final ArrayList<Integer> gastenInRestaurant = new ArrayList<>();
    private final Random rand = new Random();
    public final WachtTimer wachtTimer;
    public ArrayList<Integer> teVerwijderen;

    public void addListeners(restaurantOver listener) {
        listeners.add(listener);
    }

    public RestaurantController(WachtTimer timer) {
        this.wachtTimer = timer;
        this.teVerwijderen = new ArrayList<>();
    }

    // voeg gast toe aan lijst van gasten met een need food event
    @Override
    public void needFood(HotelEvent hotelEvent) {
        gastenInRestaurant.add(hotelEvent.getGuestId());
    }

    public void stuurGastenWeg(int gastId) {
        gastenInRestaurant.remove(Integer.valueOf(gastId));

        for (restaurantOver listener : listeners) {
            listener.gaWegUitRestaurant(gastId);
        }
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

    // als de gast in het restaurant is aangekomen, bereken de eettijd en maak een timer voor hun aan
    @Override
    public void onGastAangekomenInKamer(GastModel gast, Locatie behaaldeLocatie) {
        int gastId = gast.getID();
        if (gastenInRestaurant.contains(gast.getID())) {
            int verblijfTijd = rand.nextInt(15, 31);

            String uniekeID = gast.getTypePersoon().name() + "-" + gastId;

            wachtTimer.startTimer(uniekeID, () -> stuurGastenWeg(gastId), verblijfTijd);

            System.out.println("Gast " + gastId + " is gaan eten voor " + verblijfTijd + " ticks.");

            gast.setActivity(Activiteit.ETEN);
        }
    }

    @Override
    public void onGastGaatWegUitKamer(GastModel gast, Locatie oudeLocatie) {

    }
}