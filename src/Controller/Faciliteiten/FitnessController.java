package Controller.Faciliteiten;

import Controller.Events.fitnessEvent;
import Controller.Events.needFoodEvent;
import Controller.Timer.WachtTimer;
import hotelevents.HotelEvent;

import java.util.ArrayList;
import java.util.Random;

public class FitnessController implements fitnessEvent {

    private final ArrayList<fitnessOver> listeners = new ArrayList<>();
    private final ArrayList<Integer> gastenInFitness = new ArrayList<>();
    private final Random rand = new Random();
    public final WachtTimer wachtTimer;

    public FitnessController(WachtTimer timer) {
        this.wachtTimer = timer;
    }

    public void addlisteners(fitnessOver listener) {
        listeners.add(listener);
    }

    // voeg gast toe aan lijst van gasten in de gym met een random verblijftijd

    @Override
    public void goToFitnessEvent(HotelEvent hotelEvent) {
        int gastId = hotelEvent.getGuestId();

        gastenInFitness.add(gastId);

        int verblijfTijd = rand.nextInt(15, 31);
        wachtTimer.startTimer(() -> stuurGastenWeg(gastId), verblijfTijd);
    }

    public void stuurGastenWeg(int gastId) {
        gastenInFitness.remove(Integer.valueOf(gastId));

        for (fitnessOver listener : listeners) {
            listener.gaWegUitGym(gastId);
        }
    }
}