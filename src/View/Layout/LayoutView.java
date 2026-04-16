package View.Layout;

import Controller.Layout.GridVakjeController;
import Controller.Systeem.PauseController;
import Model.Layout.GridVakjeModel;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;
import View.Systeem.HotelSimulatieView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LayoutView {

    // attributen

    private JPanel hotelPanel;
    private HashMap<String, GridVakjeController> grid;
    private int gridBreedte;
    private int gridLengte;
    private final PauseController pauseController;
    private final HotelSimulatieView simulatieView;

    // constructor

    public LayoutView(PauseController pauseController, HotelSimulatieView SimulatieView) {
        this.pauseController = pauseController;
        this.simulatieView = SimulatieView;
    }

    // maak de gridlayout aan van de hotelweergave
    public void maakGrid(int gridBreedte, int gridLengte, int vakBreedte, int vakHoogte, HashMap<String, GridVakjeController> grid) {

        this.grid = grid;

        hotelPanel = new JPanel(null);
        hotelPanel.setPreferredSize(
                new Dimension(gridBreedte * vakBreedte, gridLengte * vakHoogte)
        );

        for (int y = 0; y < gridLengte; y++) {
            for (int x = 0; x < gridBreedte; x++) {

                // maak een grid vakje per cel
                GridVakjeController controller = new GridVakjeController(new GridVakjeModel(x, y, vakBreedte, vakHoogte), new GridVakjeView(x, y, vakBreedte, vakHoogte), pauseController, simulatieView);

                grid.put(x + ", " + y, controller);

                // voeg een nieuw panel toe
                hotelPanel.add(controller.getGridView().getVakjePanel());
            }
        }
    }

    // bereken hoe groot het hotel moet zijn
    public void berekenGridGrootte(ArrayList<RuimteModel> ruimtes) {

        for (RuimteModel ruimte : ruimtes) {

            int right = ruimte.getPositionX() + ruimte.getDimensionW() - 1;
            int bottom = ruimte.getPositionY() + ruimte.getDimensionH() - 1;

            if (right > gridBreedte) gridBreedte = right;
            if (bottom > gridLengte) gridLengte = bottom;
        }

        gridBreedte += 2;
        gridLengte += 1;
    }

    // plaats ruimten en verplichte elementen
    public void plaatsKamers(ArrayList<RuimteModel> ruimtes, ArrayList<RuimteModel> verplichteElementen) {
        plaatsRuimteLijst(ruimtes, false);
        plaatsRuimteLijst(verplichteElementen, true);
    }

    // plaats alles
    private void plaatsRuimteLijst(ArrayList<RuimteModel> lijst, boolean isVerplicht) {

        for (RuimteModel ruimte : lijst) {

            int startX = isVerplicht ? ruimte.getPositionX() - 1 : ruimte.getPositionX();
            int startY = ruimte.getPositionY() - 1;

            int w = ruimte.getDimensionW();
            int h = ruimte.getDimensionH();

            for (int y = startY; y < startY + h; y++) {
                for (int x = startX; x < startX + w; x++) {

                    GridVakjeController vak = grid.get(x + ", " + y);

                    if (vak != null) {
                        // zet per vakje wat voor ruimte er in zit
                        vak.getModel().setRuimte(ruimte);

                        vak.getModel().setlinksboven(x == startX && y == startY);

                        vak.getModel().setLinksonder(x == startX && y == startY + h -1);

                        vak.getModel().setRechtsboven(x == startX + w - 1 && y == startY);

                        vak.getGridView().zetInhoud(ruimte, vak.getModel().islinksboven(), vak.getModel().islinksOnder());

                        vak.getGridView().zetPersonenAantal(ruimte, vak.getModel().isRechtsboven());

                        vak.updateView();

                        // borders
                        boolean top = (y == startY);
                        boolean bottom = (y == startY + h - 1);
                        boolean left = (x == startX);
                        boolean right = (x == startX + w - 1);

                        vak.getGridView().setBorder(top, left, bottom, right);
                    }
                }
            }
        }
    }

    // getters & setters

    public JPanel getHotelPanel() {
        return hotelPanel;
    }

    public int getGridBreedte() {
        return gridBreedte;
    }

    public int getGridLengte() {
        return gridLengte;
    }

    public HashMap<String, GridVakjeController> getGrid() {
        return grid;
    }
}