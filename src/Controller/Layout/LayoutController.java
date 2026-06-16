package Controller.Layout;

import Controller.PersoonManagement.LiftController;
import Model.Layout.LayoutModel;
import Model.Layout.Locatie;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;
import View.Layout.LayoutView;
import View.Systeem.HotelSimulatieView; // Zorg dat deze import er staat

public class LayoutController {

    private final LayoutModel model;
    private final LayoutView view;
    private final HotelSimulatieView simulatieView;
    private LiftController liftController;

    public LayoutController(LayoutModel model, LayoutView view, HotelSimulatieView hoofdView) {
        this.model = model;
        this.view = view;
        this.simulatieView = hoofdView;
        init();
    }

    protected void init() {
        // bereken hoe groot het hotel in vakjes is
        view.berekenGridGrootte(model.getRuimtes());
        model.addVerplichteElementen(view.getGridLengte(), view.getGridBreedte());

        // bereken de dynamische vakgrootte, voordat we de grid maken
        berekenEnPasVakgrootteAan();

        // maak en vul de grid met gridvakjes ter grootte van de net berekende dynamische maten
        view.maakGrid(view.getGridBreedte(), view.getGridLengte(), model.getVakBreedte(), model.getVakHoogte(), model.getGrid());
        view.plaatsKamers(model.getRuimtes(), model.getVerplichteElementen());
    }

    public void berekenEnPasVakgrootteAan() {
        int hotelBreedteInVakjes = view.getGridBreedte();
        int hotelHoogteInVakjes = view.getGridLengte();

        // pak de grootte van het vak waar de layout in wordt laten zien
        int beschikbareBreedte = simulatieView.getLayoutScrollPane().getViewport().getWidth();
        int beschikbareHoogte = simulatieView.getLayoutScrollPane().getViewport().getHeight();

        // fallbacks
        if (beschikbareBreedte <= 0) beschikbareBreedte = 1000;
        if (beschikbareHoogte <= 0) beschikbareHoogte = 600;

        // bepaal de minimale breedte van 1 vakje gebaseerd op het maximaal
        int minimaleVakBreedte = beschikbareBreedte / 8;

        // bereken de juiste breedte om het scherm precies te vullen
        int dynamischeBreedte = beschikbareBreedte / hotelBreedteInVakjes;

        // zorgen dat een vakje nooit kleiner wordt dan de minimale breedte
        int uiteindelijkeBreedte = Math.max(dynamischeBreedte, minimaleVakBreedte);

        // voor de hoogte doen we hetzelfde
        int dynamischeHoogte = beschikbareHoogte / hotelHoogteInVakjes;
        int uiteindelijkeHoogte = Math.max(dynamischeHoogte, uiteindelijkeBreedte / 2); // houdt dat het een rechthoek blijft

        // sla de definitieve maten op in het model
        model.setVakDimensies(uiteindelijkeBreedte, uiteindelijkeHoogte);
    }

    public Locatie vindLocatie(KamerType kamerType) {
        for (RuimteModel ruimte : model.getRuimtes()) {
            if (ruimte.getAreaType() == kamerType) {
                int x = ruimte.getPosition().getX();
                int y = (ruimte.getPosition().getY()) - 1;
                return new Locatie(x, y);
            }
        }
        return null;
    }
    public void setLiftController(LiftController liftController) {
        this.liftController = liftController;
    }

    // De getter die de PathFinder gaat gebruiken
    public LiftController getLiftController() {
        return this.liftController;
    }
    public LayoutModel getModel() { return model; }
    public LayoutView getView() { return view; }
}