package Controller;

import javax.swing.JFileChooser;
import java.io.File;

public class FilePicker {
    public File kiesBestand() {
        //maak een filepicker aan
        JFileChooser chooser = new JFileChooser();

        //controleer het resultaat
        int resultaat = chooser.showOpenDialog(null);

        //kijk of er een bestand is gekozen
        if (resultaat == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            //filter op non json files
            if ( !file.getName().toLowerCase().endsWith(".json")) {
            System.out.println("kies een ander bestand.");
            return null;
            }
            System.out.println("Gekozen bestand: " + file.getAbsolutePath());
            return file;
        } else {
            System.out.println("Geen bestand gekozen.");
            return null;
        }
    }
}