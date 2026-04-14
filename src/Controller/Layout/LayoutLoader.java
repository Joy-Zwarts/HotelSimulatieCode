package Controller.Layout;

import Controller.KamerManagement.NewGuest;
import Controller.KamerManagement.NewRoom;
import Controller.RuimteFactory.*;
import Controller.Systeem.FilePicker;
import Controller.Systeem.PauseController;
import Model.Layout.LayoutModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteData;
import Model.Ruimtes.RuimteModel;
import View.Layout.LayoutView;
import View.Systeem.EventPanel;
import View.Systeem.HotelSimulatieView;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class LayoutLoader {

    private final HotelEventManager manager;
    private final HotelSimulatieView view;
    private LayoutModel model;
    private final PauseController pauseController;
    private final HotelSimulatieView simulatieView;
    private NewRoom newRoomListener;

    public LayoutLoader(HotelEventManager manager,
                        HotelSimulatieView view,
                        LayoutModel model,
                        PauseController pauseController,
                        HotelSimulatieView simulatieView) {

        this.manager = manager;
        this.view = view;
        this.model = model;
        this.pauseController = pauseController;
        this.simulatieView = simulatieView;
    }

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

            new LayoutController(model, layoutView);

            view.setLayoutView(layoutView.getHotelPanel());
            view.setLegendaView();
            view.setRightView(eventPrint.getPanelRechts());

            return model;

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Fout bij laden: " + ex.getMessage());
            return null;
        }
    }

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

        RuimteModel ruimte = factory.createRuimte(
                data.position,
                data.dimension,
                data.capacity,
                data.classification
        );

        model.addKamer(ruimte);

        if (ruimte.getAreaType().equals(KamerType.ROOM) && newRoomListener != null) {
            newRoomListener.onNewRoom(ruimte);
        }
    }

    public void setNewRoomListener(NewRoom listener) {
        this.newRoomListener = listener;
    }
}