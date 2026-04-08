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

    HotelEventManager manager;
    HotelSimulatieView view;
    LayoutModel model;

    // constructor
    public LayoutLoader(HotelEventManager manager, HotelSimulatieView view, LayoutModel model) {
        this.manager = manager;
        this.view = view;
        this.model = model;
    }

    // load de layout (basically wat eerst in main stond)
    public LayoutModel loadLayout() {
        try {
            FilePicker picker = new FilePicker(); // kies file
            File gekozenBestand = picker.kiesBestand();

            LayoutParser parser = new LayoutParser();
            this.model = parser.parse(gekozenBestand.getAbsolutePath()); // parse de gekozen file
            LayoutModel model = this.model;

            LayoutView layoutView = new LayoutView(manager);
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
