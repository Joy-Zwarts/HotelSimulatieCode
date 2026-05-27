package Controller.PersoonManagement;

import Controller.Layout.GridVakjeController;
import Controller.Layout.LayoutController;
import Controller.Layout.LayoutGeladen;
import Controller.PersoonManagement.NewGast;
import Controller.PersoonManagement.NewSchoonmaker;
import Controller.Systeem.reset;
import Model.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Personen.PersoonModel;
import Model.Personen.SchoonmakerModel;
import Model.Ruimtes.RuimteModel;
import View.Layout.LayoutView;

import javax.swing.*;
import java.util.HashMap;

public class PlaatsHelper implements NewGast, LayoutGeladen, NewSchoonmaker, reset {

    // attributen
    private HashMap<Locatie, GridVakjeController> grid;

    private LayoutView layoutView;

    // constructor
    public PlaatsHelper(LayoutView view) {
        this.grid = null;
        this.layoutView = view;
    }

    // plaats de gast hun icoontje op locatie
    @Override
    public void onGastAangemaakt(GastModel gast) {
        plaatsPersoon(gast);
    }

    @Override
    public void onSchoonmakerAangemaakt(SchoonmakerModel schoonmaker) {
        plaatsPersoon(schoonmaker);
    }

    public void plaatsPersoon(PersoonModel persoon) {
        Locatie Loc = persoon.getLocatie();
        if (grid != null && Loc != null) {
            GridVakjeController vak = grid.get(Loc);
            if (vak != null) {
                JPanel guestLayer = vak.getGridView().getGuestPanel();
                guestLayer.add(persoon.getPersoonLabel());

                guestLayer.revalidate();
                guestLayer.repaint();
            }
        }
    }

    // zet het schoonmaker-icoontje op de juiste plek na een stap
    @Override
    public void onSchoonmakerVerplaatst(SchoonmakerModel schoonmaker, Locatie oudeLocatie) {

        if (oudeLocatie != null && oudeLocatie.equals(schoonmaker.getLocatie())) {return;}

        GridVakjeController oudVak = grid.get(oudeLocatie);

        if (oudVak != null) {JPanel oudPanel = oudVak.getGridView().getGuestPanel();
            oudPanel.remove(schoonmaker.getPersoonLabel());
            oudPanel.revalidate();
            oudPanel.repaint();
        }

        GridVakjeController nieuwVak = grid.get(schoonmaker.getLocatie());

        if (nieuwVak != null) {
            JPanel nieuwPanel = nieuwVak.getGridView().getGuestPanel();
            nieuwPanel.add(schoonmaker.getPersoonLabel());
            nieuwPanel.revalidate();
            nieuwPanel.repaint();
        }
    }

    // als de schoonmaker aangekomen is in de target locatie gaat hij er in und wordt het drukte label verhoogd
    @Override
    public void onSchoonmakerAangekomenInKamer(SchoonmakerModel schoonmaker) {
        Aangekomen(schoonmaker);
    }

    @Override
    public void onSchoonmakerVerlaatKamer(SchoonmakerModel schoonmaker, Locatie oudeLocatie) {
        vertrokken(schoonmaker, oudeLocatie);
    }

    // als de gast aangekomen is in de target locatie gaat hij er in und wordt het drukte label verhoogd
    @Override
    public void onGastAangekomenInKamer(GastModel gast, Locatie loc) {
        Aangekomen(gast);
    }

    public void Aangekomen(PersoonModel persoon) {
        Locatie loc = persoon.getLocatie();
        GridVakjeController vak = grid.get(loc);

        if (vak != null) {
            RuimteModel ruimte = vak.getModel().getRuimte();
            if (ruimte != null) {

                // switch tussen gast und schoonmaker voor de tellers
                if (persoon instanceof GastModel) {
                    ruimte.setAantalGasten(ruimte.getAantalGasten() + 1);
                } else if (persoon instanceof SchoonmakerModel) {
                    ruimte.setAantalSchoonmakers(ruimte.getAantalSchoonmakers() + 1);
                }

                // verwijder het lopende icoon
                JPanel guestLayer = vak.getGridView().getGuestPanel();
                guestLayer.remove(persoon.getPersoonLabel());

                // UI verversen
                refreshRuimteVisueel(ruimte);

                guestLayer.revalidate();
                guestLayer.repaint();
            }
        }
    }

