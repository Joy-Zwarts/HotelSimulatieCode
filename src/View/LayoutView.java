package View;

import Model.GridVakje;
import Model.LayoutModel;
import Model.Ruimte;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LayoutView {
    private JPanel hotelPanel;

    private HashMap<String, GridVakje> grid;

    public void maakGrid(int gridBreedte, int gridLengte, int vakBreedte, int vakHoogte, HashMap<String, GridVakje> grid) {

        this.grid = grid;

        hotelPanel = new JPanel(null); // panel aanmaken voor de layout met een custom grid layout
        hotelPanel.setPreferredSize(new Dimension(gridBreedte * vakBreedte, gridLengte * vakHoogte)
                // bepaal aantal rijen (breedte) en hoe wijd die zijn (vakbreedte) en bepaal het aantal kolommen (lengte) en hoe hoog die zijn (vakhoogte)
        );

        for (int y = 0; y < gridLengte; y++) {
            for (int x = 0; x < gridBreedte; x++) {

                GridVakje vak = new GridVakje(x, y, vakBreedte, vakHoogte); // maak een nieuw gridvakje aan per gecreëerde kolom en rij

                grid.put(x + "," + y, vak); // voeg deze toe aan de lijst met gridvakjes

                hotelPanel.add(vak.getVakjepanel()); // voeg het vakje toe aan de hotelpanel voor de layout
            }
        }
    }

    public void plaatsKamers( ArrayList<Ruimte> ruimtes, ArrayList<Ruimte> verplichteElementen) {
        for (Ruimte ruimte : ruimtes) { // voor elke ruimte in de lijst van ruimtes
            int startX = ruimte.getPositionX(); // get de X positie -1 (de lift zit al op 0,0 dus we doen geen -1)
            int startY = ruimte.getPositionY() - 1; // get de Y positie -1 (posities beginnen vanaf 1 dus -1)

            int w = ruimte.getDimensionW(); // get de wijdte
            int h = ruimte.getDimensionH(); // get de lengte

            for (int y = startY; y < startY + h; y++) { // startTimer bij de y positie, voor elke stap die kleiner is dan y + de hoogte
                for (int x = startX; x < startX + w; x++) { // startTimer bij de x positie, voor elke stap die kleiner is dan x + de breedte

                    GridVakje vak = grid.get(x + "," + y); // get het gridvakje die bij die coordinaten hoort

                    if (vak != null) { // als die bestaat

                        vak.zetInhoud(ruimte);

                        if (x == startX && y == startY) {
                            vak.zetInhoud(ruimte); // alleen label in linkerbovenhoek
                        } else {
                            vak.clearInhoud(); // alle andere vakjes van dit gebied leegmaken
                        }

                        // bepaal of dit vakje een top-rand moet hebben
                        boolean top;
                        if (y == startY) { // als de y hetzelfde is als de y van de gegeven positie (linksboven)
                            top = true; // topborder moet er komen want dit is de bovenrand
                        } else {
                            top = false; // anders niet
                        }

                        // bepaal of dit vakje een bottom-rand moet hebben
                        boolean bottom;
                        if (y == startY + h - 1) { // startTimer y + hoogte - 1 omdat de y al een van de hoogte vakjes bevat
                            bottom = true;
                        } else {
                            bottom = false;
                        }

                        // bepaal of dit vakje een left-rand moet hebben
                        boolean left;
                        if (x == startX) {
                            left = true;
                        } else {
                            left = false;
                        }

                        // bepaal of dit vakje een right-rand moet hebben
                        boolean right;
                        if (x == startX + w - 1) {
                            right = true;
                        } else {
                            right = false;
                        }

                        vak.setBorder(top, left, bottom, right);
                    }
                }
            }
        }
        for (Ruimte element : verplichteElementen) { // voor elke element in de lijst van verplichte elementen
            int startX = element.getPositionX() - 1; // get de Y positie -1 (posities beginnen vanaf 1 dus -1)
            int startY = element.getPositionY() - 1; // get de Y positie -1 (posities beginnen vanaf 1 dus -1)

            int w = element.getDimensionW(); // get de wijdte
            int h = element.getDimensionH(); // get de lengte

            for (int y = startY; y < startY + h; y++) { // startTimer bij de y positie, voor elke stap die kleiner is dan y + de hoogte
                for (int x = startX; x < startX + w; x++) { // startTimer bij de x positie, voor elke stap die kleiner is dan x + de breedte

                    GridVakje vak = grid.get(x + "," + y); // get het gridvakje die bij die coordinaten hoort

                    if (vak != null) { // als die bestaat

                        vak.zetInhoud(element);

                        if (element.getAreaType().equals("Trappen")) {
                            vak.zetInhoud(element); // zet de inhoud van het vakje en geef het type en de classificatie mee (aantal sterren)
                        } else {
                            if (vak != null) { // als die bestaat
                                if (x == startX && y == startY) {
                                    vak.zetInhoud(element); // alleen label in linkerbovenhoek
                                } else {
                                    vak.clearInhoud(); // alle andere vakjes van dit gebied leegmaken
                                }
                            }

                            // bepaal of dit vakje een top-rand moet hebben
                            boolean top;
                            if (y == startY) { // als de y hetzelfde is als de y van de gegeven positie (linksboven)
                                top = true; // topborder moet er komen want dit is de bovenrand
                            } else {
                                top = false; // anders niet
                            }

                            // bepaal of dit vakje een bottom-rand moet hebben
                            boolean bottom;
                            if (y == startY + h - 1) { // startTimer y + hoogte - 1 omdat de y al een van de hoogte vakjes bevat
                                bottom = true;
                            } else {
                                bottom = false;
                            }

                            // bepaal of dit vakje een left-rand moet hebben
                            boolean left;
                            if (x == startX) {
                                left = true;
                            } else {
                                left = false;
                            }

                            // bepaal of dit vakje een right-rand moet hebben
                            boolean right;
                            if (x == startX + w - 1) {
                                right = true;
                            } else {
                                right = false;
                            }

                            vak.setBorder(top, left, bottom, right);
                        }
                    }
                }
            }
        }
    }

    public JPanel getHotelPanel() {
        return hotelPanel;
    }
}
