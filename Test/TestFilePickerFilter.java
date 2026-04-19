import Model.Systeem.FilePickerFilter;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class TestFilePickerFilter {

    @Test
    void accepteertJsonBestand() {
        File jsonFile = new File("testbestand.json");

        File resultaat = FilePickerFilter.filterJsonFile(jsonFile);

        assertNotNull(resultaat);
        assertEquals(jsonFile, resultaat);
    }

    @Test
    void weigertNietJsonBestand() {
        File txtFile = new File("testbestand.txt");

        File resultaat = FilePickerFilter.filterJsonFile(txtFile);

        assertNull(resultaat);
    }

    @Test
    void accepteertJsonMetHoofdletters() {
        File jsonFile = new File("testbestand.JSON");

        File resultaat = FilePickerFilter.filterJsonFile(jsonFile);

        assertNotNull(resultaat);
    }

    @Test
    void nullInputGeeftNull() {
        File resultaat = FilePickerFilter.filterJsonFile(null);

        assertNull(resultaat);
    }
}