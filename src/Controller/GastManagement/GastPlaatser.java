package Controller.GastManagement;

import Controller.Layout.GridVakjeController;
import Controller.Layout.LayoutController;
import Controller.Layout.LayoutGeladen;
import Controller.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Ruimtes.RuimteModel;
import View.Layout.LayoutView;

import javax.swing.*;
import java.util.HashMap;

public class GastPlaatser implements NewGuest, LayoutGeladen {

    // attributen
    private HashMap<Locatie, GridVakjeController> grid;

    private LayoutView layoutView;

    // constructor
    public GastPlaatser(LayoutView view) {
        this.grid = null;
        this.layoutView = view;
    }

    // plaats gast op locatie en past gasten-counter label aan in de rechterbovenhoek
    @Override
    public void onGastAangemaakt(GastModel gast) {
        Locatie gastLoc = gast.getLocatie();

        if (grid != null && gastLoc != null) {
            // zoek het vakje op basis van de locatie
            GridVakjeController vak = grid.get(gastLoc);

            if (vak != null) {
                // verhoog het aantal gasten in het model
                RuimteModel ruimte = vak.getModel().getRuimte();
                if (ruimte != null) {
                    ruimte.setAantalGasten(ruimte.getAantalGasten() + 1);

                    // voeg het label van de gast toe aan het panel
                    JPanel vakjePanel = vak.getGridView().getBackgroundPanel();
                    vakjePanel.add(gast.getGastLabel());

                    refreshRuimteVisueel(ruimte);

                    JPanel guestLayer = vak.getGridView().getGuestPanel();
                    guestLayer.add(gast.getGastLabel());
                    guestLayer.revalidate();
                    guestLayer.repaint();

                    vakjePanel.revalidate();
                    vakjePanel.repaint();
                }
            } else {
                System.out.println("GastPlaatser: Geen vakje gevonden op " + gastLoc);
            }
        }
    }

    // update en repaint de panels waar verandering in is gekomen
    private void refreshRuimteVisueel(RuimteModel ruimte) {
        for (GridVakjeController vak : grid.values()) {
            if (vak.getModel().getRuimte().equals(ruimte)) {
                // update de tellers en iconen
                vak.getGridView().zetInhoud(ruimte, vak.getModel().islinksboven(), vak.getModel().islinksOnder());
                vak.getGridView().zetPersonenAantal(ruimte, vak.getModel().isRechtsboven());
                vak.updateView();
            }
        }
    }

    // reacties of events

    // verwijder gast-icoon uit kamer en verlaag het gastenaantal label
    @Override
    public void onGastVertrokken(GastModel gast) {

        Locatie gastLoc = gast.getLocatie();

        if (grid != null && gastLoc != null) {
            GridVakjeController vak = grid.get(gastLoc);

            if (vak != null) {
                // verlaag het aantal gasten in het model
                RuimteModel ruimte = vak.getModel().getRuimte();
                if (ruimte != null) {
                    // verwijder het specifieke label van de gast in de kamer
                    JPanel guestLayer = vak.getGridView().getGuestPanel();
                    guestLayer.remove(gast.getGastLabel());

                    refreshRuimteVisueel(ruimte);

                    guestLayer.revalidate();
                    guestLayer.repaint();
                }
            }
        }
    }

    // krijg layout controller na het aanmaken van de layout
    @Override
    public void onLayoutGeladen(LayoutController layoutController) {
        layoutView = layoutController.getView();
        this.grid = layoutView.getGrid();
    }
}
