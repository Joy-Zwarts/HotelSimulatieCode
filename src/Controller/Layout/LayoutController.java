package Controller.Layout;

import Model.Layout.LayoutModel;
import View.Layout.LayoutView;

public class LayoutController {

    // attributen

    private final LayoutModel model;
    private final LayoutView view;

    // constructor
    public LayoutController(LayoutModel model, LayoutView view) {
        this.model = model;
        this.view = view;
        init();
    }

    // maak de layout stap voor stap
    private void init() {
        view.berekenGridGrootte(model.getRuimtes());
        model.addverplichteElementen(view.getGridLengte(), view.getGridBreedte());

        view.maakGrid(view.getGridBreedte(), view.getGridLengte(), model.getVakBreedte(), model.getVakHoogte(), model.getGrid());

        view.plaatsKamers(model.getRuimtes(), model.getVerplichteElementen()
        );
    }
}