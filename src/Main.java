import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);

        // lees JSON en krijg layout
        LayoutParser parser = new LayoutParser();
        Layout layoutFixed = parser.parse("Layout1Fixed.json");

        // bereken hotel grootte
        layoutFixed.berekenGridGrootte();

        // voeg de lift, schachten, trappen en lobby toe
        layoutFixed.addverplichteElementen(layoutFixed);

        // maak grid
        layoutFixed.maakGrid();

        // plaats kamers
        layoutFixed.plaatsKamers();

        HotelEventManager manager = new HotelEventManager(1000); // maak een nieuwe hotelmanager aan

        EventPrint printEvent = new EventPrint(manager);


        JFrame frame1 = new JFrame("Hotel Layout"); // maak een nieuw scherm aan om de simulatie te tonen
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // als je het venster sluit stopt het programma
        frame1.add(layoutFixed.getHotelPanel(),BorderLayout.CENTER); // get het panel van de layout van het hotel en voeg deze toe aan het scherm
        frame1.add(printEvent.getEventPanel(), BorderLayout.EAST);
        frame1.setSize(1500, 800);
        frame1.setVisible(true); // laat het zien
        frame1.setLocationRelativeTo(null);



        EventPrint printerScherm = new EventPrint(manager); // maak een nieuw scherm aan om de events te printen

        manager.startTimer(); // startTimer de klok
    }
}