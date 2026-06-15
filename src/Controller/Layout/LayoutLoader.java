package Controller.Layout;

import Controller.Layout.Interfaces.LayoutGeladen;
import Controller.PersoonManagement.Interfaces.NewKamer;
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
    private NewKamer newRoomListener;
    private final ArrayList<LayoutGeladen> listeners;

    // constructor
    public LayoutLoader(HotelEventManager manager, HotelSimulatieView view, LayoutModel model, PauseController pauseController) {
        this.manager = manager;
        this.view = view;
        this.model = model;
        this.pauseController = pauseController;
        this.listeners = new ArrayList<>();
    }

    // maakt een nieuwe file picker klasse aan, krijgt daarvan een bestand en laad de layout daarvan en notified de listeners daarover
    public LayoutModel loadLayout() {
        try {
            File gekozenBestand = getFileFromPicker();

            if (gekozenBestand == null) {
                showMessage("Geen bestand geselecteerd.");
                return null;
            }

            Controller.Layout.LayoutParser parser = new Controller.Layout.LayoutParser();
            List<RuimteData> ruimtes = parser.parse(gekozenBestand.getAbsolutePath());

            // nieuw model maken
            this.model = new LayoutModel();

            // objecten maken via factory en toevoegen aan model
            for (RuimteData data : ruimtes) {
                maakKamer(data);
            }

            LayoutView layoutView = new LayoutView(pauseController, view);
            EventPanel eventPrint = new EventPanel(manager);

            controller = new LayoutController(model, layoutView, view);

            // de kamers nummeren (moet gebeuren nadat kamers en grid geplaatst zijn)
            layoutView.nummerDeKamers();

            // koppel de gegenereerde views
            view.setLayoutView(layoutView.getHotelPanel());
            view.setLegendaView();
            view.setRightView(eventPrint.getContainer());

            layoutView.getHotelPanel().revalidate();
            layoutView.getHotelPanel().repaint();

            // notify de listeners dat de layout succesvol is geladen
            for (LayoutGeladen listener : listeners) {
                listener.onLayoutGeladen(controller);
            }

            return model;

        } catch (Exception ex) {
            ex.printStackTrace();
            showMessage("Fout bij laden: " + ex.getMessage());
            return null;
        }
    }

    // kiest wat voor type kamer er moet worden gemaakt en kiest de juiste factory daarvoor, maakt een nieuw ruimtemodel aan met de opgehaalde data uit de json
    private void maakKamer(RuimteData data) {
        RuimteFactory factory;

        switch (data.areaType) {
            case "Room":
                factory = new KamerCreator();
                break;
            case "Restaurant":
                factory = new RestaurantCreator();
                break;
            case "Fitness":
                factory = new FitnessCreator();
                break;
            case "Cinema":
                factory = new BioscoopCreator();
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
        model.addRuimte(ruimte);

        // notify de listeners dat er een nieuwe kamer is aangemaakt als het type ruimte ROOM is
        if (ruimte.getAreaType().equals(KamerType.ROOM) && newRoomListener != null) {
            newRoomListener.onNewKamer(ruimte);
        }
    }

    // set een nieuwe listener voor als er een kamer wordt aangemaakt
    public void setNewRoomListener(NewKamer listener) {
        this.newRoomListener = listener;
    }

    public LayoutController getController() {
        return controller;
    }

    // set een nieuwe listener als de layout is geladen
    public void setNewLayoutListener(LayoutGeladen listener) {
        listeners.add(listener);
    }

    protected File getFileFromPicker() {
        return new FilePicker().kiesBestand();
    }

    protected void showMessage(String message) {
        JOptionPane.showMessageDialog(view, message);
    }
}