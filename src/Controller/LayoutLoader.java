package Controller;

import Model.LayoutModel;
import View.EventPanel;
import View.HotelSimulatieView;
import View.LayoutView;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.io.File;

public class LayoutLoader {

    // attributen
    private final HotelEventManager manager;
    private final HotelSimulatieView view;
    private LayoutModel model;
    private final PauseController pauseController;
    private HotelSimulatieView simulatieView;

    // constructor
    public LayoutLoader(HotelEventManager manager, HotelSimulatieView view, LayoutModel model, PauseController pauseController, HotelSimulatieView SimulatieView) {
        this.manager = manager;
        this.view = view;
        this.model = model;
        this.pauseController = pauseController;
        this.simulatieView = SimulatieView;
    }

    // load de layout (basically wat eerst in main stond)
    public LayoutModel loadLayout() {
        try {
            FilePicker picker = new FilePicker(); // kies file
            File gekozenBestand = picker.kiesBestand();

            LayoutParser parser = new LayoutParser();
            this.model = parser.parse(gekozenBestand.getAbsolutePath()); // parse de gekozen file

            LayoutView layoutView = new LayoutView(manager, pauseController, simulatieView);
            EventPanel eventPrint = new EventPanel(manager);

            new LayoutController(model, layoutView);

            view.setLayoutView(layoutView.getHotelPanel());
            view.setLegendaView(view.getLegendaPanel());
            view.setRightView(eventPrint.getPanelRechts());

            return model; // return het gemaakte layout model

        } catch (Exception ex) { // failsafe
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Fout bij laden: " + ex.getMessage());
            return null;
        }
    }
}
