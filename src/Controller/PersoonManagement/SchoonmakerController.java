package Controller.PersoonManagement;

import Controller.Events.Interfaces.checkOutEvent;
import Controller.Events.Interfaces.cleaningEmergencyEvent;
import Controller.Layout.LayoutController;
import Controller.PersoonManagement.Interfaces.NewSchoonmaker;
import Controller.Systeem.Interfaces.onTimeChange;
import Controller.Systeem.Interfaces.reset;
import Controller.Timer.WachtTimer;
import Model.Layout.Locatie;
import Model.Entiteiten.EntiteitenModel;
import Model.Entiteiten.SchoonmakerModel;
import Controller.PersoonFactory.SchoonmakerCreator;
import Model.Entiteiten.TypePersoon;
import Model.Ruimtes.KamerModel;
import View.Systeem.OverzichtView;
import View.Systeem.TijdsDuur;
import Controller.Systeem.Interfaces.settingsListener;
import hotelevents.HotelEvent;

import javax.swing.SwingUtilities;
import java.util.*;

public class SchoonmakerController extends EntiteitenController implements cleaningEmergencyEvent, checkOutEvent, onTimeChange, reset, settingsListener {

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
    private final HashMap<TijdsDuur, Integer> maxTijdEmergency;
    private final HashMap<TijdsDuur, Integer> minTijdEmergency;
    private final HashMap<TijdsDuur, Integer> maxTijdCheckout;
    private final HashMap<TijdsDuur, Integer> minTijdCheckout;
    private TijdsDuur cleanDuur = TijdsDuur.NORMAAL;

    public SchoonmakerController(ReceptieController rec, OverzichtView overzichtView, WachtTimer wachtTimer) {
        super();
        this.receptieController = rec;
        this.overzichtView = overzichtView;
        this.listeners = new ArrayList<>();
        this.factory = new SchoonmakerCreator();
        this.taakWachtrijen = new HashMap<>();
        this.actieveKlussen = new HashMap<>();
        this.wachtTimer = wachtTimer;

        this.maxTijdEmergency = new HashMap<>();
        this.minTijdEmergency = new HashMap<>();

        minTijdEmergency.put(TijdsDuur.LANG, 17);
        minTijdEmergency.put(TijdsDuur.NORMAAL, 10);
        minTijdEmergency.put(TijdsDuur.KORT, 7);

        maxTijdEmergency.put(TijdsDuur.LANG, 27);
        maxTijdEmergency.put(TijdsDuur.NORMAAL, 16);
        maxTijdEmergency.put(TijdsDuur.KORT, 11);

        this.maxTijdCheckout = new HashMap<>();
        this.minTijdCheckout = new HashMap<>();

        minTijdCheckout.put(TijdsDuur.LANG, 9);
        minTijdCheckout.put(TijdsDuur.NORMAAL, 5);
        minTijdCheckout.put(TijdsDuur.KORT, 3);

        maxTijdCheckout.put(TijdsDuur.LANG, 14);
        maxTijdCheckout.put(TijdsDuur.NORMAAL, 8);
        maxTijdCheckout.put(TijdsDuur.KORT, 6);
    }

    // plaats schoonmakers zodra de layout geladen is
    @Override
    public void onLayoutGeladen(LayoutController controller) {
        super.onLayoutGeladen(controller);

        int breedte = layoutController.getView().getGridBreedte();
        int lengte = layoutController.getView().getGridLengte();

        schoonmaker1 = (SchoonmakerModel) factory.createEntiteit(1, new Locatie(breedte / 2, 0), null, 0, new Locatie(breedte / 2, 0), TypePersoon.SCHOONMAKER);
        schoonmaker2 = (SchoonmakerModel) factory.createEntiteit(2, new Locatie(breedte / 2, lengte / 2), null, 0, new Locatie(breedte / 2, lengte / 2), TypePersoon.SCHOONMAKER);

        actieveEntiteiten.put(schoonmaker1.getID(), schoonmaker1);
        actieveEntiteiten.put(schoonmaker2.getID(), schoonmaker2);

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
        KamerModel kamer = receptieController.getGast(hotelEvent.getGuestId()).getKamer();
        if (kamer == null) return;

        SchoonmakerModel gekozen = kiesSchoonmaker(kamer);

        int min = minTijdEmergency.get(cleanDuur);
        int max = maxTijdEmergency.get(cleanDuur);

        int benodigdeTijd;
        if (kamer.getDimensionH() == 1 && kamer.getDimensionW() == 1){
            int berekendMin = (int)(min * 0.6);
            int berekendMax = Math.max(berekendMin + 1, (int)(max * 0.6));
            benodigdeTijd = rand.nextInt(berekendMin, berekendMax);
        } else if (kamer.getDimensionH() == 2 && kamer.getDimensionW() == 2){
            benodigdeTijd = rand.nextInt(min, max);
        } else {
            benodigdeTijd = rand.nextInt((int)(min * 1.6), (int)(max * 1.6));
        }

        verwerkNieuweTaak(gekozen, kamer, benodigdeTijd);
    }

