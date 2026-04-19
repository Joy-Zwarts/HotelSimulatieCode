package Controller.GastManagement;

import Controller.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.RoomClassificatie;

public class RoomAssign implements NewGuest {

    // attributen
    private final ReceptieController controller;

    // contructor
    public RoomAssign(ReceptieController receptieController) {
        this.controller = receptieController;
    }

    // get de wensen van een gast en get een kamer die aan deze voldoet
    public void assignRoom(GastModel gast) {
        if (gast == null || gast.getWensen() == null) return;

        RoomClassificatie wensen = gast.getWensen();

        for (KamerModel kamer : controller.getKamers().values()) {
            if (kamer == null || kamer.isBezet()) continue;

            if (kamer.getClassification() == wensen) {
                gast.setKamer(kamer);
                kamer.setBezet(true);
                kamer.setVerblijvende(gast);

                controller.setKamerVol(kamer);
                controller.refreshView();

                gast.getTargetLocatie().setX(kamer.getPosition().getX());
                gast.getTargetLocatie().setY(kamer.getPosition().getY() -1);

                return;
            }
        }

        // als er geen kamer meer over is met die specifieke classificaties wordt er gekeken of er een kamer over is met 1 ster minder totdat er geen kamers meer over zijn

        System.out.println("Geen kamer beschikbaar voor gast " + gast.getGastID() + " met de wensen " + gast.getWensen());

        // kamer downgraden als eer geen kamer is met de wensen van de gast

        switch(gast.getWensen()) {
            case RoomClassificatie.vijfSterren:
                gast.setWensen(RoomClassificatie.vierSterren);
                assignRoom(gast);
                break;
            case RoomClassificatie.vierSterren:
                gast.setWensen(RoomClassificatie.drieSterren);
                assignRoom(gast);
                break;
            case RoomClassificatie.drieSterren:
                gast.setWensen(RoomClassificatie.tweeSterren);
                assignRoom(gast);
                break;
            case RoomClassificatie.tweeSterren:
                gast.setWensen(RoomClassificatie.eenSter);
                assignRoom(gast);
                break;
            case RoomClassificatie.eenSter:
                break;
        }
        System.out.println("Kamer zoeken voor gast " + gast.getGastID() + " met de wensen " + gast.getWensen());
    }

    // reacties op events

    // elke nieuwe gast krijgt een kamer
    @Override
    public void onGastAangemaakt(GastModel gast) {
        assignRoom(gast);
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
}