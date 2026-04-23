package Controller.Layout;

import Model.Layout.LayoutModel;
import Model.Layout.Locatie;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;
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
        model.addVerplichteElementen(view.getGridLengte(), view.getGridBreedte());

        view.maakGrid(view.getGridBreedte(), view.getGridLengte(), model.getVakBreedte(), model.getVakHoogte(), model.getGrid());

        view.plaatsKamers(model.getRuimtes(), model.getVerplichteElementen()
        );
    }

    public LayoutModel getModel() {
        return model;
    }
    public LayoutView getView() {
        return view;
    }

    public Locatie vindLocatie(KamerType kamerType) {
        for (RuimteModel ruimte : model.getRuimtes()) {
            // Check of de ruimte een restaurant is (pas dit aan op jouw Enum/Model)
            if (ruimte.getAreaType() == kamerType) {
                int x = ruimte.getPosition().getX();
                int y = (ruimte.getPosition().getY())-1;
                return new Locatie(x, y);
            }
        }
        return null;
    }
}