package Controller.Faciliteiten;

import Controller.Events.needFoodEvent;
import Controller.Events.noneEvent;
import Controller.Timer.TimerPing;
import Controller.Timer.WachtTimer;
import View.Systeem.OverzichtView;
import hotelevents.HotelEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RestaurantController implements needFoodEvent {

    private final ArrayList<restaurantOver> listeners = new ArrayList<>();
    private final ArrayList<Integer> gastenInRestaurant = new ArrayList<>();
    private final Random rand = new Random();
    public final WachtTimer wachtTimer;
    public ArrayList<Integer> teVerwijderen;

    public void addlisteners(restaurantOver listener) {
        listeners.add(listener);
    }

    public RestaurantController(WachtTimer timer) {
        this.wachtTimer = timer;
        this.teVerwijderen = new ArrayList<>();
    }

    // voeg gast toe aan lijst van gasten in het restaurant met een random verblijftijd
    @Override
    public void needFoodEvent(HotelEvent hotelEvent) {
        int gastId = hotelEvent.getGuestId();

        gastenInRestaurant.add(gastId);

        int verblijfTijd = rand.nextInt(15, 31);
        wachtTimer.startTimer(() -> stuurGastenWeg(gastId), verblijfTijd);
    }

    public void stuurGastenWeg(int gastId) {
        gastenInRestaurant.remove(Integer.valueOf(gastId));

        for (restaurantOver listener : listeners) {
            listener.gaWegUitRestaurant(gastId);
        }
    }
}