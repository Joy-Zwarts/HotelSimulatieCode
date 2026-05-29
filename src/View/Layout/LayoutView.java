package View.Layout;

import Controller.Layout.GridVakjeController;
import Model.Layout.Locatie;
import Controller.Systeem.PauseController;
import Model.Layout.GridVakjeModel;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.RuimteModel;
import View.Systeem.HotelSimulatieView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LayoutView {

    private JPanel hotelPanel;
    private HashMap<Locatie, GridVakjeController> grid;
    private int gridBreedte;
    private int gridLengte;
    private final PauseController pauseController;

    public LayoutView(PauseController pauseController, HotelSimulatieView SimulatieView) {
        this.pauseController = pauseController;
    }

    // maakt de grid voor de hotel layout
    public void maakGrid(int gridBreedte, int gridLengte, int vakBreedte, int vakHoogte, HashMap<Locatie, GridVakjeController> grid) {
        this.grid = grid;
        this.gridBreedte = gridBreedte;
        this.gridLengte = gridLengte;

        // maak een nieuw hotelpaneel aan
        if (hotelPanel == null) {
            hotelPanel = new JPanel();
        } else {
            hotelPanel.removeAll();
        }

        int totalePixelsBreedte = gridBreedte * vakBreedte;
        int totalePixelsHoogte = gridLengte * vakHoogte;

        hotelPanel.setPreferredSize(new Dimension(totalePixelsBreedte, totalePixelsHoogte));
        hotelPanel.setLayout(new GridLayout(gridLengte, gridBreedte));

        // loopt door de x en y uitgerekend van de grid en maak een nieuw gridvakje aan voor elke x en y
        for (int y = 0; y < gridLengte; y++) {
            for (int x = 0; x < gridBreedte; x++) {
                GridVakjeController controller = new GridVakjeController(new GridVakjeModel(x, y), new GridVakjeView(x, y, vakBreedte, vakHoogte), pauseController);

                // sla de cel op in de lijst van vakjes met de locatie als key
                Locatie locatie = new Locatie(x, y);
                grid.put(locatie, controller);

                // voeg toe aan scherm
                hotelPanel.add(controller.getGridView().getVakjePanel());
            }
        }

        // ververs UI
        hotelPanel.revalidate();
        hotelPanel.repaint();
    }

    // zet alle kamers op een rij en telt het aantal vakjes verticaal en horizontaal bij elkaar op om de lengte en breedte te berekenen
    public void berekenGridGrootte(ArrayList<RuimteModel> ruimtes) {
        gridBreedte = 0;
        gridLengte = 0;
        for (RuimteModel ruimte : ruimtes) {
            int right = ruimte.getPosition().getX() + ruimte.getDimensionW() - 1;
            int bottom = ruimte.getPosition().getY() + ruimte.getDimensionH() - 1;

            if (right > gridBreedte) gridBreedte = right;
            if (bottom > gridLengte) gridLengte = bottom;
        }

        gridBreedte += 2; // ruimte voor lift links en trap rechts
        gridLengte += 1;  // ruimte voor lobby onderin
    }

    public void plaatsKamers(ArrayList<RuimteModel> ruimtes, ArrayList<RuimteModel> verplichteElementen) {
        plaatsRuimteLijst(ruimtes, false);
        plaatsRuimteLijst(verplichteElementen, true);
    }

    private void plaatsRuimteLijst(ArrayList<RuimteModel> lijst, boolean isVerplicht) {
        for (RuimteModel ruimte : lijst) {
            int startX;

            if (isVerplicht) { // verplichte elementen gebruiken 0 based x
                startX = ruimte.getPosition().getX() - 1;
            } else { // normale kamers gebruiken 1 based x
                startX = ruimte.getPosition().getX();
            }

            int startY = ruimte.getPosition().getY() - 1;

            int w = ruimte.getDimensionW();
            int h = ruimte.getDimensionH();

            // maak de kamers visueel klaar om geplaatst te kunnen worden
            for (int y = startY; y < startY + h; y++) {
                for (int x = startX; x < startX + w; x++) {
                    GridVakjeController vak = grid.get(new Locatie(x, y));

                    if (vak != null) {
                        vak.getModel().setRuimte(ruimte);
                        vak.getModel().setlinksboven(x == startX && y == startY);
                        vak.getModel().setLinksonder(x == startX && y == startY + h - 1);
                        vak.getModel().setRechtsboven(x == startX + w - 1 && y == startY);

                        vak.getGridView().zetInhoud(ruimte, vak.getModel().islinksboven(), vak.getModel().islinksOnder());
                        vak.getGridView().zetPersonenAantal(ruimte, vak.getModel().isRechtsboven(), vak.getModel().islinksboven());
                        vak.updateView();

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

    // geef de kamers een nummer gebaseerd op verdieping
    public void nummerDeKamers() {
        // bepaal de hoogste rij waar hotelkamers kunnen staan
        int maxKamerRij = gridLengte - 2;

        // loop door alle rijen van de grid
        for (int y = 0; y <= maxKamerRij; y++) {

            // bereken het verdiepingsnummer
            int verdieping = maxKamerRij - y + 1;

            // start teller voor de kamers op deze specifieke verdieping
            int kamerTellerOpVerdieping = 1;

            // loop door alle kolommen van de grid
            for (int x = 0; x < gridBreedte; x++) {

                // pak de controller van het specifieke gridvakje
                GridVakjeController vak = grid.get(new Locatie(x, y));

                if (vak != null && vak.getModel().getRuimte() != null) {

                    RuimteModel ruimte = vak.getModel().getRuimte();

                    // controleer of dit vakje de linkerbovenhoek van de ruimte is en of het een echte kamer is
                    if (vak.getModel().islinksboven() && ruimte instanceof KamerModel) {
                        KamerModel kamer = (KamerModel) ruimte;

                        // bereken het kamernummer
                        int kamerNummer = (verdieping * 100) + kamerTellerOpVerdieping;


                        kamer.setRoomNumber(kamerNummer);


                        kamerTellerOpVerdieping++;


                        int startX = x;
                        int startY = y;

                        // hoeveel gridvakjes is deze kamer breed
                        int w = kamer.getDimensionW();

                        // en hoog
                        int h = kamer.getDimensionH();

                        // loop door alle rijen die deze kamer in beslag neemt
                        for (int ky = startY; ky < startY + h; ky++) {

                            // loop door alle kolommen die deze kamer in beslag neemt
                            for (int kx = startX; kx < startX + w; kx++) {

                                // haal het gridvakje op dat onderdeel is van deze kamer
                                GridVakjeController deelVak = grid.get(new Locatie(kx, ky));

                                // als dit deelvakje bestaat
                                if (deelVak != null) {

                                    deelVak.getGridView().zetInhoud(kamer, deelVak.getModel().islinksboven(), deelVak.getModel().islinksOnder());
                                    deelVak.updateView();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int getGridBreedte() { return gridBreedte; }
    public int getGridLengte() { return gridLengte; }
    public HashMap<Locatie, GridVakjeController> getGrid() { return grid; }
    public JPanel getHotelPanel() { return hotelPanel; }
    public void setGrid(HashMap<Locatie, GridVakjeController> newGrid) { this.grid = newGrid; }
    public void setGridBreedte(int i) { gridBreedte = i; }
    public void setGridLengte(int i) { gridLengte = i; }
}