    // als de gast weg is gegaan van de vorige target locatie wordt het drukte label verlaagt
    @Override
    public void onGastGaatWegUitKamer(GastModel gast, Locatie oudeLocatie) {
        vertrokken(gast, oudeLocatie);
    }

    public void vertrokken(PersoonModel persoon, Locatie oudeLocatie) {
        GridVakjeController vak = grid.get(oudeLocatie);

        if (vak != null) {
            RuimteModel ruimte = vak.getModel().getRuimte();
            if (ruimte != null) {

                // controleer wie er vertrekt und verlaag de juiste teller
                if (persoon instanceof GastModel) {
                    ruimte.setAantalGasten(Math.max(0, ruimte.getAantalGasten() - 1));
                } else if (persoon instanceof SchoonmakerModel) {
                    ruimte.setAantalSchoonmakers(Math.max(0, ruimte.getAantalSchoonmakers() - 1));
                }

                // UI updaten
                refreshRuimteVisueel(ruimte);
                JPanel guestLayer = vak.getGridView().getGuestPanel();
                guestLayer.add(persoon.getPersoonLabel());

                guestLayer.revalidate();
                guestLayer.repaint();
            }
        }
    }

    // update und repaint de panels waar verandering in is gekomen
    private void refreshRuimteVisueel(RuimteModel ruimte) {
        if (grid == null) return;

        for (GridVakjeController vak : grid.values()) {
            if (vak != null && vak.getModel() != null) {

                // DE CRUCIALE FIX: Eerst controleren of het vakje wel een ruimte heeft (!= null)
                // Gangen en lege vakjes hebben geen ruimte, dus die sloegen voorheen de boel plat.
                RuimteModel vakRuimte = vak.getModel().getRuimte();

                if (vakRuimte != null && vakRuimte.equals(ruimte)) {
                    // update de tellers und iconen
                    vak.getGridView().zetInhoud(ruimte, vak.getModel().islinksboven(), vak.getModel().islinksOnder());
                    vak.getGridView().zetPersonenAantal(ruimte, vak.getModel().isRechtsboven(), vak.getModel().islinksboven());
                    vak.updateView();
                }
            }
        }
    }


    // verwijder gast-icoon uit kamer und verlaag het gastenaantal label
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
                    guestLayer.remove(gast.getPersoonLabel());

                    refreshRuimteVisueel(ruimte);

                    guestLayer.revalidate();
                    guestLayer.repaint();
                }
            }
        }
    }

    // als de gast verplaatst moet hun icoontje op de juiste plek verschijnen
    @Override
    public void onGastVerplaatst(GastModel gast, Locatie oudeLocatie) {
        if (oudeLocatie != null && oudeLocatie.equals(gast.getLocatie())) return;

        // oude vakje opschonen
        GridVakjeController oudVak = grid.get(oudeLocatie);
        if (oudVak != null) {
            JPanel oudPanel = oudVak.getGridView().getGuestPanel();
            oudPanel.remove(gast.getPersoonLabel());

            oudPanel.revalidate();
            oudPanel.repaint();
        }

        // gast icoontje op nieuw vakje plaatsten
        GridVakjeController nieuwVak = grid.get(gast.getLocatie());
        if (nieuwVak != null) {
            JPanel nieuwPanel = nieuwVak.getGridView().getGuestPanel();
            nieuwPanel.add(gast.getPersoonLabel());

            nieuwPanel.revalidate();
            nieuwPanel.repaint();
        }
    }

    // krijg layout controller na het aanmaken van de layout
    @Override
    public void onLayoutGeladen(LayoutController layoutController) {
        layoutView = layoutController.getView();
        this.grid = layoutView.getGrid();
    }

    @Override
    public void resetSimulatie() {
        if (grid != null) {
            for (GridVakjeController vak : grid.values()) {
                if (vak != null) {
                    // 1. Verwijder alle lopende/aanwezige gasten JLabels uit het gridvakje
                    JPanel guestLayer = vak.getGridView().getGuestPanel();
                    guestLayer.removeAll();

                    // 2. Zet de tellers van het kamer-model terug naar 0
                    RuimteModel ruimte = vak.getModel().getRuimte();
                    if (ruimte != null) {
                        ruimte.setAantalGasten(0);
                        ruimte.setAantalSchoonmakers(0);
                        // Update de visuele cijfers und bezem-iconen van de ruimte
                        refreshRuimteVisueel(ruimte);
                    }

                    guestLayer.revalidate();
                    guestLayer.repaint();
                }
            }
        }
    }
}
