package Controller.PersoonManagement;

import Model.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerClassificatie;

public class KamerAssign implements NewGast {

    // attributen
    private final ReceptieController controller;

    // contructor
    public KamerAssign(ReceptieController receptieController) {
        this.controller = receptieController;
    }

    // get de wensen van een gast en get een kamer die aan deze voldoet
    public void assignKamer(GastModel gast) {
        if (gast == null || gast.getWensen() == null) return;

        KamerClassificatie wensen = gast.getWensen();

        for (KamerModel kamer : controller.getKamers().values()) {
            if (kamer == null || kamer.isBezet()) continue;

            if (kamer.getClassification() == wensen) {
                gast.setKamer(kamer);
                kamer.setBezet(true);
                kamer.setVerblijvende(gast);

                controller.setKamerVol(kamer);
                controller.refreshView();

                gast.getTargetLocatie().setX(kamer.getPosition().getX());
                gast.getTargetLocatie().setY(kamer.getEntryPoint().getY() -1);

                return;
            }
        }

        // als er geen kamer meer over is met die specifieke classificaties wordt er gekeken of er een kamer over is met 1 ster minder totdat er geen kamers meer over zijn

        System.out.println("Geen kamer beschikbaar voor gast " + gast.getID() + " met de wensen " + gast.getWensen());

        // kamer downgraden als eer geen kamer is met de wensen van de gast

        switch(gast.getWensen()) {
            case KamerClassificatie.vijfSterren:
                gast.setWensen(KamerClassificatie.vierSterren);
                assignKamer(gast);
                break;
            case KamerClassificatie.vierSterren:
                gast.setWensen(KamerClassificatie.drieSterren);
                assignKamer(gast);
                break;
            case KamerClassificatie.drieSterren:
                gast.setWensen(KamerClassificatie.tweeSterren);
                assignKamer(gast);
                break;
            case KamerClassificatie.tweeSterren:
                gast.setWensen(KamerClassificatie.eenSter);
                assignKamer(gast);
                break;
            case KamerClassificatie.eenSter:
                break;
        }
        System.out.println("Kamer zoeken voor gast " + gast.getID() + " met de wensen " + gast.getWensen());
    }

    // reacties op events

    // elke nieuwe gast krijgt een kamer
    @Override
    public void onGastAangemaakt(GastModel gast) {
        assignKamer(gast);
    }

    // als de gast is vertrokken wordt de kamer weer op vrij gezet
    @Override
    public void onGastVertrokken(GastModel gast) {

        if (gast == null || gast.getKamer() == null) return;

        KamerModel kamer = gast.getKamer();

        kamer.setBezet(false);
        kamer.setVerblijvende(null);

        controller.setKamerLeeg(kamer);
    }

    @Override
    public void onGastVerplaatst(GastModel gast, Locatie oudeLocatie) {

    }

    @Override
    public void onGastAangekomenInKamer(GastModel gast, Locatie behaaldeLocatie) {

    }

    @Override
    public void onGastGaatWegUitKamer(GastModel gast, Locatie oudeLocatie) {

    }
}