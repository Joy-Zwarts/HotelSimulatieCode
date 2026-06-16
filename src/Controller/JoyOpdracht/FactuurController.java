package Controller.JoyOpdracht;

import Controller.Events.Interfaces.checkOutEvent;
import Controller.Events.Interfaces.noneEvent;
import Controller.Layout.Interfaces.LayoutGeladen;
import Controller.Layout.LayoutController;
import Controller.PersoonManagement.Interfaces.NewGast;
import Model.Entiteiten.GastModel;
import Model.Layout.LayoutModel;
import Model.Layout.Locatie;
import Model.Ruimtes.KamerClassificatie;
import Model.Ruimtes.RuimteModel;
import View.JoyOpdracht.FactuurPrint;
import View.JoyOpdracht.NewDag;
import hotelevents.HotelEvent;

import java.util.HashMap;
import java.util.Random;

public class FactuurController implements NewGast, LayoutGeladen, NewDag, checkOutEvent, noneEvent {

    // attributen
    private final Random rand = new Random();
    private LayoutModel layoutModel;
    private final HashMap<GastModel, HashMap<String, Integer>> gastenInSysteem;
    private final HashMap<KamerClassificatie, Integer> prijsPerNacht;
    private final HashMap<GastModel, String> checkInTijdstip;
    private final HashMap<GastModel, String> checkOutTijdstip;
    private Locatie uitgang;
    private int huidigeDag = 1;
    private int huidigeTicks = 0;
    private final FactuurPrint factuurPrint;

    // constructor
    public FactuurController(FactuurPrint factuurPrint) {
        gastenInSysteem = new HashMap<>();
        prijsPerNacht = new HashMap<>();
        checkInTijdstip = new HashMap<>();
        checkOutTijdstip = new HashMap<>();

        this.factuurPrint = factuurPrint;

        prijsPerNacht.put(KamerClassificatie.eenSter, 75);
        prijsPerNacht.put(KamerClassificatie.tweeSterren, 110);
        prijsPerNacht.put(KamerClassificatie.drieSterren, 160);
        prijsPerNacht.put(KamerClassificatie.vierSterren, 240);
        prijsPerNacht.put(KamerClassificatie.vijfSterren, 350);
    }

    // voegt kosten toe aan de rekening van de gast met de bijbehorende omschrijving en prijs
    private void voegKostenToe(GastModel gast, String omschrijving, int bedrag) {
        // kijk of de gast al in het systeem zit
        if (!gastenInSysteem.containsKey(gast)) {
            gastenInSysteem.put(gast, new HashMap<>());
        }
        // pak de gast
        HashMap<String, Integer> factuurVanGast = gastenInSysteem.get(gast);

        int oudBedrag = 0;

        // kijk of er al een zelfde kost op de rekening staat van die gast om de binnenkomende kost daar bij op te tellen
        if (factuurVanGast.containsKey(omschrijving)) {
            oudBedrag = factuurVanGast.get(omschrijving);
        }
        int nieuwBedrag = oudBedrag + bedrag;

        // zet het op de rekening van de gast
        factuurVanGast.put(omschrijving, nieuwBedrag);
    }

    // zoekt een gast in het systeem bij hun ID
    private GastModel vindGastBijId(int id) {
        for (GastModel gast : gastenInSysteem.keySet()) {
            if (gast.getID() == id) {
                return gast;
            }
        }
        return null;
    }

    // kijkt in wat voor sterren kamer de gast zit, rekent de overnachtingsprijs per nacht en zet deze op hun rekening
    private void rekenKamerPrijsVoorGast(GastModel gast) {
        if (gast.getKamer() != null) {
            KamerClassificatie aantalSterren = gast.getKamer().getClassification();

            int nachtPrijs = prijsPerNacht.get(aantalSterren);
            int nachtNummer = 1;
            HashMap<String, Integer> factuur = gastenInSysteem.get(gast);

            if (factuur != null) {
                // kijkt of de gast al een nacht heeft verbleven, zo ja, dan zet je het er in als een dagje meer dan de vorige nacht
                for (String omschrijving : factuur.keySet()) {
                    if (omschrijving.contains("Overnachting")) {
                        nachtNummer++;
                    }
                }
            }

            // voeg het toe aan de gast hun rekening
            String omschrijving = "Overnachting NACHT " + nachtNummer + " (" + aantalSterren + ")";
            voegKostenToe(gast, omschrijving, nachtPrijs);
        }
    }

    // bereken de kosten voor gebruik van de faciliteiten en voeg deze kosten toe aan de rekening van de gast

    public void gastGaatNaarBios(GastModel gast) {
        int randomKostBioscoop = rand.nextInt(20, 25);
        voegKostenToe(gast, "Prijs voor Bioscoopbezoek", randomKostBioscoop);
    }

