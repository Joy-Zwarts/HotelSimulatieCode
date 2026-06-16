package Controller.JoyOpdracht;

import Controller.Events.Interfaces.checkOutEvent;
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
import java.util.Map;
import java.util.Random;

public class FactuurController implements NewGast, LayoutGeladen, NewDag, checkOutEvent {

    // attributen

    private final Random rand = new Random();
    private LayoutModel layoutModel;

    // per gast wordt er een lijstje bijgehouden van de naam van de kost en het bedrag
    private final HashMap<GastModel, HashMap<String, Integer>> gastenInSysteem;

    //  lijstje om op te zoeken hoeveel een kamer per ster kost
    private final HashMap<KamerClassificatie, Integer> prijsPerNacht;
    private Locatie uitgang;
    private int huidigeDag = 1;
    private FactuurPrint factuurPrint;

    // constructor
    public FactuurController(FactuurPrint factuurPrint) {
        gastenInSysteem = new HashMap<>();
        prijsPerNacht = new HashMap<>();

        this.factuurPrint = factuurPrint;

        prijsPerNacht.put(KamerClassificatie.eenSter, 75);
        prijsPerNacht.put(KamerClassificatie.tweeSterren, 110);
        prijsPerNacht.put(KamerClassificatie.drieSterren, 160);
        prijsPerNacht.put(KamerClassificatie.vierSterren, 240);
        prijsPerNacht.put(KamerClassificatie.vijfSterren, 350);
    }


    // voegt kosten toe aan de rekening van een gast
    private void voegKostenToe(GastModel gast, String omschrijving, int bedrag) {
        // kijkt of de gast al een rekening heeft, zo niet, maak dan een lege rekening
        if (!gastenInSysteem.containsKey(gast)) {
            gastenInSysteem.put(gast, new HashMap<>());
        }

        // pak de rekening van deze gast erbij
        HashMap<String, Integer> factuurVanGast = gastenInSysteem.get(gast);

        // kijkt of deze kost al op de rekening staat zodat je ze bij elkaar op kan tellen
        int oudBedrag = 0;
        if (factuurVanGast.containsKey(omschrijving)) {
            oudBedrag = factuurVanGast.get(omschrijving);
        }

        // tel het nieuwe bedrag op bij het oude bedrag en sla het op
        int nieuwBedrag = oudBedrag + bedrag;
        factuurVanGast.put(omschrijving, nieuwBedrag);
    }

    // zoek het GastModel bij hun ID zo nodig
    private GastModel vindGastBijId(int id) {
        for (GastModel gast : gastenInSysteem.keySet()) {
            if (gast.getID() == id) {
                return gast;
            }
        }
        return null;
    }

    // berekent de prijs van de kamer op basis van hoeveel nachten de gast er al slaapt
    private void rekenKamerPrijsVoorGast(GastModel gast) {
        if (gast.getKamer() != null) {
            // kijk hoeveel sterren de kamer heeft en zoek de prijs op
            KamerClassificatie aantalSterren = gast.getKamer().getClassification();
            int nachtPrijs = prijsPerNacht.getOrDefault(aantalSterren, 0);

            // tel de hoeveelste nacht dit wordt voor deze specifieke gast
            int nachtNummer = 1;
            HashMap<String, Integer> factuur = gastenInSysteem.get(gast);

            // we lopen door de huidige rekening van de gast om te kijken hoeveel overnachtingen er al op staan
            if (factuur != null) {
                for (String omschrijving : factuur.keySet()) {
                    if (omschrijving.contains("Overnachting")) {
                        nachtNummer++; // elke overnachtings entry die wordt gevonden, betekent een nacht extra
                    }
                }
            }

            String omschrijving = "Overnachting " + nachtNummer + " (" + aantalSterren + ")";

            // voeg de kosten toe
            voegKostenToe(gast, omschrijving, nachtPrijs);
            System.out.println("Gast: " + gast.getID() + " betaalt voor nacht #" + nachtNummer);
        }
    }

    // als de gast naar de bios gaat, bereken een random prijs en voeg deze toe
    public void gastGaatNaarBios(GastModel gast) {
        int maxPrijsBioscoop = 25;
        int minPrijsBioscoop = 20;

        int randomKostBioscoop = rand.nextInt(minPrijsBioscoop, maxPrijsBioscoop);
        voegKostenToe(gast, "Prijs voor Bioscoopbezoek", randomKostBioscoop);

        System.out.println("Gast: " + gast.getID() + " heeft een bedrag van " + randomKostBioscoop + " op zijn rekening gekregen voor zijn Bioscoopbezoek");
    }

