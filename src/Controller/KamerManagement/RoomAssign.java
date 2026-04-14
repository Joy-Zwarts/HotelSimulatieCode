package Controller.KamerManagement;

import Model.Personen.GastModel;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.RoomClassificatie;

import java.util.HashMap;

public class RoomAssign implements NewGuest{

    private final ReceptieController controller;
    private RoomClassificatie wensen;
    private HashMap<Integer, KamerModel> mogelijkeKamers;

    public RoomAssign(ReceptieController receptieController) {
        this.controller = receptieController;
        this.mogelijkeKamers = new HashMap<>();
    }

    @Override
    public void onGastAangemaakt(GastModel gast) {
        assignRoom(gast);
    }

    public void assignRoom(GastModel gast) {
        mogelijkeKamers.clear();
        wensen = gast.getWensen();

        for (KamerModel kamer : controller.getLegeKamers().values()){
            if (kamer.getClassification().equals(wensen)){
                mogelijkeKamers.put(kamer.getRoomNumber(), kamer);
            }
        }

        boolean kamerGevonden = false;

        for (KamerModel mogelijkeKamer : mogelijkeKamers.values()){
            if (!mogelijkeKamer.isBezet() && mogelijkeKamer.getVerblijvende() == null){
                gast.setKamer(mogelijkeKamer);
                mogelijkeKamer.setBezet(true);
                mogelijkeKamer.setVerblijvende(gast);
                controller.setKamerVol(mogelijkeKamer);
                controller.refreshView();
                kamerGevonden = true;
                break;
            }
        }

        if (!kamerGevonden) {
            System.out.println("Geen beschikbare " + wensen + " kamer voor gast " + gast.getGastID());
        }
    }

    @Override
    public void onGastVertrokken(int gastID) {
        GastModel gast = controller.getGast(gastID);

        gast.getKamer().setBezet(false);
        gast.getKamer().setVerblijvende(gast);
        controller.setKamerVol(gast.getKamer());
        controller.refreshView();
    }
}
