import Controller.HotelEventManager;
import Controller.LayoutController;
import Controller.LayoutParser;
import Model.LayoutModel;
import View.EventPrint;
import View.LayoutView;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {

        // 1. File chooser
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result != JFileChooser.APPROVE_OPTION) {
            System.out.println("Geen bestand geselecteerd");
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();
        System.out.println("Geselecteerd bestand: " + selectedFile.getAbsolutePath());

        // 2. Parser → Model vullen
        LayoutParser parser = new LayoutParser();
        LayoutModel model = parser.parse(selectedFile.getAbsolutePath());

        // 3. View maken
        LayoutView view = new LayoutView();

        // 4. Event systeem
        HotelEventManager manager = new HotelEventManager(1000);
        EventPrint printEvent = new EventPrint(manager);

        // 5. Controller (doet alle layout logica)
        LayoutController controller = new LayoutController(model, view);

        // 6. JFrame bouwen
        JFrame frame = new JFrame("Hotel Layout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Layout + events toevoegen
        frame.add(view.getHotelPanel(), BorderLayout.CENTER);
        frame.add(printEvent.getEventPanel(), BorderLayout.EAST);

        frame.setSize(1500, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // 7. Event systeem starten
        manager.startTimer();
    }
}