    // als de gast naar het restaurant gaat, bereken een random prijs en voeg deze toe
    public void gastGaatNaarRestaurant(GastModel gast) {
        int minPrijsRestaurant = 35;
        int maxPrijsRestaurant = 110;

        int randomKostRestaurant = rand.nextInt(minPrijsRestaurant, maxPrijsRestaurant);
        voegKostenToe(gast, "Prijs voor Restaurantbezoek", randomKostRestaurant);

        System.out.println("Gast: " + gast.getID() + " heeft een bedrag van " + randomKostRestaurant + " op zijn rekening gekregen voor zijn Restaurantbezoek");
    }

    // voeg de prijs van een gym bezoek toe aan de gast zijn rekening
    public void gastGaatNaarGym(GastModel gast) {
        int prijsPerBezoekGym = 10;
        voegKostenToe(gast, "Prijs voor Gym bezoek", prijsPerBezoekGym);

        System.out.println("Gast: " + gast.getID() + " heeft een bedrag van " + prijsPerBezoekGym + " op zijn rekening gekregen voor zijn Gym bezoek");
    }

    // als de gast uitcheckt, print zijn vacature uit
    public void checkOutGast(GastModel gast) {
        // pak de rekening van de gast
        HashMap<String, Integer> factuur = gastenInSysteem.get(gast);

        // print de bon visueel uit in swing met de factuurPrint klasse
        factuurPrint.PrintBon(factuur, gast);

        // de gast heeft betaald, dus we halen hem uit het rekeningsysteem
        gastenInSysteem.remove(gast);
    }


    // als een gast een checkout event krijgt, laat hem afrekenen met de methode
    @Override
    public void checkOut(HotelEvent hotelEvent) {
        int gastId = hotelEvent.getGuestId();
        GastModel gast = vindGastBijId(gastId);

        if (gast != null) {
            System.out.println("CheckOut-event ontvangen via HotelEventManager voor gast: " + gastId);
            checkOutGast(gast);
        }
    }

    // maak per nieuwe gast, maak een rekening voor hun aan met 5 euro administratiekosten up front
    // zet ook alvast de kamer prijs van de dag van aankomst op hun rekening
    @Override
    public void onGastAangemaakt(GastModel gast) {
        if (!gastenInSysteem.containsKey(gast)) {
            gastenInSysteem.put(gast, new HashMap<>());
        }
        voegKostenToe(gast, "Administratiekosten", 5);
        rekenKamerPrijsVoorGast(gast);
    }

    // kijk in wat voor kamer de gast is aangekomen en roep daarbij de bijpassende methode aan
    @Override
    public void onGastAangekomenInKamer(GastModel gast, Locatie behaaldeLocatie) {
        if (layoutModel == null) return;

        // zoek op wat voor soort kamer er op deze locatie staat
        RuimteModel aankomstRuimte = layoutModel.getRuimteBijLocatie(behaaldeLocatie);

        // als het geen faciliteit is
        if (aankomstRuimte == null) {
            if (behaaldeLocatie.equals(uitgang)) { // is het de locatie van de uitgang?
                checkOutGast(gast);
            }
            return;
        }

        // als het wel een faciliteit is roep de bijbehorende methode aan
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
                // check nog voor de zekerheid of hij niet bij de uitgang staat
                if (behaaldeLocatie.equals(uitgang)) {
                    checkOutGast(gast);
                }
                break;
        }
    }

    @Override
    public void onLayoutGeladen(LayoutController layoutController) {
        this.layoutModel = layoutController.getModel(); // sla layout model op om de ruimte bij de locatie op te kunnen halen

        // bereken de startlocatie net zoals bij de gastcontroller
        int x = layoutController.getView().getGridBreedte() / 2;
        int y = layoutController.getView().getGridLengte() - 1;
        uitgang = new Locatie(x, y);
    }

    // als er een dag voorbij is, zet dit in de huidige dag en voor elke gast in het hotel, bereken hun vaste dagtarief
    @Override
    public void dagVoorbij(int dag) {
        this.huidigeDag = dag;

        // geef elke gast die nu in het hotel is de volgende nacht op de bon
        for (GastModel gast : gastenInSysteem.keySet()) {
            rekenKamerPrijsVoorGast(gast);
        }
    }

    // onnodige implements

    @Override
    public void onGastVertrokken(GastModel gast) {}
    @Override
    public void onGastVerplaatst(GastModel gast, Locatie oudeLocatie) {}
    @Override
    public void onGastGaatWegUitKamer(GastModel gast, Locatie oudeLocatie) {}
}