package Controller.KamerManagement;

import Controller.PersoonFactory.GastCreator;
import Model.Personen.GastModel;
import Model.Ruimtes.KamerType;
import View.Systeem.OverzichtView;
import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;
import hotelevents.HotelEventManager;
import hotelevents.HotelEventType;

import java.util.ArrayList;

public class PersoonController implements HotelEventListener {
    private final ArrayList<GastModel> gastenLijst;
    private final OverzichtView view;
    private final GastCreator factory;
    private NewGuest newGuestListener;

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

            addGastenLijst(gast);

            newGuestListener.onGastAangemaakt(gast);

            view.tekenGastLijst(gastenLijst);

        } else if (hotelEvent.getEventType() == HotelEventType.CHECK_OUT) {
            if(getGastenLijst().contains(gastenLijst.get(hotelEvent.getGuestId()))) {
                removeGastenLijst(gastenLijst.get(hotelEvent.getGuestId()));
            }
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
    public void setNewGuestListener(NewGuest listener) {
        this.newGuestListener = listener;
    }
}
