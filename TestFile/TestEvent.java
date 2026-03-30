import Controller.HotelEventManager;
import Model.HotelEvent;
import Model.HotelEventListener;
import Model.HotelEventType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestEvent {

    private HotelEventManager manager;
    private TestListener listener;

    // Fake listener
    static class TestListener implements HotelEventListener {
        ArrayList<HotelEvent> ontvangenEvents = new ArrayList<>();

        @Override
        public void notify(HotelEvent evt) {
            ontvangenEvents.add(evt);
        }
    }

    @Test
    void testListenerOntvangtEvent() {
        listener = new TestListener();

        HotelEvent event = new HotelEvent(1000, HotelEventType.CHECK_IN, 123, 1);

        listener.notify(event);

        assertEquals(1, listener.ontvangenEvents.size());
    }
}