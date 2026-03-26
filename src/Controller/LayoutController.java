package Controller;

import Model.LayoutModel;
import View.LayoutView;

public class LayoutController {

    private LayoutModel model;
    private LayoutView view;

    public LayoutController(LayoutModel model, LayoutView view) {
        this.model = model;
        this.view = view;

        init();
    }

    private void init() {
        // 1. Model voorbereiden
        model.berekenGridGrootte();
        model.addverplichteElementen();

        // 2. View opbouwen
        view.maakGrid(model.getGridBreedte(), model.getGridLengte(), model.getVakBreedte(), model.getVakHoogte(), model.getGrid());

        // 3. Data tonen
        view.plaatsKamers(model.getRuimtes(), model.getVerplichteElementen()
        );
    }
}