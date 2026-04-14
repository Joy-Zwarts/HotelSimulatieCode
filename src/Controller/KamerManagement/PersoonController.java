package Controller.KamerManagement;

import Controller.PersoonFactory.GastCreator;
import Controller.PersoonFactory.PersoonFactory;
import Model.Personen.GastModel;
import Model.Ruimtes.KamerType;
import View.Systeem.OverzichtView;
import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;
import hotelevents.HotelEventManager;
import hotelevents.HotelEventType;

import java.util.ArrayList;

public class PersoonController implements HotelEventListener {

    private PersoonFactory persoonFactory;
    private final ArrayList<GastModel> gastenLijst;
    private final OverzichtView view;
    private final GastCreator factory;

    public PersoonController(HotelEventManager hotelEventManager,  OverzichtView overzichtView) {
        this.view = overzichtView;
        gastenLijst = new ArrayList<>();
        hotelEventManager.register(this);

        factory = new GastCreator();
    }

    @Override
    public void notify(HotelEvent hotelEvent) {

        if (hotelEvent.getEventType() == HotelEventType.CHECK_IN) {

            GastModel gast = (GastModel) factory.createPersoon(hotelEvent.getGuestId(), KamerType.LOBBY, null, hotelEvent.getData());

            gastenLijst.add(gast);
            view.tekenGastLijst(gastenLijst);

        } else if (hotelEvent.getEventType() == HotelEventType.CHECK_OUT) {

            gastenLijst.removeIf(gast -> gast.getGastID() == hotelEvent.getGuestId());
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
