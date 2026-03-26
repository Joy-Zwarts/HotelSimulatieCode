import Controller.HotelEventManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestTimer {

    @Test
    @Order(1)
    void testTimerStarts() {
        HotelEventManager manager = new HotelEventManager(1000);

        manager.tick();

        assertTrue(manager.getTime() > 0);
    }

    @Test
    @Order(2)
    void testSixtySeconds() {
        HotelEventManager manager = new HotelEventManager(1000);

        for (int i = 0; i < 60; i++) {
            manager.tick();
        }

        assertEquals(60000, manager.getTime());
    }

    @Test
    @Order(3)
    void testSixtyMinutes() {
        HotelEventManager manager = new HotelEventManager(1000);

        for (int i = 0; i < 3600; i++) {
            manager.tick();
        }

        assertEquals(3600000, manager.getTime());
    }
}