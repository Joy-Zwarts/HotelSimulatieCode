package Controller;

import Model.LayoutModel;
import View.LayoutView;
import hotelevents.HotelEventManager;

public class LayoutController {

    private final LayoutModel model;
    private final LayoutView view;

    public LayoutController(LayoutModel model, LayoutView view, HotelEventManager manager) {
        this.model = model;
        this.view = view;
        init();
    }

    private void init() {
        // 1. Model voorbereiden
        view.berekenGridGrootte(model.getRuimtes());
        model.addverplichteElementen(view.getGridLengte(), view.getGridBreedte());

        // 2. View opbouwen
        view.maakGrid(view.getGridBreedte(), view.getGridLengte(), model.getVakBreedte(), model.getVakHoogte(), model.getGrid());

        // 3. Data tonen
        view.plaatsKamers(model.getRuimtes(), model.getVerplichteElementen()
        );
    }
}