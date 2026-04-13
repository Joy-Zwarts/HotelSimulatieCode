package Controller;

import Model.FilePickerFilter;

import javax.swing.*;
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

            // gebruik je filter methode
            File gefilterd = FilePickerFilter.filterJsonFile(file);

            if (gefilterd == null) {
                System.out.println("Kies een ander bestand.");
                return null;
            }
            System.out.println("Gekozen bestand: " + gefilterd.getAbsolutePath());
            return gefilterd;

        } else {
            System.out.println("Geen bestand gekozen.");
            return null;
        }
    }
}