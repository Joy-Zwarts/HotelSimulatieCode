package Controller.GastManagement;

import Controller.Layout.LayoutController;
import Controller.Layout.LayoutGeladen;
import Controller.Layout.Locatie;
import Controller.PersoonFactory.GastCreator;
import Model.Personen.GastModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.LobbyModel;
import Model.Ruimtes.RuimteModel;
import View.Systeem.OverzichtView;
import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;
import hotelevents.HotelEventManager;
import hotelevents.HotelEventType;

import java.util.ArrayList;

public class PersoonController implements HotelEventListener, LayoutGeladen {

    // attributen
    private final OverzichtView view;
    private final GastCreator factory;
    private final ArrayList<NewGuest> listeners;
    private final ReceptieController receptie;
    private Locatie startLocatie;

    // constructor
    public PersoonController(HotelEventManager hotelEventManager, OverzichtView overzichtView, ReceptieController receptieController) {
        this.view = overzichtView;
        this.receptie = receptieController;
        this.listeners = new ArrayList<>();
        hotelEventManager.register(this);

        factory = new GastCreator();
    }

    // reacties op events

    // bij een check-in event wordt er een nieuwe gast aan gemaakt en worden de listeners ge-notified hierover
    @Override
    public void notify(HotelEvent hotelEvent) {

        if (hotelEvent.getEventType() == HotelEventType.CHECK_IN) {

            // maak nieuwe locatie klassen aan voor current locatie en target locatie
            Locatie locatie = startLocatie;
            Locatie targetLocatie = new Locatie(0,0);

            GastModel gast = (GastModel) factory.createPersoon(
                    hotelEvent.getGuestId(),
                    locatie,
                    targetLocatie,
                    hotelEvent.getData()
            );

            // notify de listeners dat er een nieuwe gast is aangemaakt
            if (listeners != null) {
                for (NewGuest listener : listeners) {
                    listener.onGastAangemaakt(gast);
                }
            }

        } else if (hotelEvent.getEventType() == HotelEventType.CHECK_OUT) { // bij een checkout event

            GastModel gast = receptie.getGast(hotelEvent.getGuestId());

            // notify listeners dat de gast is weggegaan
            if (listeners != null) {
                for (NewGuest listener : listeners) {
                    listener.onGastVertrokken(gast);
                }
            }
        }
    }

    // getters en setters

    // zet een listener voor de gast-events
    public void setNewGuestListener(NewGuest listener) {
        listeners.add(listener);
    }

    @Override
    public void onLayoutGeladen(LayoutController layoutController) {
        int x = layoutController.getView().getGridBreedte()/2;
        int y = layoutController.getView().getGridLengte() -1 ;
        startLocatie = new Locatie(x,y);
    }
}
