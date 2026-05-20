package Controller.PersoonManagement;

import Controller.Systeem.reset;
import Model.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Personen.PersoonModel;
import View.Systeem.OverzichtView; // Voeg deze import toe

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BeweegHelper  {
    private final Map<Integer, PathFinder> actieveRoutes;
    private final Map<Integer, PersoonModel> actieveMensen;
    private final Timer bewegingsTimer;
    private final MovementListener listener;
    private OverzichtView overzichtView;

    public void reset() {
        bewegingsTimer.stop();
        actieveRoutes.clear();
        actieveMensen.clear();
        bewegingsTimer.start();
    }

    // interface voor stappen genomen en als de targetlocatie is bereikt
    public interface MovementListener {
        void onStepTaken(PersoonModel persoon, Locatie oudeLocatie);
        void onDestinationReached(PersoonModel persoon);
    }

    public BeweegHelper(int hteSnelheid, MovementListener listener) {
        this.actieveRoutes = new HashMap<>();
        this.actieveMensen = new HashMap<>();
        this.listener = listener;
        // timer die per hte tick movement processed
        this.bewegingsTimer = new Timer(hteSnelheid, e -> processMovement());
    }

    public void setOverzichtView(OverzichtView overzichtView) {
        this.overzichtView = overzichtView;
    }
    public void start() { bewegingsTimer.start(); }
    public void setSpeed(int speed) { bewegingsTimer.setDelay(speed); }

    public void voegRouteToe(PersoonModel persoon, PathFinder pf) {
        actieveMensen.put(persoon.getID(), persoon);
        actieveRoutes.put(persoon.getID(), pf);
    }

    // wordt per hte tick aangeroepen om (als er een stap in de lijst staat) een stap te nemen
    private void processMovement() {

        // controleer of de simulatie is gepauzeerd
        if (overzichtView != null && overzichtView.isGepauzeerd()) {
            return;
        }

        // loopt door alle IDs van actieve routes
        for (Integer id : new ArrayList<>(actieveRoutes.keySet())) {

            // haal het persoon op die hoort bij deze ID
            PersoonModel persoon = actieveMensen.get(id);

            PathFinder pf = actieveRoutes.get(id);

            // als de bestemming bereikt is
            if (pf.isBestemmingBereikt()) {

                // ping listener
                listener.onDestinationReached(persoon);

                // verwijder de route uit actieve routes
                actieveRoutes.remove(id);

            } else {

                // bewaar de oude locatie
                Locatie oudeLocatie = new Locatie(
                        persoon.getLocatie().getX(),
                        persoon.getLocatie().getY()
                );

                // pak de volgende stap
                Locatie volgendeStap = pf.getNextStep();

                // verplaats de persoon naar de nieuwe x en y
                persoon.getLocatie().setX(volgendeStap.getX());
                persoon.getLocatie().setY(volgendeStap.getY());

                // ping listener
                listener.onStepTaken(persoon, oudeLocatie);
            }
        }
    }
}
