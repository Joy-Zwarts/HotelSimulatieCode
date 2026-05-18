package Controller.PersoonManagement;

import Model.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Personen.PersoonModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BeweegHelper {
    private final Map<Integer, PathFinder> actieveRoutes;
    private final Map<Integer, PersoonModel> actieveMensen;
    private final Timer bewegingsTimer;
    private final MovementListener listener;

    public interface MovementListener {
        void onStepTaken(PersoonModel persoon, Locatie oudeLocatie);
        void onDestinationReached(PersoonModel persoon);
    }

    public BeweegHelper(int hteSnelheid, MovementListener listener) {
        this.actieveRoutes = new HashMap<>();
        this.actieveMensen = new HashMap<>();
        this.listener = listener;
        this.bewegingsTimer = new Timer(hteSnelheid, e -> processMovement());
    }

    public void start() { bewegingsTimer.start(); }
    public void setSpeed(int speed) { bewegingsTimer.setDelay(speed); }

    public void voegRouteToe(PersoonModel persoon, PathFinder pf) {
        actieveMensen.put(persoon.getID(), persoon);
        actieveRoutes.put(persoon.getID(), pf);
    }

    private void processMovement() {
        for (Integer id : new ArrayList<>(actieveRoutes.keySet())) {
            PersoonModel persoon = actieveMensen.get(id);
            PathFinder pf = actieveRoutes.get(id);

            if (pf.isBestemmingBereikt()) {
                listener.onDestinationReached(persoon);
                actieveRoutes.remove(id); // stop met bewegen voor deze gast
            } else {
                Locatie oudeLocatie = new Locatie(persoon.getLocatie().getX(), persoon.getLocatie().getY());
                Locatie volgendeStap = pf.getNextStep();
                persoon.getLocatie().setX(volgendeStap.getX());
                persoon.getLocatie().setY(volgendeStap.getY());

                listener.onStepTaken(persoon, oudeLocatie);
            }
        }
    }
}

