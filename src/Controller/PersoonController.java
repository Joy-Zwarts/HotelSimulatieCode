package Controller;

import Controller.PersoonFactory.GastCreator;
import Controller.PersoonFactory.PersoonFactory;
import Model.GastModel;
import View.OverzichtView;
import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;
import hotelevents.HotelEventManager;
import hotelevents.HotelEventType;

import java.util.ArrayList;

public class PersoonController implements HotelEventListener {

    private PersoonFactory persoonFactory;
    private ArrayList<GastModel> gastenLijst;
    private HotelEventManager manager;
    private OverzichtView view;
    private GastCreator factory;

    public PersoonController(HotelEventManager hotelEventManager,  OverzichtView overzichtView) {
        this.manager = hotelEventManager;
        this.view = overzichtView;
        gastenLijst = new ArrayList<>();
        manager.register(this);

        factory = new GastCreator();
    }

    @Override
    public void notify(HotelEvent hotelEvent) {

        if (hotelEvent.getEventType() == HotelEventType.CHECK_IN) {

            GastModel gast = (GastModel) factory.createPersoon(hotelEvent.getGuestId(), "Lobby", null);

            gastenLijst.add(gast);
            view.tekenGastLijst(gastenLijst);

        } else if (hotelEvent.getEventType() == HotelEventType.CHECK_OUT) {

            gastenLijst.removeIf(g -> g.getGastID() == hotelEvent.getGuestId());
            view.tekenGastLijst(gastenLijst);
        }
    }

    public ArrayList<GastModel> getGastenLijst() {
        return gastenLijst;
    }
    public void addGastenLijst(GastModel gast) {
        gastenLijst.add(gast);
    }
    public void removeGastenLijst(GastModel gast) {
        gastenLijst.remove(gast);
    }
}
