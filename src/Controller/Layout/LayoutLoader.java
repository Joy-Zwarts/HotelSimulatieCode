package Controller.Layout;

import Controller.GastManagement.NewRoom;
import Controller.RuimteFactory.*;
import Controller.Systeem.FilePicker;
import Controller.Systeem.PauseController;
import Model.Layout.LayoutModel;
import Model.Ruimtes.KamerType;
import Model.Layout.RuimteData;
import Model.Ruimtes.RuimteModel;
import View.Layout.LayoutView;
import View.Systeem.EventPanel;
import View.Systeem.HotelSimulatieView;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LayoutLoader {
    // attributen

    private final HotelEventManager manager;
    private final HotelSimulatieView view;
    private LayoutModel model;
    private LayoutController controller;
    private final PauseController pauseController;
    private final HotelSimulatieView simulatieView;
    private NewRoom newRoomListener;
    private final ArrayList<LayoutGeladen> listeners;

    // constructor
    public LayoutLoader(HotelEventManager manager, HotelSimulatieView view, LayoutModel model, PauseController pauseController, HotelSimulatieView simulatieView) {
        this.manager = manager;
        this.view = view;
        this.model = model;
        this.pauseController = pauseController;
        this.simulatieView = simulatieView;
        this.listeners = new ArrayList<>();
    }

    // maakt een nieuwe file picker klasse aan, krijgt daarvan een bestand en laad de layout daarvan en notified de listeners daarover
    public LayoutModel loadLayout() {
        try {
            FilePicker picker = new FilePicker();
            File gekozenBestand = picker.kiesBestand();

            if (gekozenBestand == null) {
                JOptionPane.showMessageDialog(view, "Geen bestand geselecteerd.");
                return null;
            }

            Controller.Layout.LayoutParser parser = new Controller.Layout.LayoutParser();
            List<RuimteData> ruimtes = parser.parse(gekozenBestand.getAbsolutePath());

            // nieuw model maken
            this.model = new LayoutModel();

            // objecten maken via factory
            for (RuimteData data : ruimtes) {
                maakKamer(data);
            }

            LayoutView layoutView = new LayoutView(pauseController, simulatieView);
            EventPanel eventPrint = new EventPanel(manager);

            controller = new LayoutController(model, layoutView);

            view.setLayoutView(layoutView.getHotelPanel());
            view.setLegendaView();
            view.setRightView(eventPrint.getPanelRechts());

            // notify de listeners dat de layout is geladen

            for (LayoutGeladen listener : listeners) {
                listener.onLayoutGeladen(controller);
            }

            return model;

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Fout bij laden: " + ex.getMessage());
            return null;
        }
    }

    // kiest wat voor type kamer er moet worden gemaakt en kiest de juiste factory daarvoor, maakt een nieuw ruimtemodel aan met de opgehaalde data uit de json
    private void maakKamer(RuimteData data) {
        RuimteFactory factory;

        switch (data.areaType) {
            case "Room":
                factory = new RoomCreator();
                break;
            case "Restaurant":
                factory = new RestaurantCreator();
                break;
            case "Fitness":
                factory = new FitnessCreator();
                break;
            case "Cinema":
                factory = new CinemaCreator();
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + data.areaType);
        }

        // maak een nieuw ruimtemodel aan met de data en de bijbehorende factory

        RuimteModel ruimte = factory.createRuimte(
                data.position,
                data.dimension,
                data.capacity,
                data.classification
        );

        // voeg de ruimte toe aan het layout model
        model.addKamer(ruimte);

        // notify de listeners dat er een nieuwe kamer is aangemaakt als het type ruimte ROOM is
        if (ruimte.getAreaType().equals(KamerType.ROOM) && newRoomListener != null) {
            newRoomListener.onNewRoom(ruimte);
        }
    }

    // set een nieuwe listener voor als er een kamer wordt aangemaakt
    public void setNewRoomListener(NewRoom listener) {
        this.newRoomListener = listener;
    }

    public LayoutController getController() {
        return controller;
    }

    // set een nieuwe listener als de layout is geladen
    public void setNewLayoutListener(LayoutGeladen listener) {
        listeners.add(listener);
    }
}