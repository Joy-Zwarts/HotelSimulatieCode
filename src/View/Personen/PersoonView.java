package View.Personen;

import Controller.Layout.GridVakjeController;
import Model.Personen.GastModel;
import Model.Ruimtes.RuimteModel;
import View.Layout.GridVakjeView;
import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;

import java.awt.event.ActionListener;
import java.util.HashMap;

public class PersoonView implements HotelEventListener {

    private GastModel gast;
    private HashMap<String, GridVakjeController> grid;
    private RuimteModel ruimte;

    public PersoonView(GastModel gast, HashMap<String, GridVakjeController> Grid) {
        this.gast = gast;
        this.grid = Grid;
    }

    public void plaatsGast(String locatie, GastModel gast) {

        ruimte = null;

        GridVakjeController startVak = grid.get(locatie);
        if (startVak == null) return;

        ruimte = startVak.getModel().getRuimte();

        ruimte.setAantalGasten(ruimte.getAantalGasten() + 1);


        refreshRuimte(ruimte);
    }

    public void refreshRuimte(RuimteModel ruimte) {

        for (GridVakjeController vak : grid.values()) {

            if (!ruimte.equals(vak.getModel().getRuimte())) continue;

            GridVakjeView view = vak.getGridView();

            view.clearInhoud();
            view.zetInhoud(
                    ruimte,
                    vak.getModel().islinksboven(),
                    vak.getModel().islinksOnder()
            );

            view.zetPersonenAantal(
                    ruimte,
                    vak.getModel().isRechtsboven()
            );

            vak.updateView();
        }
    }


    @Override
    public void notify(HotelEvent hotelEvent) {
        if (ruimte == null){
            refreshRuimte(ruimte);
        }
    }
}
