package Controller.PersoonManagement;

import Controller.Events.checkOutEvent;
import Controller.Events.cleaningEmergencyEvent;
import Controller.Events.noneEvent;
import Controller.Layout.LayoutController;
import Controller.Systeem.onTimeChange;
import Model.Layout.Locatie;
import Model.Personen.PersoonModel;
import Model.Personen.SchoonmakerModel;
import Controller.PersoonFactory.SchoonmakerCreator;
import Model.Ruimtes.KamerModel;
import View.Systeem.OverzichtView;
import hotelevents.HotelEvent;
import hotelevents.HotelEventManager;

import javax.swing.SwingUtilities;
import java.util.*;

public class SchoonmakerController extends PersoonController implements cleaningEmergencyEvent, noneEvent, checkOutEvent, onTimeChange {

    private final SchoonmakerCreator factory;
    private final ArrayList<NewSchoonmaker> listeners;
    private SchoonmakerModel schoonmaker1;
    private SchoonmakerModel schoonmaker2;
    private final ReceptieController receptieController;
    public Random rand = new Random();
    public OverzichtView overzichtView;
    private final HashMap<Integer, Queue<KamerModel>> taakWachtrijen;
    private final HashMap<Integer, KamerModel> actieveKlussen;

    public SchoonmakerController(ReceptieController rec, OverzichtView overzichtView) {
        super();
        this.receptieController = rec;
        this.overzichtView = overzichtView;
        this.listeners = new ArrayList<>();
        this.factory = new SchoonmakerCreator();
        this.taakWachtrijen = new HashMap<>();
        this.actieveKlussen = new HashMap<>();
    }

    @Override
    public void onLayoutGeladen(LayoutController controller) {
        super.onLayoutGeladen(controller);

        int breedte = layoutController.getView().getGridBreedte();
        int lengte = layoutController.getView().getGridLengte();

        schoonmaker1 = (SchoonmakerModel) factory.createPersoon(1, new Locatie(breedte / 2, 0), null, 0, new Locatie(breedte / 2, 0));
        schoonmaker2 = (SchoonmakerModel) factory.createPersoon(2, new Locatie(breedte / 2, lengte / 2), null, 0, new Locatie(breedte / 2, lengte / 2));

        actievePersonen.put(schoonmaker1.getID(), schoonmaker1);
        actievePersonen.put(schoonmaker2.getID(), schoonmaker2);

        taakWachtrijen.put(schoonmaker1.getID(), new LinkedList<>());
        taakWachtrijen.put(schoonmaker2.getID(), new LinkedList<>());

        for (NewSchoonmaker listener : listeners) {
            listener.onSchoonmakerAangemaakt(schoonmaker1);
            listener.onSchoonmakerAangemaakt(schoonmaker2);
        }
    }

    @Override
    public void cleaningEmergencyEvent(HotelEvent hotelEvent) {
        KamerModel kamer = receptieController.getGast(hotelEvent.getGuestId()).getKamer();
        if (kamer == null) return;
        SchoonmakerModel gekozen = kiesSchoonmaker(kamer);

        int benodigdeTijd = 0;
        if (kamer.getDimensionH() == 1 && kamer.getDimensionW() == 1){
            benodigdeTijd = rand.nextInt(6, 10);
        } else if (kamer.getDimensionH() == 2 && kamer.getDimensionW() == 2){
            benodigdeTijd = rand.nextInt(10, 16);
        } else {
            benodigdeTijd = rand.nextInt(16, 24);
        }

        verwerkNieuweTaak(gekozen, kamer, benodigdeTijd);
    }

    @Override
    public void checkOutEvent(HotelEvent hotelEvent) {
        KamerModel kamer = receptieController.getGast(hotelEvent.getGuestId()).getKamer();
        if (kamer == null) return;
        SchoonmakerModel gekozen = kiesSchoonmaker(kamer);

        int benodigdeTijd = 0;
        if (kamer.getDimensionH() == 1 && kamer.getDimensionW() == 1){
            benodigdeTijd = rand.nextInt(3, 5);
        } else if (kamer.getDimensionH() == 2 && kamer.getDimensionW() == 2){
            benodigdeTijd = rand.nextInt(5, 8);
        } else {
            benodigdeTijd = rand.nextInt(8, 12);
        }

        verwerkNieuweTaak(gekozen, kamer, benodigdeTijd);
    }

    private SchoonmakerModel kiesSchoonmaker(KamerModel kamer) {
        int kamerY = kamer.getPosition().getY();
        int helftVanHotel = layoutController.getView().getGridLengte() / 2;

        if (kamerY < helftVanHotel) {
            return schoonmaker1;
        } else {
            return schoonmaker2;
        }
    }

    private synchronized void verwerkNieuweTaak(SchoonmakerModel schoonmaker, KamerModel kamer, int benodigdeTijd) {
        boolean isAlBezig = actieveKlussen.containsKey(schoonmaker.getID()) || !taakWachtrijen.get(schoonmaker.getID()).isEmpty();

        if (isAlBezig) {
            taakWachtrijen.get(schoonmaker.getID()).add(kamer);
            System.out.println("Schoonmaker " + schoonmaker.getID() + " bezet. Kamer " + kamer.getRoomNumber() + " in queue gezet.");
        } else {
            stuurSchoonmakerNaarKamer(schoonmaker, kamer, benodigdeTijd);
        }

        updateOverzichtView();
    }