    public void gastGaatNaarRestaurant(GastModel gast) {
        int randomKostRestaurant = rand.nextInt(35, 110);
        voegKostenToe(gast, "Prijs voor Restaurantbezoek", randomKostRestaurant);
    }

    public void gastGaatNaarGym(GastModel gast) {
        voegKostenToe(gast, "Prijs voor Gym bezoek", 10);
    }

    // bij check out, pak alle nodige attributen van die gast en print hun bon met de FactuurPrint klasse
    public void checkOutGast(GastModel gast) {
        HashMap<String, Integer> factuur = gastenInSysteem.get(gast);

        // pak de opgeslagen tijden
        String checkIn = checkInTijdstip.get(gast);
        String checkOut = checkOutTijdstip.get(gast);

        factuurPrint.PrintBon(factuur, gast, checkIn, checkOut);

        gastenInSysteem.remove(gast);
        checkInTijdstip.remove(gast);
        checkOutTijdstip.remove(gast);
    }

    // registreer de checkout tijd van de gast en handel e rest van de checkout af met CheckOutGast
    @Override
    public void checkOut(HotelEvent hotelEvent) {
        int gastId = hotelEvent.getGuestId();
        GastModel gast = vindGastBijId(gastId);

        if (gast != null) {
            // registreer de check out tijd
            checkOutTijdstip.put(gast, getDatum(hotelEvent.getTime()));
            checkOutGast(gast);
        }
    }

    // registreer de check in tijd en reken de kamerprijs voor de gast voor de eerste nacht
    @Override
    public void onGastAangemaakt(GastModel gast) {
        if (!gastenInSysteem.containsKey(gast)) {
            gastenInSysteem.put(gast, new HashMap<>());
        }
        // registreer de check in tijd
        checkInTijdstip.put(gast, getDatum(huidigeTicks));

        voegKostenToe(gast, "Administratiekosten", 5);
        rekenKamerPrijsVoorGast(gast);
    }

    // kijk of een gast een faciliteit is binnen gelopen en roep daarbij de bijpassende methode aan om de kosten te rekenen
    @Override
    public void onGastAangekomenInKamer(GastModel gast, Locatie behaaldeLocatie) {
        if (layoutModel == null) return;

        RuimteModel aankomstRuimte = layoutModel.getRuimteBijLocatie(behaaldeLocatie);

        if (aankomstRuimte == null) {
            if (behaaldeLocatie.equals(uitgang)) {
                checkOutGast(gast);
            }
            return;
        }

        switch (aankomstRuimte.getAreaType()) {
            case CINEMA:
                gastGaatNaarBios(gast);
                break;
            case RESTAURANT:
                gastGaatNaarRestaurant(gast);
                break;
            case FITNESS:
                gastGaatNaarGym(gast);
                break;
            default:
                if (behaaldeLocatie.equals(uitgang)) {
                    checkOutGast(gast);
                }
                break;
        }
    }

    // bereken de locatie van de uitgang zodra de layout is geladen
    @Override
    public void onLayoutGeladen(LayoutController layoutController) {
        this.layoutModel = layoutController.getModel();
        int x = layoutController.getView().getGridBreedte() / 2;
        int y = layoutController.getView().getGridLengte() - 1;
        uitgang = new Locatie(x, y);
    }

    // als de dag voorbij is pas de huidige dag aan en reken de prijs voor die nacht voor elke gast in het systeem
    @Override
    public void dagVoorbij(int dag) {
        this.huidigeDag = dag;
        for (GastModel gast : gastenInSysteem.keySet()) {
            rekenKamerPrijsVoorGast(gast);
        }
    }

    // bereken de datum op dezelfde manier als de time panel om de check in en out tijdstippen te berekenen
    public String getDatum(int verstrekenHTE){
        int dag = (int) (verstrekenHTE / 500) + 1;
        int ticksVandaag = (int) (verstrekenHTE % 500);

        double fractieVanDag = (double) ticksVandaag / 500;
        int totaleMinutenVandaag = (int) (fractieVanDag * 1440);

        int uren = totaleMinutenVandaag / 60;
        int minuten = totaleMinutenVandaag % 60;

        return String.format("Dag %d, %02d:%02d", dag, uren, minuten);
    }

    @Override
    public void onGastVertrokken(GastModel gast) {}
    @Override
    public void onGastVerplaatst(GastModel gast, Locatie oudeLocatie) {}
    @Override
    public void onGastGaatWegUitKamer(GastModel gast, Locatie oudeLocatie) {}

    // luister naar de HTE ticks om de huidige tijd exact te weten voor de check in- en out tijden te berekenen
    @Override
    public void HTETick(HotelEvent event) throws InterruptedException {
        huidigeTicks++;
    }
}