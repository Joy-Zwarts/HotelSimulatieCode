package Controller.PersoonManagement;

import Controller.Layout.LayoutController;
import Controller.Layout.LayoutGeladen;
import Model.Personen.PersoonModel;
import View.Systeem.OverzichtView; // Import toevoegen

import java.util.HashMap;
import java.util.Map;

public abstract class PersoonController implements LayoutGeladen, BeweegHelper.MovementListener {

    protected LayoutController layoutController;
    protected final Map<Integer, PersoonModel> actievePersonen;
    protected final BeweegHelper movementEngine;

    public PersoonController() {
        this.actievePersonen = new HashMap<>();
        this.movementEngine = new BeweegHelper(1000, this);
        this.movementEngine.start();
    }

    // om de schoonmakers te tonen in de overzicht view
    public void injecteerOverzichtView(OverzichtView overzichtView) {
        if (this.movementEngine != null) {
            this.movementEngine.setOverzichtView(overzichtView);
        }
    }

    @Override
    public void onLayoutGeladen(LayoutController controller) {
        this.layoutController = controller;
    }
}