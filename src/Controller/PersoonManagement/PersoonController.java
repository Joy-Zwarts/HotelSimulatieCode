package Controller.PersoonManagement;

import Controller.Layout.LayoutController;
import Controller.Layout.LayoutGeladen;
import Model.Personen.PersoonModel;

import java.util.HashMap;
import java.util.Map;

public abstract class PersoonController implements LayoutGeladen, BeweegHelper.MovementListener {

    // attributen
    protected LayoutController layoutController;

    protected final Map<Integer, PersoonModel> actievePersonen;

    protected final BeweegHelper movementEngine;

    // constructor
    public PersoonController() {
        this.actievePersonen = new HashMap<>();

        this.movementEngine = new BeweegHelper(1000, this);

        this.movementEngine.start();
    }

    // zodra de layout is geladen, neem deze op
    @Override
    public void onLayoutGeladen(LayoutController controller) {
        this.layoutController = controller;
    }
}