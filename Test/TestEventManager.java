import Controller.HotelEventManager;
import Model.HotelEvent;
import Model.HotelEventListener;
import Model.HotelEventType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestEventManager {

    @Test
    public void testEventGeneration() {
        HotelEventManager manager = new HotelEventManager(1000);

        // test listener om events op te vangen
        TestListener listener = new TestListener();
        manager.registerListener(listener);

        // simuleer meerdere ticks
        for (int i = 0; i < 100; i++) {
            manager.tick();
        }

        // controleer of er events zijn gegenereerd
        assertNotNull(listener.lastEvent, "Er is geen event gegenereerd");

        // controleer of event type bestaat
        assertNotNull(listener.lastEvent.getType(), "Event type is null");

        // controleer tijd
        assertTrue(listener.lastEvent.getTime() > 0, "Tijd is niet verhoogd");
    }

    // (fake listener)
    static class TestListener implements HotelEventListener {
        HotelEvent lastEvent;

        @Override
        public void notify(HotelEvent evt) {
            this.lastEvent = evt;
        }
    }
}