package Controller.Events;

import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;
import hotelevents.HotelEventManager;

import java.util.ArrayList;

public class EventHandler implements HotelEventListener {

    private ArrayList<checkInEvent> checkinListeners;
    private ArrayList<checkOutEvent> checkoutListeners;
    private ArrayList<evacuateEvent> evacuateListeners;
    private ArrayList<godzillaEvent> godzillaListeners;
    private ArrayList<needFoodEvent> foodListeners;
    private ArrayList<cinemaEvent> cinemaListeners;
    private ArrayList<fitnessEvent> fitnessListeners;
    private ArrayList<cleaningEmergencyEvent> cleaningListeners;
    private ArrayList<noneEvent> noneEventListeners;


    public EventHandler(HotelEventManager manager) {
        manager.register(this);
        checkinListeners = new ArrayList<>();
        checkoutListeners = new ArrayList<>();
        evacuateListeners = new ArrayList<>();
        godzillaListeners = new ArrayList<>();
        foodListeners = new ArrayList<>();
        cinemaListeners = new ArrayList<>();
        fitnessListeners = new ArrayList<>();
        cleaningListeners = new ArrayList<>();
        noneEventListeners = new ArrayList<>();
    }

    @Override
    public void notify(HotelEvent hotelEvent) {
        switch (hotelEvent.getEventType()) {
            case CHECK_IN:
                for (checkInEvent listener : checkinListeners) {
                    listener.checkInEvent(hotelEvent);
                }
                break;
            case CHECK_OUT:
                for (checkOutEvent listener : checkoutListeners) {
                    listener.checkOutEvent(hotelEvent);
                }
                break;
            case EVACUATE:
                for (evacuateEvent listener : evacuateListeners) {
                    listener.evacuateEvent(hotelEvent);
                }
                break;
            case GODZILLA:
                for (godzillaEvent listener : godzillaListeners) {
                    listener.godzillaEvent(hotelEvent);
                }
                break;
            case NEED_FOOD:
                for (needFoodEvent listener : foodListeners) {
                    listener.needFoodEvent(hotelEvent);
                }
                break;
            case GOTO_CINEMA:
                for (cinemaEvent listener : cinemaListeners) {
                    listener.goToCinemaEvent(hotelEvent);
                }
                break;
            case GOTO_FITNESS:
                for (fitnessEvent listener : fitnessListeners) {
                    listener.goToFitnessEvent(hotelEvent);
                }
                break;
            case START_CINEMA:
                for (cinemaEvent listener : cinemaListeners) {
                    listener.startCinemaEvent(hotelEvent);
                }
                break;
            case CLEANING_EMERGENCY:
                for (cleaningEmergencyEvent listener : cleaningListeners) {
                    listener.cleaningEmergencyEvent(hotelEvent);
                }
                break;
            case NONE:
                for (noneEvent listener : noneEventListeners) {
                    listener.noneEvent(hotelEvent);
                }
                break;
            default:
                break;
        }
    }

    public void setEventListenerCheckIn(checkInEvent listener) {
        checkinListeners.add(listener);
    }
    public void setEventListenerCheckOut(checkOutEvent listener) {
        checkoutListeners.add(listener);
    }
    public void setEventListenerEvacuate(evacuateEvent listener) {
        evacuateListeners.add(listener);
    }
    public void setEventListenerGodzilla(godzillaEvent listener) {
        godzillaListeners.add(listener);
    }
    public void setEventListenerFood(needFoodEvent listener) {
        foodListeners.add(listener);
    }
    public void setEventListenerCinema(cinemaEvent listener) {
        cinemaListeners.add(listener);
    }
    public void setEventListenerFitness(fitnessEvent listener) {
        fitnessListeners.add(listener);
    }
    public void setEventListenerCleaning(cleaningEmergencyEvent listener) {
        cleaningListeners.add(listener);
    }
    public void setEventListenerNoneEvent(noneEvent listener) {
        noneEventListeners.add(listener);
    }
}
