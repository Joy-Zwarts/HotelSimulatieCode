import Controller.HotelEventManager;
import Model.HotelEvent;
import Model.HotelEventListener;
import Model.HotelEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import View.OverzichtScherm;
import javax.swing.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

    class TestEvent {

        private HotelEventManager manager;
        private TestListener listener;

        // Fake listener om events op te vangen
        static class TestListener implements HotelEventListener {
            ArrayList<HotelEvent> ontvangenEvents = new ArrayList<>();

            @Override
            public void notify(HotelEvent evt) {
                ontvangenEvents.add(evt);
            }
        }
    }
