package Controller.PersoonManagement;

import Controller.Layout.GridVakjeController;
import Controller.Layout.LayoutController;
import Controller.Layout.Interfaces.LayoutGeladen;
import Controller.PersoonManagement.Interfaces.NewGast;
import Controller.PersoonManagement.Interfaces.NewLift;
import Controller.PersoonManagement.Interfaces.NewSchoonmaker;
import Controller.Systeem.Interfaces.reset;
import Model.Layout.Locatie;
import Model.Personen.GastModel;
import Model.Personen.LiftModel;
import Model.Personen.PersoonModel;
import Model.Personen.SchoonmakerModel;
import Model.Ruimtes.RuimteModel;
import View.Layout.GridVakjeView;
import View.Layout.LayoutView;
import View.Layout.LiftView;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class PlaatsHelper implements NewGast, LayoutGeladen, NewSchoonmaker, reset, NewLift {

    // attributen
    private HashMap<Locatie, GridVakjeController> grid;
    private LayoutView layoutView;
    private LayoutController layoutController;

    // constructor
    public PlaatsHelper(LayoutView view, LayoutController controller) {
        this.grid = null;
        this.layoutView = view;
        this.layoutController = controller;
    }

    // plaats de gast hun icoontje op locatie
    @Override
    public void onGastAangemaakt(GastModel gast) {
        plaatsPersoon(gast);
    }

    @Override
    public void onLiftAangemaakt(LiftModel lift) {
        // Zorg dat het grid up-to-date is vanuit de controller
        if (this.grid == null && this.layoutController != null && this.layoutController.getView() != null) {
            this.grid = this.layoutController.getView().getGrid();
        }

        Locatie loc = lift.getLocatie();

        if (grid != null && loc != null) {
            GridVakjeController vak = grid.get(loc);
            if (vak != null) {
                GridVakjeView vakView = vak.getGridView();

                // Vraag direct de echte maten op uit het LayoutModel via de controller
                int actueleBreedte = layoutController.getModel().getVakBreedte();
                int actueleHoogte = layoutController.getModel().getVakHoogte();

                // Veilige fallback (just in case)
                if (actueleBreedte <= 0) actueleBreedte = 118;
                if (actueleHoogte <= 0) actueleHoogte = 59;

                // Maak de LiftView aan met de exacte model-maten
                LiftView liftView = new LiftView(actueleBreedte, actueleHoogte);

                // VOEG TOE AAN DE LIFT LAYER (LAAG 1) via de nieuwe methode in GridVakjeView
                vakView.voegLiftToe(liftView);
            }
        }
    }

    @Override
    public void onSchoonmakerAangemaakt(SchoonmakerModel schoonmaker) {
        plaatsPersoon(schoonmaker);
    }

    public void plaatsPersoon(PersoonModel persoon) {
        // Snelkoppeling: als grid null is, probeer hem NU live op te halen uit de controller
        if (this.grid == null && this.layoutController != null && this.layoutController.getView() != null) {
            this.grid = this.layoutController.getView().getGrid();
        }

        if (persoon == null) {
            System.out.println("Fout: Persoon is null in plaatsPersoon!");
            return;
        }

        Locatie Loc = persoon.getLocatie();

        if (this.grid != null && Loc != null) {
            GridVakjeController vak = this.grid.get(Loc);
            if (vak != null) {
                JPanel guestLayer = vak.getGridView().getGuestPanel();

                // Check of het label wel bestaat
                if (persoon.getPersoonLabel() != null) {
                    guestLayer.add(persoon.getPersoonLabel());
                    guestLayer.revalidate();
                    guestLayer.repaint();
                } else {
                    System.out.println("Fout: " + persoon.getClass().getSimpleName() + " heeft geen persoonLabel!");
                }
            } else {
                System.out.println("Waarschuwing: Geen gridvakje gevonden op locatie: " + Loc.getX() + ", " + Loc.getY());
            }
        } else {
            System.out.println("CRITIEK: Persoon kon niet worden geplaatst omdat het GRID nog steeds NULL is!");
        }
    }

    // zet het schoonmaker-icoontje op de juiste plek na een stap
    @Override
    public void onSchoonmakerVerplaatst(SchoonmakerModel schoonmaker, Locatie oudeLocatie) {
        if (oudeLocatie != null && oudeLocatie.equals(schoonmaker.getLocatie())) {return;}

        GridVakjeController oudVak = grid.get(oudeLocatie);
        if (oudVak != null) {
            JPanel oudPanel = oudVak.getGridView().getGuestPanel();
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

                // verhoog de juiste teller in het model
                if (persoon instanceof GastModel) {
                    ruimte.setAantalGasten(ruimte.getAantalGasten() + 1);

                    // als het een kamer is zetten we deze op bezet
                    if (ruimte instanceof Model.Ruimtes.KamerModel) {
                        ((Model.Ruimtes.KamerModel) ruimte).setBezet(true);
                    }
                } else if (persoon instanceof SchoonmakerModel) {
                    ruimte.setAantalSchoonmakers(ruimte.getAantalSchoonmakers() + 1);
                }

                // verwijder het lopende icoon van het gridvakje
                JPanel guestLayer = vak.getGridView().getGuestPanel();
                guestLayer.remove(persoon.getPersoonLabel());

                refreshRuimteVisueel(ruimte);
                guestLayer.revalidate();
                guestLayer.repaint();
            }
        }
    }

    public void vertrokken(PersoonModel persoon, Locatie oudeLocatie) {
        GridVakjeController vak = grid.get(oudeLocatie);

        if (vak != null) {
            RuimteModel ruimte = vak.getModel().getRuimte();
            if (ruimte != null) {

                // verlaag de juiste teller en zorg dat deze nooit onder de 0 zakt
                if (persoon instanceof GastModel) {
                    ruimte.setAantalGasten(Math.max(0, ruimte.getAantalGasten() - 1));

                    // zet de bezet status op false als geen gasten meer in de kamer zijn
                    if (ruimte instanceof Model.Ruimtes.KamerModel && ruimte.getAantalGasten() == 0) {
                        ((Model.Ruimtes.KamerModel) ruimte).setBezet(false);
                    }
                } else if (persoon instanceof SchoonmakerModel) {
                    ruimte.setAantalSchoonmakers(Math.max(0, ruimte.getAantalSchoonmakers() - 1));
                }

                // verwijder het lopende icoon van het gridvakje
                JPanel guestLayer = vak.getGridView().getGuestPanel();
                guestLayer.remove(persoon.getPersoonLabel());

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

    // update und repaint de panels waar verandering in is gekomen
    private void refreshRuimteVisueel(RuimteModel ruimte) {
        if (grid == null) return;

        for (GridVakjeController vak : grid.values()) {
            if (vak != null && vak.getModel() != null) {
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
                RuimteModel ruimte = vak.getModel().getRuimte();
                if (ruimte != null) {
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

    @Override
    public void onLiftVerplaatst(LiftModel lift, Locatie oudeLocatie) {
        if (oudeLocatie != null && oudeLocatie.equals(lift.getLocatie())) return;

        LiftView deLiftView = null;

        // 1. Oude vakje opschonen en de LiftView uit de liftContainer halen (Laag 1)
        GridVakjeController oudVak = grid.get(oudeLocatie);
        if (oudVak != null) {
            JPanel oudLiftContainer = oudVak.getGridView().getLiftContainer();

            for (Component comp : oudLiftContainer.getComponents()) {
                if (comp instanceof LiftView) {
                    deLiftView = (LiftView) comp;
                    oudLiftContainer.remove(deLiftView);
                    break;
                }
            }
            oudLiftContainer.revalidate();
            oudLiftContainer.repaint();
        }

        // 2. De lift in de liftContainer van het nieuwe vakje plaatsen
        if (deLiftView != null) {
            GridVakjeController nieuwVak = grid.get(lift.getLocatie());
            if (nieuwVak != null) {
                // Voeg direct toe via de methode van GridVakjeView (zet hem weer netjes op Laag 1)
                nieuwVak.getGridView().voegLiftToe(deLiftView);
            }
        }
    }

    @Override
    public void onLayoutGeladen(LayoutController layoutController) {
        this.layoutController = layoutController;
        if (layoutController != null) {
            this.layoutView = layoutController.getView();
            this.grid = layoutView.getGrid();
        }
    }

    @Override
    public void resetSimulatie() {
        if (grid != null) {
            for (GridVakjeController vak : grid.values()) {
                if (vak != null) {
                    // Verwijder alle lopende entiteiten uit de guest panel (Laag 2)
                    JPanel guestLayer = vak.getGridView().getGuestPanel();
                    guestLayer.removeAll();

                    // Verwijder ook de lift uit de lift layer (Laag 1)
                    vak.getGridView().verwijderLift();

                    // zet de tellers van het kamer-model terug naar 0
                    RuimteModel ruimte = vak.getModel().getRuimte();
                    if (ruimte != null) {
                        ruimte.setAantalGasten(0);
                        ruimte.setAantalSchoonmakers(0);
                        refreshRuimteVisueel(ruimte);
                    }

                    guestLayer.revalidate();
                    guestLayer.repaint();
                }
            }
        }
    }
}