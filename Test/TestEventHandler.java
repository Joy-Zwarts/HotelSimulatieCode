import Controller.Events.*;
import hotelevents.HotelEvent;
import hotelevents.HotelEventManager;
import hotelevents.HotelEventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestEventHandler implements checkInEvent, checkOutEvent, cinemaEvent, cleaningEmergencyEvent, evacuateEvent, fitnessEvent, godzillaEvent, needFoodEvent, noneEvent {

    private int eventsCalledCount;
    private EventHandler handler;
    private HotelEventManager manager;

    @BeforeEach
    public void setUp() {
        manager = new HotelEventManager();
        handler = new EventHandler(manager);
        eventsCalledCount = 0;

        handler.setEventListenerCheckIn(this);
        handler.setEventListenerCheckOut(this);
        handler.setEventListenerEvacuate(this);
        handler.setEventListenerGodzilla(this);
        handler.setEventListenerFood(this);
        handler.setEventListenerCinema(this);
        handler.setEventListenerFitness(this);
        handler.setEventListenerCleaning(this);
        handler.setEventListenerNoneEvent(this);
    }

    @Test
    public void testNotifyCoverage() {
        for (HotelEventType type : HotelEventType.values()) {
            handler.notify(new HotelEvent(0, type, 0, 0));
        }

        Assertions.assertEquals(10, eventsCalledCount, "Niet alle event types hebben de listeners getriggerd");
    }

    @Override public void checkInEvent(HotelEvent e) { eventsCalledCount++; }
    @Override public void checkOutEvent(HotelEvent e) { eventsCalledCount++; }
    @Override public void evacuateEvent(HotelEvent e) { eventsCalledCount++; }
    @Override public void godzillaEvent(HotelEvent e) { eventsCalledCount++; }
    @Override public void needFoodEvent(HotelEvent e) { eventsCalledCount++; }
    @Override public void goToCinemaEvent(HotelEvent e) { eventsCalledCount++; }
    @Override public void startCinemaEvent(HotelEvent e) { eventsCalledCount++; }
    @Override public void goToFitnessEvent(HotelEvent e) { eventsCalledCount++; }
    @Override public void cleaningEmergencyEvent(HotelEvent e) { eventsCalledCount++; }
    @Override public void noneEvent(HotelEvent e) { eventsCalledCount++; }
}