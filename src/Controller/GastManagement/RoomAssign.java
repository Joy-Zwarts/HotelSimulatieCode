package Controller.GastManagement;

import Model.Personen.GastModel;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.RoomClassificatie;
import Model.Ruimtes.RuimteModel;

public class RoomAssign implements NewGuest {

    private final ReceptieController controller;

    public RoomAssign(ReceptieController receptieController) {
        this.controller = receptieController;
    }

    @Override
    public void onGastAangemaakt(GastModel gast) {
        assignRoom(gast);
    }

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

                String locatieKey = kamer.getPositionX() + ", " + kamer.getPositionY();
                gast.setLocatie(locatieKey);

                System.out.println("Gast " + gast.getGastID() + " gaat naar grid-positie: " + gast.getLocatie());
                return;
            }
        }

        System.out.println("Geen kamer beschikbaar voor gast " + gast.getGastID());
    }

    @Override
    public void onGastVertrokken(int gastID) {

        GastModel gast = controller.getGast(gastID);

        if (gast == null || gast.getKamer() == null) return;

        KamerModel kamer = gast.getKamer();

        kamer.setBezet(false);
        kamer.setVerblijvende(null);



        controller.setKamerLeeg(kamer);
    }
}