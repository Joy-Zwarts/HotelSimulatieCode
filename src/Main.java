import Controller.HotelEventManager;
import Controller.LayoutController;
import Controller.LayoutParser;
import Model.LayoutModel;
import View.*;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
            // start opstartScherm met callback
            new OpstartScherm(file -> {
                try {
                    // parser & model
                    LayoutParser parser = new LayoutParser();
                    LayoutModel model = parser.parse(file.getAbsolutePath());

                    // views
                    LayoutView view = new LayoutView();
                    LegendaView legendaView = new LegendaView();

                    // event systeem
                    HotelEventManager manager = new HotelEventManager(1000);
                    EventPrint printEvent = new EventPrint(manager);
                    OverzichtScherm scherm = new OverzichtScherm();

                    // controller
                    LayoutController controller = new LayoutController(model, view);

                    // JFrame
                    JFrame frame = new JFrame("Hotel Layout");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLayout(new java.awt.BorderLayout());

                    JPanel hotelPanel = new JPanel(new java.awt.BorderLayout());
                    hotelPanel.add(view.getHotelPanel(), java.awt.BorderLayout.CENTER);
                    hotelPanel.add(legendaView.getLegendaPanel(), java.awt.BorderLayout.SOUTH);
                    frame.add(hotelPanel, java.awt.BorderLayout.CENTER);
                    frame.add(printEvent.getPanelRechts(), java.awt.BorderLayout.EAST);

                    frame.setSize(1500, 800);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);

                    // start events
                    manager.startTimer();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Fout bij het laden van het bestand: " + ex.getMessage());
                }
            });
    }
}