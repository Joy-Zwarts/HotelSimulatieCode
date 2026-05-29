package Controller.PersoonManagement;

import Controller.Events.Interfaces.checkOutEvent;
import Controller.Events.Interfaces.cleaningEmergencyEvent;
import Controller.Layout.LayoutController;
import Controller.PersoonManagement.Interfaces.NewSchoonmaker;
import Controller.Systeem.Intefaces.onTimeChange;
import Controller.Systeem.Intefaces.reset;
import Controller.Timer.WachtTimer;
import Model.Layout.Locatie;
import Model.Personen.PersoonModel;
import Model.Personen.SchoonmakerModel;
import Controller.PersoonFactory.SchoonmakerCreator;
import Model.Personen.TypePersoon;
import Model.Ruimtes.KamerModel;
import View.Systeem.OverzichtView;
import hotelevents.HotelEvent;

import javax.swing.SwingUtilities;
import java.util.*;

public class SchoonmakerController extends PersoonController implements cleaningEmergencyEvent, checkOutEvent, onTimeChange, reset {

    private final SchoonmakerCreator factory;
    private final ArrayList<NewSchoonmaker> listeners;
    private SchoonmakerModel schoonmaker1;
    private SchoonmakerModel schoonmaker2;
    private final ReceptieController receptieController;
    public Random rand = new Random();
    public OverzichtView overzichtView;
    private final HashMap<Integer, Queue<KamerModel>> taakWachtrijen;
    private final HashMap<Integer, KamerModel> actieveKlussen;
    private final WachtTimer wachtTimer;

    public SchoonmakerController(ReceptieController rec, OverzichtView overzichtView, WachtTimer wachtTimer) {
        super();
        this.receptieController = rec;
        this.overzichtView = overzichtView;
        this.listeners = new ArrayList<>();
        this.factory = new SchoonmakerCreator();
        this.taakWachtrijen = new HashMap<>();
        this.actieveKlussen = new HashMap<>();
        this.wachtTimer = wachtTimer;
    }

    // plaats schoonmakers zodra de layout geladen is
    @Override
    public void onLayoutGeladen(LayoutController controller) {
        super.onLayoutGeladen(controller);

        int breedte = layoutController.getView().getGridBreedte();
        int lengte = layoutController.getView().getGridLengte();

        schoonmaker1 = (SchoonmakerModel) factory.createPersoon(1, new Locatie(breedte / 2, 0), null, 0, new Locatie(breedte / 2, 0), TypePersoon.SCHOONMAKER);
        schoonmaker2 = (SchoonmakerModel) factory.createPersoon(2, new Locatie(breedte / 2, lengte / 2), null, 0, new Locatie(breedte / 2, lengte / 2), TypePersoon.SCHOONMAKER);

        actievePersonen.put(schoonmaker1.getID(), schoonmaker1);
        actievePersonen.put(schoonmaker2.getID(), schoonmaker2);

        taakWachtrijen.put(schoonmaker1.getID(), new LinkedList<>());
        taakWachtrijen.put(schoonmaker2.getID(), new LinkedList<>());

        // notify listeners
        for (NewSchoonmaker listener : listeners) {
            listener.onSchoonmakerAangemaakt(schoonmaker1);
            listener.onSchoonmakerAangemaakt(schoonmaker2);
        }
    }

    // maak de kamer van een gast met een cleaning emergency schoon
    @Override
    public void cleaningEmergency(HotelEvent hotelEvent) {
        // get de kamer
        KamerModel kamer = receptieController.getGast(hotelEvent.getGuestId()).getKamer();
        if (kamer == null) return;
        // kies schoonmaker
        SchoonmakerModel gekozen = kiesSchoonmaker(kamer);

        // bepaal random tijd
        int benodigdeTijd;
        if (kamer.getDimensionH() == 1 && kamer.getDimensionW() == 1){
            benodigdeTijd = rand.nextInt(6, 10);
        } else if (kamer.getDimensionH() == 2 && kamer.getDimensionW() == 2){
            benodigdeTijd = rand.nextInt(10, 16);
        } else {
            benodigdeTijd = rand.nextInt(16, 24);
        }

        verwerkNieuweTaak(gekozen, kamer, benodigdeTijd);
    }

