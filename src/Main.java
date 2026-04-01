import Controller.LayoutController;
import Controller.LayoutParser;
import Model.LayoutModel;
import View.*;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {

        // Startscherm voor het selecteren van layout-bestand
        new OpstartScherm(file -> {
            try {
                // 1️⃣ Layout inladen
                LayoutParser parser = new LayoutParser();
                LayoutModel model = parser.parse(file.getAbsolutePath());

                LayoutView view = new LayoutView();
                LegendaView legendaView = new LegendaView();

                // 2️⃣ HotelEventManager (externe library)
                // false = lokaal scenario-bestand gebruiken
                HotelEventManager manager = new HotelEventManager(false);

                // 3️⃣ Event listener koppelen (GUI update)
                EventPrint printEvent = new EventPrint(manager);

                // 4️⃣ Controller
                LayoutController controller = new LayoutController(model, view);

                // 5️⃣ GUI bouwen
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

                manager.start(1);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Fout bij laden: " + ex.getMessage());
            }
        });
    }
}