package Controller.Timer;

import Controller.Events.Interfaces.noneEvent;
import hotelevents.HotelEvent;
import java.util.*;

public class WachtTimer implements noneEvent {
    private final Map<String, Integer> resterendeTijdMap = new HashMap<>(); // houdt persoon en resterende ticks bij
    private final Map<String, TimerPing> listeners = new HashMap<>(); // koppelt elke persoon aan de TimerPing


    // start een timer voor een persoon op basis van hun ID
    public synchronized void startTimer(String persoonsID, TimerPing listener, int verblijfTijd) {
        if (resterendeTijdMap.containsKey(persoonsID)) {
            return; // gast heeft al een lopende timer
        }
        // sla de resterende ticks en de bijbehorende listener op
        resterendeTijdMap.put(persoonsID, verblijfTijd);
        listeners.put(persoonsID, listener);
    }

    // verlaagt alle timers en roept timerPing aan als de tijd om is
    @Override
    public synchronized void noneEvent(HotelEvent event) {
        ArrayList<String> gastenDieKlaarZijn = new ArrayList<>();

        // loop door alle persoon id's heen
        for (String id : resterendeTijdMap.keySet()) {
            int huidigeTijd = resterendeTijdMap.get(id);
            int nieuweTijd = huidigeTijd - 1;

            if (nieuweTijd <= 0) { // als na de verlaging de timer af is gelopen
                // markeer dat deze gast klaar is
                gastenDieKlaarZijn.add(id);
            } else {
                // update de tijd in de map
                resterendeTijdMap.put(id, nieuweTijd);
            }
        }

        // stuur alle gasten die klaar zijn weg
        for (String id : gastenDieKlaarZijn) {
            resterendeTijdMap.remove(id);
            TimerPing listener = listeners.remove(id);

            if (listener != null) {
                listener.timerAfgelopen();
            }
        }
    }
}