    // boven > schoonmaker 1 | onder > schoonmaker 2
    private SchoonmakerModel kiesSchoonmaker(KamerModel kamer) {
        int kamerY = kamer.getPosition().getY();
        int helftVanHotel = layoutController.getView().getGridLengte() / 2;

        if (kamerY < helftVanHotel) {
            return schoonmaker1;
        } else {
            return schoonmaker2;
        }
    }

    // bij een checkout, maak die kamer schoon
    @Override
    public void checkOut(HotelEvent hotelEvent) {
        // get de kamer van de gast
        KamerModel kamer = receptieController.getGast(hotelEvent.getGuestId()).getKamer();
        if (kamer == null) return;
        // kies schoonmaker voor de klus
        SchoonmakerModel gekozen = kiesSchoonmaker(kamer);

        // bereken random tijd
        int benodigdeTijd;
        if (kamer.getDimensionH() == 1 && kamer.getDimensionW() == 1) {
            benodigdeTijd = rand.nextInt(3, 5);
        } else if (kamer.getDimensionH() == 2 && kamer.getDimensionW() == 2) {
            benodigdeTijd = rand.nextInt(5, 8);
        } else {
            benodigdeTijd = rand.nextInt(8, 12);
        }

        verwerkNieuweTaak(gekozen, kamer, benodigdeTijd);
    }

    // check of de schoonmaker al bezig is met een taak als er een nieuwe taak binnen komt
    private synchronized void verwerkNieuweTaak(SchoonmakerModel schoonmaker, KamerModel kamer, int benodigdeTijd) {
        boolean isAlBezig = actieveKlussen.containsKey(schoonmaker.getID()) || !taakWachtrijen.get(schoonmaker.getID()).isEmpty();

        // zo ja zet die in de wachtrij
        if (isAlBezig) {
            taakWachtrijen.get(schoonmaker.getID()).add(kamer);
            System.out.println("Schoonmaker " + schoonmaker.getID() + " bezet. Kamer " + kamer.getRoomNumber() + " in queue gezet.");
        } else { // zo nee, stuur hun er gelijk heen
            stuurSchoonmakerNaarKamer(schoonmaker, kamer, benodigdeTijd);
        }

        updateOverzichtView();
    }

    // zet de schoonmaker met de schoonmaak klus (kamer) in de lijst en stuur hun daarheen
    private void stuurSchoonmakerNaarKamer(SchoonmakerModel schoonmaker, KamerModel kamer, int benodigdeTijd) {
        actieveKlussen.put(schoonmaker.getID(), kamer);

        schoonmaker.setCleaning(false);
        schoonmaker.setSchoonmaakTijd(benodigdeTijd);

        SwingUtilities.invokeLater(() -> {
            Locatie target = new Locatie(kamer.getPosition().getX(), kamer.getPosition().getY() - 1);
            PathFinder pf = new PathFinder(schoonmaker.getLocatie(), target, layoutController);
            if (beweegHelper != null) {
                beweegHelper.voegRouteToe(schoonmaker, pf);
            }
        });
    }

    // kijk of de schoonmaker klaar is met schoonmaken
    public synchronized void klaarCleaning(SchoonmakerModel schoonmaker) {
        schoonmaker.setCleaning(false);
        actieveKlussen.remove(schoonmaker.getID());

        // laat de listeners weten
        SwingUtilities.invokeLater(() -> {
            for (NewSchoonmaker listener : listeners) {
                listener.onSchoonmakerVerlaatKamer(schoonmaker, schoonmaker.getLocatie());
            }
        });

        //  pak de wachtrij van klussen voor die schoonmaker
        Queue<KamerModel> wachtrij = taakWachtrijen.get(schoonmaker.getID());

        // als de wachtrij nog een taak heeft ga daar heen
        if (wachtrij != null && !wachtrij.isEmpty()) {
            KamerModel volgendeKamer = wachtrij.poll();
            int opvolgendeTijd = rand.nextInt(4, 8);
            stuurSchoonmakerNaarKamer(schoonmaker, volgendeKamer, opvolgendeTijd);
            // als er geen taken meer in de wachtrij staan ga dan terug naar je station
        } else {
            SwingUtilities.invokeLater(() -> {
                PathFinder pf = new PathFinder(schoonmaker.getLocatie(), schoonmaker.getStation(), layoutController);
                if (beweegHelper != null) {
                    beweegHelper.voegRouteToe(schoonmaker, pf);
                }
            });
        }
        updateOverzichtView();
    }

