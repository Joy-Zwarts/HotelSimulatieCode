package Controller.Timer;

import Controller.Events.noneEvent;
import hotelevents.HotelEvent;
import java.util.*;

public class WachtTimer implements noneEvent {

    // We maken een kleine interne klasse om de timer data uniek te groeperen
    private static class TimerTaak {
        final TimerPing callback;
        final int eindtijd;
        int huidigeTijd;

        TimerTaak(TimerPing callback, int eindtijd) {
            this.callback = callback;
            this.eindtijd = eindtijd;
            this.huidigeTijd = 1;
        }
    }

    // We gebruiken nu een List in plaats van een Map met Lambdas als keys.
    // Dit voorkomt het 'overschrijf-probleem' van de HashMap volledig!
    private final List<TimerTaak> actieveTimers = new ArrayList<>();

    public synchronized void startTimer(TimerPing listener, int eindtijd) {
        actieveTimers.add(new TimerTaak(listener, eindtijd));
    }

    @Override
    public synchronized void noneEvent(HotelEvent event) {
        List<TimerTaak> teVerwijderen = new ArrayList<>();

        // Loop veilig door alle actieve timers heen
        for (TimerTaak taak : actieveTimers) {
            taak.huidigeTijd++;

            if (taak.huidigeTijd >= taak.eindtijd) {
                teVerwijderen.add(taak);
            }
        }

        // Cleanup en vuur de callbacks af
        for (TimerTaak taakKlaar : teVerwijderen) {
            actieveTimers.remove(taakKlaar);

            // Dit voert nu gegarandeerd de juiste lambda uit voor de juiste gast!
            taakKlaar.callback.timerAfgelopen();
        }
    }

    // Handige extra methode voor je Reset-knop!
    public synchronized void clear() {
        actieveTimers.clear();
    }
}