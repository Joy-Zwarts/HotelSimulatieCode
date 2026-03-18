import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;

public class Layout {
    private ArrayList<Ruimte> ruimtes = new ArrayList();
    private ArrayList<Ruimte> verplichteElementen = new ArrayList();
    private HashMap<String, GridVakje> grid = new HashMap();
    private JPanel hotelPanel;
    private int vakHoogte = 60;
    private int vakBreedte = 120;
    private int gridBreedte;
    private int gridLengte;

    public void addKamer(Ruimte ruimte) {
        this.ruimtes.add(ruimte);
    }

    public void berekenGridGrootte() {
        for(Ruimte ruimte : this.ruimtes) {
            int right = ruimte.getPositionX() + ruimte.getDimensionW() - 1;
            int bottom = ruimte.getPositionY() + ruimte.getDimensionH() - 1;
            if (right > this.gridBreedte) {
                this.gridBreedte = right;
            }

            if (bottom > this.gridLengte) {
                this.gridLengte = bottom;
            }
        }

        this.gridBreedte += 2;
        ++this.gridLengte;
    }

    public void maakGrid() {
        this.hotelPanel = new JPanel((LayoutManager)null);
        this.hotelPanel.setPreferredSize(new Dimension(this.gridBreedte * this.vakBreedte, this.gridLengte * this.vakHoogte));

        for(int y = 0; y < this.gridLengte; ++y) {
            for(int x = 0; x < this.gridBreedte; ++x) {
                GridVakje vak = new GridVakje(x, y, this.vakBreedte, this.vakHoogte);
                this.grid.put(x + "," + y, vak);
                this.hotelPanel.add(vak.getVakjepanel());
            }
        }

    }

    public void plaatsKamers() {
        for(Ruimte ruimte : this.ruimtes) {
            int startX = ruimte.getPositionX();
            int startY = ruimte.getPositionY() - 1;
            int w = ruimte.getDimensionW();
            int h = ruimte.getDimensionH();

            for(int y = startY; y < startY + h; ++y) {
                for(int x = startX; x < startX + w; ++x) {
                    GridVakje vak = this.getGridVakje(x, y);
                    if (vak != null) {
                        vak.zetInhoud(ruimte);
                        boolean top;
                        if (y == startY) {
                            top = true;
                        } else {
                            top = false;
                        }

                        boolean bottom;
                        if (y == startY + h - 1) {
                            bottom = true;
                        } else {
                            bottom = false;
                        }

                        boolean left;
                        if (x == startX) {
                            left = true;
                        } else {
                            left = false;
                        }

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

        for(Ruimte element : this.verplichteElementen) {
            int startX = element.getPositionX() - 1;
            int startY = element.getPositionY() - 1;
            int w = element.getDimensionW();
            int h = element.getDimensionH();

            for(int y = startY; y < startY + h; ++y) {
                for(int x = startX; x < startX + w; ++x) {
                    GridVakje vak = this.getGridVakje(x, y);
                    if (vak != null) {
                        vak.zetInhoud(element);
                        boolean top;
                        if (y == startY) {
                            top = true;
                        } else {
                            top = false;
                        }

                        boolean bottom;
                        if (y == startY + h - 1) {
                            bottom = true;
                        } else {
                            bottom = false;
                        }

                        boolean left;
                        if (x == startX) {
                            left = true;
                        } else {
                            left = false;
                        }

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

    public JPanel getHotelPanel() {
        return this.hotelPanel;
    }

    public GridVakje getGridVakje(int x, int y) {
        String coordinaten = x + "," + y;
        return (GridVakje)this.grid.get(coordinaten);
    }

    public void addverplichteElementen(Layout layout) {
        String schachtDimension = "1," + this.gridLengte / 2;
        layout.addKamerBuitenJson("Schacht", "1,1", schachtDimension);
        int var10000 = this.gridLengte + 1;
        String verdiepingLift = "1," + var10000 / 2;
        layout.addKamerBuitenJson("Lift", verdiepingLift, "1,1");
        var10000 = this.gridLengte / 2;
        String schachtStart = "1," + (var10000 + 2);
        layout.addKamerBuitenJson("Schacht", schachtStart, schachtDimension);
        String trappenPosition = this.gridBreedte + ",1";
        String trappenDimension = "1," + this.gridLengte;
        layout.addKamerBuitenJson("Trappen", trappenPosition, trappenDimension);
        String lobbyPosition = "2," + this.gridLengte;
        String lobbyDimension = this.gridLengte - 3 + ",1";
        layout.addKamerBuitenJson("Lobby", lobbyPosition, lobbyDimension);
    }

    public void addKamerBuitenJson(String AreaType, String position, String dimension) {
        switch (AreaType) {
            case "Lift" -> this.verplichteElementen.add(new Lift(AreaType, position, dimension, (this.gridLengte + 1) / 2, true));
            case "Schacht" -> this.verplichteElementen.add(new Schacht(AreaType, position, dimension));
            case "Trappen" -> this.verplichteElementen.add(new Trappenhuis(AreaType, position, dimension));
            case "Lobby" -> this.verplichteElementen.add(new Lobby(AreaType, position, dimension));
        }

    }
}
