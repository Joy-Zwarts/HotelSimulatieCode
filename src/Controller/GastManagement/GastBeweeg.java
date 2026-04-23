package Controller.GastManagement;

import Model.Layout.Locatie;
import Model.Personen.GastModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GastBeweeg {
    private final Map<Integer, PathFinder> actieveRoutes;
    private final Map<Integer, GastModel> actieveGasten;
    private final Timer bewegingsTimer;
    private final MovementListener listener;

    public interface MovementListener {
        void onStepTaken(GastModel gast, Locatie oudeLocatie);
        void onDestinationReached(GastModel gast);
    }

    public GastBeweeg(int hteSnelheid, MovementListener listener) {
        this.actieveRoutes = new HashMap<>();
        this.actieveGasten = new HashMap<>();
        this.listener = listener;
        this.bewegingsTimer = new Timer(hteSnelheid, e -> processMovement());
    }

    public void start() { bewegingsTimer.start(); }
    public void setSpeed(int speed) { bewegingsTimer.setDelay(speed); }

    public void voegRouteToe(GastModel gast, PathFinder pf) {
        actieveGasten.put(gast.getGastID(), gast);
        actieveRoutes.put(gast.getGastID(), pf);
    }

    public void verwijderGast(int gastId) {
        actieveGasten.remove(gastId);
        actieveRoutes.remove(gastId);
    }

    private void processMovement() {
        for (Integer id : new ArrayList<>(actieveRoutes.keySet())) {
            GastModel gast = actieveGasten.get(id);
            PathFinder pf = actieveRoutes.get(id);

            if (pf.isBestemmingBereikt()) {
                listener.onDestinationReached(gast);
                actieveRoutes.remove(id); // Stop met bewegen voor deze gast
            } else {
                Locatie oudeLocatie = new Locatie(gast.getLocatie().getX(), gast.getLocatie().getY());
                Locatie volgendeStap = pf.getNextStep();
                gast.getLocatie().setX(volgendeStap.getX());
                gast.getLocatie().setY(volgendeStap.getY());

                listener.onStepTaken(gast, oudeLocatie);
            }
        }
    }
}