    // per stap laat de listeners dat weten en stop bij pauze
    @Override
    public void onStepTaken(PersoonModel persoon, Locatie oudeLocatie) {
        if (overzichtView != null && overzichtView.isGepauzeerd()) {
            return;
        }

        SchoonmakerModel schoonmaker = (SchoonmakerModel) persoon;
        SwingUtilities.invokeLater(() -> {
            for (NewSchoonmaker listener : listeners) {
                listener.onSchoonmakerVerplaatst(schoonmaker, oudeLocatie);
            }
            updateOverzichtView();
        });
    }

    // als de schoonmaker is aangekomen in de kamer start een timer voor hoe lang ze moeten schoonmaken
    @Override
    public void onDestinationReached(PersoonModel persoon) {
        // bij pauze, niet bewegen
        if (overzichtView != null && overzichtView.isGepauzeerd()) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            SchoonmakerModel schoonmaker = (SchoonmakerModel) persoon;

            // als de schoonmaker bij de station is aangekomen remove de schoonmaker van de actieve klussen
            if (schoonmaker.getLocatie().equals(schoonmaker.getStation())) {
                actieveKlussen.remove(schoonmaker.getID());
                updateOverzichtView();
                return;
            }

            schoonmaker.setCleaning(true);

            String uniekeID = schoonmaker.getTypePersoon().name() + "-" + schoonmaker.getID();

            // start een nieuwe WachtTimer voor deze schoonmaker
            wachtTimer.startTimer(uniekeID, () -> klaarCleaning(schoonmaker), schoonmaker.getSchoonmaakTijd());

            // listener ping
            for (NewSchoonmaker listener : listeners) {
                listener.onSchoonmakerAangekomenInKamer(schoonmaker);
            }
            updateOverzichtView();
        });
    }

    // laat de schoonmakers bewegen op de snelheid van de timer
    @Override
    public void timeChange(int HTE) {
        if (beweegHelper != null) {
            beweegHelper.setSpeed(HTE);
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

    @Override
    public void resetSimulatie() {
        super.resetController();

        if (this.taakWachtrijen != null) {
            for (Queue<KamerModel> queue : taakWachtrijen.values()) {
                if (queue != null) queue.clear();
            }
        }
        if (this.actieveKlussen != null) {
            this.actieveKlussen.clear();
        }

        if (schoonmaker1 != null && schoonmaker2 != null) {
            schoonmaker1.setCleaning(false);
            schoonmaker1.setSchoonmaakTijd(0);

            schoonmaker2.setCleaning(false);
            schoonmaker2.setSchoonmaakTijd(0);

            Locatie oudeLoc1 = new Locatie(schoonmaker1.getLocatie().getX(), schoonmaker1.getLocatie().getY());
            Locatie oudeLoc2 = new Locatie(schoonmaker2.getLocatie().getX(), schoonmaker2.getLocatie().getY());

            schoonmaker1.setLocatie(new Locatie(schoonmaker1.getStation().getX(), schoonmaker1.getStation().getY()));
            schoonmaker2.setLocatie(new Locatie(schoonmaker2.getStation().getX(), schoonmaker2.getStation().getY()));

            actievePersonen.put(schoonmaker1.getID(), schoonmaker1);
            actievePersonen.put(schoonmaker2.getID(), schoonmaker2);

            SwingUtilities.invokeLater(() -> {
                for (NewSchoonmaker listener : listeners) {
                    listener.onSchoonmakerVerplaatst(schoonmaker1, oudeLoc1);
                    listener.onSchoonmakerVerplaatst(schoonmaker2, oudeLoc2);
                }
            });
        }

        updateOverzichtView();
    }
}