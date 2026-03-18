import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFrame;
import org.json.simple.parser.ParseException;

//hoi

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        new Scanner(System.in);
        LayoutParser parser = new LayoutParser();
        Layout layoutFixed = parser.parse("Layout1Fixed.json");
        layoutFixed.berekenGridGrootte();
        layoutFixed.addverplichteElementen(layoutFixed);
        layoutFixed.maakGrid();
        layoutFixed.plaatsKamers();
        JFrame frame1 = new JFrame("Hotel Layout");
        frame1.setDefaultCloseOperation(3);
        frame1.add(layoutFixed.getHotelPanel());
        frame1.pack();
        frame1.setVisible(true);
    }
}
