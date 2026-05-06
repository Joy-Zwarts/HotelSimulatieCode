import Controller.Events.*;
import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;
import hotelevents.HotelEventManager;
import hotelevents.HotelEventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestEventHandler implements checkInEvent, checkOutEvent, cinemaEvent, cleaningEmergencyEvent, evacuateEvent, fitnessEvent, godzillaEvent, needFoodEvent, noneEvent {

    private boolean checkInCalled;
    private boolean checkOutCalled;
    private boolean evacuateCalled;
    private boolean godzillaCalled;
    private boolean foodCalled;
    private boolean goCinemaCalled;
    private boolean startCinemaCalled;
    private boolean fitnessCalled;
    private boolean cleaningCalled;
    private boolean noneCalled;

    @Test
    public void testAllEvents() {
        HotelEventManager manager = new HotelEventManager();
        EventHandler handler = new EventHandler(manager);

        handler.setEventListenerCheckIn(this);
        handler.setEventListenerCheckOut(this);
        handler.setEventListenerEvacuate(this);
        handler.setEventListenerGodzilla(this);
        handler.setEventListenerFood(this);
        handler.setEventListenerCinema(this);
        handler.setEventListenerFitness(this);
        handler.setEventListenerCleaning(this);
        handler.setEventListenerNoneEvent(this);

        handler.notify(new HotelEvent(0, HotelEventType.CHECK_IN, 0, 0));
        handler.notify(new HotelEvent(0, HotelEventType.CHECK_OUT, 0, 0));
        handler.notify(new HotelEvent(0, HotelEventType.EVACUATE, 0, 0));
        handler.notify(new HotelEvent(0, HotelEventType.GODZILLA, 0, 0));
        handler.notify(new HotelEvent(0, HotelEventType.NEED_FOOD, 0, 0));
        handler.notify(new HotelEvent(0, HotelEventType.GOTO_CINEMA, 0, 0));
        handler.notify(new HotelEvent(0, HotelEventType.START_CINEMA, 0, 0));
        handler.notify(new HotelEvent(0, HotelEventType.GOTO_FITNESS, 0, 0));
        handler.notify(new HotelEvent(0, HotelEventType.CLEANING_EMERGENCY, 0, 0));
        handler.notify(new HotelEvent(0, HotelEventType.NONE, 0, 0));

        // assertions
        Assertions.assertTrue(checkInCalled);
        Assertions.assertTrue(checkOutCalled);
        Assertions.assertTrue(evacuateCalled);
        Assertions.assertTrue(godzillaCalled);
        Assertions.assertTrue(foodCalled);
        Assertions.assertTrue(goCinemaCalled);
        Assertions.assertTrue(startCinemaCalled);
        Assertions.assertTrue(fitnessCalled);
        Assertions.assertTrue(cleaningCalled);
        Assertions.assertTrue(noneCalled);
    }

    @Override
    public void checkInEvent(HotelEvent e) {
        checkInCalled = true;
    }

    @Override
    public void checkOutEvent(HotelEvent e) {
        checkOutCalled = true;
    }

    @Override
    public void evacuateEvent(HotelEvent e) {
        evacuateCalled = true;
    }

    @Override
    public void godzillaEvent(HotelEvent e) {
        godzillaCalled = true;
    }

    @Override
    public void needFoodEvent(HotelEvent e) {
        foodCalled = true;
    }

    @Override
    public void goToCinemaEvent(HotelEvent e) {
        goCinemaCalled = true;
    }

    @Override
    public void startCinemaEvent(HotelEvent e) {
        startCinemaCalled = true;
    }

    @Override
    public void goToFitnessEvent(HotelEvent e) {
        fitnessCalled = true;
    }

    @Override
    public void cleaningEmergencyEvent(HotelEvent e) {
        cleaningCalled = true;
    }

    @Override
    public void noneEvent(HotelEvent e) {
        noneCalled = true;
    }
}
