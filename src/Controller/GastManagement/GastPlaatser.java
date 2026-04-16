package Controller.GastManagement;

import Controller.Layout.GridVakjeController;
import Controller.Layout.LayoutController;
import Controller.Layout.LayoutGeladen;
import Model.Personen.GastModel;
import Model.Ruimtes.RuimteModel;
import View.Layout.GridVakjeView;
import View.Layout.LayoutView;
import View.Personen.PersoonView;

import java.util.HashMap;

public class GastPlaatser implements NewGuest, LayoutGeladen {

    private HashMap<String, GridVakjeController> grid;

    private LayoutView layoutView;

    public GastPlaatser(LayoutView view) {
        this.grid = null;
        this.layoutView = view;
    }

    @Override
    public void onGastAangemaakt(GastModel gast) {
        // Check of de locatie inmiddels is aangepast door RoomAssign (niet meer "0,0")
        String locatie = gast.getLocatie();

        if (grid != null && locatie != null && !locatie.equals("0,0")) {
            // Gebruik de bestaande logica om het grid-vakje te vinden
            GridVakjeController startVak = grid.get(locatie);
            if (startVak != null) {
                RuimteModel ruimte = startVak.getModel().getRuimte();

                // Verhoog het aantal in het model
                ruimte.setAantalGasten(ruimte.getAantalGasten() + 1);

                // Update alle vakjes die bij deze ruimte horen (belangrijk voor grote kamers!)
                refreshRuimteVisueel(ruimte);
            }
        } else {
            System.out.println("GastPlaatser: Locatie voor gast " + gast.getGastID() + " is nog onbekend.");
        }
    }

    // Hulpmethode om de view te forceren
    private void refreshRuimteVisueel(RuimteModel ruimte) {
        for (GridVakjeController vak : grid.values()) {
            if (vak.getModel().getRuimte().equals(ruimte)) {
                GridVakjeView view = vak.getGridView();

                // Teken de inhoud opnieuw (sterren, nummers, etc.)
                view.zetInhoud(ruimte, vak.getModel().islinksboven(), vak.getModel().islinksOnder());

                // Teken de gasten-teller opnieuw
                view.zetPersonenAantal(ruimte, vak.getModel().isRechtsboven());

                // Forceer Swing om de boel te herschilderen
                vak.updateView();
            }
        }
    }

    @Override
    public void onGastVertrokken(int gastID) {

    }

    @Override
    public void onLayoutGeladen(LayoutController layoutController) {
        layoutView = layoutController.getView();
        this.grid = layoutView.getGrid();
    }
}
