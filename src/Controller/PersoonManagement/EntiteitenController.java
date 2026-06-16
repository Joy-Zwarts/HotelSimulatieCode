package Controller.PersoonManagement;

import Controller.Layout.LayoutController;
import Controller.Layout.Interfaces.LayoutGeladen;
import Controller.Systeem.Interfaces.settingsListener;
import Model.Entiteiten.EntiteitenModel;
import View.JoyOpdracht.FactuurPrint;
import View.Systeem.OverzichtView;

import java.util.HashMap;
import java.util.Map;

public abstract class EntiteitenController implements LayoutGeladen, BeweegHelper.MovementListener, settingsListener {

    protected LayoutController layoutController;
    public final Map<Integer, EntiteitenModel> actieveEntiteiten;
    public final BeweegHelper beweegHelper;

    public EntiteitenController() {
        this.actieveEntiteiten = new HashMap<>();
        this.beweegHelper = new BeweegHelper(1000, this);
        this.beweegHelper.start();
    }

    // om de schoonmakers te tonen in de overzicht view
    public void injecteerOverzichtView(OverzichtView overzichtView) {
        if (this.beweegHelper != null) {
            this.beweegHelper.setOverzichtView(overzichtView);
        }
    }

    public void injecteerFactuurPrint(FactuurPrint factuurPrint) {
        if (this.beweegHelper != null) {
            this.beweegHelper.setFactuurPrint(factuurPrint);
        }
    }

    @Override
    public void onLayoutGeladen(LayoutController controller) {
        this.layoutController = controller;
    }

    public void resetController() {
        this.actieveEntiteiten.clear();
        if (this.beweegHelper != null) {
            this.beweegHelper.reset();
        }
    }

    @Override
    public void trapLoopDuurVeranderd(int trapLoopDuur) {
        beweegHelper.setTrapVertragingTicks(trapLoopDuur);
    }
}