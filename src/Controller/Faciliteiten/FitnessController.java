package Controller.Faciliteiten;

import Controller.Events.fitnessEvent;
import Controller.Events.noneEvent;
import View.Systeem.OverzichtView;
import hotelevents.HotelEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FitnessController implements fitnessEvent, noneEvent {
    private final ArrayList<fitnessOver> listeners = new ArrayList<>();
    private final ArrayList<Integer> gastenInGym = new ArrayList<>();

    // per gast eigen eindtijd
    private final Map<Integer, Integer> gastEindTijd = new HashMap<>();
    private final Map<Integer, Integer> gastTimer = new HashMap<>();
    private final Random rand = new Random();

    public void addlisteners(fitnessOver listener) {
        listeners.add(listener);
    }

    @Override
    public void noneEvent(HotelEvent event) {
        ArrayList<Integer> teVerwijderen = new ArrayList<>();

        for (int gastId : gastenInGym) {

            int timer = gastTimer.get(gastId) + 1;
            gastTimer.put(gastId, timer);

            if (timer >= gastEindTijd.get(gastId)) {

                for (fitnessOver listener : listeners) {
                    listener.gaWegUitGym(gastId);
                }

                teVerwijderen.add(gastId);
            }
        }

        // cleanup
        for (int gastId : teVerwijderen) {
            gastenInGym.remove(Integer.valueOf(gastId));
            gastTimer.remove(gastId);
            gastEindTijd.remove(gastId);
        }
    }

    @Override
    public void goToFitnessEvent(HotelEvent hotelEvent) {
        int gastId = hotelEvent.getGuestId();

        gastenInGym.add(gastId);

        gastTimer.put(gastId, 1);
        gastEindTijd.put(gastId, rand.nextInt(15, 31));

        System.out.println(
                "Gast " + gastId + " is gaan sporten voor " + gastEindTijd.get(gastId) + " ticks."
        );
    }
}
