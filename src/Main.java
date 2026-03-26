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

        // file chooser
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result != JFileChooser.APPROVE_OPTION) {
            System.out.println("Geen bestand geselecteerd");
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();
        System.out.println("Geselecteerd bestand: " + selectedFile.getAbsolutePath());


        // parser → model vullen
        LayoutParser parser = new LayoutParser();
        LayoutModel model = parser.parse(selectedFile.getAbsolutePath());

        // view maken
        LayoutView view = new LayoutView();

        // event systeem
        HotelEventManager manager = new HotelEventManager(1000);
        EventPrint printEvent = new EventPrint(manager);

        // controller (doet alle layout logica)
        LayoutController controller = new LayoutController(model, view);

        // jframe bouwen
        JFrame frame = new JFrame("Hotel Layout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // layout + events toevoegen
        frame.add(view.getHotelPanel(), BorderLayout.CENTER);
        frame.add(printEvent.getPanelRechts(), BorderLayout.EAST);


        frame.setSize(1500, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // event systeem starten
        manager.startTimer();
    }
}