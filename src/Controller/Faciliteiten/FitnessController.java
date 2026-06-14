package Controller.Faciliteiten;

import Controller.Events.Interfaces.fitnessEvent;
import Controller.Faciliteiten.Interfaces.fitnessOver;
import Controller.PersoonManagement.Interfaces.NewGast;
import Controller.Timer.WachtTimer;
import Model.Layout.Locatie;
import Model.Entiteiten.Activiteit;
import Model.Entiteiten.GastModel;
import hotelevents.HotelEvent;

import java.util.ArrayList;
import java.util.Random;

public class FitnessController implements fitnessEvent, NewGast {

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

    // voeg gast toe aan lijst van gasten die willen sporten
    @Override
    public void goToFitnessEvent(HotelEvent hotelEvent) {
        gastenInFitness.add(hotelEvent.getGuestId());
    }

    public void stuurGastenWeg(int gastId) {
        gastenInFitness.remove(Integer.valueOf(gastId));

        for (fitnessOver listener : listeners) {
            listener.gaWegUitGym(gastId);
        }
    }

    @Override
    public void onGastAangemaakt(GastModel gast) {

    }

    @Override
    public void onGastVertrokken(GastModel gast) {

    }

    @Override
    public void onGastVerplaatst(GastModel gast, Locatie oudeLocatie) {

    }

    // als de gast in de gym is aangekomen, bereken de sporttijd en maak een timer voor hun aan
    @Override
    public void onGastAangekomenInKamer(GastModel gast, Locatie behaaldeLocatie) {
        int gastId = gast.getID();
        if (gastenInFitness.contains(gast.getID())) {
            int verblijfTijd = rand.nextInt(15, 31);

            String uniekeID = gast.getTypePersoon().name() + "-" + gastId;

            wachtTimer.startTimer(uniekeID, () -> stuurGastenWeg(gastId), verblijfTijd);

            System.out.println("Gast " + gastId + " is gaan sporten voor " + verblijfTijd + " ticks.");

            gast.setActiviteit(Activiteit.SPORTEN);
        }
    }

    @Override
    public void onGastGaatWegUitKamer(GastModel gast, Locatie oudeLocatie) {

    }
}