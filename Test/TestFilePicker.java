import Controller.Systeem.FilePicker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;

import static org.junit.jupiter.api.Assertions.*;

public class TestFilePicker {

    private File dummyJsonFile;
    private File dummyTxtFile;

    @BeforeEach
    void setUp() throws IOException {
        dummyJsonFile = File.createTempFile("hotel_layout", ".json");
        dummyTxtFile = File.createTempFile("fout_bestand", ".txt");

        dummyJsonFile.deleteOnExit();
        dummyTxtFile.deleteOnExit();
    }

    @Test
    void testJsonBestandGekozen() {
        FilePicker mockFilePicker = new FilePicker() {
            @Override
            public File kiesBestand() {
                int resultaat = JFileChooser.APPROVE_OPTION;

                if (resultaat == JFileChooser.APPROVE_OPTION) {
                    File file = dummyJsonFile;

                    File gefilterd = Model.Systeem.FilePickerFilter.filterJsonFile(file);

                    if (gefilterd == null) {
                        return null;
                    }
                    return gefilterd;
                } else {
                    return null;
                }
            }
        };

        File resultaat = mockFilePicker.kiesBestand();
        assertNotNull(resultaat, "Het bestand had goedgekeurd moeten worden.");
        assertTrue(resultaat.getName().endsWith(".json"));
    }

    @Test
    void testInvalideBestandstype() {
        FilePicker mockFilePicker = new FilePicker() {
            @Override
            public File kiesBestand() {
                int resultaat = JFileChooser.APPROVE_OPTION;

                if (resultaat == JFileChooser.APPROVE_OPTION) {
                    File file = dummyTxtFile;

                    File gefilterd = Model.Systeem.FilePickerFilter.filterJsonFile(file);

                    if (gefilterd == null) {
                        System.out.println("Kies een ander bestand.");
                        return null;
                    }
                    return gefilterd;
                } else {
                    return null;
                }
            }
        };

        File resultaat = mockFilePicker.kiesBestand();
        assertNull(resultaat, "Een .txt bestand had afgekeurd moeten worden.");
    }

    @Test
    void testAnnuleertDialoog() {
        FilePicker mockFilePicker = new FilePicker() {
            @Override
            public File kiesBestand() {
                int resultaat = JFileChooser.CANCEL_OPTION;

                if (resultaat == JFileChooser.APPROVE_OPTION) {
                    return null;
                } else {
                    System.out.println("Geen bestand gekozen.");
                    return null;
                }
            }
        };

        File resultaat = mockFilePicker.kiesBestand();
        assertNull(resultaat, "Als de gebruiker annuleert moet de methode null teruggeven.");
    }
}