    private void stuurSchoonmakerNaarKamer(SchoonmakerModel schoonmaker, KamerModel kamer, int benodigdeTijd) {
        actieveKlussen.put(schoonmaker.getID(), kamer);

        schoonmaker.setHuidigeSchoonmaakTijd(0);
        schoonmaker.setCleaning(false);
        schoonmaker.setSchoonmaakTijd(benodigdeTijd);

        SwingUtilities.invokeLater(() -> {
            Locatie target = new Locatie(kamer.getPosition().getX(), kamer.getPosition().getY() - 1);
            PathFinder pf = new PathFinder(schoonmaker.getLocatie(), target, layoutController);
            if (movementEngine != null) {
                movementEngine.voegRouteToe(schoonmaker, pf);
            }
        });
    }

    public synchronized void checkKlaarCleaning(SchoonmakerModel schoonmaker) {
        if (schoonmaker.getHuidigeSchoonmaakTijd() >= schoonmaker.getSchoonmaakTijd()) {
            schoonmaker.setCleaning(false);
            actieveKlussen.remove(schoonmaker.getID());

            SwingUtilities.invokeLater(() -> {
                for (NewSchoonmaker listener : listeners) {
                    listener.onSchoonmakerVerlaatKamer(schoonmaker, schoonmaker.getLocatie());
                }
            });

            Queue<KamerModel> wachtrij = taakWachtrijen.get(schoonmaker.getID());

            if (wachtrij != null && !wachtrij.isEmpty()) {
                KamerModel volgendeKamer = wachtrij.poll();
                int opvolgendeTijd = rand.nextInt(4, 8);
                stuurSchoonmakerNaarKamer(schoonmaker, volgendeKamer, opvolgendeTijd);
            } else {
                SwingUtilities.invokeLater(() -> {
                    PathFinder pf = new PathFinder(schoonmaker.getLocatie(), schoonmaker.getStation(), layoutController);
                    if (movementEngine != null) {
                        movementEngine.voegRouteToe(schoonmaker, pf);
                    }
                });
            }
            updateOverzichtView();
        }
    }

    @Override
    public void onStepTaken(PersoonModel persoon, Locatie oudeLocatie) {
        // stop met bewegen als de simulatie gepauzeerd is
        if (overzichtView != null && overzichtView.isGepauzeerd()) {
            return;
        }

        SchoonmakerModel sm = (SchoonmakerModel) persoon;
        SwingUtilities.invokeLater(() -> {
            for (NewSchoonmaker listener : listeners) {
                listener.onSchoonmakerVerplaatst(sm, oudeLocatie);
            }
            updateOverzichtView();
        });
    }

    @Override
    public void onDestinationReached(PersoonModel persoon) {
        if (overzichtView != null && overzichtView.isGepauzeerd()) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            SchoonmakerModel sm = (SchoonmakerModel) persoon;

            if (sm.getLocatie().equals(sm.getStation())) {
                actieveKlussen.remove(sm.getID());
                updateOverzichtView();
                return;
            }

            sm.setCleaning(true);

            for (NewSchoonmaker listener : listeners) {
                listener.onSchoonmakerAangekomenInKamer(sm);
            }
            updateOverzichtView();
        });
    }

    @Override
    public void noneEvent(HotelEvent event) {
        // als het hotel op pauze staat geen ticks verwerken
        if (overzichtView != null && overzichtView.isGepauzeerd()) {
            return;
        }
        verwerkTick(schoonmaker1);
        verwerkTick(schoonmaker2);
    }

    private void verwerkTick(SchoonmakerModel schoonmaker) {
        if (schoonmaker != null && schoonmaker.isCleaning()) {
            schoonmaker.setHuidigeSchoonmaakTijd(schoonmaker.getHuidigeSchoonmaakTijd() + 1);
            checkKlaarCleaning(schoonmaker);
            updateOverzichtView();
        }
    }

    @Override
    public void timeChange(int HTE) {
        if (movementEngine != null) {
            movementEngine.setSpeed(HTE);
        }
        updateOverzichtView();
    }

    private void updateOverzichtView() {
        if (overzichtView != null) {
            SwingUtilities.invokeLater(() -> overzichtView.tekenSchoonmakerStatus(this));
        }
    }

    // getters & setters

    public SchoonmakerModel getSchoonmaker1() { return schoonmaker1; }
    public SchoonmakerModel getSchoonmaker2() { return schoonmaker2; }
    public Queue<KamerModel> getWachtrij(int id) { return taakWachtrijen.get(id); }
    public KamerModel getActieveKlus(int id) { return actieveKlussen.get(id); }
    public void setNewSchoonmakerListener(NewSchoonmaker listener) {
        listeners.add(listener);
    }
}