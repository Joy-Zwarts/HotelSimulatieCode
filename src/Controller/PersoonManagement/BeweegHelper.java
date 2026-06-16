package Controller.PersoonManagement;

import Model.Layout.Locatie;
import Model.Entiteiten.EntiteitenModel;
import Model.Entiteiten.PersoonModel;
import View.Systeem.OverzichtView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BeweegHelper  {
    private final Map<Integer, PathFinder> actieveRoutes;
    private final Map<Integer, PersoonModel> actieveMensen;
    private final Map<Integer, Integer> wachtTicksPerPersoon;
    private final Timer bewegingsTimer;
    private final MovementListener listener;
    private OverzichtView overzichtView;
    private static int trapVertragingTicks = 1;

    public void reset() {
        bewegingsTimer.stop();
        actieveRoutes.clear();
        actieveMensen.clear();
        wachtTicksPerPersoon.clear();
        bewegingsTimer.start();
    }

    public interface MovementListener {
        void onStepTaken(EntiteitenModel Entiteit, Locatie oudeLocatie);
        void onDestinationReached(EntiteitenModel Entiteit);
    }

    public BeweegHelper(int hteSnelheid, MovementListener listener) {
        this.actieveRoutes = new HashMap<>();
        this.actieveMensen = new HashMap<>();
        this.wachtTicksPerPersoon = new HashMap<>();
        this.listener = listener;
        this.bewegingsTimer = new Timer(hteSnelheid, _ -> processMovement());
    }

    public void setOverzichtView(OverzichtView overzichtView) {
        this.overzichtView = overzichtView;
    }
    public void start() { bewegingsTimer.start(); }
    public void setSpeed(int speed) { bewegingsTimer.setDelay(speed); }

    public void voegRouteToe(PersoonModel persoon, PathFinder pf) {
        actieveMensen.put(persoon.getID(), persoon);
        actieveRoutes.put(persoon.getID(), pf);
        wachtTicksPerPersoon.put(persoon.getID(), 0);

        pf.berekenRoute();
    }

    private void processMovement() {
        if (overzichtView != null && overzichtView.isGepauzeerd()) {
            return;
        }

        for (Integer id : new ArrayList<>(actieveRoutes.keySet())) {
            PersoonModel persoon = actieveMensen.get(id);
            PathFinder pf = actieveRoutes.get(id);

            // 1. Controleer of deze persoon nog in de wachtstand staat (bijvoorbeeld op de trap)
            int resterendeWachtTicks = wachtTicksPerPersoon.getOrDefault(id, 0);
            if (resterendeWachtTicks > 0) {
                wachtTicksPerPersoon.put(id, resterendeWachtTicks - 1);
                continue;
            }

            if (pf.isBestemmingBereikt()) {
                listener.onDestinationReached(persoon);
                actieveRoutes.remove(id);
                wachtTicksPerPersoon.remove(id);
            } else {
                Locatie oudeLocatie = new Locatie(
                        persoon.getLocatie().getX(),
                        persoon.getLocatie().getY()
                );

                // Peek de volgende stap om te controleren wat voor soort beweging dit is
                Locatie volgendeStap = pf.peekNextStep();

                if (volgendeStap != null) {
                    // --- NIEUW: LIFT LOGICA START ---
                    // Staat de gast bij de liftschacht (X=0) en gaat de volgende stap verticaal (Y verandert)?
                    boolean staatBijLiftSchacht = (oudeLocatie.getX() == 0);
                    boolean gaatVerdiepingWisselen = (oudeLocatie.getY() != volgendeStap.getY());

                    if (staatBijLiftSchacht && gaatVerdiepingWisselen && pf.getLayoutController().getLiftController() != null) {

                        // Haal de actuele Y-positie van de lift op
                        int liftY = pf.getLayoutController().getLiftController().getLiftModel().getLocatie().getY();

                        // Als de lift nog NIET op de verdieping van de gast is:
                        if (liftY != oudeLocatie.getY()) {
                            // Roep de lift naar de huidige verdieping van de gast
                            pf.getLayoutController().getLiftController().liftCalled(oudeLocatie.getY());
                            // Sla deze bewegingstick over (wacht op de gang)
                            continue;
                        }

                        // Als de lift er WEL is: de gast stapt in!
                        // Meld direct de DOEL-verdieping aan bij de lift, zodat de lift weet waar hij heen moet
                        pf.getLayoutController().getLiftController().liftCalled(volgendeStap.getY());
                    }
                    // --- NIEUW: LIFT LOGICA EIND ---

                    // Bestaande trap logica (alleen uitvoeren als het geen lift-beweging was)
                    if (!staatBijLiftSchacht && oudeLocatie.getY() != volgendeStap.getY()) {
                        int ticks = Math.max(1, trapVertragingTicks);
                        wachtTicksPerPersoon.put(id, ticks);
                    }
                }

                // Voer de stap nu uit
                volgendeStap = pf.getNextStep();
                persoon.getLocatie().setX(volgendeStap.getX());
                persoon.getLocatie().setY(volgendeStap.getY());

                listener.onStepTaken(persoon, oudeLocatie);
            }
        }
    }

    public void setTrapVertragingTicks(int vertraging) {
        this.trapVertragingTicks = vertraging;
    }
}