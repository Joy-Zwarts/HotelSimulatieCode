import Controller.HotelEventManager;
import Model.HotelEvent;
import Model.HotelEventListener;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HotelManagerTest {

    @Test
    void BredeTest() throws InterruptedException {
        HotelEventManager manager = new HotelEventManager(50);

        // simpele listener
        HotelEventListener listener = evt -> {
            assertNotNull(evt);
        };

        manager.registerListener(listener);

        // tick test
        manager.tick();
        assertTrue(manager.getTime() > 0);

        // timer test
        manager.startTimer();
        Thread.sleep(100);

        // pause test (beide paden)
        HotelEventManager.pause();
        HotelEventManager.pause();

        // setHte test
        manager.setHte(30);

        // deregister
        manager.deregisterListener(listener);

        // stop test
        manager.stop();
    }
}