package Controller.PersoonManagement;

import Controller.Layout.LayoutController;
import Controller.Layout.Interfaces.LayoutGeladen;
import Controller.Systeem.Interfaces.settingsListener;
import Model.Entiteiten.EntiteitenModel;
import Model.Layout.Locatie;
import View.JoyOpdracht.FactuurPrint;
import View.Systeem.OverzichtView;

import java.util.HashMap;
import java.util.Map;

public abstract class EntiteitenController implements LayoutGeladen, BeweegHelper.MovementListener, settingsListener {

    protected LayoutController layoutController;
    public final Map<Integer, EntiteitenModel> actieveEntiteiten;
    public final BeweegHelper beweegHelper;
    public Locatie startLocatie;

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

    // zet de locatie van de ingang
    @Override
    public void onLayoutGeladen(LayoutController controller) {
        this.layoutController = controller;

        // onder midden van de grid (waar de lobby is)
        int x = layoutController.getView().getGridBreedte() / 2;
        int y = layoutController.getView().getGridLengte() - 1;
        startLocatie = new Locatie(x, y);
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