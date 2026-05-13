    package Controller.PersoonManagement;

    import Controller.Layout.GridVakjeController;
    import Controller.Layout.LayoutController;
    import Controller.Layout.LayoutGeladen;
    import Model.Layout.Locatie;
    import Model.Personen.GastModel;
    import Model.Personen.PersoonModel;
    import Model.Ruimtes.RuimteModel;
    import View.Layout.LayoutView;

    import javax.swing.*;
    import java.util.HashMap;

    public class PlaatsHelper implements NewGast, LayoutGeladen {

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
            Locatie gastLoc = gast.getLocatie();
            if (grid != null && gastLoc != null) {
                GridVakjeController vak = grid.get(gastLoc);
                if (vak != null) {
                    JPanel guestLayer = vak.getGridView().getGuestPanel();
                    guestLayer.add(gast.getPersoonLabel());

                    guestLayer.revalidate();
                    guestLayer.repaint();
                }
            }
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

        // als de gast aangekomen is in de target locatie gaat hij er in en wordt het drukte label verhoogd
        @Override
        public void onGastAangekomenInKamer(GastModel gast, Locatie behaaldeLocatie) {
            Locatie gastLoc = gast.getLocatie();
            GridVakjeController vak = grid.get(gastLoc);

            if (vak != null) {
                RuimteModel ruimte = vak.getModel().getRuimte();
                if (ruimte != null) {
                    // teller verhogen
                    ruimte.setAantalGasten(ruimte.getAantalGasten() + 1);
                    refreshRuimteVisueel(ruimte);

                    // verwijder gast want die gaat naar binnen
                    JPanel guestLayer = vak.getGridView().getGuestPanel();
                    guestLayer.remove(gast.getPersoonLabel());

                    refreshRuimteVisueel(ruimte);

                    guestLayer.revalidate();
                    guestLayer.repaint();
                }
            }
        }

        // als de gast weg is gegaan van de vorige target locatie wordt het drukte label verlaagt
        @Override
        public void onGastGaatWegUitKamer(GastModel gast, Locatie oudeLocatie) {
            GridVakjeController vak = grid.get(oudeLocatie);

            if (vak != null) {
                RuimteModel ruimte = vak.getModel().getRuimte();
                if (ruimte != null) {
                    // teller verlagen
                    ruimte.setAantalGasten(ruimte.getAantalGasten() - 1);
                    refreshRuimteVisueel(ruimte);

                    // plaats gast weer want die is nu weer uit de kamer
                    GridVakjeController nieuwVak = grid.get(gast.getVorigeLocatie());
                    JPanel nieuwPanel = nieuwVak.getGridView().getGuestPanel();
                    nieuwPanel.add(gast.getPersoonLabel());

                    nieuwPanel.revalidate();
                    nieuwPanel.repaint();
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

        // reacties op events

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
    }
