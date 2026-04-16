package Controller.GastManagement;

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
    private final OverzichtView view;
    private final GastCreator factory;
    private ArrayList<NewGuest> listeners;
    private final ReceptieController receptie;

    public PersoonController(HotelEventManager hotelEventManager, OverzichtView overzichtView, ReceptieController receptieController) {
        this.view = overzichtView;
        this.receptie = receptieController;
        this.listeners = new ArrayList<>();
        hotelEventManager.register(this);

        factory = new GastCreator();
    }

    @Override
    public void notify(HotelEvent hotelEvent) {

        if (hotelEvent.getEventType() == HotelEventType.CHECK_IN) {

            GastModel gast = (GastModel) factory.createPersoon(
                    hotelEvent.getGuestId(),
                    "0,0",
                    null,
                    hotelEvent.getData()
            );

            if (listeners != null) {
                for (NewGuest listener : listeners) {
                    listener.onGastAangemaakt(gast);
                }
            }

        } else if (hotelEvent.getEventType() == HotelEventType.CHECK_OUT) {

            if (listeners != null) {
                for (NewGuest listener : listeners) {
                    listener.onGastVertrokken(hotelEvent.getGuestId());
                }
            }
        }
    }
    public void setNewGuestListener(NewGuest listener) {
        listeners.add(listener);
    }
}
