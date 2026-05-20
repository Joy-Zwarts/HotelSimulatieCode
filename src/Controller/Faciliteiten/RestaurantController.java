package Controller.Faciliteiten;

import Controller.Events.needFoodEvent;
import Controller.Events.noneEvent;
import View.Systeem.OverzichtView;
import hotelevents.HotelEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RestaurantController implements needFoodEvent, noneEvent {

    private final ArrayList<restaurantOver> listeners = new ArrayList<>();
    private final ArrayList<Integer> gastenInRestaurant = new ArrayList<>();

    // per gast eigen eindtijd
    private final Map<Integer, Integer> gastEindTijd = new HashMap<>();
    private final Map<Integer, Integer> gastTimer = new HashMap<>();
    private final Random rand = new Random();

    public void addlisteners(restaurantOver listener) {
        listeners.add(listener);
    }

    // voeg gast toe aan lijst van gasten in het restaurant met een random verblijftijd
    @Override
    public void needFoodEvent(HotelEvent hotelEvent) {
        int gastId = hotelEvent.getGuestId();

        gastenInRestaurant.add(gastId);

        gastTimer.put(gastId, 1);
        gastEindTijd.put(gastId, rand.nextInt(15, 31));

        System.out.println(
                "Gast " + gastId + " is gaan eten voor " + gastEindTijd.get(gastId) + " ticks."
        );
    }

    // per tick de timer ophogen en checken of de eindtijd is bereikt
    @Override
    public void noneEvent(HotelEvent event) {
        ArrayList<Integer> teVerwijderen = new ArrayList<>();

        for (int gastId : gastenInRestaurant) {

            int timer = gastTimer.get(gastId) + 1;
            gastTimer.put(gastId, timer);

            if (timer >= gastEindTijd.get(gastId)) {

                for (restaurantOver listener : listeners) {
                    listener.gaWegUitRestaurant(gastId);
                }

                teVerwijderen.add(gastId);
            }
        }

        // cleanup
        for (int gastId : teVerwijderen) {
            gastenInRestaurant.remove(Integer.valueOf(gastId));
            gastTimer.remove(gastId);
            gastEindTijd.remove(gastId);
        }
    }
    public Map<Integer, Integer> getGastTimer() {
        return gastTimer;
    }

    public Map<Integer, Integer> getGastEindTijd() {
        return gastEindTijd;
    }
}