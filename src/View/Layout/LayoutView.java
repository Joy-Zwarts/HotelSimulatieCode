package View.Layout;

import Controller.Layout.GridVakjeController;
import Model.Layout.Locatie;
import Controller.Systeem.PauseController;
import Model.Layout.GridVakjeModel;
import Model.Ruimtes.RuimteModel;
import View.Systeem.HotelSimulatieView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LayoutView {

    // attributen

    private JPanel hotelPanel;
    private HashMap<Locatie, GridVakjeController> grid;
    private int gridBreedte;
    private int gridLengte;
    private final PauseController pauseController;

    // constructor

    public LayoutView(PauseController pauseController, HotelSimulatieView SimulatieView) {
        this.pauseController = pauseController;
    }

    // Maak de gridlayout aan van de hotelweergave met een nieuw grid vakje object per cel in de grid
    public void maakGrid(int gridBreedte, int gridLengte, int vakBreedte, int vakHoogte, HashMap<Locatie, GridVakjeController> grid) {

        this.grid = grid;
        this.gridBreedte = gridBreedte;
        this.gridLengte = gridLengte;

        // als het hotelpaneel nog niet bestaat, maken we het aan, als het al wel bestaat, maken we het leeg
        if (hotelPanel == null) {
            hotelPanel = new JPanel();
        } else {
            hotelPanel.removeAll();
        }

        // bereken de totale benodigde breedte en hoogte van het hotel in pixels
        int totalePixelsBreedte = gridBreedte * vakBreedte;
        int totalePixelsHoogte = gridLengte * vakHoogte;

        // zorg dat het paneel deze grootte wordt
        hotelPanel.setPreferredSize(new Dimension(totalePixelsBreedte, totalePixelsHoogte));
        hotelPanel.setLayout(new GridLayout(gridLengte, gridBreedte));

        // loop door alle cellen en bouw de grid op
        for (int y = 0; y < gridLengte; y++) {
            for (int x = 0; x < gridBreedte; x++) {

                // Maak een grid vakje per cel met de nieuwe maten
                GridVakjeController controller = new GridVakjeController(
                        new GridVakjeModel(x, y, vakBreedte, vakHoogte),
                        new GridVakjeView(x, y, vakBreedte, vakHoogte),
                        pauseController
                );

                Locatie locatie = new Locatie(x, y);
                grid.put(locatie, controller);

                // voeg de view van het vakje toe aan het hoofd-hotelpaneel
                hotelPanel.add(controller.getGridView().getVakjePanel());
            }
        }

        hotelPanel.revalidate();
        hotelPanel.repaint();
    }

    // bereken hoe groot het hotel moet zijn gebaseerd op het aantal kamers en hun posities
    public void berekenGridGrootte(ArrayList<RuimteModel> ruimtes) {

        for (RuimteModel ruimte : ruimtes) {

            int right = ruimte.getPosition().getX() + ruimte.getDimensionW() - 1;
            int bottom = ruimte.getPosition().getY() + ruimte.getDimensionH() - 1;

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

    // plaats alles op de juiste plek
    private void plaatsRuimteLijst(ArrayList<RuimteModel> lijst, boolean isVerplicht) {

        for (RuimteModel ruimte : lijst) {

            int startX = isVerplicht ? ruimte.getPosition().getX() - 1 : ruimte.getPosition().getX();
            int startY = ruimte.getPosition().getY() - 1;

            int w = ruimte.getDimensionW();
            int h = ruimte.getDimensionH();

            for (int y = startY; y < startY + h; y++) {
                for (int x = startX; x < startX + w; x++) {

                    GridVakjeController vak = grid.get(new Locatie(x, y));

                    if (vak != null) {

                        // zet per vakje wat voor ruimte er in zit
                        vak.getModel().setRuimte(ruimte);

                        vak.getModel().setlinksboven(x == startX && y == startY);

                        vak.getModel().setLinksonder(x == startX && y == startY + h -1);

                        vak.getModel().setRechtsboven(x == startX + w - 1 && y == startY);

                        vak.getGridView().zetInhoud(ruimte, vak.getModel().islinksboven(), vak.getModel().islinksOnder());

                        vak.getGridView().zetPersonenAantal(ruimte, vak.getModel().isRechtsboven(), vak.getModel().islinksboven());

                        vak.updateView();

                        // bepaal de borders per cel gebaseerd op de dimensies van de kamer
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

    public HashMap<Locatie, GridVakjeController> getGrid() {
        return grid;
    }

    public void setGrid(HashMap<Locatie, GridVakjeController> newGrid) {
        this.grid = newGrid;
    }
    public void setGridBreedte(int i) {
        gridBreedte = i;
    }
    public void setGridLengte(int i) {
        gridLengte = i;
    }
}