    // bij een checkout event kies het aantal ticks om schoon te maken en kies een schoonmaker voor de klus
    @Override
    public void checkOut(HotelEvent hotelEvent) {
        KamerModel kamer = receptieController.getGast(hotelEvent.getGuestId()).getKamer();
        if (kamer == null) return;

        SchoonmakerModel gekozen = kiesSchoonmaker(kamer);

        int min = minTijdCheckout.get(cleanDuur);
        int max = maxTijdCheckout.get(cleanDuur);

        int benodigdeTijd;
        if (kamer.getDimensionH() == 1 && kamer.getDimensionW() == 1) {
            int berekendMin = (int)(min * 0.6);
            int berekendMax = Math.max(berekendMin + 1, (int)(max * 0.6));
            benodigdeTijd = rand.nextInt(berekendMin, berekendMax);
        } else if (kamer.getDimensionH() == 2 && kamer.getDimensionW() == 2) {
            benodigdeTijd = rand.nextInt(min, max);
        } else {
            benodigdeTijd = rand.nextInt((int)(min * 1.6), (int)(max * 1.6));
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
    public void onStepTaken(EntiteitenModel Entiteit, Locatie oudeLocatie) {
        if (overzichtView != null && overzichtView.isGepauzeerd()) {
            return;
        }

        SchoonmakerModel schoonmaker = (SchoonmakerModel) Entiteit;
        SwingUtilities.invokeLater(() -> {
            for (NewSchoonmaker listener : listeners) {
                listener.onSchoonmakerVerplaatst(schoonmaker, oudeLocatie);
            }
            updateOverzichtView();
        });
    }

    // als de schoonmaker is aangekomen in de kamer start een timer voor hoe lang ze moeten schoonmaken
    @Override
    public void onDestinationReached(EntiteitenModel Entiteit) {
        // bij pauze, niet bewegen
        if (overzichtView != null && overzichtView.isGepauzeerd()) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            SchoonmakerModel schoonmaker = (SchoonmakerModel) Entiteit;

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

            actieveEntiteiten.put(schoonmaker1.getID(), schoonmaker1);
            actieveEntiteiten.put(schoonmaker2.getID(), schoonmaker2);

            SwingUtilities.invokeLater(() -> {
                for (NewSchoonmaker listener : listeners) {
                    listener.onSchoonmakerVerplaatst(schoonmaker1, oudeLoc1);
                    listener.onSchoonmakerVerplaatst(schoonmaker2, oudeLoc2);
                }
            });
        }

        updateOverzichtView();
    }

    @Override
    public void schoonmaakTijdVeranderd(TijdsDuur tijdsDuur) {
        this.cleanDuur = tijdsDuur;
    }

    @Override
    public void filmDuurVeranderd(TijdsDuur tijdsDuur) {

    }

    @Override
    public void aantalSchoonmakersVeranderd(int aantalSchoonmakers) {

    }

    @Override
    public void restaurantCapaciteitVeranderd(int restaurantCapaciteit) {

    }

    @Override
    public void trapLoopDuurVeranderd(int trapLoopDuur) {

    }

    @Override
    public void gastMaxWachttijdVeranderd(int gastMaxWachttijd) {

    }

    public int getActieveKlussenAantal() {
        return this.actieveKlussen.size();
    }
}