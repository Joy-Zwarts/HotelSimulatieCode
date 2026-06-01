import Controller.Events.*;
import Controller.Events.Interfaces.*;
import hotelevents.HotelEvent;
import hotelevents.HotelEventManager;
import hotelevents.HotelEventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestEventHandler implements checkInEvent, checkOutEvent, cinemaEvent, cleaningEmergencyEvent, evacuateEvent, fitnessEvent, godzillaEvent, needFoodEvent, noneEvent {

    private int eventsCalledCount;
    private EventHandler handler;

    @BeforeEach
    public void setUp() {
        HotelEventManager manager = new HotelEventManager();
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

    @Override public void checkIn(HotelEvent e) { eventsCalledCount++; }
    @Override public void checkOut(HotelEvent e) { eventsCalledCount++; }
    @Override public void evacuate(HotelEvent e) { eventsCalledCount++; }
    @Override public void godzilla(HotelEvent e) { eventsCalledCount++; }
    @Override public void needFood(HotelEvent e) { eventsCalledCount++; }
    @Override public void goToCinemaEvent(HotelEvent e) { eventsCalledCount++; }
    @Override public void startCinemaEvent(HotelEvent e) { eventsCalledCount++; }
    @Override public void goToFitnessEvent(HotelEvent e) { eventsCalledCount++; }
    @Override public void cleaningEmergency(HotelEvent e) { eventsCalledCount++; }
    @Override public void HTETick(HotelEvent e) { eventsCalledCount++; }
}