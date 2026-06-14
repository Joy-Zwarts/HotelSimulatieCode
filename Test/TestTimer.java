import Controller.Timer.TimerPing;
import Controller.Timer.WachtTimer;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestTimer {

    // Dummy listener waarmee gecontroleerd kan worden
    // of timerAfgelopen() daadwerkelijk wordt aangeroepen.
    static class TestListener implements TimerPing {
        boolean afgelopen = false;

        @Override
        public void timerAfgelopen() {
            afgelopen = true;
        }
    }

    // Test of een nieuwe timer correct wordt toegevoegd.
    @Test
    void startTimerVoegtTimerToe() throws Exception {
        WachtTimer timer = new WachtTimer();
        TestListener listener = new TestListener();

        // Start een timer van 5 ticks.
        timer.startTimer("1", listener, 5);

        // Lees de private map uit met reflection.
        Field field = WachtTimer.class.getDeclaredField("resterendeTijdMap");
        field.setAccessible(true);

        Map<String, Integer> map =
                (Map<String, Integer>) field.get(timer);

        // Controleer of de timer met de juiste waarde is opgeslagen.
        assertEquals(5, map.get("1"));
    }

    // Test dat dezelfde persoon niet twee timers krijgt.
    @Test
    void startTimerVoegtGeenDubbeleTimerToe() throws Exception {
        WachtTimer timer = new WachtTimer();
        TestListener listener = new TestListener();

        // Eerste timer wordt toegevoegd.
        timer.startTimer("1", listener, 5);

        // Tweede poging moet genegeerd worden.
        timer.startTimer("1", listener, 10);

        Field field = WachtTimer.class.getDeclaredField("resterendeTijdMap");
        field.setAccessible(true);

        Map<String, Integer> map =
                (Map<String, Integer>) field.get(timer);

        // De oorspronkelijke waarde moet behouden blijven.
        assertEquals(5, map.get("1"));
    }

    // Test dat een tick de resterende tijd met 1 verlaagt.
    @Test
    void tickVerlaagtTimer() throws Exception {
        WachtTimer timer = new WachtTimer();
        TestListener listener = new TestListener();

        // Timer start met 3 ticks.
        timer.startTimer("1", listener, 3);

        // Eén tick uitvoeren.
        timer.HTETick(null);

        Field field = WachtTimer.class.getDeclaredField("resterendeTijdMap");
        field.setAccessible(true);

        Map<String, Integer> map =
                (Map<String, Integer>) field.get(timer);

        // Timer moet nu nog 2 ticks over hebben.
        assertEquals(2, map.get("1"));

        // Listener mag nog niet zijn aangeroepen.
        assertFalse(listener.afgelopen);
    }

    // Test dat een timer afloopt en de listener wordt aangeroepen.
    @Test
    void tickLaatTimerAflopen() throws Exception {
        WachtTimer timer = new WachtTimer();
        TestListener listener = new TestListener();

        // Timer loopt na één tick af.
        timer.startTimer("1", listener, 1);

        timer.HTETick(null);

        // Wacht totdat invokeLater() is uitgevoerd.
        SwingUtilities.invokeAndWait(() -> {});

        // Controleer of timerAfgelopen() is aangeroepen.
        assertTrue(listener.afgelopen);
    }

    // Test meerdere timers tegelijk.
    // Eén timer moet aflopen, de andere moet doorgaan.
    @Test
    void meerdereTimersWordenGoedVerwerkt() throws Exception {
        WachtTimer timer = new WachtTimer();

        TestListener listener1 = new TestListener();
        TestListener listener2 = new TestListener();

        // Eerste timer loopt direct af.
        timer.startTimer("1", listener1, 1);

        // Tweede timer heeft nog meerdere ticks nodig.
        timer.startTimer("2", listener2, 3);

        timer.HTETick(null);

        // Wacht tot invokeLater() klaar is.
        SwingUtilities.invokeAndWait(() -> {});

        // Eerste listener moet zijn aangeroepen.
        assertTrue(listener1.afgelopen);

        // Tweede listener nog niet.
        assertFalse(listener2.afgelopen);

        Field field = WachtTimer.class.getDeclaredField("resterendeTijdMap");
        field.setAccessible(true);

        Map<String, Integer> map =
                (Map<String, Integer>) field.get(timer);

        // Tweede timer moet nog 2 ticks over hebben.
        assertEquals(2, map.get("2"));
    }
}