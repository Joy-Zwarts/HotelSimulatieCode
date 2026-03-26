import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;

import org.json.simple.parser.ParseException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        new Scanner(System.in);
        LayoutParser parser = new LayoutParser();
        Layout layoutFixed = parser.parse("Layout1Fixed.json");
        layoutFixed.berekenGridGrootte();
        layoutFixed.addverplichteElementen(layoutFixed);
        layoutFixed.maakGrid();
        layoutFixed.plaatsKamers();
        HotelEventManager manager = new HotelEventManager(1000);
        EventPrint printEvent = new EventPrint(manager);
        JFrame frame1 = new JFrame("Hotel Layout");
        // Maak de file chooser aan
        JFileChooser fileChooser = new JFileChooser();

        // Open de file chooser
        int result = fileChooser.showOpenDialog(frame1);

        // Controleer of de gebruiker een bestand heeft geselecteerd
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Geselecteerd bestand: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("Geen bestand geselecteerd");
        }
        frame1.setDefaultCloseOperation(3);
        frame1.add(layoutFixed.getHotelPanel(), "Center");
        frame1.add(printEvent.getEventPanel(), "East");
        frame1.setSize(1500, 800);
        frame1.setVisible(true);
        frame1.setLocationRelativeTo((Component)null);
        new EventPrint(manager);
        manager.startTimer();
    }
}