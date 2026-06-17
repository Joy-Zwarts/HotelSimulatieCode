package Controller.Events;

import Controller.PersoonManagement.GastController;
import Controller.PersoonManagement.SchoonmakerController;
import Controller.Systeem.Interfaces.reset;
import hotelevents.HotelEvent;
import Controller.Events.Interfaces.*;


    public class GodzillaController implements godzillaEvent, noneEvent, reset {

        private final GastController gastController;
        private final SchoonmakerController schoonmakerController;

        private boolean godzillaActief = false;
        private int huidigeVerwoesteX = 0;
        private int tickTeller = 0;

        private int ticksPerKolom = 3;

        public GodzillaController(GastController gastController, SchoonmakerController schoonmakerController) {
            this.gastController = gastController;
            this.schoonmakerController = schoonmakerController;
        }

        @Override
        public void godzilla(HotelEvent hotelEvent) {
            System.out.println("GODZILLA EVENT! Het hotel wordt verwoest!");

            godzillaActief = true;
            huidigeVerwoesteX = 0;
            tickTeller = 0;

            // bestaande evacuatie hergebruiken
            gastController.evacuate(hotelEvent);
            schoonmakerController.evacuate(hotelEvent);
        }

        @Override
        public void HTETick(HotelEvent hotelEvent) throws InterruptedException {
            if (!godzillaActief) {
                return;
            }

            tickTeller++;

            if (tickTeller < ticksPerKolom) {
                return;
            }

            tickTeller = 0;

            System.out.println("Godzilla verwoest x-kolom: " + huidigeVerwoesteX);

            gastController.verwerkGodzillaSchade(huidigeVerwoesteX);
            schoonmakerController.verwerkGodzillaSchade(huidigeVerwoesteX);

            huidigeVerwoesteX++;
        }

        @Override
        public void resetSimulatie() {
            godzillaActief = false;
            huidigeVerwoesteX = 0;
            tickTeller = 0;
        }
    }


